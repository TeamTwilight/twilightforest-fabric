package twilightforest.world.components.chunkgenerators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.synth.*;

//TODO: Clean this class up
@SuppressWarnings("ALL")
public class TFNoiseBasedChunkGenerator extends ChunkGenerator {
    public static final Codec<TFNoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((noiseBasedChunkGenerator) -> {
            return noiseBasedChunkGenerator.biomeSource;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((noiseBasedChunkGenerator) -> {
            return noiseBasedChunkGenerator.seed;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((noiseBasedChunkGenerator) -> {
            return noiseBasedChunkGenerator.settings;
        })).apply(instance, instance.stable(TFNoiseBasedChunkGenerator::new));
    });
    private static final BlockState AIR;
    private static final BlockState[] EMPTY_COLUMN;
    private int cellHeight;
    private int cellWidth;
    int cellCountX;
    int cellCountY;
    int cellCountZ;
    public SurfaceNoise surfaceNoise;
    private NormalNoise barrierNoise;
    private NormalNoise waterLevelNoise;
    private NormalNoise lavaNoise;
    protected BlockState defaultBlock;
    protected BlockState defaultFluid;
    private long seed;
    protected Supplier<NoiseGeneratorSettings> settings;
    private int height;
    private NoiseSampler sampler;
    private BaseStoneSource baseStoneSource;
    OreVeinifier oreVeinifier;
    NoodleCavifier noodleCavifier;

    public TFNoiseBasedChunkGenerator(BiomeSource biomeSource, long l, Supplier<NoiseGeneratorSettings> supplier) {
        this(biomeSource, biomeSource, l, supplier);
    }

    private TFNoiseBasedChunkGenerator(BiomeSource biomeSource, BiomeSource biomeSource2, long l, Supplier<NoiseGeneratorSettings> supplier) {
        super(biomeSource, biomeSource2, ((NoiseGeneratorSettings)supplier.get()).structureSettings(), l);
        this.seed = l;
        NoiseGeneratorSettings noiseGeneratorSettings = (NoiseGeneratorSettings)supplier.get();
        this.settings = supplier;
        NoiseSettings noiseSettings = noiseGeneratorSettings.noiseSettings();
        this.height = noiseSettings.height();
        this.cellHeight = QuartPos.toBlock(noiseSettings.noiseSizeVertical());
        this.cellWidth = QuartPos.toBlock(noiseSettings.noiseSizeHorizontal());
        this.defaultBlock = noiseGeneratorSettings.getDefaultBlock();
        this.defaultFluid = noiseGeneratorSettings.getDefaultFluid();
        this.cellCountX = 16 / this.cellWidth;
        this.cellCountY = noiseSettings.height() / this.cellHeight;
        this.cellCountZ = 16 / this.cellWidth;
        WorldgenRandom worldgenRandom = new WorldgenRandom(l);
        BlendedNoise blendedNoise = new BlendedNoise(worldgenRandom);
        this.surfaceNoise = (SurfaceNoise)(noiseSettings.useSimplexSurfaceNoise() ? new PerlinSimplexNoise(worldgenRandom, IntStream.rangeClosed(-3, 0)) : new PerlinNoise(worldgenRandom, IntStream.rangeClosed(-3, 0)));
        worldgenRandom.consumeCount(2620);
        PerlinNoise perlinNoise = new PerlinNoise(worldgenRandom, IntStream.rangeClosed(-15, 0));
        SimplexNoise simplexNoise2;
        if (noiseSettings.islandNoiseOverride()) {
            WorldgenRandom worldgenRandom2 = new WorldgenRandom(l);
            worldgenRandom2.consumeCount(17292);
            simplexNoise2 = new SimplexNoise(worldgenRandom2);
        } else {
            simplexNoise2 = null;
        }

        this.barrierNoise = NormalNoise.create(new SimpleRandomSource(worldgenRandom.nextLong()), -3, 1.0D);
        this.waterLevelNoise = NormalNoise.create(new SimpleRandomSource(worldgenRandom.nextLong()), -3, 1.0D, 0.0D, 2.0D);
        this.lavaNoise = NormalNoise.create(new SimpleRandomSource(worldgenRandom.nextLong()), -1, 1.0D, 0.0D);
        Object noiseModifier2;
        if (noiseGeneratorSettings.isNoiseCavesEnabled()) {
            noiseModifier2 = new Cavifier(worldgenRandom, noiseSettings.minY() / this.cellHeight);
        } else {
            noiseModifier2 = NoiseModifier.PASSTHROUGH;
        }

        this.sampler = new NoiseSampler(biomeSource, this.cellWidth, this.cellHeight, this.cellCountY, noiseSettings, blendedNoise, simplexNoise2, perlinNoise, (NoiseModifier)noiseModifier2);
        this.baseStoneSource = new DepthBasedReplacingBaseStoneSource(l, this.defaultBlock, Blocks.DEEPSLATE.defaultBlockState(), noiseGeneratorSettings);
        this.oreVeinifier = new OreVeinifier(l, this.defaultBlock, this.cellWidth, this.cellHeight, noiseGeneratorSettings.noiseSettings().minY());
        this.noodleCavifier = new NoodleCavifier(l);
    }

    private boolean isAquifersEnabled() {
        return ((NoiseGeneratorSettings)this.settings.get()).isAquifersEnabled();
    }

    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public ChunkGenerator withSeed(long l) {
        return new net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator(this.biomeSource.withSeed(l), l, this.settings);
    }

    public boolean stable(long l, ResourceKey<NoiseGeneratorSettings> resourceKey) {
        return this.seed == l && this.settings.get().stable(resourceKey);
    }

    private double[] makeAndFillNoiseColumn(int i, int j, int k, int l) {
        double[] ds = new double[l + 1];
        this.fillNoiseColumn(ds, i, j, k, l);
        return ds;
    }

    private void fillNoiseColumn(double[] ds, int i, int j, int k, int l) {
        NoiseSettings noiseSettings = (this.settings.get()).noiseSettings();
        this.sampler.fillNoiseColumn(ds, i, j, noiseSettings, this.getSeaLevel(), k, l);
    }

    public int getBaseHeight(int i, int j, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor) {
        int k = Math.max((this.settings.get()).noiseSettings().minY(), levelHeightAccessor.getMinBuildHeight());
        int l = Math.min((this.settings.get()).noiseSettings().minY() + (this.settings.get()).noiseSettings().height(), levelHeightAccessor.getMaxBuildHeight());
        int m = Mth.intFloorDiv(k, this.cellHeight);
        int n = Mth.intFloorDiv(l - k, this.cellHeight);
        return n <= 0 ? levelHeightAccessor.getMinBuildHeight() : this.iterateNoiseColumn(i, j, (BlockState[])null, types.isOpaque(), m, n).orElse(levelHeightAccessor.getMinBuildHeight());
    }

    public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelHeightAccessor) {
        int k = Math.max((this.settings.get()).noiseSettings().minY(), levelHeightAccessor.getMinBuildHeight());
        int l = Math.min((this.settings.get()).noiseSettings().minY() + (this.settings.get()).noiseSettings().height(), levelHeightAccessor.getMaxBuildHeight());
        int m = Mth.intFloorDiv(k, this.cellHeight);
        int n = Mth.intFloorDiv(l - k, this.cellHeight);
        if (n <= 0) {
            return new NoiseColumn(k, EMPTY_COLUMN);
        } else {
            BlockState[] blockStates = new BlockState[n * this.cellHeight];
            this.iterateNoiseColumn(i, j, blockStates, (Predicate)null, m, n);
            return new NoiseColumn(k, blockStates);
        }
    }

