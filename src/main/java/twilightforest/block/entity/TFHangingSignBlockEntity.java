package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class TFHangingSignBlockEntity extends HangingSignBlockEntity {

	public TFHangingSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.TF_HANGING_SIGN.get();
	}
}
