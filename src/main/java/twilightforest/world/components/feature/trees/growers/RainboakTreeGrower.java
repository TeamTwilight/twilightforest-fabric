package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFConfiguredFeatures;

public class RainboakTreeGrower extends AbstractTreeGrower {

	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean b) {
		if (random.nextInt(10) == 0) {
			return TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE;
		} else {
			return TFConfiguredFeatures.RAINBOW_OAK_TREE;
		}
	}
}
