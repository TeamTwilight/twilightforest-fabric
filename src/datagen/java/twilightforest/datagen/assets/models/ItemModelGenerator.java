package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.Count;
import net.minecraft.client.renderer.item.properties.numeric.Time;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import twilightforest.TFCommon;
import twilightforest.client.model.item.TravellersGearItemModel;
import twilightforest.client.properties.*;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.ItemModelBuilders;
import twilightforest.init.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ItemModelGenerator extends ItemModelBuilders {

	public ItemModelGenerator(ItemModelOutput output, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public void run() {
		this.generateFlatItem(TFItems.MAGIC_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ORE_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_MAGIC_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_MAZE_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_ORE_MAP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TORCHBERRIES, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAVEN_FEATHER, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAGIC_MAP_FOCUS, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_MAP_FOCUS, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_LIFE_1, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_LIFE_2, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_1, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_2, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_3, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TRANSFORMATION_POWDER, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_VENISON, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COOKED_VENISON, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_MEEF, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COOKED_MEEF, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_WAFER, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MEEF_STROGANOFF, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.HYDRA_CHOP, ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFBlocks.EXPERIMENT_115.asItem(), ItemModelUtils.select(new Experiment115Type(), ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.EXPERIMENT_115.asItem(), ModelTemplates.FLAT_ITEM)),
			ItemModelUtils.when("think", ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(TFBlocks.EXPERIMENT_115.asItem(), "_think"), TextureMapping.layer0(new Material(TFCommon.prefix("item/think115"))), this.modelOutput))),
			ItemModelUtils.when("full", ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.EXPERIMENT_115, "_8_8_regenerating")))));
		this.generateFlatItem(TFItems.LIVEROOT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_IRONWOOD, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_INGOT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_INGOT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.NAGA_SCALE, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.WROUGHT_IRON_BAR, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARMOR_SHARD, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARMOR_SHARD_CLUSTER, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_INGOT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_RING, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_BLOOD, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_TEARS, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_INGOT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARCTIC_FUR, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ALPHA_YETI_FUR, ModelTemplates.FLAT_ITEM);
		Identifier empty = ModelTemplates.FLAT_ITEM.create(TFCommon.prefix("item/potion_flask_empty"), TextureMapping.layer0(new Material(TFCommon.prefix("block/blank"))), this.modelOutput);
		this.generatePotionFlask(TFItems.BRITTLE_FLASK, true, empty);
		this.generatePotionFlask(TFItems.GREATER_FLASK, false, empty);
		this.generateTwoLayerItem(TFItems.EXANIMATE_ESSENCE, "_flames", ModelTemplates.TWO_LAYERED_ITEM);
		this.generateFlatItem(TFItems.CROWN_SPLINTER, ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFBlocks.RED_THREAD.asItem(), ItemModelUtils.rangeSelect(new Count(true), ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), ModelTemplates.FLAT_ITEM)), List.of(
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_0", ModelTemplates.FLAT_ITEM)), 4.0F / 64.0F),
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_1", ModelTemplates.FLAT_ITEM)), 16.0F / 64.0F),
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_2", ModelTemplates.FLAT_ITEM)), 32.0F / 64.0F))));
		this.generateTwoLayerItem(TFItems.BORER_ESSENCE, "_particles", ModelTemplates.TWO_LAYERED_ITEM);
		this.generateFlatItem(TFItems.CARMINITE, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TOWER_KEY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAGIC_BEANS, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MUSIC_DISC_THREAD, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_FINDINGS, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_RADIANCE, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_STEPS, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_MOTION, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_WAYFARER, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_HOME, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_MAKER, ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_SUPERSTITIOUS, ModelTemplates.MUSIC_DISC);

		this.generatePattern(TFItems.NAGA_BANNER_PATTERN);
		this.generatePattern(TFItems.LICH_BANNER_PATTERN);
		this.generatePattern(TFItems.MINOSHROOM_BANNER_PATTERN);
		this.generatePattern(TFItems.HYDRA_BANNER_PATTERN);
		this.generatePattern(TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
		this.generatePattern(TFItems.UR_GHAST_BANNER_PATTERN);
		this.generatePattern(TFItems.ALPHA_YETI_BANNER_PATTERN);
		this.generatePattern(TFItems.SNOW_QUEEN_BANNER_PATTERN);
		this.generatePattern(TFItems.QUEST_RAM_BANNER_PATTERN);

		this.generateFlatItem(TFItems.TWILIGHT_OAK_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CANOPY_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MANGROVE_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.DARK_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TIME_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TRANSFORMATION_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MINING_BOAT, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SORTING_BOAT, ModelTemplates.FLAT_ITEM);

		this.generateChestBoat(TFItems.TWILIGHT_OAK_CHEST_BOAT);
		this.generateChestBoat(TFItems.CANOPY_CHEST_BOAT);
		this.generateChestBoat(TFItems.MANGROVE_CHEST_BOAT);
		this.generateChestBoat(TFItems.DARK_CHEST_BOAT);
		this.generateChestBoat(TFItems.TIME_CHEST_BOAT);
		this.generateChestBoat(TFItems.TRANSFORMATION_CHEST_BOAT);
		this.generateChestBoat(TFItems.MINING_CHEST_BOAT);
		this.generateChestBoat(TFItems.SORTING_CHEST_BOAT);

		this.generateFlatItem(TFItems.IRONWOOD_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateFlatItem(TFItems.STEELEAF_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateFlatItem(TFItems.KNIGHTMETAL_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.BLOCK_AND_CHAIN, ItemModelUtils.conditional(new HasComponent(TFDataComponents.THROWN_PROJECTILE, false),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN, "_thrown", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN, ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateKnightmetalShield(TFItems.KNIGHTMETAL_SHIELD);

		this.generateFlatItem(TFItems.FIERY_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.FIERY_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);

		this.itemModelOutput.accept(TFItems.MYSTIC_CROWN, ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(TFItems.MYSTIC_CROWN), new MysticCrownSpecialRenderer.Unbaked()));

		this.generateFlatItem(TFItems.MAZEBREAKER_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.DIAMOND_MINOTAUR_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.GOLDEN_MINOTAUR_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateTwoLayerItem(TFItems.ICE_SWORD, "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_HANDHELD);
		this.generateTwoLayerItem(TFItems.GLASS_SWORD, "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_HANDHELD);

		this.generateBow(TFItems.TRIPLE_BOW, false);
		this.generateBow(TFItems.SEEKER_BOW, false);
		this.generateBow(TFItems.ICE_BOW, true);
		this.generateBow(TFItems.ENDER_BOW, false);

		this.generateFlatItem(TFItems.ICE_BOMB, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TWILIGHT_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.LIFEDRAIN_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.ZOMBIE_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.FORTIFICATION_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.LAMP_OF_CINDERS, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFItems.LAMP_OF_CINDERS)));
		this.generateFlatItem(TFItems.EMPERORS_CLOTH, ModelTemplates.FLAT_ITEM);
		this.generateOreMagnet(TFItems.ORE_MAGNET);
		this.itemModelOutput.accept(TFItems.ORE_METER, ItemModelUtils.conditional(new OreMeterFlash(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER, "_active", ModelTemplates.FLAT_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER, ModelTemplates.FLAT_ITEM))));
		this.generateFlatItem(TFItems.POCKET_WATCH, ModelTemplates.FLAT_ITEM);
		this.generateMoonDial(TFItems.MOON_DIAL);
		this.generateBooleanDispatch(TFItems.CRUMBLE_HORN, ItemModelUtils.isUsingItem(),
			ItemModelUtils.plainModel(ModelTemplates.createItem(Identifier.withDefaultNamespace("tooting_goat_horn").toString(), TextureSlot.LAYER0).create(TFCommon.prefix("tooting_crumble_horn"), TextureMapping.layer0(TFItems.CRUMBLE_HORN), this.modelOutput)),
			ItemModelUtils.plainModel(ModelTemplates.createItem(Identifier.withDefaultNamespace("goat_horn").toString(), TextureSlot.LAYER0).create(TFItems.CRUMBLE_HORN, TextureMapping.layer0(TFItems.CRUMBLE_HORN), this.modelOutput)));
		this.generateFlatItem(TFItems.PEACOCK_FEATHER_FAN, ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.MOONWORM_QUEEN, ItemModelUtils.conditional(new MoonwormQueenPulse(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN, "_alt", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN, ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateFlatItem(TFItems.MAGIC_PAINTING, ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.CUBE_TALISMAN, ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.CUBE_OF_ANNIHILATION, ItemModelUtils.conditional(new HasComponent(TFDataComponents.THROWN_PROJECTILE, false),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.CUBE_OF_ANNIHILATION, "_thrown", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.CUBE_OF_ANNIHILATION, ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateFlatItem(TFItems.FOUR_LEAF_CLOVER, ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.RASPBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLUEBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLACKBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MALOBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLIGHTBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.DUSKBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SKYBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.STINGBERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COPPER_BERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.IRON_BERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.GOLD_BERRY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ESSENCE_BERRY, ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.BEEF_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHICKEN_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.PORK_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MUTTON_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RABBIT_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MEEF_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.VENISON_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MONSTER_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COD_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SALMON_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TROPICAL_FISH_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FUGU_JERKY, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SHIKA_SENBEI, ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.GELATINOUS_MAZE_SLIME_DROP, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.GELATINOUS_SLIME_DROP, ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.BERRY_MEDLEY, ItemModelUtils.plainModel(this.generateLayeredItem(TFItems.BERRY_MEDLEY, TextureMapping.getItemTexture(Items.BOWL), TextureMapping.getItemTexture(TFItems.BERRY_MEDLEY))));
		this.itemModelOutput.accept(TFItems.MOSS_SOUP, ItemModelUtils.plainModel(this.generateLayeredItem(TFItems.MOSS_SOUP, TextureMapping.getItemTexture(Items.BOWL), TextureMapping.getItemTexture(TFItems.MOSS_SOUP))));

		this.generateFlatItem(TFItems.MAZE_SLIME_BALL, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TANNIN, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TREATED_LEATHER, ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TANNED_LEATHER, ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.STALE_BREAD, ItemModelUtils.plainModel(ModelTemplates.FLAT_HANDHELD_ITEM.create(TFItems.STALE_BREAD, TextureMapping.layer0(Items.BREAD), this.modelOutput)));

		this.generateTravellersGear(TFItems.TRAVELLERS_GOGGLES, TFCommon.prefix("travellers_modifiers/goggles"));
		this.generateLayeredTravellersGear(TFItems.TRAVELLERS_VEST, TFItems.TRAVELLERS_GLOVES, "gloves", new HasComponent(TFDataComponents.TRAVELLERS_HAS_GLOVES, true), TFCommon.prefix("travellers_modifiers/vest"));
		this.generateTravellersGear(TFItems.TRAVELLERS_WINGS, TFCommon.prefix("travellers_modifiers/wings"));
		this.generateTravellersGear(TFItems.TRAVELLERS_BOOTS, TFCommon.prefix("travellers_modifiers/boots"));
		this.generateFlatItem(TFItems.TRAVELLERS_BELT, ModelTemplates.FLAT_ITEM);

		this.generateSpawnEgg("alpha_yeti", 0xCDCDCD, 0x29486E);
		this.generateSpawnEgg("armored_giant", 0x239391, 0x9A9A9A);
		this.generateSpawnEgg("bighorn_sheep", 0xDBCEAF, 0xD7C771);
		this.generateSpawnEgg("block_and_chain_goblin", 0xD3E7BC, 0x1F3FFF);
		this.generateSpawnEgg("boar", 0x83653B, 0xFFEFCA);
		this.generateSpawnEgg("carminite_broodling", 0x343C14, 0xBAEE02);
		this.generateSpawnEgg("carminite_ghastguard", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("carminite_ghastling", 0xBCBCBC, 0xA74343);
		this.generateSpawnEgg("carminite_golem", 0x6B3D20, 0xE2DDDA);
		this.generateSpawnEgg("death_tome", 0x774E22, 0xDBCDBE);
		this.generateSpawnEgg("deer", 0x7B4D2E, 0x4B241D);
		this.generateSpawnEgg("dwarf_rabbit", 0xFEFEEE, 0xCCAA99);
		this.generateSpawnEgg("fire_beetle", 0x1D0B00, 0xCB6F25);
		this.generateSpawnEgg("giant_miner", 0x211B52, 0x9A9A9A);
		this.generateSpawnEgg("hedge_spider", 0x235F13, 0x562653);
		this.generateSpawnEgg("helmet_crab", 0xFB904B, 0xD3E7BC);
		this.generateSpawnEgg("hostile_wolf", 0xD7D3D3, 0xAB1E14);
		this.generateSpawnEgg("hydra", 0x142940, 0x29806B);
		this.generateSpawnEgg("ice_crystal", 0xDCE9FE, 0xADCAFB);
		this.generateSpawnEgg("king_spider", 0x2C1A0E, 0xFFC017);
		this.generateSpawnEgg("knight_phantom", 0xA6673B, 0xD3E7BC);
		this.generateSpawnEgg("kobold", 0x372096, 0x895D1B);
		this.generateSpawnEgg("lich", 0xACA489, 0x360472);
		this.generateSpawnEgg("lower_goblin_knight", 0x566055, 0xD3E7BC);
		this.generateSpawnEgg("maze_slime", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("minoshroom", 0xA81012, 0xAA7D66);
		this.generateSpawnEgg("minotaur", 0x3F3024, 0xAA7D66);
		this.generateSpawnEgg("mist_wolf", 0x3A1411, 0xE2C88A);
		this.generateSpawnEgg("mosquito_swarm", 0x080904, 0x2D2F21);
		this.generateSpawnEgg("naga", 0xA4D316, 0x1B380B);
		this.generateSpawnEgg("penguin", 0x12151B, 0xF9EDD2);
		this.generateSpawnEgg("pinch_beetle", 0xBC9327, 0x241609);
		this.generateSpawnEgg("quest_ram", 0xFEFEEE, 0x33AADD);
		this.generateSpawnEgg("raven", 0x000011, 0x222233);
		this.generateSpawnEgg("redcap", 0x3B3A6C, 0xAB1E14);
		this.generateSpawnEgg("redcap_sapper", 0x575D21, 0xAB1E14);
		this.generateSpawnEgg("skeleton_druid", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("slime_beetle", 0x0C1606, 0x60A74C);
		this.generateSpawnEgg("snow_guardian", 0xD3E7BC, 0xFEFEFE);
		this.generateSpawnEgg("snow_queen", 0xB1B2D4, 0x87006E);
		this.generateSpawnEgg("squirrel", 0x904F12, 0xEEEEEE);
		this.generateSpawnEgg("stable_ice_core", 0xA1BFF3, 0x7000F8);
		this.generateSpawnEgg("swarm_spider", 0x32022E, 0x17251E);
		this.generateSpawnEgg("tiny_bird", 0x33AADD, 0x1188EE);
		this.generateSpawnEgg("towerwood_borer", 0x5D2B21, 0xACA03A);
		this.generateSpawnEgg("troll", 0x9EA98F, 0xB0948E);
		this.generateSpawnEgg("unstable_ice_core", 0x9AACF5, 0x9B0FA5);
		this.generateSpawnEgg("ur_ghast", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("winter_wolf", 0xDFE3E5, 0xB2BCCA);
		this.generateSpawnEgg("wraith", 0x505050, 0x838383);
		this.generateSpawnEgg("yeti", 0xDEDEDE, 0x4675BB);

		this.generateLayeredItem(TFCommon.prefix("item/shield"), new Material(TFCommon.prefix("item/lich_shield_frame")), new Material(TFCommon.prefix("item/lich_shield_fill")));
	}

	private void generateSpawnEgg(String entityName, int primary, int secondary) {
		Item item = BuiltInRegistries.ITEM.getValue(TFCommon.prefix(entityName + "_spawn_egg"));
		Identifier model = this.generateLayeredItem(item, new Material(TFCommon.prefix("item/spawn_egg_base")), new Material(TFCommon.prefix("item/spawn_egg_overlay")));
		this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, ItemModelUtils.constantTint(primary), ItemModelUtils.constantTint(secondary)));
	}

	public void generatePattern(Item item) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(new Material(TFCommon.prefix("item/tf_banner_pattern"))), this.modelOutput)));
	}

	public void generateChestBoat(Item boat) {
		this.itemModelOutput.accept(boat, ItemModelUtils.plainModel(ModelTemplates.TWO_LAYERED_ITEM.create(boat, TextureMapping.layered(new Material(ModelLocationUtils.getModelLocation(Items.OAK_CHEST_BOAT)), new Material(ModelLocationUtils.getModelLocation(boat))), this.modelOutput)));
	}

	public void generateKnightmetalShield(Item shieldItem) {
		var normal = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem), new KnightmetalShieldSpecialRenderer.Unbaked());
		var blocking = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem, "_blocking"), new KnightmetalShieldSpecialRenderer.Unbaked());
		this.generateBooleanDispatch(shieldItem, ItemModelUtils.isUsingItem(), blocking, normal);
	}

	public void generateBow(Item bowItem, boolean twoLayered) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, ModelTemplates.BOW));
		ItemModel.Unbaked pull0 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_0", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_0", ModelTemplates.BOW));
		ItemModel.Unbaked pull1 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_1", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_1", ModelTemplates.BOW));
		ItemModel.Unbaked pull2 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_2", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_2", ModelTemplates.BOW));
		this.itemModelOutput.accept(bowItem, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
			ItemModelUtils.rangeSelect(
				new UseDuration(false),
				0.05F,
				pull0,
				ItemModelUtils.override(pull1, 0.65F),
				ItemModelUtils.override(pull2, 0.9F)
			), base));
	}

	public void generateOreMagnet(Item magnetItem) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_1", TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_2", TFModelTemplates.SPECIAL_HANDHELD));
		this.itemModelOutput.accept(magnetItem, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
			ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, base,
				ItemModelUtils.override(pulling1, 0.5F),
				ItemModelUtils.override(pulling2, 1.0F)),
			base));
	}

	public void generateMoonDial(Item dial) {
		List<RangeSelectItemModel.Entry> list = new ArrayList<>();
		ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(this.createFlatItemModel(dial, TFModelTemplates.MOON_DIAL));
		list.add(ItemModelUtils.override(itemmodel$unbaked, 0.0F));

		for (int i = 1; i < 8; i++) {
			ItemModel.Unbaked phase = ItemModelUtils.plainModel(this.createFlatItemModel(dial, "_" + i, TFModelTemplates.MOON_DIAL));
			list.add(ItemModelUtils.override(phase, (float) i - 0.5F));
		}

		list.add(ItemModelUtils.override(itemmodel$unbaked, 7.5F));
		this.itemModelOutput.accept(dial, ItemModelUtils.rangeSelect(new Time(false, Time.TimeSource.MOON_PHASE), 8.0F, list));
	}

	public void generatePotionFlask(Item flask, boolean crackable, Identifier empty) {
		List<RangeSelectItemModel.Entry> potionEntries = new ArrayList<>();
		List<RangeSelectItemModel.Entry> flaskEntries = new ArrayList<>();
		String[] suffixes = {"_labelled", "_splintered", "_damaged"};


		for (int i = 0; i < 3; i++) {
			potionEntries.add(ItemModelUtils.override(ItemModelUtils.tintedModel(this.createFlatItemModel(flask, "_" + (i + 1), ModelTemplates.FLAT_ITEM), new PotionFlaskTintSource()), i + 1));
			if (i == 0) {
				var base = ItemModelUtils.plainModel(this.createFlatItemModel(flask, ModelTemplates.FLAT_ITEM));
				flaskEntries.add(ItemModelUtils.override(crackable ?
					ItemModelUtils.conditional(new HasComponent(TFDataComponents.POTION_FLASK_CONTENTS, true),
						ItemModelUtils.plainModel(this.createFlatItemModel(flask, suffixes[i], ModelTemplates.FLAT_ITEM)), base) : base, 0));
			} else if (crackable) {
				flaskEntries.add(ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(flask, suffixes[i], ModelTemplates.FLAT_ITEM)), i));
			}
		}

		ItemModel.Unbaked flaskModel = crackable ? ItemModelUtils.rangeSelect(new PotionFlaskDamage(false), flaskEntries) : flaskEntries.getFirst().model();
		ItemModel.Unbaked potionModel = ItemModelUtils.rangeSelect(new PotionFlaskDosage(false), ItemModelUtils.plainModel(empty), potionEntries);

		this.itemModelOutput.accept(flask, ItemModelUtils.composite(potionModel, flaskModel));
	}

	public void generateTwoLayerItem(Item item, String modelSuffix, String suffix1, String suffix2, ModelTemplate template) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.twoLayerItem(item, modelSuffix, suffix1, suffix2, template)));
	}

	public void generateTwoLayerItem(Item item, String suffix, ModelTemplate template) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.twoLayerItem(item, "", "", suffix, template)));
	}

	public Identifier twoLayerItem(Item item, String suffix, ModelTemplate template) {
		return this.twoLayerItem(item, "", "", suffix, template);
	}

	public Identifier twoLayerItem(Item item, String modelSuffix, String suffix1, String suffix2, ModelTemplate template) {
		return template.create(ModelLocationUtils.getModelLocation(item, modelSuffix), TextureMapping.layered(TextureMapping.getItemTexture(item, suffix1 + modelSuffix), TextureMapping.getItemTexture(item, suffix2 + modelSuffix)), this.modelOutput);
	}

	public void generateTravellersGear(Item item, Identifier modifierDirectory) {
		this.itemModelOutput.accept(item, ItemModelUtils.conditional(new Broken(),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM)), modifierDirectory.withSuffix("/broken")),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM)), modifierDirectory)));
	}

	public void generateLayeredTravellersGear(Item item, Item overlay, String overlayName, ConditionalItemModelProperty property, Identifier modifierDirectory) {
		ItemModel.Unbaked gearModel = ItemModelUtils.conditional(new Broken(),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM)), modifierDirectory.withSuffix("/broken")),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM)), modifierDirectory));
		ItemModel.Unbaked baseOverlay = ItemModelUtils.plainModel(this.createFlatItemModel(overlay, ModelTemplates.FLAT_ITEM));
		ItemModel.Unbaked overlayModel = ItemModelUtils.conditional(new Broken(),
			ItemModelUtils.plainModel(this.attachedOverlayModel(modifierDirectory.withSuffix("/broken/" + overlayName))),
			ItemModelUtils.plainModel(this.attachedOverlayModel(modifierDirectory.withSuffix("/" + overlayName))));
		this.itemModelOutput.accept(overlay, baseOverlay);
		this.itemModelOutput.accept(item, ItemModelUtils.conditional(property, ItemModelUtils.composite(gearModel, overlayModel), gearModel));
	}

	public Identifier attachedOverlayModel(Identifier sprite) {
		Identifier texture = sprite.withPrefix("item/");
		return ModelTemplates.FLAT_ITEM.create(texture, TextureMapping.layer0(new Material(texture)), this.modelOutput);
	}
}
