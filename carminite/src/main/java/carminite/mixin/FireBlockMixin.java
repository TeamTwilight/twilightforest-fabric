package carminite.mixin;

import carminite.block.IFlammableBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class FireBlockMixin {

	@Inject(
		method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
		),
		cancellable = true
	)
	private void carminite$onCaughtFire(
		Level level,
		BlockPos pos,
		int chance,
		RandomSource random,
		int age,
		CallbackInfo ci,
		@Local(name = "oldState") BlockState oldState
	) {
		if (oldState.getBlock() instanceof IFlammableBlock flammableBlock) {
			flammableBlock.onCaughtFire(oldState, level, pos, null, null);
			ci.cancel();
		}
	}
}