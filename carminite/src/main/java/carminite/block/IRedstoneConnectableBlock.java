package carminite.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface IRedstoneConnectableBlock {
	default boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
		if (state.is(Blocks.REDSTONE_WIRE)) {
			return true;
		} else if (state.is(Blocks.REPEATER)) {
			Direction facing = state.getValue(RepeaterBlock.FACING);
			return facing == direction || facing.getOpposite() == direction;
		} else if (state.is(Blocks.OBSERVER)) {
			return direction == state.getValue(ObserverBlock.FACING);
		} else {
			return state.isSignalSource() && direction != null;
		}
	}
}