package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.data.worldgen.Features;
import net.minecraft.sounds.Music;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.WaterDepthThresholdConfiguration;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.config.CaveStalactiteConfig;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.Supplier;

public final class ConfiguredFeatures {
    // Base configurations
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> TWILIGHT_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/twilight_oak"), Feature.TREE.configured(TreeConfigurations.TWILIGHT_OAK));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> SWAMPY_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/swampy_oak"), Feature.TREE.configured(TreeConfigurations.SWAMPY_OAK));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> FIREFLY_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/firefly_canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE_FIREFLY));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DEAD_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dead_canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE_DEAD));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> MANGROVE_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mangrove_tree"), Feature.TREE.configured(TreeConfigurations.MANGROVE_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.configured(TreeConfigurations.DARKWOOD_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> HOMEGROWN_DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/homegrown_darkwood_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.configured(TreeConfigurations.HOMEGROWN_DARKWOOD_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DARKWOOD_LANTERN_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_lantern_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.configured(TreeConfigurations.DARKWOOD_LANTERN_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> TIME_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/time_tree"), TFBiomeFeatures.TREE_OF_TIME.configured(TreeConfigurations.TIME_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> TRANSFORM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/transform_tree"), Feature.TREE.configured(TreeConfigurations.TRANSFORM_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> MINING_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mining_tree"), TFBiomeFeatures.MINERS_TREE.configured(TreeConfigurations.MINING_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> SORT_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/sort_tree"), Feature.TREE.configured(TreeConfigurations.SORT_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> DENSE_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.configured(TreeConfigurations.DENSE_OAK));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> HOLLOW_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.configured(TreeConfigurations.HOLLOW_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> RAINBOW_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/rainbow_oak"), Feature.TREE.configured(TreeConfigurations.RAINBOAK_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> LARGE_RAINBOW_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/large_rainbow_oak"), Feature.TREE.configured(TreeConfigurations.LARGE_RAINBOAK_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> BROWN_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/brown_canopy_mushroom"), Feature.TREE.configured(TreeConfigurations.MUSHROOM_BROWN));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> RED_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/red_canopy_mushroom"), Feature.TREE.configured(TreeConfigurations.MUSHROOM_RED));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> MEGA_SPRUCE_NO_PODZOL_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mega_spruce_no_podzol"), TFBiomeFeatures.SNOW_TREE.configured(TreeConfigurations.BIG_SPRUCE));
    private static final ConfiguredDecorator<?> DEFAULT_TREE_PLACEMENT = TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE.decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.4F, 1)));
    //Spruce trees that can be placed on snow
    public static final ConfiguredFeature<TreeConfiguration, ?> SNOW_SPRUCE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce"), TFBiomeFeatures.SNOW_TREE.configured(Features.SPRUCE.config()));
    public static final ConfiguredFeature<?, ?> SNOW_SPRUCE_SNOWY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce_snowy"), SNOW_SPRUCE.decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD));
    //Dark Forest
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> OAK_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/oak_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.configured(Features.OAK.config()));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> BIRCH_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/birch_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.configured(Features.BIRCH.config()));
    //fun, can't inherit the config so just have to copy-paste it :)
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> BUSH_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/bush_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(BlockConstants.OAK_LOG), new StraightTrunkPlacer(1, 0, 0), new SimpleStateProvider(BlockConstants.OAK_LEAVES), new SimpleStateProvider(BlockConstants.OAK_SAPLING), new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2), new TwoLayersFeatureSize(0, 0, 0))).build()));

    //Trees!
    public static final ConfiguredFeature<?, ?> CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/canopy_trees"), CANOPY_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> FIREFLY_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/firefly_canopy_trees"), FIREFLY_CANOPY_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> FIREFLY_CANOPY_TREE_MIX = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/firefly_canopy_tree_mix"), Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(ImmutableList.of(() -> CANOPY_TREE_BASE, () -> FIREFLY_CANOPY_TREE_BASE))).decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DEAD_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dead_canopy_trees"), DEAD_CANOPY_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/rainbow_oak_trees"), RAINBOW_OAK_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> LARGE_RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/large_rainbow_oak_trees"), LARGE_RAINBOW_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> MANGROVE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mangrove_trees"), MANGROVE_TREE_BASE.decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR.decorated(FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(6))).squared()));
    public static final ConfiguredFeature<?, ?> DARKWOOD_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_trees"), DARKWOOD_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).count(4).countRandom(2));
    public static final ConfiguredFeature<?, ?> DARKWOOD_LANTERN_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_lantern_trees"), DARKWOOD_LANTERN_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).count(4).countRandom(2));
    public static final ConfiguredFeature<?, ?> TWILIGHT_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/twilight_oak_trees"), TWILIGHT_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> SAVANNAH_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/savannah_oak_trees"), TWILIGHT_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> SWAMPY_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/swampy_oak_trees"), SWAMPY_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> HOLLOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_oak_trees"), HOLLOW_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));
    public static final ConfiguredFeature<?, ?> MEGA_SPRUCE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mega_spruce_no_podzol"), MEGA_SPRUCE_NO_PODZOL_BASE.decorated(DEFAULT_TREE_PLACEMENT).squared().countRandom(4));
    public static final ConfiguredFeature<?, ?> OAK_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/oak_dark_forest_trees"), OAK_DARK_FOREST.decorated(DEFAULT_TREE_PLACEMENT).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.6F, 1))).squared());
    public static final ConfiguredFeature<?, ?> BIRCH_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/birch_dark_forest_trees"), BIRCH_DARK_FOREST.decorated(DEFAULT_TREE_PLACEMENT).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.6F, 1))).squared());
    public static final ConfiguredFeature<?, ?> BUSH_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/bush_dark_forest_trees"), BUSH_DARK_FOREST.decorated(DEFAULT_TREE_PLACEMENT).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.6F, 1))).squared());

    //"structures" that arent actually structures
    public static final ConfiguredFeature<?, ?> WELL = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("well"), TFBiomeFeatures.WELL.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90));
    public static final ConfiguredFeature<?, ?> DRUID_HUT = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("druid_hut"), TFBiomeFeatures.DRUID_HUT.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90));
    public static final ConfiguredFeature<?, ?> GRAVEYARD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("graveyard"), TFBiomeFeatures.GRAVEYARD.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).rarity(95));

    //all the fun little things you find around the dimension
    public static final ConfiguredFeature<?, ?> BIG_MUSHGLOOM = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("big_mushgloom"), TFBiomeFeatures.BIG_MUSHGLOOM.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fallen_leaves"), TFBiomeFeatures.FALLEN_LEAVES.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> FIDDLEHEAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fiddlehead"), Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FIDDLEHEAD), SimpleBlockPlacer.INSTANCE)).tries(16).build()));
    public static final ConfiguredFeature<?, ?> FIRE_JET = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fire_jet"), TFBiomeFeatures.FIRE_JET.configured(new BlockStateConfiguration(BlockConstants.FIRE_JET)).decorated(Features.Decorators.HEIGHTMAP_SQUARE));
    public static final ConfiguredFeature<?, ?> FOUNDATION = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("foundation"), TFBiomeFeatures.FOUNDATION.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90).squared());
    public static final ConfiguredFeature<?, ?> GROVE_RUINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grove_ruins"), TFBiomeFeatures.GROVE_RUINS.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(95).squared());
    public static final ConfiguredFeature<?, ?> HOLLOW_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_log"), TFBiomeFeatures.FALLEN_HOLLOW_LOG.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90).squared());
    public static final ConfiguredFeature<?, ?> HOLLOW_STUMP = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_stump"), TFBiomeFeatures.HOLLOW_STUMP.configured(TreeConfigurations.HOLLOW_TREE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(88).squared());
    public static final ConfiguredFeature<?, ?> HUGE_LILY_PAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_lily_pad"), TFBiomeFeatures.HUGE_LILY_PAD.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(50).squared());
    public static final ConfiguredFeature<?, ?> HUGE_WATER_LILY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_water_lily"), TFBiomeFeatures.HUGE_WATER_LILY.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(65).squared());
    public static final ConfiguredFeature<?, ?> LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("lamppost"), TFBiomeFeatures.LAMPPOSTS.configured(new BlockStateConfiguration(BlockConstants.FIREFLY_JAR)).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> MONOLITH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("monolith"), TFBiomeFeatures.MONOLITH.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().rarity(90));
    public static final ConfiguredFeature<?, ?> MUSHGLOOM_CLUSTER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushgloom_cluster"), Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.INSTANCE)).tries(8).build()));
    public static final ConfiguredFeature<?, ?> MYCELIUM_BLOB = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mycelium_blob"), TFBiomeFeatures.MYCELIUM_BLOB.configured(new DiskConfiguration(BlockConstants.MYCELIUM, UniformInt.of(4, 6), 3, ImmutableList.of(BlockConstants.GRASS_BLOCK))).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> OUTSIDE_STALAGMITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("outside_stalagmite"), TFBiomeFeatures.OUTSIDE_STALAGMITE.configured(new CaveStalactiteConfig(BlockConstants.STONE, 3, 5, 10, false)).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(88).squared());
    public static final ConfiguredFeature<?, ?> PLANT_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("plant_roots"), TFBiomeFeatures.PLANT_ROOTS.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4));
    public static final ConfiguredFeature<?, ?> PUMPKIN_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("pumpkin_lamppost"), TFBiomeFeatures.LAMPPOSTS.configured(new BlockStateConfiguration(BlockConstants.JACK_O_LANTERN)).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> SMALL_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90).squared());
    public static final ConfiguredFeature<?, ?> SMOKER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("smoker"), TFBiomeFeatures.FIRE_JET.configured(new BlockStateConfiguration(BlockConstants.SMOKER)).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));
    public static final ConfiguredFeature<?, ?> STONE_CIRCLE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("stone_circle"), TFBiomeFeatures.STONE_CIRCLE.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(90).squared());
    public static final ConfiguredFeature<?, ?> THORNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("thorns"), TFBiomeFeatures.THORNS.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_THORNLANDS_BLANKETING));
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("torch_berries"), TFBiomeFeatures.TORCH_BERRIES.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4));
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("troll_roots"), TFBiomeFeatures.TROLL_ROOTS.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4).countRandom(8));
    public static final ConfiguredFeature<?, ?> WEBS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("webs"), TFBiomeFeatures.WEBS.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(60));
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("ore/wood_roots_spread"), TFBiomeFeatures.WOOD_ROOTS.configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).range(Features.Decorators.RANGE_BOTTOM_TO_60).rarity(30).squared().count(20));
    public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_under_trees"), TFBiomeFeatures.SNOW_UNDER_TREES.configured(FeatureConfiguration.NONE));
    //smol stone veins
    public static final ConfiguredFeature<?, ?> SMALL_GRANITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_granite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.GRANITE.defaultBlockState(), 16)).rarity(60).range(Features.Decorators.FULL_RANGE).squared().count(5));
    public static final ConfiguredFeature<?, ?> SMALL_DIORITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_diorite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.DIORITE.defaultBlockState(), 16)).rarity(60).range(Features.Decorators.FULL_RANGE).squared().count(5));
    public static final ConfiguredFeature<?, ?> SMALL_ANDESITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_andesite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.ANDESITE.defaultBlockState(), 16)).rarity(60).range(Features.Decorators.FULL_RANGE).squared().count(5));
    //Dark Forest needs special placements, so here we go
    public static final ConfiguredFeature<?, ?> DARK_MUSHGLOOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushglooms"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(10));
    public static final ConfiguredFeature<?, ?> DARK_PUMPKINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_pumpkins"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.PUMPKIN), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(16).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(30));
    public static final ConfiguredFeature<?, ?> DARK_GRASS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_grass"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(128).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).count(4));
    public static final ConfiguredFeature<?, ?> DARK_FERNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_ferns"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(128).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).count(4));
    public static final ConfiguredFeature<?, ?> DARK_MUSHROOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushrooms"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.BROWN_MUSHROOM), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));
    public static final ConfiguredFeature<?, ?> DARK_DEAD_BUSHES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_dead_bushes"), TFBiomeFeatures.DARK_FOREST_PLACER.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.DEAD_BUSH), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(32).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(15));

    //Since we use some vanilla features, we have to register some of them as our own with our own placements
    public static final ConfiguredFeature<?, ?> VANILLA_TF_OAK = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_oak"), Feature.TREE.configured(Features.OAK.config()).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED));
    public static final ConfiguredFeature<?, ?> VANILLA_TF_BIRCH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_birch"), Feature.TREE.configured(Features.BIRCH.config()).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED));
    public static final ConfiguredFeature<?, ?> VANILLA_TF_BIG_MUSH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_big_mush"), Feature.RANDOM_BOOLEAN_SELECTOR.configured(new RandomBooleanFeatureConfiguration(
            () -> Features.HUGE_RED_MUSHROOM,
            () -> Features.HUGE_BROWN_MUSHROOM
            )).decorated(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));

    //tree and mushroom placements
    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_sparse"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).weighted(0.15f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).weighted(0.05f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .count(8)
    );

    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_DENSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_dense"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).weighted(0.675f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT).weighted(0.225f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .count(8)
    );

    public static final ConfiguredFeature<?, ?> OAK_TREES_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/oak_trees_sparse"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.HOLLOW_OAK_TREES.weighted(0.05f),
                    ConfiguredFeatures.SAVANNAH_OAK_TREES.weighted(0.35f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE)
                    .squared()
                    .count(1)
    );
    //why does it only place hollow trees this way?
    public static final ConfiguredFeature<?, ?> HOLLOW_TREE_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_placer"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.HOLLOW_OAK_TREES.weighted(0.01f),
                    ConfiguredFeatures.HOLLOW_OAK_TREES.weighted(0.01f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE)
                    .count(1)
    );

    //ground decoration
    public static final RandomPatchConfiguration SMALL_FLOWER_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
            .add(BlockConstants.POPPY, 1)
            .add(BlockConstants.DANDELION, 1)
            .add(BlockConstants.RED_TULIP, 1)
            .add(BlockConstants.ORANGE_TULIP, 1)
            .add(BlockConstants.PINK_TULIP, 1)
            .add(BlockConstants.WHITE_TULIP, 1)
            .add(BlockConstants.CORNFLOWER, 1)
            .add(BlockConstants.LILY, 1)
            .add(BlockConstants.ORCHID, 1)
            .add(BlockConstants.ALLIUM, 1)
            .add(BlockConstants.AZURE, 1)
            .add(BlockConstants.OXEYE, 1).build())),
            SimpleBlockPlacer.INSTANCE))
            .tries(128)
            .build();

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MAYAPPLE), SimpleBlockPlacer.INSTANCE)).tries(4).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_GRASS), DoublePlantPlacer.INSTANCE)).tries(32).noProjection().build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_FERN), DoublePlantPlacer.INSTANCE)).tries(16).noProjection().build()));

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> OTHER_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_GRASS), DoublePlantPlacer.INSTANCE)).tries(32).noProjection().build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_FERN), DoublePlantPlacer.INSTANCE)).tries(16).noProjection().build()));

    public static final ConfiguredFeature<?, ?> GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(OTHER_GRASS)).count(UniformInt.of(1, 4)).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(30).rarity(5));
    public static final ConfiguredFeature<?, ?> FOREST_GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("forest_grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(FOREST_GRASS)).count(UniformInt.of(1, 4)).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(30).rarity(5));
    public static final ConfiguredFeature<?, ?> FLOWER_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("flower_placer"), Feature.FLOWER.configured(SMALL_FLOWER_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(25).rarity(15));

    //music!
    public static final Music TFMUSICTYPE = new Music(TFSounds.MUSIC, 1200, 12000, true);

    public static void init() {
    }
}