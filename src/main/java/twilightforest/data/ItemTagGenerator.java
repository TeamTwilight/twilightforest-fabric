package twilightforest.data;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class ItemTagGenerator extends ItemTagsProvider {
	public static final Tag.Named<Item> TWILIGHT_OAK_LOGS = ItemTags.bind(TwilightForestMod.prefix("twilight_oak_logs").toString());
	public static final Tag.Named<Item> CANOPY_LOGS = ItemTags.bind(TwilightForestMod.prefix("canopy_logs").toString());
	public static final Tag.Named<Item> MANGROVE_LOGS = ItemTags.bind(TwilightForestMod.prefix("mangrove_logs").toString());
	public static final Tag.Named<Item> DARKWOOD_LOGS = ItemTags.bind(TwilightForestMod.prefix("darkwood_logs").toString());
	public static final Tag.Named<Item> TIME_LOGS = ItemTags.bind(TwilightForestMod.prefix("timewood_logs").toString());
	public static final Tag.Named<Item> TRANSFORMATION_LOGS = ItemTags.bind(TwilightForestMod.prefix("transwood_logs").toString());
	public static final Tag.Named<Item> MINING_LOGS = ItemTags.bind(TwilightForestMod.prefix("mining_logs").toString());
	public static final Tag.Named<Item> SORTING_LOGS = ItemTags.bind(TwilightForestMod.prefix("sortwood_logs").toString());

	public static final Tag.Named<Item> TWILIGHT_LOGS = ItemTags.bind(TwilightForestMod.prefix("logs").toString());
	public static final Tag.Named<Item> TF_FENCES = ItemTags.bind(TwilightForestMod.prefix("fences").toString());
	public static final Tag.Named<Item> TF_FENCE_GATES = ItemTags.bind(TwilightForestMod.prefix("fence_gates").toString());

	public static final Tag.Named<Item> PAPER = ItemTags.bind("forge:paper");

	public static final Tag.Named<Item> TOWERWOOD = ItemTags.bind(TwilightForestMod.prefix("towerwood").toString());

	public static final Tag.Named<Item> FIERY_VIAL = ItemTags.bind(TwilightForestMod.prefix("fiery_vial").toString());

	public static final Tag.Named<Item> ARCTIC_FUR = ItemTags.bind(TwilightForestMod.prefix("arctic_fur").toString());
	public static final Tag.Named<Item> CARMINITE_GEMS = ItemTags.bind("forge:gems/carminite");
	public static final Tag.Named<Item> FIERY_INGOTS = ItemTags.bind("forge:ingots/fiery");
	public static final Tag.Named<Item> IRONWOOD_INGOTS = ItemTags.bind("forge:ingots/ironwood");
	public static final Tag.Named<Item> KNIGHTMETAL_INGOTS = ItemTags.bind("forge:ingots/knightmetal");
	public static final Tag.Named<Item> STEELEAF_INGOTS = ItemTags.bind("forge:ingots/steeleaf");

	public static final Tag.Named<Item> STORAGE_BLOCKS_ARCTIC_FUR = ItemTags.bind("forge:storage_blocks/arctic_fur");
	public static final Tag.Named<Item> STORAGE_BLOCKS_CARMINITE = ItemTags.bind("forge:storage_blocks/carminite");
	public static final Tag.Named<Item> STORAGE_BLOCKS_FIERY = ItemTags.bind("forge:storage_blocks/fiery");
	public static final Tag.Named<Item> STORAGE_BLOCKS_IRONWOOD = ItemTags.bind("forge:storage_blocks/ironwood");
	public static final Tag.Named<Item> STORAGE_BLOCKS_KNIGHTMETAL = ItemTags.bind("forge:storage_blocks/knightmetal");
	public static final Tag.Named<Item> STORAGE_BLOCKS_STEELEAF = ItemTags.bind("forge:storage_blocks/steeleaf");

	public static final Tag.Named<Item> ORES_IRONWOOD = ItemTags.bind("forge:ores/ironwood");
	public static final Tag.Named<Item> ORES_KNIGHTMETAL = ItemTags.bind("forge:ores/knightmetal");

	public static final Tag.Named<Item> PORTAL_ACTIVATOR = ItemTags.bind(TwilightForestMod.prefix("portal/activator").toString());

	public static final Tag.Named<Item> WIP = ItemTags.bind(TwilightForestMod.prefix("wip").toString());
	public static final Tag.Named<Item> NYI = ItemTags.bind(TwilightForestMod.prefix("nyi").toString());

	public static final Tag.Named<Item> KOBOLD_PACIFICATION_BREADS = ItemTags.bind(TwilightForestMod.prefix("kobold_pacification_breads").toString());

	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockprovider, ExistingFileHelper exFileHelper) {
		super(generator, blockprovider, TwilightForestMod.ID, exFileHelper);
	}

	@Override
	protected void addTags() {
		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);

		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
		tag(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		tag(ItemTags.LOGS_THAT_BURN)
				.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
				.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTagGenerator.TF_FENCES, TF_FENCES);
		this.copy(BlockTagGenerator.TF_FENCE_GATES, TF_FENCE_GATES);
		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
		this.copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);

		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);

		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		tag(ItemTags.SIGNS).add(TFBlocks.twilight_oak_sign.get().asItem(), TFBlocks.canopy_sign.get().asItem(),
				TFBlocks.mangrove_sign.get().asItem(), TFBlocks.darkwood_sign.get().asItem(),
				TFBlocks.time_sign.get().asItem(), TFBlocks.trans_sign.get().asItem(),
				TFBlocks.mine_sign.get().asItem(), TFBlocks.sort_sign.get().asItem());

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		tag(Tags.Items.STORAGE_BLOCKS)
				.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
				.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
				.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.ORES_IRONWOOD, ORES_IRONWOOD);
		this.copy(BlockTagGenerator.ORES_KNIGHTMETAL, ORES_KNIGHTMETAL);

		tag(Tags.Items.ORES).addTag(ORES_IRONWOOD).addTag(ORES_KNIGHTMETAL);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);

		tag(PAPER).add(Items.PAPER);
		tag(Tags.Items.FEATHERS).add(Items.FEATHER).add(TFItems.raven_feather.get());

		tag(FIERY_VIAL).add(TFItems.fiery_blood.get(), TFItems.fiery_tears.get());

		tag(ARCTIC_FUR).add(TFItems.arctic_fur.get());
		tag(CARMINITE_GEMS).add(TFItems.carminite.get());
		tag(FIERY_INGOTS).add(TFItems.fiery_ingot.get());
		tag(IRONWOOD_INGOTS).add(TFItems.ironwood_ingot.get());
		tag(KNIGHTMETAL_INGOTS).add(TFItems.knightmetal_ingot.get());
		tag(STEELEAF_INGOTS).add(TFItems.steeleaf_ingot.get());

		tag(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		tag(Tags.Items.INGOTS)
				.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
				.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		tag(ORES_IRONWOOD).add(TFItems.ironwood_raw.get());
		tag(ORES_KNIGHTMETAL).add(TFItems.armor_shard_cluster.get());

		tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);

		tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
				TFItems.fiery_helmet.get(),
				TFItems.fiery_chestplate.get(),
				TFItems.fiery_leggings.get(),
				TFItems.fiery_boots.get(),
				TFItems.arctic_helmet.get(),
				TFItems.arctic_chestplate.get(),
				TFItems.arctic_leggings.get(),
				TFItems.arctic_boots.get(),
				TFItems.yeti_helmet.get(),
				TFItems.yeti_chestplate.get(),
				TFItems.yeti_leggings.get(),
				TFItems.yeti_boots.get()
		);

		tag(WIP).add(
				TFBlocks.moss_patch.get().asItem(),
				TFBlocks.underbrick_floor.get().asItem(),
				TFBlocks.keepsake_casket.get().asItem(),
				TFItems.cube_of_annihilation.get()
		);

		tag(NYI).add(
				TFBlocks.cinder_furnace.get().asItem(),
				TFBlocks.cinder_log.get().asItem(),
				TFBlocks.cinder_wood.get().asItem(),
				TFBlocks.twilight_portal_miniature_structure.get().asItem(),
				TFBlocks.naga_courtyard_miniature_structure.get().asItem(),
				TFBlocks.lich_tower_miniature_structure.get().asItem(),
				TFBlocks.clover_patch.get().asItem(),
				TFBlocks.auroralized_glass.get().asItem(),
				TFBlocks.slider.get().asItem(),
				TFBlocks.stone_twist.get().asItem(),
				TFBlocks.stone_twist_thin.get().asItem(),
				TFItems.ore_meter.get()
		);

		tag(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
	}
}
