package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.TFBlocks;

@Mixin(MushroomBlock.class)
public class MushroomBlockMixin {

	@ModifyReturnValue(
		method = "canSurvive",
		at = @At("RETURN")
	)
	private boolean twilightforest$modifySoilDecision(
		boolean original,
		BlockState state,
		LevelReader level,
		BlockPos pos
	) {
		if (original) {
			return true;
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0) continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL)) {
					return true;
				}
			}
		}
		return false;
	}
}
