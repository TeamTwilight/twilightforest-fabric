package twilightforest.block;

import net.minecraft.world.level.block.SlimeBlock;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.StickToBlock;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.StickyBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;

public class MazeSlimeBlock extends SlimeBlock implements StickToBlock, StickyBlock {
	public MazeSlimeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canStickTo(BlockState state, BlockState other) {
		return other.is(TFBlocks.MAZE_SLIME_BLOCK) || other.is(BlockTagGenerator.MAZESTONE);
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}
}