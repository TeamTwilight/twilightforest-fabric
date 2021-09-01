package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.BaseDiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.CaveStalactiteConfig;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;
import twilightforest.world.components.feature.templates.DruidHutFeature;
import twilightforest.world.components.feature.templates.GroveRuinsFeature;
import twilightforest.world.components.feature.templates.StoneCircleFeature;
import twilightforest.world.components.feature.trees.*;
import twilightforest.world.components.feature.trees.growers.SnowTreePlacer;
import twilightforest.world.components.feature.trees.growers.SnowUnderTrees;
import twilightforest.world.components.feature.templates.GraveyardFeature;

//I'd call this TFFeatures, but that'd be confused with TFFeature.

public class TFBiomeFeatures {

	//public static final DeferredRegister<Feature<?> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TwilightForestMod.ID);
	
	public static final Feature<NoneFeatureConfiguration> BIG_MUSHGLOOM = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":big_mushgloom", 
			new TFGenBigMushgloom(NoneFeatureConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> CANOPY_OAK = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":canopy_oak", 
			new TFGenCanopyOak(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<CaveStalactiteConfig> CAVE_STALACTITE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":cave_stalactite", 
			new TFGenCaveStalactite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final Feature<TreeConfiguration> DARK_CANOPY_TREE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":dark_canopy_tree", 
			new TFGenDarkCanopyTree(TreeConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> DRUID_HUT = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":druid_hut", 
			new DruidHutFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> FALLEN_HOLLOW_LOG = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":fallen_hollow_log", 
			new TFGenFallenHollowLog(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> FALLEN_LEAVES = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":fallen_leaves", 
			new TFGenFallenLeaves(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> FALLEN_SMALL_LOG = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":fallen_small_log", 
			new TFGenFallenSmallLog(NoneFeatureConfiguration.CODEC));
	public static final Feature<BlockStateConfiguration> FIRE_JET = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":fire_jet", 
			new TFGenFireJet(BlockStateConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> FOUNDATION = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":foundation", 
			new TFGenFoundation(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> GRAVEYARD = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":graveyard", 
			new GraveyardFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> GROVE_RUINS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":grove_ruins", 
			new GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> HOLLOW_STUMP = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":hollow_stump", 
			new TFGenHollowStump(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<TFTreeFeatureConfig> HOLLOW_TREE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":hollow_tree", 
			new TFGenHollowTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> HUGE_LILY_PAD = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":huge_lily_pad", 
			new TFGenHugeLilyPad(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> HUGE_WATER_LILY = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":huge_water_lily", 
			new TFGenHugeWaterLily(NoneFeatureConfiguration.CODEC));
	public static final Feature<BlockStateConfiguration> LAMPPOSTS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":lampposts", 
			new TFGenLampposts(BlockStateConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> MINERS_TREE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":miners_tree", 
			new TFGenMinersTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> MONOLITH = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":monolith", 
			new TFGenMonolith(NoneFeatureConfiguration.CODEC));
	public static final Feature<DiskConfiguration> MYCELIUM_BLOB = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":mycelium_blob", 
			new BaseDiskFeature(DiskConfiguration.CODEC));
	public static final Feature<CaveStalactiteConfig> OUTSIDE_STALAGMITE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":outside_stalagmite", 
			new TFGenOutsideStalagmite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final Feature<NoneFeatureConfiguration> PLANT_ROOTS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":plant_roots", 
			new TFGenPlantRoots(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> STONE_CIRCLE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":stone_circle", 
			new StoneCircleFeature(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> THORNS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":thorns", 
			new TFGenThorns(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> TORCH_BERRIES = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":torch_berries", 
			new TFGenTorchBerries(NoneFeatureConfiguration.CODEC));
	public static final Feature<TFTreeFeatureConfig> TREE_OF_TIME = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":tree_of_time", 
			new TFGenTreeOfTime(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Feature<NoneFeatureConfiguration> TROLL_ROOTS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":troll_roots", 
			new TFGenTrollRoots(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> WEBS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":webs", 
			new TFGenWebs(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> WELL = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":well", 
			new TFGenWell(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> WOOD_ROOTS = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":wood_roots", 
			new TFGenWoodRoots(NoneFeatureConfiguration.CODEC));
	public static final Feature<NoneFeatureConfiguration> SNOW_UNDER_TREES = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":snow_under_trees", 
			new SnowUnderTrees(NoneFeatureConfiguration.CODEC));
	public static final Feature<TreeConfiguration> SNOW_TREE = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":anywhere_tree", 
			new SnowTreePlacer(TreeConfiguration.CODEC));
	public static final Feature<RandomPatchConfiguration> DARK_FOREST_PLACER = Registry.register(Registry.FEATURE, TwilightForestMod.ID+ ":dark_forest_placer", 
			new TFGenDarkForestFeature(RandomPatchConfiguration.CODEC));
}
