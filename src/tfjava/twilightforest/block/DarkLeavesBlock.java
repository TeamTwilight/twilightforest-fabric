package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 1:1 port of upstream {@code twilightforest.block.DarkLeavesBlock} — vanilla LeavesBlock
 * subclass that exposes a full-block support shape (so blocks like fences/walls bond to
 * its top face) and full opacity so it blocks all sky light. Used by Dark Tower canopy.
 */
public class DarkLeavesBlock extends LeavesBlock {

	public DarkLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter getter, BlockPos pos) {
		return 15;
	}
}
