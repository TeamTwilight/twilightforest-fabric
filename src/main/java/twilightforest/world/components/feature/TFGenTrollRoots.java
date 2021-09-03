package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TrollRootBlock;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenTrollRoots extends Feature<NoneFeatureConfiguration> {

	public TFGenTrollRoots(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random random = ctx.random();

		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.below()) {
			if (world.isEmptyBlock(pos) && TrollRootBlock.canPlaceRootBelow(world, pos.above()) && random.nextInt(6) > 0) {
				if (random.nextInt(10) == 0) {
					world.setBlock(pos, TFBlocks.unripe_trollber.defaultBlockState(), 16 | 2);
				} else {
					world.setBlock(pos, TFBlocks.trollvidr.defaultBlockState(), 16 | 2);
				}
			} else {
				pos = new BlockPos(
						copyX + random.nextInt(4) - random.nextInt(4),
						pos.getY(),
						copyZ + random.nextInt(4) - random.nextInt(4)
				);
			}
		}

		return true;
	}
}
