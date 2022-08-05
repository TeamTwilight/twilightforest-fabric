package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import twilightforest.init.TFBlocks;

public class FallenLeavesFeature extends Feature<NoneFeatureConfiguration> {

	public FallenLeavesFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private final BlockState state = TFBlocks.FALLEN_LEAVES.get().defaultBlockState();

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel worldIn = ctx.level();
		ChunkGenerator generator = ctx.chunkGenerator();
		BlockPos position = ctx.origin();
		RandomSource rand = ctx.random();

		do {
			if (canPlace(position, worldIn)) break;
			position = position.below();
		} while (position.getY() > generator.getSpawnHeight(worldIn));

		for (int x = 0; x < 5; x++)
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(3) != 0)
					continue;
				boolean flag = false;
				int y = 2;
				do {
					if (canPlace(position.offset(x, y, z), worldIn)) {
						flag = true;
						break;
					}
					y--;
				} while (y >= -2);
				if (!flag)
					continue;
				BlockPos pos = position.offset(x, y, z);
				if (((BushBlock) state.getBlock()).canSurvive(state, worldIn, pos))
					worldIn.setBlock(pos, state, 16 | 2);
			}

		return true;
	}

	private static boolean canPlace(BlockPos pos, WorldGenLevel worldIn) {
		BlockState state = worldIn.getBlockState(pos.below());
		return worldIn.isEmptyBlock(pos) && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.DIRT || state.getMaterial() == Material.ICE || worldIn.getFluidState(pos.below()).getType() == Fluids.WATER);
	}
}
