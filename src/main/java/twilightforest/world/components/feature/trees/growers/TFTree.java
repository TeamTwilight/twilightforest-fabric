package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * It's just a Tree, but for TFTreeFeatureConfig
 */
public abstract class TFTree extends AbstractTreeGrower {

	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean b) {
		return null;
	}

	public abstract Holder<? extends ConfiguredFeature<?, ?>> createTreeFeature();

	@Override
	public boolean growTree(ServerLevel world, ChunkGenerator generator, BlockPos pos, BlockState state, Random rand) {
		Holder<? extends ConfiguredFeature<?, ?>> feature = this.createTreeFeature();
		if (feature == null) {
			return false;
		} else {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
			//feature.value().config().forcePlacement();
			if (feature.value().place(world, generator, rand, pos)) {
				return true;
			} else {
				world.setBlock(pos, state, 4);
				return false;
			}
		}
	}
}
