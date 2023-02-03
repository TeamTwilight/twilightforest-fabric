package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.start.LegacyLandmark;

public class TFStructures {

	public static final ResourceKey<Structure> HEDGE_MAZE = registerKey("hedge_maze");
	public static final ResourceKey<Structure> QUEST_GROVE = registerKey("quest_grove");
	public static final ResourceKey<Structure> MUSHROOM_TOWER = registerKey("mushroom_tower");
	public static final ResourceKey<Structure> HOLLOW_HILL_SMALL = registerKey("small_hollow_hill");
	public static final ResourceKey<Structure> HOLLOW_HILL_MEDIUM = registerKey("medium_hollow_hill");
	public static final ResourceKey<Structure> HOLLOW_HILL_LARGE = registerKey("large_hollow_hill");
	public static final ResourceKey<Structure> NAGA_COURTYARD = registerKey("naga_courtyard");
	public static final ResourceKey<Structure> LICH_TOWER = registerKey("lich_tower");
	public static final ResourceKey<Structure> LABYRINTH = registerKey("labyrinth");
	public static final ResourceKey<Structure> HYDRA_LAIR = registerKey("hydra_lair");
	public static final ResourceKey<Structure> KNIGHT_STRONGHOLD = registerKey("knight_stronghold");
	public static final ResourceKey<Structure> DARK_TOWER = registerKey("dark_tower");
	public static final ResourceKey<Structure> YETI_CAVE = registerKey("yeti_cave");
	public static final ResourceKey<Structure> AURORA_PALACE = registerKey("aurora_palace");
	public static final ResourceKey<Structure> TROLL_CAVE = registerKey("troll_cave");
	public static final ResourceKey<Structure> FINAL_CASTLE = registerKey("final_castle");

	public static ResourceKey<Structure> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE, TwilightForestMod.prefix(name));
	}

	@SuppressWarnings("deprecation")
	public static void bootstrap(BootstapContext<Structure> context) {
		context.register(HEDGE_MAZE, LegacyLandmark.extractLandmark(context, TFLandmark.HEDGE_MAZE));
		context.register(QUEST_GROVE, LegacyLandmark.extractLandmark(context, TFLandmark.QUEST_GROVE));
		context.register(MUSHROOM_TOWER, LegacyLandmark.extractLandmark(context, TFLandmark.MUSHROOM_TOWER));
		context.register(HOLLOW_HILL_SMALL, LegacyLandmark.extractLandmark(context, TFLandmark.SMALL_HILL));
		context.register(HOLLOW_HILL_MEDIUM, LegacyLandmark.extractLandmark(context, TFLandmark.MEDIUM_HILL));
		context.register(HOLLOW_HILL_LARGE, LegacyLandmark.extractLandmark(context, TFLandmark.LARGE_HILL));
		context.register(NAGA_COURTYARD, LegacyLandmark.extractLandmark(context, TFLandmark.NAGA_COURTYARD));
		context.register(LICH_TOWER, LegacyLandmark.extractLandmark(context, TFLandmark.LICH_TOWER));
		context.register(LABYRINTH, LegacyLandmark.extractLandmark(context, TFLandmark.LABYRINTH));
		context.register(HYDRA_LAIR, LegacyLandmark.extractLandmark(context, TFLandmark.HYDRA_LAIR));
		context.register(KNIGHT_STRONGHOLD, LegacyLandmark.extractLandmark(context, TFLandmark.KNIGHT_STRONGHOLD));
		context.register(DARK_TOWER, LegacyLandmark.extractLandmark(context, TFLandmark.DARK_TOWER));
		context.register(YETI_CAVE, LegacyLandmark.extractLandmark(context, TFLandmark.YETI_CAVE));
		context.register(AURORA_PALACE, LegacyLandmark.extractLandmark(context, TFLandmark.ICE_TOWER));
		context.register(TROLL_CAVE, LegacyLandmark.extractLandmark(context, TFLandmark.TROLL_CAVE));
		context.register(FINAL_CASTLE, LegacyLandmark.extractLandmark(context, TFLandmark.FINAL_CASTLE));
	}

	public static void register() {}
}
