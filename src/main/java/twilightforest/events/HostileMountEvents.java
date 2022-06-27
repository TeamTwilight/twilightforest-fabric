package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.MountEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.entity.IHostileMount;

public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	public static void init() {
		LivingEntityEvents.ACTUALLY_HURT.register(HostileMountEvents::entityHurts);
		MountEntityCallback.EVENT.register(HostileMountEvents::preventMountDismount);
		LivingEntityEvents.TICK.register(HostileMountEvents::livingUpdate);
	}

	public static float entityHurts(DamageSource damageSource, LivingEntity living, float amount) {
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			return 0;
		}
		return amount;
	}

	public static InteractionResult preventMountDismount(Entity mounted, Entity mounting, boolean isMounting) {
		if (!mounted.getLevel().isClientSide() && !isMounting && mounted.isAlive() && mounting instanceof LivingEntity living && living.isAlive() && isRidingUnfriendly(living) && !allowDismount)
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
