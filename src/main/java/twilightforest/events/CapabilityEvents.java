package twilightforest.events;

import carminite.events.api.TickEvents;
import carminite.events.neoforge.EntityTickEvent;
import carminite.events.neoforge.PlayerTickEvent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelData;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

public class CapabilityEvents {
	public static final CapabilityEvents INSTANCE = new CapabilityEvents();

	public static void init() {
		TickEvents.ENTITY_TICK_POST.register(INSTANCE::updateShields);
		TickEvents.PLAYER_TICK_POST.register(INSTANCE::updatePlayerCaps);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(INSTANCE::absorbShieldHits);
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, _) -> INSTANCE.spawnInTFIfNecessary(newPlayer));
		ServerPlayerEvents.JOIN.register(INSTANCE::playerLogsIn);
	}

	private void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	private void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getAttached(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true, event.getEntity().position());
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				event.getEntity().setAttached(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		event.getEntity().getAttached(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		event.getEntity().getAttached(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	private boolean absorbShieldHits(LivingEntity entity, DamageSource source, float amount) {
		if (!entity.level().isClientSide() && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			FortificationShieldAttachment attachment = entity.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS);
			if (attachment.shieldsLeft() > 0) {
				if (entity.invulnerableTime <= 0) {
					attachment.breakShield(entity, false);
					FortificationShieldAttachment.addShieldBreakParticles(source, entity);
					entity.invulnerableTime = LivingEntity.DEATH_DURATION;
				}
				return false;
			}
		}
		return true;
	}

	private void spawnInTFIfNecessary(ServerPlayer newPlayer) {
		if (newPlayer.getRespawnConfig() == null) {
			newSpawnInTwilightForest(newPlayer);
		}
	}

	public void playerLogsIn(ServerPlayer player) {
		if (player.level().isClientSide())
			return;
		if (!player.hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			CapabilityEvents.newSpawnInTwilightForest(player);
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