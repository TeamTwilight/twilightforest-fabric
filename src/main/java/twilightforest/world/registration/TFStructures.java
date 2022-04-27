package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.world.components.structures.courtyard.NagaCourtyardPieces;
import twilightforest.world.components.structures.darktower.DarkTowerPieces;
import twilightforest.world.components.structures.finalcastle.FinalCastlePieces;
import twilightforest.world.components.structures.icetower.IceTowerPieces;
import twilightforest.world.components.structures.lichtower.LichTowerPieces;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerRevampPieces;
import twilightforest.world.components.structures.minotaurmaze.MinotaurMazePieces;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerPieces;
import twilightforest.world.components.structures.start.LegacyStructureFeature;
import twilightforest.world.components.structures.stronghold.StrongholdPieces;
import twilightforest.world.components.structures.trollcave.TrollCavePieces;

@SuppressWarnings("deprecation")
public class TFStructures {

	public static final StructureFeature<NoneFeatureConfiguration> HEDGE_MAZE = new LegacyStructureFeature(TFFeature.HEDGE_MAZE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HEDGE_MAZE = HEDGE_MAZE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_HEDGE_MAZE_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_HEDGE_MAZE = createKey("hedge_maze");

	public static final StructureFeature<NoneFeatureConfiguration> QUEST_GROVE = new LegacyStructureFeature(TFFeature.QUEST_GROVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_QUEST_GROVE = QUEST_GROVE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_QUEST_GROVE = createKey("quest_grove");

	public static final StructureFeature<NoneFeatureConfiguration> MUSHROOM_TOWER = new LegacyStructureFeature(TFFeature.MUSHROOM_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_MUSHROOM_TOWER = MUSHROOM_TOWER.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_MUSHROOM_TOWER_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_MUSHROOM_TOWER = createKey("mushroom_tower");

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_SMALL = new LegacyStructureFeature(TFFeature.SMALL_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_SMALL = HOLLOW_HILL_SMALL.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_HOLLOW_HILL_SMALL = createKey("small_hollow_hill");

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_MEDIUM = new LegacyStructureFeature(TFFeature.MEDIUM_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_MEDIUM = HOLLOW_HILL_MEDIUM.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_HOLLOW_HILL_MEDIUM = createKey("medium_hollow_hill");

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_LARGE = new LegacyStructureFeature(TFFeature.LARGE_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_LARGE = HOLLOW_HILL_LARGE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_HOLLOW_HILL_LARGE = createKey("large_hollow_hill");

	public static final StructureFeature<NoneFeatureConfiguration> NAGA_COURTYARD = new LegacyStructureFeature(TFFeature.NAGA_COURTYARD);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_NAGA_COURTYARD = NAGA_COURTYARD.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_NAGA_COURTYARD_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_NAGA_COURTYARD = createKey("naga_courtyard");

	public static final StructureFeature<NoneFeatureConfiguration> LICH_TOWER = new LegacyStructureFeature(TFFeature.LICH_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_LICH_TOWER = LICH_TOWER.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_LICH_TOWER_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_LICH_TOWER = createKey("lich_tower");

	public static final StructureFeature<NoneFeatureConfiguration> LABYRINTH = new LegacyStructureFeature(TFFeature.LABYRINTH);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_LABYRINTH = LABYRINTH.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_LABYRINTH_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_LABYRINTH = createKey("labyrinth");

	public static final StructureFeature<NoneFeatureConfiguration> HYDRA_LAIR = new LegacyStructureFeature(TFFeature.HYDRA_LAIR);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HYDRA_LAIR = HYDRA_LAIR.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_HYDRA_LAIR_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_HYDRA_LAIR = createKey("hydra_lair");

	public static final StructureFeature<NoneFeatureConfiguration> KNIGHT_STRONGHOLD = new LegacyStructureFeature(TFFeature.KNIGHT_STRONGHOLD);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_KNIGHT_STRONGHOLD = KNIGHT_STRONGHOLD.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_KNIGHT_STRONGHOLD_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_KNIGHT_STRONGHOLD = createKey("knight_stronghold");

	public static final StructureFeature<NoneFeatureConfiguration> DARK_TOWER = new LegacyStructureFeature(TFFeature.DARK_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_DARK_TOWER = DARK_TOWER.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_DARK_TOWER_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_DARK_TOWER = createKey("dark_tower");

	public static final StructureFeature<NoneFeatureConfiguration> YETI_CAVE = new LegacyStructureFeature(TFFeature.YETI_CAVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_YETI_CAVE = YETI_CAVE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_YETI_CAVE_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_YETI_CAVE = createKey("yeti_lair");

	public static final StructureFeature<NoneFeatureConfiguration> AURORA_PALACE = new LegacyStructureFeature(TFFeature.ICE_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_AURORA_PALACE = AURORA_PALACE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_AURORA_PALACE_BIOMES);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_AURORA_PALACE = createKey("aurora_palace");

	public static final StructureFeature<NoneFeatureConfiguration> TROLL_CAVE = new LegacyStructureFeature(TFFeature.TROLL_CAVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_TROLL_CAVE = TROLL_CAVE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_TROLL_CAVE_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_TROLL_CAVE = createKey("troll_cave");

	public static final StructureFeature<NoneFeatureConfiguration> FINAL_CASTLE = new LegacyStructureFeature(TFFeature.FINAL_CASTLE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_FINAL_CASTLE = FINAL_CASTLE.configured(FeatureConfiguration.NONE, BiomeTagGenerator.VALID_FINAL_CASTLE_BIOMES, true);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> KEY_FINAL_CASTLE = createKey("final_castle");

	private static ResourceKey<ConfiguredStructureFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, TwilightForestMod.prefix(name));
	}

	public static void register() {
		TFFeature.init();
		new MushroomTowerPieces();
		new NagaCourtyardPieces();
		new LichTowerPieces();
		new LichTowerRevampPieces();
		new MinotaurMazePieces();
		new StrongholdPieces();
		new DarkTowerPieces();
		new IceTowerPieces();
		new TrollCavePieces();
		new FinalCastlePieces();

		register(HEDGE_MAZE, CONFIGURED_HEDGE_MAZE, TwilightForestMod.prefix("hedge_maze"), 0, 1);
		register(QUEST_GROVE, CONFIGURED_QUEST_GROVE, TwilightForestMod.prefix("quest_grove"), 0, 1);
		register(MUSHROOM_TOWER, CONFIGURED_MUSHROOM_TOWER, TwilightForestMod.prefix("mushroom_tower"), 0, 1);
		register(HOLLOW_HILL_SMALL, CONFIGURED_HOLLOW_HILL_SMALL, TwilightForestMod.prefix("hollow_hill_small"), 0, 1);
		register(HOLLOW_HILL_MEDIUM, CONFIGURED_HOLLOW_HILL_MEDIUM, TwilightForestMod.prefix("hollow_hill_medium"), 0, 1);
		register(HOLLOW_HILL_LARGE, CONFIGURED_HOLLOW_HILL_LARGE, TwilightForestMod.prefix("hollow_hill_large"), 0, 1);
		register(NAGA_COURTYARD, CONFIGURED_NAGA_COURTYARD, TwilightForestMod.prefix("courtyard"), 0, 1);
		register(LICH_TOWER, CONFIGURED_LICH_TOWER, TwilightForestMod.prefix("lich_tower"), 0, 1);
		register(LABYRINTH, CONFIGURED_LABYRINTH, TwilightForestMod.prefix("labyrinth"), 0, 1);
		register(HYDRA_LAIR, CONFIGURED_HYDRA_LAIR, TwilightForestMod.prefix("hydra_lair"), 0, 1);
		register(KNIGHT_STRONGHOLD, CONFIGURED_KNIGHT_STRONGHOLD, TwilightForestMod.prefix("knight_stronghold"), 0, 1);
		register(DARK_TOWER, CONFIGURED_DARK_TOWER, TwilightForestMod.prefix("dark_tower"), 0, 1);
		register(YETI_CAVE, CONFIGURED_YETI_CAVE, TwilightForestMod.prefix("yeti_cave"), 0, 1);
		register(AURORA_PALACE, CONFIGURED_AURORA_PALACE, TwilightForestMod.prefix("aurora_palace"), 0, 1);
		register(TROLL_CAVE, CONFIGURED_TROLL_CAVE, TwilightForestMod.prefix("troll_cave"), 0, 1);
		register(FINAL_CASTLE, CONFIGURED_FINAL_CASTLE, TwilightForestMod.prefix("final_castle"), 0, 1);

	}

	private static void register(StructureFeature<?> structure, ConfiguredStructureFeature<?, ?> config, ResourceLocation name, int min, int max) {
		Registry.register(Registry.STRUCTURE_FEATURE, name, structure);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(name.getNamespace(), "configured_".concat(name.getPath())), config);
	}
}
