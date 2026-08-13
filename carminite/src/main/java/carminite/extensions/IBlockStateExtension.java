package carminite.extensions;

import carminite.block.ISpecialStickyBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockStateExtension {
	default boolean carminite$isStickyBlock() {
		Block block = ((BlockState) this).getBlock();
		if (block instanceof ISpecialStickyBlock specialStickyBlock)
			return specialStickyBlock.isStickyBlock((BlockState) this);
		return block == Blocks.SLIME_BLOCK || block == Blocks.HONEY_BLOCK;
	}

	default boolean carminite$canStickTo(BlockState other) {
		Block block = ((BlockState) this).getBlock();
		if (block instanceof ISpecialStickyBlock stickTo)
			return stickTo.canStickTo((BlockState) this, other);
		if (block == Blocks.HONEY_BLOCK && other.getBlock() == Blocks.SLIME_BLOCK) return false;
		if (block == Blocks.SLIME_BLOCK && other.getBlock() == Blocks.HONEY_BLOCK) return false;
		return carminite$isStickyBlock() || other.carminite$isStickyBlock();
	}
}