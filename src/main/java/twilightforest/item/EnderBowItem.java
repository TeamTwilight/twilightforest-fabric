package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

public class EnderBowItem extends BowItem {
	private static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void onHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
						&& evt.getRayTraceResult() instanceof EntityHitResult
						&& ((EntityHitResult) evt.getRayTraceResult()).getEntity() instanceof LivingEntity living) {

			if (arrow.getPersistentData().contains(KEY) && player.getVehicle() == null) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.yRot, sourcePitch = player.xRot;

				player.yRot = living.yRot;
				player.xRot = living.xRot;
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.yRot = sourceYaw;
				living.xRot = sourcePitch;
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		arrow.getPersistentData().putBoolean(KEY, true);
		return arrow;
	}
}
