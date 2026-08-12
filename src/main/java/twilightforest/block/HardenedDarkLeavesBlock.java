package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

public class HardenedDarkLeavesBlock extends Block {

	public HardenedDarkLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(TFBlocks.DARK_LEAVES);
	}
}