package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends IntrinsicHolderTagsProvider<Item> {

	public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.ITEM, future, item -> item.builtInRegistryHolder().key());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFItemTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.asItem(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem(), TFBlocks.TWILIGHT_OAK_WOOD.asItem(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.asItem());
		this.tag(TFItemTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.asItem(), TFBlocks.STRIPPED_CANOPY_LOG.asItem(), TFBlocks.CANOPY_WOOD.asItem(), TFBlocks.STRIPPED_CANOPY_WOOD.asItem());
		this.tag(TFItemTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.asItem(), TFBlocks.STRIPPED_MANGROVE_LOG.asItem(), TFBlocks.MANGROVE_WOOD.asItem(), TFBlocks.STRIPPED_MANGROVE_WOOD.asItem());
		this.tag(TFItemTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.asItem(), TFBlocks.STRIPPED_DARK_LOG.asItem(), TFBlocks.DARK_WOOD.asItem(), TFBlocks.STRIPPED_DARK_WOOD.asItem());
		this.tag(TFItemTags.TIME_LOGS).add(TFBlocks.TIME_LOG.asItem(), TFBlocks.STRIPPED_TIME_LOG.asItem(), TFBlocks.TIME_WOOD.asItem(), TFBlocks.STRIPPED_TIME_WOOD.asItem());
		this.tag(TFItemTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.asItem(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem(), TFBlocks.TRANSFORMATION_WOOD.asItem(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.asItem());
		this.tag(TFItemTags.MINING_LOGS).add(TFBlocks.MINING_LOG.asItem(), TFBlocks.STRIPPED_MINING_LOG.asItem(), TFBlocks.MINING_WOOD.asItem(), TFBlocks.STRIPPED_MINING_WOOD.asItem());
		this.tag(TFItemTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.asItem(), TFBlocks.STRIPPED_SORTING_LOG.asItem(), TFBlocks.SORTING_WOOD.asItem(), TFBlocks.STRIPPED_SORTING_WOOD.asItem());
		this.tag(TFItemTags.TWILIGHT_LOGS)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS)
			.addTag(TFItemTags.CANOPY_LOGS)
			.addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.DARKWOOD_LOGS)
			.addTag(TFItemTags.TIME_LOGS)
			.addTag(TFItemTags.TRANSFORMATION_LOGS)
			.addTag(TFItemTags.MINING_LOGS)
			.addTag(TFItemTags.SORTING_LOGS);
		this.tag(ItemTags.LOGS).addTag(TFItemTags.TWILIGHT_LOGS);
		this.tag(ItemTags.LOGS_THAT_BURN)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS).addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS).addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);

		this.tag(ItemTags.SAPLINGS).add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem(), TFBlocks.CANOPY_SAPLING.asItem(), TFBlocks.MANGROVE_SAPLING.asItem(), TFBlocks.DARKWOOD_SAPLING.asItem(), TFBlocks.TIME_SAPLING.asItem(), TFBlocks.TRANSFORMATION_SAPLING.asItem(), TFBlocks.MINING_SAPLING.asItem(), TFBlocks.SORTING_SAPLING.asItem(), TFBlocks.HOLLOW_OAK_SAPLING.asItem(), TFBlocks.RAINBOW_OAK_SAPLING.asItem());
		this.tag(ItemTags.LEAVES).add(TFBlocks.RAINBOW_OAK_LEAVES.asItem(), TFBlocks.TWILIGHT_OAK_LEAVES.asItem(), TFBlocks.CANOPY_LEAVES.asItem(), TFBlocks.MANGROVE_LEAVES.asItem(), TFBlocks.DARK_LEAVES.asItem(), TFBlocks.TIME_LEAVES.asItem(), TFBlocks.TRANSFORMATION_LEAVES.asItem(), TFBlocks.MINING_LEAVES.asItem(), TFBlocks.SORTING_LEAVES.asItem(), TFBlocks.THORN_LEAVES.asItem(), TFBlocks.BEANSTALK_LEAVES.asItem());

		this.tag(ItemTags.PLANKS).add(TFBlocks.TWILIGHT_OAK_PLANKS.asItem(), TFBlocks.CANOPY_PLANKS.asItem(), TFBlocks.MANGROVE_PLANKS.asItem(), TFBlocks.DARK_PLANKS.asItem(), TFBlocks.TIME_PLANKS.asItem(), TFBlocks.TRANSFORMATION_PLANKS.asItem(), TFBlocks.MINING_PLANKS.asItem(), TFBlocks.SORTING_PLANKS.asItem()).addTag(TFItemTags.TOWERWOOD);

		this.tag(ItemTags.WOODEN_FENCES).add(TFBlocks.TWILIGHT_OAK_FENCE.asItem(), TFBlocks.CANOPY_FENCE.asItem(), TFBlocks.MANGROVE_FENCE.asItem(), TFBlocks.DARK_FENCE.asItem(), TFBlocks.TIME_FENCE.asItem(), TFBlocks.TRANSFORMATION_FENCE.asItem(), TFBlocks.MINING_FENCE.asItem(), TFBlocks.SORTING_FENCE.asItem());
		this.tag(ItemTags.FENCE_GATES).add(TFBlocks.TWILIGHT_OAK_GATE.asItem(), TFBlocks.CANOPY_GATE.asItem(), TFBlocks.MANGROVE_GATE.asItem(), TFBlocks.DARK_GATE.asItem(), TFBlocks.TIME_GATE.asItem(), TFBlocks.TRANSFORMATION_GATE.asItem(), TFBlocks.MINING_GATE.asItem(), TFBlocks.SORTING_GATE.asItem());

		this.tag(ItemTags.WOODEN_SLABS).add(TFBlocks.TWILIGHT_OAK_SLAB.asItem(), TFBlocks.CANOPY_SLAB.asItem(), TFBlocks.MANGROVE_SLAB.asItem(), TFBlocks.DARK_SLAB.asItem(), TFBlocks.TIME_SLAB.asItem(), TFBlocks.TRANSFORMATION_SLAB.asItem(), TFBlocks.MINING_SLAB.asItem(), TFBlocks.SORTING_SLAB.asItem());
		this.tag(ItemTags.SLABS).add(TFBlocks.AURORA_SLAB.asItem());
		this.tag(ItemTags.WOODEN_STAIRS).add(TFBlocks.TWILIGHT_OAK_STAIRS.asItem(), TFBlocks.CANOPY_STAIRS.asItem(), TFBlocks.MANGROVE_STAIRS.asItem(), TFBlocks.DARK_STAIRS.asItem(), TFBlocks.TIME_STAIRS.asItem(), TFBlocks.TRANSFORMATION_STAIRS.asItem(), TFBlocks.MINING_STAIRS.asItem(), TFBlocks.SORTING_STAIRS.asItem());
		this.tag(ItemTags.STAIRS).add(TFBlocks.CASTLE_BRICK_STAIRS.asItem(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.asItem(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.asItem(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.asItem(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.asItem(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.asItem(), TFBlocks.NAGASTONE_STAIRS_LEFT.asItem(), TFBlocks.NAGASTONE_STAIRS_RIGHT.asItem(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.asItem(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.asItem(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.asItem(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.asItem());

		this.tag(ItemTags.WOODEN_BUTTONS).add(TFBlocks.TWILIGHT_OAK_BUTTON.asItem(), TFBlocks.CANOPY_BUTTON.asItem(), TFBlocks.MANGROVE_BUTTON.asItem(), TFBlocks.DARK_BUTTON.asItem(), TFBlocks.TIME_BUTTON.asItem(), TFBlocks.TRANSFORMATION_BUTTON.asItem(), TFBlocks.MINING_BUTTON.asItem(), TFBlocks.SORTING_BUTTON.asItem());
		this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(TFBlocks.TWILIGHT_OAK_PLATE.asItem(), TFBlocks.CANOPY_PLATE.asItem(), TFBlocks.MANGROVE_PLATE.asItem(), TFBlocks.DARK_PLATE.asItem(), TFBlocks.TIME_PLATE.asItem(), TFBlocks.TRANSFORMATION_PLATE.asItem(), TFBlocks.MINING_PLATE.asItem(), TFBlocks.SORTING_PLATE.asItem());

		this.tag(ItemTags.WOODEN_TRAPDOORS).add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.asItem(), TFBlocks.CANOPY_TRAPDOOR.asItem(), TFBlocks.MANGROVE_TRAPDOOR.asItem(), TFBlocks.DARK_TRAPDOOR.asItem(), TFBlocks.TIME_TRAPDOOR.asItem(), TFBlocks.TRANSFORMATION_TRAPDOOR.asItem(), TFBlocks.MINING_TRAPDOOR.asItem(), TFBlocks.SORTING_TRAPDOOR.asItem());
		this.tag(ItemTags.WOODEN_DOORS).add(TFBlocks.TWILIGHT_OAK_DOOR.asItem(), TFBlocks.CANOPY_DOOR.asItem(), TFBlocks.MANGROVE_DOOR.asItem(), TFBlocks.DARK_DOOR.asItem(), TFBlocks.TIME_DOOR.asItem(), TFBlocks.TRANSFORMATION_DOOR.asItem(), TFBlocks.MINING_DOOR.asItem(), TFBlocks.SORTING_DOOR.asItem());

		this.tag(ItemTags.HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.asItem(), TFBlocks.CANOPY_HANGING_SIGN.asItem(),
			TFBlocks.MANGROVE_HANGING_SIGN.asItem(), TFBlocks.DARK_HANGING_SIGN.asItem(),
			TFBlocks.TIME_HANGING_SIGN.asItem(), TFBlocks.TRANSFORMATION_HANGING_SIGN.asItem(),
			TFBlocks.MINING_HANGING_SIGN.asItem(), TFBlocks.SORTING_HANGING_SIGN.asItem());
		this.tag(ItemTags.SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.asItem(), TFBlocks.CANOPY_SIGN.asItem(),
			TFBlocks.MANGROVE_SIGN.asItem(), TFBlocks.DARK_SIGN.asItem(),
			TFBlocks.TIME_SIGN.asItem(), TFBlocks.TRANSFORMATION_SIGN.asItem(),
			TFBlocks.MINING_SIGN.asItem(), TFBlocks.SORTING_SIGN.asItem());

		this.tag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.asItem());

		this.tag(ConventionalItemTags.STORAGE_BLOCKS)
			.addTag(TFItemTags.STORAGE_BLOCKS_FIERY).addTag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(TFItemTags.STORAGE_BLOCKS_CARMINITE).addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(TFItemTags.TOWERWOOD).add(TFBlocks.TOWERWOOD.asItem(), TFBlocks.MOSSY_TOWERWOOD.asItem(), TFBlocks.CRACKED_TOWERWOOD.asItem(), TFBlocks.INFESTED_TOWERWOOD.asItem());
		this.tag(TFItemTags.BANISTERS).add(
			TFBlocks.OAK_BANISTER.asItem(),
			TFBlocks.SPRUCE_BANISTER.asItem(),
			TFBlocks.BIRCH_BANISTER.asItem(),
			TFBlocks.JUNGLE_BANISTER.asItem(),
			TFBlocks.ACACIA_BANISTER.asItem(),
			TFBlocks.DARK_OAK_BANISTER.asItem(),
			TFBlocks.CRIMSON_BANISTER.asItem(),
			TFBlocks.WARPED_BANISTER.asItem(),
			TFBlocks.VANGROVE_BANISTER.asItem(),
			TFBlocks.BAMBOO_BANISTER.asItem(),
			TFBlocks.CHERRY_BANISTER.asItem(),
			TFBlocks.PALE_OAK_BANISTER.asItem(),

			TFBlocks.TWILIGHT_OAK_BANISTER.asItem(),
			TFBlocks.CANOPY_BANISTER.asItem(),
			TFBlocks.MANGROVE_BANISTER.asItem(),
			TFBlocks.DARK_BANISTER.asItem(),
			TFBlocks.TIME_BANISTER.asItem(),
			TFBlocks.TRANSFORMATION_BANISTER.asItem(),
			TFBlocks.MINING_BANISTER.asItem(),
			TFBlocks.SORTING_BANISTER.asItem()
		);

		this.tag(TFItemTags.PAPER).add(Items.PAPER);
		this.tag(ConventionalItemTags.FEATHERS).add(TFItems.RAVEN_FEATHER);

		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD, TFItems.FIERY_TEARS);

		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR);
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE);
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT);
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT);
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT);
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT);
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR);

		this.tag(ConventionalItemTags.GEMS).addTag(TFItemTags.CARMINITE_GEMS);

		this.tag(ConventionalItemTags.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD);
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER);
		this.tag(ConventionalItemTags.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);

		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(ConventionalItemTags.DIAMOND_GEMS);

		this.tag(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT, TFItems.CANOPY_BOAT,
			TFItems.MANGROVE_BOAT, TFItems.DARK_BOAT,
			TFItems.TIME_BOAT, TFItems.TRANSFORMATION_BOAT,
			TFItems.MINING_BOAT, TFItems.SORTING_BOAT
		);

		this.tag(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT, TFItems.CANOPY_CHEST_BOAT,
			TFItems.MANGROVE_CHEST_BOAT, TFItems.DARK_CHEST_BOAT,
			TFItems.TIME_CHEST_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT,
			TFItems.MINING_CHEST_BOAT, TFItems.SORTING_CHEST_BOAT
		);

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET,
			TFItems.FIERY_CHESTPLATE,
			TFItems.FIERY_LEGGINGS,
			TFItems.FIERY_BOOTS,
			TFItems.ARCTIC_HELMET,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_HELMET,
			TFItems.YETI_CHESTPLATE,
			TFItems.YETI_LEGGINGS,
			TFItems.YETI_BOOTS
		);

		this.tag(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN,
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN,
			TFItems.CUBE_OF_ANNIHILATION,
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.BRAZIER.asItem(),
			TFBlocks.MAZE_SLIME_BLOCK.asItem()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(ConventionalItemTags.CARROT_CROPS).addTag(ConventionalItemTags.POTATO_CROPS).addTag(ConventionalItemTags.BEETROOT_CROPS);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(ConventionalItemTags.WHEAT_CROPS).add(Items.APPLE);
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).addTag(ConventionalItemTags.CARROT_CROPS).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);

		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER);

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(ConventionalItemTags.WOODEN_RODS);

		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY, TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);

		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER, TFItems.LIFEDRAIN_SCEPTER, TFItems.ZOMBIE_SCEPTER, TFItems.FORTIFICATION_SCEPTER);

		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());

		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_2, TFItems.LAMP_OF_CINDERS);

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.PHANTOM_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET);

		this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_CHESTPLATE,
			TFItems.IRONWOOD_CHESTPLATE,
			TFItems.STEELEAF_CHESTPLATE,
			TFItems.KNIGHTMETAL_CHESTPLATE,
			TFItems.PHANTOM_CHESTPLATE,
			TFItems.FIERY_CHESTPLATE,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.YETI_CHESTPLATE);

		this.tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_LEGGINGS,
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS);

		this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.FIERY_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS);

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD,
			TFItems.STEELEAF_SWORD,
			TFItems.KNIGHTMETAL_SWORD,
			TFItems.FIERY_SWORD,
			TFItems.GIANT_SWORD,
			TFItems.ICE_SWORD,
			TFItems.GLASS_SWORD);

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.tag(ItemTags.AXES).add(TFItems.IRONWOOD_AXE, TFItems.STEELEAF_AXE, TFItems.KNIGHTMETAL_AXE, TFItems.GOLDEN_MINOTAUR_AXE, TFItems.DIAMOND_MINOTAUR_AXE);
		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL, TFItems.STEELEAF_SHOVEL);
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE, TFItems.STEELEAF_HOE);
		this.tag(ConventionalItemTags.SHIELD_TOOLS).add(TFItems.KNIGHTMETAL_SHIELD);
		this.tag(ConventionalItemTags.BOW_TOOLS).add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.tag(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.asItem());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT, TFItems.STEELEAF_INGOT, TFItems.KNIGHTMETAL_INGOT, TFItems.NAGA_SCALE, TFItems.CARMINITE, TFItems.FIERY_INGOT);

		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		this.tag(ItemTags.MEAT).add(TFItems.RAW_VENISON, TFItems.COOKED_VENISON, TFItems.RAW_MEEF, TFItems.COOKED_MEEF, TFItems.MEEF_STROGANOFF, TFItems.EXPERIMENT_115, TFItems.HYDRA_CHOP);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS);

		this.tag(ItemTags.TRIMMABLE_ARMOR).remove(TFItems.YETI_HELMET);

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.PHANTOM_HELMET);

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE,
			TFItems.STEELEAF_CHESTPLATE,
			TFItems.KNIGHTMETAL_CHESTPLATE,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.YETI_CHESTPLATE,
			TFItems.FIERY_CHESTPLATE,
			TFItems.PHANTOM_CHESTPLATE,
			TFItems.NAGA_CHESTPLATE);

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.NAGA_LEGGINGS);

		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS,
			TFItems.FIERY_BOOTS);

		//this.tag(ItemTags.DYEABLE).add(TFItems.ARCTIC_HELMET, TFItems.ARCTIC_CHESTPLATE, TFItems.ARCTIC_LEGGINGS, TFItems.ARCTIC_BOOTS);
		this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(TFItems.ARCTIC_HELMET, TFItems.ARCTIC_CHESTPLATE, TFItems.ARCTIC_LEGGINGS, TFItems.ARCTIC_BOOTS);

		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW,
			TFItems.BLOCK_AND_CHAIN, TFItems.KNIGHTMETAL_SHIELD, TFItems.ORE_MAGNET,
			TFItems.PEACOCK_FEATHER_FAN, TFItems.CRUMBLE_HORN);
		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD, TFItems.ICE_SWORD);
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN);

		this.tag(ConventionalItemTags.BERRY_FOODS).add(TFItems.TORCHBERRIES);
		this.tag(ConventionalItemTags.RAW_MEAT_FOODS).add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);
		this.tag(ConventionalItemTags.COOKED_MEAT_FOODS).add(TFItems.COOKED_VENISON, TFItems.COOKED_MEEF, TFItems.HYDRA_CHOP);
		this.tag(ConventionalItemTags.SOUP_FOODS).add(TFItems.MEEF_STROGANOFF);
		this.tag(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).add(TFItems.EXPERIMENT_115);
		this.tag(ConventionalItemTags.ROPES).add(TFItems.ROPE);
		this.tag(ConventionalItemTags.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem());
		this.tag(ConventionalItemTags.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE, TFItems.MUSIC_DISC_STEPS, TFItems.MUSIC_DISC_SUPERSTITIOUS,
			TFItems.MUSIC_DISC_HOME, TFItems.MUSIC_DISC_WAYFARER, TFItems.MUSIC_DISC_FINDINGS,
			TFItems.MUSIC_DISC_MAKER, TFItems.MUSIC_DISC_THREAD, TFItems.MUSIC_DISC_MOTION
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
