package carminite.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IFireSourceBlock {
	default boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
		return state.is(level.dimensionType().infiniburn());
	}
}