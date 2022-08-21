package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents.Teleport.EntityTeleportEvent;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.MountEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageSources;

public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	public static void init() {
		EntityEvents.TELEPORT.register(HostileMountEvents::entityTeleports);
		LivingEntityEvents.ACTUALLY_HURT.register(HostileMountEvents::entityHurts);
		MountEntityCallback.EVENT.register(HostileMountEvents::preventMountDismount);
		LivingEntityEvents.TICK.register(HostileMountEvents::livingUpdate);
	}

	public static float entityHurts(DamageSource damageSource, LivingEntity living, float amount) {
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			return 0;
		}

		if (damageSource == DamageSource.FALL && CapabilityList.YETI_THROWN.maybeGet(living).map(YetiThrowCapability::getThrown).orElse(false)) {
			event.setCanceled(true);
			living.hurt(TFDamageSources.yeeted(CapabilityList.YETI_THROWN.maybeGet(living).get().getThrower()), amount);
		}

		return amount;
	}

	public static void entityTeleports(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static InteractionResult preventMountDismount(Entity mounted, Entity mounting, boolean isMounting) {
		if (!mounted.getLevel().isClientSide() &&
				!isMounting && mounted.isAlive() &&
				mounting instanceof Player player && player.isAlive() &&
				isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	public static void livingUpdate(LivingEntity entity) {
		if (entity instanceof IHostileMount)
			entity.getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
