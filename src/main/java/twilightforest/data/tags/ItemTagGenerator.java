package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
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

		getOrCreateTagBuilder(ConventionalItemTags.STRIPPED_LOGS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value().asItem(),
			TFBlocks.STRIPPED_CANOPY_LOG.value().asItem(),
			TFBlocks.STRIPPED_MANGROVE_LOG.value().asItem(),
			TFBlocks.STRIPPED_DARK_LOG.value().asItem(),
			TFBlocks.STRIPPED_TIME_LOG.value().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.value().asItem(),
			TFBlocks.STRIPPED_MINING_LOG.value().asItem(),
			TFBlocks.STRIPPED_SORTING_LOG.value().asItem()
		);

		getOrCreateTagBuilder(ConventionalItemTags.STRIPPED_WOODS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value().asItem(),
			TFBlocks.STRIPPED_CANOPY_WOOD.value().asItem(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.value().asItem(),
			TFBlocks.STRIPPED_DARK_WOOD.value().asItem(),
			TFBlocks.STRIPPED_TIME_WOOD.value().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value().asItem(),
			TFBlocks.STRIPPED_MINING_WOOD.value().asItem(),
			TFBlocks.STRIPPED_SORTING_WOOD.value().asItem()
		);

		getOrCreateTagBuilder(ItemTags.SAPLINGS).add(
			TFBlocks.TWILIGHT_OAK_SAPLING.asItem(),
			TFBlocks.CANOPY_SAPLING.asItem(),
			TFBlocks.MANGROVE_SAPLING.asItem(),
			TFBlocks.DARKWOOD_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.RAINBOW_OAK_SAPLING.asItem()
		);

		getOrCreateTagBuilder(ItemTags.LEAVES).add(
			TFBlocks.RAINBOW_OAK_LEAVES.asItem(),
			TFBlocks.TWILIGHT_OAK_LEAVES.asItem(),
			TFBlocks.CANOPY_LEAVES.asItem(),
			TFBlocks.MANGROVE_LEAVES.asItem(),
			TFBlocks.DARK_LEAVES.asItem(),
			TFBlocks.TIME_LEAVES.asItem(),
			TFBlocks.TRANSFORMATION_LEAVES.asItem(),
			TFBlocks.MINING_LEAVES.asItem(),
			TFBlocks.SORTING_LEAVES.asItem(),
			TFBlocks.THORN_LEAVES.asItem(),
			TFBlocks.BEANSTALK_LEAVES.asItem()
			// NOTE: HARDENED_DARK_LEAVES is intentionally excluded here: it has no item form
			// (it is a decorative Dark Tower block whose drops are DARK_LEAVES), so asItem()
			// returns Items.AIR. Adding it would put minecraft:air into the minecraft:leaves
			// item tag, which makes any recipe ingredient using that tag resolve to an empty
			// ItemStack and crash with "Empty ItemStack not allowed" when the recipe list is
			// synced to clients (e.g. VanillaBackport's leaf_litter smelting recipe).
		);

		getOrCreateTagBuilder(ItemTags.PLANKS).add(
			TFBlocks.TWILIGHT_OAK_PLANKS.asItem(),
			TFBlocks.CANOPY_PLANKS.asItem(),
			TFBlocks.MANGROVE_PLANKS.asItem(),
			TFBlocks.DARK_PLANKS.asItem(),
			TFBlocks.TIME_PLANKS.asItem(),
			TFBlocks.TRANSFORMATION_PLANKS.asItem(),
			TFBlocks.MINING_PLANKS.asItem(),
			TFBlocks.SORTING_PLANKS.asItem()
		).addTag(TOWERWOOD);

		getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(
			TFBlocks.TWILIGHT_OAK_SLAB.asItem(),
			TFBlocks.CANOPY_SLAB.asItem(),
			TFBlocks.MANGROVE_SLAB.asItem(),
			TFBlocks.DARK_SLAB.asItem(),
			TFBlocks.TIME_SLAB.asItem(),
			TFBlocks.TRANSFORMATION_SLAB.asItem(),
			TFBlocks.MINING_SLAB.asItem(),
			TFBlocks.SORTING_SLAB.asItem()
		);

		getOrCreateTagBuilder(ItemTags.SLABS).add(
			TFBlocks.AURORA_SLAB.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(
			TFBlocks.TWILIGHT_OAK_STAIRS.asItem(),
			TFBlocks.CANOPY_STAIRS.asItem(),
			TFBlocks.MANGROVE_STAIRS.asItem(),
			TFBlocks.DARK_STAIRS.asItem(),
			TFBlocks.TIME_STAIRS.asItem(),
			TFBlocks.TRANSFORMATION_STAIRS.asItem(),
			TFBlocks.MINING_STAIRS.asItem(),
			TFBlocks.SORTING_STAIRS.asItem()
		);

		getOrCreateTagBuilder(ItemTags.STAIRS).add(
			TFBlocks.CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.WORN_CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.BOLD_CASTLE_BRICK_STAIRS.asItem(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.asItem(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.asItem(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.asItem(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.asItem(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.asItem(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(
			TFBlocks.TWILIGHT_OAK_FENCE.asItem(),
			TFBlocks.CANOPY_FENCE.asItem(),
			TFBlocks.MANGROVE_FENCE.asItem(),
			TFBlocks.DARK_FENCE.asItem(),
			TFBlocks.TIME_FENCE.asItem(),
			TFBlocks.TRANSFORMATION_FENCE.asItem(),
			TFBlocks.MINING_FENCE.asItem(),
			TFBlocks.SORTING_FENCE.asItem()
		);

		getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(
			TFBlocks.TWILIGHT_OAK_GATE.asItem(),
			TFBlocks.CANOPY_GATE.asItem(),
			TFBlocks.MANGROVE_GATE.asItem(),
			TFBlocks.DARK_GATE.asItem(),
			TFBlocks.TIME_GATE.asItem(),
			TFBlocks.TRANSFORMATION_GATE.asItem(),
			TFBlocks.MINING_GATE.asItem(),
			TFBlocks.SORTING_GATE.asItem()
		);

		getOrCreateTagBuilder(ConventionalItemTags.WOODEN_FENCE_GATES).add(
			TFBlocks.TWILIGHT_OAK_GATE.asItem(),
			TFBlocks.CANOPY_GATE.asItem(),
			TFBlocks.MANGROVE_GATE.asItem(),
			TFBlocks.DARK_GATE.asItem(),
			TFBlocks.TIME_GATE.asItem(),
			TFBlocks.TRANSFORMATION_GATE.asItem(),
			TFBlocks.MINING_GATE.asItem(),
			TFBlocks.SORTING_GATE.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(
			TFBlocks.TWILIGHT_OAK_BUTTON.asItem(),
			TFBlocks.CANOPY_BUTTON.asItem(),
			TFBlocks.MANGROVE_BUTTON.asItem(),
			TFBlocks.DARK_BUTTON.asItem(),
			TFBlocks.TIME_BUTTON.asItem(),
			TFBlocks.TRANSFORMATION_BUTTON.asItem(),
			TFBlocks.MINING_BUTTON.asItem(),
			TFBlocks.SORTING_BUTTON.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(
			TFBlocks.TWILIGHT_OAK_PLATE.asItem(),
			TFBlocks.CANOPY_PLATE.asItem(),
			TFBlocks.MANGROVE_PLATE.asItem(),
			TFBlocks.DARK_PLATE.asItem(),
			TFBlocks.TIME_PLATE.asItem(),
			TFBlocks.TRANSFORMATION_PLATE.asItem(),
			TFBlocks.MINING_PLATE.asItem(),
			TFBlocks.SORTING_PLATE.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(
			TFBlocks.TWILIGHT_OAK_TRAPDOOR.asItem(),
			TFBlocks.CANOPY_TRAPDOOR.asItem(),
			TFBlocks.MANGROVE_TRAPDOOR.asItem(),
			TFBlocks.DARK_TRAPDOOR.asItem(),
			TFBlocks.TIME_TRAPDOOR.asItem(),
			TFBlocks.TRANSFORMATION_TRAPDOOR.asItem(),
			TFBlocks.MINING_TRAPDOOR.asItem(),
			TFBlocks.SORTING_TRAPDOOR.asItem()
		);

		getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(
			TFBlocks.TWILIGHT_OAK_DOOR.asItem(),
			TFBlocks.CANOPY_DOOR.asItem(),
			TFBlocks.MANGROVE_DOOR.asItem(),
			TFBlocks.DARK_DOOR.asItem(),
			TFBlocks.TIME_DOOR.asItem(),
			TFBlocks.TRANSFORMATION_DOOR.asItem(),
			TFBlocks.MINING_DOOR.asItem(),
			TFBlocks.SORTING_DOOR.asItem()
		);

		getOrCreateTagBuilder(ConventionalItemTags.WOODEN_CHESTS)
			.add(
				TFBlocks.TWILIGHT_OAK_CHEST.asItem(),
				TFBlocks.CANOPY_CHEST.asItem(),
				TFBlocks.MANGROVE_CHEST.asItem(),
				TFBlocks.DARK_CHEST.asItem(),
				TFBlocks.TIME_CHEST.asItem(),
				TFBlocks.TRANSFORMATION_CHEST.asItem(),
				TFBlocks.MINING_CHEST.asItem(),
				TFBlocks.SORTING_CHEST.asItem()
			)
			.add(
				TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.asItem(),
				TFBlocks.CANOPY_TRAPPED_CHEST.asItem(),
				TFBlocks.MANGROVE_TRAPPED_CHEST.asItem(),
				TFBlocks.DARK_TRAPPED_CHEST.asItem(),
				TFBlocks.TIME_TRAPPED_CHEST.asItem(),
				TFBlocks.TRANSFORMATION_TRAPPED_CHEST.asItem(),
				TFBlocks.MINING_TRAPPED_CHEST.asItem(),
				TFBlocks.SORTING_TRAPPED_CHEST.asItem()
			);

		getOrCreateTagBuilder(ConventionalItemTags.TRAPPED_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.asItem(),
			TFBlocks.CANOPY_TRAPPED_CHEST.asItem(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.asItem(),
			TFBlocks.DARK_TRAPPED_CHEST.asItem(),
			TFBlocks.TIME_TRAPPED_CHEST.asItem(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.asItem(),
			TFBlocks.MINING_TRAPPED_CHEST.asItem(),
			TFBlocks.SORTING_TRAPPED_CHEST.asItem()
		);

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		getOrCreateTagBuilder(ConventionalItemTags.STORAGE_BLOCKS)
			.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
			.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);
		this.copy(BlockTagGenerator.BANISTERS, BANISTERS);
		this.copy(BlockTagGenerator.DRYING_RACKS, DRYING_RACKS);

		getOrCreateTagBuilder(PAPER).add(Items.PAPER);
		getOrCreateTagBuilder(ConventionalItemTags.FEATHERS).add(TFItems.RAVEN_FEATHER.get());

		getOrCreateTagBuilder(FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());

		getOrCreateTagBuilder(ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		getOrCreateTagBuilder(CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		getOrCreateTagBuilder(FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		getOrCreateTagBuilder(IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		getOrCreateTagBuilder(KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		getOrCreateTagBuilder(STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());
		getOrCreateTagBuilder(WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.get());

		getOrCreateTagBuilder(ConventionalItemTags.GEMS).addTag(CARMINITE_GEMS);

		getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
			.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
			.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		getOrCreateTagBuilder(ConventionalItemTags.NUGGETS)
			.add(TFItems.COPPER_NUGGET.asItem());
		getOrCreateTagBuilder(COPPER_NUGGETS)
			.add(TFItems.COPPER_NUGGET.asItem());

		getOrCreateTagBuilder(RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		getOrCreateTagBuilder(RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		getOrCreateTagBuilder(ConventionalItemTags.RAW_MATERIALS).addTag(RAW_MATERIALS_IRONWOOD).addTag(RAW_MATERIALS_KNIGHTMETAL);

		getOrCreateTagBuilder(PORTAL_ACTIVATOR).add(Items.DIAMOND).addOptionalTag(ConventionalItemTags.DIAMOND_GEMS);

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
		getOrCreateTagBuilder(BOAR_TEMPT_ITEMS).addOptionalTag(ConventionalItemTags.CARROT_CROPS.location()).addOptionalTag(ConventionalItemTags.POTATO_CROPS.location()).addOptionalTag(ConventionalItemTags.BEETROOT_CROPS.location());
		getOrCreateTagBuilder(DEER_TEMPT_ITEMS).add(Items.APPLE).add(TFItems.SHIKA_SENBEI.get()).addOptionalTag(ConventionalItemTags.WHEAT_CROPS.location());
		getOrCreateTagBuilder(DWARF_RABBIT_TEMPT_ITEMS).add(Items.GOLDEN_CARROT).add(Items.DANDELION).addOptionalTag(ConventionalItemTags.CARROT_CROPS.location());
		getOrCreateTagBuilder(PENGUIN_TEMPT_ITEMS).addOptionalTag(ItemTags.FISHES.location());
		getOrCreateTagBuilder(RAVEN_TEMPT_ITEMS).addOptionalTag(ConventionalItemTags.SEEDS.location());
		getOrCreateTagBuilder(SQUIRREL_TEMPT_ITEMS).addOptionalTag(ConventionalItemTags.SEEDS.location());
		getOrCreateTagBuilder(TINY_BIRD_TEMPT_ITEMS).addOptionalTag(ConventionalItemTags.SEEDS.location());

		getOrCreateTagBuilder(BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER.get());

		getOrCreateTagBuilder(BANNED_UNCRAFTABLES);
		getOrCreateTagBuilder(UNCRAFTING_IGNORES_COST).addOptionalTag(ConventionalItemTags.WOODEN_RODS.location());

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
		getOrCreateTagBuilder(ConventionalItemTags.SHIELD_TOOLS).add(TFItems.KNIGHTMETAL_SHIELD.get());
		getOrCreateTagBuilder(ConventionalItemTags.BOW_TOOLS).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());

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

		getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET.get())
			.remove(TFItems.TRAVELLERS_GOGGLES.get())
			.remove(TFItems.TRAVELLERS_VEST.get())
			.remove(TFItems.TRAVELLERS_GLOVES.get())
			.remove(TFItems.TRAVELLERS_BELT.get())
			.remove(TFItems.TRAVELLERS_WINGS.get())
			.remove(TFItems.TRAVELLERS_BOOTS.get());

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

		getOrCreateTagBuilder(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.get(), TFItems.ICE_SWORD.get());
		getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		getOrCreateTagBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());

		getOrCreateTagBuilder(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.get());

		getOrCreateTagBuilder(ConventionalItemTags.FOODS).addTag(FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get(), TFItems.BERRY_MEDLEY.get(), TFItems.MAZE_WAFER.get());
		getOrCreateTagBuilder(FOODS_JERKY).add(
			TFItems.MONSTER_JERKY.get(), TFItems.BEEF_JERKY.get(),
			TFItems.PORK_JERKY.get(), TFItems.CHICKEN_JERKY.get(),
			TFItems.RABBIT_JERKY.get(), TFItems.MUTTON_JERKY.get(),
			TFItems.VENISON_JERKY.get(), TFItems.MEEF_JERKY.get(),
			TFItems.COD_JERKY.get(), TFItems.SALMON_JERKY.get(),
			TFItems.TROPICAL_FISH_JERKY.get(), TFItems.FUGU_JERKY.get());

		getOrCreateTagBuilder(ConventionalItemTags.BERRY_FOODS).add(
			TFItems.TORCHBERRIES.get(), TFItems.RASPBERRY.get(),
			TFItems.BLACKBERRY.get(), TFItems.BLUEBERRY.get(),
			TFItems.MALOBERRY.get(), TFItems.DUSKBERRY.get(),
			TFItems.SKYBERRY.get(), TFItems.BLIGHTBERRY.get(),
			TFItems.STINGBERRY.get());
		getOrCreateTagBuilder(ConventionalItemTags.RAW_MEAT_FOODS).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());
		getOrCreateTagBuilder(ConventionalItemTags.COOKED_MEAT_FOODS).add(TFItems.COOKED_VENISON.get(), TFItems.COOKED_MEEF.get(), TFItems.HYDRA_CHOP.get());
		getOrCreateTagBuilder(ConventionalItemTags.SOUP_FOODS).add(TFItems.MEEF_STROGANOFF.get(), TFItems.MOSS_SOUP.get());

		getOrCreateTagBuilder(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).add(TFItems.EXPERIMENT_115.get());
		getOrCreateTagBuilder(ConventionalItemTags.ROPES).add(TFItems.ROPE.get());
		getOrCreateTagBuilder(ConventionalItemTags.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem());
		getOrCreateTagBuilder(ConventionalItemTags.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.get(), TFItems.MUSIC_DISC_STEPS.get(), TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
			TFItems.MUSIC_DISC_HOME.get(), TFItems.MUSIC_DISC_WAYFARER.get(), TFItems.MUSIC_DISC_FINDINGS.get(),
			TFItems.MUSIC_DISC_MAKER.get(), TFItems.MUSIC_DISC_THREAD.get(), TFItems.MUSIC_DISC_MOTION.get()
		);
		getOrCreateTagBuilder(NUGGETS_COPPER).add(TFItems.COPPER_NUGGET.get());
		getOrCreateTagBuilder(ConventionalItemTags.NUGGETS).addTag(NUGGETS_COPPER);
		getOrCreateTagBuilder(ConventionalItemTags.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.get());
		getOrCreateTagBuilder(ConventionalItemTags.STORAGE_BLOCKS_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.asItem());

		getOrCreateTagBuilder(RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get())
			.add(TFItems.ZOMBIE_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get())
			.add(Items.POINTED_DRIPSTONE, Items.RECOVERY_COMPASS, Items.CLOCK, Items.SPYGLASS, Items.TRIDENT)
			.addOptionalTag(ItemTags.BANNERS.location())
			.addOptionalTag(ConventionalItemTags.TOOLS.location());
			// .remove() 在 vanilla tag builders 中不可用
			// .remove(ConventionalItemTags.TOOLS_SHIELD);

		getOrCreateTagBuilder(TROPHIES).add(
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(),
			TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(),
			TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(),
			TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get());

		getOrCreateTagBuilder(EMPERORS_CLOTH_APPLICABLE).add(Items.ELYTRA).addOptionalTag(ConventionalItemTags.ARMORS.location());
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
