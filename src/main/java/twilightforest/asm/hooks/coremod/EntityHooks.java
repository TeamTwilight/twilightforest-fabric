package twilightforest.asm.hooks.coremod;

import net.minecraft.world.entity.PathfinderMob;
import twilightforest.init.TFDataAttachments;

public final class EntityHooks {
	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !mob.hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}
}