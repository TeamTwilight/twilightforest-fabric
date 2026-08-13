package carminite.mixin;

import carminite.block.ISpecialExplosionBlock;
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

import java.util.Optional;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageCalculatorMixin {

	@Inject(
		method = "getBlockExplosionResistance",
		at = @At("HEAD"),
		cancellable = true
	)
	public void carminite$specialExplosionBlock(
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