package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TFLeavesBlock extends LeavesBlock {
	public TFLeavesBlock(BlockBehaviour.Properties properties) {
		super(properties);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFlammability(), getFireSpreadSpeed());
	}

	public int getFlammability() {
		return 60;
	}

	public int getFireSpreadSpeed() {
		return 30;
	}
}
