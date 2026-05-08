package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;

public final class BlockTagGenerator {
    /** Boss-explosion grief blacklist — blocks tagged here resist Hydra mortar megaBlast etc. */
    public static final TagKey<Block> COMMON_PROTECTIONS = create("common_protections");
    public static final TagKey<Block> PORTAL_EDGE = create("portal/edge");
    public static final TagKey<Block> PORTAL_POOL = create("portal/fluid");
    public static final TagKey<Block> PORTAL_DECO = create("portal/decoration");
    public static final TagKey<Block> GENERATED_PORTAL_DECO = create("portal/generated_decoration");
    public static final TagKey<Block> CASTLE_BLOCKS = create("castle_blocks");
    public static final TagKey<Block> CLOUDS = create("clouds");
    public static final TagKey<Block> CANNOT_TROLL_CAVE_HOLLOW = create("cannot_troll_cave_hollow");
    public static final TagKey<Block> DARK_TOWER_ALLOWED_POTS = create("dark_tower_allowed_pots");
    public static final TagKey<Block> DRUID_PROJECTILE_REPLACEABLE = create("druid_projectile_replaceable");
    public static final TagKey<Block> ROOT_TRACE_SKIP = create("tree_roots_skip");
    public static final TagKey<Block> PLANTS_HANG_ON = create("plants_hang_on");
    public static final TagKey<Block> SUPPORTS_STALAGMITES = create("supports_stalagmites");
    public static final TagKey<Block> WORLDGEN_REPLACEABLES = create("worldgen_replaceables");
    public static final TagKey<Block> CARVER_REPLACEABLES = create("carver_replaceables");
    public static final TagKey<Block> TF_BERRY_BUSHES_REPLACE = create("tf_berry_bushes_replace");
    public static final TagKey<Block> TF_BERRY_BUSHES_SURVIVE = create("tf_berry_bushes_survive");
    public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_SURVIVE = create("dark_tower_berry_bushes_survive");
    public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_DIE = create("dark_tower_berry_bushes_die");
    public static final TagKey<Block> SMALL_LAKES_DONT_REPLACE = create("small_lakes_dont_replace");
    public static final TagKey<Block> ICE_BOMB_REPLACEABLES = create("ice_bomb_replaceables");
    public static final TagKey<Block> PENGUINS_SPAWNABLE_ON = create("penguins_spawnable_on");
    public static final TagKey<Block> GIANTS_SPAWNABLE_ON = create("giants_spawnable_on");
    public static final TagKey<Block> BANISTERS = create("banisters");
    public static final TagKey<Block> TF_CHESTS = create("chests");
    public static final TagKey<Block> DRYING_RACKS = create("drying_racks");
    public static final TagKey<Block> HOLLOW_LOGS = create("hollow_logs");
    public static final TagKey<Block> TOWERWOOD = create("towerwood");
    public static final TagKey<Block> OREBERRY_BUSHES_SURVIVE = create("oreberry_bushes_survive");
    public static final TagKey<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = create("ore_magnet/ore_safe_replace_block");
    public static final TagKey<Block> TIME_CORE_EXCLUDED = create("time_core_excluded");
    public static final TagKey<Block> TROPHY_PEDESTAL_ACTIVATION_BLOCKS = create("trophy_pedestal_activation_blocks");
    public static final TagKey<Block> FIRE_JET_FUEL = create("fire_jet_fuel");
    public static final TagKey<Block> MAZESTONE = create("mazestone");
    public static final TagKey<Block> MAZEBREAKER_ACCELERATED = create("mazebreaker_accelerated_mining");
    public static final TagKey<Block> ANTIBUILDER_IGNORES = create("antibuilder_ignores");
    public static final TagKey<Block> CARMINITE_REACTOR_IMMUNE = create("carminite_reactor_immune");
    public static final TagKey<Block> CARMINITE_REACTOR_ORES = create("carminite_reactor_ores");
    public static final TagKey<Block> BLOCK_AND_CHAIN_NEVER_BREAKS = create("block_and_chain_never_breaks");
    public static final TagKey<Block> MINEABLE_WITH_BLOCK_AND_CHAIN = create("mineable_with_block_and_chain");
    public static final TagKey<Block> STRUCTURE_BANNED_INTERACTIONS = create("structure_banned_interactions");
    public static final TagKey<Block> PROGRESSION_ALLOW_BREAKING = create("progression_allow_breaking");
    public static final TagKey<Block> ORE_METER_TARGETABLE = create("ore_meter_targetable");
    public static final TagKey<Block> ORE_MAGNET_IGNORE = create("ore_magnet/ignored_ores");
    public static final TagKey<Block> MINING_CORE_EXCLUDED = create("mining_tree_excluded");
    public static final TagKey<Block> STORAGE_BLOCKS_FIERY = common("storage_blocks/fiery");
    public static final TagKey<Block> STORAGE_BLOCKS_IRONWOOD = common("storage_blocks/ironwood");
    public static final TagKey<Block> STORAGE_BLOCKS_KNIGHTMETAL = common("storage_blocks/knightmetal");
    public static final TagKey<Block> STORAGE_BLOCKS_STEELEAF = common("storage_blocks/steeleaf");
    public static final TagKey<Block> INCORRECT_FOR_IRONWOOD_TOOL = create("incorrect_for_ironwood_tool");
    public static final TagKey<Block> INCORRECT_FOR_FIERY_TOOL = create("incorrect_for_fiery_tool");
    public static final TagKey<Block> INCORRECT_FOR_STEELEAF_TOOL = create("incorrect_for_steeleaf_tool");
    public static final TagKey<Block> INCORRECT_FOR_KNIGHTMETAL_TOOL = create("incorrect_for_knightmetal_tool");
    public static final TagKey<Block> INCORRECT_FOR_GIANT_TOOL = create("incorrect_for_giant_tool");
    public static final TagKey<Block> INCORRECT_FOR_ICE_TOOL = create("incorrect_for_ice_tool");
    public static final TagKey<Block> INCORRECT_FOR_GLASS_TOOL = create("incorrect_for_glass_tool");

    private BlockTagGenerator() {
    }

    private static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, TwilightForestMod.prefix(path));
    }

    private static TagKey<Block> common(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
