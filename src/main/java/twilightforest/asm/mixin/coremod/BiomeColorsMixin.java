package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {

	@ModifyReturnValue(
		method = "lambda$static$0",
		at = @At("RETURN")
	)
	private static int twilightforest$resolveFoliageColor(
		int original,
		@Local(argsOnly = true, name = "biome") Biome biome,
		@Local(argsOnly = true, name = "x") double x,
		@Local(argsOnly = true, name = "z") double z
	) {
		return BlockHooks.resolveFoliageColor(original, biome, x, z);
	}
}