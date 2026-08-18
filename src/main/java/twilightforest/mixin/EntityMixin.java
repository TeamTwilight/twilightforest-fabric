package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.EntityHooks;

@Mixin(Entity.class)
public class EntityMixin {

	@ModifyReturnValue(
		method = "getBlockJumpFactor",
		at = @At("RETURN")
	)
	private float twilightforest$resetBlockJumpFactor(float original) {
		return EntityHooks.resetFactorWithUnrestrained(original, (Entity) (Object) this);
	}

	@ModifyReturnValue(
		method = "getBlockSpeedFactor",
		at = @At("RETURN")
	)
	private float twilightforest$resetBlockSpeedFactor(float original) {
		return EntityHooks.resetFactorWithUnrestrained(original, (Entity) (Object) this);
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
