package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.material.Material;
import twilightforest.util.WorldUtil;

import java.util.Random;

//Places single block features in patches around the dark forest
public class DarkForestFeature extends Feature<RandomPatchConfiguration> {
    public DarkForestFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        RandomSource rand = ctx.random();
        RandomPatchConfiguration config = ctx.config();

        boolean foundDirt = false;
        Material materialUnder;

        if(pos.getY() <= 40) {
            for (int dy = pos.getY(); dy >= WorldUtil.getSeaLevel(ctx.chunkGenerator()); dy--) {
                materialUnder = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ())).getMaterial();
                if ((materialUnder == Material.GRASS || materialUnder == Material.DIRT) && reader.getBlockState(pos) == Blocks.AIR.defaultBlockState()) {
                    foundDirt = true;
                    pos = new BlockPos(pos.getX(), dy, pos.getZ());
                    break;
                } else if (materialUnder == Material.STONE || materialUnder == Material.SAND) {
                    break;
                }
            }
        }

        if (!foundDirt) {
            return false;
        }

        //RandomPatchFeature placement logic
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = config.xzSpread() + 1;
        int k = config.ySpread() + 1;

        for(int l = 0; l < config.tries(); ++l) {
            blockpos$mutableblockpos.setWithOffset(pos, rand.nextInt(j) - rand.nextInt(j), rand.nextInt(k) - rand.nextInt(k), rand.nextInt(j) - rand.nextInt(j));
            if (config.feature().value().place(reader, ctx.chunkGenerator(), rand, blockpos$mutableblockpos)) {
                ++i;
            }
        }

        return i > 0;
    }
}
