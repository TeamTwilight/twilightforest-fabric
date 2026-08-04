package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.BlockHooks;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

	@WrapOperation(
		method = "stepOn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;isSteppingCarefully()Z"
		)
	)
	private boolean twilightforest$resetSlimeMomentumWithUnrestrained(
		Entity entity,
		Operation<Boolean> original,
		Level level,
		BlockPos pos,
		BlockState state
	) {
		boolean originalResult = original.call(entity);
		return BlockHooks.resetSlimeMomentumWithUnrestrained(originalResult, entity);
	}

	@Inject(
		method = "bounceUp",
		at = @At("HEAD")
	)
	private void twilightforest$stopBouncing(
		Entity entity,
		CallbackInfo ci
	) {
		BlockHooks.stopBouncing(entity);
	}
}