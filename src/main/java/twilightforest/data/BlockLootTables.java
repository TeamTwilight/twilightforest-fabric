package twilightforest.data;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import com.google.common.collect.Sets;
import twilightforest.block.AbstractParticleSpawnerBlock;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
	private final Set<Block> knownBlocks = new HashSet<>();
	// [VanillaCopy] super
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

	@Override
	public void add(Block block, LootTable.Builder builder) {
		super.add(block, builder);
		knownBlocks.add(block);
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_124179_) {
		this.addTables();
		Set<ResourceLocation> set = Sets.newHashSet();

		for(Block block : getKnownBlocks()) {
			ResourceLocation resourcelocation = block.getLootTable();
			if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
				LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
				if (loottable$builder == null) {
					throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey(block)));
				}

				p_124179_.accept(resourcelocation, loottable$builder);
			}
		}

		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}

	//@Override
	protected void addTables() {
		registerEmpty(TFBlocks.experiment_115);
		dropSelf(TFBlocks.tower_wood);
		dropSelf(TFBlocks.tower_wood_encased);
		dropSelf(TFBlocks.tower_wood_cracked);
		dropSelf(TFBlocks.tower_wood_mossy);
		registerEmpty(TFBlocks.antibuilder);
		dropSelf(TFBlocks.carminite_builder);
		dropSelf(TFBlocks.ghast_trap);
		dropSelf(TFBlocks.carminite_reactor);
		dropSelf(TFBlocks.reappearing_block);
		dropSelf(TFBlocks.vanishing_block);
		dropSelf(TFBlocks.locked_vanishing_block);
		dropSelf(TFBlocks.firefly);
		dropSelf(TFBlocks.cicada);
		dropSelf(TFBlocks.moonworm);
		dropSelf(TFBlocks.trophy_pedestal);
		//registerDropSelfLootTable(TFBlocks.terrorcotta_circle);
		//registerDropSelfLootTable(TFBlocks.terrorcotta_diagonal);
		dropSelf(TFBlocks.aurora_block);
		dropSelf(TFBlocks.aurora_pillar);
		add(TFBlocks.aurora_slab, createSlabItemTable(TFBlocks.aurora_slab));
		dropWhenSilkTouch(TFBlocks.auroralized_glass);
		dropSelf(TFBlocks.underbrick);
		dropSelf(TFBlocks.underbrick_cracked);
		dropSelf(TFBlocks.underbrick_mossy);
		dropSelf(TFBlocks.underbrick_floor);
		dropSelf(TFBlocks.thorn_rose);
		add(TFBlocks.thorn_leaves, silkAndStick(TFBlocks.thorn_leaves, Items.STICK, RARE_SAPLING_DROP_RATES));
		add(TFBlocks.beanstalk_leaves, silkAndStick(TFBlocks.beanstalk_leaves, TFItems.magic_beans, RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.deadrock);
		dropSelf(TFBlocks.deadrock_cracked);
		dropSelf(TFBlocks.deadrock_weathered);
		dropSelf(TFBlocks.trollsteinn);
		dropWhenSilkTouch(TFBlocks.wispy_cloud);
		dropSelf(TFBlocks.fluffy_cloud);
		dropSelf(TFBlocks.giant_cobblestone);
		dropSelf(TFBlocks.giant_log);
		dropSelf(TFBlocks.giant_leaves);
		dropSelf(TFBlocks.giant_obsidian);
		add(TFBlocks.uberous_soil, createSingleItemTable(Blocks.DIRT));
		dropSelf(TFBlocks.huge_stalk);
		add(TFBlocks.huge_mushgloom, createMushroomBlockDrop(TFBlocks.huge_mushgloom, TFBlocks.mushgloom));
		add(TFBlocks.huge_mushgloom_stem, createMushroomBlockDrop(TFBlocks.huge_mushgloom_stem, TFBlocks.mushgloom));
		add(TFBlocks.trollvidr, createShearsOnlyDrop(TFBlocks.trollvidr));
		add(TFBlocks.unripe_trollber, createShearsOnlyDrop(TFBlocks.unripe_trollber));
		add(TFBlocks.trollber, createShearsDispatchTable(TFBlocks.trollber, LootItem.lootTableItem(TFItems.torchberries).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(TFBlocks.huge_lilypad);
		dropSelf(TFBlocks.huge_waterlily);
		dropSelf(TFBlocks.castle_brick);
		dropSelf(TFBlocks.castle_brick_worn);
		dropSelf(TFBlocks.castle_brick_cracked);
		dropSelf(TFBlocks.castle_brick_mossy);
		dropSelf(TFBlocks.castle_brick_frame);
		dropSelf(TFBlocks.castle_brick_roof);
		dropSelf(TFBlocks.castle_pillar_encased);
		dropSelf(TFBlocks.castle_pillar_encased_tile);
		dropSelf(TFBlocks.castle_pillar_bold);
		dropSelf(TFBlocks.castle_pillar_bold_tile);
		dropSelf(TFBlocks.castle_stairs_brick);
		dropSelf(TFBlocks.castle_stairs_worn);
		dropSelf(TFBlocks.castle_stairs_cracked);
		dropSelf(TFBlocks.castle_stairs_mossy);
		dropSelf(TFBlocks.castle_stairs_encased);
		dropSelf(TFBlocks.castle_stairs_bold);
		dropSelf(TFBlocks.castle_rune_brick_purple);
		dropSelf(TFBlocks.castle_rune_brick_yellow);
		dropSelf(TFBlocks.castle_rune_brick_pink);
		dropSelf(TFBlocks.castle_rune_brick_blue);
		dropSelf(TFBlocks.cinder_furnace);
		dropSelf(TFBlocks.cinder_log);
		dropSelf(TFBlocks.cinder_wood);
		dropSelf(TFBlocks.castle_door_purple);
		dropSelf(TFBlocks.castle_door_yellow);
		dropSelf(TFBlocks.castle_door_pink);
		dropSelf(TFBlocks.castle_door_blue);
		dropSelf(TFBlocks.twilight_portal_miniature_structure);
		dropSelf(TFBlocks.naga_courtyard_miniature_structure);
		dropSelf(TFBlocks.lich_tower_miniature_structure);
		dropSelf(TFBlocks.knightmetal_block);
		dropSelf(TFBlocks.ironwood_block);
		dropSelf(TFBlocks.fiery_block);
		dropSelf(TFBlocks.steeleaf_block);
		dropSelf(TFBlocks.arctic_fur_block);
		dropSelf(TFBlocks.carminite_block);
		dropSelf(TFBlocks.maze_stone);
		dropSelf(TFBlocks.maze_stone_brick);
		dropSelf(TFBlocks.maze_stone_chiseled);
		dropSelf(TFBlocks.maze_stone_decorative);
		dropSelf(TFBlocks.maze_stone_cracked);
		dropSelf(TFBlocks.maze_stone_mossy);
		dropSelf(TFBlocks.maze_stone_mosaic);
		dropSelf(TFBlocks.maze_stone_border);
		dropWhenSilkTouch(TFBlocks.hedge);
		add(TFBlocks.root, createSingleItemTableWithSilkTouch(TFBlocks.root, Items.STICK, UniformGenerator.between(3, 5)));
		add(TFBlocks.liveroot_block, createSilkTouchDispatchTable(TFBlocks.liveroot_block, applyExplosionCondition(TFBlocks.liveroot_block, LootItem.lootTableItem(TFItems.liveroot).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
		dropSelf(TFBlocks.uncrafting_table);
		dropSelf(TFBlocks.firefly_jar);
		add(TFBlocks.firefly_spawner, particleSpawner());
		dropSelf(TFBlocks.cicada_jar);
		add(TFBlocks.moss_patch, createShearsOnlyDrop(TFBlocks.moss_patch));
		add(TFBlocks.mayapple, createShearsOnlyDrop(TFBlocks.mayapple));
		add(TFBlocks.clover_patch, createShearsOnlyDrop(TFBlocks.clover_patch));
		add(TFBlocks.fiddlehead, createShearsOnlyDrop(TFBlocks.fiddlehead));
		dropSelf(TFBlocks.mushgloom);
		add(TFBlocks.torchberry_plant, createShearsDispatchTable(TFBlocks.torchberry_plant, LootItem.lootTableItem(TFItems.torchberries)));
		add(TFBlocks.root_strand, createShearsOnlyDrop(TFBlocks.root_strand));
		add(TFBlocks.fallen_leaves, createShearsOnlyDrop(TFBlocks.fallen_leaves));
		dropSelf(TFBlocks.smoker);
		dropSelf(TFBlocks.encased_smoker);
		dropSelf(TFBlocks.fire_jet);
		dropSelf(TFBlocks.encased_fire_jet);
		dropSelf(TFBlocks.naga_stone_head);
		dropSelf(TFBlocks.naga_stone);
		dropSelf(TFBlocks.spiral_bricks);
		dropSelf(TFBlocks.nagastone_pillar);
		dropSelf(TFBlocks.nagastone_pillar_mossy);
		dropSelf(TFBlocks.nagastone_pillar_weathered);
		dropSelf(TFBlocks.etched_nagastone);
		dropSelf(TFBlocks.etched_nagastone_mossy);
		dropSelf(TFBlocks.etched_nagastone_weathered);
		dropSelf(TFBlocks.nagastone_stairs_left);
		dropSelf(TFBlocks.nagastone_stairs_right);
		dropSelf(TFBlocks.nagastone_stairs_mossy_left);
		dropSelf(TFBlocks.nagastone_stairs_mossy_right);
		dropSelf(TFBlocks.nagastone_stairs_weathered_left);
		dropSelf(TFBlocks.nagastone_stairs_weathered_right);
		add(TFBlocks.naga_trophy, createSingleItemTable(TFBlocks.naga_trophy.asItem()));
		add(TFBlocks.naga_wall_trophy, createSingleItemTable(TFBlocks.naga_trophy.asItem()));
		add(TFBlocks.lich_trophy, createSingleItemTable(TFBlocks.lich_trophy.asItem()));
		add(TFBlocks.lich_wall_trophy, createSingleItemTable(TFBlocks.lich_trophy.asItem()));
		add(TFBlocks.minoshroom_trophy, createSingleItemTable(TFBlocks.minoshroom_trophy.asItem()));
		add(TFBlocks.minoshroom_wall_trophy, createSingleItemTable(TFBlocks.minoshroom_trophy.asItem()));
		add(TFBlocks.hydra_trophy, createSingleItemTable(TFBlocks.hydra_trophy.asItem()));
		add(TFBlocks.hydra_wall_trophy, createSingleItemTable(TFBlocks.hydra_trophy.asItem()));
		add(TFBlocks.knight_phantom_trophy, createSingleItemTable(TFBlocks.knight_phantom_trophy.asItem()));
		add(TFBlocks.knight_phantom_wall_trophy, createSingleItemTable(TFBlocks.knight_phantom_trophy.asItem()));
		add(TFBlocks.ur_ghast_trophy, createSingleItemTable(TFBlocks.ur_ghast_trophy.asItem()));
		add(TFBlocks.ur_ghast_wall_trophy, createSingleItemTable(TFBlocks.ur_ghast_trophy.asItem()));
		add(TFBlocks.yeti_trophy, createSingleItemTable(TFBlocks.yeti_trophy.asItem()));
		add(TFBlocks.yeti_wall_trophy, createSingleItemTable(TFBlocks.yeti_wall_trophy.asItem()));
		add(TFBlocks.snow_queen_trophy, createSingleItemTable(TFBlocks.snow_queen_trophy.asItem()));
		add(TFBlocks.snow_queen_wall_trophy, createSingleItemTable(TFBlocks.snow_queen_trophy.asItem()));
		add(TFBlocks.quest_ram_trophy, createSingleItemTable(TFBlocks.quest_ram_trophy.asItem()));
		add(TFBlocks.quest_ram_wall_trophy, createSingleItemTable(TFBlocks.quest_ram_trophy.asItem()));

		add(TFBlocks.zombie_skull_candle, dropWithoutSilk(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.zombie_wall_skull_candle, dropWithoutSilk(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.skeleton_skull_candle, dropWithoutSilk(Blocks.SKELETON_SKULL));
		add(TFBlocks.skeleton_wall_skull_candle, dropWithoutSilk(Blocks.SKELETON_SKULL));
		add(TFBlocks.wither_skele_skull_candle, dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.wither_skele_wall_skull_candle, dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.creeper_skull_candle, dropWithoutSilk(Blocks.CREEPER_HEAD));
		add(TFBlocks.creeper_wall_skull_candle, dropWithoutSilk(Blocks.CREEPER_HEAD));
		add(TFBlocks.player_skull_candle, dropWithoutSilk(Blocks.PLAYER_HEAD));
		add(TFBlocks.player_wall_skull_candle, dropWithoutSilk(Blocks.PLAYER_HEAD));

		dropSelf(TFBlocks.iron_ladder);
		dropSelf(TFBlocks.stone_twist);
		dropSelf(TFBlocks.stone_twist_thin);
		dropSelf(TFBlocks.stone_bold);
		registerEmpty(TFBlocks.tome_spawner);
		dropWhenSilkTouch(TFBlocks.empty_bookshelf);
		//registerDropSelfLootTable(TFBlocks.lapis_block);
		add(TFBlocks.keepsake_casket, casketInfo(TFBlocks.keepsake_casket));
		dropPottedContents(TFBlocks.potted_twilight_oak_sapling);
		dropPottedContents(TFBlocks.potted_canopy_sapling);
		dropPottedContents(TFBlocks.potted_mangrove_sapling);
		dropPottedContents(TFBlocks.potted_darkwood_sapling);
		dropPottedContents(TFBlocks.potted_hollow_oak_sapling);
		dropPottedContents(TFBlocks.potted_rainboak_sapling);
		dropPottedContents(TFBlocks.potted_time_sapling);
		dropPottedContents(TFBlocks.potted_trans_sapling);
		dropPottedContents(TFBlocks.potted_mine_sapling);
		dropPottedContents(TFBlocks.potted_sort_sapling);
		dropPottedContents(TFBlocks.potted_mayapple);
		dropPottedContents(TFBlocks.potted_fiddlehead);
		dropPottedContents(TFBlocks.potted_mushgloom);
		dropPottedContents(TFBlocks.potted_thorn);
		dropPottedContents(TFBlocks.potted_green_thorn);
		dropPottedContents(TFBlocks.potted_dead_thorn);

		dropSelf(TFBlocks.oak_log);
		dropSelf(TFBlocks.stripped_oak_log);
		dropSelf(TFBlocks.oak_wood);
		dropSelf(TFBlocks.stripped_oak_wood);
		dropSelf(TFBlocks.oak_sapling);
		add(TFBlocks.oak_leaves, createLeavesDrops(TFBlocks.oak_leaves, TFBlocks.oak_sapling, DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.rainboak_sapling);
		add(TFBlocks.rainboak_leaves, createLeavesDrops(TFBlocks.rainboak_leaves, TFBlocks.rainboak_sapling, RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.hollow_oak_sapling);
		dropSelf(TFBlocks.twilight_oak_planks);
		dropSelf(TFBlocks.twilight_oak_stairs);
		add(TFBlocks.twilight_oak_slab, createSlabItemTable(TFBlocks.twilight_oak_slab));
		dropSelf(TFBlocks.twilight_oak_button);
		dropSelf(TFBlocks.twilight_oak_fence);
		dropSelf(TFBlocks.twilight_oak_gate);
		dropSelf(TFBlocks.twilight_oak_plate);
		add(TFBlocks.twilight_oak_door, createSinglePropConditionTable(TFBlocks.twilight_oak_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.twilight_oak_trapdoor);
		add(TFBlocks.twilight_oak_sign, createSingleItemTable(TFBlocks.twilight_oak_sign.asItem()));
		add(TFBlocks.twilight_wall_sign, createSingleItemTable(TFBlocks.twilight_oak_sign.asItem()));

		dropSelf(TFBlocks.canopy_log);
		dropSelf(TFBlocks.stripped_canopy_log);
		dropSelf(TFBlocks.canopy_wood);
		dropSelf(TFBlocks.stripped_canopy_wood);
		dropSelf(TFBlocks.canopy_sapling);
		add(TFBlocks.canopy_leaves, createLeavesDrops(TFBlocks.canopy_leaves, TFBlocks.canopy_sapling, DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.canopy_planks);
		dropSelf(TFBlocks.canopy_stairs);
		add(TFBlocks.canopy_slab, createSlabItemTable(TFBlocks.canopy_slab));
		dropSelf(TFBlocks.canopy_button);
		dropSelf(TFBlocks.canopy_fence);
		dropSelf(TFBlocks.canopy_gate);
		dropSelf(TFBlocks.canopy_plate);
		add(TFBlocks.canopy_door, createSinglePropConditionTable(TFBlocks.canopy_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.canopy_trapdoor);
		add(TFBlocks.canopy_sign, createSingleItemTable(TFBlocks.canopy_sign.asItem()));
		add(TFBlocks.canopy_wall_sign, createSingleItemTable(TFBlocks.canopy_sign.asItem()));
		add(TFBlocks.canopy_bookshelf, createSingleItemTableWithSilkTouch(TFBlocks.canopy_bookshelf, Items.BOOK, ConstantValue.exactly(3.0F)));

		dropSelf(TFBlocks.mangrove_log);
		dropSelf(TFBlocks.stripped_mangrove_log);
		dropSelf(TFBlocks.mangrove_wood);
		dropSelf(TFBlocks.stripped_mangrove_wood);
		dropSelf(TFBlocks.mangrove_sapling);
		add(TFBlocks.mangrove_leaves, createLeavesDrops(TFBlocks.mangrove_leaves, TFBlocks.mangrove_sapling, DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.mangrove_planks);
		dropSelf(TFBlocks.mangrove_stairs);
		add(TFBlocks.mangrove_slab, createSlabItemTable(TFBlocks.mangrove_slab));
		dropSelf(TFBlocks.mangrove_button);
		dropSelf(TFBlocks.mangrove_fence);
		dropSelf(TFBlocks.mangrove_gate);
		dropSelf(TFBlocks.mangrove_plate);
		add(TFBlocks.mangrove_door, createSinglePropConditionTable(TFBlocks.mangrove_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.mangrove_trapdoor);
		add(TFBlocks.mangrove_sign, createSingleItemTable(TFBlocks.mangrove_sign.asItem()));
		add(TFBlocks.mangrove_wall_sign, createSingleItemTable(TFBlocks.mangrove_sign.asItem()));

		dropSelf(TFBlocks.dark_log);
		dropSelf(TFBlocks.stripped_dark_log);
		dropSelf(TFBlocks.dark_wood);
		dropSelf(TFBlocks.stripped_dark_wood);
		dropSelf(TFBlocks.darkwood_sapling);
		add(TFBlocks.dark_leaves, createLeavesDrops(TFBlocks.dark_leaves, TFBlocks.darkwood_sapling, RARE_SAPLING_DROP_RATES));
		add(TFBlocks.hardened_dark_leaves, createLeavesDrops(TFBlocks.dark_leaves, TFBlocks.darkwood_sapling, RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.dark_planks);
		dropSelf(TFBlocks.dark_stairs);
		add(TFBlocks.dark_slab, createSlabItemTable(TFBlocks.dark_slab));
		dropSelf(TFBlocks.dark_button);
		dropSelf(TFBlocks.dark_fence);
		dropSelf(TFBlocks.dark_gate);
		dropSelf(TFBlocks.dark_plate);
		add(TFBlocks.dark_door, createSinglePropConditionTable(TFBlocks.dark_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.dark_trapdoor);
		add(TFBlocks.darkwood_sign, createSingleItemTable(TFBlocks.darkwood_sign.asItem()));
		add(TFBlocks.darkwood_wall_sign, createSingleItemTable(TFBlocks.darkwood_sign.asItem()));

		dropSelf(TFBlocks.time_log);
		dropSelf(TFBlocks.stripped_time_log);
		dropSelf(TFBlocks.time_wood);
		dropSelf(TFBlocks.stripped_time_wood);
		dropOther(TFBlocks.time_log_core, TFBlocks.time_log);
		dropSelf(TFBlocks.time_sapling);
		registerLeavesNoSapling(TFBlocks.time_leaves);
		dropSelf(TFBlocks.time_planks);
		dropSelf(TFBlocks.time_stairs);
		add(TFBlocks.time_slab, createSlabItemTable(TFBlocks.time_slab));
		dropSelf(TFBlocks.time_button);
		dropSelf(TFBlocks.time_fence);
		dropSelf(TFBlocks.time_gate);
		dropSelf(TFBlocks.time_plate);
		add(TFBlocks.time_door, createSinglePropConditionTable(TFBlocks.time_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.time_trapdoor);
		add(TFBlocks.time_sign, createSingleItemTable(TFBlocks.time_sign.asItem()));
		add(TFBlocks.time_wall_sign, createSingleItemTable(TFBlocks.time_sign.asItem()));

		dropSelf(TFBlocks.transformation_log);
		dropSelf(TFBlocks.stripped_transformation_log);
		dropSelf(TFBlocks.transformation_wood);
		dropSelf(TFBlocks.stripped_transformation_wood);
		dropOther(TFBlocks.transformation_log_core, TFBlocks.transformation_log);
		dropSelf(TFBlocks.transformation_sapling);
		registerLeavesNoSapling(TFBlocks.transformation_leaves);
		dropSelf(TFBlocks.trans_planks);
		dropSelf(TFBlocks.trans_stairs);
		add(TFBlocks.trans_slab, createSlabItemTable(TFBlocks.trans_slab));
		dropSelf(TFBlocks.trans_button);
		dropSelf(TFBlocks.trans_fence);
		dropSelf(TFBlocks.trans_gate);
		dropSelf(TFBlocks.trans_plate);
		add(TFBlocks.trans_door, createSinglePropConditionTable(TFBlocks.trans_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.trans_trapdoor);
		add(TFBlocks.trans_sign, createSingleItemTable(TFBlocks.trans_sign.asItem()));
		add(TFBlocks.trans_wall_sign, createSingleItemTable(TFBlocks.trans_sign.asItem()));

		dropSelf(TFBlocks.mining_log);
		dropSelf(TFBlocks.stripped_mining_log);
		dropSelf(TFBlocks.mining_wood);
		dropSelf(TFBlocks.stripped_mining_wood);
		dropOther(TFBlocks.mining_log_core, TFBlocks.mining_log);
		dropSelf(TFBlocks.mining_sapling);
		registerLeavesNoSapling(TFBlocks.mining_leaves);
		dropSelf(TFBlocks.mine_planks);
		dropSelf(TFBlocks.mine_stairs);
		add(TFBlocks.mine_slab, createSlabItemTable(TFBlocks.mine_slab));
		dropSelf(TFBlocks.mine_button);
		dropSelf(TFBlocks.mine_fence);
		dropSelf(TFBlocks.mine_gate);
		dropSelf(TFBlocks.mine_plate);
		add(TFBlocks.mine_door, createSinglePropConditionTable(TFBlocks.mine_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.mine_trapdoor);
		add(TFBlocks.mine_sign, createSingleItemTable(TFBlocks.mine_sign.asItem()));
		add(TFBlocks.mine_wall_sign, createSingleItemTable(TFBlocks.mine_sign.asItem()));

		dropSelf(TFBlocks.sorting_log);
		dropSelf(TFBlocks.stripped_sorting_log);
		dropSelf(TFBlocks.sorting_wood);
		dropSelf(TFBlocks.stripped_sorting_wood);
		dropOther(TFBlocks.sorting_log_core, TFBlocks.sorting_log);
		dropSelf(TFBlocks.sorting_sapling);
		registerLeavesNoSapling(TFBlocks.sorting_leaves);
		dropSelf(TFBlocks.sort_planks);
		dropSelf(TFBlocks.sort_stairs);
		add(TFBlocks.sort_slab, createSlabItemTable(TFBlocks.sort_slab));
		dropSelf(TFBlocks.sort_button);
		dropSelf(TFBlocks.sort_fence);
		dropSelf(TFBlocks.sort_gate);
		dropSelf(TFBlocks.sort_plate);
		add(TFBlocks.sort_door, createSinglePropConditionTable(TFBlocks.sort_door, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.sort_trapdoor);
		add(TFBlocks.sort_sign, createSingleItemTable(TFBlocks.sort_sign.asItem()));
		add(TFBlocks.sort_wall_sign, createSingleItemTable(TFBlocks.sort_sign.asItem()));


	}

	private void registerLeavesNoSapling(Block leaves) {
		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
						.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
	}


	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	private static LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
		LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = BlockLoot.HAS_NO_SHEARS_OR_SILK_TOUCH;
		return createSilkTouchOrShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NOT_SILK_TOUCH_OR_SHEARS).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	private static LootTable.Builder casketInfo(Block block) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE)));
	}

	private static LootTable.Builder particleSpawner() {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(applyExplosionDecay(TFBlocks.firefly_spawner, LootItem.lootTableItem(TFBlocks.firefly_spawner))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFBlocks.firefly)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 2))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 3))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 4))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 5))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 6))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 7))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 8))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 9))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(9.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.firefly_spawner).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 10))))));
	}

	private static LootTable.Builder dropWithoutSilk(Block block) {
		LootItemCondition.Builder HAS_SILK_TOUCH = BlockLoot.HAS_SILK_TOUCH;
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(HAS_SILK_TOUCH.invert()).add(LootItem.lootTableItem(block)));
	}

	private void registerEmpty(Block b) {
		add(b, LootTable.lootTable());
	}

	//@Override
	protected Iterable<Block> getKnownBlocks() {
		// todo 1.15 once all blockitems are ported, change this to all TF blocks, so an error will be thrown if we're missing any tables
		return knownBlocks;
	}
}
