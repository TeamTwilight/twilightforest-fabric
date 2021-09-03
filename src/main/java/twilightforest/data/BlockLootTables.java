//package twilightforest.data;
//
//import net.minecraft.advancements.critereon.StatePropertiesPredicate;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.ItemLike;
//import net.minecraft.world.level.block.*;
//import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
//import net.minecraft.world.level.storage.loot.LootPool;
//import net.minecraft.world.level.storage.loot.LootTable;
//import net.minecraft.world.level.storage.loot.entries.LootItem;
//import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
//import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
//import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
//import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
//import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
//import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
//import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
//import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
//import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
//import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
//import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
//import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
//import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
//import twilightforest.block.AbstractSkullCandleBlock;
//import twilightforest.block.KeepsakeCasketBlock;
//import twilightforest.block.TFBlocks;
//import twilightforest.item.TFItems;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
//	private final Set<Block> knownBlocks = new HashSet<>();
//	// [VanillaCopy] super
//	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
//	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
//
//	@Override
//	protected void add(Block block, LootTable.Builder builder) {
//		super.add(block, builder);
//		knownBlocks.add(block);
//	}
//
//	@Override
//	protected void addTables() {
//		registerEmpty(TFBlocks.experiment_115.get());
//		dropSelf(TFBlocks.tower_wood.get());
//		dropSelf(TFBlocks.tower_wood_encased.get());
//		dropSelf(TFBlocks.tower_wood_cracked.get());
//		dropSelf(TFBlocks.tower_wood_mossy.get());
//		registerEmpty(TFBlocks.antibuilder.get());
//		dropSelf(TFBlocks.carminite_builder.get());
//		dropSelf(TFBlocks.ghast_trap.get());
//		dropSelf(TFBlocks.carminite_reactor.get());
//		dropSelf(TFBlocks.reappearing_block.get());
//		dropSelf(TFBlocks.vanishing_block.get());
//		dropSelf(TFBlocks.locked_vanishing_block.get());
//		dropSelf(TFBlocks.firefly.get());
//		dropSelf(TFBlocks.cicada.get());
//		dropSelf(TFBlocks.moonworm.get());
//		dropSelf(TFBlocks.trophy_pedestal.get());
//		//registerDropSelfLootTable(TFBlocks.terrorcotta_circle.get());
//		//registerDropSelfLootTable(TFBlocks.terrorcotta_diagonal.get());
//		dropSelf(TFBlocks.aurora_block.get());
//		dropSelf(TFBlocks.aurora_pillar.get());
//		add(TFBlocks.aurora_slab.get(), createSlabItemTable(TFBlocks.aurora_slab.get()));
//		dropWhenSilkTouch(TFBlocks.auroralized_glass.get());
//		dropSelf(TFBlocks.underbrick.get());
//		dropSelf(TFBlocks.underbrick_cracked.get());
//		dropSelf(TFBlocks.underbrick_mossy.get());
//		dropSelf(TFBlocks.underbrick_floor.get());
//		dropSelf(TFBlocks.thorn_rose.get());
//		add(TFBlocks.thorn_leaves.get(), silkAndStick(TFBlocks.thorn_leaves.get(), Items.STICK, RARE_SAPLING_DROP_RATES));
//		add(TFBlocks.beanstalk_leaves.get(), silkAndStick(TFBlocks.beanstalk_leaves.get(), TFItems.magic_beans.get(), RARE_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.deadrock.get());
//		dropSelf(TFBlocks.deadrock_cracked.get());
//		dropSelf(TFBlocks.deadrock_weathered.get());
//		dropSelf(TFBlocks.trollsteinn.get());
//		dropWhenSilkTouch(TFBlocks.wispy_cloud.get());
//		dropSelf(TFBlocks.fluffy_cloud.get());
//		dropSelf(TFBlocks.giant_cobblestone.get());
//		dropSelf(TFBlocks.giant_log.get());
//		dropSelf(TFBlocks.giant_leaves.get());
//		dropSelf(TFBlocks.giant_obsidian.get());
//		add(TFBlocks.uberous_soil.get(), createSingleItemTable(Blocks.DIRT));
//		dropSelf(TFBlocks.huge_stalk.get());
//		add(TFBlocks.huge_mushgloom.get(), createMushroomBlockDrop(TFBlocks.huge_mushgloom.get(), TFBlocks.mushgloom.get()));
//		add(TFBlocks.huge_mushgloom_stem.get(), createMushroomBlockDrop(TFBlocks.huge_mushgloom_stem.get(), TFBlocks.mushgloom.get()));
//		add(TFBlocks.trollvidr.get(), createShearsOnlyDrop(TFBlocks.trollvidr.get()));
//		add(TFBlocks.unripe_trollber.get(), createShearsOnlyDrop(TFBlocks.unripe_trollber.get()));
//		add(TFBlocks.trollber.get(), createShearsDispatchTable(TFBlocks.trollber.get(), LootItem.lootTableItem(TFItems.torchberries.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
//		dropSelf(TFBlocks.huge_lilypad.get());
//		dropSelf(TFBlocks.huge_waterlily.get());
//		dropSelf(TFBlocks.castle_brick.get());
//		dropSelf(TFBlocks.castle_brick_worn.get());
//		dropSelf(TFBlocks.castle_brick_cracked.get());
//		dropSelf(TFBlocks.castle_brick_mossy.get());
//		dropSelf(TFBlocks.castle_brick_frame.get());
//		dropSelf(TFBlocks.castle_brick_roof.get());
//		dropSelf(TFBlocks.castle_pillar_encased.get());
//		dropSelf(TFBlocks.castle_pillar_encased_tile.get());
//		dropSelf(TFBlocks.castle_pillar_bold.get());
//		dropSelf(TFBlocks.castle_pillar_bold_tile.get());
//		dropSelf(TFBlocks.castle_stairs_brick.get());
//		dropSelf(TFBlocks.castle_stairs_worn.get());
//		dropSelf(TFBlocks.castle_stairs_cracked.get());
//		dropSelf(TFBlocks.castle_stairs_mossy.get());
//		dropSelf(TFBlocks.castle_stairs_encased.get());
//		dropSelf(TFBlocks.castle_stairs_bold.get());
//		dropSelf(TFBlocks.castle_rune_brick_purple.get());
//		dropSelf(TFBlocks.castle_rune_brick_yellow.get());
//		dropSelf(TFBlocks.castle_rune_brick_pink.get());
//		dropSelf(TFBlocks.castle_rune_brick_blue.get());
//		dropSelf(TFBlocks.cinder_furnace.get());
//		dropSelf(TFBlocks.cinder_log.get());
//		dropSelf(TFBlocks.cinder_wood.get());
//		dropSelf(TFBlocks.castle_door_purple.get());
//		dropSelf(TFBlocks.castle_door_yellow.get());
//		dropSelf(TFBlocks.castle_door_pink.get());
//		dropSelf(TFBlocks.castle_door_blue.get());
//		dropSelf(TFBlocks.twilight_portal_miniature_structure.get());
//		dropSelf(TFBlocks.naga_courtyard_miniature_structure.get());
//		dropSelf(TFBlocks.lich_tower_miniature_structure.get());
//		dropSelf(TFBlocks.knightmetal_block.get());
//		dropSelf(TFBlocks.ironwood_block.get());
//		dropSelf(TFBlocks.fiery_block.get());
//		dropSelf(TFBlocks.steeleaf_block.get());
//		dropSelf(TFBlocks.arctic_fur_block.get());
//		dropSelf(TFBlocks.carminite_block.get());
//		dropSelf(TFBlocks.maze_stone.get());
//		dropSelf(TFBlocks.maze_stone_brick.get());
//		dropSelf(TFBlocks.maze_stone_chiseled.get());
//		dropSelf(TFBlocks.maze_stone_decorative.get());
//		dropSelf(TFBlocks.maze_stone_cracked.get());
//		dropSelf(TFBlocks.maze_stone_mossy.get());
//		dropSelf(TFBlocks.maze_stone_mosaic.get());
//		dropSelf(TFBlocks.maze_stone_border.get());
//		dropWhenSilkTouch(TFBlocks.hedge.get());
//		add(TFBlocks.root.get(), createSingleItemTableWithSilkTouch(TFBlocks.root.get(), Items.STICK, UniformGenerator.between(3, 5)));
//		add(TFBlocks.liveroot_block.get(), createSingleItemTableWithSilkTouch(TFBlocks.liveroot_block.get(), TFItems.liveroot.get()));
//		dropSelf(TFBlocks.uncrafting_table.get());
//		dropSelf(TFBlocks.firefly_jar.get());
//		dropSelf(TFBlocks.firefly_spawner.get());
//		dropSelf(TFBlocks.cicada_jar.get());
//		add(TFBlocks.moss_patch.get(), createShearsOnlyDrop(TFBlocks.moss_patch.get()));
//		add(TFBlocks.mayapple.get(), createShearsOnlyDrop(TFBlocks.mayapple.get()));
//		add(TFBlocks.clover_patch.get(), createShearsOnlyDrop(TFBlocks.clover_patch.get()));
//		add(TFBlocks.fiddlehead.get(), createShearsOnlyDrop(TFBlocks.fiddlehead.get()));
//		dropSelf(TFBlocks.mushgloom.get());
//		add(TFBlocks.torchberry_plant.get(), createShearsDispatchTable(TFBlocks.torchberry_plant.get(), LootItem.lootTableItem(TFItems.torchberries.get())));
//		add(TFBlocks.root_strand.get(), createShearsOnlyDrop(TFBlocks.root_strand.get()));
//		add(TFBlocks.fallen_leaves.get(), createShearsOnlyDrop(TFBlocks.fallen_leaves.get()));
//		dropSelf(TFBlocks.smoker.get());
//		dropSelf(TFBlocks.encased_smoker.get());
//		dropSelf(TFBlocks.fire_jet.get());
//		dropSelf(TFBlocks.encased_fire_jet.get());
//		dropSelf(TFBlocks.naga_stone_head.get());
//		dropSelf(TFBlocks.naga_stone.get());
//		dropSelf(TFBlocks.spiral_bricks.get());
//		dropSelf(TFBlocks.nagastone_pillar.get());
//		dropSelf(TFBlocks.nagastone_pillar_mossy.get());
//		dropSelf(TFBlocks.nagastone_pillar_weathered.get());
//		dropSelf(TFBlocks.etched_nagastone.get());
//		dropSelf(TFBlocks.etched_nagastone_mossy.get());
//		dropSelf(TFBlocks.etched_nagastone_weathered.get());
//		dropSelf(TFBlocks.nagastone_stairs_left.get());
//		dropSelf(TFBlocks.nagastone_stairs_right.get());
//		dropSelf(TFBlocks.nagastone_stairs_mossy_left.get());
//		dropSelf(TFBlocks.nagastone_stairs_mossy_right.get());
//		dropSelf(TFBlocks.nagastone_stairs_weathered_left.get());
//		dropSelf(TFBlocks.nagastone_stairs_weathered_right.get());
//		add(TFBlocks.naga_trophy.get(), createSingleItemTable(TFBlocks.naga_trophy.get().asItem()));
//		add(TFBlocks.naga_wall_trophy.get(), createSingleItemTable(TFBlocks.naga_trophy.get().asItem()));
//		add(TFBlocks.lich_trophy.get(), createSingleItemTable(TFBlocks.lich_trophy.get().asItem()));
//		add(TFBlocks.lich_wall_trophy.get(), createSingleItemTable(TFBlocks.lich_trophy.get().asItem()));
//		add(TFBlocks.minoshroom_trophy.get(), createSingleItemTable(TFBlocks.minoshroom_trophy.get().asItem()));
//		add(TFBlocks.minoshroom_wall_trophy.get(), createSingleItemTable(TFBlocks.minoshroom_trophy.get().asItem()));
//		add(TFBlocks.hydra_trophy.get(), createSingleItemTable(TFBlocks.hydra_trophy.get().asItem()));
//		add(TFBlocks.hydra_wall_trophy.get(), createSingleItemTable(TFBlocks.hydra_trophy.get().asItem()));
//		add(TFBlocks.knight_phantom_trophy.get(), createSingleItemTable(TFBlocks.knight_phantom_trophy.get().asItem()));
//		add(TFBlocks.knight_phantom_wall_trophy.get(), createSingleItemTable(TFBlocks.knight_phantom_trophy.get().asItem()));
//		add(TFBlocks.ur_ghast_trophy.get(), createSingleItemTable(TFBlocks.ur_ghast_trophy.get().asItem()));
//		add(TFBlocks.ur_ghast_wall_trophy.get(), createSingleItemTable(TFBlocks.ur_ghast_trophy.get().asItem()));
//		add(TFBlocks.yeti_trophy.get(), createSingleItemTable(TFBlocks.yeti_trophy.get().asItem()));
//		add(TFBlocks.yeti_wall_trophy.get(), createSingleItemTable(TFBlocks.yeti_wall_trophy.get().asItem()));
//		add(TFBlocks.snow_queen_trophy.get(), createSingleItemTable(TFBlocks.snow_queen_trophy.get().asItem()));
//		add(TFBlocks.snow_queen_wall_trophy.get(), createSingleItemTable(TFBlocks.snow_queen_trophy.get().asItem()));
//		add(TFBlocks.quest_ram_trophy.get(), createSingleItemTable(TFBlocks.quest_ram_trophy.get().asItem()));
//		add(TFBlocks.quest_ram_wall_trophy.get(), createSingleItemTable(TFBlocks.quest_ram_trophy.get().asItem()));
//
//		add(TFBlocks.zombie_skull_candle.get(), dropWithoutSilk(Blocks.ZOMBIE_HEAD));
//		add(TFBlocks.zombie_wall_skull_candle.get(), dropWithoutSilk(Blocks.ZOMBIE_HEAD));
//		add(TFBlocks.skeleton_skull_candle.get(), dropWithoutSilk(Blocks.SKELETON_SKULL));
//		add(TFBlocks.skeleton_wall_skull_candle.get(), dropWithoutSilk(Blocks.SKELETON_SKULL));
//		add(TFBlocks.wither_skele_skull_candle.get(), dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
//		add(TFBlocks.wither_skele_wall_skull_candle.get(), dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
//		add(TFBlocks.creeper_skull_candle.get(), dropWithoutSilk(Blocks.CREEPER_HEAD));
//		add(TFBlocks.creeper_wall_skull_candle.get(), dropWithoutSilk(Blocks.CREEPER_HEAD));
//		add(TFBlocks.player_skull_candle.get(), dropWithoutSilk(Blocks.PLAYER_HEAD));
//		add(TFBlocks.player_wall_skull_candle.get(), dropWithoutSilk(Blocks.PLAYER_HEAD));
//
//		dropSelf(TFBlocks.iron_ladder.get());
//		dropSelf(TFBlocks.stone_twist.get());
//		dropSelf(TFBlocks.stone_twist_thin.get());
//		dropSelf(TFBlocks.stone_bold.get());
//		registerEmpty(TFBlocks.tome_spawner.get());
//		dropWhenSilkTouch(TFBlocks.empty_bookshelf.get());
//		//registerDropSelfLootTable(TFBlocks.lapis_block.get());
//		add(TFBlocks.keepsake_casket.get(), casketInfo(TFBlocks.keepsake_casket.get()));
//		dropPottedContents(TFBlocks.potted_twilight_oak_sapling.get());
//		dropPottedContents(TFBlocks.potted_canopy_sapling.get());
//		dropPottedContents(TFBlocks.potted_mangrove_sapling.get());
//		dropPottedContents(TFBlocks.potted_darkwood_sapling.get());
//		dropPottedContents(TFBlocks.potted_hollow_oak_sapling.get());
//		dropPottedContents(TFBlocks.potted_rainboak_sapling.get());
//		dropPottedContents(TFBlocks.potted_time_sapling.get());
//		dropPottedContents(TFBlocks.potted_trans_sapling.get());
//		dropPottedContents(TFBlocks.potted_mine_sapling.get());
//		dropPottedContents(TFBlocks.potted_sort_sapling.get());
//		dropPottedContents(TFBlocks.potted_mayapple.get());
//		dropPottedContents(TFBlocks.potted_fiddlehead.get());
//		dropPottedContents(TFBlocks.potted_mushgloom.get());
//		dropPottedContents(TFBlocks.potted_thorn.get());
//		dropPottedContents(TFBlocks.potted_green_thorn.get());
//		dropPottedContents(TFBlocks.potted_dead_thorn.get());
//
//		dropSelf(TFBlocks.oak_log.get());
//		dropSelf(TFBlocks.stripped_oak_log.get());
//		dropSelf(TFBlocks.oak_wood.get());
//		dropSelf(TFBlocks.stripped_oak_wood.get());
//		dropSelf(TFBlocks.oak_sapling.get());
//		add(TFBlocks.oak_leaves.get(), createLeavesDrops(TFBlocks.oak_leaves.get(), TFBlocks.oak_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.rainboak_sapling.get());
//		add(TFBlocks.rainboak_leaves.get(), createLeavesDrops(TFBlocks.rainboak_leaves.get(), TFBlocks.rainboak_sapling.get(), RARE_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.hollow_oak_sapling.get());
//		dropSelf(TFBlocks.twilight_oak_planks.get());
//		dropSelf(TFBlocks.twilight_oak_stairs.get());
//		add(TFBlocks.twilight_oak_slab.get(), createSlabItemTable(TFBlocks.twilight_oak_slab.get()));
//		dropSelf(TFBlocks.twilight_oak_button.get());
//		dropSelf(TFBlocks.twilight_oak_fence.get());
//		dropSelf(TFBlocks.twilight_oak_gate.get());
//		dropSelf(TFBlocks.twilight_oak_plate.get());
//		add(TFBlocks.twilight_oak_door.get(), createSinglePropConditionTable(TFBlocks.twilight_oak_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.twilight_oak_trapdoor.get());
//		add(TFBlocks.twilight_oak_sign.get(), createSingleItemTable(TFBlocks.twilight_oak_sign.get().asItem()));
//		add(TFBlocks.twilight_wall_sign.get(), createSingleItemTable(TFBlocks.twilight_oak_sign.get().asItem()));
//
//		dropSelf(TFBlocks.canopy_log.get());
//		dropSelf(TFBlocks.stripped_canopy_log.get());
//		dropSelf(TFBlocks.canopy_wood.get());
//		dropSelf(TFBlocks.stripped_canopy_wood.get());
//		dropSelf(TFBlocks.canopy_sapling.get());
//		add(TFBlocks.canopy_leaves.get(), createLeavesDrops(TFBlocks.canopy_leaves.get(), TFBlocks.canopy_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.canopy_planks.get());
//		dropSelf(TFBlocks.canopy_stairs.get());
//		add(TFBlocks.canopy_slab.get(), createSlabItemTable(TFBlocks.canopy_slab.get()));
//		dropSelf(TFBlocks.canopy_button.get());
//		dropSelf(TFBlocks.canopy_fence.get());
//		dropSelf(TFBlocks.canopy_gate.get());
//		dropSelf(TFBlocks.canopy_plate.get());
//		add(TFBlocks.canopy_door.get(), createSinglePropConditionTable(TFBlocks.canopy_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.canopy_trapdoor.get());
//		add(TFBlocks.canopy_sign.get(), createSingleItemTable(TFBlocks.canopy_sign.get().asItem()));
//		add(TFBlocks.canopy_wall_sign.get(), createSingleItemTable(TFBlocks.canopy_sign.get().asItem()));
//		add(TFBlocks.canopy_bookshelf.get(), createSingleItemTableWithSilkTouch(TFBlocks.canopy_bookshelf.get(), Items.BOOK, ConstantValue.exactly(3.0F)));
//
//		dropSelf(TFBlocks.mangrove_log.get());
//		dropSelf(TFBlocks.stripped_mangrove_log.get());
//		dropSelf(TFBlocks.mangrove_wood.get());
//		dropSelf(TFBlocks.stripped_mangrove_wood.get());
//		dropSelf(TFBlocks.mangrove_sapling.get());
//		add(TFBlocks.mangrove_leaves.get(), createLeavesDrops(TFBlocks.mangrove_leaves.get(), TFBlocks.mangrove_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.mangrove_planks.get());
//		dropSelf(TFBlocks.mangrove_stairs.get());
//		add(TFBlocks.mangrove_slab.get(), createSlabItemTable(TFBlocks.mangrove_slab.get()));
//		dropSelf(TFBlocks.mangrove_button.get());
//		dropSelf(TFBlocks.mangrove_fence.get());
//		dropSelf(TFBlocks.mangrove_gate.get());
//		dropSelf(TFBlocks.mangrove_plate.get());
//		add(TFBlocks.mangrove_door.get(), createSinglePropConditionTable(TFBlocks.mangrove_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.mangrove_trapdoor.get());
//		add(TFBlocks.mangrove_sign.get(), createSingleItemTable(TFBlocks.mangrove_sign.get().asItem()));
//		add(TFBlocks.mangrove_wall_sign.get(), createSingleItemTable(TFBlocks.mangrove_sign.get().asItem()));
//
//		dropSelf(TFBlocks.dark_log.get());
//		dropSelf(TFBlocks.stripped_dark_log.get());
//		dropSelf(TFBlocks.dark_wood.get());
//		dropSelf(TFBlocks.stripped_dark_wood.get());
//		dropSelf(TFBlocks.darkwood_sapling.get());
//		add(TFBlocks.dark_leaves.get(), createLeavesDrops(TFBlocks.dark_leaves.get(), TFBlocks.darkwood_sapling.get(), RARE_SAPLING_DROP_RATES));
//		dropSelf(TFBlocks.dark_planks.get());
//		dropSelf(TFBlocks.dark_stairs.get());
//		add(TFBlocks.dark_slab.get(), createSlabItemTable(TFBlocks.dark_slab.get()));
//		dropSelf(TFBlocks.dark_button.get());
//		dropSelf(TFBlocks.dark_fence.get());
//		dropSelf(TFBlocks.dark_gate.get());
//		dropSelf(TFBlocks.dark_plate.get());
//		add(TFBlocks.dark_door.get(), createSinglePropConditionTable(TFBlocks.dark_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.dark_trapdoor.get());
//		add(TFBlocks.darkwood_sign.get(), createSingleItemTable(TFBlocks.darkwood_sign.get().asItem()));
//		add(TFBlocks.darkwood_wall_sign.get(), createSingleItemTable(TFBlocks.darkwood_sign.get().asItem()));
//
//		dropSelf(TFBlocks.time_log.get());
//		dropSelf(TFBlocks.stripped_time_log.get());
//		dropSelf(TFBlocks.time_wood.get());
//		dropSelf(TFBlocks.stripped_time_wood.get());
//		dropOther(TFBlocks.time_log_core.get(), TFBlocks.time_log.get());
//		dropSelf(TFBlocks.time_sapling.get());
//		registerLeavesNoSapling(TFBlocks.time_leaves.get());
//		dropSelf(TFBlocks.time_planks.get());
//		dropSelf(TFBlocks.time_stairs.get());
//		add(TFBlocks.time_slab.get(), createSlabItemTable(TFBlocks.time_slab.get()));
//		dropSelf(TFBlocks.time_button.get());
//		dropSelf(TFBlocks.time_fence.get());
//		dropSelf(TFBlocks.time_gate.get());
//		dropSelf(TFBlocks.time_plate.get());
//		add(TFBlocks.time_door.get(), createSinglePropConditionTable(TFBlocks.time_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.time_trapdoor.get());
//		add(TFBlocks.time_sign.get(), createSingleItemTable(TFBlocks.time_sign.get().asItem()));
//		add(TFBlocks.time_wall_sign.get(), createSingleItemTable(TFBlocks.time_sign.get().asItem()));
//
//		dropSelf(TFBlocks.transformation_log.get());
//		dropSelf(TFBlocks.stripped_transformation_log.get());
//		dropSelf(TFBlocks.transformation_wood.get());
//		dropSelf(TFBlocks.stripped_transformation_wood.get());
//		dropOther(TFBlocks.transformation_log_core.get(), TFBlocks.transformation_log.get());
//		dropSelf(TFBlocks.transformation_sapling.get());
//		registerLeavesNoSapling(TFBlocks.transformation_leaves.get());
//		dropSelf(TFBlocks.trans_planks.get());
//		dropSelf(TFBlocks.trans_stairs.get());
//		add(TFBlocks.trans_slab.get(), createSlabItemTable(TFBlocks.trans_slab.get()));
//		dropSelf(TFBlocks.trans_button.get());
//		dropSelf(TFBlocks.trans_fence.get());
//		dropSelf(TFBlocks.trans_gate.get());
//		dropSelf(TFBlocks.trans_plate.get());
//		add(TFBlocks.trans_door.get(), createSinglePropConditionTable(TFBlocks.trans_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.trans_trapdoor.get());
//		add(TFBlocks.trans_sign.get(), createSingleItemTable(TFBlocks.trans_sign.get().asItem()));
//		add(TFBlocks.trans_wall_sign.get(), createSingleItemTable(TFBlocks.trans_sign.get().asItem()));
//
//		dropSelf(TFBlocks.mining_log.get());
//		dropSelf(TFBlocks.stripped_mining_log.get());
//		dropSelf(TFBlocks.mining_wood.get());
//		dropSelf(TFBlocks.stripped_mining_wood.get());
//		dropOther(TFBlocks.mining_log_core.get(), TFBlocks.mining_log.get());
//		dropSelf(TFBlocks.mining_sapling.get());
//		registerLeavesNoSapling(TFBlocks.mining_leaves.get());
//		dropSelf(TFBlocks.mine_planks.get());
//		dropSelf(TFBlocks.mine_stairs.get());
//		add(TFBlocks.mine_slab.get(), createSlabItemTable(TFBlocks.mine_slab.get()));
//		dropSelf(TFBlocks.mine_button.get());
//		dropSelf(TFBlocks.mine_fence.get());
//		dropSelf(TFBlocks.mine_gate.get());
//		dropSelf(TFBlocks.mine_plate.get());
//		add(TFBlocks.mine_door.get(), createSinglePropConditionTable(TFBlocks.mine_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.mine_trapdoor.get());
//		add(TFBlocks.mine_sign.get(), createSingleItemTable(TFBlocks.mine_sign.get().asItem()));
//		add(TFBlocks.mine_wall_sign.get(), createSingleItemTable(TFBlocks.mine_sign.get().asItem()));
//
//		dropSelf(TFBlocks.sorting_log.get());
//		dropSelf(TFBlocks.stripped_sorting_log.get());
//		dropSelf(TFBlocks.sorting_wood.get());
//		dropSelf(TFBlocks.stripped_sorting_wood.get());
//		dropOther(TFBlocks.sorting_log_core.get(), TFBlocks.sorting_log.get());
//		dropSelf(TFBlocks.sorting_sapling.get());
//		registerLeavesNoSapling(TFBlocks.sorting_leaves.get());
//		dropSelf(TFBlocks.sort_planks.get());
//		dropSelf(TFBlocks.sort_stairs.get());
//		add(TFBlocks.sort_slab.get(), createSlabItemTable(TFBlocks.sort_slab.get()));
//		dropSelf(TFBlocks.sort_button.get());
//		dropSelf(TFBlocks.sort_fence.get());
//		dropSelf(TFBlocks.sort_gate.get());
//		dropSelf(TFBlocks.sort_plate.get());
//		add(TFBlocks.sort_door.get(), createSinglePropConditionTable(TFBlocks.sort_door.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
//		dropSelf(TFBlocks.sort_trapdoor.get());
//		add(TFBlocks.sort_sign.get(), createSingleItemTable(TFBlocks.sort_sign.get().asItem()));
//		add(TFBlocks.sort_wall_sign.get(), createSingleItemTable(TFBlocks.sort_sign.get().asItem()));
//
//
//	}
//
//	private void registerLeavesNoSapling(Block leaves) {
//		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
//						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
//						.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
//		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
//	}
//
//
//	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
//	private static LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
//		LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124066_");
//		return createSilkTouchOrShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NOT_SILK_TOUCH_OR_SHEARS).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
//	}
//
//	private static LootTable.Builder casketInfo(Block block) {
//		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE)));
//	}
//
//	private static LootTable.Builder dropWithoutSilk(Block block) {
//		LootItemCondition.Builder HAS_SILK_TOUCH = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124062_");
//		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(HAS_SILK_TOUCH.invert()).add(LootItem.lootTableItem(block)));
//	}
//
//	private void registerEmpty(Block b) {
//		add(b, LootTable.lootTable());
//	}
//
//	@Override
//	protected Iterable<Block> getKnownBlocks() {
//		// todo 1.15 once all blockitems are ported, change this to all TF blocks, so an error will be thrown if we're missing any tables
//		return knownBlocks;
//	}
//}
