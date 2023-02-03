package twilightforest.world.components.feature.trees.growers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFConfiguredFeatures;

public class TransformationTreeGrower extends AbstractTreeGrower {

	@Nullable
	@Override
	public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean makeBees) {
		return TFConfiguredFeatures.TRANSFORMATION_TREE;
	}
}
