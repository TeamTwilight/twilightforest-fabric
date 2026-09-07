package twilightforest.datagen.data.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.block.*;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {
	// [VanillaCopy] of BlockLoot fields, just changed shears to work with modded ones
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

	public BlockLootTables(HolderLookup.Provider provider) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
	}

	@Override
	public void generate() {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		dropSelf(TFBlocks.TOWERWOOD);
		dropSelf(TFBlocks.ENCASED_TOWERWOOD);
		dropSelf(TFBlocks.CRACKED_TOWERWOOD);
		dropSelf(TFBlocks.MOSSY_TOWERWOOD);
		dropSelf(TFBlocks.CARMINITE_BUILDER);
		dropSelf(TFBlocks.GHAST_TRAP);
		dropSelf(TFBlocks.CARMINITE_REACTOR);
		dropSelf(TFBlocks.REAPPEARING_BLOCK);
		dropSelf(TFBlocks.VANISHING_BLOCK);
		dropSelf(TFBlocks.LOCKED_VANISHING_BLOCK);
		dropSelf(TFBlocks.FIREFLY);
		dropSelf(TFBlocks.CICADA);
		dropSelf(TFBlocks.MOONWORM);
		dropSelf(TFBlocks.TROPHY_PEDESTAL);
		dropSelf(TFBlocks.AURORA_BLOCK);
		dropSelf(TFBlocks.AURORA_PILLAR);
		add(TFBlocks.AURORA_SLAB, createSlabItemTable(TFBlocks.AURORA_SLAB));
		dropWhenSilkTouch(TFBlocks.AURORALIZED_GLASS);
		dropSelf(TFBlocks.UNDERBRICK);
		dropSelf(TFBlocks.CRACKED_UNDERBRICK);
		dropSelf(TFBlocks.MOSSY_UNDERBRICK);
		dropSelf(TFBlocks.UNDERBRICK_FLOOR);
		dropSelf(TFBlocks.THORN_ROSE);
		dropSelf(TFBlocks.DEADROCK);
		dropSelf(TFBlocks.CRACKED_DEADROCK);
		dropSelf(TFBlocks.WEATHERED_DEADROCK);
		dropSelf(TFBlocks.TROLLSTEINN);
		dropWhenSilkTouch(TFBlocks.WISPY_CLOUD);
		dropSelf(TFBlocks.FLUFFY_CLOUD);
		dropSelf(TFBlocks.RAINY_CLOUD);
		dropSelf(TFBlocks.SNOWY_CLOUD);
		dropSelf(TFBlocks.GIANT_COBBLESTONE);
		dropSelf(TFBlocks.GIANT_LOG);
		dropSelf(TFBlocks.GIANT_LEAVES);
		dropSelf(TFBlocks.GIANT_OBSIDIAN);
		add(TFBlocks.UBEROUS_SOIL, createSingleItemTable(Blocks.DIRT));
		dropSelf(TFBlocks.HUGE_STALK);
		add(TFBlocks.HUGE_MUSHGLOOM, createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM, TFBlocks.MUSHGLOOM));
		add(TFBlocks.HUGE_MUSHGLOOM_STEM, createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM_STEM, TFBlocks.MUSHGLOOM));
		add(TFBlocks.TROLLVIDR, createShearsOnlyDrop(TFBlocks.TROLLVIDR));
		add(TFBlocks.UNRIPE_TROLLBER, createShearsOnlyDrop(TFBlocks.UNRIPE_TROLLBER));
		add(TFBlocks.TROLLBER, createShearsDispatchTable(TFBlocks.TROLLBER, LootItem.lootTableItem(TFItems.TORCHBERRIES).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
		dropSelf(TFBlocks.HUGE_LILY_PAD);
		dropSelf(TFBlocks.HUGE_WATER_LILY);
		dropSelf(TFBlocks.CASTLE_BRICK);
		dropSelf(TFBlocks.WORN_CASTLE_BRICK);
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK);
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK);
		dropSelf(TFBlocks.THICK_CASTLE_BRICK);
		dropSelf(TFBlocks.CASTLE_ROOF_TILE);
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_TILE);
		dropSelf(TFBlocks.CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.WORN_CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		dropSelf(TFBlocks.VIOLET_CASTLE_RUNE_BRICK);
		dropSelf(TFBlocks.YELLOW_CASTLE_RUNE_BRICK);
		dropSelf(TFBlocks.PINK_CASTLE_RUNE_BRICK);
		dropSelf(TFBlocks.BLUE_CASTLE_RUNE_BRICK);
		dropSelf(TFBlocks.CINDER_FURNACE);
		dropSelf(TFBlocks.CINDER_LOG);
		dropSelf(TFBlocks.CINDER_WOOD);
		dropSelf(TFBlocks.VIOLET_CASTLE_DOOR);
		dropSelf(TFBlocks.YELLOW_CASTLE_DOOR);
		dropSelf(TFBlocks.PINK_CASTLE_DOOR);
		dropSelf(TFBlocks.BLUE_CASTLE_DOOR);
		dropSelf(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE);
		dropSelf(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE);
		dropSelf(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE);
		dropSelf(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE);
		dropSelf(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE);
		dropSelf(TFBlocks.KNIGHTMETAL_BLOCK);
		dropSelf(TFBlocks.IRONWOOD_BLOCK);
		dropSelf(TFBlocks.FIERY_BLOCK);
		dropSelf(TFBlocks.STEELEAF_BLOCK);
		dropSelf(TFBlocks.ARCTIC_FUR_BLOCK);
		dropSelf(TFBlocks.CARMINITE_BLOCK);
		dropSelf(TFBlocks.MAZESTONE);
		dropSelf(TFBlocks.MAZESTONE_BRICK);
		dropSelf(TFBlocks.CUT_MAZESTONE);
		dropSelf(TFBlocks.DECORATIVE_MAZESTONE);
		dropSelf(TFBlocks.CRACKED_MAZESTONE);
		dropSelf(TFBlocks.MOSSY_MAZESTONE);
		dropSelf(TFBlocks.MAZESTONE_MOSAIC);
		dropSelf(TFBlocks.MAZESTONE_BORDER);
		dropSelf(TFBlocks.MAZE_SLIME_BLOCK);
		add(TFBlocks.RED_THREAD, redThread());
		dropWhenSilkTouch(TFBlocks.HEDGE);
		add(TFBlocks.ROOT_BLOCK, createSingleItemTableWithSilkTouch(TFBlocks.ROOT_BLOCK, Items.STICK, UniformGenerator.between(3, 5)));
		add(TFBlocks.LIVEROOT_BLOCK, createSilkTouchDispatchTable(TFBlocks.LIVEROOT_BLOCK, applyExplosionCondition(TFBlocks.LIVEROOT_BLOCK, LootItem.lootTableItem(TFItems.LIVEROOT).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
		add(TFBlocks.MANGROVE_ROOT, createSingleItemTableWithSilkTouch(TFBlocks.MANGROVE_ROOT, Items.STICK, UniformGenerator.between(3, 5)));
		dropSelf(TFBlocks.UNCRAFTING_TABLE);
		add(TFBlocks.BRAZIER, block -> this.createSinglePropConditionTable(block, BrazierBlock.HALF, DoubleBlockHalf.LOWER));

		this.add(TFBlocks.MASON_JAR, LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.MASON_JAR,
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFBlocks.MASON_JAR)
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(DataComponents.CUSTOM_NAME)
									.include(DataComponents.CONTAINER)
									.include(DataComponents.LOCK)
									.include(DataComponents.CONTAINER_LOOT)
									.include(TFDataComponents.JAR_LID)
							)
					)
			)
		));

		this.add(TFBlocks.FIREFLY_JAR, LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.FIREFLY_JAR,
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFBlocks.FIREFLY_JAR)
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(TFDataComponents.JAR_LID)
							)
					)
			)
		));

		this.add(TFBlocks.CICADA_JAR, LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.CICADA_JAR,
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFBlocks.CICADA_JAR)
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(TFDataComponents.JAR_LID)
							)
					)
			)
		));

		add(TFBlocks.FIREFLY_SPAWNER, particleSpawner());
		add(TFBlocks.MOSS_PATCH, createShearsOnlyDrop(TFBlocks.MOSS_PATCH));
		add(TFBlocks.MAYAPPLE, createShearsOnlyDrop(TFBlocks.MAYAPPLE));
		addTFBush(TFBlocks.IRON_OREBERRY_BUSH, TFLootTables.IRON_OREBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.GOLD_OREBERRY_BUSH, TFLootTables.GOLD_OREBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.COPPER_OREBERRY_BUSH, TFLootTables.COPPER_OREBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.ESSENCE_OREBERRY_BUSH, TFLootTables.ESSENCE_BERRY_BUSH_DROPS);
		addTFBush(TFBlocks.RASPBERRY_BUSH, TFLootTables.RASPBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.BLUEBERRY_BUSH, TFLootTables.BLUEBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.BLACKBERRY_BUSH, TFLootTables.BLACKBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.MALOBERRY_BUSH, TFLootTables.MALOBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.BLIGHTBERRY_BUSH, TFLootTables.BLIGHTBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.DUSKBERRY_BUSH, TFLootTables.DUSKBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.SKYBERRY_BUSH, TFLootTables.SKYBERRY_BUSH_DROPS);
		addTFBush(TFBlocks.STINGBERRY_BUSH, TFLootTables.STINGBERRY_BUSH_DROPS);
		add(TFBlocks.CLOVER_PATCH, createShearsOnlyDrop(TFBlocks.CLOVER_PATCH));
		add(TFBlocks.FIDDLEHEAD, createShearsOnlyDrop(TFBlocks.FIDDLEHEAD));
		dropSelf(TFBlocks.MUSHGLOOM);
		add(TFBlocks.ROOT_STRAND, block -> createShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		dropSelf(TFBlocks.SMOKER);
		dropSelf(TFBlocks.ENCASED_SMOKER);
		dropSelf(TFBlocks.FIRE_JET);
		dropSelf(TFBlocks.ENCASED_FIRE_JET);
		dropSelf(TFBlocks.NAGASTONE_HEAD);
		dropSelf(TFBlocks.NAGASTONE);
		dropSelf(TFBlocks.SPIRAL_BRICKS);
		dropSelf(TFBlocks.NAGASTONE_PILLAR);
		dropSelf(TFBlocks.MOSSY_NAGASTONE_PILLAR);
		dropSelf(TFBlocks.CRACKED_NAGASTONE_PILLAR);
		dropSelf(TFBlocks.ETCHED_NAGASTONE);
		dropSelf(TFBlocks.MOSSY_ETCHED_NAGASTONE);
		dropSelf(TFBlocks.CRACKED_ETCHED_NAGASTONE);
		dropSelf(TFBlocks.NAGASTONE_STAIRS_LEFT);
		dropSelf(TFBlocks.NAGASTONE_STAIRS_RIGHT);
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);
		add(TFBlocks.NAGA_TROPHY, createSingleItemTable(TFBlocks.NAGA_TROPHY.asItem()));
		add(TFBlocks.NAGA_WALL_TROPHY, createSingleItemTable(TFBlocks.NAGA_TROPHY.asItem()));
		add(TFBlocks.LICH_TROPHY, createSingleItemTable(TFBlocks.LICH_TROPHY.asItem()));
		add(TFBlocks.LICH_WALL_TROPHY, createSingleItemTable(TFBlocks.LICH_TROPHY.asItem()));
		add(TFBlocks.MINOSHROOM_TROPHY, createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.asItem()));
		add(TFBlocks.MINOSHROOM_WALL_TROPHY, createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.asItem()));
		add(TFBlocks.HYDRA_TROPHY, createSingleItemTable(TFBlocks.HYDRA_TROPHY.asItem()));
		add(TFBlocks.HYDRA_WALL_TROPHY, createSingleItemTable(TFBlocks.HYDRA_TROPHY.asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_TROPHY, createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY, createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.asItem()));
		add(TFBlocks.UR_GHAST_TROPHY, createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.asItem()));
		add(TFBlocks.UR_GHAST_WALL_TROPHY, createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.asItem()));
		add(TFBlocks.ALPHA_YETI_TROPHY, createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.asItem()));
		add(TFBlocks.ALPHA_YETI_WALL_TROPHY, createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.asItem()));
		add(TFBlocks.SNOW_QUEEN_TROPHY, createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.asItem()));
		add(TFBlocks.SNOW_QUEEN_WALL_TROPHY, createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.asItem()));
		add(TFBlocks.QUEST_RAM_TROPHY, createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.asItem()));
		add(TFBlocks.QUEST_RAM_WALL_TROPHY, createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.asItem()));

		add(TFBlocks.ZOMBIE_SKULL_CANDLE, createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.SKELETON_SKULL_CANDLE, createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.SKELETON_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_SKULL_CANDLE, createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.CREEPER_SKULL_CANDLE, createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.CREEPER_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.PLAYER_SKULL_CANDLE, createSingleItemTable(Blocks.PLAYER_HEAD).apply(
			CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
				.include(DataComponents.PROFILE)
				.include(DataComponents.NOTE_BLOCK_SOUND)
				.include(DataComponents.CUSTOM_NAME)
		));
		add(TFBlocks.PLAYER_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.PLAYER_HEAD).apply(
			CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
				.include(DataComponents.PROFILE)
				.include(DataComponents.NOTE_BLOCK_SOUND)
				.include(DataComponents.CUSTOM_NAME)
		));
		add(TFBlocks.PIGLIN_SKULL_CANDLE, createSingleItemTable(Blocks.PIGLIN_HEAD));
		add(TFBlocks.PIGLIN_WALL_SKULL_CANDLE, createSingleItemTable(Blocks.PIGLIN_HEAD));

		dropSelf(TFBlocks.IRON_LADDER);
		add(TFBlocks.ROPE, this.rope());
		dropWhenSilkTouch(TFBlocks.CANOPY_WINDOW);
		dropWhenSilkTouch(TFBlocks.CANOPY_WINDOW_PANE);
		dropSelf(TFBlocks.TWISTED_STONE);
		dropSelf(TFBlocks.TWISTED_STONE_PILLAR);
		dropSelf(TFBlocks.BOLD_STONE_PILLAR);
		add(TFBlocks.SKULL_CHEST, skullChestInfo(TFBlocks.SKULL_CHEST));
		add(TFBlocks.KEEPSAKE_CASKET, casketInfo(TFBlocks.KEEPSAKE_CASKET));
		dropSelf(TFBlocks.CANDELABRA);
		dropSelf(TFBlocks.WROUGHT_IRON_FENCE);
		dropSelf(TFBlocks.TERRORCOTTA_ARCS);
		dropSelf(TFBlocks.TERRORCOTTA_CURVES);
		dropSelf(TFBlocks.TERRORCOTTA_LINES);
		dropSelf(TFBlocks.CORONATION_CARPET);

		dropPottedContents(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING);
		dropPottedContents(TFBlocks.POTTED_CANOPY_SAPLING);
		dropPottedContents(TFBlocks.POTTED_MANGROVE_SAPLING);
		dropPottedContents(TFBlocks.POTTED_DARKWOOD_SAPLING);
		dropPottedContents(TFBlocks.POTTED_HOLLOW_OAK_SAPLING);
		dropPottedContents(TFBlocks.POTTED_RAINBOW_OAK_SAPLING);
		dropPottedContents(TFBlocks.POTTED_TIME_SAPLING);
		dropPottedContents(TFBlocks.POTTED_TRANSFORMATION_SAPLING);
		dropPottedContents(TFBlocks.POTTED_MINING_SAPLING);
		dropPottedContents(TFBlocks.POTTED_SORTING_SAPLING);
		dropPottedContents(TFBlocks.POTTED_MAYAPPLE);
		dropPottedContents(TFBlocks.POTTED_FIDDLEHEAD);
		dropPottedContents(TFBlocks.POTTED_MUSHGLOOM);
		add(TFBlocks.POTTED_THORN, createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_GREEN_THORN, createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_DEAD_THORN, createSingleItemTable(Items.FLOWER_POT));

		dropSelf(TFBlocks.OAK_BANISTER);
		dropSelf(TFBlocks.SPRUCE_BANISTER);
		dropSelf(TFBlocks.BIRCH_BANISTER);
		dropSelf(TFBlocks.JUNGLE_BANISTER);
		dropSelf(TFBlocks.ACACIA_BANISTER);
		dropSelf(TFBlocks.DARK_OAK_BANISTER);
		dropSelf(TFBlocks.CRIMSON_BANISTER);
		dropSelf(TFBlocks.WARPED_BANISTER);
		dropSelf(TFBlocks.VANGROVE_BANISTER);
		dropSelf(TFBlocks.BAMBOO_BANISTER);
		dropSelf(TFBlocks.CHERRY_BANISTER);
		dropSelf(TFBlocks.PALE_OAK_BANISTER);

		dropSelf(TFBlocks.OAK_DRYING_RACK);
		dropSelf(TFBlocks.SPRUCE_DRYING_RACK);
		dropSelf(TFBlocks.BIRCH_DRYING_RACK);
		dropSelf(TFBlocks.JUNGLE_DRYING_RACK);
		dropSelf(TFBlocks.ACACIA_DRYING_RACK);
		dropSelf(TFBlocks.DARK_OAK_DRYING_RACK);
		dropSelf(TFBlocks.CRIMSON_DRYING_RACK);
		dropSelf(TFBlocks.WARPED_DRYING_RACK);
		dropSelf(TFBlocks.VANGROVE_DRYING_RACK);
		dropSelf(TFBlocks.BAMBOO_DRYING_RACK);
		dropSelf(TFBlocks.CHERRY_DRYING_RACK);
		dropSelf(TFBlocks.PALE_OAK_DRYING_RACK);

		add(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL));
		add(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL));
		add(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL));

		add(TFBlocks.HOLLOW_OAK_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_OAK_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL));
		add(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_DARK_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_DARK_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_TIME_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_TIME_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_MINING_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_MINING_LOG_VERTICAL));
		add(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, verticalHollowLog(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL));

		add(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE));
		add(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE));
		add(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, hollowLog(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE));


		dropSelf(TFBlocks.TWILIGHT_OAK_LOG);
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);
		dropSelf(TFBlocks.TWILIGHT_OAK_WOOD);
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD);
		dropSelf(TFBlocks.TWILIGHT_OAK_SAPLING);
		dropSelf(TFBlocks.RAINBOW_OAK_SAPLING);
		dropSelf(TFBlocks.HOLLOW_OAK_SAPLING);
		dropSelf(TFBlocks.TWILIGHT_OAK_PLANKS);
		dropSelf(TFBlocks.TWILIGHT_OAK_STAIRS);
		add(TFBlocks.TWILIGHT_OAK_SLAB, createSlabItemTable(TFBlocks.TWILIGHT_OAK_SLAB));
		dropSelf(TFBlocks.TWILIGHT_OAK_BUTTON);
		dropSelf(TFBlocks.TWILIGHT_OAK_FENCE);
		dropSelf(TFBlocks.TWILIGHT_OAK_GATE);
		dropSelf(TFBlocks.TWILIGHT_OAK_PLATE);
		add(TFBlocks.TWILIGHT_OAK_DOOR, createSinglePropConditionTable(TFBlocks.TWILIGHT_OAK_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TWILIGHT_OAK_TRAPDOOR);
		add(TFBlocks.TWILIGHT_OAK_SIGN, createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.asItem()));
		add(TFBlocks.TWILIGHT_WALL_SIGN, createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.asItem()));
		add(TFBlocks.TWILIGHT_OAK_HANGING_SIGN, createSingleItemTable(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.asItem()));
		add(TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BANISTER);
		dropSelf(TFBlocks.TWILIGHT_OAK_CHEST);
		dropSelf(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST);
		dropSelf(TFBlocks.TWILIGHT_OAK_DRYING_RACK);

		dropSelf(TFBlocks.CANOPY_LOG);
		dropSelf(TFBlocks.STRIPPED_CANOPY_LOG);
		dropSelf(TFBlocks.CANOPY_WOOD);
		dropSelf(TFBlocks.STRIPPED_CANOPY_WOOD);
		dropSelf(TFBlocks.CANOPY_SAPLING);
		dropSelf(TFBlocks.CANOPY_PLANKS);
		dropSelf(TFBlocks.CANOPY_STAIRS);
		add(TFBlocks.CANOPY_SLAB, createSlabItemTable(TFBlocks.CANOPY_SLAB));
		dropSelf(TFBlocks.CANOPY_BUTTON);
		dropSelf(TFBlocks.CANOPY_FENCE);
		dropSelf(TFBlocks.CANOPY_GATE);
		dropSelf(TFBlocks.CANOPY_PLATE);
		add(TFBlocks.CANOPY_DOOR, createSinglePropConditionTable(TFBlocks.CANOPY_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.CANOPY_TRAPDOOR);
		add(TFBlocks.CANOPY_SIGN, createSingleItemTable(TFBlocks.CANOPY_SIGN.asItem()));
		add(TFBlocks.CANOPY_WALL_SIGN, createSingleItemTable(TFBlocks.CANOPY_SIGN.asItem()));
		add(TFBlocks.CANOPY_HANGING_SIGN, createSingleItemTable(TFBlocks.CANOPY_HANGING_SIGN.asItem()));
		add(TFBlocks.CANOPY_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.CANOPY_HANGING_SIGN.asItem()));
		add(TFBlocks.CANOPY_BOOKSHELF, createSingleItemTableWithSilkTouch(TFBlocks.CANOPY_BOOKSHELF, Items.BOOK, ConstantValue.exactly(2.0F)));
		dropSelf(TFBlocks.CHISELED_CANOPY_BOOKSHELF);
		dropSelf(TFBlocks.CANOPY_BANISTER);
		dropSelf(TFBlocks.CANOPY_CHEST);
		dropSelf(TFBlocks.CANOPY_TRAPPED_CHEST);
		dropSelf(TFBlocks.CANOPY_DRYING_RACK);

		dropSelf(TFBlocks.MANGROVE_LOG);
		dropSelf(TFBlocks.STRIPPED_MANGROVE_LOG);
		dropSelf(TFBlocks.MANGROVE_WOOD);
		dropSelf(TFBlocks.STRIPPED_MANGROVE_WOOD);
		dropSelf(TFBlocks.MANGROVE_SAPLING);
		dropSelf(TFBlocks.MANGROVE_PLANKS);
		dropSelf(TFBlocks.MANGROVE_STAIRS);
		add(TFBlocks.MANGROVE_SLAB, createSlabItemTable(TFBlocks.MANGROVE_SLAB));
		dropSelf(TFBlocks.MANGROVE_BUTTON);
		dropSelf(TFBlocks.MANGROVE_FENCE);
		dropSelf(TFBlocks.MANGROVE_GATE);
		dropSelf(TFBlocks.MANGROVE_PLATE);
		add(TFBlocks.MANGROVE_DOOR, createSinglePropConditionTable(TFBlocks.MANGROVE_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MANGROVE_TRAPDOOR);
		add(TFBlocks.MANGROVE_SIGN, createSingleItemTable(TFBlocks.MANGROVE_SIGN.asItem()));
		add(TFBlocks.MANGROVE_WALL_SIGN, createSingleItemTable(TFBlocks.MANGROVE_SIGN.asItem()));
		add(TFBlocks.MANGROVE_HANGING_SIGN, createSingleItemTable(TFBlocks.MANGROVE_HANGING_SIGN.asItem()));
		add(TFBlocks.MANGROVE_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.MANGROVE_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.MANGROVE_BANISTER);
		dropSelf(TFBlocks.MANGROVE_CHEST);
		dropSelf(TFBlocks.MANGROVE_TRAPPED_CHEST);
		dropSelf(TFBlocks.MANGROVE_DRYING_RACK);

		dropSelf(TFBlocks.DARK_LOG);
		dropSelf(TFBlocks.STRIPPED_DARK_LOG);
		dropSelf(TFBlocks.DARK_WOOD);
		dropSelf(TFBlocks.STRIPPED_DARK_WOOD);
		dropSelf(TFBlocks.DARKWOOD_SAPLING);
		dropSelf(TFBlocks.DARK_PLANKS);
		dropSelf(TFBlocks.DARK_STAIRS);
		add(TFBlocks.DARK_SLAB, createSlabItemTable(TFBlocks.DARK_SLAB));
		dropSelf(TFBlocks.DARK_BUTTON);
		dropSelf(TFBlocks.DARK_FENCE);
		dropSelf(TFBlocks.DARK_GATE);
		dropSelf(TFBlocks.DARK_PLATE);
		add(TFBlocks.DARK_DOOR, createSinglePropConditionTable(TFBlocks.DARK_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.DARK_TRAPDOOR);
		add(TFBlocks.DARK_SIGN, createSingleItemTable(TFBlocks.DARK_SIGN.asItem()));
		add(TFBlocks.DARK_WALL_SIGN, createSingleItemTable(TFBlocks.DARK_SIGN.asItem()));
		add(TFBlocks.DARK_HANGING_SIGN, createSingleItemTable(TFBlocks.DARK_HANGING_SIGN.asItem()));
		add(TFBlocks.DARK_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.DARK_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.DARK_BANISTER);
		dropSelf(TFBlocks.DARK_CHEST);
		dropSelf(TFBlocks.DARK_TRAPPED_CHEST);
		dropSelf(TFBlocks.DARK_DRYING_RACK);

		dropSelf(TFBlocks.TIME_LOG);
		dropSelf(TFBlocks.STRIPPED_TIME_LOG);
		dropSelf(TFBlocks.TIME_WOOD);
		dropSelf(TFBlocks.STRIPPED_TIME_WOOD);
		dropOther(TFBlocks.TIME_LOG_CORE, TFBlocks.TIME_LOG);
		dropSelf(TFBlocks.TIME_SAPLING);
		registerLeavesNoSapling(TFBlocks.TIME_LEAVES, registrylookup);
		dropSelf(TFBlocks.TIME_PLANKS);
		dropSelf(TFBlocks.TIME_STAIRS);
		add(TFBlocks.TIME_SLAB, createSlabItemTable(TFBlocks.TIME_SLAB));
		dropSelf(TFBlocks.TIME_BUTTON);
		dropSelf(TFBlocks.TIME_FENCE);
		dropSelf(TFBlocks.TIME_GATE);
		dropSelf(TFBlocks.TIME_PLATE);
		add(TFBlocks.TIME_DOOR, createSinglePropConditionTable(TFBlocks.TIME_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TIME_TRAPDOOR);
		add(TFBlocks.TIME_SIGN, createSingleItemTable(TFBlocks.TIME_SIGN.asItem()));
		add(TFBlocks.TIME_WALL_SIGN, createSingleItemTable(TFBlocks.TIME_SIGN.asItem()));
		add(TFBlocks.TIME_HANGING_SIGN, createSingleItemTable(TFBlocks.TIME_HANGING_SIGN.asItem()));
		add(TFBlocks.TIME_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.TIME_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.TIME_BANISTER);
		dropSelf(TFBlocks.TIME_CHEST);
		dropSelf(TFBlocks.TIME_TRAPPED_CHEST);
		dropSelf(TFBlocks.TIME_DRYING_RACK);

		dropSelf(TFBlocks.TRANSFORMATION_LOG);
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		dropSelf(TFBlocks.TRANSFORMATION_WOOD);
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_WOOD);
		dropOther(TFBlocks.TRANSFORMATION_LOG_CORE, TFBlocks.TRANSFORMATION_LOG);
		dropSelf(TFBlocks.TRANSFORMATION_SAPLING);
		registerLeavesNoSapling(TFBlocks.TRANSFORMATION_LEAVES, registrylookup);
		dropSelf(TFBlocks.TRANSFORMATION_PLANKS);
		dropSelf(TFBlocks.TRANSFORMATION_STAIRS);
		add(TFBlocks.TRANSFORMATION_SLAB, createSlabItemTable(TFBlocks.TRANSFORMATION_SLAB));
		dropSelf(TFBlocks.TRANSFORMATION_BUTTON);
		dropSelf(TFBlocks.TRANSFORMATION_FENCE);
		dropSelf(TFBlocks.TRANSFORMATION_GATE);
		dropSelf(TFBlocks.TRANSFORMATION_PLATE);
		add(TFBlocks.TRANSFORMATION_DOOR, createSinglePropConditionTable(TFBlocks.TRANSFORMATION_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TRANSFORMATION_TRAPDOOR);
		add(TFBlocks.TRANSFORMATION_SIGN, createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.asItem()));
		add(TFBlocks.TRANSFORMATION_WALL_SIGN, createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.asItem()));
		add(TFBlocks.TRANSFORMATION_HANGING_SIGN, createSingleItemTable(TFBlocks.TRANSFORMATION_HANGING_SIGN.asItem()));
		add(TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.TRANSFORMATION_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.TRANSFORMATION_BANISTER);
		dropSelf(TFBlocks.TRANSFORMATION_CHEST);
		dropSelf(TFBlocks.TRANSFORMATION_TRAPPED_CHEST);
		dropSelf(TFBlocks.TRANSFORMATION_DRYING_RACK);

		dropSelf(TFBlocks.MINING_LOG);
		dropSelf(TFBlocks.STRIPPED_MINING_LOG);
		dropSelf(TFBlocks.MINING_WOOD);
		dropSelf(TFBlocks.STRIPPED_MINING_WOOD);
		dropOther(TFBlocks.MINING_LOG_CORE, TFBlocks.MINING_LOG);
		dropSelf(TFBlocks.MINING_SAPLING);
		registerLeavesNoSapling(TFBlocks.MINING_LEAVES, registrylookup);
		dropSelf(TFBlocks.MINING_PLANKS);
		dropSelf(TFBlocks.MINING_STAIRS);
		add(TFBlocks.MINING_SLAB, createSlabItemTable(TFBlocks.MINING_SLAB));
		dropSelf(TFBlocks.MINING_BUTTON);
		dropSelf(TFBlocks.MINING_FENCE);
		dropSelf(TFBlocks.MINING_GATE);
		dropSelf(TFBlocks.MINING_PLATE);
		add(TFBlocks.MINING_DOOR, createSinglePropConditionTable(TFBlocks.MINING_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MINING_TRAPDOOR);
		add(TFBlocks.MINING_SIGN, createSingleItemTable(TFBlocks.MINING_SIGN.asItem()));
		add(TFBlocks.MINING_WALL_SIGN, createSingleItemTable(TFBlocks.MINING_SIGN.asItem()));
		add(TFBlocks.MINING_HANGING_SIGN, createSingleItemTable(TFBlocks.MINING_HANGING_SIGN.asItem()));
		add(TFBlocks.MINING_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.MINING_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.MINING_BANISTER);
		dropSelf(TFBlocks.MINING_CHEST);
		dropSelf(TFBlocks.MINING_TRAPPED_CHEST);
		dropSelf(TFBlocks.MINING_DRYING_RACK);

		dropSelf(TFBlocks.SORTING_LOG);
		dropSelf(TFBlocks.STRIPPED_SORTING_LOG);
		dropSelf(TFBlocks.SORTING_WOOD);
		dropSelf(TFBlocks.STRIPPED_SORTING_WOOD);
		dropOther(TFBlocks.SORTING_LOG_CORE, TFBlocks.SORTING_LOG);
		dropSelf(TFBlocks.SORTING_SAPLING);
		registerLeavesNoSapling(TFBlocks.SORTING_LEAVES, registrylookup);
		dropSelf(TFBlocks.SORTING_PLANKS);
		dropSelf(TFBlocks.SORTING_STAIRS);
		add(TFBlocks.SORTING_SLAB, createSlabItemTable(TFBlocks.SORTING_SLAB));
		dropSelf(TFBlocks.SORTING_BUTTON);
		dropSelf(TFBlocks.SORTING_FENCE);
		dropSelf(TFBlocks.SORTING_GATE);
		dropSelf(TFBlocks.SORTING_PLATE);
		add(TFBlocks.SORTING_DOOR, createSinglePropConditionTable(TFBlocks.SORTING_DOOR, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.SORTING_TRAPDOOR);
		add(TFBlocks.SORTING_SIGN, createSingleItemTable(TFBlocks.SORTING_SIGN.asItem()));
		add(TFBlocks.SORTING_WALL_SIGN, createSingleItemTable(TFBlocks.SORTING_SIGN.asItem()));
		add(TFBlocks.SORTING_HANGING_SIGN, createSingleItemTable(TFBlocks.SORTING_HANGING_SIGN.asItem()));
		add(TFBlocks.SORTING_WALL_HANGING_SIGN, createSingleItemTable(TFBlocks.SORTING_HANGING_SIGN.asItem()));
		dropSelf(TFBlocks.SORTING_BANISTER);
		dropSelf(TFBlocks.SORTING_CHEST);
		dropSelf(TFBlocks.SORTING_TRAPPED_CHEST);
		dropSelf(TFBlocks.SORTING_DRYING_RACK);

		add(TFBlocks.OMINOUS_FIRE, noDrop());
		ominousCandle(TFBlocks.OMINOUS_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_WHITE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_ORANGE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_MAGENTA_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_YELLOW_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIME_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_PINK_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_GRAY_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_CYAN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_PURPLE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BLUE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BROWN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_GREEN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_RED_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BLACK_CANDLE);
	}

	private void registerLeavesNoSapling(Block leaves, HolderLookup.RegistryLookup<Enchantment> registrylookup) {
		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
			.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
			.when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
	}

	private LootTable.Builder hollowLog(Block log) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(log.asItem()).when(this.hasSilkTouch()).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.SHORT_GRASS).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.SNOWBALL).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.SNOW)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.VINE).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.VINE)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.LADDER)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.LADDER_WATERLOGGED)))));
	}

	private LootTable.Builder verticalHollowLog(Block log) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(log.asItem()).when(this.hasSilkTouch()).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
	}

	private void addTFBush(Block block, ResourceKey<LootTable> berry) {
		if (!(block instanceof TFBushBlock bush))
			throw new IllegalArgumentException(block + " is not a TFBushBlock");

		add(block,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.when(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(bush)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TFBushBlock.AGE, 3))
						)
						.add(NestedLootTable.lootTableReference(berry))
				)
				.withPool(
					LootPool.lootPool()
						.add(LootItem.lootTableItem(bush.asItem()))
				)
		);
	}

	private static LootTable.Builder skullChestInfo(Block block) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(block)
					.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME))));
	}

	private static LootTable.Builder casketInfo(Block block) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(block)
					.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME))
					.apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE))));
	}

	private LootTable.Builder particleSpawner() {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(this.applyExplosionDecay(TFBlocks.FIREFLY_SPAWNER, LootItem.lootTableItem(TFBlocks.FIREFLY_SPAWNER))))
			.withPool(LootPool.lootPool()
				.add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(FireflySpawnerBlock.RADIUS.getPossibleValues(), layer ->
					LootItem.lootTableItem(TFBlocks.FIREFLY)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER)
							.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FireflySpawnerBlock.RADIUS, layer)))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer - 1)))))));
	}

	protected LootTable.Builder redThread() {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.add(this.applyExplosionDecay(TFBlocks.RED_THREAD, LootItem.lootTableItem(TFBlocks.RED_THREAD)
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.EAST, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.WEST, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.NORTH, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.SOUTH, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.UP, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.DOWN, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
	}

	protected LootTable.Builder rope() {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.add(this.applyExplosionDecay(TFBlocks.ROPE, LootItem.lootTableItem(TFBlocks.ROPE)
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.X, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.Y, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE)
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.Z, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
	}

	protected void ominousCandle(OminousCandleBlock block) {
		this.add(block, LootTable.lootTable()
			.withPool(
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						this.applyExplosionDecay(
							block,
							LootItem.lootTableItem(block.candle)
								.apply(
									List.of(2, 3, 4),
									value -> SetItemCountFunction.setCount(ConstantValue.exactly((float) value))
										.when(
											LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
												.setProperties(
													StatePropertiesPredicate.Builder.properties().hasProperty(OminousCandleBlock.CANDLES, value)
												)
										)
								)
						)
					)
			));
	}
}
