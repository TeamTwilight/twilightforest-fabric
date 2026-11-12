package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.datagen.helpers.BlockToItemConverter;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends TagsProvider<Item> {

	public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.ITEM, future);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider) {
		new BlockItemTagGenerator() {
			@Override
			protected TagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new BlockToItemConverter(ItemTagGenerator.this.tag(itemTag));
			}
		}.run();

		this.tag(ItemTags.SAPLINGS).twilightforest$add(
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

		this.tag(ItemTags.LOGS_THAT_BURN)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS)
			.addTag(TFItemTags.CANOPY_LOGS)
			.addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.DARKWOOD_LOGS)
			.addTag(TFItemTags.TIME_LOGS)
			.addTag(TFItemTags.TRANSFORMATION_LOGS)
			.addTag(TFItemTags.MINING_LOGS)
			.addTag(TFItemTags.SORTING_LOGS);

		this.tag(ItemTags.BOATS).twilightforest$add(
			TFItems.TWILIGHT_OAK_BOAT, TFItems.CANOPY_BOAT,
			TFItems.MANGROVE_BOAT, TFItems.DARK_BOAT,
			TFItems.TIME_BOAT, TFItems.TRANSFORMATION_BOAT,
			TFItems.MINING_BOAT, TFItems.SORTING_BOAT
		);

		this.tag(ItemTags.CHEST_BOATS).twilightforest$add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT, TFItems.CANOPY_CHEST_BOAT,
			TFItems.MANGROVE_CHEST_BOAT, TFItems.DARK_CHEST_BOAT,
			TFItems.TIME_CHEST_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT,
			TFItems.MINING_CHEST_BOAT, TFItems.SORTING_CHEST_BOAT
		);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS).addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(ItemTags.PIGLIN_LOVED).twilightforest$add(TFItems.GOLDEN_MINOTAUR_AXE, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_2, TFItems.LAMP_OF_CINDERS);
		this.tag(ItemTags.FOX_FOOD).twilightforest$add(TFItems.TORCHBERRIES);

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).twilightforest$add(
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
			TFItems.YETI_BOOTS,
			TFItems.TRAVELLERS_VEST,
			TFItems.TRAVELLERS_BOOTS);

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).twilightforest$add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.tag(ItemTags.SWORDS).twilightforest$add(
			TFItems.IRONWOOD_SWORD,
			TFItems.STEELEAF_SWORD,
			TFItems.KNIGHTMETAL_SWORD,
			TFItems.FIERY_SWORD,
			TFItems.GIANT_SWORD,
			TFItems.ICE_SWORD,
			TFItems.GLASS_SWORD);

		this.tag(ItemTags.AXES).twilightforest$add(
			TFItems.IRONWOOD_AXE,
			TFItems.STEELEAF_AXE,
			TFItems.KNIGHTMETAL_AXE,
			TFItems.GOLDEN_MINOTAUR_AXE,
			TFItems.DIAMOND_MINOTAUR_AXE);

		this.tag(ItemTags.PICKAXES).twilightforest$add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.tag(ItemTags.SHOVELS).twilightforest$add(TFItems.IRONWOOD_SHOVEL, TFItems.STEELEAF_SHOVEL);
		this.tag(ItemTags.HOES).twilightforest$add(TFItems.IRONWOOD_HOE, TFItems.STEELEAF_HOE);

		this.tag(ItemTags.BREAKS_DECORATED_POTS).twilightforest$add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.FOOT_ARMOR).twilightforest$add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS,
			TFItems.FIERY_BOOTS,
			TFItems.TRAVELLERS_BOOTS);

		this.tag(ItemTags.LEG_ARMOR).twilightforest$add(
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.NAGA_LEGGINGS,
			TFItems.TRAVELLERS_WINGS,
			TFItems.TRAVELLERS_BELT);

		this.tag(ItemTags.CHEST_ARMOR).twilightforest$add(
			TFItems.IRONWOOD_CHESTPLATE,
			TFItems.STEELEAF_CHESTPLATE,
			TFItems.KNIGHTMETAL_CHESTPLATE,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.YETI_CHESTPLATE,
			TFItems.FIERY_CHESTPLATE,
			TFItems.PHANTOM_CHESTPLATE,
			TFItems.NAGA_CHESTPLATE,
			TFItems.TRAVELLERS_VEST,
			TFItems.TRAVELLERS_GLOVES);

		this.tag(ItemTags.HEAD_ARMOR).twilightforest$add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.PHANTOM_HELMET,
			TFItems.TRAVELLERS_GOGGLES);

		this.tag(ItemTags.SKULLS).twilightforest$add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.tag(ItemTags.TRIMMABLE_ARMOR)
			.twilightforest$remove(TFItems.YETI_HELMET)
			.twilightforest$remove(TFItems.TRAVELLERS_GOGGLES)
			.twilightforest$remove(TFItems.TRAVELLERS_VEST)
			.twilightforest$remove(TFItems.TRAVELLERS_GLOVES)
			.twilightforest$remove(TFItems.TRAVELLERS_BELT)
			.twilightforest$remove(TFItems.TRAVELLERS_WINGS)
			.twilightforest$remove(TFItems.TRAVELLERS_BOOTS);

		this.tag(ItemTags.TRIM_MATERIALS).twilightforest$add(TFItems.IRONWOOD_INGOT, TFItems.STEELEAF_INGOT, TFItems.KNIGHTMETAL_INGOT, TFItems.NAGA_SCALE, TFItems.CARMINITE, TFItems.FIERY_INGOT);

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).twilightforest$add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).twilightforest$remove(TFItems.FIERY_SWORD, TFItems.ICE_SWORD);
		this.tag(ItemTags.MINING_ENCHANTABLE).twilightforest$add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).twilightforest$add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).twilightforest$add(
			TFItems.TRIPLE_BOW,
			TFItems.SEEKER_BOW,
			TFItems.ICE_BOW,
			TFItems.ENDER_BOW,
			TFItems.BLOCK_AND_CHAIN,
			TFItems.KNIGHTMETAL_SHIELD,
			TFItems.ORE_MAGNET,
			TFItems.PEACOCK_FEATHER_FAN,
			TFItems.CRUMBLE_HORN);
		this.tag(ItemTags.BOW_ENCHANTABLE).twilightforest$add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);
		this.tag(ItemTags.VANISHING_ENCHANTABLE).twilightforest$remove(TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).twilightforest$remove(TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);
		this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).twilightforest$add(TFItems.ARCTIC_HELMET, TFItems.ARCTIC_CHESTPLATE, TFItems.ARCTIC_LEGGINGS, TFItems.ARCTIC_BOOTS);
		this.tag(ItemTags.MEAT).twilightforest$add(
			TFItems.RAW_VENISON,
			TFItems.COOKED_VENISON,
			TFItems.RAW_MEEF,
			TFItems.COOKED_MEEF,
			TFItems.MEEF_STROGANOFF,
			TFItems.EXPERIMENT_115,
			TFItems.HYDRA_CHOP,
			TFItems.MONSTER_JERKY,
			TFItems.BEEF_JERKY,
			TFItems.PORK_JERKY,
			TFItems.CHICKEN_JERKY,
			TFItems.RABBIT_JERKY,
			TFItems.MUTTON_JERKY,
			TFItems.VENISON_JERKY,
			TFItems.MEEF_JERKY,
			TFItems.COD_JERKY,
			TFItems.SALMON_JERKY,
			TFItems.TROPICAL_FISH_JERKY,
			TFItems.FUGU_JERKY
		);
		this.tag(ItemTags.LOOM_PATTERNS).twilightforest$add(
			TFItems.NAGA_BANNER_PATTERN,
			TFItems.LICH_BANNER_PATTERN,
			TFItems.MINOSHROOM_BANNER_PATTERN,
			TFItems.HYDRA_BANNER_PATTERN,
			TFItems.KNIGHT_PHANTOM_BANNER_PATTERN,
			TFItems.UR_GHAST_BANNER_PATTERN,
			TFItems.ALPHA_YETI_BANNER_PATTERN,
			TFItems.SNOW_QUEEN_BANNER_PATTERN,
			TFItems.QUEST_RAM_BANNER_PATTERN);

		this.tag(ConventionalItemTags.FEATHERS).twilightforest$add(TFItems.RAVEN_FEATHER);
		this.tag(ConventionalItemTags.FOODS).addTag(TFItemTags.FOODS_JERKY).twilightforest$add(TFItems.GELATINOUS_SLIME_DROP, TFItems.GELATINOUS_MAZE_SLIME_DROP, TFItems.BERRY_MEDLEY, TFItems.MAZE_WAFER);
		this.tag(ConventionalItemTags.BERRY_FOODS).twilightforest$add(
			TFItems.TORCHBERRIES,
			TFItems.RASPBERRY,
			TFItems.BLACKBERRY,
			TFItems.BLUEBERRY,
			TFItems.MALOBERRY,
			TFItems.DUSKBERRY,
			TFItems.SKYBERRY,
			TFItems.BLIGHTBERRY,
			TFItems.STINGBERRY);
		this.tag(ConventionalItemTags.RAW_MEAT_FOODS).twilightforest$add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);
		this.tag(ConventionalItemTags.COOKED_MEAT_FOODS).twilightforest$add(TFItems.COOKED_VENISON, TFItems.COOKED_MEEF, TFItems.HYDRA_CHOP);
		this.tag(ConventionalItemTags.SOUP_FOODS).twilightforest$add(TFItems.MEEF_STROGANOFF, TFItems.MOSS_SOUP);
		this.tag(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).twilightforest$add(TFItems.EXPERIMENT_115);
		this.tag(ConventionalItemTags.GEMS).addTag(TFItemTags.CARMINITE_GEMS);
		this.tag(ConventionalItemTags.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(ConventionalItemTags.MUSHROOMS).twilightforest$add(TFBlocks.MUSHGLOOM.asItem());
		this.tag(ConventionalItemTags.MUSIC_DISCS).twilightforest$add(
			TFItems.MUSIC_DISC_RADIANCE,
			TFItems.MUSIC_DISC_STEPS,
			TFItems.MUSIC_DISC_SUPERSTITIOUS,
			TFItems.MUSIC_DISC_HOME,
			TFItems.MUSIC_DISC_WAYFARER,
			TFItems.MUSIC_DISC_FINDINGS,
			TFItems.MUSIC_DISC_MAKER,
			TFItems.MUSIC_DISC_THREAD,
			TFItems.MUSIC_DISC_MOTION
		);
		this.tag(ConventionalItemTags.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);
		this.tag(ConventionalItemTags.RANGED_WEAPON_TOOLS).twilightforest$add(TFItems.BLOCK_AND_CHAIN);
		this.tag(ConventionalItemTags.SLIME_BALLS).twilightforest$add(TFItems.MAZE_SLIME_BALL);
		this.tag(ConventionalItemTags.SHIELD_TOOLS).twilightforest$add(TFItems.KNIGHTMETAL_SHIELD);
		this.tag(ConventionalItemTags.BOW_TOOLS).twilightforest$add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);

		this.tag(TFItemTags.PAPER).twilightforest$add(Items.PAPER);
		this.tag(TFItemTags.FIERY_VIAL).twilightforest$add(TFItems.FIERY_BLOOD, TFItems.FIERY_TEARS);
		this.tag(TFItemTags.ARCTIC_FUR).twilightforest$add(TFItems.ARCTIC_FUR);
		this.tag(TFItemTags.CARMINITE_GEMS).twilightforest$add(TFItems.CARMINITE);
		this.tag(TFItemTags.FIERY_INGOTS).twilightforest$add(TFItems.FIERY_INGOT);
		this.tag(TFItemTags.IRONWOOD_INGOTS).twilightforest$add(TFItems.IRONWOOD_INGOT);
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).twilightforest$add(TFItems.KNIGHTMETAL_INGOT);
		this.tag(TFItemTags.STEELEAF_INGOTS).twilightforest$add(TFItems.STEELEAF_INGOT);
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).twilightforest$add(TFItems.WROUGHT_IRON_BAR);
		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).twilightforest$add(TFItems.RAW_IRONWOOD);
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).twilightforest$add(TFItems.ARMOR_SHARD_CLUSTER);
		this.tag(TFItemTags.PORTAL_ACTIVATOR).forceAddTag(ConventionalItemTags.DIAMOND_GEMS);
		this.tag(TFItemTags.WIP).twilightforest$add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN,
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN,
			TFItems.CUBE_OF_ANNIHILATION,
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.BRAZIER.asItem()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).twilightforest$add(Items.BREAD);

		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.CARROT_CROPS).forceAddTag(ConventionalItemTags.POTATO_CROPS).forceAddTag(ConventionalItemTags.BEETROOT_CROPS);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.WHEAT_CROPS).twilightforest$add(Items.APPLE).twilightforest$add(TFItems.SHIKA_SENBEI);
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.CARROT_CROPS).twilightforest$add(Items.GOLDEN_CARROT).twilightforest$add(Items.DANDELION);
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).forceAddTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).twilightforest$add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER);

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).twilightforest$add(TFBlocks.GIANT_LOG.asItem());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).forceAddTag(ConventionalItemTags.WOODEN_RODS);
		this.tag(TFItemTags.KEPT_ON_DEATH).twilightforest$add(TFItems.TOWER_KEY, TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);
		this.tag(TFItemTags.SCEPTERS).twilightforest$add(TFItems.TWILIGHT_SCEPTER, TFItems.LIFEDRAIN_SCEPTER, TFItems.ZOMBIE_SCEPTER, TFItems.FORTIFICATION_SCEPTER);
		this.tag(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS).twilightforest$add(TFItems.EXANIMATE_ESSENCE);
		this.tag(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS).twilightforest$add(TFItems.TORCHBERRIES);
		this.tag(TFItemTags.KEEPSAKE_CASKET_REPAIR_ITEMS).twilightforest$add(TFItems.CHARM_OF_KEEPING_3);
		this.tag(TFItemTags.IMMUNE_TO_THORNS).twilightforest$add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());
		this.tag(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED).twilightforest$add(TFItems.MOONWORM_QUEEN);
		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).twilightforest$add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).twilightforest$add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());
		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).twilightforest$add(TFItems.BLOCK_AND_CHAIN);

		this.tag(TFItemTags.FOODS_JERKY).twilightforest$add(
			TFItems.MONSTER_JERKY,
			TFItems.BEEF_JERKY,
			TFItems.PORK_JERKY,
			TFItems.CHICKEN_JERKY,
			TFItems.RABBIT_JERKY,
			TFItems.MUTTON_JERKY,
			TFItems.VENISON_JERKY,
			TFItems.MEEF_JERKY,
			TFItems.COD_JERKY,
			TFItems.SALMON_JERKY,
			TFItems.TROPICAL_FISH_JERKY,
			TFItems.FUGU_JERKY);

		this.tag(TFItemTags.MAZE_SLIME_BALLS).twilightforest$add(TFItems.MAZE_SLIME_BALL);

		this.tag(TFItemTags.RENDER_LOWER_ON_DRYING_RACK)
			.twilightforest$add(TFItems.GELATINOUS_SLIME_DROP, TFItems.GELATINOUS_MAZE_SLIME_DROP)
			.twilightforest$add(TFItems.ZOMBIE_SKULL_CANDLE, TFItems.SKELETON_SKULL_CANDLE, TFItems.WITHER_SKELETON_SKULL_CANDLE, TFItems.CREEPER_SKULL_CANDLE, TFItems.PLAYER_SKULL_CANDLE, TFItems.PIGLIN_SKULL_CANDLE)
			.twilightforest$add(Items.POINTED_DRIPSTONE, Items.RECOVERY_COMPASS, Items.CLOCK, Items.SPYGLASS, Items.TRIDENT)
			.forceAddTag(ItemTags.BANNERS)
			.forceAddTag(ConventionalItemTags.TOOLS)
			.removeTag(ConventionalItemTags.SHIELD_TOOLS);

		this.tag(TFItemTags.TROPHIES).twilightforest$add(
			TFItems.NAGA_TROPHY, TFItems.LICH_TROPHY,
			TFItems.MINOSHROOM_TROPHY, TFItems.HYDRA_TROPHY,
			TFItems.KNIGHT_PHANTOM_TROPHY, TFItems.UR_GHAST_TROPHY,
			TFItems.ALPHA_YETI_TROPHY, TFItems.SNOW_QUEEN_TROPHY);

		this.tag(TFItemTags.EMPERORS_CLOTH_APPLICABLE).forceAddTag(ConventionalItemTags.ARMORS).twilightforest$add(Items.ELYTRA);

		// Mod Compatibility Tags

		this.tag(TFItemTags.AC_FERNS).twilightforest$add(TFBlocks.FIDDLEHEAD.asItem());
		this.tag(TFItemTags.AC_FERROMAGNETIC_ITEMS)
			.addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)
			.twilightforest$add(TFBlocks.CANDELABRA.asItem(), TFBlocks.WROUGHT_IRON_FENCE.asItem())
			.twilightforest$add(TFItems.RAW_IRONWOOD, TFItems.IRONWOOD_INGOT, TFItems.STEELEAF_INGOT,
				TFItems.ARMOR_SHARD, TFItems.ARMOR_SHARD_CLUSTER, TFItems.KNIGHTMETAL_INGOT, TFItems.KNIGHTMETAL_RING,
				TFItems.FIERY_INGOT, TFItems.CHARM_OF_KEEPING_2, TFItems.ORE_MAGNET,
				TFItems.IRONWOOD_HELMET, TFItems.IRONWOOD_CHESTPLATE, TFItems.IRONWOOD_LEGGINGS, TFItems.IRONWOOD_BOOTS,
				TFItems.STEELEAF_HELMET, TFItems.STEELEAF_CHESTPLATE, TFItems.STEELEAF_LEGGINGS, TFItems.STEELEAF_BOOTS,
				TFItems.KNIGHTMETAL_HELMET, TFItems.KNIGHTMETAL_CHESTPLATE, TFItems.KNIGHTMETAL_LEGGINGS, TFItems.KNIGHTMETAL_BOOTS,
				TFItems.FIERY_HELMET, TFItems.FIERY_CHESTPLATE, TFItems.FIERY_LEGGINGS, TFItems.FIERY_BOOTS,
				TFItems.IRONWOOD_SWORD, TFItems.IRONWOOD_PICKAXE, TFItems.IRONWOOD_AXE, TFItems.IRONWOOD_SHOVEL, TFItems.IRONWOOD_HOE,
				TFItems.STEELEAF_SWORD, TFItems.STEELEAF_PICKAXE, TFItems.STEELEAF_AXE, TFItems.STEELEAF_SHOVEL, TFItems.STEELEAF_HOE,
				TFItems.KNIGHTMETAL_SWORD, TFItems.KNIGHTMETAL_PICKAXE, TFItems.KNIGHTMETAL_AXE, TFItems.BLOCK_AND_CHAIN, TFItems.KNIGHTMETAL_SHIELD,
				TFItems.FIERY_SWORD, TFItems.FIERY_PICKAXE, TFItems.MAZEBREAKER_PICKAXE);

		this.tag(TFItemTags.AC_RAW_MEATS).twilightforest$add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);

		this.tag(TFItemTags.CA_PLANT_FOODS).twilightforest$add(TFItems.TORCHBERRIES);

		this.tag(TFItemTags.CA_PLANTS).twilightforest$add(TFItems.LIVEROOT, TFItems.MAGIC_BEANS,
			TFBlocks.HUGE_WATER_LILY.asItem(), TFBlocks.HUGE_LILY_PAD.asItem(),
			TFBlocks.TROLLVIDR.asItem(), TFBlocks.UNRIPE_TROLLBER.asItem(),
			TFBlocks.TROLLBER.asItem(), TFBlocks.HUGE_STALK.asItem(),
			TFBlocks.THORN_ROSE.asItem(), TFBlocks.MAYAPPLE.asItem(),
			TFBlocks.CLOVER_PATCH.asItem(), TFBlocks.FIDDLEHEAD.asItem(),
			TFBlocks.MUSHGLOOM.asItem(), TFBlocks.TORCHBERRY_PLANT.asItem(),
			TFBlocks.ROOT_STRAND.asItem(), TFBlocks.FALLEN_LEAVES.asItem(),
			TFBlocks.HEDGE.asItem(), TFBlocks.ROOT_BLOCK.asItem(), TFBlocks.LIVEROOT_BLOCK.asItem());

		this.tag(TFItemTags.FA_MODIFIER_ETERNAL_INCOMPATIBLE).twilightforest$add(TFItems.GLASS_SWORD);

		this.tag(TFItemTags.FD_CABBAGE_ROLL_INGREDIENTS).twilightforest$add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);

		this.tag(TFItemTags.RANDOMIUM_BLACKLIST).addTag(TFItemTags.WIP).twilightforest$add(TFItems.GLASS_SWORD, //this one is here because the ore can give the unbreakable one
			TFBlocks.TIME_LOG_CORE.asItem(), TFBlocks.TRANSFORMATION_LOG_CORE.asItem(),
			TFBlocks.MINING_LOG_CORE.asItem(), TFBlocks.SORTING_LOG_CORE.asItem(),
			TFBlocks.ANTIBUILDER.asItem(), TFBlocks.STRONGHOLD_SHIELD.asItem(),
			TFBlocks.LOCKED_VANISHING_BLOCK.asItem(), TFBlocks.BROWN_THORNS.asItem(),
			TFBlocks.GREEN_THORNS.asItem(), TFBlocks.BURNT_THORNS.asItem(),
			TFBlocks.PINK_FORCE_FIELD.asItem(), TFBlocks.ORANGE_FORCE_FIELD.asItem(),
			TFBlocks.GREEN_FORCE_FIELD.asItem(), TFBlocks.BLUE_FORCE_FIELD.asItem(),
			TFBlocks.VIOLET_FORCE_FIELD.asItem(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFBlocks.NAGA_BOSS_SPAWNER.asItem(), TFBlocks.LICH_BOSS_SPAWNER.asItem(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.asItem(), TFBlocks.HYDRA_BOSS_SPAWNER.asItem(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.asItem(), TFBlocks.UR_GHAST_BOSS_SPAWNER.asItem(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.asItem(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.asItem());
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}