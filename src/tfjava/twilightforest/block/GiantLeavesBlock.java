package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 1:1 port of upstream {@code twilightforest.block.GiantLeavesBlock} — GiantBlock subclass
 * with empty support shape (mobs/players can't place blocks against it as if it were solid).
 */
public class GiantLeavesBlock extends GiantBlock {

	public GiantLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.empty();
	}
}
