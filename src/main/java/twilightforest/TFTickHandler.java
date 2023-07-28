package twilightforest;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerTickEvents;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFPortalBlock;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;
import twilightforest.init.custom.Enforcement;
import twilightforest.item.BrittleFlaskItem;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.StructureProtectionClearPacket;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.PlayerHelper;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TFTickHandler {
	public static void init() {
		PlayerTickEvents.END.register(TFTickHandler::playerTick);
	}

	public static void playerTick(Player eventPlayer) {
		if (!(eventPlayer instanceof ServerPlayer player)) return;
		if (!(player.level() instanceof ServerLevel world)) return;

		// check for portal creation, at least if it's not disabled
		if (!TFConfig.COMMON_CONFIG.disablePortalCreation.get() && player.tickCount % (TFConfig.COMMON_CONFIG.checkPortalDestination.get() ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (TFConfig.COMMON_CONFIG.adminOnlyPortals.get()) {
				if (world.getServer().getProfilePermissions(player.getGameProfile()) != 0) {
					// reduce range to 4.0 when the option is on
					checkForPortalCreation(player, world, 4.0F);
				}
			} else {
				// normal check, no special options
				checkForPortalCreation(player, world, 32.0F);
			}
		}

		//tick every second for the advancement trigger bit of the flask
		if(player.tickCount % 20 == 0) {
			BrittleFlaskItem.ticker();
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (player.tickCount % 20 == 0 && LandmarkUtil.isProgressionEnforced(world) && TFGenerationSettings.usesTwilightChunkGenerator(world) && !player.isCreative() && !player.isSpectator()) {
			Enforcement.enforceBiomeProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (player.tickCount % 100 == 0 && LandmarkUtil.isProgressionEnforced(world)) {
			if (TFGenerationSettings.usesTwilightChunkGenerator(world)) {
				if (player.isCreative() || player.isSpectator()) {
					sendAllClearPacket(player);
				} else {
					checkForLockedStructuresSendPacket(player, world);
				}
			}
		}
	}

	private static void sendStructureProtectionPacket(Player player, BoundingBox sbb) {
		if (player instanceof ServerPlayer serverPlayer) {
			TFPacketHandler.CHANNEL.sendToClient(new StructureProtectionPacket(sbb), serverPlayer);
		}
	}

	private static void sendAllClearPacket(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			TFPacketHandler.CHANNEL.sendToClient(new StructureProtectionClearPacket(), serverPlayer);
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, ServerLevel world) {
		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(world);
		if (chunkGenerator == null)
			return false;

		ChunkPos chunkPlayer = player.chunkPosition();
		return LandmarkUtil.locateNearestLandmarkStart(world, chunkPlayer.x, chunkPlayer.z).map(structureStart -> {
			if (structureStart.getStructure() instanceof AdvancementLockedStructure advancementLockedStructure && !advancementLockedStructure.doesPlayerHaveRequiredAdvancements(player)) {
				//FIXME this is a gross hack. For some reason the stronghold locked effect doesnt properly lock to the structure after 1.19.2 and I have no idea why.
				// I really dont feel like looking into this right now, someone else can if they feel so inclined.
				if (structureStart.getStructure().equals(world.registryAccess().registryOrThrow(Registries.STRUCTURE).get(TFStructures.KNIGHT_STRONGHOLD)) && player.blockPosition().getY() > TFGenerationSettings.SEALEVEL) {
					return false;
				}
				sendStructureProtectionPacket(player, structureStart.getBoundingBox());
				return true;
			}

			sendAllClearPacket(player);
			return false;
		}).orElse(false);
	}

	private static void checkForPortalCreation(ServerPlayer player, Level world, float rangeToCheck) {
		if (world.dimension().location().equals(new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()))
				|| TFGenerationSettings.isTwilightPortalDestination(world)
				|| TFConfig.COMMON_CONFIG.allowPortalsInOtherDimensions.get()) {

			List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
			ItemEntity qualified = null;

			for (ItemEntity entityItem : itemList) {
				if (entityItem.getItem().is(ItemTagGenerator.PORTAL_ACTIVATOR) &&
						TFBlocks.TWILIGHT_PORTAL.get().canFormPortal(world.getBlockState(entityItem.blockPosition())) &&
						Objects.equals(entityItem.getOwner(), player)) {

					qualified = entityItem;
					break;
				}
			}

			if (qualified == null) return;

			if (!player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
				Advancement requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));
				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.displayClientMessage(TFPortalBlock.PORTAL_UNWORTHY, true);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.getDisplay();
						TFPacketHandler.CHANNEL.sendToClient(info == null ? new MissingAdvancementToastPacket(Component.translatable(".ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()), player);

						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return; // Item qualifies, but the player doesn't
				}
			}

			Random rand = new Random();
			for (int i = 0; i < 2; i++) {
				double vx = rand.nextGaussian() * 0.02D;
				double vy = rand.nextGaussian() * 0.02D;
				double vz = rand.nextGaussian() * 0.02D;

				world.addParticle(ParticleTypes.EFFECT, qualified.getX(), qualified.getY() + 0.2, qualified.getZ(), vx, vy, vz);
			}

			if (TFBlocks.TWILIGHT_PORTAL.get().tryToCreatePortal(world, qualified.blockPosition(), qualified, player))
				TFAdvancements.MADE_TF_PORTAL.trigger(player);

		}
	}

}
