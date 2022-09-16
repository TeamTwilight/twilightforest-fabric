package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.start.LegacyLandmark;

public class TFStructures {
	public static final LazyRegistrar<Structure> STRUCTURES = LazyRegistrar.create(Registry.STRUCTURE_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<LegacyLandmark> HEDGE_MAZE = STRUCTURES.register("hedge_maze", () -> LegacyLandmark.extractLandmark(TFLandmark.HEDGE_MAZE));
	public static final RegistryObject<LegacyLandmark> QUEST_GROVE = STRUCTURES.register("quest_grove", () -> LegacyLandmark.extractLandmark(TFLandmark.QUEST_GROVE));
	public static final RegistryObject<LegacyLandmark> MUSHROOM_TOWER = STRUCTURES.register("mushroom_tower", () -> LegacyLandmark.extractLandmark(TFLandmark.MUSHROOM_TOWER));
	public static final RegistryObject<LegacyLandmark> HOLLOW_HILL_SMALL = STRUCTURES.register("small_hollow_hill", () -> LegacyLandmark.extractLandmark(TFLandmark.SMALL_HILL));
	public static final RegistryObject<LegacyLandmark> HOLLOW_HILL_MEDIUM = STRUCTURES.register("medium_hollow_hill", () -> LegacyLandmark.extractLandmark(TFLandmark.MEDIUM_HILL));
	public static final RegistryObject<LegacyLandmark> HOLLOW_HILL_LARGE = STRUCTURES.register("large_hollow_hill", () -> LegacyLandmark.extractLandmark(TFLandmark.LARGE_HILL));
	public static final RegistryObject<LegacyLandmark> NAGA_COURTYARD = STRUCTURES.register("naga_courtyard", () -> LegacyLandmark.extractLandmark(TFLandmark.NAGA_COURTYARD));
	public static final RegistryObject<LegacyLandmark> LICH_TOWER = STRUCTURES.register("lich_tower", () -> LegacyLandmark.extractLandmark(TFLandmark.LICH_TOWER));
	public static final RegistryObject<LegacyLandmark> LABYRINTH = STRUCTURES.register("labyrinth", () -> LegacyLandmark.extractLandmark(TFLandmark.LABYRINTH));
	public static final RegistryObject<LegacyLandmark> HYDRA_LAIR = STRUCTURES.register("hydra_lair", () -> LegacyLandmark.extractLandmark(TFLandmark.HYDRA_LAIR));
	public static final RegistryObject<LegacyLandmark> KNIGHT_STRONGHOLD = STRUCTURES.register("knight_stronghold", () -> LegacyLandmark.extractLandmark(TFLandmark.KNIGHT_STRONGHOLD));
	public static final RegistryObject<LegacyLandmark> DARK_TOWER = STRUCTURES.register("dark_tower", () -> LegacyLandmark.extractLandmark(TFLandmark.DARK_TOWER));
	public static final RegistryObject<LegacyLandmark> YETI_CAVE = STRUCTURES.register("yeti_cave", () -> LegacyLandmark.extractLandmark(TFLandmark.YETI_CAVE));
	public static final RegistryObject<LegacyLandmark> AURORA_PALACE = STRUCTURES.register("aurora_palace", () -> LegacyLandmark.extractLandmark(TFLandmark.ICE_TOWER));
	public static final RegistryObject<LegacyLandmark> TROLL_CAVE = STRUCTURES.register("troll_cave", () -> LegacyLandmark.extractLandmark(TFLandmark.TROLL_CAVE));
	public static final RegistryObject<LegacyLandmark> FINAL_CASTLE = STRUCTURES.register("final_castle", () -> LegacyLandmark.extractLandmark(TFLandmark.FINAL_CASTLE));

	@SuppressWarnings("unchecked")
	public static ResourceKey<Structure> cleanKey(RegistryObject<? extends Structure> keyProvider) {
		return (ResourceKey<Structure>) keyProvider.getKey();
	}

	public static void register() {}
}
