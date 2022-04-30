package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TFLogBlock extends RotatedPillarBlock {

	protected TFLogBlock(BlockBehaviour.Properties props) {
		super(props);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFlammability(), getFireSpreadSpeed());
	}

	public int getFlammability() {
		return 5;
	}

	public int getFireSpreadSpeed() {
		return 5;
	}
}
