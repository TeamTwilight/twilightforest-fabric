package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.StickyBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CarminiteBlock extends Block implements StickyBlock {
	public CarminiteBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}
}
