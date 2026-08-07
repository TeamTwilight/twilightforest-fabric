package twilightforest.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;

import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

public class CapabilityEvents {

	public static final CapabilityEvents INSTANCE = new CapabilityEvents();

	public static void init() {
		EntityTickEvent.Post.EVENT.register(INSTANCE::updateShields);
		PlayerTickEvent.Post.EVENT.register(INSTANCE::updatePlayerCaps);
		PlayerEvents.PlayerLoggedInEvent.EVENT.register(INSTANCE::playerLogsIn);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if (newPlayer.getRespawnPosition() == null) {
				newSpawnInTwilightForest(newPlayer);
			}
		});
	}

	private void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getAttachedOrCreate(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true);
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();
			if (event.getEntity().hasAttached(TFDataAttachments.FEATHER_FAN)) {
				if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
					event.getEntity().setAttached(TFDataAttachments.FEATHER_FAN, false);
				}
			}
		}
		event.getEntity().getAttachedOrCreate(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		event.getEntity().getAttachedOrCreate(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	private void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public void playerLogsIn(PlayerEvents.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		dataFixLegacyBanish(player);
		if (!player.hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			newSpawnInTwilightForest(player);
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.changeDimension(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), true, false);

		player.setAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		// Legacy data migration from old NeoForge persistent data system
		/*
		CompoundTag tagCompound = player.getPersistentData();
		if (!tagCompound.contains(Player.PERSISTED_NBT_TAG))
			return;
		CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put(Player.PERSISTED_NBT_TAG, playerData);

		if (player.hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			return;

		player.setAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
		*/
	}
}
