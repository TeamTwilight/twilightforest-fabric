package twilightforest.util;

import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Utility class for Huge Mushroom blocks. Contains presets
 */
public class HugeMushroomUtil {
	public static BlockState getState(HugeMushroomType type, BlockState base) {
		return base
			.setValue(HugeMushroomBlock.UP, type.isTop())
			.setValue(HugeMushroomBlock.DOWN, type.isBottom())
			.setValue(HugeMushroomBlock.NORTH, type.isNorth())
			.setValue(HugeMushroomBlock.SOUTH, type.isSouth())
			.setValue(HugeMushroomBlock.EAST, type.isEast())
			.setValue(HugeMushroomBlock.WEST, type.isWest());
	}
}
