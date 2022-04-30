package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FlammableBlock extends Block {
	public FlammableBlock(int flammability, int spreadSpeed, BlockBehaviour.Properties props) {
		super(props);
		FlammableBlockRegistry.getDefaultInstance().add(this, flammability, spreadSpeed);
	}
}
