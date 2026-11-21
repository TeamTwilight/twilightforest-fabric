package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(Level.class)
public class LevelMixin {

	@ModifyReturnValue(
		method = "precipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;",
		at = @At("RETURN")
	)
	private Biome.Precipitation twilightforest$precipitationAt(
		Biome.Precipitation original,
		@Local(argsOnly = true, name = "pos") BlockPos pos
	) {
		return BlockHooks.precipitationAt(original, (Level) (Object) this, pos);
	}
}