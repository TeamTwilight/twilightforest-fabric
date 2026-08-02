package twilightforest.mixin;

import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.EntityHooks;

@Mixin(PathfinderMob.class)
public class PathFinderMixin {

	@Inject(method = "shouldStayCloseToLeashHolder", at = @At("RETURN"), cancellable = true)
	private void twilightforest$overrideStayCloseToHolder(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(EntityHooks.overrideStayCloseToHolder(cir.getReturnValue(), (PathfinderMob) (Object) this));
	}
}