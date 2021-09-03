package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class FlammableBlock extends Block {
	private final int flammability;
	private final int spreadSpeed;

	public FlammableBlock(int flammability, int spreadSpeed, BlockBehaviour.Properties props) {
		super(props);
		this.flammability = flammability;
		this.spreadSpeed = spreadSpeed;
	}

	//TODO: PORT
//	@Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//		return flammability;
//	}
//
//	@Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//		return spreadSpeed;
//	}
}
