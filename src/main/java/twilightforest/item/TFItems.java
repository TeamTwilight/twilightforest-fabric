package twilightforest.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TwilightArmorMaterial;
import twilightforest.util.TwilightItemTier;

import javax.annotation.Nullable;
import java.util.UUID;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class TFItems {
	public static final FoodProperties EXPERIMENT_115 = new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
	public static final FoodProperties HYDRA_CHOP = new FoodProperties.Builder().nutrition(18).saturationMod(2.0F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodProperties MAZE_WAFER = new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build();
	public static final FoodProperties MEEF_COOKED = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build();
	public static final FoodProperties MEEF_RAW = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build();
	public static final FoodProperties MEEF_STROGANOFF = new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build();
	public static final FoodProperties VENISON_COOKED = new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build();
	public static final FoodProperties VENISON_RAW = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build();
	public static final FoodProperties TORCHBERRIES = new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.GLOWING, 100, 0), 0.75F).build();

	public static final UUID GIANT_REACH_MODIFIER = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);

	public static final RegistryObject<Item> naga_scale = ITEMS.register("naga_scale", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> naga_chestplate = ITEMS.register("naga_chestplate", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> naga_leggings = ITEMS.register("naga_leggings", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> twilight_scepter = ITEMS.register("twilight_scepter", () -> new TwilightWandItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> lifedrain_scepter = ITEMS.register("lifedrain_scepter", () -> new LifedrainScepterItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> zombie_scepter = ITEMS.register("zombie_scepter", () -> new ZombieWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> shield_scepter = ITEMS.register("shield_scepter", () -> new FortificationWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	//items.register("Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final RegistryObject<Item> ore_meter = ITEMS.register("ore_meter", () -> new OreMeterItem(defaultBuilder()));
	public static final RegistryObject<Item> magic_map = ITEMS.register("magic_map", () -> new MagicMapItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> maze_map = ITEMS.register("maze_map", () -> new MazeMapItem(false, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> ore_map = ITEMS.register("ore_map", () -> new MazeMapItem(true, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> raven_feather = ITEMS.register("raven_feather", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> magic_map_focus = ITEMS.register("magic_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> maze_map_focus = ITEMS.register("maze_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> magic_map_empty = ITEMS.register("magic_map_empty", () -> new EmptyMagicMapItem(defaultBuilder()));
	public static final RegistryObject<Item> maze_map_empty = ITEMS.register("maze_map_empty", () -> new EmptyMazeMapItem(false, defaultBuilder()));
	public static final RegistryObject<Item> ore_map_empty = ITEMS.register("ore_map_empty", () -> new EmptyMazeMapItem(true, defaultBuilder()));
	public static final RegistryObject<Item> liveroot = ITEMS.register("liveroot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_raw = ITEMS.register("ironwood_raw", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_ingot = ITEMS.register("ironwood_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_helmet = ITEMS.register("ironwood_helmet", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_chestplate = ITEMS.register("ironwood_chestplate", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_leggings = ITEMS.register("ironwood_leggings", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_boots = ITEMS.register("ironwood_boots", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_sword = ITEMS.register("ironwood_sword", () -> new IronwoodSwordItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_shovel = ITEMS.register("ironwood_shovel", () -> new IronwoodShovelItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_pickaxe = ITEMS.register("ironwood_pickaxe", () -> new IronwoodPickItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_axe = ITEMS.register("ironwood_axe", () -> new IronwoodAxeItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_hoe = ITEMS.register("ironwood_hoe", () -> new IronwoodHoeItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> torchberries = ITEMS.register("torchberries", () -> new Item(defaultBuilder().food(TFItems.TORCHBERRIES)));
	public static final RegistryObject<Item> raw_venison = ITEMS.register("raw_venison", () -> new Item(defaultBuilder().food(TFItems.VENISON_RAW)));
	public static final RegistryObject<Item> cooked_venison = ITEMS.register("cooked_venison", () -> new Item(defaultBuilder().food(TFItems.VENISON_COOKED)));
	public static final RegistryObject<Item> hydra_chop = ITEMS.register("hydra_chop", () -> new HydraChopItem(defaultBuilder().fireResistant().food(TFItems.HYDRA_CHOP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_blood = ITEMS.register("fiery_blood", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_tears = ITEMS.register("fiery_tears", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_ingot = ITEMS.register("fiery_ingot", () -> new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_helmet = ITEMS.register("fiery_helmet", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.HEAD, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_chestplate = ITEMS.register("fiery_chestplate", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.CHEST, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_leggings = ITEMS.register("fiery_leggings", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.LEGS, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_boots = ITEMS.register("fiery_boots", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.FEET, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_sword = ITEMS.register("fiery_sword", () -> new FierySwordItem(TwilightItemTier.FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_pickaxe = ITEMS.register("fiery_pickaxe", () -> new FieryPickItem(TwilightItemTier.FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> steeleaf_ingot = ITEMS.register("steeleaf_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_helmet = ITEMS.register("steeleaf_helmet", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_chestplate = ITEMS.register("steeleaf_chestplate", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_leggings = ITEMS.register("steeleaf_leggings", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_boots = ITEMS.register("steeleaf_boots", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_sword = ITEMS.register("steeleaf_sword", () -> new SteeleafSwordItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_shovel = ITEMS.register("steeleaf_shovel", () -> new SteeleafShovelItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_pickaxe = ITEMS.register("steeleaf_pickaxe", () -> new SteeleafPickItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_axe = ITEMS.register("steeleaf_axe", () -> new SteeleafAxeItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_hoe = ITEMS.register("steeleaf_hoe", () -> new SteeleafHoeItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> minotaur_axe_gold = ITEMS.register("minotaur_axe_gold", () -> new MinotaurAxeItem(Tiers.GOLD, defaultBuilder().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> minotaur_axe = ITEMS.register("minotaur_axe", () -> new MinotaurAxeItem(Tiers.DIAMOND, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> mazebreaker_pickaxe = ITEMS.register("mazebreaker_pickaxe", () -> new MazebreakerPickItem(Tiers.DIAMOND, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> transformation_powder = ITEMS.register("transformation_powder", () -> new TransformPowderItem(defaultBuilder()));
	public static final RegistryObject<Item> raw_meef = ITEMS.register("raw_meef", () -> new Item(defaultBuilder().food(TFItems.MEEF_RAW)));
	public static final RegistryObject<Item> cooked_meef = ITEMS.register("cooked_meef", () -> new Item(defaultBuilder().food(TFItems.MEEF_COOKED)));
	public static final RegistryObject<Item> meef_stroganoff = ITEMS.register("meef_stroganoff", () -> new BowlFoodItem(defaultBuilder().food(TFItems.MEEF_STROGANOFF).stacksTo(1)));
	public static final RegistryObject<Item> maze_wafer = ITEMS.register("maze_wafer", () -> new Item(defaultBuilder().food(TFItems.MAZE_WAFER)));
	public static final RegistryObject<Item> ore_magnet = ITEMS.register("ore_magnet", () -> new OreMagnetItem(defaultBuilder().durability(12)));
	public static final RegistryObject<Item> crumble_horn = ITEMS.register("crumble_horn", () -> new CrumbleHornItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> peacock_fan = ITEMS.register("peacock_fan", () -> new PeacockFanItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> moonworm_queen = ITEMS.register("moonworm_queen", () -> new MoonwormQueenItem(defaultBuilder().setNoRepair().durability(256).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> brittle_flask = ITEMS.register("brittle_potion_flask", () -> new BrittleFlaskItem(defaultBuilder().stacksTo(1)));
	public static final RegistryObject<Item> greater_flask = ITEMS.register("greater_potion_flask", () -> new GreaterFlaskItem(defaultBuilder().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1)));
	public static final RegistryObject<Item> charm_of_life_1 = ITEMS.register("charm_of_life_1", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_life_2 = ITEMS.register("charm_of_life_2", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_1 = ITEMS.register("charm_of_keeping_1", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_2 = ITEMS.register("charm_of_keeping_2", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_3 = ITEMS.register("charm_of_keeping_3", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> tower_key = ITEMS.register("tower_key", () -> new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> borer_essence = ITEMS.register("borer_essence", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> carminite = ITEMS.register("carminite", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> experiment_115 = ITEMS.register("experiment_115", () -> new Experiment115Item(TFBlocks.experiment_115.get(), defaultBuilder().food(TFItems.EXPERIMENT_115)));
	public static final RegistryObject<Item> armor_shard = ITEMS.register("armor_shard", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_ingot = ITEMS.register("knightmetal_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> armor_shard_cluster = ITEMS.register("armor_shard_cluster", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_helmet = ITEMS.register("knightmetal_helmet", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_chestplate = ITEMS.register("knightmetal_chestplate", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_leggings = ITEMS.register("knightmetal_leggings", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_boots = ITEMS.register("knightmetal_boots", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_sword = ITEMS.register("knightmetal_sword", () -> new KnightmetalSwordItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_pickaxe = ITEMS.register("knightmetal_pickaxe", () -> new KnightmetalPickItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_axe = ITEMS.register("knightmetal_axe", () -> new KnightmetalAxeItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_shield = ITEMS.register("knightmetal_shield", () -> new KnightmetalShieldItem(defaultBuilder().durability(1024)));
	public static final RegistryObject<Item> phantom_helmet = ITEMS.register("phantom_helmet", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> phantom_chestplate = ITEMS.register("phantom_chestplate", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> lamp_of_cinders = ITEMS.register("lamp_of_cinders", () -> new LampOfCindersItem(defaultBuilder().fireResistant().durability(1024).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> alpha_fur = ITEMS.register("alpha_fur", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_helmet = ITEMS.register("yeti_helmet", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_chestplate = ITEMS.register("yeti_chestplate", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_leggings = ITEMS.register("yeti_leggings", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_boots = ITEMS.register("yeti_boots", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.FEET, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ice_bomb = ITEMS.register("ice_bomb", () -> new IceBombItem(defaultBuilder().stacksTo(16)));
	public static final RegistryObject<Item> arctic_fur = ITEMS.register("arctic_fur", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> arctic_helmet = ITEMS.register("arctic_helmet", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> arctic_chestplate = ITEMS.register("arctic_chestplate", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> arctic_leggings = ITEMS.register("arctic_leggings", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> arctic_boots = ITEMS.register("arctic_boots", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> magic_beans = ITEMS.register("magic_beans", () -> new MagicBeansItem(defaultBuilder()));
	public static final RegistryObject<Item> giant_pickaxe = ITEMS.register("giant_pickaxe", () -> new GiantPickItem(TwilightItemTier.GIANT, defaultBuilder()));
	public static final RegistryObject<Item> giant_sword = ITEMS.register("giant_sword", () -> new GiantSwordItem(TwilightItemTier.GIANT, defaultBuilder()));
	public static final RegistryObject<Item> triple_bow = ITEMS.register("triple_bow", () -> new TripleBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> seeker_bow = ITEMS.register("seeker_bow", () -> new SeekerBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ice_bow = ITEMS.register("ice_bow", () -> new IceBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ender_bow = ITEMS.register("ender_bow", () -> new EnderBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ice_sword = ITEMS.register("ice_sword", () -> new IceSwordItem(TwilightItemTier.ICE, defaultBuilder()));
	public static final RegistryObject<Item> glass_sword = ITEMS.register("glass_sword", () -> new GlassSwordItem(TwilightItemTier.GLASS, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> knightmetal_ring = ITEMS.register("knightmetal_ring", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> block_and_chain = ITEMS.register("block_and_chain", () -> new ChainBlockItem(defaultBuilder().durability(99)));
	public static final RegistryObject<Item> cube_talisman = ITEMS.register("cube_talisman", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> cube_of_annihilation = ITEMS.register("cube_of_annihilation", () -> new CubeOfAnnihilationItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> moon_dial = ITEMS.register("moon_dial", () -> new Item(defaultBuilder()));

	public static CreativeModeTab creativeTab = new CreativeModeTab(TwilightForestMod.ID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(TFBlocks.twilight_portal_miniature_structure.get());
		}
	};

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(creativeTab);
	}

	public static Item.Properties unstackable() {
		return defaultBuilder().stacksTo(1);
	}

	@OnlyIn(Dist.CLIENT)
	public static void addItemModelProperties() {
		ItemProperties.register(cube_of_annihilation.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity, idk) ->
				CubeOfAnnihilationItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(TFItems.knightmetal_shield.get(), new ResourceLocation("blocking"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(moon_dial.get(), new ResourceLocation("phase"), new ItemPropertyFunction() {
			@Override
			public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entityBase, int idk) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getFrame();

				if (world == null && entity != null) world = (ClientLevel) entity.level;

				return world == null ? 0.0F : (float) (world.dimensionType().natural() ? Mth.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
			}

			@OnlyIn(Dist.CLIENT)
			double rotation;
			@OnlyIn(Dist.CLIENT)
			double rota;
			@OnlyIn(Dist.CLIENT)
			long lastUpdateTick;

			@OnlyIn(Dist.CLIENT)
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

		ItemProperties.register(moonworm_queen.get(), TwilightForestMod.prefix("alt"), (stack, world, entity, idk) -> {
			if (entity != null && entity.getUseItem() == stack) {
				int useTime = stack.getUseDuration() - entity.getUseItemRemainingTicks();
				if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}
			return 0;
		});

		ItemProperties.register(TFItems.ender_bow.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ender_bow.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ice_bow.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ice_bow.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.seeker_bow.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.seeker_bow.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.triple_bow.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.triple_bow.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(ore_magnet.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}});

		ItemProperties.register(ore_magnet.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(block_and_chain.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity, idk) ->
				ChainBlockItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(experiment_115.get(), Experiment115Item.THINK, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("think") ? 1 : 0);

		ItemProperties.register(experiment_115.get(), Experiment115Item.FULL, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("full") ? 1 : 0);

		ItemProperties.register(TFItems.brittle_flask.get(), TwilightForestMod.prefix("breakage"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Breakage"));

		ItemProperties.register(TFItems.brittle_flask.get(), TwilightForestMod.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));

		ItemProperties.register(TFItems.greater_flask.get(), TwilightForestMod.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));
	}
}
