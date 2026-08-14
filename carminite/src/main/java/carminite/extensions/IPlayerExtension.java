package carminite.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlayerExtension {
	default boolean carminite$hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
		throw new AssertionError();
	}
}