package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

import java.util.function.Supplier;

public final class TFFeatureModifiers {

	public static final LazyRegistrar<FoliagePlacerType<?>> FOLIAGE_PLACERS = LazyRegistrar.create(Registries.FOLIAGE_PLACER_TYPE, TwilightForestMod.ID);
	public static final LazyRegistrar<TreeDecoratorType<?>> TREE_DECORATORS = LazyRegistrar.create(Registries.TREE_DECORATOR_TYPE, TwilightForestMod.ID);
	public static final LazyRegistrar<PlacementModifierType<?>> PLACEMENT_MODIFIERS = LazyRegistrar.create(Registries.PLACEMENT_MODIFIER_TYPE, TwilightForestMod.ID);
	public static final LazyRegistrar<TrunkPlacerType<?>> TRUNK_PLACERS = LazyRegistrar.create(Registries.TRUNK_PLACER_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<TrunkPlacerType<BranchingTrunkPlacer>> TRUNK_BRANCHING = TRUNK_PLACERS.register("branching_trunk_placer", () -> new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
	public static final RegistryObject<TrunkPlacerType<TrunkRiser>> TRUNK_RISER = TRUNK_PLACERS.register("trunk_mover_upper", () -> new TrunkPlacerType<>(TrunkRiser.CODEC));

	public static final RegistryObject<FoliagePlacerType<LeafSpheroidFoliagePlacer>> FOLIAGE_SPHEROID = FOLIAGE_PLACERS.register("spheroid_foliage_placer", () -> new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC));

	public static final RegistryObject<TreeDecoratorType<TreeCorePlacer>> CORE_PLACER = TREE_DECORATORS.register("core_placer", () -> new TreeDecoratorType<>(TreeCorePlacer.CODEC));
	public static final RegistryObject<TreeDecoratorType<TrunkSideDecorator>> TRUNKSIDE_DECORATOR = TREE_DECORATORS.register("trunkside_decorator", () -> new TreeDecoratorType<>(TrunkSideDecorator.CODEC));
	public static final RegistryObject<TreeDecoratorType<TreeRootsDecorator>> TREE_ROOTS = TREE_DECORATORS.register("tree_roots", () -> new TreeDecoratorType<>(TreeRootsDecorator.CODEC));
	public static final RegistryObject<TreeDecoratorType<DangleFromTreeDecorator>> DANGLING_DECORATOR = TREE_DECORATORS.register("dangle_from_tree_decorator", () -> new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC));

	public static final RegistryObject<PlacementModifierType<AvoidLandmarkModifier>> NO_STRUCTURE = registerPlacer("no_structure", () -> () -> AvoidLandmarkModifier.CODEC);
	public static final RegistryObject<PlacementModifierType<ChunkCenterModifier>> CHUNK_CENTERER = registerPlacer("chunk_centerer", () -> () -> ChunkCenterModifier.CODEC);
	public static final RegistryObject<PlacementModifierType<ChunkBlanketingModifier>> CHUNK_BLANKETING = registerPlacer("chunk_blanketing", () -> () -> ChunkBlanketingModifier.CODEC);

	//goofy but needed
	private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> registerPlacer(String name, Supplier<PlacementModifierType<P>> factory) {
		return PLACEMENT_MODIFIERS.register(name, factory);
	}
}
