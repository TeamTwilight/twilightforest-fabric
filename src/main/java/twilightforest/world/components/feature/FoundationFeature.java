package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeatureUtil;
import twilightforest.world.components.feature.config.RuinedFoundationConfig;

public class FoundationFeature extends Feature<RuinedFoundationConfig> {
    public FoundationFeature(Codec<RuinedFoundationConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<RuinedFoundationConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        RuinedFoundationConfig config = context.config();
        RuinedFoundationConfig.RuinedFoundationDimensions dimensions = config.dimensions();
        RuinedFoundationConfig.RuinedFoundationBlocks blocks = config.blocks();

        IntProvider wallWidths = dimensions.wallWidth();
        int xWidth = wallWidths.sample(random);
        int zWidth = wallWidths.sample(random);

        if (!FeatureUtil.isAreaSuitable(world, pos.offset(1, 0, 1), xWidth - 1, 4, zWidth - 1)) {
            return false;
        }

        generateFoundation(world, random, pos, xWidth, zWidth, dimensions.wallHeights(), dimensions.placeFloorTest(), blocks.wallBlock(), blocks.wallTop(), blocks.decayedWall(), blocks.decayedTop(), blocks.floor());

        int basementDepth = dimensions.basementHeight().sample(random);
        if (basementDepth > 0) {
            BlockPos basementCeilingPos = pos.offset(1, -3, 1);
            generateBasement(xWidth - 2, zWidth - 2, basementDepth, world, basementCeilingPos, random, dimensions.placeFloorTest(), blocks.floor(), blocks.basementPosts(), blocks.lootContainer(), config.lootTable());
        }

        return true;
    }

    private static void generateFoundation(WorldGenLevel world, RandomSource random, BlockPos origin, int xWidth, int zWidth, IntProvider wallHeights, FloatProvider placeFloorTest, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop, BlockStateProvider floor) {
        for (int deltaX = 0; deltaX <= xWidth; deltaX++) {
            for (int deltaZ = 0; deltaZ <= zWidth; deltaZ++) {
                Rotation wallRotation = FeatureLogic.wallVolumeRotation(random, deltaX, deltaZ, xWidth, zWidth);
                if (wallRotation != null) {
                    int height = wallHeights.sample(random);

                    for (int yBlock = 0; yBlock < height; yBlock++) {
                        setWallBlock(world, random, wallBlock, decayedWall, yBlock, origin.offset(deltaX, yBlock - 1, deltaZ), wallRotation);
                    }

                    setWallBlock(world, random, wallTop, decayedTop, height, origin.offset(deltaX, height - 1, deltaZ), wallRotation);
                } else if (placeFloorTest.sample(random) <= 0) {
                    setAndUpdate(world, random, floor, origin.offset(deltaX, -1, deltaZ));
                }
            }
        }
    }

    private static void setWallBlock(WorldGenLevel world, RandomSource random, BlockStateProvider main, BlockStateProvider decay, int yBlock, BlockPos placeAt, Rotation rotation) {
        setAndUpdate(world, random, rollDecay(random, yBlock, main, decay), placeAt, rotation);
    }

    public static BlockStateProvider rollDecay(RandomSource random, int decayRarity, BlockStateProvider main, BlockStateProvider decay) {
        return random.nextInt(decayRarity + 1) >= 1 ? main : decay;
    }

    private static void generateBasement(int xWidth, int zWidth, int depth, WorldGenLevel world, BlockPos ceilingPos, RandomSource random, FloatProvider placeFloorTest, BlockStateProvider floor, BlockStateProvider basementPost, BlockStateProvider lootContainer, ResourceKey<LootTable> lootTable) {
        if (xWidth < 1 || zWidth < 1 || depth < 1) {
            return;
        }

        int chestX = rollChestCoord(xWidth, random);
        int chestZ = rollChestCoord(zWidth, random);

        for (int deltaX = 0; deltaX <= xWidth; deltaX++) {
            for (int deltaZ = 0; deltaZ <= zWidth; deltaZ++) {
                int cornerOverlap = 0;
                if (deltaX == 0) cornerOverlap++;
                if (deltaZ == 0) cornerOverlap++;
                if (deltaX == xWidth) cornerOverlap++;
                if (deltaZ == zWidth) cornerOverlap++;

                boolean isInCorner = cornerOverlap > 1;

                for (int deltaY = 1 - depth; deltaY <= 0; deltaY++) {
                    BlockPos placeAt = ceilingPos.offset(deltaX, deltaY, deltaZ);
                    world.setBlock(placeAt, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    if (isInCorner) {
                        setAndUpdate(world, random, basementPost, placeAt);
                    }
                }

                if ((deltaX == chestX && deltaZ == chestZ) || (cornerOverlap == 0 && placeFloorTest.sample(random) <= 0)) {
                    setAndUpdate(world, random, floor, ceilingPos.offset(deltaX, -depth, deltaZ));
                }
            }
        }

        BlockPos lootPos = ceilingPos.offset(chestX, 1 - depth, chestZ);
        world.setBlock(lootPos, lootContainer.getState(random, lootPos), Block.UPDATE_ALL);
        if (world.getBlockEntity(lootPos) instanceof RandomizableContainerBlockEntity lootBlockEntity) {
            lootBlockEntity.setLootTable(lootTable, world.getSeed() * lootPos.getX() + lootPos.getY() ^ lootPos.getZ());
        }
    }

    private static int rollChestCoord(int width, RandomSource random) {
        if (width < 3) {
            return random.nextInt(Math.max(0, width) + 1);
        }

        return random.nextInt(Math.max(0, width - 1) + 1) + 1;
    }

    private static void setAndUpdate(WorldGenLevel world, RandomSource random, BlockStateProvider floor, BlockPos placeAt) {
        setAndUpdate(world, random, floor, placeAt, Rotation.NONE);
    }

    private static void setAndUpdate(WorldGenLevel world, RandomSource random, BlockStateProvider floor, BlockPos placeAt, Rotation rotation) {
        BlockState state = floor.getState(random, placeAt).rotate(rotation);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            boolean hasWaterOrAbove = world.getFluidState(placeAt).is(FluidTags.WATER) || world.getFluidState(placeAt.above()).is(FluidTags.WATER);
            if (hasWaterOrAbove) {
                state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            }
        }

        world.setBlock(placeAt, state, Block.UPDATE_ALL);
        world.getChunk(placeAt).markPosForPostprocessing(placeAt);
    }
}
