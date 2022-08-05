package twilightforest.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.BiomeKeys;
import twilightforest.init.TFLandmark;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class LegacyLandmarkPlacements {

    private static final Map<ResourceLocation, TFLandmark> BIOME_FEATURES = new ImmutableMap.Builder<ResourceLocation, TFLandmark>()
            //.put(BiomeKeys.DENSE_MUSHROOM_FOREST.location(), MUSHROOM_TOWER)
            .put(BiomeKeys.ENCHANTED_FOREST.location(), TFLandmark.QUEST_GROVE)
            .put(BiomeKeys.LAKE.location(), TFLandmark.QUEST_ISLAND)
            .put(BiomeKeys.SWAMP.location(), TFLandmark.LABYRINTH)
            .put(BiomeKeys.FIRE_SWAMP.location(), TFLandmark.HYDRA_LAIR)
            .put(BiomeKeys.DARK_FOREST.location(), TFLandmark.KNIGHT_STRONGHOLD)
            .put(BiomeKeys.DARK_FOREST_CENTER.location(), TFLandmark.DARK_TOWER)
            .put(BiomeKeys.SNOWY_FOREST.location(), TFLandmark.YETI_CAVE)
            .put(BiomeKeys.GLACIER.location(), TFLandmark.ICE_TOWER)
            .put(BiomeKeys.HIGHLANDS.location(), TFLandmark.TROLL_CAVE)
            .put(BiomeKeys.FINAL_PLATEAU.location(), TFLandmark.FINAL_CASTLE)
            .build();

    /**
     * @return The type of feature directly at the specified Chunk coordinates
     */
    public static TFLandmark getLandmarkDirectlyAt(int chunkX, int chunkZ, WorldGenLevel world) {
        if (blockIsInLandmarkCenter(chunkX << 4, chunkZ << 4)) {
            return pickLandmarkAtBlock(chunkX << 4, chunkZ << 4, world);
        }
        return TFLandmark.NOTHING;
    }

    public static boolean blockIsInLandmarkCenter(int blockX, int blockZ) {
        return chunkHasLandmarkCenter(blockX >> 4, blockZ >> 4);
    }

    public static boolean chunkHasLandmarkCenter(ChunkPos chunkPos) {
        return LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkPos.x, chunkPos.z);
    }

    public static boolean chunkHasLandmarkCenter(int chunkX, int chunkZ) {
        BlockPos nearestCenter = getNearestCenterXZ(chunkX, chunkZ);

        return chunkX == nearestCenter.getX() >> 4 && chunkZ == nearestCenter.getZ() >> 4;
    }

    // TODO For use with Hollow Hills
    public static float distanceFromCenter(BlockPos posXZ, boolean euclidean) {
        BlockPos nearestCenter = getNearestCenterXZ(posXZ.getX() >> 4, posXZ.getZ() >> 4);

        float dX = posXZ.getX() - nearestCenter.getX();
        float dZ = posXZ.getZ() - nearestCenter.getZ();

        if (euclidean) return Mth.sqrt(dX * dX + dZ * dZ);

        return Mth.abs(dX) + Mth.abs(dZ);
    }

    public static TFLandmark pickLandmarkAtBlock(int blockX, int blockZ, WorldGenLevel world) {
        return pickLandmarkForChunk(blockX >> 4, blockZ >> 4, world);
    }

    /**
     * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
     * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
     */
    public static TFLandmark pickLandmarkForChunk(int chunkX, int chunkZ, WorldGenLevel world) {
        // set the chunkX and chunkZ to the center of the biome
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        // what biome is at the center of the chunk?
        Biome biomeAt = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8)).value();
        return pickBiomeLandmarkLegacy(world.registryAccess(), chunkX, chunkZ, biomeAt, world.getSeed());
    }

    public static TFLandmark pickBiomeLandmarkLegacy(RegistryAccess access, int chunkX, int chunkZ, Biome biome, long seed) {
        Optional<? extends Registry<Biome>> registryOpt = access.registry(Registry.BIOME_REGISTRY);

        if (registryOpt.isPresent()) {
            TFLandmark biomeFeature = BIOME_FEATURES.get(registryOpt.get().getKey(biome));

            if(biomeFeature != null)
                return biomeFeature;
        }

        return pickVarietyLandmark(chunkX, chunkZ, seed);
    }

    public static TFLandmark pickVarietyLandmark(int chunkX, int chunkZ, long seed) {
        // set the chunkX and chunkZ to the center of the biome in case they arent already
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
        int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);

        // plant two lich towers near the center of each 2048x2048 map area
        if ((regionOffsetX == 4 && regionOffsetZ == 5) || (regionOffsetX == 4 && regionOffsetZ == 3)) {
            return TFLandmark.LICH_TOWER;
        }

        // also two nagas
        if ((regionOffsetX == 5 && regionOffsetZ == 4) || (regionOffsetX == 3 && regionOffsetZ == 4)) {
            return TFLandmark.NAGA_COURTYARD;
        }

        // get random value
        // okay, well that takes care of most special cases
        return switch (new Random(seed + chunkX * 25117L + chunkZ * 151121L).nextInt(16)) {
            case 6, 7, 8 -> TFLandmark.MEDIUM_HILL;
            case 9 -> TFLandmark.LARGE_HILL;
            case 10, 11 -> TFLandmark.HEDGE_MAZE;
            case 12, 13 -> TFLandmark.NAGA_COURTYARD;
            case 14, 15 -> TFLandmark.LICH_TOWER;
            default -> TFLandmark.SMALL_HILL;
        };
    }

    /**
     * Returns the feature nearest to the specified chunk coordinates.
     */
    public static TFLandmark getNearestLandmark(int cx, int cz, WorldGenLevel world) {
        return getNearestLandmark(cx, cz, world, null);
    }

    /**
     * Returns the feature nearest to the specified chunk coordinates.
     *
     * If a non-null {@code center} is provided and a valid feature is found,
     * it will be set to relative block coordinates indicating the center of
     * that feature relative to the current chunk block coordinate system.
     */
    public static TFLandmark getNearestLandmark(int cx, int cz, WorldGenLevel world, @Nullable Vec2i center) {
        int maxSize = TFLandmark.getMaxSearchSize();
        int diam = maxSize * 2 + 1;
        TFLandmark[] features = new TFLandmark[diam * diam];

        for (int rad = 1; rad <= maxSize; rad++) {
            for (int x = -rad; x <= rad; x++) {
                for (int z = -rad; z <= rad; z++) {

                    int idx = (x + maxSize) * diam + (z + maxSize);
                    TFLandmark directlyAt = features[idx];
                    if (directlyAt == null) {
                        features[idx] = directlyAt = getLandmarkDirectlyAt(x + cx, z + cz, world);
                    }

                    if (directlyAt.size == rad) {
                        if (center != null) {
                            center.x = (x << 4) + 8;
                            center.z = (z << 4) + 8;
                        }
                        return directlyAt;
                    }
                }
            }
        }

        return TFLandmark.NOTHING;
    }

    /**
     * @return The feature in the chunk "region"
     */
    public static TFLandmark getFeatureForRegion(int chunkX, int chunkZ, WorldGenLevel world) {
        //just round to the nearest multiple of 16 chunks?
        int featureX = Math.round(chunkX / 16F) * 16;
        int featureZ = Math.round(chunkZ / 16F) * 16;

        return pickLandmarkForChunk(featureX, featureZ, world);
    }

    /**
     * @return The feature in the chunk "region"
     */
    public static TFLandmark getFeatureForRegionPos(int posX, int posZ, WorldGenLevel world) {
        return getFeatureForRegion(posX >> 4, posZ >> 4, world);
    }

    public static XZQuadrantIterator<BlockPos> landmarkCenterScanner(BlockPos searchFocus, int gridSearchRadius) {
        return new XZQuadrantIterator<>((searchFocus.getX() >> 4) & ~0b1111, (searchFocus.getZ() >> 4) & ~0b1111, false, gridSearchRadius, 16, LegacyLandmarkPlacements::getNearestCenterXZ);
    }

    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXZ(int chunkX, int chunkZ) {
        return getNearestCenterXZ(chunkX, chunkZ, 0);
    }

    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXZ(int chunkX, int chunkZ, int height) {
        // generate random number for the whole biome area
        int regionX = (chunkX + 8) >> 4;
        int regionZ = (chunkZ + 8) >> 4;

        long seed = regionX * 3129871 ^ regionZ * 116129781L;
        seed = seed * seed * 42317861L + seed * 7L;

        int num0 = (int) (seed >> 12 & 3L);
        int num1 = (int) (seed >> 15 & 3L);
        int num2 = (int) (seed >> 18 & 3L);
        int num3 = (int) (seed >> 21 & 3L);

        // slightly randomize center of biome (+/- 3)
        int centerX = 8 + num0 - num1;
        int centerZ = 8 + num2 - num3;

        // centers are offset strangely depending on +/-
        int ccz;
        if (regionZ >= 0) {
            ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
        } else {
            ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
        }

        int ccx;
        if (regionX >= 0) {
            ccx = (regionX * 16 + centerX - 8) * 16 + 8;
        } else {
            ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
        }

        return new BlockPos(ccx, height, ccz);
    }

    public static boolean isTheseFeatures(TFLandmark feature, TFLandmark... predicates) {
        for (TFLandmark predicate : predicates)
            if (feature == predicate)
                return true;
        return false;
    }
}
