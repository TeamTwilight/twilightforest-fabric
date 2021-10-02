package twilightforest.world.registration;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import twilightforest.TFConstants;
import twilightforest.world.components.feature.trees.treeplacers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import twilightforest.world.components.placements.ChunkBlanketingDecorator;
import twilightforest.world.components.placements.ChunkCenterDecorator;
import twilightforest.world.components.placements.OutOfStructureFilter;

public final class TwilightFeatures {
    private static final List<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = new ArrayList<>();
    private static final List<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = new ArrayList<>();

    public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = registerTrunk(TFConstants.prefix("branching_trunk_placer"), BranchingTrunkPlacer.CODEC);
    public static final TrunkPlacerType<TrunkRiser> TRUNK_RISER = registerTrunk(TFConstants.prefix("trunk_mover_upper"), TrunkRiser.CODEC);

    public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = registerFoliage(TFConstants.prefix("spheroid_foliage_placer"), LeafSpheroidFoliagePlacer.CODEC);

    public static final TreeDecoratorType<TreeCorePlacer> CORE_PLACER = registerTreeFeature(TFConstants.prefix("core_placer"), TreeCorePlacer.CODEC);
    public static final TreeDecoratorType<TrunkSideDecorator> TRUNKSIDE_DECORATOR = registerTreeFeature(TFConstants.prefix("trunkside_decorator"), TrunkSideDecorator.CODEC);
    public static final TreeDecoratorType<TreeRootsDecorator> TREE_ROOTS = registerTreeFeature(TFConstants.prefix("tree_roots"), TreeRootsDecorator.CODEC);
    public static final TreeDecoratorType<DangleFromTreeDecorator> DANGLING_DECORATOR = registerTreeFeature(TFConstants.prefix("dangle_from_tree_decorator"), DangleFromTreeDecorator.CODEC);

    public static final FeatureDecorator<NoneDecoratorConfiguration> PLACEMENT_NOTFSTRUCTURE = new OutOfStructureFilter(NoneDecoratorConfiguration.CODEC);
    public static final FeatureDecorator<NoneDecoratorConfiguration> CHUNK_CENTERER = new ChunkCenterDecorator(NoneDecoratorConfiguration.CODEC);
    public static final FeatureDecorator<ChunkBlanketingDecorator.ChunkBlanketingConfig> PLACEMENT_CHUNK_BLANKETING = new ChunkBlanketingDecorator(ChunkBlanketingDecorator.ChunkBlanketingConfig.CODEC);

    public static final ConfiguredDecorator<?> CONFIGURED_PLACEMENT_NOTFSTRUCTURE = PLACEMENT_NOTFSTRUCTURE.configured(NoneDecoratorConfiguration.INSTANCE);
    public static final ConfiguredDecorator<?> CONFIGURED_CHUNK_CENTERER = CHUNK_CENTERER.configured(NoneDecoratorConfiguration.INSTANCE);
    public static final ConfiguredDecorator<?> CONFIGURED_THORNLANDS_BLANKETING = PLACEMENT_CHUNK_BLANKETING.configured(new ChunkBlanketingDecorator.ChunkBlanketingConfig(0.7f, Heightmap.Types.OCEAN_FLOOR_WG, Optional.of(TFConstants.prefix("thornlands"))));

    private static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(ResourceLocation name, Codec<P> codec) {
        FoliagePlacerType<P> type = new FoliagePlacerType<>(codec);
        Registry.register(Registry.FOLIAGE_PLACER_TYPES, name, type);
        FOLIAGE_PLACER_TYPES.add(type);
        return type;
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(ResourceLocation name, Codec<P> codec) {
        // TRUNK_REPLACER is wrong, it only places, not replacing
        return Registry.register(Registry.TRUNK_PLACER_TYPES, name, new TrunkPlacerType<>(codec));
    }

    private static <P extends TreeDecorator> TreeDecoratorType<P> registerTreeFeature(ResourceLocation name, Codec<P> codec) {
        // TRUNK_REPLACER is wrong, it only places, not replacing
        TreeDecoratorType<P> type = new TreeDecoratorType<>(codec);
        Registry.register(Registry.TREE_DECORATOR_TYPES, name, type);
        TREE_DECORATOR_TYPES.add(type);
        return type;
    }

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(ResourceLocation rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, rl, feature);
    }

    public static void registerPlacementConfigs() {
        Registry.register(Registry.DECORATOR, TFConstants.prefix("nostructure"), PLACEMENT_NOTFSTRUCTURE);
        Registry.register(Registry.DECORATOR, TFConstants.prefix("chunk_centerer"), CHUNK_CENTERER);
        Registry.register(Registry.DECORATOR, TFConstants.prefix("chunk_blanketing"), PLACEMENT_CHUNK_BLANKETING);
    }
}
