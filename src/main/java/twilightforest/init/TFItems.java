package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.tags.TagKey;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.item.CodexArmorItem;
import twilightforest.item.CodexDiggerItem;
import twilightforest.item.CodexItem;
import twilightforest.item.CodexSwordItem;
import twilightforest.item.CharmOfKeepingItem;
import twilightforest.item.CharmOfLifeItem;
import twilightforest.item.CrumbleHornItem;
import twilightforest.item.CubeOfAnnihilationItem;
import twilightforest.item.CustomDamageSwordItem;
import twilightforest.item.FortificationWandItem;
import twilightforest.item.IceBombItem;
import twilightforest.item.LifedrainScepterItem;
import twilightforest.item.MagicBeansItem;
import twilightforest.item.MoonwormQueenItem;
import twilightforest.item.TransformPowderItem;
import twilightforest.item.ChainBlockItem;
import twilightforest.item.BrittleFlaskItem;
import twilightforest.item.EssenceBerryItem;
import twilightforest.item.ExanimateEssenceItem;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.EmptyMagicMapItem;
import twilightforest.item.EmptyMazeMapItem;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.FieryPickItem;
import twilightforest.item.FierySwordItem;
import twilightforest.item.GlassSwordItem;
import twilightforest.item.GiantPickItem;
import twilightforest.item.GiantSwordItem;
import twilightforest.item.GreaterFlaskItem;
import twilightforest.item.HydraChopItem;
import twilightforest.item.IceSwordItem;
import twilightforest.item.KnightmetalAxeItem;
import twilightforest.item.KnightmetalPickItem;
import twilightforest.item.KnightmetalShieldItem;
import twilightforest.item.KnightmetalSwordItem;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.MinotaurAxeItem;
import twilightforest.item.MoonDialItem;
import twilightforest.item.MysticCrownItem;
import twilightforest.item.StackableEffectItem;
import twilightforest.item.StackableEffectItem.StackableEffectInstance;
import twilightforest.item.OreMagnetItem;
import twilightforest.item.OreMeterItem;
import twilightforest.item.PeacockFanItem;
import twilightforest.item.PhantomArmorItem;
import twilightforest.item.PocketWatchItem;
import twilightforest.item.MagicPaintingItem;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.item.YetiArmorItem;
import twilightforest.item.EnderBowItem;
import twilightforest.item.IceBowItem;
import twilightforest.item.LampOfCindersItem;
import twilightforest.item.MagicMapItem;
import twilightforest.item.MazeMapItem;
import twilightforest.item.SeekerBowItem;
import twilightforest.item.TripleBowItem;
import twilightforest.item.TwilightWandItem;
import twilightforest.item.ZombieWandItem;
import twilightforest.item.ZombieScepterItem;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.util.TFToolMaterials;

/**
 * Real paired-client item registrations. For paired-client clients the
 * raw {@code twilightforest:*} item id syncs over and the paired client mod
 * supplies the official model. For vanilla clients each item falls back to a sensible
 * vanilla equivalent stamped with the matching CMD from {@link TFItemVisuals}.
 *
 * <p>Items registered here back the boss-drop loot tables, MythicMobs gear refs,
 * structure chest loot, and structure-decor item-frame contents that previously
 * dropped nothing. Adding a new item: pick its {@link TFItemVisuals} CMD, choose
 * a vanilla fallback (sword→iron_sword, helmet→iron_helmet, food→bread, etc.),
 * and use the shortest applicable helper below.</p>
 */
public final class TFItems {
    // ====== Resources (essences, ingots, materials) ======
    public static final TFRegistryObject<Item> BORER_ESSENCE = tfItem("borer_essence", new Item.Properties(), Items.BLAZE_POWDER);
    public static final TFRegistryObject<Item> CARMINITE = tfItem("carminite", new Item.Properties(), Items.REDSTONE);
    public static final TFRegistryObject<Item> LIVEROOT = tfItem("liveroot", new Item.Properties(), Items.STICK);
    public static final TFRegistryObject<Item> RAW_IRONWOOD = tfItem("raw_ironwood", new Item.Properties(), Items.RAW_IRON);
    public static final TFRegistryObject<Item> IRONWOOD_INGOT = tfItem("ironwood_ingot", new Item.Properties(), Items.IRON_INGOT);
    public static final TFRegistryObject<Item> STEELEAF_INGOT = tfItem("steeleaf_ingot", new Item.Properties(), Items.IRON_INGOT);
    public static final TFRegistryObject<Item> FIERY_INGOT = tfItem("fiery_ingot", rarity(Rarity.UNCOMMON).fireResistant(), Items.NETHERITE_INGOT);
    public static final TFRegistryObject<Item> FIERY_BLOOD = tfItem("fiery_blood", rarity(Rarity.UNCOMMON), Items.MAGMA_CREAM);
    public static final TFRegistryObject<Item> FIERY_TEARS = tfItem("fiery_tears", rarity(Rarity.UNCOMMON), Items.GHAST_TEAR);
    public static final TFRegistryObject<Item> KNIGHTMETAL_INGOT = tfItem("knightmetal_ingot", new Item.Properties(), Items.IRON_INGOT);
    public static final TFRegistryObject<Item> ARMOR_SHARD = tfItem("armor_shard", new Item.Properties(), Items.IRON_NUGGET);
    public static final TFRegistryObject<Item> ARMOR_SHARD_CLUSTER = tfItem("armor_shard_cluster", new Item.Properties(), Items.IRON_INGOT);
    public static final TFRegistryObject<Item> WROUGHT_IRON_BAR = tfItem("wrought_iron_bar", new Item.Properties(), Items.IRON_INGOT);
    public static final TFRegistryObject<Item> RAVEN_FEATHER = tfItem("raven_feather", new Item.Properties(), Items.FEATHER);
    public static final TFRegistryObject<Item> MAZE_SLIME_BALL = tfItem("maze_slime_ball", new Item.Properties(), Items.SLIME_BALL);
    public static final TFRegistryObject<Item> EXANIMATE_ESSENCE = exanimateEssenceItem("exanimate_essence", rarity(Rarity.UNCOMMON).stacksTo(16), Items.GLOW_INK_SAC);
    public static final TFRegistryObject<Item> TANNIN = tfItem("tannin", new Item.Properties().craftRemainder(Items.GLASS_BOTTLE), Items.GLASS_BOTTLE);
    public static final TFRegistryObject<Item> COPPER_NUGGET = tfItem("copper_nugget", new Item.Properties(), Items.COPPER_INGOT);
    public static final TFRegistryObject<Item> NAGA_SCALE = tfItem("naga_scale", rarity(Rarity.UNCOMMON), Items.PRISMARINE_SHARD);

    // ====== Spawn eggs referenced by JEED/effect-provider data ======
    public static final TFRegistryObject<Item> ALPHA_YETI_SPAWN_EGG = spawnEggItem("alpha_yeti_spawn_egg", TFEntities.ALPHA_YETI, 0xcdcdcd, 0x29486e);
    public static final TFRegistryObject<Item> DEATH_TOME_SPAWN_EGG = spawnEggItem("death_tome_spawn_egg", TFEntities.DEATH_TOME, 0x774e22, 0xdbcdbe);
    public static final TFRegistryObject<Item> MIST_WOLF_SPAWN_EGG = spawnEggItem("mist_wolf_spawn_egg", TFEntities.MIST_WOLF, 0x3a1411, 0xe2c88a);
    public static final TFRegistryObject<Item> MOSQUITO_SWARM_SPAWN_EGG = spawnEggItem("mosquito_swarm_spawn_egg", TFEntities.MOSQUITO_SWARM, 0x080904, 0x2d2f21);
    public static final TFRegistryObject<Item> SKELETON_DRUID_SPAWN_EGG = spawnEggItem("skeleton_druid_spawn_egg", TFEntities.SKELETON_DRUID, 0xa3a3a3, 0x2a3b17);

