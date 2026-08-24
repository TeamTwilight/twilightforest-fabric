package twilightforest.asm.hooks.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.TFDataAttachments;

// TODO [Fabric] : Integrate these hooks into mixins and validate each one of them once the project compiles
public final class CapabilityEventHooks {
	public static void updateShields(Entity entity) {
		if (entity instanceof LivingEntity living && !living.level().isClientSide() && living.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			entity.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	public static void updatePlayerCaps(Player player) {
		if (player.getAttached(TFDataAttachments.FEATHER_FAN)) {
			player.setIgnoreFallDamageFromCurrentImpulse(true, player.position());
			player.currentImpulseImpactPos = player.position();

			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				player.setAttached(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		player.getAttached(TFDataAttachments.YETI_THROWING).tick(player);
		player.getAttached(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(player);
	}
}