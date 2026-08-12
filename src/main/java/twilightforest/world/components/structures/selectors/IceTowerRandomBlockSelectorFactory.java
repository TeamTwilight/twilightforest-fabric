package twilightforest.world.components.structures.selectors;

import com.mojang.datafixers.util.Pair;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.util.SimpleRandomBlockSelector;

import java.util.List;

public class IceTowerRandomBlockSelectorFactory {
	public static final IceTowerRandomBlockSelectorFactory INSTANCE = new IceTowerRandomBlockSelectorFactory();

	public SimpleRandomBlockSelector make() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.AURORA_BLOCK.defaultBlockState(), 1F)
			)
		);
	}
}