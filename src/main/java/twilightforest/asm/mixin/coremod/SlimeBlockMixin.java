package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SlimeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

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