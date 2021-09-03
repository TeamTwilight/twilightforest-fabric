package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenFallenSmallLog extends Feature<NoneFeatureConfiguration> {

	public TFGenFallenSmallLog(Codec<NoneFeatureConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();

		// determine direction
		boolean goingX = rand.nextBoolean();

		// length
		int length = rand.nextInt(4) + 3;

		// check area clear
		if (goingX) {
			if (!FeatureUtil.isAreaSuitable(world, pos, length, 3, 2)) {
				return false;
			}
		} else {
			if (!FeatureUtil.isAreaSuitable(world, pos, 3, length, 2)) {
				return false;
			}
		}

		// determine wood type
		BlockState logState;
		BlockState branchState;

		switch (rand.nextInt(7)) {
			case 0:
			default:
				logState = TFBlocks.oak_log.defaultBlockState();
				break;
			case 1:
				logState = TFBlocks.canopy_log.defaultBlockState();
				break;
			case 2:
				logState = TFBlocks.mangrove_log.defaultBlockState();
				break;
			case 3:
				logState = Blocks.OAK_LOG.defaultBlockState();
				break;
			case 4:
				logState = Blocks.SPRUCE_LOG.defaultBlockState();
				break;
			case 5:
				logState = Blocks.BIRCH_LOG.defaultBlockState();
				break;
			case 6:
				logState = Blocks.JUNGLE_LOG.defaultBlockState();
				break;
		}

		// check biome
		// Androsa: Uh...what are we checking?


		// make log
		if (goingX) {
			logState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
			branchState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);

			for (int lx = 0; lx < length; lx++) {
				world.setBlock(pos.offset(lx, 0, 1), logState, 3);
				if (rand.nextInt(3) > 0) {
					world.setBlock(pos.offset(lx, 1, 1), TFBlocks.moss_patch.defaultBlockState(), 3);
				}
			}
		} else {
			logState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
			branchState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

			for (int lz = 0; lz < length; lz++) {
				world.setBlock(pos.offset(1, 0, lz), logState, 3);
				if (rand.nextInt(3) > 0) {
					world.setBlock(pos.offset(1, 1, lz), TFBlocks.moss_patch.defaultBlockState(), 3);
				}
			}
		}

		// possibly make branch
		if (rand.nextInt(3) > 0) {
			if (goingX) {
				int bx = rand.nextInt(length);
				int bz = rand.nextBoolean() ? 2 : 0;

				world.setBlock(pos.offset(bx, 0, bz), branchState, 3);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(bx, 1, bz), TFBlocks.moss_patch.defaultBlockState(), 3);
				}
			} else {
				int bx = rand.nextBoolean() ? 2 : 0;
				int bz = rand.nextInt(length);

				world.setBlock(pos.offset(bx, 0, bz), branchState, 3);
				if (rand.nextBoolean()) {
					world.setBlock(pos.offset(bx, 1, bz), TFBlocks.moss_patch.defaultBlockState(), 3);
				}
			}
		}

		return true;
	}
}
