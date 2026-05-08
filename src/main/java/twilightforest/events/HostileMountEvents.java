package twilightforest.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.components.entity.YetiThrowAttachment;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

public final class HostileMountEvents {
	public static volatile boolean allowDismount;
	private static boolean bootstrapped;

	private HostileMountEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ServerLivingEntityEvents.ALLOW_DAMAGE.register(HostileMountEvents::handleMountDamage);
		ServerTickEvents.END_WORLD_TICK.register(level -> {
			for (Entity entity : level.getAllEntities()) {
				if (entity instanceof IHostileMount) {
					entity.getPassengers().forEach(passenger -> passenger.setShiftKeyDown(false));
				}
			}
		});
	}

	private static boolean handleMountDamage(LivingEntity living, DamageSource source, float amount) {
		if (living instanceof Player && isRidingUnfriendly(living) && source.is(DamageTypes.IN_WALL)) {
			return false;
		}

		YetiThrowAttachment throwing = TFDataAttachments.get(living, TFDataAttachments.YETI_THROWING);
		if (source.is(DamageTypes.FALL) && throwing.getThrown()) {
			living.hurt(TFDamageTypes.entitySource(living.level(), TFDamageTypes.YEETED, throwing.getThrower()), amount);
			return false;
		}
		return true;
	}

	public static void hostileDismount(Entity rider) {
		allowDismount = true;
		rider.stopRiding();
		allowDismount = false;
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
