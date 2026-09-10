package twilightforest.events;

import carminite.events.neoforge.EntityMountEvent;
import carminite.events.neoforge.EntityTickEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

public class HostileMountEvents {
	public static final HostileMountEvents INSTANCE = new HostileMountEvents();

	public static volatile boolean allowDismount = false;

	public boolean handleMountDamage(LivingEntity entity, DamageSource source, float amount) {
		// lets not make the player take suffocation damage if riding something
		if (entity instanceof Player && isRidingUnfriendly(entity) && source.is(DamageTypes.IN_WALL)) {
			return false;
		}

		if (source.is(DamageTypes.FALL) && entity.getAttachedOrCreate(TFDataAttachments.YETI_THROWING).getThrown()) {
			entity.hurt(TFDamageTypes.getEntityDamageSource(entity.level(), TFDamageTypes.YEETED, entity.getAttached(TFDataAttachments.YETI_THROWING).getThrower()), amount);
			return false;
		}

		return true;
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	public void preventMountDismount(EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	public void preventHostileMountCrouching(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}