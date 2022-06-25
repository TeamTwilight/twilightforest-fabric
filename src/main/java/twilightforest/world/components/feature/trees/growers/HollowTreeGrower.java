package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import twilightforest.init.TFConfiguredFeatures;

public class HollowTreeGrower extends TFTreeGrower {

	@Override
	public Holder<? extends ConfiguredFeature<?, ?>> createTreeFeature() {
		return TFConfiguredFeatures.HOLLOW_TREE;
	}
}
