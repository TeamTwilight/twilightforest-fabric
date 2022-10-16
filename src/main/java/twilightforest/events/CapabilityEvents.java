package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.TFConfig;
import twilightforest.block.TFPortalBlock;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;

import java.util.concurrent.atomic.AtomicBoolean;

public class CapabilityEvents {

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	public static void init() {
		LivingEntityEvents.TICK.register(CapabilityEvents::updateCaps);
		LivingEntityEvents.ATTACK.register(CapabilityEvents::livingAttack);
		ServerPlayerEvents.AFTER_RESPAWN.register(CapabilityEvents::onPlayerRespawn);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(CapabilityEvents::playerPortals);
		EntityTrackingEvents.START_TRACKING.register(CapabilityEvents::onStartTracking);
	}

	public static void updateCaps(LivingEntity entity) {
		CapabilityList.SHIELDS.maybeGet(entity).ifPresent(IShieldCapability::update);
		CapabilityList.FEATHER_FAN_FALLING.maybeGet(entity).ifPresent(FeatherFanFallCapability::update);
		CapabilityList.YETI_THROWN.maybeGet(entity).ifPresent(YetiThrowCapability::update);
	}

	public static boolean livingAttack(LivingEntity living, DamageSource source, float damage) {
		// shields
		AtomicBoolean cancel = new AtomicBoolean(false);
		if (!living.getLevel().isClientSide() && !source.isBypassArmor()) {
			CapabilityList.SHIELDS.maybeGet(living).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					cancel.set(true);
				}
			});
		}
		return cancel.get();
	}

	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer serverPlayer, boolean alive) {
		if (alive) {
			updateCapabilities(serverPlayer, serverPlayer);
		}

		if (TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && serverPlayer.getRespawnPosition() == null) {
			CompoundTag tagCompound = serverPlayer.getExtraCustomData();
			CompoundTag playerData = tagCompound.getCompound("PlayerPersisted");
			playerData.putBoolean(NBT_TAG_TWILIGHT, false); // set to false so that the method works
			tagCompound.put("PlayerPersisted", playerData); // commit
			banishNewbieToTwilightZone(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public static void playerLogsIn(ServerPlayer player) {
		updateCapabilities(player, player);
		banishNewbieToTwilightZone(player);
	}

	public static void playerPortals(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
		updateCapabilities(player, player);
	}

	public static void onStartTracking(Entity trackedEntity, ServerPlayer player) {
		updateCapabilities(player, trackedEntity);
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		CapabilityList.SHIELDS.maybeGet(shielded).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.sendToClient(new UpdateShieldPacket(shielded, cap), clientTarget);
			}
		});
	}

	// Teleport first-time players to Twilight Forest
	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = player.getExtraCustomData();
		CompoundTag playerData = tagCompound.getCompound("PlayerPersisted");

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put("PlayerPersisted", playerData); // commit

		if (shouldBanishPlayer)
			TFPortalBlock.attemptSendEntity(player, true, TFConfig.COMMON_CONFIG.DIMENSION.portalForNewPlayerSpawn.get()); // See ya hate to be ya
	}
}
