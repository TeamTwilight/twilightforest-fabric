package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import twilightforest.init.TFBlocks;
import twilightforest.util.TFBeanRegistry;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

public class TowerwoodRandomBlockSelectorFactory {
	public static final TowerwoodRandomBlockSelectorFactory INSTANCE = new TowerwoodRandomBlockSelectorFactory();

	static {
		TFBeanRegistry.register(TowerwoodRandomBlockSelectorFactory.class, INSTANCE);
	}

	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.CRACKED_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.MOSSY_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState(), 0.025F),
				Pair.of(TFBlocks.TOWERWOOD.get().defaultBlockState(), 0.775F)
			)
		);
	}
}
