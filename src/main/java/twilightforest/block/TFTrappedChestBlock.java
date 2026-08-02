package twilightforest.block;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TFTrappedChestBlock extends ChestBlock {

	public TFTrappedChestBlock(Properties properties) {
		super(properties, () -> BlockEntityType.TRAPPED_CHEST);
	}
}