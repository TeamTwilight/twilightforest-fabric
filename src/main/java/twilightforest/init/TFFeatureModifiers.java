package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TFMain;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

public final class TFFeatureModifiers {

	public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = registerTrunkPlacerType("branching_trunk_placer", new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
	public static final TrunkPlacerType<TrunkRiser> TRUNK_RISER = registerTrunkPlacerType("trunk_mover_upper", new TrunkPlacerType<>(TrunkRiser.CODEC));

	public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = registerFoliagePlacerType("spheroid_foliage_placer", new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC));

	public static final TreeDecoratorType<TreeCorePlacer> CORE_PLACER = registerTreeDecoratorType("core_placer", new TreeDecoratorType<>(TreeCorePlacer.CODEC));
	public static final TreeDecoratorType<TrunkSideDecorator> TRUNKSIDE_DECORATOR = registerTreeDecoratorType("trunkside_decorator", new TreeDecoratorType<>(TrunkSideDecorator.CODEC));
	public static final TreeDecoratorType<TreeRootsDecorator> TREE_ROOTS = registerTreeDecoratorType("tree_roots", new TreeDecoratorType<>(TreeRootsDecorator.CODEC));
	public static final TreeDecoratorType<DangleFromTreeDecorator> DANGLING_DECORATOR = registerTreeDecoratorType("dangle_from_tree_decorator", new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC));

	public static final PlacementModifierType<AvoidLandmarkModifier> NO_STRUCTURE = registerPlacementModifierType("no_structure", () -> AvoidLandmarkModifier.CODEC);
	public static final PlacementModifierType<ChunkCenterModifier> CHUNK_CENTERER = registerPlacementModifierType("chunk_centerer", () -> ChunkCenterModifier.CODEC);
	public static final PlacementModifierType<ChunkBlanketingModifier> CHUNK_BLANKETING = registerPlacementModifierType("chunk_blanketing", () -> ChunkBlanketingModifier.CODEC);

	private static <T extends TrunkPlacer> TrunkPlacerType<T> registerTrunkPlacerType(String name, TrunkPlacerType<T> type) {
		return Registry.register(
			BuiltInRegistries.TRUNK_PLACER_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	private static <T extends FoliagePlacer> FoliagePlacerType<T> registerFoliagePlacerType(String name, FoliagePlacerType<T> type) {
		return Registry.register(
			BuiltInRegistries.FOLIAGE_PLACER_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	private static <T extends TreeDecorator> TreeDecoratorType<T> registerTreeDecoratorType(String name, TreeDecoratorType<T> type) {
		return Registry.register(
			BuiltInRegistries.TREE_DECORATOR_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	private static <T extends PlacementModifier> PlacementModifierType<T> registerPlacementModifierType(String name, PlacementModifierType<T> type) {
		return Registry.register(
			BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
			TFMain.prefix(name),
			type
		);
	}
}