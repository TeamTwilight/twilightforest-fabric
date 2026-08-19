package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.EntityHooks;

@Mixin(PathfinderMob.class)
public class PathfinderMobMixin {

	@ModifyReturnValue(
		method = "shouldStayCloseToLeashHolder",
		at = @At("RETURN")
	)
	private boolean twilightforest$overrideStayCloseToHolder(boolean original) {
		return EntityHooks.overrideStayCloseToHolder(original, (PathfinderMob) (Object) this);
	}
}
