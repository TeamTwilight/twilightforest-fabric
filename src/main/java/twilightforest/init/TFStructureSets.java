package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;

public class TFStructureSets {

	public static final ResourceKey<StructureSet> HEDGE_MAZE = registerKey("hedge_maze");
	public static final ResourceKey<StructureSet> QUEST_GROVE = registerKey("quest_grove");
	public static final ResourceKey<StructureSet> MUSHROOM_TOWER = registerKey("mushroom_tower");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_SMALL = registerKey("small_hollow_hill");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_MEDIUM = registerKey("medium_hollow_hill");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_LARGE = registerKey("large_hollow_hill");
	public static final ResourceKey<StructureSet> NAGA_COURTYARD = registerKey("naga_courtyard");
	public static final ResourceKey<StructureSet> LICH_TOWER = registerKey("lich_tower");
	public static final ResourceKey<StructureSet> LABYRINTH = registerKey("labyrinth");
	public static final ResourceKey<StructureSet> HYDRA_LAIR = registerKey("hydra_lair");
	public static final ResourceKey<StructureSet> KNIGHT_STRONGHOLD = registerKey("knight_stronghold");
	public static final ResourceKey<StructureSet> DARK_TOWER = registerKey("dark_tower");
	public static final ResourceKey<StructureSet> YETI_CAVE = registerKey("yeti_cave");
	public static final ResourceKey<StructureSet> AURORA_PALACE = registerKey("aurora_palace");
	public static final ResourceKey<StructureSet> TROLL_CAVE = registerKey("troll_cave");
	public static final ResourceKey<StructureSet> FINAL_CASTLE = registerKey("final_castle");


	private static ResourceKey<StructureSet> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE_SET, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstapContext<StructureSet> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
		context.register(HEDGE_MAZE, new StructureSet(structures.getOrThrow(TFStructures.HEDGE_MAZE), new BiomeForcedLandmarkPlacement(TFLandmark.HEDGE_MAZE, 256)));
		context.register(QUEST_GROVE, new StructureSet(structures.getOrThrow(TFStructures.QUEST_GROVE), new BiomeForcedLandmarkPlacement(TFLandmark.QUEST_GROVE, 256)));
		context.register(MUSHROOM_TOWER, new StructureSet(structures.getOrThrow(TFStructures.MUSHROOM_TOWER), new BiomeForcedLandmarkPlacement(TFLandmark.MUSHROOM_TOWER, 256)));
		context.register(HOLLOW_HILL_SMALL, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_SMALL), new BiomeForcedLandmarkPlacement(TFLandmark.SMALL_HILL, 256)));
		context.register(HOLLOW_HILL_MEDIUM, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_MEDIUM), new BiomeForcedLandmarkPlacement(TFLandmark.MEDIUM_HILL, 256)));
		context.register(HOLLOW_HILL_LARGE, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_LARGE), new BiomeForcedLandmarkPlacement(TFLandmark.LARGE_HILL, 256)));
		context.register(NAGA_COURTYARD, new StructureSet(structures.getOrThrow(TFStructures.NAGA_COURTYARD), new BiomeForcedLandmarkPlacement(TFLandmark.NAGA_COURTYARD, 256)));
		context.register(LICH_TOWER, new StructureSet(structures.getOrThrow(TFStructures.LICH_TOWER), new BiomeForcedLandmarkPlacement(TFLandmark.LICH_TOWER, 256)));
		context.register(LABYRINTH, new StructureSet(structures.getOrThrow(TFStructures.LABYRINTH), new BiomeForcedLandmarkPlacement(TFLandmark.LABYRINTH, 256)));
		context.register(HYDRA_LAIR, new StructureSet(structures.getOrThrow(TFStructures.HYDRA_LAIR), new BiomeForcedLandmarkPlacement(TFLandmark.HYDRA_LAIR, 256)));
		context.register(KNIGHT_STRONGHOLD, new StructureSet(structures.getOrThrow(TFStructures.KNIGHT_STRONGHOLD), new BiomeForcedLandmarkPlacement(TFLandmark.KNIGHT_STRONGHOLD, 256)));
		context.register(DARK_TOWER, new StructureSet(structures.getOrThrow(TFStructures.DARK_TOWER), new BiomeForcedLandmarkPlacement(TFLandmark.DARK_TOWER, 256)));
		context.register(YETI_CAVE, new StructureSet(structures.getOrThrow(TFStructures.YETI_CAVE), new BiomeForcedLandmarkPlacement(TFLandmark.YETI_CAVE, 256)));
		context.register(AURORA_PALACE, new StructureSet(structures.getOrThrow(TFStructures.AURORA_PALACE), new BiomeForcedLandmarkPlacement(TFLandmark.ICE_TOWER, 256)));
		context.register(TROLL_CAVE, new StructureSet(structures.getOrThrow(TFStructures.TROLL_CAVE), new BiomeForcedLandmarkPlacement(TFLandmark.TROLL_CAVE, 256)));
		context.register(FINAL_CASTLE, new StructureSet(structures.getOrThrow(TFStructures.FINAL_CASTLE), new BiomeForcedLandmarkPlacement(TFLandmark.FINAL_CASTLE, 256)));
	}
}
