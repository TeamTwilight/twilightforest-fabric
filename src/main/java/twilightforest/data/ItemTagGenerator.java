//package twilightforest.data;
//
//import net.minecraft.data.tags.BlockTagsProvider;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.tags.ItemTagsProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.Items;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.tags.Tag;
//import net.minecraft.tags.ItemTags;
//
//import net.fabricmc.fabric.api.tag.FabricItemTags;
//import net.fabricmc.fabric.api.tag.(Tag.Named<Item>) TagRegistry;
//import net.minecraftforge.common.Tags;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import twilightforest.TwilightForestMod;
//import twilightforest.block.TFBlocks;
//import twilightforest.item.TFItems;
//
//public class ItemTagGenerator extends ItemTagsProvider {
//	public static final Tag.Named<Item> TWILIGHT_OAK_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("twilight_oak_logs").toString());
//	public static final Tag.Named<Item> CANOPY_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("canopy_logs").toString());
//	public static final Tag.Named<Item> MANGROVE_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("mangrove_logs").toString());
//	public static final Tag.Named<Item> DARKWOOD_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("darkwood_logs").toString());
//	public static final Tag.Named<Item> TIME_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("timewood_logs").toString());
//	public static final Tag.Named<Item> TRANSFORMATION_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("transwood_logs").toString());
//	public static final Tag.Named<Item> MINING_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("mining_logs").toString());
//	public static final Tag.Named<Item> SORTING_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("sortwood_logs").toString());
//
//	public static final Tag.Named<Item> TWILIGHT_LOGS = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("logs").toString());
//	public static final Tag.Named<Item> TF_FENCES = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("fences").toString());
//	public static final Tag.Named<Item> TF_FENCE_GATES = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("fence_gates").toString());
//
//	public static final Tag.Named<Item> PAPER = (Tag.Named<Item>) TagRegistry.item("forge:paper");
//
//	public static final Tag.Named<Item> TOWERWOOD = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("towerwood"));
//
//	public static final Tag.Named<Item> FIERY_VIAL = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("fiery_vial"));
//
//	public static final Tag.Named<Item> ARCTIC_FUR = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("arctic_fur"));
//	public static final Tag.Named<Item> CARMINITE_GEMS = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:carminite_gems"));
//	public static final Tag.Named<Item> FIERY_INGOTS = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:ingots/fiery"));
//	public static final Tag.Named<Item> IRONWOOD_INGOTS = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:ironwood_ingots"));
//	public static final Tag.Named<Item> KNIGHTMETAL_INGOTS = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:knightmetal_ingots"));
//	public static final Tag.Named<Item> STEELEAF_INGOTS = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:steeleaf_ingots"));
//
//	public static final Tag.Named<Item> STORAGE_BLOCKS_ARCTIC_FUR = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:arctic_fur_storage_block"));
//	public static final Tag.Named<Item> STORAGE_BLOCKS_CARMINITE = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:carminite_storage_block"));
//	public static final Tag.Named<Item> STORAGE_BLOCKS_FIERY = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:fiery_storage_block"));
//	public static final Tag.Named<Item> STORAGE_BLOCKS_IRONWOOD = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:ironwood_storage_block"));
//	public static final Tag.Named<Item> STORAGE_BLOCKS_KNIGHTMETAL = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:knightmetal_storage_block"));
//	public static final Tag.Named<Item> STORAGE_BLOCKS_STEELEAF = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:steeleaf_storage_block"));
//
//	public static final Tag.Named<Item> ORES_IRONWOOD = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:ironwood_ores"));
//	public static final Tag.Named<Item> ORES_KNIGHTMETAL = (Tag.Named<Item>) TagRegistry.item(new ResourceLocation("c:knightmetal_ores"));
//
//	public static final Tag.Named<Item> PORTAL_ACTIVATOR = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("portal/activator"));
//
//	public static final Tag.Named<Item> WIP = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("wip"));
//	public static final Tag.Named<Item> NYI = (Tag.Named<Item>) TagRegistry.item(TwilightForestMod.prefix("nyi"));
//
//	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockprovider, ExistingFileHelper exFileHelper) {
//		super(generator, blockprovider, TwilightForestMod.ID, exFileHelper);
//	}
//
//	@Override
//	protected void addTags() {
//		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
//		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
//		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
//		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
//		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
//		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
//		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
//		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);
//
//		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
//		tag(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
//		tag(ItemTags.LOGS_THAT_BURN)
//				.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
//				.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);
//
//		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
//		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);
//
//		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
//
//		this.copy(BlockTagGenerator.TF_FENCES, TF_FENCES);
//		this.copy(BlockTagGenerator.TF_FENCE_GATES, TF_FENCE_GATES);
//		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
//		this.copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
//		this.copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
//		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
//		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
//
//		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
//		this.copy(BlockTags.SLABS, ItemTags.SLABS);
//		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
//		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
//
//		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
//		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
//
//		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
//		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
//		tag(ItemTags.SIGNS).add(TFBlocks.twilight_oak_sign.get().asItem(), TFBlocks.canopy_sign.get().asItem(),
//				TFBlocks.mangrove_sign.get().asItem(), TFBlocks.darkwood_sign.get().asItem(),
//				TFBlocks.time_sign.get().asItem(), TFBlocks.trans_sign.get().asItem(),
//				TFBlocks.mine_sign.get().asItem(), TFBlocks.sort_sign.get().asItem());
//
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
//		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);
//
//		tag(Tags.Items.STORAGE_BLOCKS)
//				.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
//				.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
//				.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);
//
//		this.copy(BlockTagGenerator.ORES_IRONWOOD, ORES_IRONWOOD);
//		this.copy(BlockTagGenerator.ORES_KNIGHTMETAL, ORES_KNIGHTMETAL);
//
//		tag(Tags.Items.ORES).addTag(ORES_IRONWOOD).addTag(ORES_KNIGHTMETAL);
//
//		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);
//
//		tag(PAPER).add(Items.PAPER);
//		tag(Tags.Items.FEATHERS).add(Items.FEATHER).add(TFItems.raven_feather.get());
//
//		tag(FIERY_VIAL).add(TFItems.fiery_blood.get(), TFItems.fiery_tears.get());
//
//		tag(ARCTIC_FUR).add(TFItems.arctic_fur.get());
//		tag(CARMINITE_GEMS).add(TFItems.carminite.get());
//		tag(FIERY_INGOTS).add(TFItems.fiery_ingot.get());
//		tag(IRONWOOD_INGOTS).add(TFItems.ironwood_ingot.get());
//		tag(KNIGHTMETAL_INGOTS).add(TFItems.knightmetal_ingot.get());
//		tag(STEELEAF_INGOTS).add(TFItems.steeleaf_ingot.get());
//
//		tag(Tags.Items.GEMS).addTag(CARMINITE_GEMS);
//
//		tag(Tags.Items.INGOTS)
//				.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
//				.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);
//
//		tag(ORES_IRONWOOD).add(TFItems.ironwood_raw.get());
//		tag(ORES_KNIGHTMETAL).add(TFItems.armor_shard_cluster.get());
//
//		tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
//
//		tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
//				TFItems.fiery_helmet.get(),
//				TFItems.fiery_chestplate.get(),
//				TFItems.fiery_leggings.get(),
//				TFItems.fiery_boots.get(),
//				TFItems.arctic_helmet.get(),
//				TFItems.arctic_chestplate.get(),
//				TFItems.arctic_leggings.get(),
//				TFItems.arctic_boots.get(),
//				TFItems.yeti_helmet.get(),
//				TFItems.yeti_chestplate.get(),
//				TFItems.yeti_leggings.get(),
//				TFItems.yeti_boots.get()
//		);
//
//		tag(WIP).add(
//				TFBlocks.moss_patch.get().asItem(),
//				TFBlocks.underbrick_floor.get().asItem(),
//				TFBlocks.keepsake_casket.get().asItem(),
//				TFItems.cube_of_annihilation.get()
//		);
//
//		tag(NYI).add(
//				TFBlocks.cinder_furnace.get().asItem(),
//				TFBlocks.cinder_log.get().asItem(),
//				TFBlocks.cinder_wood.get().asItem(),
//				TFBlocks.twilight_portal_miniature_structure.get().asItem(),
//				TFBlocks.naga_courtyard_miniature_structure.get().asItem(),
//				TFBlocks.lich_tower_miniature_structure.get().asItem(),
//				TFBlocks.clover_patch.get().asItem(),
//				TFBlocks.auroralized_glass.get().asItem(),
//				TFBlocks.slider.get().asItem(),
//				TFBlocks.stone_twist.get().asItem(),
//				TFBlocks.stone_twist_thin.get().asItem(),
//				TFItems.ore_meter.get()
//		);
//	}
//}
