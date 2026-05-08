package twilightforest.init;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TFStructureSets {
    public static final ResourceKey<StructureSet> FALLEN_TRUNK = key("fallen_trunk");
    public static final ResourceKey<StructureSet> HOLLOW_TREE = key("hollow_tree");
    public static final ResourceKey<StructureSet> CAMP = key("camp");
    public static final ResourceKey<StructureSet> HEDGE_MAZE = key("hedge_maze");
    public static final ResourceKey<StructureSet> QUEST_GROVE = key("quest_grove");
    public static final ResourceKey<StructureSet> HOLLOW_HILL_SMALL = key("small_hollow_hill");
    public static final ResourceKey<StructureSet> HOLLOW_HILL_MEDIUM = key("medium_hollow_hill");
    public static final ResourceKey<StructureSet> HOLLOW_HILL_LARGE = key("large_hollow_hill");
    public static final ResourceKey<StructureSet> NAGA_COURTYARD = key("naga_courtyard");
    public static final ResourceKey<StructureSet> LICH_TOWER = key("lich_tower");
    public static final ResourceKey<StructureSet> LABYRINTH = key("labyrinth");
    public static final ResourceKey<StructureSet> HYDRA_LAIR = key("hydra_lair");
    public static final ResourceKey<StructureSet> KNIGHT_STRONGHOLD = key("knight_stronghold");
    public static final ResourceKey<StructureSet> DARK_TOWER = key("dark_tower");
    public static final ResourceKey<StructureSet> YETI_CAVE = key("yeti_cave");
    public static final ResourceKey<StructureSet> AURORA_PALACE = key("aurora_palace");
    public static final ResourceKey<StructureSet> TROLL_CAVE = key("troll_cave");
    public static final ResourceKey<StructureSet> GIANT_HOUSE = key("giant_house");
    public static final ResourceKey<StructureSet> FINAL_CASTLE = key("final_castle");

    public static final ResourceKey<StructureSet> MUSHROOM_TOWER = key("mushroom_tower");
    public static final ResourceKey<StructureSet> QUEST_ISLAND = key("quest_island");
    public static final ResourceKey<StructureSet> DRUID_GROVE = key("druid_grove");
    public static final ResourceKey<StructureSet> FLOATING_RUINS = key("floating_ruins");
    public static final ResourceKey<StructureSet> WORLD_TREE = key("world_tree");

    private TFStructureSets() {
    }

    private static ResourceKey<StructureSet> key(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, TwilightForestMod.prefix(name));
    }

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);

        List<StructureSet.StructureSelectionEntry> hollowTrees = List.of(
            new StructureSet.StructureSelectionEntry(structures.getOrThrow(TFStructures.HOLLOW_TREE), 1),
            new StructureSet.StructureSelectionEntry(structures.getOrThrow(TFStructures.SWAMP_HOLLOW_TREE), 1)
        );
        Holder.Reference<StructureSet> fallenTrunk = context.register(FALLEN_TRUNK, new StructureSet(
            structures.getOrThrow(TFStructures.FALLEN_TRUNK),
            new AvoidLandmarkGridPlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 0.8F, 1275623845,
                Optional.of(new StructurePlacement.ExclusionZone(structureSets.getOrThrow(HOLLOW_TREE), 1)), 7, 5,
                RandomSpreadType.TRIANGULAR, Optional.empty())));
        Holder.Reference<StructureSet> hollowTree = context.register(HOLLOW_TREE, new StructureSet(
            hollowTrees,
            new AvoidLandmarkGridPlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 0.5F, 34481210,
                Optional.empty(), 7, 5, RandomSpreadType.TRIANGULAR, Optional.empty())));

        Optional<AvoidLandmarkGridPlacement.AvoidAdditionalStructures> avoidTrees = Optional.of(
            new AvoidLandmarkGridPlacement.AvoidAdditionalStructures(new Object2IntArrayMap<>(Map.of(
                fallenTrunk, 2,
                hollowTree, 1
            ))));
        context.register(CAMP, new StructureSet(structures.getOrThrow(TFStructures.CAMP),
            new AvoidLandmarkGridPlacement(18, 14, RandomSpreadType.TRIANGULAR, 701432212, avoidTrees)));

        context.register(HEDGE_MAZE, landmark(structures, TFStructures.HEDGE_MAZE));
        context.register(HOLLOW_HILL_SMALL, landmark(structures, TFStructures.HOLLOW_HILL_SMALL));
        context.register(HOLLOW_HILL_MEDIUM, landmark(structures, TFStructures.HOLLOW_HILL_MEDIUM));
        context.register(HOLLOW_HILL_LARGE, landmark(structures, TFStructures.HOLLOW_HILL_LARGE));
        context.register(NAGA_COURTYARD, landmark(structures, TFStructures.NAGA_COURTYARD));
        context.register(LICH_TOWER, landmark(structures, TFStructures.LICH_TOWER));

        context.register(QUEST_GROVE, center(structures, TFStructures.QUEST_GROVE));
        context.register(LABYRINTH, center(structures, TFStructures.LABYRINTH));
        context.register(HYDRA_LAIR, center(structures, TFStructures.HYDRA_LAIR));
        context.register(KNIGHT_STRONGHOLD, center(structures, TFStructures.KNIGHT_STRONGHOLD));
        context.register(DARK_TOWER, center(structures, TFStructures.DARK_TOWER));
        context.register(YETI_CAVE, center(structures, TFStructures.YETI_CAVE));
        context.register(AURORA_PALACE, center(structures, TFStructures.AURORA_PALACE));
        context.register(TROLL_CAVE, center(structures, TFStructures.TROLL_CAVE));
        context.register(GIANT_HOUSE, center(structures, TFStructures.GIANT_HOUSE));
        context.register(FINAL_CASTLE, center(structures, TFStructures.FINAL_CASTLE));
    }

    private static StructureSet landmark(HolderGetter<Structure> structures, ResourceKey<Structure> structure) {
        return new StructureSet(structures.getOrThrow(structure), new LandmarkGridPlacement(Optional.of(structure)));
    }

    private static StructureSet center(HolderGetter<Structure> structures, ResourceKey<Structure> structure) {
        return new StructureSet(structures.getOrThrow(structure), LandmarkGridPlacement.forceStructureForCenters());
    }
}
