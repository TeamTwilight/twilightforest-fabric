package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.MoonwormBlockEntity;
import twilightforest.init.TFBlockEntities;

import org.jetbrains.annotations.Nullable;

public class MoonwormBlock extends CritterBlock {

	public MoonwormBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MoonwormBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.MOONWORM.get(), MoonwormBlockEntity::tick);
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.LIME_DYE, 1);
	}
}
