package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DarkLeavesBlock extends TFLeavesBlock {

	public DarkLeavesBlock(Properties properties) {
		super(properties);
	}

    @Override
	public int getFlammability() {
		return 1;
	}

	@Override
	public int getFireSpreadSpeed() {
		return 0;
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