    public BaseStoneSource getBaseStoneSource() {
        return this.baseStoneSource;
    }

    private OptionalInt iterateNoiseColumn(int i, int j, @Nullable BlockState[] blockStates, @Nullable Predicate<BlockState> predicate, int k, int l) {
        int m = SectionPos.blockToSectionCoord(i);
        int n = SectionPos.blockToSectionCoord(j);
        int o = Math.floorDiv(i, this.cellWidth);
        int p = Math.floorDiv(j, this.cellWidth);
        int q = Math.floorMod(i, this.cellWidth);
        int r = Math.floorMod(j, this.cellWidth);
        double d = (double)q / (double)this.cellWidth;
        double e = (double)r / (double)this.cellWidth;
        double[][] ds = new double[][]{this.makeAndFillNoiseColumn(o, p, k, l), this.makeAndFillNoiseColumn(o, p + 1, k, l), this.makeAndFillNoiseColumn(o + 1, p, k, l), this.makeAndFillNoiseColumn(o + 1, p + 1, k, l)};
        Aquifer aquifer = this.getAquifer(k, l, new ChunkPos(m, n));

        for(int s = l - 1; s >= 0; --s) {
            double f = ds[0][s];
            double g = ds[1][s];
            double h = ds[2][s];
            double t = ds[3][s];
            double u = ds[0][s + 1];
            double v = ds[1][s + 1];
            double w = ds[2][s + 1];
            double x = ds[3][s + 1];

            for(int y = this.cellHeight - 1; y >= 0; --y) {
                double z = (double)y / (double)this.cellHeight;
                double aa = Mth.lerp3(z, d, e, f, u, h, w, g, v, t, x);
                int ab = s * this.cellHeight + y;
                int ac = ab + k * this.cellHeight;
                BlockState blockState = this.updateNoiseAndGenerateBaseState(Beardifier.NO_BEARDS, aquifer, this.baseStoneSource, NoiseModifier.PASSTHROUGH, i, ac, j, aa);
                if (blockStates != null) {
                    blockStates[ab] = blockState;
                }

                if (predicate != null && predicate.test(blockState)) {
                    return OptionalInt.of(ac + 1);
                }
            }
        }

        return OptionalInt.empty();
    }

