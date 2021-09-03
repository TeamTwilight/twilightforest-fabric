package twilightforest.block;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TFLeavesBlock extends LeavesBlock {
	protected TFLeavesBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	//TODO: PORT
//	@Override
//	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//		return 60;
//	}
//
//	@Override
//	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//		return 30;
//	}
}
