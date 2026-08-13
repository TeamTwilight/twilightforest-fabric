package twilightforest.block;

import carminite.block.ISpecialStickyBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

public class MazeSlimeBlock extends SlimeBlock implements ISpecialStickyBlock {
	public MazeSlimeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canStickTo(BlockState state, BlockState other) {
		return other.is(TFBlocks.MAZE_SLIME_BLOCK) || other.is(TFBlocks.MAZESTONE);
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}
}