    private Aquifer getAquifer(int i, int j, ChunkPos chunkPos) {
        return !this.isAquifersEnabled() ? Aquifer.createDisabled(this.getSeaLevel(), this.defaultFluid) : Aquifer.create(chunkPos, this.barrierNoise, this.waterLevelNoise, this.lavaNoise, this.settings.get(), this.sampler, i * this.cellHeight, j * this.cellHeight);
    }

    protected BlockState updateNoiseAndGenerateBaseState(Beardifier beardifier, Aquifer aquifer, BaseStoneSource baseStoneSource, NoiseModifier noiseModifier, int i, int j, int k, double d) {
        double e = Mth.clamp(d / 200.0D, -1.0D, 1.0D);
        e = e / 2.0D - e * e * e / 24.0D;
        e = noiseModifier.modifyNoise(e, i, j, k);
        e += beardifier.beardifyOrBury(i, j, k);
        return aquifer.computeState(baseStoneSource, i, j, k, e);
    }

    public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, ChunkAccess chunkAccess) {
        ChunkPos chunkPos = chunkAccess.getPos();
        int i = chunkPos.x;
        int j = chunkPos.z;
        WorldgenRandom worldgenRandom = new WorldgenRandom();
        worldgenRandom.setBaseChunkSeed(i, j);
        ChunkPos chunkPos2 = chunkAccess.getPos();
        int k = chunkPos2.getMinBlockX();
        int l = chunkPos2.getMinBlockZ();
        double d = 0.0625D;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int m = 0; m < 16; ++m) {
            for(int n = 0; n < 16; ++n) {
                int o = k + m;
                int p = l + n;
                int q = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, m, n) + 1;
                double e = this.surfaceNoise.getSurfaceNoiseValue((double)o * 0.0625D, (double)p * 0.0625D, 0.0625D, (double)m * 0.0625D) * 15.0D;
                int r = (this.settings.get()).getMinSurfaceLevel();
                worldGenRegion.getBiome(mutableBlockPos.set(k + m, q, l + n)).buildSurfaceAt(worldgenRandom, chunkAccess, o, p, q, e, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), r, worldGenRegion.getSeed());
            }
        }

        this.setBedrock(chunkAccess, worldgenRandom);
    }

    private void setBedrock(ChunkAccess chunkAccess, Random random) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int i = chunkAccess.getPos().getMinBlockX();
        int j = chunkAccess.getPos().getMinBlockZ();
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.get();
        int k = noiseGeneratorSettings.noiseSettings().minY();
        int l = k + noiseGeneratorSettings.getBedrockFloorPosition();
        int m = this.height - 1 + k - noiseGeneratorSettings.getBedrockRoofPosition();
        boolean n = true;
        int o = chunkAccess.getMinBuildHeight();
        int p = chunkAccess.getMaxBuildHeight();
        boolean bl = m + 5 - 1 >= o && m < p;
        boolean bl2 = l + 5 - 1 >= o && l < p;
        if (bl || bl2) {
            Iterator var15 = BlockPos.betweenClosed(i, 0, j, i + 15, 0, j + 15).iterator();

            while(true) {
                BlockPos blockPos;
                int r;
                do {
                    if (!var15.hasNext()) {
                        return;
                    }

                    blockPos = (BlockPos)var15.next();
                    if (bl) {
                        for(r = 0; r < 5; ++r) {
                            if (r <= random.nextInt(5)) {
                                chunkAccess.setBlockState(mutableBlockPos.set(blockPos.getX(), m - r, blockPos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                            }
                        }
                    }
                } while(!bl2);

                for(r = 4; r >= 0; --r) {
                    if (r <= random.nextInt(5)) {
                        chunkAccess.setBlockState(mutableBlockPos.set(blockPos.getX(), l + r, blockPos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                    }
                }
            }
        }
    }

    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess) {
        NoiseSettings noiseSettings = (this.settings.get()).noiseSettings();
        int i = Math.max(noiseSettings.minY(), chunkAccess.getMinBuildHeight());
        int j = Math.min(noiseSettings.minY() + noiseSettings.height(), chunkAccess.getMaxBuildHeight());
        int k = Mth.intFloorDiv(i, this.cellHeight);
        int l = Mth.intFloorDiv(j - i, this.cellHeight);
        if (l <= 0) {
            return CompletableFuture.completedFuture(chunkAccess);
        } else {
            int m = chunkAccess.getSectionIndex(l * this.cellHeight - 1 + i);
            int n = chunkAccess.getSectionIndex(i);
            return CompletableFuture.supplyAsync(() -> {
                HashSet set = Sets.newHashSet();
                boolean var15 = false;

                ChunkAccess var17;
                try {
                    var15 = true;
                    int mx = m;

                    while(true) {
                        if (mx < n) {
                            var17 = this.doFill(structureFeatureManager, chunkAccess, k, l);
                            var15 = false;
                            break;
                        }

                        LevelChunkSection levelChunkSection = chunkAccess.getOrCreateSection(mx);
                        levelChunkSection.acquire();
                        set.add(levelChunkSection);
                        --mx;
                    }
                } finally {
                    if (var15) {
                        Iterator var12 = set.iterator();

                        while(var12.hasNext()) {
                            LevelChunkSection levelChunkSection3 = (LevelChunkSection)var12.next();
                            levelChunkSection3.release();
                        }

                    }
                }

                Iterator var18 = set.iterator();

                while(var18.hasNext()) {
                    LevelChunkSection levelChunkSection2 = (LevelChunkSection)var18.next();
                    levelChunkSection2.release();
                }

                return var17;
            }, Util.backgroundExecutor());
        }
    }

    private ChunkAccess doFill(StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, int i, int j) {
        Heightmap heightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos chunkPos = chunkAccess.getPos();
        int k = chunkPos.getMinBlockX();
        int l = chunkPos.getMinBlockZ();
        Beardifier beardifier = new Beardifier(structureFeatureManager, chunkAccess);
        Aquifer aquifer = this.getAquifer(i, j, chunkPos);
        NoiseInterpolator noiseInterpolator = new NoiseInterpolator(this.cellCountX, j, this.cellCountZ, chunkPos, i, this::fillNoiseColumn);
        List<NoiseInterpolator> list = Lists.newArrayList(noiseInterpolator);
        Objects.requireNonNull(list);
        Consumer<NoiseInterpolator> consumer = list::add;
        DoubleFunction<BaseStoneSource> doubleFunction = this.createBaseStoneSource(i, chunkPos, consumer);
        DoubleFunction<NoiseModifier> doubleFunction2 = this.createCaveNoiseModifier(i, chunkPos, consumer);
        list.forEach(NoiseInterpolator::initializeForFirstCellX);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int m = 0; m < this.cellCountX; ++m) {
            int finalM = m;
            list.forEach((noiseInterpolatorx) -> {
                noiseInterpolatorx.advanceCellX(finalM);
            });

            for(int o = 0; o < this.cellCountZ; ++o) {
                LevelChunkSection levelChunkSection = chunkAccess.getOrCreateSection(chunkAccess.getSectionsCount() - 1);

                for(int p = j - 1; p >= 0; --p) {
                    int finalP = p;
                    int finalO = o;
                    list.forEach((noiseInterpolatorx) -> {
                        noiseInterpolatorx.selectCellYZ(finalP, finalO);
                    });

                    for(int s = this.cellHeight - 1; s >= 0; --s) {
                        int t = (i + p) * this.cellHeight + s;
                        int u = t & 15;
                        int v = chunkAccess.getSectionIndex(t);
                        if (chunkAccess.getSectionIndex(levelChunkSection.bottomBlockY()) != v) {
                            levelChunkSection = chunkAccess.getOrCreateSection(v);
                        }

                        double d = (double)s / (double)this.cellHeight;
                        list.forEach((noiseInterpolatorx) -> {
                            noiseInterpolatorx.updateForY(d);
                        });

                        for(int w = 0; w < this.cellWidth; ++w) {
                            int x = k + m * this.cellWidth + w;
                            int y = x & 15;
                            double e = (double)w / (double)this.cellWidth;
                            list.forEach((noiseInterpolatorx) -> {
                                noiseInterpolatorx.updateForX(e);
                            });

                            for(int z = 0; z < this.cellWidth; ++z) {
                                int aa = l + o * this.cellWidth + z;
                                int ab = aa & 15;
                                double f = (double)z / (double)this.cellWidth;
                                double g = noiseInterpolator.calculateValue(f);
                                BlockState blockState = this.updateNoiseAndGenerateBaseState(beardifier, aquifer, (BaseStoneSource)doubleFunction.apply(f), (NoiseModifier)doubleFunction2.apply(f), x, t, aa, g);
                                if (blockState != AIR) {
                                    if (blockState.getLightEmission() != 0 && chunkAccess instanceof ProtoChunk) {
                                        mutableBlockPos.set(x, t, aa);
                                        ((ProtoChunk)chunkAccess).addLight(mutableBlockPos);
                                    }

                                    levelChunkSection.setBlockState(y, u, ab, blockState, false);
                                    heightmap.update(y, t, ab, blockState);
                                    heightmap2.update(y, t, ab, blockState);
                                    if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
                                        mutableBlockPos.set(x, t, aa);
                                        chunkAccess.getLiquidTicks().scheduleTick(mutableBlockPos, blockState.getFluidState().getType(), 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            list.forEach(NoiseInterpolator::swapSlices);
        }

        return chunkAccess;
    }

    private DoubleFunction<NoiseModifier> createCaveNoiseModifier(int i, ChunkPos chunkPos, Consumer<NoiseInterpolator> consumer) {
        if (!((NoiseGeneratorSettings)this.settings.get()).isNoodleCavesEnabled()) {
            return (d) -> {
                return NoiseModifier.PASSTHROUGH;
            };
        } else {
            TFNoiseBasedChunkGenerator.NoodleCaveNoiseModifier noodleCaveNoiseModifier = new TFNoiseBasedChunkGenerator.NoodleCaveNoiseModifier(chunkPos, i);
            noodleCaveNoiseModifier.listInterpolators(consumer);
            Objects.requireNonNull(noodleCaveNoiseModifier);
            return noodleCaveNoiseModifier::prepare;
        }
    }

    private DoubleFunction<BaseStoneSource> createBaseStoneSource(int i, ChunkPos chunkPos, Consumer<NoiseInterpolator> consumer) {
        if (!((NoiseGeneratorSettings)this.settings.get()).isOreVeinsEnabled()) {
            return (d) -> this.baseStoneSource;
        } else {
            TFNoiseBasedChunkGenerator.OreVeinNoiseSource oreVeinNoiseSource = new TFNoiseBasedChunkGenerator.OreVeinNoiseSource(chunkPos, i, this.seed + 1L);
            oreVeinNoiseSource.listInterpolators(consumer);
            BaseStoneSource baseStoneSource = (ix, j, k) -> {
                BlockState blockState = oreVeinNoiseSource.getBaseBlock(ix, j, k);
                return blockState != this.defaultBlock ? blockState : this.baseStoneSource.getBaseBlock(ix, j, k);
            };
            return (d) -> {
                oreVeinNoiseSource.prepare(d);
                return baseStoneSource;
            };
        }
    }

    protected Aquifer createAquifer(ChunkAccess chunkAccess) {
        ChunkPos chunkPos = chunkAccess.getPos();
        int i = Math.max(this.settings.get().noiseSettings().minY(), chunkAccess.getMinBuildHeight());
        int j = Mth.intFloorDiv(i, this.cellHeight);
        return this.getAquifer(j, this.cellCountY, chunkPos);
    }

    public int getGenDepth() {
        return this.height;
    }

    public int getSeaLevel() {
        return this.settings.get().seaLevel();
    }

    public int getMinY() {
        return this.settings.get().noiseSettings().minY();
    }

    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome biome, StructureFeatureManager structureFeatureManager, MobCategory mobCategory, BlockPos blockPos) {
        if (structureFeatureManager.getStructureAt(blockPos, true, StructureFeature.SWAMP_HUT).isValid()) {
            if (mobCategory == MobCategory.MONSTER) {
                return StructureFeature.SWAMP_HUT.getSpecialEnemies();
            }

            if (mobCategory == MobCategory.CREATURE) {
                return StructureFeature.SWAMP_HUT.getSpecialAnimals();
            }
        }

        if (mobCategory == MobCategory.MONSTER) {
            if (structureFeatureManager.getStructureAt(blockPos, false, StructureFeature.PILLAGER_OUTPOST).isValid()) {
                return StructureFeature.PILLAGER_OUTPOST.getSpecialEnemies();
            }

            if (structureFeatureManager.getStructureAt(blockPos, false, StructureFeature.OCEAN_MONUMENT).isValid()) {
                return StructureFeature.OCEAN_MONUMENT.getSpecialEnemies();
            }

            if (structureFeatureManager.getStructureAt(blockPos, true, StructureFeature.NETHER_BRIDGE).isValid()) {
                return StructureFeature.NETHER_BRIDGE.getSpecialEnemies();
            }
        }

        return mobCategory == MobCategory.UNDERGROUND_WATER_CREATURE && structureFeatureManager.getStructureAt(blockPos, false, StructureFeature.OCEAN_MONUMENT).isValid() ? StructureFeature.OCEAN_MONUMENT.getSpecialUndergroundWaterAnimals() : super.getMobsAt(biome, structureFeatureManager, mobCategory, blockPos);
    }

    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
        if (!((NoiseGeneratorSettings)this.settings.get()).disableMobGeneration()) {
            ChunkPos chunkPos = worldGenRegion.getCenter();
            Biome biome = worldGenRegion.getBiome(chunkPos.getWorldPosition());
            WorldgenRandom worldgenRandom = new WorldgenRandom();
            worldgenRandom.setDecorationSeed(worldGenRegion.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
            NaturalSpawner.spawnMobsForChunkGeneration(worldGenRegion, biome, chunkPos, worldgenRandom);
        }
    }

    static {
        AIR = Blocks.AIR.defaultBlockState();
        EMPTY_COLUMN = new BlockState[0];
    }

    class NoodleCaveNoiseModifier implements NoiseModifier {
        private final NoiseInterpolator toggle;
        private final NoiseInterpolator thickness;
        private final NoiseInterpolator ridgeA;
        private final NoiseInterpolator ridgeB;
        private double factorZ;

        public NoodleCaveNoiseModifier(ChunkPos chunkPos, int i) {
            int var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            int var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            int var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            NoodleCavifier var10008 = TFNoiseBasedChunkGenerator.this.noodleCavifier;
            Objects.requireNonNull(var10008);
            this.toggle = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillToggleNoiseColumn);
            var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            var10008 = TFNoiseBasedChunkGenerator.this.noodleCavifier;
            Objects.requireNonNull(var10008);
            this.thickness = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillThicknessNoiseColumn);
            var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            var10008 = TFNoiseBasedChunkGenerator.this.noodleCavifier;
            Objects.requireNonNull(var10008);
            this.ridgeA = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillRidgeANoiseColumn);
            var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            var10008 = TFNoiseBasedChunkGenerator.this.noodleCavifier;
            Objects.requireNonNull(var10008);
            this.ridgeB = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillRidgeBNoiseColumn);
        }

        public NoiseModifier prepare(double d) {
            this.factorZ = d;
            return this;
        }

        public double modifyNoise(double d, int i, int j, int k) {
            double e = this.toggle.calculateValue(this.factorZ);
            double f = this.thickness.calculateValue(this.factorZ);
            double g = this.ridgeA.calculateValue(this.factorZ);
            double h = this.ridgeB.calculateValue(this.factorZ);
            return TFNoiseBasedChunkGenerator.this.noodleCavifier.noodleCavify(d, i, j, k, e, f, g, h, TFNoiseBasedChunkGenerator.this.getMinY());
        }

        public void listInterpolators(Consumer<NoiseInterpolator> consumer) {
            consumer.accept(this.toggle);
            consumer.accept(this.thickness);
            consumer.accept(this.ridgeA);
            consumer.accept(this.ridgeB);
        }
    }

    private class OreVeinNoiseSource implements BaseStoneSource {
        private final NoiseInterpolator veininess;
        private final NoiseInterpolator veinA;
        private final NoiseInterpolator veinB;
        private double factorZ;
        private final long seed;
        private final WorldgenRandom random = new WorldgenRandom();

        public OreVeinNoiseSource(ChunkPos chunkPos, int i, long l) {
            int var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            int var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            int var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            OreVeinifier var10008 = TFNoiseBasedChunkGenerator.this.oreVeinifier;
            Objects.requireNonNull(var10008);
            this.veininess = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillVeininessNoiseColumn);
            var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            var10008 = TFNoiseBasedChunkGenerator.this.oreVeinifier;
            Objects.requireNonNull(var10008);
            this.veinA = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillNoiseColumnA);
            var10003 = TFNoiseBasedChunkGenerator.this.cellCountX;
            var10004 = TFNoiseBasedChunkGenerator.this.cellCountY;
            var10005 = TFNoiseBasedChunkGenerator.this.cellCountZ;
            var10008 = TFNoiseBasedChunkGenerator.this.oreVeinifier;
            Objects.requireNonNull(var10008);
            this.veinB = new NoiseInterpolator(var10003, var10004, var10005, chunkPos, i, var10008::fillNoiseColumnB);
            this.seed = l;
        }

        public void listInterpolators(Consumer<NoiseInterpolator> consumer) {
            consumer.accept(this.veininess);
            consumer.accept(this.veinA);
            consumer.accept(this.veinB);
        }

        public void prepare(double d) {
            this.factorZ = d;
        }

        public BlockState getBaseBlock(int i, int j, int k) {
            double d = this.veininess.calculateValue(this.factorZ);
            double e = this.veinA.calculateValue(this.factorZ);
            double f = this.veinB.calculateValue(this.factorZ);
            this.random.setBaseStoneSeed(this.seed, i, j, k);
            return TFNoiseBasedChunkGenerator.this.oreVeinifier.oreVeinify(this.random, i, j, k, d, e, f);
        }
    }
}
