package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;

public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	public static void init() {
		EntityEvents.TELEPORT.register(HostileMountEvents::entityTeleports);
		LivingDamageEvent.DAMAGE.register(HostileMountEvents::entityHurts);
		EntityMountEvents.DISMOUNT.register(HostileMountEvents::preventMountDismount);
		LivingEntityEvents.LivingTickEvent.TICK.register(HostileMountEvents::livingUpdate);
	}

	public static void entityHurts(LivingDamageEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && CapabilityList.YETI_THROWN.maybeGet(living).map(YetiThrowCapability::getThrown).orElse(false)) {
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, CapabilityList.YETI_THROWN.maybeGet(living).get().getThrower()), event.getAmount());
			event.setCanceled(true);
		}
	}

	public static void entityTeleports(EntityEvents.Teleport.EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	public static boolean preventMountDismount(Entity mounted, Entity mounting) {
		return mounted.level().isClientSide() ||
				!mounted.isAlive() ||
				!(mounting instanceof Player player) || !player.isAlive() ||
				!isRidingUnfriendly(player) || allowDismount || player.getAbilities().invulnerable;
	}

	public static void livingUpdate(LivingEntityEvents.LivingTickEvent event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
