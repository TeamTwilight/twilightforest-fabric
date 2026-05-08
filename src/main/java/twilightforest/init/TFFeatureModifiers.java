package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.BranchingTrunkPlacer;
import twilightforest.world.components.feature.trees.treeplacers.DangleFromTreeDecorator;
import twilightforest.world.components.feature.trees.treeplacers.LeafSpheroidFoliagePlacer;
import twilightforest.world.components.feature.trees.treeplacers.TreeCorePlacer;
import twilightforest.world.components.feature.trees.treeplacers.TreeRootsDecorator;
import twilightforest.world.components.feature.trees.treeplacers.TrunkRiser;
import twilightforest.world.components.feature.trees.treeplacers.TrunkSideDecorator;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

public final class TFFeatureModifiers {
    public static final TFRegistryObject<TrunkPlacerType<BranchingTrunkPlacer>> TRUNK_BRANCHING = trunkPlacer("branching_trunk_placer", new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
    public static final TFRegistryObject<TrunkPlacerType<TrunkRiser>> TRUNK_RISER = trunkPlacer("trunk_mover_upper", new TrunkPlacerType<>(TrunkRiser.CODEC));

    public static final TFRegistryObject<FoliagePlacerType<LeafSpheroidFoliagePlacer>> FOLIAGE_SPHEROID = foliagePlacer("spheroid_foliage_placer", new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC));

    public static final TFRegistryObject<TreeDecoratorType<TreeCorePlacer>> CORE_PLACER = treeDecorator("core_placer", new TreeDecoratorType<>(TreeCorePlacer.CODEC));
    public static final TFRegistryObject<TreeDecoratorType<TrunkSideDecorator>> TRUNKSIDE_DECORATOR = treeDecorator("trunkside_decorator", new TreeDecoratorType<>(TrunkSideDecorator.CODEC));
    public static final TFRegistryObject<TreeDecoratorType<TreeRootsDecorator>> TREE_ROOTS = treeDecorator("tree_roots", new TreeDecoratorType<>(TreeRootsDecorator.CODEC));
    public static final TFRegistryObject<TreeDecoratorType<DangleFromTreeDecorator>> DANGLING_DECORATOR = treeDecorator("dangle_from_tree_decorator", new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC));

    public static final TFRegistryObject<PlacementModifierType<AvoidLandmarkModifier>> NO_STRUCTURE = placementModifier("no_structure", () -> AvoidLandmarkModifier.CODEC);
    public static final TFRegistryObject<PlacementModifierType<ChunkCenterModifier>> CHUNK_CENTERER = placementModifier("chunk_centerer", () -> ChunkCenterModifier.CODEC);
    public static final TFRegistryObject<PlacementModifierType<ChunkBlanketingModifier>> CHUNK_BLANKETING = placementModifier("chunk_blanketing", () -> ChunkBlanketingModifier.CODEC);

    private TFFeatureModifiers() {
    }

    public static void bootstrap() {
        TRUNK_BRANCHING.get();
        TRUNK_RISER.get();
        FOLIAGE_SPHEROID.get();
        CORE_PLACER.get();
        TRUNKSIDE_DECORATOR.get();
        TREE_ROOTS.get();
        DANGLING_DECORATOR.get();
        NO_STRUCTURE.get();
        CHUNK_CENTERER.get();
        CHUNK_BLANKETING.get();
    }

    private static <P extends TrunkPlacer> TFRegistryObject<TrunkPlacerType<P>> trunkPlacer(String path, TrunkPlacerType<P> type) {
        ResourceKey<TrunkPlacerType<?>> key = ResourceKey.create(BuiltInRegistries.TRUNK_PLACER_TYPE.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        TrunkPlacerType<P> registered = (TrunkPlacerType<P>) Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<TrunkPlacerType<P>> holder = new TFRegistryObject(registered, key);
        return holder;
    }

    private static <P extends FoliagePlacer> TFRegistryObject<FoliagePlacerType<P>> foliagePlacer(String path, FoliagePlacerType<P> type) {
        ResourceKey<FoliagePlacerType<?>> key = ResourceKey.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        FoliagePlacerType<P> registered = (FoliagePlacerType<P>) Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<FoliagePlacerType<P>> holder = new TFRegistryObject(registered, key);
        return holder;
    }

    private static <P extends TreeDecorator> TFRegistryObject<TreeDecoratorType<P>> treeDecorator(String path, TreeDecoratorType<P> type) {
        ResourceKey<TreeDecoratorType<?>> key = ResourceKey.create(BuiltInRegistries.TREE_DECORATOR_TYPE.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        TreeDecoratorType<P> registered = (TreeDecoratorType<P>) Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<TreeDecoratorType<P>> holder = new TFRegistryObject(registered, key);
        return holder;
    }

    private static <P extends PlacementModifier> TFRegistryObject<PlacementModifierType<P>> placementModifier(String path, PlacementModifierType<P> type) {
        ResourceKey<PlacementModifierType<?>> key = ResourceKey.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        PlacementModifierType<P> registered = (PlacementModifierType<P>) Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<PlacementModifierType<P>> holder = new TFRegistryObject(registered, key);
        return holder;
    }
}
