package carminite.mixin;

import carminite.block.IFireSourceBlock;
import carminite.block.IFlammableBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

	@ModifyVariable(
		method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
		at = @At("STORE"),
		name = "infiniBurn"
	)
	private boolean carminite$isFireSource(
		boolean infiniBurn,
		BlockState state,
		ServerLevel level,
		BlockPos pos,
		@Local(name = "belowState") BlockState belowState
	) {
		if (belowState.getBlock() instanceof IFireSourceBlock fireSourceBlock) {
			return fireSourceBlock.isFireSource(belowState, level, pos, Direction.UP);
		}
		return infiniBurn;
	}
}