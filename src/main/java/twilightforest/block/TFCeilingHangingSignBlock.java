package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.block.entity.TFHangingSignBlockEntity;

public class TFCeilingHangingSignBlock extends CeilingHangingSignBlock {
	public TFCeilingHangingSignBlock(Properties properties, WoodType type) {
		super(properties, type);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFHangingSignBlockEntity(pos, state);
	}
}
