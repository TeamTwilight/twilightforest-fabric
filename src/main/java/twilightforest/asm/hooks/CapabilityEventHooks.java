package twilightforest.asm.hooks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.events.CapabilityEvents;
import twilightforest.init.TFDataAttachments;

// TODO [Fabric] : Integrate these hooks into mixins once the project compiles and can be tested
public final class CapabilityEventHooks {
	/*
	private void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasData(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getData(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	private void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getData(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true);
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				event.getEntity().setData(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		event.getEntity().getData(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		event.getEntity().getData(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	public void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		if (!player.hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			CapabilityEvents.newSpawnInTwilightForest(player);
	}
	 */
}