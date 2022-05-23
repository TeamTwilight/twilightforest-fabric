package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.extensions.EntityExtensions;
import io.github.fabricators_of_create.porting_lib.util.CustomArrowItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

public class EnderBowItem extends BowItem implements CustomArrowItem {
	private static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties props) {
		super(props);
	}

	public static boolean onHit(Projectile arrow, HitResult hitResult) {
		if (arrow.getOwner() instanceof Player player
						&& evt.getRayTraceResult() instanceof EntityHitResult result
						&& result.getEntity() instanceof LivingEntity living
						&& arrow.getOwner() != result.getEntity()) {

			if (((EntityExtensions)arrow).getExtraCustomData().contains(KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
				player.level.broadcastEntityEvent(player, (byte) 46);
				if(living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle(), true);
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.level.broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle, true);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
		return false;
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		((EntityExtensions)arrow).getExtraCustomData().putBoolean(KEY, true);
		return arrow;
	}
}
