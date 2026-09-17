package twilightforest.asm.hooks.coremod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;

public final class EntityHooks {
	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !mob.hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}

	public static Entity resetStuckUnrestrained(Entity entity) {
		if (!(entity instanceof LivingEntity living) || living.stuckSpeedMultiplier.lengthSqr() <= 1.0E-7 || !TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			return entity;
		living.stuckSpeedMultiplier = Vec3.ZERO;

		return entity;
	}

	public static float resetFactorWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 1.0F : o;
	}
}