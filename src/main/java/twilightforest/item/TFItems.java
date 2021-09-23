package twilightforest.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFConstants;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TwilightArmorMaterial;
import twilightforest.enums.TwilightItemTier;

import javax.annotation.Nullable;
import java.util.UUID;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.food.FoodProperties;

import static twilightforest.TwilightForestMod.creativeTab;

public class TFItems {
	public static final FoodProperties EXPERIMENT_115 = new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
	public static final FoodProperties HYDRA_CHOP = new FoodProperties.Builder().nutrition(18).saturationMod(2.0F).meat().effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodProperties MAZE_WAFER = new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build();
	public static final FoodProperties MEEF_COOKED = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build();
	public static final FoodProperties MEEF_RAW = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build();
	public static final FoodProperties MEEF_STROGANOFF = new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).alwaysEat().build();
	public static final FoodProperties VENISON_COOKED = new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build();
	public static final FoodProperties VENISON_RAW = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build();
	public static final FoodProperties TORCHBERRIES = new FoodProperties.Builder().alwaysEat().effect(new MobEffectInstance(MobEffects.GLOWING, 100, 0), 0.75F).build();

	public static final UUID GIANT_REACH_MODIFIER = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	//public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);

	public static final Item naga_scale = Registry.register(Registry.ITEM, TFConstants.ID + ":naga_scale", new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item naga_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":naga_chestplate", new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item naga_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":naga_leggings", new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item twilight_scepter = Registry.register(Registry.ITEM, TFConstants.ID + ":twilight_scepter", new TwilightWandItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final Item lifedrain_scepter = Registry.register(Registry.ITEM, TFConstants.ID + ":lifedrain_scepter", new LifedrainScepterItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final Item zombie_scepter = Registry.register(Registry.ITEM, TFConstants.ID + ":zombie_scepter", new ZombieWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	public static final Item shield_scepter = Registry.register(Registry.ITEM, TFConstants.ID + ":shield_scepter", new FortificationWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	//Registry.register(Registry.ITEM, TwilightForestMod.ID + ":Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final Item ore_meter = Registry.register(Registry.ITEM, TFConstants.ID + ":ore_meter", new OreMeterItem(defaultBuilder()));
	public static final Item magic_map = Registry.register(Registry.ITEM, TFConstants.ID + ":magic_map", new MagicMapItem(new Item.Properties().stacksTo(1)));
	public static final Item maze_map = Registry.register(Registry.ITEM, TFConstants.ID + ":maze_map", new MazeMapItem(false, new Item.Properties().stacksTo(1)));
	public static final Item ore_map = Registry.register(Registry.ITEM, TFConstants.ID + ":ore_map", new MazeMapItem(true, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final Item raven_feather = Registry.register(Registry.ITEM, TFConstants.ID + ":raven_feather", new Item(defaultBuilder()));
	public static final Item magic_map_focus = Registry.register(Registry.ITEM, TFConstants.ID + ":magic_map_focus", new Item(defaultBuilder()));
	public static final Item maze_map_focus = Registry.register(Registry.ITEM, TFConstants.ID + ":maze_map_focus", new Item(defaultBuilder()));
	public static final Item magic_map_empty = Registry.register(Registry.ITEM, TFConstants.ID + ":magic_map_empty", new EmptyMagicMapItem(defaultBuilder()));
	public static final Item maze_map_empty = Registry.register(Registry.ITEM, TFConstants.ID + ":maze_map_empty", new EmptyMazeMapItem(false, defaultBuilder()));
	public static final Item ore_map_empty = Registry.register(Registry.ITEM, TFConstants.ID + ":ore_map_empty", new EmptyMazeMapItem(true, defaultBuilder()));
	public static final Item liveroot = Registry.register(Registry.ITEM, TFConstants.ID + ":liveroot", new Item(defaultBuilder()));
	public static final Item ironwood_raw = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_raw", new Item(defaultBuilder()));
	public static final Item ironwood_ingot = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_ingot", new Item(defaultBuilder()));
	public static final Item ironwood_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_helmet", new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.HEAD, defaultBuilder()));
	public static final Item ironwood_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_chestplate", new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.CHEST, defaultBuilder()));
	public static final Item ironwood_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_leggings", new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.LEGS, defaultBuilder()));
	public static final Item ironwood_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_boots", new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.FEET, defaultBuilder()));
	public static final Item ironwood_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_sword", new IronwoodSwordItem(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final Item ironwood_shovel = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_shovel", new IronwoodShovelItem(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final Item ironwood_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_pickaxe", new IronwoodPickItem(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final Item ironwood_axe = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_axe", new IronwoodAxeItem(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final Item ironwood_hoe = Registry.register(Registry.ITEM, TFConstants.ID + ":ironwood_hoe", new IronwoodHoeItem(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final Item torchberries = Registry.register(Registry.ITEM, TFConstants.ID + ":torchberries", new Item(defaultBuilder().food(TFItems.TORCHBERRIES)));
	public static final Item raw_venison = Registry.register(Registry.ITEM, TFConstants.ID + ":raw_venison", new Item(defaultBuilder().food(TFItems.VENISON_RAW)));
	public static final Item cooked_venison = Registry.register(Registry.ITEM, TFConstants.ID + ":cooked_venison", new Item(defaultBuilder().food(TFItems.VENISON_COOKED)));
	public static final Item hydra_chop = Registry.register(Registry.ITEM, TFConstants.ID + ":hydra_chop", new HydraChopItem(defaultBuilder().fireResistant().food(TFItems.HYDRA_CHOP).rarity(Rarity.UNCOMMON)));
	public static final Item fiery_blood = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_blood", new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_tears = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_tears", new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_ingot = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_ingot", new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_helmet", new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.HEAD, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_chestplate", new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.CHEST, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_leggings", new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.LEGS, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_boots", new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.FEET, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_sword", new FierySwordItem(TwilightItemTier.TOOL_FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item fiery_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":fiery_pickaxe", new FieryPickItem(TwilightItemTier.TOOL_FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item steeleaf_ingot = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_ingot", new Item(defaultBuilder()));
	public static final Item steeleaf_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_helmet", new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.HEAD, defaultBuilder()));
	public static final Item steeleaf_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_chestplate", new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.CHEST, defaultBuilder()));
	public static final Item steeleaf_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_leggings", new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.LEGS, defaultBuilder()));
	public static final Item steeleaf_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_boots", new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.FEET, defaultBuilder()));
	public static final Item steeleaf_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_sword", new SteeleafSwordItem(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final Item steeleaf_shovel = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_shovel", new SteeleafShovelItem(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final Item steeleaf_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_pickaxe", new SteeleafPickItem(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final Item steeleaf_axe = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_axe", new SteeleafAxeItem(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final Item steeleaf_hoe = Registry.register(Registry.ITEM, TFConstants.ID + ":steeleaf_hoe", new SteeleafHoeItem(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final Item minotaur_axe_gold = Registry.register(Registry.ITEM, TFConstants.ID + ":minotaur_axe_gold", new MinotaurAxeItem(Tiers.GOLD, defaultBuilder().rarity(Rarity.COMMON)));
	public static final Item minotaur_axe = Registry.register(Registry.ITEM, TFConstants.ID + ":minotaur_axe", new MinotaurAxeItem(Tiers.DIAMOND, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item mazebreaker_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":mazebreaker_pickaxe", new MazebreakerPickItem(Tiers.DIAMOND, defaultBuilder()/*.setNoRepair()*/.rarity(Rarity.RARE)));
	public static final Item transformation_powder = Registry.register(Registry.ITEM, TFConstants.ID + ":transformation_powder", new TransformPowderItem(defaultBuilder()));
	public static final Item raw_meef = Registry.register(Registry.ITEM, TFConstants.ID + ":raw_meef", new Item(defaultBuilder().food(TFItems.MEEF_RAW)));
	public static final Item cooked_meef = Registry.register(Registry.ITEM, TFConstants.ID + ":cooked_meef", new Item(defaultBuilder().food(TFItems.MEEF_COOKED)));
	public static final Item meef_stroganoff = Registry.register(Registry.ITEM, TFConstants.ID + ":meef_stroganoff", new BowlFoodItem(defaultBuilder().food(TFItems.MEEF_STROGANOFF).stacksTo(1)));
	public static final Item maze_wafer = Registry.register(Registry.ITEM, TFConstants.ID + ":maze_wafer", new Item(defaultBuilder().food(TFItems.MAZE_WAFER)));
	public static final Item ore_magnet = Registry.register(Registry.ITEM, TFConstants.ID + ":ore_magnet", new OreMagnetItem(defaultBuilder().durability(12)));
	public static final Item crumble_horn = Registry.register(Registry.ITEM, TFConstants.ID + ":crumble_horn", new CrumbleHornItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final Item peacock_fan = Registry.register(Registry.ITEM, TFConstants.ID + ":peacock_fan", new PeacockFanItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final Item moonworm_queen = Registry.register(Registry.ITEM, TFConstants.ID + ":moonworm_queen", new MoonwormQueenItem(defaultBuilder()/*.setNoRepair()*/.durability(256).rarity(Rarity.RARE)));
	public static final Item brittle_flask = Registry.register(Registry.ITEM, TFConstants.ID + ":brittle_potion_flask", new BrittleFlaskItem(defaultBuilder().stacksTo(1)));
	public static final Item greater_flask = Registry.register(Registry.ITEM, TFConstants.ID + ":greater_potion_flask", new GreaterFlaskItem(defaultBuilder().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1)));
	public static final Item charm_of_life_1 = Registry.register(Registry.ITEM, TFConstants.ID + ":charm_of_life_1", new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item charm_of_life_2 = Registry.register(Registry.ITEM, TFConstants.ID + ":charm_of_life_2", new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item charm_of_keeping_1 = Registry.register(Registry.ITEM, TFConstants.ID + ":charm_of_keeping_1", new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item charm_of_keeping_2 = Registry.register(Registry.ITEM, TFConstants.ID + ":charm_of_keeping_2", new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item charm_of_keeping_3 = Registry.register(Registry.ITEM, TFConstants.ID + ":charm_of_keeping_3", new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item tower_key = Registry.register(Registry.ITEM, TFConstants.ID + ":tower_key", new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item borer_essence = Registry.register(Registry.ITEM, TFConstants.ID + ":borer_essence", new Item(defaultBuilder()));
	public static final Item carminite = Registry.register(Registry.ITEM, TFConstants.ID + ":carminite", new Item(defaultBuilder()));
	public static final Item experiment_115 = Registry.register(Registry.ITEM, TFConstants.ID + ":experiment_115", new Experiment115Item(TFBlocks.experiment_115, defaultBuilder().food(TFItems.EXPERIMENT_115)));
	public static final Item armor_shard = Registry.register(Registry.ITEM, TFConstants.ID + ":armor_shard", new Item(defaultBuilder()));
	public static final Item knightmetal_ingot = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_ingot", new Item(defaultBuilder()));
	public static final Item armor_shard_cluster = Registry.register(Registry.ITEM, TFConstants.ID + ":armor_shard_cluster", new Item(defaultBuilder()));
	public static final Item knightmetal_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_helmet", new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.HEAD, defaultBuilder()));
	public static final Item knightmetal_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_chestplate", new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.CHEST, defaultBuilder()));
	public static final Item knightmetal_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_leggings", new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.LEGS, defaultBuilder()));
	public static final Item knightmetal_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_boots", new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.FEET, defaultBuilder()));
	public static final Item knightmetal_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_sword", new KnightmetalSwordItem(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final Item knightmetal_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_pickaxe", new KnightmetalPickItem(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final Item knightmetal_axe = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_axe", new KnightmetalAxeItem(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final Item knightmetal_shield = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_shield", new KnightmetalShieldItem(defaultBuilder().durability(1024)));
	public static final Item phantom_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":phantom_helmet", new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item phantom_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":phantom_chestplate", new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item lamp_of_cinders = Registry.register(Registry.ITEM, TFConstants.ID + ":lamp_of_cinders", new LampOfCindersItem(defaultBuilder().fireResistant().durability(1024).rarity(Rarity.UNCOMMON)));
	public static final Item alpha_fur = Registry.register(Registry.ITEM, TFConstants.ID + ":alpha_fur", new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item yeti_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":yeti_helmet", new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item yeti_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":yeti_chestplate", new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item yeti_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":yeti_leggings", new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item yeti_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":yeti_boots", new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.FEET, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item ice_bomb = Registry.register(Registry.ITEM, TFConstants.ID + ":ice_bomb", new IceBombItem(defaultBuilder().stacksTo(16)));
	public static final Item arctic_fur = Registry.register(Registry.ITEM, TFConstants.ID + ":arctic_fur", new Item(defaultBuilder()));
	public static final Item arctic_helmet = Registry.register(Registry.ITEM, TFConstants.ID + ":arctic_helmet", new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.HEAD, defaultBuilder()));
	public static final Item arctic_chestplate = Registry.register(Registry.ITEM, TFConstants.ID + ":arctic_chestplate", new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.CHEST, defaultBuilder()));
	public static final Item arctic_leggings = Registry.register(Registry.ITEM, TFConstants.ID + ":arctic_leggings", new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.LEGS, defaultBuilder()));
	public static final Item arctic_boots = Registry.register(Registry.ITEM, TFConstants.ID + ":arctic_boots", new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.FEET, defaultBuilder()));
	public static final Item magic_beans = Registry.register(Registry.ITEM, TFConstants.ID + ":magic_beans", new MagicBeansItem(defaultBuilder()));
	public static final Item giant_pickaxe = Registry.register(Registry.ITEM, TFConstants.ID + ":giant_pickaxe", new GiantPickItem(TwilightItemTier.TOOL_GIANT, defaultBuilder()));
	public static final Item giant_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":giant_sword", new GiantSwordItem(TwilightItemTier.TOOL_GIANT, defaultBuilder()));
	public static final Item triple_bow = Registry.register(Registry.ITEM, TFConstants.ID + ":triple_bow", new TripleBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final Item seeker_bow = Registry.register(Registry.ITEM, TFConstants.ID + ":seeker_bow", new SeekerBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final Item ice_bow = Registry.register(Registry.ITEM, TFConstants.ID + ":ice_bow", new IceBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final Item ender_bow = Registry.register(Registry.ITEM, TFConstants.ID + ":ender_bow", new EnderBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final Item ice_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":ice_sword", new IceSwordItem(TwilightItemTier.TOOL_ICE, defaultBuilder()));
	public static final Item glass_sword = Registry.register(Registry.ITEM, TFConstants.ID + ":glass_sword", new GlassSwordItem(TwilightItemTier.TOOL_GLASS, defaultBuilder()/*.setNoRepair()*/.rarity(Rarity.RARE)));
	public static final Item knightmetal_ring = Registry.register(Registry.ITEM, TFConstants.ID + ":knightmetal_ring", new Item(defaultBuilder()));
	public static final Item block_and_chain = Registry.register(Registry.ITEM, TFConstants.ID + ":block_and_chain", new ChainBlockItem(defaultBuilder().durability(99)));
	public static final Item cube_talisman = Registry.register(Registry.ITEM, TFConstants.ID + ":cube_talisman", new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item cube_of_annihilation = Registry.register(Registry.ITEM, TFConstants.ID + ":cube_of_annihilation", new CubeOfAnnihilationItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item moon_dial = Registry.register(Registry.ITEM, TFConstants.ID + ":moon_dial", new Item(defaultBuilder()));
	public static final Item naga_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":naga_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_NAGA"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item lich_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":lich_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_LICH"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item minoshroom_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":minoshroom_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_MINOSHROOM"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item hydra_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":hydra_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_HYDRA"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item knight_phantom_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":knight_phantom_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_PHANTOMS"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item ur_ghast_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":ur_ghast_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_UR_GHAST"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item alpha_yeti_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":alpha_yeti_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_ALPHA_YETI"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item snow_queen_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":snow_queen_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_SNOW_QUEEN"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));
	public static final Item questing_ram_banner_pattern = Registry.register(Registry.ITEM, TFConstants.ID + ":questing_ram_banner_pattern", new BannerPatternItem(ClassTinkerers.getEnum(BannerPattern.class, "TF_QUEST_RAM"), defaultBuilder().stacksTo(1).rarity(TFConstants.getRarity())));

	public static void init(){

	}

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(creativeTab);
	}

	public static Item.Properties unstackable() {
		return defaultBuilder().stacksTo(1);
	}

	@Environment(EnvType.CLIENT)
	public static void addItemModelProperties() {
		ItemProperties.register(cube_of_annihilation, TFConstants.prefix("thrown"), (stack, world, entity, idk) ->
				CubeOfAnnihilationItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(TFItems.knightmetal_shield, new ResourceLocation("blocking"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(moon_dial, new ResourceLocation("phase"), new ClampedItemPropertyFunction() {
			@Override
			public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entityBase, int idk) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getFrame();

				if (world == null && entity != null) world = (ClientLevel) entity.level;

				return world == null ? 0.0F : (float) (world.dimensionType().natural() ? Mth.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
			}

			@Override
			public float unclampedCall(ItemStack itemStack, @org.jetbrains.annotations.Nullable ClientLevel clientLevel, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, int i) {
				return call(itemStack, clientLevel, livingEntity, i);
			}

			@Environment(EnvType.CLIENT)
			double rotation;
			@Environment(EnvType.CLIENT)
			double rota;
			@Environment(EnvType.CLIENT)
			long lastUpdateTick;

			@Environment(EnvType.CLIENT)
			private double wobble(Level world, double rotation) {
				if (world.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = world.getGameTime();
					double delta = rotation - this.rotation;
					delta = Mth.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
					this.rota += delta * 0.1D;
					this.rota *= 0.9D;
					this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
				}
				return this.rotation;
			}
		});

		ItemProperties.register(moonworm_queen, TFConstants.prefix("alt"), (stack, world, entity, idk) -> {
			if (entity != null && entity.getUseItem() == stack) {
				int useTime = stack.getUseDuration() - entity.getUseItemRemainingTicks();
				if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}
			return 0;
		});

		ItemProperties.register(TFItems.ender_bow, new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ender_bow, new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ice_bow, new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ice_bow, new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.seeker_bow, new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.seeker_bow, new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.triple_bow, new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.triple_bow, new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(ore_magnet, new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}});

		ItemProperties.register(ore_magnet, new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(block_and_chain, TFConstants.prefix("thrown"), (stack, world, entity, idk) ->
				ChainBlockItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(experiment_115, Experiment115Item.THINK, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("think") ? 1 : 0);

		ItemProperties.register(experiment_115, Experiment115Item.FULL, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("full") ? 1 : 0);

		ItemProperties.register(TFItems.brittle_flask, TFConstants.prefix("breakage"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Breakage"));

		ItemProperties.register(TFItems.brittle_flask, TFConstants.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));

		ItemProperties.register(TFItems.greater_flask, TFConstants.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));
	}
}
