package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import twilightforest.tileentity.FireflyTileEntity;
import twilightforest.tileentity.TFTileEntities;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class FireflyBlock extends CritterBlock {

	protected FireflyBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FireflyTileEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFTileEntities.FIREFLY, FireflyTileEntity::tick);
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GLOWSTONE_DUST);
	}
}
