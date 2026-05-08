package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.BerryBushFeature;
import twilightforest.world.components.feature.BigMushgloomFeature;
import twilightforest.world.components.feature.BlockSpikeFeature;
import twilightforest.world.components.feature.CheckAbovePatchFeature;
import twilightforest.world.components.feature.DarkForestFeature;
import twilightforest.world.components.feature.EnchantedForestVinesFeature;
import twilightforest.world.components.feature.FallenHollowLogFeature;
import twilightforest.world.components.feature.FallenLeavesFeature;
import twilightforest.world.components.feature.FireJetFeature;
import twilightforest.world.components.feature.FoundationFeature;
import twilightforest.world.components.feature.MonolithFeature;
import twilightforest.world.components.feature.TFSmallLakeFeature;
import twilightforest.world.components.feature.trees.SnowUnderTreeFeature;
import twilightforest.world.components.feature.HugeLilypadFeature;
import twilightforest.world.components.feature.HugeWaterLilyFeature;
import twilightforest.world.components.feature.LampostFeature;
import twilightforest.world.components.feature.SmallFallenLogFeature;
import twilightforest.world.components.feature.ThornFeature;
import twilightforest.world.components.feature.UndergroundPlantFeature;
import twilightforest.world.components.feature.WebFeature;
import twilightforest.world.components.feature.WeightedListFeature;
import twilightforest.world.components.feature.WoodRootFeature;
import twilightforest.world.components.feature.config.BerryBushConfig;
import twilightforest.world.components.feature.config.HollowLogConfig;
import twilightforest.world.components.feature.config.RuinedFoundationConfig;
import twilightforest.world.components.feature.config.RootConfig;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.feature.config.ThornsConfig;
import twilightforest.world.components.feature.config.WeightedListFeatureConfig;
import twilightforest.world.components.feature.trollcave.TrollBigMushgloomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeBrownMushroomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeRedMushroomFeature;

/**
 * Registers TF custom Feature types into BuiltInRegistries.FEATURE.
 *
 * Phase 2.5 (initial 4 features) + Phase Q1 polish (16 simple decoration features).
 * Q2/Q3 features (TFTreeFeatureConfig-based, SwizzleConfig wells, mega trees,
 * dark canopy, anywhere/large_winter trees) are deferred — see AGENTS.md.
 */
