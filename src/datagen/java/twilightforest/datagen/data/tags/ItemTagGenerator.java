package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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
		new BlockItemTagGenerator() {
			@Override
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new VanillaItemTagsProvider.BlockToItemConverter(ItemTagGenerator.this.tag(itemTag));
			}
		}.run();

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

		this.tag(TFItemTags.PORTAL_ACTIVATOR).forceAddTag(ConventionalItemTags.DIAMOND_GEMS);

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
			TFItems.YETI_BOOTS,
			TFItems.TRAVELLERS_VEST,
			TFItems.TRAVELLERS_BOOTS
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
			TFBlocks.BRAZIER.asItem()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.CARROT_CROPS).forceAddTag(ConventionalItemTags.POTATO_CROPS).forceAddTag(ConventionalItemTags.BEETROOT_CROPS);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.WHEAT_CROPS).add(Items.APPLE).add(TFItems.SHIKA_SENBEI);
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.CARROT_CROPS).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).forceAddTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).forceAddTag(ConventionalItemTags.SEEDS);

		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER);

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).forceAddTag(ConventionalItemTags.WOODEN_RODS);

		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY, TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);

		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER, TFItems.LIFEDRAIN_SCEPTER, TFItems.ZOMBIE_SCEPTER, TFItems.FORTIFICATION_SCEPTER);
		this.tag(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS).add(TFItems.EXANIMATE_ESSENCE);
		this.tag(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS).add(TFItems.TORCHBERRIES);
		this.tag(TFItemTags.KEEPSAKE_CASKET_REPAIR_ITEMS).add(TFItems.CHARM_OF_KEEPING_3);

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
		this.tag(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED)
			.add(TFItems.MOONWORM_QUEEN);

		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		this.tag(ItemTags.MEAT).add(
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

		this.tag(ItemTags.BEACON_PAYMENT_ITEMS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS);

		this.tag(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET)
			.remove(TFItems.TRAVELLERS_GOGGLES)
			.remove(TFItems.TRAVELLERS_VEST)
			.remove(TFItems.TRAVELLERS_GLOVES)
			.remove(TFItems.TRAVELLERS_BELT)
			.remove(TFItems.TRAVELLERS_WINGS)
			.remove(TFItems.TRAVELLERS_BOOTS);

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.PHANTOM_HELMET,
			TFItems.TRAVELLERS_GOGGLES);

		this.tag(ItemTags.CHEST_ARMOR).add(
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

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.NAGA_LEGGINGS,
			TFItems.TRAVELLERS_WINGS,
			TFItems.TRAVELLERS_BELT);

		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS,
			TFItems.FIERY_BOOTS,
			TFItems.TRAVELLERS_BOOTS);

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

		this.tag(ConventionalItemTags.FOODS).addTag(TFItemTags.FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP, TFItems.GELATINOUS_MAZE_SLIME_DROP, TFItems.BERRY_MEDLEY, TFItems.MAZE_WAFER);
		this.tag(TFItemTags.FOODS_JERKY).add(
			TFItems.MONSTER_JERKY, TFItems.BEEF_JERKY,
			TFItems.PORK_JERKY, TFItems.CHICKEN_JERKY,
			TFItems.RABBIT_JERKY, TFItems.MUTTON_JERKY,
			TFItems.VENISON_JERKY, TFItems.MEEF_JERKY,
			TFItems.COD_JERKY, TFItems.SALMON_JERKY,
			TFItems.TROPICAL_FISH_JERKY, TFItems.FUGU_JERKY);
		this.tag(ConventionalItemTags.BERRY_FOODS).add(
			TFItems.TORCHBERRIES, TFItems.RASPBERRY,
			TFItems.BLACKBERRY, TFItems.BLUEBERRY,
			TFItems.MALOBERRY, TFItems.DUSKBERRY,
			TFItems.SKYBERRY, TFItems.BLIGHTBERRY,
			TFItems.STINGBERRY);
		this.tag(ConventionalItemTags.RAW_MEAT_FOODS).add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);
		this.tag(ConventionalItemTags.COOKED_MEAT_FOODS).add(TFItems.COOKED_VENISON, TFItems.COOKED_MEEF, TFItems.HYDRA_CHOP);
		this.tag(ConventionalItemTags.SOUP_FOODS).add(TFItems.MEEF_STROGANOFF, TFItems.MOSS_SOUP);
		this.tag(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).add(TFItems.EXPERIMENT_115);
		this.tag(ConventionalItemTags.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem());
		this.tag(ConventionalItemTags.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE, TFItems.MUSIC_DISC_STEPS, TFItems.MUSIC_DISC_SUPERSTITIOUS,
			TFItems.MUSIC_DISC_HOME, TFItems.MUSIC_DISC_WAYFARER, TFItems.MUSIC_DISC_FINDINGS,
			TFItems.MUSIC_DISC_MAKER, TFItems.MUSIC_DISC_THREAD, TFItems.MUSIC_DISC_MOTION
		);

		this.tag(TFItemTags.MAZE_SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL);

		this.tag(ConventionalItemTags.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL);

		this.tag(TFItemTags.RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP, TFItems.GELATINOUS_MAZE_SLIME_DROP)
			.add(TFItems.ZOMBIE_SKULL_CANDLE, TFItems.SKELETON_SKULL_CANDLE, TFItems.WITHER_SKELETON_SKULL_CANDLE, TFItems.CREEPER_SKULL_CANDLE, TFItems.PLAYER_SKULL_CANDLE, TFItems.PIGLIN_SKULL_CANDLE)
			.add(Items.POINTED_DRIPSTONE, Items.RECOVERY_COMPASS, Items.CLOCK, Items.SPYGLASS, Items.TRIDENT)
			.forceAddTag(ItemTags.BANNERS)
			.forceAddTag(ConventionalItemTags.TOOLS)
			.removeTag(ConventionalItemTags.SHIELD_TOOLS);

		this.tag(TFItemTags.TROPHIES).add(
			TFItems.NAGA_TROPHY, TFItems.LICH_TROPHY,
			TFItems.MINOSHROOM_TROPHY, TFItems.HYDRA_TROPHY,
			TFItems.KNIGHT_PHANTOM_TROPHY, TFItems.UR_GHAST_TROPHY,
			TFItems.ALPHA_YETI_TROPHY, TFItems.SNOW_QUEEN_TROPHY);

		this.tag(TFItemTags.EMPERORS_CLOTH_APPLICABLE).forceAddTag(ConventionalItemTags.ARMORS).add(Items.ELYTRA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}