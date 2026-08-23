package twilightforest.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelData;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

public class CapabilityEvents {
	public static final CapabilityEvents INSTANCE = new CapabilityEvents();

	public static void init() {
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, _) -> INSTANCE.spawnInTFIfNecessary(newPlayer));
	}

	private void spawnInTFIfNecessary(ServerPlayer newPlayer) {
		if (newPlayer.getRespawnConfig() == null) {
			newSpawnInTwilightForest(newPlayer);
		}
	}

	public static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.level().getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.teleport(TFConfig.portalForNewPlayerSpawn ? TFTeleporter.createTransition(player, level, newDefaultSpawn, true) : NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		ServerPlayer.RespawnConfig newSpawn = new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), player.getXRot()), true);
		player.setRespawnPosition(newSpawn, false);

		player.setAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}
}
