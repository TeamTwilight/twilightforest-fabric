package twilightforest.world.components.feature.trees.growers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFConfiguredFeatures;

public class SmallOakTreeGrower extends AbstractMegaTreeGrower {

	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean makeBees) {
		return random.nextInt(10) == 0 ? TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE : TFConfiguredFeatures.TWILIGHT_OAK_TREE;
	}

	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
		return random.nextInt(3) == 2 ? TFConfiguredFeatures.SAVANNAH_CANOPY_OAK_TREE : TFConfiguredFeatures.FOREST_CANOPY_OAK_TREE;
	}
}
