package twilightforest.asm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.interfaces.marker.ISpecialExplosionBlock;

import java.util.Optional;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageCalculatorMixin {

	@Inject(
		method = "getBlockExplosionResistance(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Ljava/util/Optional;",
		at = @At("HEAD"),
		cancellable = true
	)
	public void twilightforest$specialExplosionBlock(
		Explosion explosion,
		BlockGetter level,
		BlockPos pos,
		BlockState block,
		FluidState fluid,
		CallbackInfoReturnable<Optional<Float>> cir
	) {
		if (block.getBlock() instanceof ISpecialExplosionBlock specialExplosionBlock) {
			cir.setReturnValue(block.isAir() && fluid.isEmpty()
				? Optional.empty()
				: Optional.of(Math.max(specialExplosionBlock.getExplosionResistance(block, level, pos, explosion), fluid.getExplosionResistance())));
		}
	}
}