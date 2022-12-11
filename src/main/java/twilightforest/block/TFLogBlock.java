package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TFLogBlock extends RotatedPillarBlock {

	public TFLogBlock(BlockBehaviour.Properties properties) {
		super(properties);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFireSpreadSpeed(), getFlammability());
	}

	public int getFlammability() {
		return 5;
	}

	public int getFireSpreadSpeed() {
		return 5;
	}
}
