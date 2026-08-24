package twilightforest.events;

import carminite.network.PacketDistributor;
import carminite.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFGameRules;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.EnforceProgressionStatusPacket;
import twilightforest.tags.TFBlockTags;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.world.components.structures.util.ProgressionPiece;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.*;

/**
 * A class to store events relating to progression
 */
public class ProgressionEvents {
	public static final ProgressionEvents INSTANCE = new ProgressionEvents();

	private void init() {
		GameRuleEvents.changeCallback(TFGameRules.ENFORCED_PROGRESSION_RULE).register(INSTANCE::gameRuleChanged);
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, _, _) -> INSTANCE.preventLockedAreaBlockBreaking(level, player, pos));
		UseBlockCallback.EVENT.register((player, level, _, hitResult) -> INSTANCE.preventLockedAreaBlockPlacing(player,level, hitResult));
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> INSTANCE.preventLockedAreaEntityDamage(entity, source));
		ServerPlayerEvents.JOIN.register(INSTANCE::syncProgressionGameRuleStatus);
	}

	/**
	 * Notify all players' clients of gamerule change if progression is the change.
	 */
	private void gameRuleChanged(Boolean progressionEnforced, MinecraftServer server) {
		PacketDistributor.sendToAllPlayers(new EnforceProgressionStatusPacket(progressionEnforced));
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 */
	private boolean preventLockedAreaBlockBreaking(Level level, Player player, BlockPos pos) {
		if (!(level instanceof ServerLevel serverLevel)) return true;

		return !isBlockProtectedFromBreaking(level, pos) || !isAreaProtected(serverLevel, player, pos);
	}

	/**
	 * Check if the player is trying to place a block in a structure that's considered inaccessible for progression reasons
	 */
	private InteractionResult preventLockedAreaBlockPlacing(Player player, Level level, BlockHitResult hitResult) {
		if (player.isSpectator() || !(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

		BlockPos pos = hitResult.getBlockPos();
		if (isAreaProtected(serverLevel, player, pos)) {
			player.inventoryMenu.sendAllDataToRemote();
			return InteractionResult.FAIL;
		}

		return InteractionResult.PASS;
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(ServerLevel level, Player player, BlockPos pos) {
		if (player.getAbilities().instabuild || player.isSpectator() ||
			!LandmarkUtil.isProgressionEnforced(level) || player instanceof FakePlayer) {
			return false;
		}

		Optional<StructureStart> struct = LandmarkUtil.locateNearestLandmarkStart(level, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
		if (struct.isPresent()) {
			StructureStart structureStart = struct.get();
			if (structureStart.getPieces().stream().anyMatch(structurePiece -> structurePiece.getBoundingBox().isInside(pos) && isPieceProtected(structurePiece)) && structureStart.getStructure() instanceof ProgressionStructure structureHints) {
				if (!structureHints.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {
					// send protection packet
					List<BoundingBox> boxes = new ArrayList<>();
					structureStart.getPieces().forEach(piece -> {
						if (piece.getBoundingBox().isInside(pos))
							boxes.add(piece.getBoundingBox());
					});

					sendAreaProtectionPacket(level, pos, boxes);

					// send a hint monster?
					structureHints.trySpawnHintMonster(level, player, pos);

					return true;
				}
			}
		}
		return false;
	}

	//TODO make ignored entities into a tag
	private boolean preventLockedAreaEntityDamage(LivingEntity entity, DamageSource source) {
		// cancel attacks in protected areas
		if (entity.level() instanceof ServerLevel serverLevel && entity instanceof Enemy && source.getEntity() instanceof Player && !(entity instanceof Kobold)
			&& isAreaProtected(serverLevel, (Player) source.getEntity(), new BlockPos(entity.blockPosition()))) {

			return false;
		}

		return true;
	}

	private static boolean isPieceProtected(StructurePiece piece) {
		return !(piece instanceof ProgressionPiece progressionPiece) || progressionPiece.isComponentProtected();
	}

	private static boolean isBlockProtectedFromBreaking(BlockGetter level, BlockPos pos) {
		return !level.getBlockState(pos).is(TFBlockTags.PROGRESSION_ALLOW_BREAKING);
	}

	private static void sendAreaProtectionPacket(ServerLevel level, BlockPos pos, List<BoundingBox> sbb) {
		PacketDistributor.sendToPlayersNear(level, null, pos.getX(), pos.getY(), pos.getZ(), 64, new AreaProtectionPacket(sbb, pos));
	}

	/**
	 * TFWeatherRenderer.progressionEnforced defaults to true on the client, so it's up to the server (via this listener) to notify true status when the player logs in
	 */
	private void syncProgressionGameRuleStatus(ServerPlayer player) {
        boolean progressionEnforced = ServerLifecycleHooks.getCurrentServer().getGameRules().get(TFGameRules.ENFORCED_PROGRESSION_RULE);
		PacketDistributor.sendToPlayer(player, new EnforceProgressionStatusPacket(progressionEnforced));
	}
}
