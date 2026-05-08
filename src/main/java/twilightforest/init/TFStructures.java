package twilightforest.init;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.world.components.structures.type.*;

public final class TFStructures {
    public static final ResourceKey<Structure> AURORA_PALACE = key("aurora_palace");
    public static final ResourceKey<Structure> CAMP = key("camp");
    public static final ResourceKey<Structure> DARK_TOWER = key("dark_tower");
    public static final ResourceKey<Structure> FALLEN_TRUNK = key("fallen_trunk");
    public static final ResourceKey<Structure> FINAL_CASTLE = key("final_castle");
    public static final ResourceKey<Structure> GIANT_HOUSE = key("giant_house");
    public static final ResourceKey<Structure> HEDGE_MAZE = key("hedge_maze");
    public static final ResourceKey<Structure> HOLLOW_HILL_SMALL = key("small_hollow_hill");
    public static final ResourceKey<Structure> HOLLOW_HILL_MEDIUM = key("medium_hollow_hill");
    public static final ResourceKey<Structure> HOLLOW_HILL_LARGE = key("large_hollow_hill");
    public static final ResourceKey<Structure> HOLLOW_TREE = key("hollow_tree");
    public static final ResourceKey<Structure> SWAMP_HOLLOW_TREE = key("swamp_hollow_tree");
    public static final ResourceKey<Structure> HYDRA_LAIR = key("hydra_lair");
    public static final ResourceKey<Structure> KNIGHT_STRONGHOLD = key("knight_stronghold");
    public static final ResourceKey<Structure> LABYRINTH = key("labyrinth");
    public static final ResourceKey<Structure> LICH_TOWER = key("lich_tower");
    public static final ResourceKey<Structure> MUSHROOM_TOWER = key("mushroom_tower");
    public static final ResourceKey<Structure> NAGA_COURTYARD = key("naga_courtyard");
    public static final ResourceKey<Structure> QUEST_GROVE = key("quest_grove");
    public static final ResourceKey<Structure> QUEST_ISLAND = key("quest_island");
    public static final ResourceKey<Structure> DRUID_GROVE = key("druid_grove");
    public static final ResourceKey<Structure> FLOATING_RUINS = key("floating_ruins");
    public static final ResourceKey<Structure> WORLD_TREE = key("world_tree");
    public static final ResourceKey<Structure> TROLL_CAVE = key("troll_cave");
    public static final ResourceKey<Structure> YETI_CAVE = key("yeti_cave");

    private TFStructures() {
    }

    private static ResourceKey<Structure> key(String path) {
        return ResourceKey.create(Registries.STRUCTURE, TwilightForestMod.prefix(path));
    }

    public static void bootstrap(BootstrapContext<Structure> context) {
        context.register(FALLEN_TRUNK, FallenTrunkStructure.buildStructureConfig(context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_TREE_BIOMES)));
        context.register(HOLLOW_TREE, HollowTreeStructure.buildStructureConfig(false, context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_TREE_BIOMES)));
        context.register(SWAMP_HOLLOW_TREE, HollowTreeStructure.buildStructureConfig(true, HolderSet.direct(context.lookup(Registries.BIOME).getOrThrow(TFBiomes.SWAMP))));
        context.register(CAMP, CampStructure.buildStructureConfig(context));
        context.register(HEDGE_MAZE, HedgeMazeStructure.buildStructureConfig(context));
        context.register(QUEST_GROVE, QuestGroveStructure.buildStructureConfig(context));
        context.register(HOLLOW_HILL_SMALL, HollowHillStructure.buildSmallHillConfig(context));
        context.register(HOLLOW_HILL_MEDIUM, HollowHillStructure.buildMediumHillConfig(context));
        context.register(HOLLOW_HILL_LARGE, HollowHillStructure.buildLargeHillConfig(context));
        context.register(NAGA_COURTYARD, NagaCourtyardStructure.buildStructureConfig(context));
        context.register(LICH_TOWER, LichTowerStructure.buildLichTowerConfig(context));
        context.register(LABYRINTH, LabyrinthStructure.buildLabyrinthConfig(context));
        context.register(HYDRA_LAIR, HydraLairStructure.buildHydraLairConfig(context));
        context.register(KNIGHT_STRONGHOLD, KnightStrongholdStructure.buildKnightStrongholdConfig(context));
        context.register(DARK_TOWER, DarkTowerStructure.buildDarkTowerConfig(context));
        context.register(YETI_CAVE, YetiCaveStructure.buildYetiCaveConfig(context));
        context.register(AURORA_PALACE, AuroraPalaceStructure.buildAuroraPalaceConfig(context));
        context.register(TROLL_CAVE, TrollCaveStructure.buildTrollCaveConfig(context));
        context.register(GIANT_HOUSE, GiantHouseStructure.buildGiantHouseConfig(context));
        context.register(FINAL_CASTLE, FinalCastleStructure.buildFinalCastleConfig(context));
        context.register(MUSHROOM_TOWER, MushroomTowerStructure.buildStructureConfig(context));
    }
}
