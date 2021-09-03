package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenThorns extends Feature<NoneFeatureConfiguration> {

	private static final int MAX_SPREAD = 7;
	private static final int CHANCE_OF_BRANCH = 3;
	private static final int CHANCE_OF_LEAF = 3;
	private static final int CHANCE_LEAF_IS_ROSE = 50;

	public TFGenThorns(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();


		// make a 3-5 long stack going up
		int nextLength = 2 + rand.nextInt(4);
		int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

		placeThorns(world, rand, pos, nextLength, Direction.UP, maxLength, pos);

		return true;
	}

	private void placeThorns(WorldGenLevel world, Random rand, BlockPos pos, int length, Direction dir, int maxLength, BlockPos oPos) {
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (world.canSeeSkyFromBelowWater(pos)) {
				if (Math.abs(dPos.getX() - oPos.getX()) < MAX_SPREAD && Math.abs(dPos.getZ() - oPos.getZ()) < MAX_SPREAD && canPlaceThorns(world, dPos)) {
					world.setBlock(dPos, TFBlocks.brown_thorns.defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), 1 | 2);

					// did we make it to the end?
					if (i == length - 1) {
						complete = true;
						// maybe a leaf?  or a rose?
						if (rand.nextInt(CHANCE_OF_LEAF) == 0 && world.isEmptyBlock(dPos.relative(dir))) {
							if (rand.nextInt(CHANCE_LEAF_IS_ROSE) > 0) {
								// leaf
								world.setBlock(dPos.relative(dir), TFBlocks.thorn_leaves.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 3);
							} else {
								// rose
								world.setBlock(dPos.relative(dir), TFBlocks.thorn_rose.defaultBlockState(), 3);
							}
						}
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		// add another off the end
		if (complete && maxLength > 1) {

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, length - 1).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe another branch off the middle
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_BRANCH) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe a leaf
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_LEAF) == 0) {

			int middle = rand.nextInt(length);

			Direction nextDir = Direction.getRandom(rand);

			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);

			if (world.isEmptyBlock(nextPos)) {
				world.setBlock(nextPos, TFBlocks.thorn_leaves.defaultBlockState(), 3/*.with(LeavesBlock.CHECK_DECAY, false)*/);
			}
		}
	}

	private boolean canPlaceThorns(LevelAccessor world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.isAir()
				|| state.is(BlockTags.LEAVES);
	}
}
