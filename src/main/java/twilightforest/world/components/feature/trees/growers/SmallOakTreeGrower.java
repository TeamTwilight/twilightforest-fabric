package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFConfiguredFeatures;

public class SmallOakTreeGrower extends AbstractMegaTreeGrower {

	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean b) {
		if (random.nextInt(10) == 0) {
			return TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE;
		} else {
			return TFConfiguredFeatures.TWILIGHT_OAK_TREE;
		}
	}

	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
		return random.nextInt(3) == 2 ? TFConfiguredFeatures.SAVANNAH_CANOPY_OAK_TREE : TFConfiguredFeatures.FOREST_CANOPY_OAK_TREE;
	}
}