public final class TFFeatures {
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS =
            register("underground_plants", new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS_IN_STRUCTURE =
            register("underground_plants_in_structure", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> TROLL_VINES =
            register("troll_vines", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
    public static final TFRegistryObject<Feature<RootConfig>> WOOD_ROOTS =
            register("wood_roots", new WoodRootFeature(RootConfig.CODEC));

    public static final TFRegistryObject<Feature<BerryBushConfig>> BERRY_BUSH =
            register("berry_bush", new BerryBushFeature(BerryBushConfig.CODEC));
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> OREBERRY_BUSHES =
            register("oreberry_bushes", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, 1));
    public static final TFRegistryObject<Feature<HollowLogConfig>> FALLEN_SMALL_LOG =
            register("fallen_small_log", new SmallFallenLogFeature(HollowLogConfig.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> FALLEN_HOLLOW_LOG =
            register("fallen_hollow_log", new FallenHollowLogFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> FALLEN_LEAVES =
            register("fallen_leaves", new FallenLeavesFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> LAMPPOSTS =
            register("lampposts", new LampostFeature(BlockStateConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> WEBS =
            register("webs", new WebFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<ThornsConfig>> THORNS =
            register("thorns", new ThornFeature(ThornsConfig.CODEC));
    public static final TFRegistryObject<Feature<BlockStateConfiguration>> FIRE_JET =
            register("fire_jet", new FireJetFeature(BlockStateConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> ENCHANTED_FOREST_VINES =
            register("enchanted_forest_vines", new EnchantedForestVinesFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> HUGE_LILY_PAD =
            register("huge_lily_pad", new HugeLilypadFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> HUGE_WATER_LILY =
            register("huge_water_lily", new HugeWaterLilyFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<WeightedListFeatureConfig>> WEIGHTED_LIST =
            register("weighted_list", new WeightedListFeature(WeightedListFeatureConfig.CODEC));
    public static final TFRegistryObject<Feature<WeightedListFeatureConfig>> WEIGHTED_LIST_SELECTOR = WEIGHTED_LIST;
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_VANILLA_MUSHROOM =
            register("canopy_red_vanilla_mushroom", new twilightforest.world.components.feature.trees.RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 0));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SMOOTH_MUSHROOM =
            register("canopy_red_smooth_mushroom", new twilightforest.world.components.feature.trees.RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 1));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SPHEROID_MUSHROOM =
            register("canopy_red_spheroid_mushroom", new twilightforest.world.components.feature.trees.RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 2));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_FLAT_MUSHROOM =
            register("canopy_red_flat_mushroom", new twilightforest.world.components.feature.trees.RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 3));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> BIG_MUSHGLOOM =
            register("big_mushgloom", new BigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> TROLL_BIG_MUSHGLOOM =
            register("troll_cave_big_mushgloom", new TrollBigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> TROLL_HUGE_RED_MUSHROOM =
            register("troll_cave_huge_red_mushroom", new TrollHugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> TROLL_HUGE_BROWN_MUSHROOM =
            register("troll_cave_huge_brown_mushroom", new TrollHugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> BLOCK_SPIKE =
            register("block_spike", new BlockSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> CAVE_STALACTITE = BLOCK_SPIKE;
    public static final TFRegistryObject<Feature<DiskConfiguration>> MYCELIUM_BLOB =
            register("mycelium_blob", new CheckAbovePatchFeature(DiskConfiguration.CODEC));

    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> MONOLITH =
            register("monolith", new MonolithFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<TFSmallLakeFeature.Configuration>> SMALL_LAKE =
            register("small_lake", new TFSmallLakeFeature(TFSmallLakeFeature.Configuration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> SNOW_UNDER_TREES =
            register("snow_under_trees", new SnowUnderTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration>> DARK_FOREST_PLACER =
            register("dark_forest_placer", new DarkForestFeature(net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> STONE_CIRCLE =
            register("stone_circle", new twilightforest.world.components.feature.templates.StoneCircleFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> GROVE_RUINS =
            register("grove_ruins", new twilightforest.world.components.feature.templates.GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<HugeMushroomFeatureConfiguration>> CANOPY_BROWN_MUSHROOM =
            register("canopy_brown_mushroom", new twilightforest.world.components.feature.trees.BrownCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final TFRegistryObject<Feature<RuinedFoundationConfig>> FOUNDATION =
            register("foundation", new FoundationFeature(RuinedFoundationConfig.CODEC));
    public static final TFRegistryObject<Feature<SwizzleConfig>> SIMPLE_WELL =
            register("simple_well", new twilightforest.world.components.feature.templates.SimpleWellFeature(SwizzleConfig.CODEC));
    public static final TFRegistryObject<Feature<SwizzleConfig>> FANCY_WELL =
            register("fancy_well", new twilightforest.world.components.feature.templates.FancyWellFeature(SwizzleConfig.CODEC));
    public static final TFRegistryObject<Feature<SwizzleConfig>> DRUID_HUT =
            register("druid_hut", new twilightforest.world.components.feature.templates.DruidHutFeature(SwizzleConfig.CODEC));
    public static final TFRegistryObject<Feature<NoneFeatureConfiguration>> GRAVEYARD =
            register("graveyard", new twilightforest.world.components.feature.templates.GraveyardFeature(NoneFeatureConfiguration.CODEC));

    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> MEGA_OAK =
            register("mega_oak", new twilightforest.world.components.feature.trees.MegaOakTreeFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));
    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> MEGA_CANOPY =
            register("mega_canopy", new twilightforest.world.components.feature.trees.MegaCanopyTreeFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));
    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> MINERS_TREE =
            register("miners_tree", new twilightforest.world.components.feature.trees.MiningTreeFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));
    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> TREE_OF_TIME =
            register("tree_of_time", new twilightforest.world.components.feature.trees.TimeTreeFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));
    public static final TFRegistryObject<Feature<net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration>> DARK_CANOPY_TREE =
            register("dark_canopy_tree", new twilightforest.world.components.feature.trees.DarkCanopyTreeFeature(net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.CODEC));
    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> LARGE_WINTER_TREE =
            register("large_winter_tree", new twilightforest.world.components.feature.trees.LargeWinterTreeFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));
    public static final TFRegistryObject<Feature<net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration>> ANYWHERE_TREE =
            register("anywhere_tree", new twilightforest.world.components.feature.trees.SnowTreeFeature(net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.CODEC));
    public static final TFRegistryObject<Feature<net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration>> SNOW_TREE = ANYWHERE_TREE;
    public static final TFRegistryObject<Feature<twilightforest.world.components.feature.config.TFTreeFeatureConfig>> HOLLOW_STUMP =
            register("hollow_stump", new twilightforest.world.components.feature.trees.HollowStumpFeature(twilightforest.world.components.feature.config.TFTreeFeatureConfig.codecTFTreeConfig));

    private TFFeatures() {
    }

    public static void bootstrap() {
        UNDERGROUND_PLANTS.get();
        UNDERGROUND_PLANTS_IN_STRUCTURE.get();
        TROLL_VINES.get();
        WOOD_ROOTS.get();
        BERRY_BUSH.get();
        OREBERRY_BUSHES.get();
        FALLEN_SMALL_LOG.get();
        FALLEN_HOLLOW_LOG.get();
        FALLEN_LEAVES.get();
        LAMPPOSTS.get();
        WEBS.get();
        THORNS.get();
        FIRE_JET.get();
        ENCHANTED_FOREST_VINES.get();
        HUGE_LILY_PAD.get();
        HUGE_WATER_LILY.get();
        WEIGHTED_LIST.get();
        WEIGHTED_LIST_SELECTOR.get();
        CANOPY_RED_VANILLA_MUSHROOM.get();
        CANOPY_RED_SMOOTH_MUSHROOM.get();
        CANOPY_RED_SPHEROID_MUSHROOM.get();
        CANOPY_RED_FLAT_MUSHROOM.get();
        BIG_MUSHGLOOM.get();
        TROLL_BIG_MUSHGLOOM.get();
        TROLL_HUGE_RED_MUSHROOM.get();
        TROLL_HUGE_BROWN_MUSHROOM.get();
        BLOCK_SPIKE.get();
        CAVE_STALACTITE.get();
        MYCELIUM_BLOB.get();
        MONOLITH.get();
        SMALL_LAKE.get();
        SNOW_UNDER_TREES.get();
        DARK_FOREST_PLACER.get();
        STONE_CIRCLE.get();
        GROVE_RUINS.get();
        CANOPY_BROWN_MUSHROOM.get();
        FOUNDATION.get();
        SIMPLE_WELL.get();
        FANCY_WELL.get();
        DRUID_HUT.get();
        GRAVEYARD.get();
        MEGA_OAK.get();
        MEGA_CANOPY.get();
        MINERS_TREE.get();
        TREE_OF_TIME.get();
        DARK_CANOPY_TREE.get();
        LARGE_WINTER_TREE.get();
        ANYWHERE_TREE.get();
        SNOW_TREE.get();
        HOLLOW_STUMP.get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <F extends Feature<?>> TFRegistryObject<F> register(String path, F feature) {
        F registered = (F) Registry.register(BuiltInRegistries.FEATURE, TwilightForestMod.prefix(path), feature);
        return new TFRegistryObject<>(registered);
    }
}