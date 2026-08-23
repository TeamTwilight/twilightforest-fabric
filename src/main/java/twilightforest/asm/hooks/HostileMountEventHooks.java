package twilightforest.asm.hooks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.entity.IHostileMount;

// TODO [Fabric] : Integrate these hooks into mixins once the project compiles and can be tested
public final class HostileMountEventHooks {
	/*
	private void preventTeleportingOffHostileMounts(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	private void preventMountDismount(EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	private void preventHostilMountCrouching(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}
	 */
}