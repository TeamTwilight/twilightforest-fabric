package carminite.mixin;

import carminite.block.IRedstoneConnectableBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {

	@WrapOperation(
		method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
		)
	)
	private boolean carminite$canConnectRedstone(
		BlockState blockState,
		Direction direction,
		Operation<Boolean> original,
		BlockGetter level,
		BlockPos pos
	) {
		if (blockState.getBlock() instanceof IRedstoneConnectableBlock connectable) {
			return connectable.canConnectRedstone(
				blockState,
				level,
				pos,
				direction
			);
		}

		return original.call(
			blockState,
			direction
		);
	}
}