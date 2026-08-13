package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import twilightforest.TFMain;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.templates.*;
import twilightforest.world.components.feature.trees.*;
import twilightforest.world.components.feature.trollcave.TrollBigMushgloomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeBrownMushroomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeRedMushroomFeature;

public class TFFeatures {

	public static final Feature<TFSmallLakeFeature.Configuration> SMALL_LAKE = register("small_lake", new TFSmallLakeFeature(TFSmallLakeFeature.Configuration.CODEC));
	public static final Feature<HugeMushroomFeatureConfiguration> BIG_MUSHGLOOM = register("big_mushgloom", new BigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Feature<HugeMushroomFeatureConfiguration> CANOPY_BROWN_MUSHROOM = register("canopy_brown_mushroom", new BrownCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Feature<HugeMushroomFeatureConfiguration> CANOPY_RED_VANILLA_MUSHROOM = register("canopy_red_vanilla_mushroom", new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 0));
	public static final Feature<HugeMushroomFeatureConfiguration> CANOPY_RED_SMOOTH_MUSHROOM = register("canopy_red_smooth_mushroom", new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 1));
	public static final Feature<HugeMushroomFeatureConfiguration> CANOPY_RED_SPHEROID_MUSHROOM = register("canopy_red_spheroid_mushroom", new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 2));
	public static final Feature<HugeMushroomFeatureConfiguration> CANOPY_RED_FLAT_MUSHROOM = register("canopy_red_flat_mushroom", new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 3));
	public static final Feature<TFTreeFeatureConfig> MEGA_OAK = register("mega_oak", new MegaOakTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<TFTreeFeatureConfig> MEGA_CANOPY = register("mega_canopy", new MegaCanopyTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> CAVE_STALACTITE = register("block_spike", new BlockSpikeFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<TreeConfiguration> DARK_CANOPY_TREE = register("dark_canopy_tree", new DarkCanopyTreeFeature(TreeConfiguration.CODEC));
	public static final Feature<SwizzleConfig> DRUID_HUT = register("druid_hut", new DruidHutFeature(SwizzleConfig.CODEC));
	public static final Feature<NoneFeatureConfiguration> FALLEN_HOLLOW_LOG = register("fallen_hollow_log", new FallenHollowLogFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> FALLEN_LEAVES = register("fallen_leaves", new FallenLeavesFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<HollowLogConfig> FALLEN_SMALL_LOG = register("fallen_small_log", new SmallFallenLogFeature(HollowLogConfig.CODEC));
	public static final Feature<BlockStateConfiguration> FIRE_JET = register("fire_jet", new FireJetFeature(BlockStateConfiguration.CODEC));
	public static final Feature<RuinedFoundationConfig> FOUNDATION = register("foundation", new FoundationFeature(RuinedFoundationConfig.CODEC));
	public static final Feature<NoneFeatureConfiguration> GRAVEYARD = register("graveyard", new GraveyardFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> GROVE_RUINS = register("grove_ruins", new GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> HOLLOW_STUMP = register("hollow_stump", new HollowStumpFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> HUGE_LILY_PAD = register("huge_lily_pad", new HugeLilypadFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> HUGE_WATER_LILY = register("huge_water_lily", new HugeWaterLilyFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<BlockStateConfiguration> LAMPPOSTS = register("lampposts", new LampostFeature(BlockStateConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> LARGE_WINTER_TREE = register("large_winter_tree", new LargeWinterTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<TFTreeFeatureConfig> MINERS_TREE = register("miners_tree", new MiningTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> MONOLITH = register("monolith", new MonolithFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<DiskConfiguration> MYCELIUM_BLOB = register("mycelium_blob", new CheckAbovePatchFeature(DiskConfiguration.CODEC));
	public static final Feature<BerryBushConfig> BERRY_BUSH = register("berry_bush", new BerryBushFeature(BerryBushConfig.CODEC));
	public static final Feature<BlockStateConfiguration> UNDERGROUND_PLANTS = register("underground_plants", new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
	public static final Feature<BlockStateConfiguration> UNDERGROUND_PLANTS_IN_STRUCTURE = register("underground_plants_in_structure", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final Feature<BlockStateConfiguration> OREBERRY_BUSHES = register("oreberry_bushes", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, 1));
	public static final Feature<BlockStateConfiguration> TROLL_VINES = register("troll_vines", new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final Feature<HugeMushroomFeatureConfiguration> TROLL_BIG_MUSHGLOOM = register("troll_cave_big_mushgloom", new TrollBigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Feature<HugeMushroomFeatureConfiguration> TROLL_HUGE_RED_MUSHROOM = register("troll_cave_huge_red_mushroom", new TrollHugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Feature<HugeMushroomFeatureConfiguration> TROLL_HUGE_BROWN_MUSHROOM = register("troll_cave_huge_brown_mushroom", new TrollHugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> STONE_CIRCLE = register("stone_circle", new StoneCircleFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<ThornsConfig> THORNS = register("thorns", new ThornFeature(ThornsConfig.CODEC));
	public static final Feature<TFTreeFeatureConfig> TREE_OF_TIME = register("tree_of_time", new TimeTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> WEBS = register("webs", new WebFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<SwizzleConfig> SIMPLE_WELL = register("simple_well", new SimpleWellFeature(SwizzleConfig.CODEC));
	public static final Feature<SwizzleConfig> FANCY_WELL = register("fancy_well", new FancyWellFeature(SwizzleConfig.CODEC));
	public static final Feature<RootConfig> WOOD_ROOTS = register("wood_roots", new WoodRootFeature(RootConfig.CODEC));
	public static final Feature<NoneFeatureConfiguration> SNOW_UNDER_TREES = register("snow_under_trees", new SnowUnderTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<TreeConfiguration> SNOW_TREE = register("anywhere_tree", new SnowTreeFeature(TreeConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> ENCHANTED_FOREST_VINES = register("enchanted_forest_vines", new EnchantedForestVinesFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<WeightedListFeatureConfig> WEIGHTED_LIST_SELECTOR = register("weighted_list", new WeightedListFeature(WeightedListFeatureConfig.CODEC));

	private static <C extends FeatureConfiguration> Feature<C> register(String name, Feature<C> feature) {
		return Registry.register(
			BuiltInRegistries.FEATURE,
			TFMain.prefix(name),
			feature
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing features...");
	}
}