package twilightforest.util;

import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class TFRemapper {
	private static final Map<ResourceLocation, ResourceLocation> BLOCK_ALIASES = new LinkedHashMap<>();
	private static final Map<ResourceLocation, ResourceLocation> ITEM_ALIASES = new LinkedHashMap<>();
	private static final Map<ResourceLocation, ResourceLocation> ENTITY_ALIASES = new LinkedHashMap<>();
	private static final Map<ResourceLocation, ResourceLocation> STRUCTURE_PIECE_ALIASES = new LinkedHashMap<>();
	private static boolean bootstrapped;

	private TFRemapper() {
	}

	public static void addRegistryAliases() {
		if (bootstrapped) return;
		bootstrapped = true;

		aliasBlockItem("yeti_trophy", "alpha_yeti_trophy");
		aliasBlockItem("yeti_wall_trophy", "alpha_yeti_wall_trophy");
		aliasBlockItem("boss_spawner_naga", "naga_boss_spawner");
		aliasBlockItem("boss_spawner_lich", "lich_boss_spawner");
		aliasBlockItem("boss_spawner_minoshroom", "minoshroom_boss_spawner");
		aliasBlockItem("boss_spawner_hydra", "hydra_boss_spawner");
		aliasBlockItem("boss_spawner_knight_phantom", "knight_phantom_boss_spawner");
		aliasBlockItem("boss_spawner_ur_ghast", "ur_ghast_boss_spawner");
		aliasBlockItem("boss_spawner_alpha_yeti", "alpha_yeti_boss_spawner");
		aliasBlockItem("boss_spawner_snow_queen", "snow_queen_boss_spawner");
		aliasBlockItem("boss_spawner_final_boss", "final_boss_boss_spawner");

		aliasBlockItem("etched_nagastone_weathered", "cracked_etched_nagastone");
		aliasBlockItem("etched_nagastone_mossy", "mossy_etched_nagastone");
		aliasBlockItem("nagastone_pillar_weathered", "cracked_nagastone_pillar");
		aliasBlockItem("nagastone_pillar_mossy", "mossy_nagastone_pillar");
		aliasBlockItem("nagastone_stairs_weathered_left", "cracked_nagastone_stairs_left");
		aliasBlockItem("nagastone_stairs_mossy_left", "mossy_nagastone_stairs_left");
		aliasBlockItem("nagastone_stairs_weathered_right", "cracked_nagastone_stairs_right");
		aliasBlockItem("nagastone_stairs_mossy_right", "mossy_nagastone_stairs_right");
		aliasBlockItem("naga_stone_head", "nagastone_head");
		aliasBlockItem("naga_stone", "nagastone");

		aliasBlockItem("stone_twist", "twisted_stone");
		aliasBlockItem("stone_twist_thin", "twisted_stone_pillar");
		aliasBlockItem("stone_pillar_bold", "bold_stone_pillar");
		aliasBlockItem("empty_bookshelf", "empty_canopy_bookshelf");
		aliasBlockItem("royal_rags", "coronation_carpet");
		aliasBlockItem("cursed_spawner", "sinister_spawner");

		aliasBlockItem("huge_lilypad", "huge_lily_pad");
		aliasBlockItem("huge_waterlily", "huge_water_lily");
		aliasBlockItem("maze_stone", "mazestone");
		aliasBlockItem("maze_stone_brick", "mazestone_brick");
		aliasBlockItem("maze_stone_cracked", "cracked_mazestone");
		aliasBlockItem("maze_stone_mossy", "mossy_mazestone");
		aliasBlockItem("maze_stone_decorative", "decorative_mazestone");
		aliasBlockItem("maze_stone_chiseled", "cut_mazestone");
		aliasBlockItem("maze_stone_border", "mazestone_border");
		aliasBlockItem("maze_stone_mosaic", "mazestone_mosaic");
		aliasBlockItem("underbrick_cracked", "cracked_underbrick");
		aliasBlockItem("underbrick_mossy", "mossy_underbrick");
		aliasBlockItem("tower_wood", "towerwood");
		aliasBlockItem("tower_wood_cracked", "cracked_towerwood");
		aliasBlockItem("tower_wood_mossy", "mossy_towerwood");
		aliasBlockItem("tower_wood_infested", "infested_towerwood");
		aliasBlockItem("tower_wood_encased", "encased_towerwood");
		aliasBlockItem("deadrock_cracked", "cracked_deadrock");
		aliasBlockItem("deadrock_weathered", "weathered_deadrock");

		aliasBlockItem("castle_brick_worn", "worn_castle_brick");
		aliasBlockItem("castle_brick_cracked", "cracked_castle_brick");
		aliasBlockItem("castle_brick_mossy", "mossy_castle_brick");
		aliasBlockItem("castle_brick_frame", "thick_castle_brick");
		aliasBlockItem("castle_brick_roof", "castle_roof_tile");
		aliasBlockItem("castle_pillar_encased", "encased_castle_brick_pillar");
		aliasBlockItem("castle_pillar_encased_tile", "encased_castle_brick_tile");
		aliasBlockItem("castle_pillar_bold", "bold_castle_brick_pillar");
		aliasBlockItem("castle_pillar_bold_tile", "bold_castle_brick_tile");
		aliasBlockItem("castle_stairs_brick", "castle_brick_stairs");
		aliasBlockItem("castle_stairs_worn", "worn_castle_brick_stairs");
		aliasBlockItem("castle_stairs_cracked", "cracked_castle_brick_stairs");
		aliasBlockItem("castle_stairs_mossy", "mossy_castle_brick_stairs");
		aliasBlockItem("castle_stairs_encased", "encased_castle_brick_stairs");
		aliasBlockItem("castle_stairs_bold", "bold_castle_brick_stairs");
		aliasBlockItem("castle_rune_brick_pink", "pink_castle_rune_brick");
		aliasBlockItem("castle_rune_brick_yellow", "yellow_castle_rune_brick");
		aliasBlockItem("castle_rune_brick_blue", "blue_castle_rune_brick");
		aliasBlockItem("castle_rune_brick_purple", "violet_castle_rune_brick");
		aliasBlockItem("castle_door_pink", "pink_castle_door");
		aliasBlockItem("castle_door_yellow", "yellow_castle_door");
		aliasBlockItem("castle_door_blue", "blue_castle_door");
		aliasBlockItem("castle_door_purple", "violet_castle_door");
		aliasBlockItem("force_field_pink", "pink_force_field");
		aliasBlockItem("force_field_orange", "orange_force_field");
		aliasBlockItem("force_field_green", "green_force_field");
		aliasBlockItem("force_field_blue", "blue_force_field");
		aliasBlockItem("force_field_purple", "violet_force_field");

		aliasBlockItem("rainboak_leaves", "rainbow_oak_leaves");
		aliasBlockItem("rainboak_sapling", "rainbow_oak_sapling");
		aliasBlockItem("potted_rainboak_sapling", "potted_rainbow_oak_sapling");
		aliasBlockItem("dark_gate", "dark_fence_gate");
		aliasBlockItem("dark_plate", "dark_pressure_plate");
		aliasBlockItem("darkwood_sign", "dark_sign");
		aliasBlockItem("darkwood_wall_sign", "dark_wall_sign");
		aliasBlockItem("darkwood_banister", "dark_banister");

		aliasMagicWood("trans", "transformation");
		aliasMagicWood("mine", "mining");
		aliasMagicWood("sort", "sorting");

		aliasItem("shield_scepter", "fortification_scepter");
		aliasItem("magic_map", "filled_magic_map");
		aliasItem("maze_map", "filled_maze_map");
		aliasItem("ore_map", "filled_ore_map");
		aliasItem("magic_map_empty", "magic_map");
		aliasItem("maze_map_empty", "maze_map");
		aliasItem("ore_map_empty", "ore_map");
		aliasItem("ironwood_raw", "raw_ironwood");
		aliasItem("minotaur_axe_gold", "gold_minotaur_axe");
		aliasItem("minotaur_axe", "diamond_minotaur_axe");
		aliasItem("peacock_fan", "peacock_feather_fan");
		aliasItem("alpha_fur", "alpha_yeti_fur");
		aliasItem("questing_ram_banner_pattern", "quest_ram_banner_pattern");
		aliasItem("travellers_chest", "travellers_vest");
		aliasItem("bunny_spawn_egg", "dwarf_rabbit_spawn_egg");
		aliasItem("goblin_knight_lower_spawn_egg", "lower_goblin_knight_spawn_egg");
		aliasItem("mini_ghast_spawn_egg", "carminite_ghastling_spawn_egg");
		aliasItem("tower_ghast_spawn_egg", "carminite_ghastguard_spawn_egg");
		aliasItem("tower_golem_spawn_egg", "carminite_golem_spawn_egg");
		aliasItem("tower_broodling_spawn_egg", "carminite_broodling_spawn_egg");
		aliasItem("tower_termite_spawn_egg", "towerwood_borer_spawn_egg");
		aliasItem("wild_boar_spawn_egg", "boar_spawn_egg");
		aliasItem("yeti_alpha_spawn_egg", "alpha_yeti_spawn_egg");

		aliasEntity("wild_boar", "boar");
		aliasEntity("bunny", "dwarf_rabbit");
		aliasEntity("mini_ghast", "carminite_ghastling");
		aliasEntity("tower_ghast", "carminite_ghastguard");
		aliasEntity("tower_golem", "carminite_golem");
		aliasEntity("tower_broodling", "carminite_broodling");
		aliasEntity("tower_termite", "towerwood_borer");
		aliasEntity("goblin_knight_upper", "upper_goblin_knight");
		aliasEntity("goblin_knight_lower", "lower_goblin_knight");
		aliasEntity("yeti_alpha", "alpha_yeti");

		aliasPiece("TFNCTr", "TFNCTe");
		aliasPiece("TFNCDu", "TFNCTe");
		aliasPiece("TFNCSt", "TFNCTe");
	}

	public static ResourceLocation remapBlock(ResourceLocation id) {
		return BLOCK_ALIASES.getOrDefault(id, id);
	}

	public static ResourceLocation remapItem(ResourceLocation id) {
		return ITEM_ALIASES.getOrDefault(id, id);
	}

	public static ResourceLocation remapEntity(ResourceLocation id) {
		return ENTITY_ALIASES.getOrDefault(id, id);
	}

	public static ResourceLocation remapStructurePiece(ResourceLocation id) {
		return STRUCTURE_PIECE_ALIASES.getOrDefault(id, id);
	}

	public static Map<ResourceLocation, ResourceLocation> blockAliases() {
		return Collections.unmodifiableMap(BLOCK_ALIASES);
	}

	public static Map<ResourceLocation, ResourceLocation> itemAliases() {
		return Collections.unmodifiableMap(ITEM_ALIASES);
	}

	public static Map<ResourceLocation, ResourceLocation> entityAliases() {
		return Collections.unmodifiableMap(ENTITY_ALIASES);
	}

	public static Map<ResourceLocation, ResourceLocation> structurePieceAliases() {
		return Collections.unmodifiableMap(STRUCTURE_PIECE_ALIASES);
	}

	private static void aliasMagicWood(String oldPrefix, String newPrefix) {
		aliasBlockItem(oldPrefix + "_planks", newPrefix + "_planks");
		aliasBlockItem(oldPrefix + "_slab", newPrefix + "_slab");
		aliasBlockItem(oldPrefix + "_stairs", newPrefix + "_stairs");
		aliasBlockItem(oldPrefix + "_button", newPrefix + "_button");
		aliasBlockItem(oldPrefix + "_fence", newPrefix + "_fence");
		aliasBlockItem(oldPrefix + "_gate", newPrefix + "_fence_gate");
		aliasBlockItem(oldPrefix + "_plate", newPrefix + "_pressure_plate");
		aliasBlockItem(oldPrefix + "_door", newPrefix + "_door");
		aliasBlockItem(oldPrefix + "_trapdoor", newPrefix + "_trapdoor");
		aliasBlockItem(oldPrefix + "_sign", newPrefix + "_sign");
		aliasBlockItem(oldPrefix + "_wall_sign", newPrefix + "_wall_sign");
		aliasBlockItem(oldPrefix + "_banister", newPrefix + "_banister");
	}

	private static void aliasBlockItem(String oldId, String newId) {
		alias(BLOCK_ALIASES, oldId, newId);
		alias(ITEM_ALIASES, oldId, newId);
	}

	private static void aliasItem(String oldId, String newId) {
		alias(ITEM_ALIASES, oldId, newId);
	}

	private static void aliasEntity(String oldId, String newId) {
		alias(ENTITY_ALIASES, oldId, newId);
	}

	private static void aliasPiece(String oldId, String newId) {
		alias(STRUCTURE_PIECE_ALIASES, oldId, newId);
	}

	private static void alias(Map<ResourceLocation, ResourceLocation> map, String oldId, String newId) {
		map.put(TwilightForestMod.prefix(oldId.toLowerCase(Locale.ROOT)), TwilightForestMod.prefix(newId.toLowerCase(Locale.ROOT)));
	}
}
