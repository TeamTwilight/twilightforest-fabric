package twilightforest.events;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

public final class CapabilityEvents {
	private static boolean bootstrapped;
	private static final String PERSISTED_NBT_TAG = "PlayerPersisted";

	private CapabilityEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ServerTickEvents.END_WORLD_TICK.register(CapabilityEvents::updateShields);
		ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(CapabilityEvents::updatePlayerCaps));
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(CapabilityEvents::absorbShieldHits);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> playerLogsIn(handler.player));
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if (newPlayer.getRespawnPosition() == null) {
				newSpawnInTwilightForest(newPlayer);
			}
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (((AttachmentTarget) oldPlayer).hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST)) {
				TFDataAttachments.set(newPlayer, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
			}
		});
	}

	private static void updateShields(ServerLevel level) {
		for (Entity entity : level.getAllEntities()) {
			if (entity instanceof LivingEntity living
					&& ((AttachmentTarget) living).hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS)) {
				TFDataAttachments.get(living, TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
			}
		}
	}

	private static void updatePlayerCaps(ServerPlayer player) {
		if (TFDataAttachments.get(player, TFDataAttachments.FEATHER_FAN)) {
			player.setIgnoreFallDamageFromCurrentImpulse(true);
			player.currentImpulseImpactPos = player.position();

			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				TFDataAttachments.set(player, TFDataAttachments.FEATHER_FAN, false);
			}
		}
		TFDataAttachments.get(player, TFDataAttachments.YETI_THROWING).tick(player);
		TFDataAttachments.get(player, TFDataAttachments.TF_PORTAL_COOLDOWN).tick(player);
	}

	private static boolean absorbShieldHits(LivingEntity living, net.minecraft.world.damagesource.DamageSource source, float amount) {
		if (living.level().isClientSide() || source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			return true;
		}
		FortificationShieldAttachment attachment = TFDataAttachments.get(living, TFDataAttachments.FORTIFICATION_SHIELDS);
		if (attachment.shieldsLeft() <= 0) {
			return true;
		}
		if (living.invulnerableTime <= 0) {
			attachment.breakShield(living, false);
			FortificationShieldAttachment.addShieldBreakParticles(source, living);
			living.invulnerableTime = living.invulnerableDuration;
		}
		return false;
	}

	private static void playerLogsIn(ServerPlayer player) {
		dataFixLegacyBanish(player);
		if (!((AttachmentTarget) player).hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST)) {
			newSpawnInTwilightForest(player);
		}
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF || player.getServer() == null) {
			return;
		}
		ServerLevel level = player.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null) {
			return;
		}

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());
		player.changeDimension(TFConfig.portalForNewPlayerSpawn
				? TFTeleporter.createTransition(player, level, newDefaultSpawn, true)
				: NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), true, false);
		TFDataAttachments.set(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = getPersistentDataIfPresent(player);
		if (tagCompound == null || !tagCompound.contains(PERSISTED_NBT_TAG)) {
			return;
		}
		CompoundTag playerData = tagCompound.getCompound(PERSISTED_NBT_TAG);
		if (!playerData.contains("twilightforest_banished")) {
			return;
		}

		playerData.remove("twilightforest_banished");
		tagCompound.put(PERSISTED_NBT_TAG, playerData);

		if (!((AttachmentTarget) player).hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST)) {
			TFDataAttachments.set(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
		}
	}

	private static CompoundTag getPersistentDataIfPresent(ServerPlayer player) {
		try {
			Object value = player.getClass().getMethod("getPersistentData").invoke(player);
			return value instanceof CompoundTag tag ? tag : null;
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}
}
