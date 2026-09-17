package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SlimeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

	@Inject(
		method = "bounceUp(Lnet/minecraft/world/entity/Entity;)V",
		at = @At("HEAD")
	)
	private void twilightforest$stopBouncing(
		Entity entity,
		CallbackInfo ci
	) {
		BlockHooks.stopBouncing(entity);
	}

	@ModifyExpressionValue(
		method = "stepOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/Entity;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;isSteppingCarefully()Z"
		)
	)
	private boolean twilightforest$resetSlimeMomentumWithUnrestrained(
		boolean original,
		@Local(argsOnly = true, name = "entity") Entity entity
	) {
		return BlockHooks.resetSlimeMomentumWithUnrestrained(original, entity);
	}
}