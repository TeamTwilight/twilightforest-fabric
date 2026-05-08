package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.ChiseledCanopyShelfBlock;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin {
	@Inject(method = "checkBurnOut", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
	private void codexTwilight$spawnBookshelfTomesBeforeBurnOut(Level level, BlockPos pos, int chance, RandomSource random, int age, CallbackInfo ci) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof ChiseledCanopyShelfBlock shelf
				&& state.hasProperty(ChiseledCanopyShelfBlock.SPAWNER)
				&& state.getValue(ChiseledCanopyShelfBlock.SPAWNER)) {
			shelf.onCaughtFire(state, level, pos, null, null);
		}
	}
}
