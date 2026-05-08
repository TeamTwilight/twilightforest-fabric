package twilightforest.events;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.block.TFPortalBlock;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimension;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.util.Enforcement;
import twilightforest.util.PlayerHelper;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ProgressionPiece;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public final class ProgressionEvents {
	private static boolean bootstrapped;

	private ProgressionEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
			if (level instanceof ServerLevel serverLevel && isBlockProtectedFromBreaking(serverLevel, pos) && isAreaProtected(serverLevel, player, pos)) {
				return false;
			}
			return true;
		});

		AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
			if (level instanceof ServerLevel serverLevel && isBlockProtectedFromBreaking(serverLevel, pos) && isAreaProtected(serverLevel, player, pos)) {
				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});

		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (!(level instanceof ServerLevel serverLevel)) {
				return InteractionResult.PASS;
			}

			BlockPos clicked = hitResult.getBlockPos();
			if (isBlockProtectedFromInteraction(level, clicked) && isAreaProtected(serverLevel, player, clicked)) {
				player.inventoryMenu.sendAllDataToRemote();
				return InteractionResult.FAIL;
			}

			if (player.getItemInHand(hand).getItem() instanceof BlockItem) {
				BlockPos placePos = clicked.relative(hitResult.getDirection());
				if (isAreaProtected(serverLevel, player, placePos)) {
					player.inventoryMenu.sendAllDataToRemote();
					return InteractionResult.FAIL;
				}
			}

			return InteractionResult.PASS;
		});

		ServerLivingEntityEvents.ALLOW_DAMAGE.register(ProgressionEvents::preventLockedAreaEntityDamage);
		ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(ProgressionEvents::performProtectionAndPortalChecks));
	}

	private static boolean preventLockedAreaEntityDamage(LivingEntity living, net.minecraft.world.damagesource.DamageSource source, float amount) {
		if (living.level() instanceof ServerLevel serverLevel && living instanceof Enemy && source.getEntity() instanceof Player player && !(living instanceof Kobold)
			&& isAreaProtected(serverLevel, player, living.blockPosition())) {
			return false;
		}
		return true;
	}

	private static void performProtectionAndPortalChecks(ServerPlayer player) {
		ServerLevel world = player.serverLevel();

		if (!TFConfig.disablePortalCreation && player.tickCount % (!TFConfig.checkPortalPlacement ? 100 : 20) == 0
			&& world.getServer().getProfilePermissions(player.getGameProfile()) >= TFConfig.portalCreationPermission) {
			checkForPortalCreation(player, world, TFConfig.portalCreationPermission >= Commands.LEVEL_ADMINS ? 4.0F : 32.0F);
		}

		if (player.tickCount % 20 == 0 && LandmarkUtil.isProgressionEnforced(world) && !player.isCreative() && !player.isSpectator()) {
			Enforcement.enforceBiomeProgression(player, world);
		}

		if (player.tickCount % 100 == 0 && LandmarkUtil.isProgressionEnforced(world)) {
			if (player.isCreative() || player.isSpectator()) {
				sendAllClearPacket(player);
			} else {
				checkForLockedStructuresSendPacket(player, world);
			}
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, ServerLevel world) {
		ChunkPos chunkPlayer = player.chunkPosition();
		return LandmarkUtil.locateNearestLandmarkStart(world, chunkPlayer.x, chunkPlayer.z).map(structureStart -> {
			if (structureStart.getStructure() instanceof AdvancementLockedStructure advancementLockedStructure && !advancementLockedStructure.doesPlayerHaveRequiredAdvancements(player)) {
				List<Pair<BoundingBox, Boolean>> boundingBoxesData = structureStart.getPieces().stream()
					.map(piece -> Pair.of(isPieceProtected(piece) ? piece.getBoundingBox().inflatedBy(4) : piece.getBoundingBox(), isPieceProtected(piece)))
					.toList();

				sendStructureProtectionPacket(player, boundingBoxesData);
				return true;
			}

			sendAllClearPacket(player);
			return false;
		}).orElse(false);
	}

	private static void checkForPortalCreation(ServerPlayer player, Level world, float rangeToCheck) {
		if (!world.dimension().location().equals(ResourceLocation.parse(TFConfig.originDimension))
			&& !TFDimension.isTwilightPortalDestination(world)
			&& !TFConfig.allowPortalsInOtherDimensions) {
			return;
		}

		List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
		ItemEntity qualified = null;
		TFPortalBlock portalBlock = (TFPortalBlock) TFBlocks.TWILIGHT_PORTAL.get();

		for (ItemEntity entityItem : itemList) {
			if (entityItem.getItem().is(ItemTagGenerator.PORTAL_ACTIVATOR)
				&& portalBlock.canFormPortal(world.getBlockState(entityItem.blockPosition()))
				&& Objects.equals(entityItem.getOwner(), player.getUUID())) {
				qualified = entityItem;
				break;
			}
		}

		if (qualified == null) return;

		if (!player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
			AdvancementHolder requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));
			if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
				player.displayClientMessage(TFPortalBlock.PORTAL_UNWORTHY, true);

				if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
					DisplayInfo info = requirement.value().display().orElse(null);
					MissingAdvancementToastPacket packet = info == null
						? new MissingAdvancementToastPacket(net.minecraft.network.chat.Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get()))
						: new MissingAdvancementToastPacket(info.getTitle(), info.getIcon());
					if (ServerPlayNetworking.canSend(player, MissingAdvancementToastPacket.TYPE)) {
						ServerPlayNetworking.send(player, packet);
					}

					TFPortalBlock.playerNotifiedOfRequirement(player);
				}

				return;
			}
		}

		Random rand = new Random();
		if (world instanceof ServerLevel serverLevel) {
			for (int i = 0; i < 2; i++) {
				serverLevel.sendParticles(ParticleTypes.EFFECT, qualified.getX(), qualified.getY() + 0.2D, qualified.getZ(), 1, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, 0.0D);
			}
		}

		if (portalBlock.tryToCreatePortal(world, qualified.blockPosition(), qualified, player)) {
			TFAdvancements.MADE_TF_PORTAL.get().trigger(player);
		}
	}

	private static boolean isAreaProtected(ServerLevel level, Player player, BlockPos pos) {
		if (player.getAbilities().instabuild || player.isSpectator() || !LandmarkUtil.isProgressionEnforced(level)) {
			return false;
		}

		Optional<StructureStart> struct = LandmarkUtil.locateNearestLandmarkStart(level, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
		if (struct.isPresent()) {
			StructureStart structureStart = struct.get();
			if (structureStart.getPieces().stream().anyMatch(piece -> piece.getBoundingBox().isInside(pos) && isPieceProtected(piece))
				&& structureStart.getStructure() instanceof ProgressionStructure structureHints
				&& !structureHints.doesPlayerHaveRequiredAdvancements(player)) {
				List<BoundingBox> boxes = new ArrayList<>();
				structureStart.getPieces().forEach(piece -> {
					if (piece.getBoundingBox().isInside(pos)) {
						boxes.add(piece.getBoundingBox());
					}
				});

				sendAreaProtectionPacket(level, pos, boxes);
				structureHints.trySpawnHintMonster(level, player, pos);
				return true;
			}
		}

		return false;
	}

	private static boolean isPieceProtected(StructurePiece piece) {
		return !(piece instanceof ProgressionPiece progressionPiece) || progressionPiece.isComponentProtected();
	}

	private static boolean isBlockProtectedFromInteraction(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos).is(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(BlockGetter level, BlockPos pos) {
		return !level.getBlockState(pos).is(BlockTagGenerator.PROGRESSION_ALLOW_BREAKING);
	}

	private static void sendAreaProtectionPacket(ServerLevel level, BlockPos pos, List<BoundingBox> boxes) {
		AreaProtectionPacket packet = new AreaProtectionPacket(boxes, pos);
		for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new net.minecraft.world.phys.AABB(pos).inflate(64.0D))) {
			if (ServerPlayNetworking.canSend(player, AreaProtectionPacket.TYPE)) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	private static void sendStructureProtectionPacket(Player player, List<Pair<BoundingBox, Boolean>> boxes) {
		if (player instanceof ServerPlayer sp && ServerPlayNetworking.canSend(sp, StructureProtectionPacket.TYPE)) {
			ServerPlayNetworking.send(sp, new StructureProtectionPacket(Optional.of(boxes)));
		}
	}

	private static void sendAllClearPacket(Player player) {
		if (player instanceof ServerPlayer sp && ServerPlayNetworking.canSend(sp, StructureProtectionPacket.TYPE)) {
			ServerPlayNetworking.send(sp, new StructureProtectionPacket(Optional.empty()));
		}
	}
}
