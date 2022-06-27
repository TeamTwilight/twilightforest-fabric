package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFLandmark;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.EnforceProgressionStatusPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class to store events relating to progression
 */
public class ProgressionEvents {

	public static void init() {
		LivingEntityEvents.ATTACK.register(ProgressionEvents::livingAttack);
		UseBlockCallback.EVENT.register(ProgressionEvents::onPlayerRightClick);
		PlayerBlockBreakEvents.BEFORE.register(ProgressionEvents::breakBlock);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(ProgressionEvents::playerPortals);
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 */
	public static boolean breakBlock(Level level, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity blockEntity) {
		if (level.isClientSide()) return true;

		if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
			return false;

		}
		return true;
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	public static InteractionResult onPlayerRightClick(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		if (!level.isClientSide() && isBlockProtectedFromInteraction(level, hitResult.getBlockPos()) && isAreaProtected(level, player, hitResult.getBlockPos())) {
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

	private static boolean isBlockProtectedFromInteraction(Level level, BlockPos pos) {
		return level.getBlockState(pos).is(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(Level level, BlockPos pos) {
		return !level.getBlockState(pos).is(BlockTagGenerator.PROGRESSION_ALLOW_BREAKING);
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(Level level, Player player, BlockPos pos) {

		if (player.getAbilities().instabuild || player.isSpectator() ||
				!TFGenerationSettings.isProgressionEnforced(level) || (player instanceof ServerPlayer && player.getClass() != ServerPlayer.class)) {
			return false;
		}

		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(level);

		if (chunkGenerator != null) {
			Optional<StructureStart> struct = TFGenerationSettings.locateTFStructureInRange((ServerLevel) level, pos, 0);
			if (struct.isPresent()) {
				StructureStart structure = struct.get();
				if (structure.getBoundingBox().isInside(pos)) {
					// what feature is nearby?  is it one the player has not unlocked?
					TFLandmark nearbyFeature = TFLandmark.getFeatureAt(pos.getX(), pos.getZ(), (ServerLevel) level);

					if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {

						// TODO: This is terrible but *works* for now.. proper solution is to figure out why the stronghold bounding box is going so high
						if (nearbyFeature == TFLandmark.KNIGHT_STRONGHOLD && pos.getY() >= TFGenerationSettings.SEALEVEL)
							return false;

						// send protection packet
						List<BoundingBox> boxes = new ArrayList<>();
						structure.getPieces().forEach(piece -> {
							if (piece.getBoundingBox().isInside(pos))
								boxes.add(piece.getBoundingBox());
						});

						sendAreaProtectionPacket(level, pos, boxes);

						// send a hint monster?
						nearbyFeature.trySpawnHintMonster(level, player, pos);

						return true;
					}
				}
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(Level level, BlockPos pos, List<BoundingBox> sbb) {
		TFPacketHandler.CHANNEL.sendToClientsAround(new AreaProtectionPacket(sbb, pos), (ServerLevel) level, pos, 64);
	}

	public static boolean livingAttack(LivingEntity living, DamageSource source, float amount) {
		// cancel attacks in protected areas
		if (!living.getLevel().isClientSide() && living instanceof Enemy && source.getEntity() instanceof Player && !(living instanceof Kobold)
				&& isAreaProtected(living.getLevel(), (Player) source.getEntity(), new BlockPos(living.blockPosition()))) {

			return true;
		}
		return false;
	}

	public static void playerPortals(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
		if (!player.getLevel().isClientSide()) {
			if (TFGenerationSettings.usesTwilightChunkGenerator(player.getLevel())) {
				sendEnforcedProgressionStatus(player, TFGenerationSettings.isProgressionEnforced(player.getLevel()));
			}
		}
	}

	public static void playerLogsIn(ServerPlayer player) {
		if (!player.getLevel().isClientSide()) {
			sendEnforcedProgressionStatus(player, TFGenerationSettings.isProgressionEnforced(player.getLevel()));
		}
	}

	private static void sendEnforcedProgressionStatus(ServerPlayer player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.sendToClient(new EnforceProgressionStatusPacket(isEnforced), player);
	}
}
