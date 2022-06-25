package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import twilightforest.init.TFConfiguredFeatures;

public class CanopyTreeGrower extends AbstractTreeGrower {

	@Override
	public Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean largeHive) {
		return TFConfiguredFeatures.CANOPY_TREE;
	}

}
