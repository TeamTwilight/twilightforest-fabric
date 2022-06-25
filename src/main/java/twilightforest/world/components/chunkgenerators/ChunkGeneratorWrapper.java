package twilightforest.world.components.chunkgenerators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class ChunkGeneratorWrapper extends ChunkGenerator {
    public final ChunkGenerator delegate;

    public ChunkGeneratorWrapper(Registry<StructureSet> structures, ChunkGenerator delegate) {
        super(structures, Optional.empty(), delegate.getBiomeSource());

        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor executor, RandomState random, Blender blender, StructureManager manager, ChunkAccess chunkAccess) {
        return this.delegate.createBiomes(biomes, executor, random, blender, manager, chunkAccess);
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState random, BiomeManager biomeManager, StructureManager manager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {
        this.delegate.applyCarvers(region, seed, random, biomeManager, manager, chunkAccess, carving);
    }

    // Runtime will not care about public -> protected overrides, only compiletime will
    // I'd rather cut our losses than to attempt delegating this method because reflection would likely end up being
    // worse than accepting the potential problems of not delegating this method
    //@Override
    //public Aquifer createAquifer(ChunkAccess chunkAccess) {
    //    return this.delegate.createAquifer(chunkAccess);
    //}


    @org.jetbrains.annotations.Nullable
    @Override
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> structure, BlockPos pos, int radius, boolean skipKnownStructures) {
        return this.delegate.findNearestMapStructure(level, structure, pos, radius, skipKnownStructures);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess access, StructureManager structureManager) {
        this.delegate.applyBiomeDecoration(level, access, structureManager);
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager manager, RandomState random, ChunkAccess chunkAccess) {
        this.delegate.buildSurface(level, manager, random, chunkAccess);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        this.delegate.spawnOriginalMobs(region);
    }

    //FIXME probably gone now, verify
//    @Override
//    public StructureSettings getSettings() {
//        return this.delegate.getSettings();
//    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor level) {
        return this.delegate.getSpawnHeight(level);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return this.delegate.getBiomeSource();
    }

    @Override
    public int getGenDepth() {
        return this.delegate.getGenDepth();
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
        return this.delegate.getMobsAt(biome, structureManager, mobCategory, pos);
    }

    @Override
    public void createStructures(RegistryAccess registry, RandomState random, StructureManager structureManager, ChunkAccess chunkAccess, StructureTemplateManager templateManager, long pSeed) {
        this.delegate.createStructures(registry, random, structureManager, chunkAccess, templateManager, pSeed);
    }

    @Override
    public void createReferences(WorldGenLevel level, StructureManager structureManager, ChunkAccess chunkAccess) {
        this.delegate.createReferences(level, structureManager, chunkAccess);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunkAccess) {
        return this.delegate.fillFromNoise(executor, blender, random, structureManager, chunkAccess);
    }

    @Override
    public int getSeaLevel() {
        return this.delegate.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return this.delegate.getMinY();
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightMap, LevelHeightAccessor level, RandomState random) {
        return this.delegate.getBaseHeight(x, z, heightMap, level, random);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
        return this.delegate.getBaseColumn(x, z, level, random);
    }

    @Override
    public int getFirstFreeHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor level, RandomState random) {
        return this.delegate.getFirstFreeHeight(x, z, heightMapType, level, random);
    }

    @Override
    public int getFirstOccupiedHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor level, RandomState random) {
        return this.delegate.getFirstOccupiedHeight(x, z, heightMapType, level, random);
    }
}
