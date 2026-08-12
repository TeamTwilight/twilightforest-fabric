package twilightforest.asm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.*;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

	@Inject(
		method = "getPathTypeFromState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/PathType;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void twilightforest$getPathTypeFromState(
		BlockGetter level,
		BlockPos pos,
		CallbackInfoReturnable<PathType> cir
	) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof BanisterBlock) {
			cir.setReturnValue(PathType.FENCE);
		}
	}
}