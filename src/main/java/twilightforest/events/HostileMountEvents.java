package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.MountEntityCallback;
import net.minecraft.world.InteractionResult;
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
		io.github.fabricators_of_create.porting_lib.event.common.EntityEvents.TELEPORT.register(HostileMountEvents::entityTeleports);
		LivingEntityEvents.ATTACK.register(HostileMountEvents::entityHurts);
		MountEntityCallback.EVENT.register(HostileMountEvents::preventMountDismount);
		LivingEntityEvents.TICK.register(HostileMountEvents::livingUpdate);
	}

	public static boolean entityHurts(LivingEntity living, DamageSource damageSource, float amount) {
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			return true;
		}

		if (damageSource.is(DamageTypes.FALL) && CapabilityList.YETI_THROWN.maybeGet(living).map(YetiThrowCapability::getThrown).orElse(false)) {
			living.hurt(TFDamageTypes.getEntityDamageSource(living.getLevel(), TFDamageTypes.YEETED, CapabilityList.YETI_THROWN.maybeGet(living).get().getThrower()), amount);
			return true;
		}

		return false;
	}

	public static void entityTeleports(io.github.fabricators_of_create.porting_lib.event.common.EntityEvents.Teleport.EntityTeleportEvent event) {
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
