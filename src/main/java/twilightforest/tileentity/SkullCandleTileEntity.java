package twilightforest.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//TODO support player skins? I couldnt figure it out, but maybe someone else can give it a shot.
public class SkullCandleTileEntity extends BlockEntity {

	public SkullCandleTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.SKULL_CANDLE.get(), pos, state);
	}
}
