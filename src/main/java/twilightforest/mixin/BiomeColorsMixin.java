package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {

	@ModifyReturnValue(
		method = "method_23791",
		at = @At("RETURN")
	)
	private static int twilightforest$resolveFoliageColor(
		int original,
		@Local(argsOnly = true) Biome biome,
		@Local(argsOnly = true, ordinal = 0) double d,
		@Local(argsOnly = true, ordinal = 1) double e
	) {
		return BlockHooks.resolveFoliageColor(original, biome, d, e);
	}
}