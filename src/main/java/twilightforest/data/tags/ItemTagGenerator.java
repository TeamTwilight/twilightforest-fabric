package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
	public static final TagKey<Item> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
	public static final TagKey<Item> CANOPY_LOGS = create("canopy_logs");
	public static final TagKey<Item> MANGROVE_LOGS = create("mangrove_logs");
	public static final TagKey<Item> DARKWOOD_LOGS = create("darkwood_logs");
	public static final TagKey<Item> TIME_LOGS = create("timewood_logs");
	public static final TagKey<Item> TRANSFORMATION_LOGS = create("transwood_logs");
	public static final TagKey<Item> MINING_LOGS = create("mining_logs");
	public static final TagKey<Item> SORTING_LOGS = create("sortwood_logs");
	public static final TagKey<Item> TWILIGHT_LOGS = create("logs");

	public static final TagKey<Item> BANISTERS = create("banisters");
	public static final TagKey<Item> DRYING_RACKS = create("drying_racks");

	public static final TagKey<Item> PAPER = makeCommonTag("paper");

	public static final TagKey<Item> TOWERWOOD = create("towerwood");

	public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");

	public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
	public static final TagKey<Item> CARMINITE_GEMS = makeCommonTag("gems/carminite");
	public static final TagKey<Item> FIERY_INGOTS = makeCommonTag("ingots/fiery");
	public static final TagKey<Item> IRONWOOD_INGOTS = makeCommonTag("ingots/ironwood");
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = makeCommonTag("ingots/knightmetal");
	public static final TagKey<Item> STEELEAF_INGOTS = makeCommonTag("ingots/steeleaf");
	public static final TagKey<Item> WROUGHT_IRON_INGOTS = makeCommonTag("ingots/wrought_iron");
	public static final TagKey<Item> COPPER_NUGGETS = makeCommonTag("nuggets/copper");

	public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur");
	public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite");
	public static final TagKey<Item> STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery");
	public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood");
	public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal");
	public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf");

	public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = makeCommonTag("raw_materials/ironwood");
	public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = makeCommonTag("raw_materials/knightmetal");

	public static final TagKey<Item> PORTAL_ACTIVATOR = create("portal/activator");

	public static final TagKey<Item> WIP = create("wip");

	public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = create("kobold_pacification_breads");
	public static final TagKey<Item> BOAR_TEMPT_ITEMS = create("boar_tempt_items");
	public static final TagKey<Item> DEER_TEMPT_ITEMS = create("deer_tempt_items");
	public static final TagKey<Item> DWARF_RABBIT_TEMPT_ITEMS = create("dwarf_rabbit_tempt_items");
	public static final TagKey<Item> PENGUIN_TEMPT_ITEMS = create("penguin_tempt_items");
	public static final TagKey<Item> RAVEN_TEMPT_ITEMS = create("raven_tempt_items");
	public static final TagKey<Item> SQUIRREL_TEMPT_ITEMS = create("squirrel_tempt_items");
	public static final TagKey<Item> TINY_BIRD_TEMPT_ITEMS = create("tiny_bird_tempt_items");

	public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = create("banned_uncrafting_ingredients");
	public static final TagKey<Item> BANNED_UNCRAFTABLES = create("banned_uncraftables");
	public static final TagKey<Item> UNCRAFTING_IGNORES_COST = create("uncrafting_ignores_cost");

	public static final TagKey<Item> KEPT_ON_DEATH = create("kept_on_death");
	public static final TagKey<Item> BLOCK_AND_CHAIN_ENCHANTABLE = create("enchantable/block_and_chain");

	public static final TagKey<Item> TRAVELLERS_BELT_BLACKLISTED = create("travellers_belt_blacklisted");
	public static final TagKey<Item> TRAVELLERS_AGILE_RANGER_WHITELISTED = create("travellers_agile_ranger_whitelisted");
	public static final TagKey<Item> TRAVELLERS_AGILE_RANGER_BLACKLISTED = create("travellers_agile_ranger_blacklisted");

	public static final TagKey<Item> REPAIRS_IRONWOOD_TOOLS = create("repairs_ironwood_tools");
	public static final TagKey<Item> REPAIRS_STEELEAF_TOOLS = create("repairs_steeleaf_tools");
	public static final TagKey<Item> REPAIRS_KNIGHTMETAL_TOOLS = create("repairs_knightmetal_tools");
	public static final TagKey<Item> REPAIRS_FIERY_TOOLS = create("repairs_fiery_tools");
	public static final TagKey<Item> REPAIRS_GIANT_TOOLS = create("repairs_giant_tools");
	public static final TagKey<Item> REPAIRS_ICE_TOOLS = create("repairs_ice_tools");
	public static final TagKey<Item> SCEPTERS = create("scepters");
	public static final TagKey<Item> IMMUNE_TO_THORNS = create("immune_to_thorns");

	public static final TagKey<Item> FOODS_JERKY = makeCommonTag("foods/jerky");
	public static final TagKey<Item> NUGGETS_COPPER = makeCommonTag("nuggets/copper");
	public static final TagKey<Item> RENDER_LOWER_ON_DRYING_RACK = create("lower_on_drying_rack");
	public static final TagKey<Item> TROPHIES = create("trophies");
	public static final TagKey<Item> EMPERORS_CLOTH_APPLICABLE = create("emperors_cloth_applicable");

	public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
		super(output, future, blockTagProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);

		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
		getOrCreateTagBuilder(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
			.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
			.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		// Tags.Items.STRIPPED_LOGS/STRIPPED_WOODS are not available in PortingLib 1.21.1
		/*
		getOrCreateTagBuilder(Tags.Items.STRIPPED_LOGS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value().asItem(),
			TFBlocks.STRIPPED_CANOPY_LOG.value().asItem(),
			TFBlocks.STRIPPED_MANGROVE_LOG.value().asItem(),
			TFBlocks.STRIPPED_DARK_LOG.value().asItem(),
			TFBlocks.STRIPPED_TIME_LOG.value().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.value().asItem(),
			TFBlocks.STRIPPED_MINING_LOG.value().asItem(),
			TFBlocks.STRIPPED_SORTING_LOG.value().asItem()
		);
		*/

		// Tags.Items.STRIPPED_LOGS is not available in PortingLib 1.21.1
		/*
		getOrCreateTagBuilder(Tags.Items.STRIPPED_WOODS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value().asItem(),
			TFBlocks.STRIPPED_CANOPY_WOOD.value().asItem(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.value().asItem(),
			TFBlocks.STRIPPED_DARK_WOOD.value().asItem(),
			TFBlocks.STRIPPED_TIME_WOOD.value().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value().asItem(),
			TFBlocks.STRIPPED_MINING_WOOD.value().asItem(),
			TFBlocks.STRIPPED_SORTING_WOOD.value().asItem()
		);
		*/

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);

		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);

		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
		this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);

		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
		this.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		getOrCreateTagBuilder(Tags.Items.STORAGE_BLOCKS)
			.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
			.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);
		this.copy(BlockTagGenerator.BANISTERS, BANISTERS);
		this.copy(BlockTagGenerator.DRYING_RACKS, DRYING_RACKS);

		getOrCreateTagBuilder(PAPER).add(Items.PAPER);
		getOrCreateTagBuilder(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.get());

		getOrCreateTagBuilder(FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());

		getOrCreateTagBuilder(ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		getOrCreateTagBuilder(CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		getOrCreateTagBuilder(FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		getOrCreateTagBuilder(IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		getOrCreateTagBuilder(KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		getOrCreateTagBuilder(STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());
		getOrCreateTagBuilder(WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.get());

		getOrCreateTagBuilder(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		getOrCreateTagBuilder(Tags.Items.INGOTS)
			.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
			.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		getOrCreateTagBuilder(Tags.Items.NUGGETS)
			.add(TFItems.COPPER_NUGGET.asItem());
		getOrCreateTagBuilder(COPPER_NUGGETS)
			.add(TFItems.COPPER_NUGGET.asItem());

		getOrCreateTagBuilder(RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		getOrCreateTagBuilder(RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		getOrCreateTagBuilder(Tags.Items.RAW_MATERIALS).addTag(RAW_MATERIALS_IRONWOOD).addTag(RAW_MATERIALS_KNIGHTMETAL);

		getOrCreateTagBuilder(PORTAL_ACTIVATOR).addOptional(Tags.Items.GEMS_DIAMOND.location());

		getOrCreateTagBuilder(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT.get(), TFItems.CANOPY_BOAT.get(),
			TFItems.MANGROVE_BOAT.get(), TFItems.DARK_BOAT.get(),
			TFItems.TIME_BOAT.get(), TFItems.TRANSFORMATION_BOAT.get(),
			TFItems.MINING_BOAT.get(), TFItems.SORTING_BOAT.get()
		);

		getOrCreateTagBuilder(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT.get(), TFItems.CANOPY_CHEST_BOAT.get(),
			TFItems.MANGROVE_CHEST_BOAT.get(), TFItems.DARK_CHEST_BOAT.get(),
			TFItems.TIME_CHEST_BOAT.get(), TFItems.TRANSFORMATION_CHEST_BOAT.get(),
			TFItems.MINING_CHEST_BOAT.get(), TFItems.SORTING_CHEST_BOAT.get()
		);

		getOrCreateTagBuilder(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_HELMET.get(),
			TFItems.YETI_CHESTPLATE.get(),
			TFItems.YETI_LEGGINGS.get(),
			TFItems.YETI_BOOTS.get(),
			TFItems.TRAVELLERS_VEST.get(),
			TFItems.TRAVELLERS_BOOTS.get()
		);

		getOrCreateTagBuilder(WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN.get(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN.get(),
			TFItems.CUBE_OF_ANNIHILATION.get(),
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.BRAZIER.asItem()
		);

		getOrCreateTagBuilder(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		getOrCreateTagBuilder(BOAR_TEMPT_ITEMS).addOptionalTag(Tags.Items.CROPS_CARROT.location()).addOptionalTag(Tags.Items.CROPS_POTATO.location()).addOptionalTag(Tags.Items.CROPS_BEETROOT.location());
		getOrCreateTagBuilder(DEER_TEMPT_ITEMS).add(Items.APPLE).add(TFItems.SHIKA_SENBEI.get()).addOptionalTag(Tags.Items.CROPS_WHEAT.location());
		getOrCreateTagBuilder(DWARF_RABBIT_TEMPT_ITEMS).add(Items.GOLDEN_CARROT).add(Items.DANDELION).addOptionalTag(Tags.Items.CROPS_CARROT.location());
		getOrCreateTagBuilder(PENGUIN_TEMPT_ITEMS).addOptionalTag(ItemTags.FISHES.location());
		getOrCreateTagBuilder(RAVEN_TEMPT_ITEMS).addOptionalTag(Tags.Items.SEEDS.location());
		getOrCreateTagBuilder(SQUIRREL_TEMPT_ITEMS).addOptionalTag(Tags.Items.SEEDS.location());
		getOrCreateTagBuilder(TINY_BIRD_TEMPT_ITEMS).addOptionalTag(Tags.Items.SEEDS.location());

		getOrCreateTagBuilder(BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER.get());

		getOrCreateTagBuilder(BANNED_UNCRAFTABLES);
		getOrCreateTagBuilder(UNCRAFTING_IGNORES_COST).addOptionalTag(Tags.Items.RODS_WOODEN.location());

		getOrCreateTagBuilder(KEPT_ON_DEATH).add(TFItems.TOWER_KEY.get(), TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());

		getOrCreateTagBuilder(SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.get(), TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get());

		getOrCreateTagBuilder(IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());

		getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_2.get(), TFItems.LAMP_OF_CINDERS.get());

		getOrCreateTagBuilder(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		getOrCreateTagBuilder(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_HELMET.get(),
			TFItems.STEELEAF_HELMET.get(),
			TFItems.KNIGHTMETAL_HELMET.get(),
			TFItems.PHANTOM_HELMET.get(),
			TFItems.FIERY_HELMET.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.YETI_HELMET.get());

		getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_CHESTPLATE.get(),
			TFItems.IRONWOOD_CHESTPLATE.get(),
			TFItems.STEELEAF_CHESTPLATE.get(),
			TFItems.KNIGHTMETAL_CHESTPLATE.get(),
			TFItems.PHANTOM_CHESTPLATE.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.YETI_CHESTPLATE.get());

		getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_LEGGINGS.get(),
			TFItems.IRONWOOD_LEGGINGS.get(),
			TFItems.STEELEAF_LEGGINGS.get(),
			TFItems.KNIGHTMETAL_LEGGINGS.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.YETI_LEGGINGS.get());

		getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_BOOTS.get(),
			TFItems.STEELEAF_BOOTS.get(),
			TFItems.KNIGHTMETAL_BOOTS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_BOOTS.get());

		getOrCreateTagBuilder(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.get(),
			TFItems.STEELEAF_SWORD.get(),
			TFItems.KNIGHTMETAL_SWORD.get(),
			TFItems.FIERY_SWORD.get(),
			TFItems.GIANT_SWORD.get(),
			TFItems.ICE_SWORD.get(),
			TFItems.GLASS_SWORD.get());

		getOrCreateTagBuilder(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		getOrCreateTagBuilder(ItemTags.AXES).add(TFItems.IRONWOOD_AXE.get(), TFItems.STEELEAF_AXE.get(), TFItems.KNIGHTMETAL_AXE.get(), TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.DIAMOND_MINOTAUR_AXE.get());
		getOrCreateTagBuilder(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.get(), TFItems.STEELEAF_SHOVEL.get());
		getOrCreateTagBuilder(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.get(), TFItems.STEELEAF_HOE.get());
		getOrCreateTagBuilder(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.get());
		getOrCreateTagBuilder(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());

		getOrCreateTagBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		getOrCreateTagBuilder(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.asItem());

		getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.NAGA_SCALE.get(), TFItems.CARMINITE.get(), TFItems.FIERY_INGOT.get());
		getOrCreateTagBuilder(TRAVELLERS_AGILE_RANGER_WHITELISTED)
			.add(TFItems.MOONWORM_QUEEN.get());

		getOrCreateTagBuilder(REPAIRS_IRONWOOD_TOOLS).addTag(IRONWOOD_INGOTS);
		getOrCreateTagBuilder(REPAIRS_STEELEAF_TOOLS).addTag(STEELEAF_INGOTS);
		getOrCreateTagBuilder(REPAIRS_KNIGHTMETAL_TOOLS).addTag(KNIGHTMETAL_INGOTS);
		getOrCreateTagBuilder(REPAIRS_FIERY_TOOLS).addTag(FIERY_INGOTS);
		getOrCreateTagBuilder(REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		getOrCreateTagBuilder(REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		getOrCreateTagBuilder(ItemTags.MEAT).add(
			TFItems.RAW_VENISON.get(),
			TFItems.COOKED_VENISON.get(),
			TFItems.RAW_MEEF.get(),
			TFItems.COOKED_MEEF.get(),
			TFItems.MEEF_STROGANOFF.get(),
			TFItems.EXPERIMENT_115.get(),
			TFItems.HYDRA_CHOP.get(),
			TFItems.MONSTER_JERKY.get(),
			TFItems.BEEF_JERKY.get(),
			TFItems.PORK_JERKY.get(),
			TFItems.CHICKEN_JERKY.get(),
			TFItems.RABBIT_JERKY.get(),
			TFItems.MUTTON_JERKY.get(),
			TFItems.VENISON_JERKY.get(),
			TFItems.MEEF_JERKY.get(),
			TFItems.COD_JERKY.get(),
			TFItems.SALMON_JERKY.get(),
			TFItems.TROPICAL_FISH_JERKY.get(),
			TFItems.FUGU_JERKY.get()
		);
		getOrCreateTagBuilder(ItemTags.BEACON_PAYMENT_ITEMS).addTags(IRONWOOD_INGOTS, STEELEAF_INGOTS, KNIGHTMETAL_INGOTS, FIERY_INGOTS);

		// .remove() 在 vanilla tag builders 中不可用，已注释掉
		/*
		getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET.get())
			.remove(TFItems.TRAVELLERS_GOGGLES.get())
			.remove(TFItems.TRAVELLERS_VEST.get())
			.remove(TFItems.TRAVELLERS_GLOVES.get())
			.remove(TFItems.TRAVELLERS_BELT.get())
			.remove(TFItems.TRAVELLERS_WINGS.get())
			.remove(TFItems.TRAVELLERS_BOOTS.get());
		*/

		getOrCreateTagBuilder(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.get(),
			TFItems.STEELEAF_HELMET.get(),
			TFItems.KNIGHTMETAL_HELMET.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.YETI_HELMET.get(),
			TFItems.FIERY_HELMET.get(),
			TFItems.PHANTOM_HELMET.get(),
			TFItems.TRAVELLERS_GOGGLES.get());

		getOrCreateTagBuilder(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.get(),
			TFItems.STEELEAF_CHESTPLATE.get(),
			TFItems.KNIGHTMETAL_CHESTPLATE.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.YETI_CHESTPLATE.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.PHANTOM_CHESTPLATE.get(),
			TFItems.NAGA_CHESTPLATE.get(),
			TFItems.TRAVELLERS_VEST.get(),
			TFItems.TRAVELLERS_GLOVES.get());

		getOrCreateTagBuilder(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.get(),
			TFItems.STEELEAF_LEGGINGS.get(),
			TFItems.KNIGHTMETAL_LEGGINGS.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.YETI_LEGGINGS.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.NAGA_LEGGINGS.get(),
			TFItems.TRAVELLERS_WINGS.get(),
			TFItems.TRAVELLERS_BELT.get());

		getOrCreateTagBuilder(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.get(),
			TFItems.STEELEAF_BOOTS.get(),
			TFItems.KNIGHTMETAL_BOOTS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_BOOTS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.TRAVELLERS_BOOTS.get());

		getOrCreateTagBuilder(ItemTags.DYEABLE).add(TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());

		getOrCreateTagBuilder(BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());
		getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		getOrCreateTagBuilder(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get(),
			TFItems.BLOCK_AND_CHAIN.get(), TFItems.KNIGHTMETAL_SHIELD.get(), TFItems.ORE_MAGNET.get(),
			TFItems.PEACOCK_FEATHER_FAN.get(), TFItems.CRUMBLE_HORN.get());
		// .remove() 在 vanilla tag builders 中不可用，已注释掉
		/*
		getOrCreateTagBuilder(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.get(), TFItems.ICE_SWORD.get());
		getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		getOrCreateTagBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		*/
		getOrCreateTagBuilder(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.get());

		getOrCreateTagBuilder(Tags.Items.FOODS).addTag(FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get(), TFItems.BERRY_MEDLEY.get(), TFItems.MAZE_WAFER.get());
		getOrCreateTagBuilder(FOODS_JERKY).add(
			TFItems.MONSTER_JERKY.get(), TFItems.BEEF_JERKY.get(),
			TFItems.PORK_JERKY.get(), TFItems.CHICKEN_JERKY.get(),
			TFItems.RABBIT_JERKY.get(), TFItems.MUTTON_JERKY.get(),
			TFItems.VENISON_JERKY.get(), TFItems.MEEF_JERKY.get(),
			TFItems.COD_JERKY.get(), TFItems.SALMON_JERKY.get(),
			TFItems.TROPICAL_FISH_JERKY.get(), TFItems.FUGU_JERKY.get());
		// Tags.Items.FOODS_BERRY is not available in PortingLib 1.21.1
		/*
		getOrCreateTagBuilder(Tags.Items.FOODS_BERRY).add(
			TFItems.TORCHBERRIES.get(), TFItems.RASPBERRY.get(),
			TFItems.BLACKBERRY.get(), TFItems.BLUEBERRY.get(),
			TFItems.MALOBERRY.get(), TFItems.DUSKBERRY.get(),
			TFItems.SKYBERRY.get(), TFItems.BLIGHTBERRY.get(),
			TFItems.STINGBERRY.get());
		getOrCreateTagBuilder(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());
		getOrCreateTagBuilder(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.get(), TFItems.COOKED_MEEF.get(), TFItems.HYDRA_CHOP.get());
		getOrCreateTagBuilder(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.get(), TFItems.MOSS_SOUP.get());
		*/
		getOrCreateTagBuilder(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.get());
		getOrCreateTagBuilder(Tags.Items.ROPES).add(TFItems.ROPE.get());
		getOrCreateTagBuilder(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem());
		getOrCreateTagBuilder(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.get(), TFItems.MUSIC_DISC_STEPS.get(), TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
			TFItems.MUSIC_DISC_HOME.get(), TFItems.MUSIC_DISC_WAYFARER.get(), TFItems.MUSIC_DISC_FINDINGS.get(),
			TFItems.MUSIC_DISC_MAKER.get(), TFItems.MUSIC_DISC_THREAD.get(), TFItems.MUSIC_DISC_MOTION.get()
		);
		getOrCreateTagBuilder(NUGGETS_COPPER).add(TFItems.COPPER_NUGGET.get());
		getOrCreateTagBuilder(Tags.Items.NUGGETS).addTag(NUGGETS_COPPER);
		// Tags.Items.SLIME_BALLS is not available in PortingLib 1.21.1
		// getOrCreateTagBuilder(Tags.Items.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.get());
		getOrCreateTagBuilder(Tags.Items.STORAGE_BLOCKS_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.asItem());

		getOrCreateTagBuilder(RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get())
			.add(TFItems.ZOMBIE_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get())
			.add(Items.POINTED_DRIPSTONE, Items.RECOVERY_COMPASS, Items.CLOCK, Items.SPYGLASS, Items.TRIDENT)
			.addOptionalTag(ItemTags.BANNERS.location())
			.addOptionalTag(Tags.Items.TOOLS.location());
			// .remove() 在 vanilla tag builders 中不可用
			// .remove(Tags.Items.TOOLS_SHIELD);

		getOrCreateTagBuilder(TROPHIES).add(
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(),
			TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(),
			TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(),
			TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get());

		getOrCreateTagBuilder(EMPERORS_CLOTH_APPLICABLE).add(Items.ELYTRA).addOptionalTag(Tags.Items.ARMORS.location());
	}

	public static TagKey<Item> create(String tagName) {
		return TagKey.create(Registries.ITEM, TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Item> makeCommonTag(String tagName) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", tagName));
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