    // ====== Foods ======
    public static final TFRegistryObject<Item> RAW_VENISON = tfItem("raw_venison",
            food(2, 0.3F, false), Items.MUTTON);
    public static final TFRegistryObject<Item> COOKED_VENISON = tfItem("cooked_venison",
            food(6, 0.6F, false), Items.COOKED_MUTTON);
    public static final TFRegistryObject<Item> RAW_MEEF = tfItem("raw_meef",
            food(2, 0.2F, false), Items.BEEF);
    public static final TFRegistryObject<Item> COOKED_MEEF = tfItem("cooked_meef",
            food(7, 0.6F, false), Items.COOKED_BEEF);
    public static final TFRegistryObject<Item> MEEF_STROGANOFF = tfItem("meef_stroganoff",
            food(14, 1.2F, true).stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant(), Items.MUSHROOM_STEW);
    public static final TFRegistryObject<Item> HYDRA_CHOP = hydraChopItem("hydra_chop",
            food(18, 2.0F, true).rarity(Rarity.UNCOMMON).fireResistant());
    public static final TFRegistryObject<Item> MAZE_WAFER = tfItem("maze_wafer",
            food(4, 0.6F, false), Items.BREAD);
    public static final TFRegistryObject<Item> TORCHBERRIES = tfItem("torchberries",
            food(2, 0.3F, true), Items.GLOW_BERRIES);
    public static final TFRegistryObject<Item> EXPERIMENT_115 = existingBlockItem("experiment_115", TFBlocks.EXPERIMENT_115);
    public static final TFRegistryObject<Item> IRON_BERRY = tfItem("iron_berry", new Item.Properties(), Items.SWEET_BERRIES);
    public static final TFRegistryObject<Item> GOLD_BERRY = tfItem("gold_berry", new Item.Properties(), Items.SWEET_BERRIES);
    public static final TFRegistryObject<Item> COPPER_BERRY = tfItem("copper_berry", new Item.Properties(), Items.SWEET_BERRIES);
    public static final TFRegistryObject<Item> ESSENCE_BERRY = essenceBerryItem("essence_berry", new Item.Properties(), Items.SWEET_BERRIES);

    // ====== Charms ======
    public static final TFRegistryObject<Item> CHARM_OF_LIFE_1 = charmOfLife("charm_of_life_1", rarity(Rarity.UNCOMMON), Items.GOLDEN_APPLE, 1);
    public static final TFRegistryObject<Item> CHARM_OF_LIFE_2 = charmOfLife("charm_of_life_2", rarity(Rarity.UNCOMMON), Items.ENCHANTED_GOLDEN_APPLE, 2);
    public static final TFRegistryObject<Item> CHARM_OF_KEEPING_1 = charmOfKeeping("charm_of_keeping_1", rarity(Rarity.UNCOMMON), Items.LEAD, 1);
    public static final TFRegistryObject<Item> CHARM_OF_KEEPING_2 = charmOfKeeping("charm_of_keeping_2", rarity(Rarity.UNCOMMON), Items.LEAD, 2);
    public static final TFRegistryObject<Item> CHARM_OF_KEEPING_3 = charmOfKeeping("charm_of_keeping_3", rarity(Rarity.UNCOMMON), Items.LEAD, 3);

    // ====== Scepters & magical tools ======
    public static final TFRegistryObject<Item> TWILIGHT_SCEPTER = twilightWandItem("twilight_scepter",
            new Item.Properties().durability(99).rarity(Rarity.UNCOMMON), Items.BLAZE_ROD, TFItemVisuals.SCEPTER_OF_TWILIGHT);
    public static final TFRegistryObject<Item> LIFEDRAIN_SCEPTER = lifedrainScepterItem("lifedrain_scepter",
            new Item.Properties().durability(99).rarity(Rarity.UNCOMMON), Items.BLAZE_ROD, TFItemVisuals.SCEPTER_OF_LIFE_DRAIN);
    public static final TFRegistryObject<Item> ZOMBIE_SCEPTER = zombieWandItem("zombie_scepter",
            new Item.Properties().durability(9).rarity(Rarity.UNCOMMON));
    public static final TFRegistryObject<Item> FORTIFICATION_SCEPTER = fortificationWandItem("fortification_scepter",
            new Item.Properties().durability(9).rarity(Rarity.UNCOMMON), Items.BLAZE_ROD);

    // ====== Maps & focuses ======
    public static final TFRegistryObject<Item> MAGIC_MAP = emptyMagicMapItem("magic_map", new Item.Properties());
    public static final TFRegistryObject<Item> MAZE_MAP = emptyMazeMapItem("maze_map", false, new Item.Properties());
    public static final TFRegistryObject<Item> ORE_MAP = emptyMazeMapItem("ore_map", true, new Item.Properties());
    public static final TFRegistryObject<Item> MAGIC_MAP_FOCUS = tfItem("magic_map_focus", new Item.Properties(), Items.COMPASS);
    public static final TFRegistryObject<Item> MAZE_MAP_FOCUS = tfItem("maze_map_focus", new Item.Properties(), Items.COMPASS);

