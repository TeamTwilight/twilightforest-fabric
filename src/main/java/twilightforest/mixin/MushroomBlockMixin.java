package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFBlocks;

/**
 * Allows mushrooms to survive on blocks near Twilight Portals.
 * In 1.21.1, BlockState#canSustainPlant was removed, so we intercept canSurvive directly.
 */
@Mixin(MushroomBlock.class)
public class MushroomBlockMixin {

	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
	private void twilightforest$modifySoilDecision(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && z == 0) continue;
					if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL)) {
						cir.setReturnValue(true);
						return;
					}
				}
			}
		}
	}
}