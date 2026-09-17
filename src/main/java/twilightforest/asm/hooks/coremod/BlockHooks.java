package twilightforest.asm.hooks.coremod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.custom.TravellersModifiersManager;

public final class BlockHooks {
	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08)
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
	}

	public static boolean resetSlimeMomentumWithUnrestrained(boolean o, Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return true; //dont return false here as the original check is looking that an entity is NOT stepping carefully
		}
		return o;
	}

	public static float resetBlockFrictionWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 0.6F : o;
	}
}