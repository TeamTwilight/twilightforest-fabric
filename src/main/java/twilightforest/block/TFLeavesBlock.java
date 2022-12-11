package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TFLeavesBlock extends LeavesBlock {
	protected TFLeavesBlock(BlockBehaviour.Properties props) {
		super(props);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFireSpreadSpeed(), getFlammability());
	}

	public int getFlammability() {
		return 60;
	}

	public int getFireSpreadSpeed() {
		return 30;
	}
}
