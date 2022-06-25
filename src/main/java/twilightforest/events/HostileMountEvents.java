package twilightforest.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.entity.IHostileMount;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {
		LivingEntity living = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void preventMountDismount(EntityMountEvent event) {
		if (!event.getEntityBeingMounted().getLevel().isClientSide() && !event.isMounting() && event.getEntityBeingMounted().isAlive() && event.getEntityMounting() instanceof LivingEntity living && living.isAlive() && isRidingUnfriendly(living) && !allowDismount)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof IHostileMount)
			event.getEntityLiving().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
