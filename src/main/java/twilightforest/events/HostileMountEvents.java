package twilightforest.events;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMoveEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMoveEvents.EntityTeleportEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityDamageEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityDamageEvents.HurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityEvents;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;

public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	public static void init() {
		EntityMoveEvents.TELEPORT.register(HostileMountEvents::entityTeleports);
		LivingEntityDamageEvents.HURT.register(HostileMountEvents::entityHurts);
		EntityMountEvents.DISMOUNT.register(HostileMountEvents::preventMountDismount);
		LivingEntityEvents.TICK.register(HostileMountEvents::livingUpdate);
	}

	public static void entityHurts(HurtEvent event) {
		LivingEntity living = event.damaged;
		DamageSource damageSource = event.damageSource;
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.cancel();
		}

		if (damageSource.is(DamageTypes.FALL) && CapabilityList.YETI_THROWN.maybeGet(living).map(YetiThrowCapability::getThrown).orElse(false)) {
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, CapabilityList.YETI_THROWN.maybeGet(living).get().getThrower()), event.damageAmount);
			event.cancel();
		}
	}

	public static void entityTeleports(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.entity instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	public static boolean preventMountDismount(Entity mounted, Entity mounting) {
		if (!mounted.level().isClientSide() &&
				mounted.isAlive() &&
				mounting instanceof Player player && player.isAlive() &&
				isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			return false;
		return true;
	}

	public static void livingUpdate(LivingEntity entity) {
		if (entity instanceof IHostileMount)
			entity.getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
