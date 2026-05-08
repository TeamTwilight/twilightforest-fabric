package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import twilightforest.TwilightForestMod;

public final class ItemTagGenerator {
    public static final TagKey<Item> BOAR_TEMPT_ITEMS = create("boar_tempt_items");
    public static final TagKey<Item> DEER_TEMPT_ITEMS = create("deer_tempt_items");
    public static final TagKey<Item> RAVEN_TEMPT_ITEMS = create("raven_tempt_items");
    public static final TagKey<Item> TINY_BIRD_TEMPT_ITEMS = create("tiny_bird_tempt_items");
    public static final TagKey<Item> DWARF_RABBIT_TEMPT_ITEMS = create("dwarf_rabbit_tempt_items");
    public static final TagKey<Item> PENGUIN_TEMPT_ITEMS = create("penguin_tempt_items");
    public static final TagKey<Item> SQUIRREL_TEMPT_ITEMS = create("squirrel_tempt_items");
    public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = create("banned_uncrafting_ingredients");
    public static final TagKey<Item> BANNED_UNCRAFTABLES = create("banned_uncraftables");
    public static final TagKey<Item> UNCRAFTING_IGNORES_COST = create("uncrafting_ignores_cost");
    public static final TagKey<Item> IMMUNE_TO_THORNS = create("immune_to_thorns");
    public static final TagKey<Item> SCEPTERS = create("scepters");
    public static final TagKey<Item> BANISTERS = create("banisters");
    public static final TagKey<Item> BLOCK_AND_CHAIN_ENCHANTABLE = create("block_and_chain_enchantable");
    public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = create("kobold_pacification_breads");
    public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");
    public static final TagKey<Item> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
    public static final TagKey<Item> CANOPY_LOGS = create("canopy_logs");
    public static final TagKey<Item> MANGROVE_LOGS = create("mangrove_logs");
    public static final TagKey<Item> DARKWOOD_LOGS = create("darkwood_logs");
    public static final TagKey<Item> TIME_LOGS = create("timewood_logs");
    public static final TagKey<Item> TRANSFORMATION_LOGS = create("transwood_logs");
    public static final TagKey<Item> MINING_LOGS = create("mining_logs");
    public static final TagKey<Item> SORTING_LOGS = create("sortwood_logs");
    public static final TagKey<Item> TOWERWOOD = create("towerwood");
    public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
    public static final TagKey<Item> PAPER = common("paper");
    public static final TagKey<Item> CARMINITE_GEMS = common("gems/carminite");
    public static final TagKey<Item> COPPER_NUGGETS = common("nuggets/copper");
    public static final TagKey<Item> FIERY_INGOTS = common("ingots/fiery");
    public static final TagKey<Item> IRONWOOD_INGOTS = common("ingots/ironwood");
    public static final TagKey<Item> KNIGHTMETAL_INGOTS = common("ingots/knightmetal");
    public static final TagKey<Item> STEELEAF_INGOTS = common("ingots/steeleaf");
    public static final TagKey<Item> WROUGHT_IRON_INGOTS = common("ingots/wrought_iron");
    public static final TagKey<Item> STORAGE_BLOCKS_FIERY = common("storage_blocks/fiery");
    public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = common("storage_blocks/ironwood");
    public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = common("storage_blocks/knightmetal");
    public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = common("storage_blocks/steeleaf");
    public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = common("storage_blocks/arctic_fur");
    public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = common("storage_blocks/carminite");
    public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = common("raw_materials/ironwood");
    public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = common("raw_materials/knightmetal");
    public static final TagKey<Item> WIP = create("wip");
    public static final TagKey<Item> REPAIRS_IRONWOOD_TOOLS = create("repairs_ironwood_tools");
    public static final TagKey<Item> REPAIRS_STEELEAF_TOOLS = create("repairs_steeleaf_tools");
    public static final TagKey<Item> REPAIRS_KNIGHTMETAL_TOOLS = create("repairs_knightmetal_tools");
    public static final TagKey<Item> REPAIRS_FIERY_TOOLS = create("repairs_fiery_tools");
    public static final TagKey<Item> REPAIRS_GIANT_TOOLS = create("repairs_giant_tools");
    public static final TagKey<Item> REPAIRS_ICE_TOOLS = create("repairs_ice_tools");
    public static final TagKey<Item> TRAVELLERS_BELT_BLACKLISTED = create("travellers_belt_blacklisted");
    public static final TagKey<Item> EMPERORS_CLOTH_APPLICABLE = create("emperors_cloth_applicable");
    public static final TagKey<Item> KEPT_ON_DEATH = create("kept_on_death");
    public static final TagKey<Item> PORTAL_ACTIVATOR = create("portal/activator");
    public static final TagKey<Item> DRYING_RACKS = create("drying_racks");
    public static final TagKey<Item> RENDER_LOWER_ON_DRYING_RACK = create("lower_on_drying_rack");
    public static final TagKey<Item> TWILIGHT_LOGS = create("logs");
    public static final TagKey<Item> FOODS_JERKY = common("foods/jerky");
    public static final TagKey<Item> TOOLS_SHIELD = common("tools/shield");
    public static final TagKey<Item> RODS_WOODEN = common("rods/wooden");
    public static final TagKey<Item> RODS_BLAZE = common("rods/blaze");
    public static final TagKey<Item> CHESTS_WOODEN = common("chests/wooden");
    public static final TagKey<Item> CHESTS_TRAPPED = common("chests/trapped");
    public static final TagKey<Item> BONES = common("bones");
    public static final TagKey<Item> DUSTS_GLOWSTONE = common("dusts/glowstone");
    public static final TagKey<Item> DUSTS_REDSTONE = common("dusts/redstone");
    public static final TagKey<Item> ENDER_PEARLS = common("ender_pearls");
    public static final TagKey<Item> GLASS_BLOCKS = common("glass_blocks");
    public static final TagKey<Item> GLASS_PANES_COLORLESS = common("glass_panes/colorless");
    public static final TagKey<Item> INGOTS_COPPER = common("ingots/copper");
    public static final TagKey<Item> INGOTS_GOLD = common("ingots/gold");
    public static final TagKey<Item> INGOTS_IRON = common("ingots/iron");
    public static final TagKey<Item> LEATHERS = common("leathers");
    public static final TagKey<Item> NUGGETS_GOLD = common("nuggets/gold");
    public static final TagKey<Item> NUGGETS_IRON = common("nuggets/iron");
    public static final TagKey<Item> ORES_REDSTONE = common("ores/redstone");
    public static final TagKey<Item> SLIME_BALLS = common("slime_balls");
    public static final TagKey<Item> STONE = common("stone");
    public static final TagKey<Item> STORAGE_BLOCKS_DIAMOND = common("storage_blocks/diamond");
    public static final TagKey<Item> STORAGE_BLOCKS_GOLD = common("storage_blocks/gold");
    public static final TagKey<Item> STORAGE_BLOCKS_IRON = common("storage_blocks/iron");
    public static final TagKey<Item> STRINGS = common("strings");

    private ItemTagGenerator() {
    }

    private static TagKey<Item> create(String path) {
        return TagKey.create(Registries.ITEM, TwilightForestMod.prefix(path));
    }

    private static TagKey<Item> common(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
