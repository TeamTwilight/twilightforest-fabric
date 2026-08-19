package twilightforest.fabric.interfaces.extension;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlayerExtension {
	default boolean twilightforest$hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
		throw new AssertionError();
	}
}