package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BeardifierDuck;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements BeardifierDuck {

	@Unique
	private ObjectListIterator<DensityFunction> twilightforest$customStructureDensities;

	@ModifyReturnValue(
		method = "compute(Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)D",
		at = @At("RETURN")
	)
	private double twilightforest$getCustomDensity(
		double original,
		@Local(argsOnly = true) DensityFunction.FunctionContext context
	) {
		return WorldgenHooks.getCustomDensity(
			original,
			context,
			this.twilightforest$customStructureDensities
		);
	}

	@Override
	public void twilightforest$setCustomStructureDensities(ObjectListIterator<DensityFunction> structureDensities) {
		this.twilightforest$customStructureDensities = structureDensities;
	}
}