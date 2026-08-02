package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.WorldgenHooks;

/**
 * Mixin that adds custom density function support to the Beardifier.
 * Uses {@link ModifyReturnValue} (matching the 1.20.1 Fabric port approach)
 * instead of {@code @Inject} + {@code cancellable = true} for reliable
 * return value modification.
 */
@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements WorldgenHooks.CustomBeardifier {

	@Unique
	private ObjectListIterator<DensityFunction> twilightforest$customDensities;

	@Override
	public void tf$setCustomDensities(ObjectListIterator<DensityFunction> densities) {
		this.twilightforest$customDensities = densities;
	}

	@Override
	public ObjectListIterator<DensityFunction> tf$getCustomDensities() {
		return this.twilightforest$customDensities;
	}

	@ModifyReturnValue(
		method = "compute",
		at = @At("RETURN")
	)
	private double twilightforest$computeCustomDensity(
		double original,
		@Local(argsOnly = true) DensityFunction.FunctionContext context
	) {
		return WorldgenHooks.getCustomDensity(original, context, this.twilightforest$customDensities);
	}
}