    // ====== Misc tools ======
    public static final TFRegistryObject<Item> ORE_METER = oreMeterItem("ore_meter",
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), Items.RECOVERY_COMPASS);
    public static final TFRegistryObject<Item> ORE_MAGNET = oreMagnetItem("ore_magnet",
            new Item.Properties().durability(64), Items.IRON_BLOCK);
    public static final TFRegistryObject<Item> CRUMBLE_HORN = crumbleHornItem("crumble_horn",
            new Item.Properties().durability(1024).rarity(Rarity.RARE), Items.GOAT_HORN);
    public static final TFRegistryObject<Item> PEACOCK_FEATHER_FAN = peacockFanItem("peacock_feather_fan",
            new Item.Properties().durability(1024).rarity(Rarity.RARE), Items.FEATHER);
    public static final TFRegistryObject<Item> MOONWORM_QUEEN = tfItem("moonworm_queen",
            new Item.Properties().durability(256).rarity(Rarity.RARE), Items.GLOW_INK_SAC);
    public static final TFRegistryObject<Item> TRANSFORMATION_POWDER = transformPowderItem("transformation_powder",
            new Item.Properties(), Items.GUNPOWDER);
    public static final TFRegistryObject<Item> TOWER_KEY = tfItem("tower_key",
            new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON), Items.TRIPWIRE_HOOK);
    public static final TFRegistryObject<Item> BRITTLE_FLASK = brittleFlaskItem("brittle_potion_flask",
            new Item.Properties().durability(4).component(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY), Items.GLASS_BOTTLE, false);
    public static final TFRegistryObject<Item> GREATER_FLASK = greaterFlaskItem("greater_potion_flask",
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant().durability(8).component(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY_UNBREAKABLE));

    // ====== Naga armor (no helmet/boots in TF original — only chest+legs) ======
    public static final TFRegistryObject<Item> NAGA_CHESTPLATE = armor("naga_chestplate",
            ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, 21, Rarity.UNCOMMON,
            Items.IRON_CHESTPLATE, TFItemVisuals.NAGA_SCALE_CHESTPLATE);
    public static final TFRegistryObject<Item> NAGA_LEGGINGS = armor("naga_leggings",
            ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, 21, Rarity.UNCOMMON,
            Items.IRON_LEGGINGS, TFItemVisuals.NAGA_SCALE_LEGGINGS);

    // ====== Ironwood tier ≈ Iron ======
    public static final TFRegistryObject<Item> IRONWOOD_HELMET = armor("ironwood_helmet",
            ArmorMaterials.IRON, ArmorItem.Type.HELMET, 20, Rarity.COMMON,
            Items.IRON_HELMET, TFItemVisuals.IRONWOOD_HELMET);
    public static final TFRegistryObject<Item> IRONWOOD_CHESTPLATE = armor("ironwood_chestplate",
            ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, 20, Rarity.COMMON,
            Items.IRON_CHESTPLATE, TFItemVisuals.IRONWOOD_CHESTPLATE);
    public static final TFRegistryObject<Item> IRONWOOD_LEGGINGS = armor("ironwood_leggings",
            ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, 20, Rarity.COMMON,
            Items.IRON_LEGGINGS, TFItemVisuals.IRONWOOD_LEGGINGS);
    public static final TFRegistryObject<Item> IRONWOOD_BOOTS = armor("ironwood_boots",
            ArmorMaterials.IRON, ArmorItem.Type.BOOTS, 20, Rarity.COMMON,
            Items.IRON_BOOTS, TFItemVisuals.IRONWOOD_BOOTS);
    public static final TFRegistryObject<Item> IRONWOOD_SWORD = sword("ironwood_sword", TFToolMaterials.IRONWOOD, 3, -2.4F,
            Items.IRON_SWORD, -1);
    public static final TFRegistryObject<Item> IRONWOOD_PICKAXE = pickaxe("ironwood_pickaxe", TFToolMaterials.IRONWOOD, 1.0F, -2.8F,
            Items.IRON_PICKAXE, TFItemVisuals.IRONWOOD_PICKAXE);
    public static final TFRegistryObject<Item> IRONWOOD_AXE = axe("ironwood_axe", TFToolMaterials.IRONWOOD, 6.0F, -3.1F,
            Items.IRON_AXE, -1);
    public static final TFRegistryObject<Item> IRONWOOD_SHOVEL = shovel("ironwood_shovel", TFToolMaterials.IRONWOOD, 1.5F, -3.0F,
            Items.IRON_SHOVEL, -1);
    public static final TFRegistryObject<Item> IRONWOOD_HOE = hoe("ironwood_hoe", TFToolMaterials.IRONWOOD, -2, -1.0F,
            Items.IRON_HOE, -1);

    // ====== Steeleaf tier ≈ Diamond stat-wise ======
    public static final TFRegistryObject<Item> STEELEAF_HELMET = armor("steeleaf_helmet",
            ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, 10, Rarity.UNCOMMON,
            Items.DIAMOND_HELMET, -1);
    public static final TFRegistryObject<Item> STEELEAF_CHESTPLATE = armor("steeleaf_chestplate",
            ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, 10, Rarity.UNCOMMON,
            Items.DIAMOND_CHESTPLATE, -1);
    public static final TFRegistryObject<Item> STEELEAF_LEGGINGS = armor("steeleaf_leggings",
            ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, 10, Rarity.UNCOMMON,
            Items.DIAMOND_LEGGINGS, -1);
    public static final TFRegistryObject<Item> STEELEAF_BOOTS = armor("steeleaf_boots",
            ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, 10, Rarity.UNCOMMON,
            Items.DIAMOND_BOOTS, -1);
    public static final TFRegistryObject<Item> STEELEAF_SWORD = sword("steeleaf_sword", TFToolMaterials.STEELEAF, 3, -2.4F,
            Items.DIAMOND_SWORD, TFItemVisuals.STEELEAF_SWORD);
    public static final TFRegistryObject<Item> STEELEAF_PICKAXE = pickaxe("steeleaf_pickaxe", TFToolMaterials.STEELEAF, 1.0F, -2.8F,
            Items.DIAMOND_PICKAXE, TFItemVisuals.STEELEAF_PICKAXE);
    public static final TFRegistryObject<Item> STEELEAF_AXE = axe("steeleaf_axe", TFToolMaterials.STEELEAF, 6.0F, -3.0F,
            Items.DIAMOND_AXE, -1);
    public static final TFRegistryObject<Item> STEELEAF_SHOVEL = shovel("steeleaf_shovel", TFToolMaterials.STEELEAF, 1.5F, -3.0F,
            Items.DIAMOND_SHOVEL, -1);
    public static final TFRegistryObject<Item> STEELEAF_HOE = hoe("steeleaf_hoe", TFToolMaterials.STEELEAF, -3.0F, -0.5F,
            Items.DIAMOND_HOE, TFItemVisuals.STEELEAF_HOE);

    // ====== Fiery tier ≈ Diamond+fire-resistant ======
    public static final TFRegistryObject<Item> FIERY_HELMET = fieryArmor("fiery_helmet", ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, 25, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> FIERY_CHESTPLATE = fieryArmor("fiery_chestplate", ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, 25, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> FIERY_LEGGINGS = fieryArmor("fiery_leggings", ArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, 25, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> FIERY_BOOTS = fieryArmor("fiery_boots", ArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, 25, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> FIERY_SWORD = fierySword("fiery_sword", TFToolMaterials.FIERY, 3, -2.4F);
    public static final TFRegistryObject<Item> FIERY_PICKAXE = fieryPick("fiery_pickaxe", TFToolMaterials.FIERY, 1.0F, -2.8F);

    // ====== Knightmetal tier ≈ Iron with shield/spike interaction ======
    public static final TFRegistryObject<Item> KNIGHTMETAL_HELMET = armor("knightmetal_helmet",
            ArmorMaterials.IRON, ArmorItem.Type.HELMET, 20, Rarity.UNCOMMON,
            Items.IRON_HELMET, -1);
    public static final TFRegistryObject<Item> KNIGHTMETAL_CHESTPLATE = armor("knightmetal_chestplate",
            ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, 20, Rarity.UNCOMMON,
            Items.IRON_CHESTPLATE, -1);
    public static final TFRegistryObject<Item> KNIGHTMETAL_LEGGINGS = armor("knightmetal_leggings",
            ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, 20, Rarity.UNCOMMON,
            Items.IRON_LEGGINGS, -1);
    public static final TFRegistryObject<Item> KNIGHTMETAL_BOOTS = armor("knightmetal_boots",
            ArmorMaterials.IRON, ArmorItem.Type.BOOTS, 20, Rarity.UNCOMMON,
            Items.IRON_BOOTS, -1);
    public static final TFRegistryObject<Item> KNIGHTMETAL_SWORD = knightmetalSword("knightmetal_sword", TFToolMaterials.KNIGHTMETAL, 3, -2.4F);
    public static final TFRegistryObject<Item> KNIGHTMETAL_AXE = knightmetalAxe("knightmetal_axe", TFToolMaterials.KNIGHTMETAL, 6.0F, -3.1F);
    public static final TFRegistryObject<Item> KNIGHTMETAL_PICKAXE = knightmetalPick("knightmetal_pickaxe", TFToolMaterials.KNIGHTMETAL, 1.0F, -2.8F);
    public static final TFRegistryObject<Item> KNIGHTMETAL_RING = tfItem("knightmetal_ring",
            new Item.Properties().rarity(Rarity.UNCOMMON), Items.GOLDEN_APPLE);

    // ====== Special boss-tier weapons ======
    public static final TFRegistryObject<Item> GOLDEN_MINOTAUR_AXE = minotaurAxeItem("gold_minotaur_axe", Tiers.GOLD, 6.0F, -3.2F,
            Items.GOLDEN_AXE, -1);
    public static final TFRegistryObject<Item> DIAMOND_MINOTAUR_AXE = minotaurAxeItem("diamond_minotaur_axe", Tiers.DIAMOND, 6.0F, -3.2F,
            Items.DIAMOND_AXE, TFItemVisuals.DIAMOND_MINOTAUR_AXE);
    public static final TFRegistryObject<Item> MAZEBREAKER_PICKAXE = mazebreakerPickItem("mazebreaker_pickaxe", Tiers.DIAMOND, 1.0F, -2.8F,
            Items.DIAMOND_PICKAXE, -1);

    // Snow/Ice queen drops
    public static final TFRegistryObject<Item> ICE_BOMB = iceBombItem("ice_bomb",
            new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16), Items.SNOWBALL);
    public static final TFRegistryObject<Item> TRIPLE_BOW = tripleBowItem("triple_bow",
            new Item.Properties().durability(384).rarity(Rarity.RARE), Items.BOW);
    public static final TFRegistryObject<Item> SEEKER_BOW = seekerBowItem("seeker_bow",
            new Item.Properties().durability(384).rarity(Rarity.RARE), Items.BOW);
    public static final TFRegistryObject<Item> ENDER_BOW = enderBowItem("ender_bow",
            new Item.Properties().durability(384).rarity(Rarity.RARE), Items.BOW);
    public static final TFRegistryObject<Item> ICE_BOW = iceBowItem("ice_bow",
            new Item.Properties().durability(384).rarity(Rarity.RARE), Items.BOW);
    public static final TFRegistryObject<Item> ICE_SWORD = iceSword("ice_sword", TFToolMaterials.ICE, 3, -2.4F);
    public static final TFRegistryObject<Item> GLASS_SWORD = glassSwordItem("glass_sword", TFToolMaterials.GLASS, 3, -2.4F,
            Items.DIAMOND_SWORD, -1);
    // Alpha Yeti / Yeti
    public static final TFRegistryObject<Item> ALPHA_YETI_FUR = tfItem("alpha_yeti_fur",
            rarity(Rarity.UNCOMMON), Items.WHITE_WOOL);
    public static final TFRegistryObject<Item> YETI_FUR = tfItem("yeti_fur", new Item.Properties(), Items.WHITE_WOOL);
    public static final TFRegistryObject<Item> ARCTIC_FUR = tfItem("arctic_fur", new Item.Properties(), Items.WHITE_WOOL);
    // Ur-Ghast trophy interaction
    public static final TFRegistryObject<Item> LAMPOFCINDERS = lampOfCindersItem("lamp_of_cinders",
            new Item.Properties().rarity(Rarity.RARE).durability(99).fireResistant(), Items.SHROOMLIGHT);
    // Block-chain goblin throwable
    public static final TFRegistryObject<Item> BLOCK_AND_CHAIN = blockAndChainItem("block_and_chain",
            new Item.Properties().rarity(Rarity.UNCOMMON).durability(99), Items.IRON_BLOCK);
    // Uberous soil interaction
    public static final TFRegistryObject<Item> MAGIC_BEANS = magicBeansItem("magic_beans", new Item.Properties(), Items.WHEAT_SEEDS);
    // Giant tools (used by GiantMiner; tier exotic-large, treat as diamond)
    public static final TFRegistryObject<Item> GIANT_PICKAXE = giantPick("giant_pickaxe", TFToolMaterials.GIANT, 1, -2.8F);
    public static final TFRegistryObject<Item> GIANT_SWORD = giantSword("giant_sword", TFToolMaterials.GIANT, 3, -2.4F);

    public static final TFRegistryObject<Item> TRAVELLERS_GOGGLES = travellersGogglesItem("travellers_goggles", TravellersArmorItem.gogglesProperties(new Item.Properties().rarity(Rarity.COMMON)));
    public static final TFRegistryObject<Item> TRAVELLERS_VEST = travellersArmorItem("travellers_vest", ArmorItem.Type.CHESTPLATE, TravellersArmorItem.chestProperties(new Item.Properties().rarity(Rarity.COMMON)), 4, 12, false);
    public static final TFRegistryObject<Item> TRAVELLERS_GLOVES = travellersArmorItem("travellers_gloves", ArmorItem.Type.CHESTPLATE, TravellersArmorItem.glovesProperties(new Item.Properties().rarity(Rarity.COMMON)), 4, 12, false);
    public static final TFRegistryObject<Item> TRAVELLERS_WINGS = travellersArmorItem("travellers_wings", ArmorItem.Type.LEGGINGS, TravellersArmorItem.wingsProperties(new Item.Properties().rarity(Rarity.COMMON)), 4, 12, false);
    public static final TFRegistryObject<Item> TRAVELLERS_BELT = travellersArmorItem("travellers_belt", ArmorItem.Type.LEGGINGS, TravellersArmorBeltItem.beltProperties(new Item.Properties().rarity(Rarity.COMMON)), 4, 12, true);
    public static final TFRegistryObject<Item> TRAVELLERS_BOOTS = travellersArmorItem("travellers_boots", ArmorItem.Type.BOOTS, TravellersArmorItem.bootsProperties(new Item.Properties().rarity(Rarity.COMMON)), 4, 12, false);

    public static final TFRegistryObject<Item> MAGIC_PAINTING = magicPaintingItem("magic_painting",
            new Item.Properties(), Items.PAINTING);
    public static final TFRegistryObject<Item> RAW_FORTIFICATION_FRUIT = tfItem("raw_fortification_fruit",
            new Item.Properties(), Items.SWEET_BERRIES);

    public static final TFRegistryObject<Item> NAGA_BANNER_PATTERN = bannerPatternItem("naga_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.NAGA_BANNER_PATTERN);
    public static final TFRegistryObject<Item> LICH_BANNER_PATTERN = bannerPatternItem("lich_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.LICH_BANNER_PATTERN);
    public static final TFRegistryObject<Item> HYDRA_BANNER_PATTERN = bannerPatternItem("hydra_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.HYDRA_BANNER_PATTERN);
    public static final TFRegistryObject<Item> UR_GHAST_BANNER_PATTERN = bannerPatternItem("ur_ghast_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.UR_GHAST_BANNER_PATTERN);
    public static final TFRegistryObject<Item> KNIGHT_PHANTOM_BANNER_PATTERN = bannerPatternItem("knight_phantom_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.KNIGHT_PHANTOM_BANNER_PATTERN);
    public static final TFRegistryObject<Item> SNOW_QUEEN_BANNER_PATTERN = bannerPatternItem("snow_queen_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.SNOW_QUEEN_BANNER_PATTERN);
    public static final TFRegistryObject<Item> MINOSHROOM_BANNER_PATTERN = bannerPatternItem("minoshroom_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.MINOSHROOM_BANNER_PATTERN);
    public static final TFRegistryObject<Item> ALPHA_YETI_BANNER_PATTERN = bannerPatternItem("alpha_yeti_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.ALPHA_YETI_BANNER_PATTERN);
    public static final TFRegistryObject<Item> QUEST_RAM_BANNER_PATTERN = bannerPatternItem("quest_ram_banner_pattern",
            CustomTagGenerator.BannerPatternTagGenerator.QUEST_RAM_BANNER_PATTERN);

    public static final TFRegistryObject<Item> FILLED_MAGIC_MAP = filledMagicMapItem("filled_magic_map", new Item.Properties());
    public static final TFRegistryObject<Item> FILLED_MAZE_MAP = filledMazeMapItem("filled_maze_map", false, new Item.Properties());
    public static final TFRegistryObject<Item> FILLED_ORE_MAP = filledMazeMapItem("filled_ore_map", true, new Item.Properties());

    public static final TFRegistryObject<Item> TWILIGHT_OAK_BOAT = tfItem("twilight_oak_boat", new Item.Properties().stacksTo(1), Items.OAK_BOAT);
    public static final TFRegistryObject<Item> TWILIGHT_OAK_CHEST_BOAT = tfItem("twilight_oak_chest_boat", new Item.Properties().stacksTo(1), Items.OAK_CHEST_BOAT);
    public static final TFRegistryObject<Item> CANOPY_BOAT = tfItem("canopy_boat", new Item.Properties().stacksTo(1), Items.DARK_OAK_BOAT);
    public static final TFRegistryObject<Item> CANOPY_CHEST_BOAT = tfItem("canopy_chest_boat", new Item.Properties().stacksTo(1), Items.DARK_OAK_CHEST_BOAT);
    public static final TFRegistryObject<Item> MANGROVE_BOAT = tfItem("mangrove_boat", new Item.Properties().stacksTo(1), Items.MANGROVE_BOAT);
    public static final TFRegistryObject<Item> MANGROVE_CHEST_BOAT = tfItem("mangrove_chest_boat", new Item.Properties().stacksTo(1), Items.MANGROVE_CHEST_BOAT);
    public static final TFRegistryObject<Item> DARK_BOAT = tfItem("dark_boat", new Item.Properties().stacksTo(1), Items.DARK_OAK_BOAT);
    public static final TFRegistryObject<Item> DARK_CHEST_BOAT = tfItem("dark_chest_boat", new Item.Properties().stacksTo(1), Items.DARK_OAK_CHEST_BOAT);
    public static final TFRegistryObject<Item> MINING_BOAT = tfItem("mining_boat", new Item.Properties().stacksTo(1), Items.BIRCH_BOAT);
    public static final TFRegistryObject<Item> MINING_CHEST_BOAT = tfItem("mining_chest_boat", new Item.Properties().stacksTo(1), Items.BIRCH_CHEST_BOAT);
    public static final TFRegistryObject<Item> TIME_BOAT = tfItem("time_boat", new Item.Properties().stacksTo(1), Items.SPRUCE_BOAT);
    public static final TFRegistryObject<Item> TIME_CHEST_BOAT = tfItem("time_chest_boat", new Item.Properties().stacksTo(1), Items.SPRUCE_CHEST_BOAT);
    public static final TFRegistryObject<Item> TRANSFORMATION_BOAT = tfItem("transformation_boat", new Item.Properties().stacksTo(1), Items.JUNGLE_BOAT);
    public static final TFRegistryObject<Item> TRANSFORMATION_CHEST_BOAT = tfItem("transformation_chest_boat", new Item.Properties().stacksTo(1), Items.JUNGLE_CHEST_BOAT);
    public static final TFRegistryObject<Item> SORTING_BOAT = tfItem("sorting_boat", new Item.Properties().stacksTo(1), Items.CHERRY_BOAT);
    public static final TFRegistryObject<Item> SORTING_CHEST_BOAT = tfItem("sorting_chest_boat", new Item.Properties().stacksTo(1), Items.CHERRY_CHEST_BOAT);

    public static final TFRegistryObject<Item> BLACKBERRY = stackableBerryItem("blackberry");
    public static final TFRegistryObject<Item> BLUEBERRY = stackableBerryItem("blueberry");
    public static final TFRegistryObject<Item> RASPBERRY = stackableBerryItem("raspberry");
    public static final TFRegistryObject<Item> MALOBERRY = stackableBerryItem("maloberry");
    public static final TFRegistryObject<Item> BLIGHTBERRY = stackableBerryItem("blightberry",
            new StackableEffectInstance(MobEffects.REGENERATION, 8),
            new StackableEffectInstance(MobEffects.POISON, 5, 0.75F),
            new StackableEffectInstance(MobEffects.WITHER, 5, 0.15F));
    public static final TFRegistryObject<Item> DUSKBERRY = stackableBerryItem("duskberry",
            new StackableEffectInstance(MobEffects.NIGHT_VISION, 15),
            new StackableEffectInstance(MobEffects.BLINDNESS, 3, 0.75F));
    public static final TFRegistryObject<Item> SKYBERRY = stackableBerryItem("skyberry",
            new StackableEffectInstance(MobEffects.JUMP, 8),
            new StackableEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3, 0.75F));
    public static final TFRegistryObject<Item> STINGBERRY = stackableBerryItem("stingberry",
            new StackableEffectInstance(MobEffects.DAMAGE_BOOST, 10),
            new StackableEffectInstance(MobEffects.DIG_SLOWDOWN, 10, 0.75F));
    public static final TFRegistryObject<Item> BERRY_MEDLEY = tfItem("berry_medley", food(6, 0.5F, false).stacksTo(16), Items.BREAD);

    public static final TFRegistryObject<Item> BEEF_JERKY = tfItem("beef_jerky", food(6, 0.6F, false), Items.COOKED_BEEF);
    public static final TFRegistryObject<Item> PORK_JERKY = tfItem("pork_jerky", food(6, 0.6F, false), Items.COOKED_PORKCHOP);
    public static final TFRegistryObject<Item> CHICKEN_JERKY = tfItem("chicken_jerky", food(4, 0.5F, false), Items.COOKED_CHICKEN);
    public static final TFRegistryObject<Item> MUTTON_JERKY = tfItem("mutton_jerky", food(6, 0.6F, false), Items.COOKED_MUTTON);
    public static final TFRegistryObject<Item> RABBIT_JERKY = tfItem("rabbit_jerky", food(4, 0.5F, false), Items.COOKED_RABBIT);
    public static final TFRegistryObject<Item> COD_JERKY = tfItem("cod_jerky", food(4, 0.5F, false), Items.COOKED_COD);
    public static final TFRegistryObject<Item> SALMON_JERKY = tfItem("salmon_jerky", food(6, 0.6F, false), Items.COOKED_SALMON);
    public static final TFRegistryObject<Item> TROPICAL_FISH_JERKY = tfItem("tropical_fish_jerky", food(4, 0.5F, false), Items.TROPICAL_FISH);
    public static final TFRegistryObject<Item> FUGU_JERKY = tfItem("fugu_jerky", food(4, 0.5F, false), Items.PUFFERFISH);
    public static final TFRegistryObject<Item> VENISON_JERKY = tfItem("venison_jerky", food(6, 0.6F, false), Items.COOKED_MUTTON);
    public static final TFRegistryObject<Item> MEEF_JERKY = tfItem("meef_jerky", food(7, 0.7F, false), Items.COOKED_BEEF);
    public static final TFRegistryObject<Item> MONSTER_JERKY = tfItem("monster_jerky", food(4, 0.4F, false), Items.ROTTEN_FLESH);
    public static final TFRegistryObject<Item> SHIKA_SENBEI = tfItem("shika_senbei", food(4, 0.5F, false), Items.BREAD);
    public static final TFRegistryObject<Item> STALE_BREAD = customDamageSwordItem("stale_bread",
            TFDamageTypes.STALE_SANDWICH, Tiers.WOOD, new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(Tiers.WOOD, 3, -2.4F).withTooltip(false)));
    public static final TFRegistryObject<Item> MOSS_SOUP = tfItem("moss_soup", food(8, 0.6F, false).stacksTo(1), Items.MUSHROOM_STEW);

    public static final TFRegistryObject<Item> ARCTIC_HELMET = arcticArmor("arctic_helmet", ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, 12, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> ARCTIC_CHESTPLATE = arcticArmor("arctic_chestplate", ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, 12, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> ARCTIC_LEGGINGS = arcticArmor("arctic_leggings", ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, 12, Rarity.UNCOMMON);
    public static final TFRegistryObject<Item> ARCTIC_BOOTS = arcticArmor("arctic_boots", ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, 12, Rarity.UNCOMMON);

    public static final TFRegistryObject<Item> YETI_HELMET = yetiArmor("yeti_helmet", ArmorMaterials.IRON, ArmorItem.Type.HELMET, 25, Rarity.RARE);
    public static final TFRegistryObject<Item> YETI_CHESTPLATE = yetiArmor("yeti_chestplate", ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, 25, Rarity.RARE);
    public static final TFRegistryObject<Item> YETI_LEGGINGS = yetiArmor("yeti_leggings", ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, 25, Rarity.RARE);
    public static final TFRegistryObject<Item> YETI_BOOTS = yetiArmor("yeti_boots", ArmorMaterials.IRON, ArmorItem.Type.BOOTS, 25, Rarity.RARE);

    public static final TFRegistryObject<Item> PHANTOM_HELMET = phantomArmor("phantom_helmet", ArmorMaterials.IRON, ArmorItem.Type.HELMET, 30, Rarity.RARE);
    public static final TFRegistryObject<Item> PHANTOM_CHESTPLATE = phantomArmor("phantom_chestplate", ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, 30, Rarity.RARE);

    public static final TFRegistryObject<Item> MUSIC_DISC_FINDINGS = tfItem("music_disc_findings", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.FINDINGS), Items.MUSIC_DISC_13);
    public static final TFRegistryObject<Item> MUSIC_DISC_HOME = tfItem("music_disc_home", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.HOME), Items.MUSIC_DISC_CAT);
    public static final TFRegistryObject<Item> MUSIC_DISC_MAKER = tfItem("music_disc_maker", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.MAKER), Items.MUSIC_DISC_BLOCKS);
    public static final TFRegistryObject<Item> MUSIC_DISC_MOTION = tfItem("music_disc_motion", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.MOTION), Items.MUSIC_DISC_CHIRP);
    public static final TFRegistryObject<Item> MUSIC_DISC_RADIANCE = tfItem("music_disc_radiance", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.RADIANCE), Items.MUSIC_DISC_FAR);
    public static final TFRegistryObject<Item> MUSIC_DISC_STEPS = tfItem("music_disc_steps", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.STEPS), Items.MUSIC_DISC_MALL);
    public static final TFRegistryObject<Item> MUSIC_DISC_SUPERSTITIOUS = tfItem("music_disc_superstitious", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.SUPERSTITIOUS), Items.MUSIC_DISC_MELLOHI);
    public static final TFRegistryObject<Item> MUSIC_DISC_THREAD = tfItem("music_disc_thread", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.THREAD), Items.MUSIC_DISC_STAL);
    public static final TFRegistryObject<Item> MUSIC_DISC_WAYFARER = tfItem("music_disc_wayfarer", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.WAYFARER), Items.MUSIC_DISC_STRAD);

        public static final TFRegistryObject<Item> CUBE_OF_ANNIHILATION = cubeOfAnnihilationItem("cube_of_annihilation", new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(99));
    public static final TFRegistryObject<Item> CUBE_TALISMAN = tfItem("cube_talisman", new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1), Items.AMETHYST_SHARD);
    public static final TFRegistryObject<Item> CROWN_SPLINTER = tfItem("crown_splinter", new Item.Properties().rarity(Rarity.UNCOMMON), Items.GOLD_NUGGET);
    public static final TFRegistryObject<Item> MYSTIC_CROWN = mysticCrownItem("mystic_crown", new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
    public static final TFRegistryObject<Item> KEEPSAKE_CASKET = existingBlockItem("keepsake_casket", TFBlocks.KEEPSAKE_CASKET);
    public static final TFRegistryObject<Item> KNIGHTMETAL_SHIELD = knightmetalShield("knightmetal_shield", new Item.Properties().durability(336).rarity(Rarity.UNCOMMON));
    public static final TFRegistryObject<Item> MOON_DIAL = moonDialItem("moon_dial", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final TFRegistryObject<Item> POCKET_WATCH = pocketWatchItem("pocket_watch", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final TFRegistryObject<Item> FOUR_LEAF_CLOVER = tfItem("four_leaf_clover", rarity(Rarity.UNCOMMON), Items.SUGAR_CANE);
    public static final TFRegistryObject<Item> EMPERORS_CLOTH = tfItem("emperors_cloth", rarity(Rarity.UNCOMMON), Items.WHITE_WOOL);
    public static final TFRegistryObject<Item> GELATINOUS_SLIME_DROP = tfItem("gelatinous_slime_drop", new Item.Properties(), Items.SLIME_BALL);
    public static final TFRegistryObject<Item> GELATINOUS_MAZE_SLIME_DROP = tfItem("gelatinous_maze_slime_drop", new Item.Properties(), Items.MAGMA_CREAM);
    public static final TFRegistryObject<Item> TANNED_LEATHER = tfItem("tanned_leather", new Item.Properties(), Items.LEATHER);
    public static final TFRegistryObject<Item> TREATED_LEATHER = tfItem("treated_leather", rarity(Rarity.UNCOMMON), Items.LEATHER);

    public static final TFRegistryObject<Item> HOLLOW_OAK_LOG_ITEM = hollowLogItem("hollow_oak_log", TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_SPRUCE_LOG_ITEM = hollowLogItem("hollow_spruce_log", TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_BIRCH_LOG_ITEM = hollowLogItem("hollow_birch_log", TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_JUNGLE_LOG_ITEM = hollowLogItem("hollow_jungle_log", TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_ACACIA_LOG_ITEM = hollowLogItem("hollow_acacia_log", TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_DARK_OAK_LOG_ITEM = hollowLogItem("hollow_dark_oak_log", TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_MANGROVE_LOG_ITEM = hollowLogItem("hollow_mangrove_log", TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_CHERRY_LOG_ITEM = hollowLogItem("hollow_cherry_log", TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_CRIMSON_STEM_ITEM = hollowLogItem("hollow_crimson_stem", TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_WARPED_STEM_ITEM = hollowLogItem("hollow_warped_stem", TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_TWILIGHT_OAK_LOG_ITEM = hollowLogItem("hollow_twilight_oak_log", TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_CANOPY_LOG_ITEM = hollowLogItem("hollow_canopy_log", TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_DARK_LOG_ITEM = hollowLogItem("hollow_dark_log", TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_MINING_LOG_ITEM = hollowLogItem("hollow_mining_log", TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_TIME_LOG_ITEM = hollowLogItem("hollow_time_log", TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_TRANSFORMATION_LOG_ITEM = hollowLogItem("hollow_transformation_log", TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_SORTING_LOG_ITEM = hollowLogItem("hollow_sorting_log", TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE);
    public static final TFRegistryObject<Item> HOLLOW_VANGROVE_LOG_ITEM = hollowLogItem("hollow_vangrove_log", TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE);

    public static final TFRegistryObject<Item> TWILIGHT_OAK_SIGN_ITEM = signItem("twilight_oak_sign", TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN);
    public static final TFRegistryObject<Item> TWILIGHT_OAK_HANGING_SIGN_ITEM = hangingSignItem("twilight_oak_hanging_sign", TFBlocks.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> CANOPY_SIGN_ITEM = signItem("canopy_sign", TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN);
    public static final TFRegistryObject<Item> CANOPY_HANGING_SIGN_ITEM = hangingSignItem("canopy_hanging_sign", TFBlocks.CANOPY_HANGING_SIGN, TFBlocks.CANOPY_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> MANGROVE_SIGN_ITEM = signItem("mangrove_sign", TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN);
    public static final TFRegistryObject<Item> MANGROVE_HANGING_SIGN_ITEM = hangingSignItem("mangrove_hanging_sign", TFBlocks.MANGROVE_HANGING_SIGN, TFBlocks.MANGROVE_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> DARK_SIGN_ITEM = signItem("dark_sign", TFBlocks.DARK_SIGN, TFBlocks.DARK_WALL_SIGN);
    public static final TFRegistryObject<Item> DARK_HANGING_SIGN_ITEM = hangingSignItem("dark_hanging_sign", TFBlocks.DARK_HANGING_SIGN, TFBlocks.DARK_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> MINING_SIGN_ITEM = signItem("mining_sign", TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN);
    public static final TFRegistryObject<Item> MINING_HANGING_SIGN_ITEM = hangingSignItem("mining_hanging_sign", TFBlocks.MINING_HANGING_SIGN, TFBlocks.MINING_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> TIME_SIGN_ITEM = signItem("time_sign", TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN);
    public static final TFRegistryObject<Item> TIME_HANGING_SIGN_ITEM = hangingSignItem("time_hanging_sign", TFBlocks.TIME_HANGING_SIGN, TFBlocks.TIME_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> TRANSFORMATION_SIGN_ITEM = signItem("transformation_sign", TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN);
    public static final TFRegistryObject<Item> TRANSFORMATION_HANGING_SIGN_ITEM = hangingSignItem("transformation_hanging_sign", TFBlocks.TRANSFORMATION_HANGING_SIGN, TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN);
    public static final TFRegistryObject<Item> SORTING_SIGN_ITEM = signItem("sorting_sign", TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN);
    public static final TFRegistryObject<Item> SORTING_HANGING_SIGN_ITEM = hangingSignItem("sorting_hanging_sign", TFBlocks.SORTING_HANGING_SIGN, TFBlocks.SORTING_WALL_HANGING_SIGN);

    // Upstream-name aliases for full TF datagen ports.
    public static final TFRegistryObject<Item> MASON_JAR = existingBlockItem("mason_jar", TFBlocks.MASON_JAR);
    public static final TFRegistryObject<Item> TWILIGHT_OAK_SIGN = TWILIGHT_OAK_SIGN_ITEM;
    public static final TFRegistryObject<Item> TWILIGHT_OAK_HANGING_SIGN = TWILIGHT_OAK_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> CANOPY_SIGN = CANOPY_SIGN_ITEM;
    public static final TFRegistryObject<Item> CANOPY_HANGING_SIGN = CANOPY_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> MANGROVE_SIGN = MANGROVE_SIGN_ITEM;
    public static final TFRegistryObject<Item> MANGROVE_HANGING_SIGN = MANGROVE_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> DARK_SIGN = DARK_SIGN_ITEM;
    public static final TFRegistryObject<Item> DARK_HANGING_SIGN = DARK_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> MINING_SIGN = MINING_SIGN_ITEM;
    public static final TFRegistryObject<Item> MINING_HANGING_SIGN = MINING_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> TIME_SIGN = TIME_SIGN_ITEM;
    public static final TFRegistryObject<Item> TIME_HANGING_SIGN = TIME_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> TRANSFORMATION_SIGN = TRANSFORMATION_SIGN_ITEM;
    public static final TFRegistryObject<Item> TRANSFORMATION_HANGING_SIGN = TRANSFORMATION_HANGING_SIGN_ITEM;
    public static final TFRegistryObject<Item> SORTING_SIGN = SORTING_SIGN_ITEM;
    public static final TFRegistryObject<Item> SORTING_HANGING_SIGN = SORTING_HANGING_SIGN_ITEM;

        public static void bootstrap() {
        }

    private TFItems() {
    }

    // -------- helpers --------
    private static Item.Properties rarity(Rarity rarity) {
        return new Item.Properties().rarity(rarity);
    }

    private static Item.Properties food(int nutrition, float saturationModifier, boolean alwaysEdible) {
        FoodProperties.Builder b = new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier);
        if (alwaysEdible) b.alwaysEdible();
        return new Item.Properties().food(b.build());
    }

    private static TFRegistryObject<Item> tfItem(String path, Item.Properties properties, Item fallback) {
        return tfItem(path, properties, fallback, -1);
    }

    private static TFRegistryObject<Item> tfItem(String path, Item.Properties properties, Item fallback, int cmd) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexItem(properties, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    @SuppressWarnings("unchecked")
    private static TFRegistryObject<Item> spawnEggItem(String path, TFRegistryObject<? extends EntityType<?>> entityType, int primary, int secondary) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path),
                new SpawnEggItem((EntityType<? extends Mob>) entityType.get(), primary, secondary, new Item.Properties()));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> bannerPatternItem(String path, TagKey<BannerPattern> bannerPattern) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path),
                new BannerPatternItem(bannerPattern, rarity(Rarity.UNCOMMON).stacksTo(1)));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> existingBlockItem(String path, TFRegistryObject<? extends Block> block) {
        block.get(); // Forces TFBlocks to register the BlockItem for this id.
        Item item = BuiltInRegistries.ITEM.getOptional(TwilightForestMod.prefix(path))
                .orElseThrow(() -> new IllegalStateException("Missing block item twilightforest:" + path));
        return new TFRegistryObject<>(item);
    }

    private static TFRegistryObject<Item> hollowLogItem(String path, TFRegistryObject<Block> horizontal, TFRegistryObject<Block> vertical, TFRegistryObject<Block> climbable) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path),
                new twilightforest.item.HollowLogItem(horizontal, vertical, climbable, new Item.Properties()));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> signItem(String path, TFRegistryObject<? extends Block> standing, TFRegistryObject<? extends Block> wall) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path),
                new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> hangingSignItem(String path, TFRegistryObject<? extends Block> ceiling, TFRegistryObject<? extends Block> wall) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path),
                new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
        return new TFRegistryObject<>(registered);
    }

        private static TFRegistryObject<Item> cubeOfAnnihilationItem(String path, Item.Properties properties) {
                Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CubeOfAnnihilationItem(properties));
                return new TFRegistryObject<>(registered);
        }

    // -- Q17 behaviour-aware item helpers --
    private static TFRegistryObject<Item> magicBeansItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MagicBeansItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> transformPowderItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new TransformPowderItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> moonwormQueenItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MoonwormQueenItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> charmOfLife(String path, Item.Properties properties, Item fallback, int tier) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CharmOfLifeItem(properties, fallback, tier));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> charmOfKeeping(String path, Item.Properties properties, Item fallback, int tier) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CharmOfKeepingItem(properties, fallback, tier));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> crumbleHornItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CrumbleHornItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> iceBombItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new IceBombItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> twilightWandItem(String path, Item.Properties properties, Item fallback, int cmd) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new TwilightWandItem(properties, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> lifedrainScepterItem(String path, Item.Properties properties, Item fallback, int cmd) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new LifedrainScepterItem(properties, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> fortificationWandItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new FortificationWandItem(properties, fallback, -1));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> zombieScepterItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new ZombieScepterItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> zombieWandItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new ZombieWandItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> brittleFlaskItem(String path, Item.Properties properties, Item fallback, boolean greater) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new BrittleFlaskItem(properties, fallback, greater));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> greaterFlaskItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new GreaterFlaskItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> emptyMagicMapItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new EmptyMagicMapItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> emptyMazeMapItem(String path, boolean mapOres, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new EmptyMazeMapItem(mapOres, properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> filledMagicMapItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MagicMapItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> filledMazeMapItem(String path, boolean mapOres, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MazeMapItem(mapOres, properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> blockAndChainItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new ChainBlockItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> peacockFanItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new PeacockFanItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> oreMagnetItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new OreMagnetItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> oreMeterItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new OreMeterItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> essenceBerryItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new EssenceBerryItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> stackableBerryItem(String path, StackableEffectInstance... effects) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new StackableEffectItem(effects));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> exanimateEssenceItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new ExanimateEssenceItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> glassSwordItem(String path, Tier tier, int damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed)).durability(1);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new GlassSwordItem(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> customDamageSwordItem(String path, net.minecraft.resources.ResourceKey<net.minecraft.world.damagesource.DamageType> damageType, Tier tier, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CustomDamageSwordItem(damageType, tier, properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> hydraChopItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new HydraChopItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> fieryArmor(String path, net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, int durabilityFactor, Rarity rarity) {
        Item.Properties props = new Item.Properties().durability(type.getDurability(durabilityFactor)).rarity(rarity).fireResistant();
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new FieryArmorItem(material, type, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> arcticArmor(String path, net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, int durabilityFactor, Rarity rarity) {
        Item.Properties props = new Item.Properties().durability(type.getDurability(durabilityFactor)).rarity(rarity);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new ArcticArmorItem(material, type, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> yetiArmor(String path, net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, int durabilityFactor, Rarity rarity) {
        Item.Properties props = new Item.Properties().durability(type.getDurability(durabilityFactor)).rarity(rarity);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new YetiArmorItem(material, type, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> phantomArmor(String path, net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, int durabilityFactor, Rarity rarity) {
        Item.Properties props = new Item.Properties().durability(type.getDurability(durabilityFactor)).rarity(rarity);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new PhantomArmorItem(material, type, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> fierySword(String path, Tier tier, int damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed)).fireResistant();
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new FierySwordItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> fieryPick(String path, Tier tier, float damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(PickaxeItem.createAttributes(tier, damage, speed)).fireResistant();
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new FieryPickItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> knightmetalSword(String path, Tier tier, int damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed)).rarity(Rarity.UNCOMMON);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new KnightmetalSwordItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> knightmetalAxe(String path, Tier tier, float damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(AxeItem.createAttributes(tier, damage, speed)).rarity(Rarity.UNCOMMON);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new KnightmetalAxeItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> knightmetalPick(String path, Tier tier, float damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(PickaxeItem.createAttributes(tier, damage, speed)).rarity(Rarity.UNCOMMON);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new KnightmetalPickItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> knightmetalShield(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new KnightmetalShieldItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> iceSword(String path, Tier tier, int damage, float speed) {
        Item.Properties props = new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed)).rarity(Rarity.UNCOMMON);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new IceSwordItem(tier, props));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> giantPick(String path, Tier tier, int damage, float speed) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new GiantPickItem(tier, new Item.Properties().attributes(GiantPickItem.createGiantAttributes(tier, damage, speed))));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> giantSword(String path, Tier tier, int damage, float speed) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new GiantSwordItem(tier, new Item.Properties().attributes(GiantSwordItem.createGiantAttributes(tier, damage, speed))));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> travellersArmorItem(String path, ArmorItem.Type type, Item.Properties properties, int slots, int durability, boolean belt) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), belt
                ? new TravellersArmorBeltItem(type, properties, slots, durability)
                : new TravellersArmorItem(type, properties, slots, durability));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> travellersGogglesItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new TravellersGogglesItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> mysticCrownItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MysticCrownItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> moonDialItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MoonDialItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> pocketWatchItem(String path, Item.Properties properties) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new PocketWatchItem(properties));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> mazebreakerPickItem(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(PickaxeItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MazebreakerPickItem(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> minotaurAxeItem(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(AxeItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MinotaurAxeItem(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> magicPaintingItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new MagicPaintingItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> lampOfCindersItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new LampOfCindersItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> tripleBowItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new TripleBowItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> seekerBowItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new SeekerBowItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> enderBowItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new EnderBowItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> iceBowItem(String path, Item.Properties properties, Item fallback) {
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new IceBowItem(properties, fallback));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> armor(String path, net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, int durabilityFactor, Rarity rarity, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().durability(type.getDurability(durabilityFactor)).rarity(rarity);
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexArmorItem(material, type, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> sword(String path, Tier tier, int damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexSwordItem(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> pickaxe(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(PickaxeItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexDiggerItem.Pickaxe(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> axe(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(AxeItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexDiggerItem.Axe(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> shovel(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(ShovelItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexDiggerItem.Shovel(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }

    private static TFRegistryObject<Item> hoe(String path, Tier tier, float damage, float speed, Item fallback, int cmd) {
        Item.Properties props = new Item.Properties().attributes(HoeItem.createAttributes(tier, damage, speed));
        Item registered = Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), new CodexDiggerItem.Hoe(tier, props, fallback, cmd));
        return new TFRegistryObject<>(registered);
    }
}
