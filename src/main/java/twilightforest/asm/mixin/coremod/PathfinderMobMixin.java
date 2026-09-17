package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.EntityHooks;

@Mixin(PathfinderMob.class)
public class PathfinderMobMixin {

	@ModifyReturnValue(
		method = "shouldStayCloseToLeashHolder()Z",
		at = @At("RETURN")
	)
	private boolean twilightforest$overrideStayCloseToHolder(boolean original) {
		return EntityHooks.overrideStayCloseToHolder(original, (PathfinderMob) (Object) this);
	}
}