package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlock extends TrappedChestBlock {

	public TFTrappedChestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
		return TFBlockEntities.TF_TRAPPED_CHEST;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFTrappedChestBlockEntity(pos, state);
	}
}
