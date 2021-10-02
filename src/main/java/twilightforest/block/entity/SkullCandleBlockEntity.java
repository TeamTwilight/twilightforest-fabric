package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//TODO support player skins? I couldnt figure it out, but maybe someone else can give it a shot.
public class SkullCandleBlockEntity extends BlockEntity {

	public SkullCandleBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.SKULL_CANDLE, pos, state);
	}
}
