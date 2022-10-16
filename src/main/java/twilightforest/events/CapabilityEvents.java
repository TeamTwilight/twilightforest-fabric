package twilightforest.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFPortalBlock;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class CapabilityEvents {

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	@SubscribeEvent
	public static void updateCaps(LivingEvent.LivingTickEvent event) {
		event.getEntity().getCapability(CapabilityList.SHIELDS).ifPresent(IShieldCapability::update);
		event.getEntity().getCapability(CapabilityList.FEATHER_FAN_FALLING).ifPresent(FeatherFanFallCapability::update);
		event.getEntity().getCapability(CapabilityList.YETI_THROWN).ifPresent(YetiThrowCapability::update);
	}

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.getLevel().isClientSide() && !event.getSource().isBypassArmor()) {
			living.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					event.setCanceled(true);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
		if (event.isEndConquered()) {
			updateCapabilities(serverPlayer, serverPlayer);
		}

		if (TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && serverPlayer.getRespawnPosition() == null) {
			CompoundTag tagCompound = serverPlayer.getPersistentData();
			CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);
			playerData.putBoolean(NBT_TAG_TWILIGHT, false); // set to false so that the method works
			tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit
			banishNewbieToTwilightZone(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getEntity().getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer) {
			updateCapabilities((ServerPlayer) event.getEntity(), event.getEntity());
			banishNewbieToTwilightZone(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getEntity().getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
			updateCapabilities(player, event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getEntity(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		shielded.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> clientTarget), new UpdateShieldPacket(shielded, cap));
			}
		});
	}

	// Teleport first-time players to Twilight Forest
	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = player.getPersistentData();
		CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer)
			TFPortalBlock.attemptSendEntity(player, true, TFConfig.COMMON_CONFIG.DIMENSION.portalForNewPlayerSpawn.get()); // See ya hate to be ya
	}
}
