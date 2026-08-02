package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import twilightforest.components.entity.FortificationShieldAttachment;
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
		LivingHurtEvent.EVENT.register(INSTANCE::absorbShieldHits);
		// PlayerRespawnEvent needs migration to Fabric API ServerPlayerEvents.AFTER_RESPAWN
		PlayerEvents.PlayerLoggedInEvent.EVENT.register(INSTANCE::playerLogsIn);
	}

		private void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
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

	private void absorbShieldHits(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = living.getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS);
			if (attachment.shieldsLeft() > 0) {
				if (living.invulnerableTime <= 0) {
					attachment.breakShield(living, false);
					FortificationShieldAttachment.addShieldBreakParticles(event.getSource(), living);
					living.invulnerableTime = living.invulnerableDuration;
				}
				event.setCanceled(true);
			}
		}
	}
	// PlayerEvents.CloneEvent needs migration to Fabric API
	/*
	private void spawnInTFIfNecessary(PlayerEvents.CloneEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

		if (serverPlayer.getRespawnPosition() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}
	*/

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
		// getPersistentData() 和 Player.PERSISTED_NBT_TAG 在 1.21.1 中不再可用，使用 DataAttachment 替代
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
