package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.EntityHooks;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(
		method = "getBlockJumpFactor",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$resetBlockJumpFactor(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(EntityHooks.resetFactorWithUnrestrained(cir.getReturnValue(), (Entity) (Object) this));
	}

	@Inject(
		method = "getBlockSpeedFactor",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$resetBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(EntityHooks.resetFactorWithUnrestrained(cir.getReturnValue(), (Entity) (Object) this));
	}

	@Inject(
		method = "move",
		at = @At("HEAD")
	)
	private void twilightforest$resetStuckUnrestrained(
		MoverType type,
		Vec3 pos,
		CallbackInfo ci
	) {
		EntityHooks.resetStuckUnrestrained((Entity) (Object) this);
	}
}