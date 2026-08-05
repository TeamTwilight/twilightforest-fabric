package twilightforest.data.tags;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
	public static final TagKey<Block> MAZESTONE = create("mazestone");
	public static final TagKey<Block> TOWERWOOD = create("towerwood");
	public static final TagKey<Block> CLOUDS = create("clouds");
	public static final TagKey<Block> DEADROCK = create("deadrock");
	public static final TagKey<Block> CASTLE_BLOCKS = create("castle_blocks");

	public static final TagKey<Block> TF_LOGS = create("logs");
	public static final TagKey<Block> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
	public static final TagKey<Block> CANOPY_LOGS = create("canopy_logs");
	public static final TagKey<Block> MANGROVE_LOGS = create("mangrove_logs");
	public static final TagKey<Block> DARKWOOD_LOGS = create("darkwood_logs");
	public static final TagKey<Block> TIME_LOGS = create("timewood_logs");
	public static final TagKey<Block> TRANSFORMATION_LOGS = create("transwood_logs");
	public static final TagKey<Block> MINING_LOGS = create("mining_logs");
	public static final TagKey<Block> SORTING_LOGS = create("sortwood_logs");

	public static final TagKey<Block> HOLLOW_LOGS = create("hollow_logs");
	public static final TagKey<Block> HOLLOW_LOGS_HORIZONTAL = create("hollow_logs_horizontal");
	public static final TagKey<Block> HOLLOW_LOGS_VERTICAL = create("hollow_logs_vertical");
	public static final TagKey<Block> HOLLOW_LOGS_CLIMBABLE = create("hollow_logs_climbable");

	public static final TagKey<Block> BANISTERS = create("banisters");
	public static final TagKey<Block> TF_CHESTS = create("chests");

	public static final TagKey<Block> PORTAL_EDGE = create("portal/edge");
	public static final TagKey<Block> PORTAL_POOL = create("portal/fluid");
	public static final TagKey<Block> PORTAL_DECO = create("portal/decoration");
	public static final TagKey<Block> GENERATED_PORTAL_DECO = create("portal/generated_decoration");

	public static final TagKey<Block> DARK_TOWER_ALLOWED_POTS = create("dark_tower_allowed_pots");
	public static final TagKey<Block> TROPHY_PEDESTAL_ACTIVATION_BLOCKS = create("trophy_pedestal_activation_blocks");
	public static final TagKey<Block> FIRE_JET_FUEL = create("fire_jet_fuel");
	public static final TagKey<Block> ICE_BOMB_REPLACEABLES = create("ice_bomb_replaceables");
	public static final TagKey<Block> MAZEBREAKER_ACCELERATED = create("mazebreaker_accelerated_mining");

	public static final TagKey<Block> COMMON_PROTECTIONS = create("common_protections");
	public static final TagKey<Block> ANNIHILATION_INCLUSIONS = create("annihilation_inclusions");
	public static final TagKey<Block> ANTIBUILDER_IGNORES = create("antibuilder_ignores");
	public static final TagKey<Block> CARMINITE_REACTOR_IMMUNE = create("carminite_reactor_immune");
	public static final TagKey<Block> CARMINITE_REACTOR_ORES = create("carminite_reactor_ores");
	public static final TagKey<Block> STRUCTURE_BANNED_INTERACTIONS = create("structure_banned_interactions");
	public static final TagKey<Block> PROGRESSION_ALLOW_BREAKING = create("progression_allow_breaking");
	public static final TagKey<Block> CANNOT_TROLL_CAVE_HOLLOW = create("cannot_troll_cave_hollow");

	public static final TagKey<Block> WORLDGEN_REPLACEABLES = create("worldgen_replaceables");
	public static final TagKey<Block> ROOT_TRACE_SKIP = create("tree_roots_skip");
	public static final TagKey<Block> SUPPORTS_STALAGMITES = create("supports_stalagmites");
	public static final TagKey<Block> CARVER_REPLACEABLES = create("carver_replaceables");
	public static final TagKey<Block> PLANTS_HANG_ON = create("plants_hang_on");
	public static final TagKey<Block> OREBERRY_BUSHES_SURVIVE = create("oreberry_bushes_survive");
	public static final TagKey<Block> TF_BERRY_BUSHES_SURVIVE = create("tf_berry_bushes_survive");
	public static final TagKey<Block> TF_BERRY_BUSHES_REPLACE = create("tf_berry_bushes_replace");
	public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_SURVIVE = create("dark_tower_berry_bushes_survive");
	public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_DIE = create("dark_tower_berry_bushes_die");

	public static final TagKey<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = create("ore_magnet/ore_safe_replace_block");
	public static final TagKey<Block> ORE_MAGNET_IGNORE = create("ore_magnet/ignored_ores");

	public static final TagKey<Block> ROOT_GROUND = makeCommonTag("ore_bearing_ground/root");
	public static final TagKey<Block> ROOT_ORES = makeCommonTag("ores_in_ground/root");

	public static final TagKey<Block> TIME_CORE_EXCLUDED = create("time_core_excluded");
	public static final TagKey<Block> MINING_CORE_EXCLUDED = create("mining_tree_excluded");
	public static final TagKey<Block> ORE_METER_TARGETABLE = create("ore_meter_targetable");

	public static final TagKey<Block> PENGUINS_SPAWNABLE_ON = create("penguins_spawnable_on");
	public static final TagKey<Block> GIANTS_SPAWNABLE_ON = create("giants_spawnable_on");
	public static final TagKey<Block> DRUID_PROJECTILE_REPLACEABLE = create("druid_projectile_replaceable");

	public static final TagKey<Block> STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur");
	public static final TagKey<Block> STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite");
	public static final TagKey<Block> STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery");
	public static final TagKey<Block> STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood");
	public static final TagKey<Block> STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal");
	public static final TagKey<Block> STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf");

	public static final TagKey<Block> INCORRECT_FOR_IRONWOOD_TOOL = create("incorrect_for_ironwood_tool");
	public static final TagKey<Block> INCORRECT_FOR_FIERY_TOOL = create("incorrect_for_fiery_tool");
	public static final TagKey<Block> INCORRECT_FOR_STEELEAF_TOOL = create("incorrect_for_steeleaf_tool");
	public static final TagKey<Block> INCORRECT_FOR_KNIGHTMETAL_TOOL = create("incorrect_for_knightmetal_tool");
	public static final TagKey<Block> INCORRECT_FOR_GIANT_TOOL = create("incorrect_for_giant_tool");
	public static final TagKey<Block> INCORRECT_FOR_ICE_TOOL = create("incorrect_for_ice_tool");
	public static final TagKey<Block> INCORRECT_FOR_GLASS_TOOL = create("incorrect_for_glass_tool");

	public static final TagKey<Block> MINEABLE_WITH_BLOCK_AND_CHAIN = create("mineable_with_block_and_chain");
	public static final TagKey<Block> BLOCK_AND_CHAIN_NEVER_BREAKS = create("block_and_chain_never_breaks");

	public static final TagKey<Block> SMALL_LAKES_DONT_REPLACE = create("small_lakes_dont_replace");
	public static final TagKey<Block> DRYING_RACKS = create("drying_racks");

	public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {

		getOrCreateTagBuilder(TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		getOrCreateTagBuilder(CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
		getOrCreateTagBuilder(MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		getOrCreateTagBuilder(DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
		getOrCreateTagBuilder(TIME_LOGS).add(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
		getOrCreateTagBuilder(TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		getOrCreateTagBuilder(MINING_LOGS).add(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
		getOrCreateTagBuilder(SORTING_LOGS).add(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());
		getOrCreateTagBuilder(TF_LOGS).addTags(TWILIGHT_OAK_LOGS, CANOPY_LOGS, MANGROVE_LOGS, DARKWOOD_LOGS, TIME_LOGS, TRANSFORMATION_LOGS, MINING_LOGS, SORTING_LOGS);
		getOrCreateTagBuilder(BlockTags.LOGS).addTag(TF_LOGS);
		getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(TF_LOGS);

		getOrCreateTagBuilder(BlockTags.SAPLINGS).add(TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.CANOPY_SAPLING.get(), TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.TIME_SAPLING.get(), TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.MINING_SAPLING.get(), TFBlocks.SORTING_SAPLING.get(), TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_SAPLING.get());
		getOrCreateTagBuilder(BlockTags.LEAVES).add(TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.TIME_LEAVES.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.SORTING_LEAVES.get(), TFBlocks.THORN_LEAVES.get(), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.HARDENED_DARK_LEAVES.get());

		getOrCreateTagBuilder(BlockTags.PLANKS).add(TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.CANOPY_PLANKS.get(), TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.DARK_PLANKS.get(), TFBlocks.TIME_PLANKS.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.MINING_PLANKS.get(), TFBlocks.SORTING_PLANKS.get()).addTag(TOWERWOOD);
		getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.SORTING_SLAB.get());
		getOrCreateTagBuilder(BlockTags.SLABS).add(TFBlocks.AURORA_SLAB.get());
		getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.SORTING_STAIRS.get());
		getOrCreateTagBuilder(BlockTags.STAIRS).add(TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());
		getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		getOrCreateTagBuilder(ConventionalBlockTags.WOODEN_FENCE_GATES).add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.SORTING_BUTTON.get());
		getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.SORTING_PLATE.get());

		getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.SORTING_TRAPDOOR.get());
		getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.SORTING_DOOR.get());

		getOrCreateTagBuilder(ConventionalBlockTags.WOODEN_CHESTS).add(TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.MANGROVE_CHEST.get(), TFBlocks.DARK_CHEST.get(), TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get())
			.add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get());

		getOrCreateTagBuilder(ConventionalBlockTags.TRAPPED_CHESTS).add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get());

		getOrCreateTagBuilder(BlockTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.get());

		getOrCreateTagBuilder(BANISTERS).add(
			TFBlocks.OAK_BANISTER.get(),
			TFBlocks.SPRUCE_BANISTER.get(),
			TFBlocks.BIRCH_BANISTER.get(),
			TFBlocks.JUNGLE_BANISTER.get(),
			TFBlocks.ACACIA_BANISTER.get(),
			TFBlocks.DARK_OAK_BANISTER.get(),
			TFBlocks.CRIMSON_BANISTER.get(),
			TFBlocks.WARPED_BANISTER.get(),
			TFBlocks.VANGROVE_BANISTER.get(),
			TFBlocks.BAMBOO_BANISTER.get(),
			TFBlocks.CHERRY_BANISTER.get(),

			TFBlocks.TWILIGHT_OAK_BANISTER.get(),
			TFBlocks.CANOPY_BANISTER.get(),
			TFBlocks.MANGROVE_BANISTER.get(),
			TFBlocks.DARK_BANISTER.get(),
			TFBlocks.TIME_BANISTER.get(),
			TFBlocks.TRANSFORMATION_BANISTER.get(),
			TFBlocks.MINING_BANISTER.get(),
			TFBlocks.SORTING_BANISTER.get()
		);

		getOrCreateTagBuilder(HOLLOW_LOGS_HORIZONTAL).add(
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(),
			TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()
		);

		getOrCreateTagBuilder(HOLLOW_LOGS_VERTICAL).add(
			TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(),
			TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(),
			TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get()
		);

		getOrCreateTagBuilder(HOLLOW_LOGS_CLIMBABLE).add(
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(),
			TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()
		);

		getOrCreateTagBuilder(HOLLOW_LOGS).addTags(HOLLOW_LOGS_HORIZONTAL, HOLLOW_LOGS_VERTICAL, HOLLOW_LOGS_CLIMBABLE);

		getOrCreateTagBuilder(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.get());
		getOrCreateTagBuilder(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.get());
		getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.get());
		getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES).add(
			TFBlocks.HARDENED_DARK_LEAVES.get(),
			TFBlocks.MAYAPPLE.get(),
			TFBlocks.FIDDLEHEAD.get(),
			TFBlocks.MOSS_PATCH.get(),
			TFBlocks.CLOVER_PATCH.get(),
			TFBlocks.MUSHGLOOM.get(),
			TFBlocks.FIREFLY.get(),
			TFBlocks.FALLEN_LEAVES.get(),
			TFBlocks.TORCHBERRY_PLANT.get(),
			TFBlocks.ROOT_STRAND.get(),
			TFBlocks.ROOT_BLOCK.get(),
			TFBlocks.RASPBERRY_BUSH.get(),
			TFBlocks.BLUEBERRY_BUSH.get(),
			TFBlocks.BLACKBERRY_BUSH.get(),
			TFBlocks.MALOBERRY_BUSH.get()
		);

		getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.get(), TFBlocks.ROPE.get(), TFBlocks.ROOT_STRAND.get()).addTag(HOLLOW_LOGS_CLIMBABLE);

		getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.CANOPY_SIGN.get(),
			TFBlocks.MANGROVE_SIGN.get(), TFBlocks.DARK_SIGN.get(),
			TFBlocks.TIME_SIGN.get(), TFBlocks.TRANSFORMATION_SIGN.get(),
			TFBlocks.MINING_SIGN.get(), TFBlocks.SORTING_SIGN.get());

		getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
			TFBlocks.MANGROVE_WALL_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(),
			TFBlocks.TIME_WALL_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
			TFBlocks.MINING_WALL_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get());

		getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.CANOPY_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.DARK_HANGING_SIGN.get(),
			TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_HANGING_SIGN.get(),
			TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.SORTING_HANGING_SIGN.get());

		getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(),
			TFBlocks.TIME_WALL_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(),
			TFBlocks.MINING_WALL_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get());

		getOrCreateTagBuilder(TOWERWOOD).add(TFBlocks.TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get());

		getOrCreateTagBuilder(MAZESTONE).add(
			TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get(),
			TFBlocks.CRACKED_MAZESTONE.get(), TFBlocks.MOSSY_MAZESTONE.get(),
			TFBlocks.CUT_MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get(),
			TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BORDER.get());

		getOrCreateTagBuilder(CASTLE_BLOCKS).add(
			TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get(),
			TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(),
			TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.THICK_CASTLE_BRICK.get(),
			TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(),
			TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(),
			TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
			TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get(),
			TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get()
		);

		getOrCreateTagBuilder(MAZEBREAKER_ACCELERATED).addTag(MAZESTONE).addTag(CASTLE_BLOCKS);

		getOrCreateTagBuilder(STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		getOrCreateTagBuilder(STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.get());
		getOrCreateTagBuilder(STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.get());
		getOrCreateTagBuilder(STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.get());
		getOrCreateTagBuilder(STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.get());
		getOrCreateTagBuilder(STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.get());

		getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS).addTags(STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_STEELEAF);

		getOrCreateTagBuilder(ConventionalBlockTags.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_STEELEAF);

		getOrCreateTagBuilder(BlockTags.DIRT).add(TFBlocks.UBEROUS_SOIL.get());
		getOrCreateTagBuilder(PORTAL_EDGE).add(Blocks.FARMLAND, Blocks.DIRT_PATH).addTags(BlockTags.DIRT);
		getOrCreateTagBuilder(PORTAL_POOL).add(Blocks.WATER);
		getOrCreateTagBuilder(PORTAL_DECO).add(
				Blocks.BAMBOO,
				Blocks.SHORT_GRASS, Blocks.TALL_GRASS,
				Blocks.FERN, Blocks.LARGE_FERN,
				Blocks.DEAD_BUSH,
				Blocks.SUGAR_CANE,
				Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
				Blocks.SWEET_BERRY_BUSH,
				Blocks.NETHER_WART,
				Blocks.COCOA,
				Blocks.VINE, Blocks.GLOW_LICHEN,
				Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
				Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS,
				Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM,
				Blocks.MOSS_CARPET,
				Blocks.PINK_PETALS,
				Blocks.BIG_DRIPLEAF,
				Blocks.BIG_DRIPLEAF_STEM,
				Blocks.SMALL_DRIPLEAF,
				TFBlocks.FIDDLEHEAD.get(),
				TFBlocks.MOSS_PATCH.get(),
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.CLOVER_PATCH.get(),
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.FALLEN_LEAVES.get(),
				TFBlocks.GIANT_LEAVES.get(),
				TFBlocks.STEELEAF_BLOCK.get(),
				TFBlocks.HARDENED_DARK_LEAVES.get())
			.addOptionalTag(BlockTags.FLOWERS.location())
			.addOptionalTag(BlockTags.LEAVES.location())
			.addOptionalTag(BlockTags.SAPLINGS.location())
			.addOptionalTag(BlockTags.CROPS.location());

		getOrCreateTagBuilder(GENERATED_PORTAL_DECO)
			.add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM,
				Blocks.SHORT_GRASS, Blocks.FERN,
				Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET,
				Blocks.LILY_OF_THE_VALLEY, Blocks.OXEYE_DAISY,
				Blocks.ALLIUM, Blocks.CORNFLOWER,
				Blocks.WHITE_TULIP, Blocks.PINK_TULIP,
				Blocks.ORANGE_TULIP, Blocks.RED_TULIP,
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.FIDDLEHEAD.get());

		getOrCreateTagBuilder(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.get());

		getOrCreateTagBuilder(TROPHY_PEDESTAL_ACTIVATION_BLOCKS).add(
			TFBlocks.NAGA_TROPHY.get(), TFBlocks.LICH_TROPHY.get(),
			TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.HYDRA_TROPHY.get(),
			TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.UR_GHAST_TROPHY.get(),
			TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.SNOW_QUEEN_TROPHY.get());

		getOrCreateTagBuilder(FIRE_JET_FUEL).add(Blocks.LAVA);

		getOrCreateTagBuilder(ICE_BOMB_REPLACEABLES)
			.add(TFBlocks.MAYAPPLE.get(), TFBlocks.FIDDLEHEAD.get(), Blocks.SHORT_GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN)
			.addOptionalTag(BlockTags.FLOWERS.location());

		getOrCreateTagBuilder(PLANTS_HANG_ON)
			.addTag(BlockTags.DIRT)
			.add(Blocks.MOSS_BLOCK, TFBlocks.MANGROVE_ROOT.get(), TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get());

		getOrCreateTagBuilder(OREBERRY_BUSHES_SURVIVE)
			.addOptionalTag(ConventionalBlockTags.STONES.location())
			.addOptionalTag(BlockTags.STONE_BRICKS.location())
			.addOptionalTag(ConventionalBlockTags.ORES_IN_GROUND_STONE.location())
			.addOptionalTag(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE.location())
			.addOptionalTag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK.location())
			.addOptionalTag(ConventionalBlockTags.COBBLESTONES.location())
			.addOptionalTag(ConventionalBlockTags.NETHERRACKS.location())
			.add(TFBlocks.GIANT_COBBLESTONE.getKey())
			.add(Blocks.POLISHED_ANDESITE.builtInRegistryHolder().key())
			.add(Blocks.POLISHED_DIORITE.builtInRegistryHolder().key())
			.add(Blocks.POLISHED_GRANITE.builtInRegistryHolder().key())
			.add(Blocks.SMOOTH_STONE.builtInRegistryHolder().key())
			.add(Blocks.INFESTED_CHISELED_STONE_BRICKS.builtInRegistryHolder().key())
			.add(Blocks.INFESTED_CRACKED_STONE_BRICKS.builtInRegistryHolder().key())
			.add(Blocks.INFESTED_MOSSY_STONE_BRICKS.builtInRegistryHolder().key())
			.add(Blocks.INFESTED_STONE_BRICKS.builtInRegistryHolder().key());

		getOrCreateTagBuilder(TF_BERRY_BUSHES_REPLACE)
			.addOptionalTag(BlockTags.REPLACEABLE.location())
			.addOptionalTag(BlockTags.FLOWERS.location())
			.add(TFBlocks.MAYAPPLE.getKey());

		getOrCreateTagBuilder(TF_BERRY_BUSHES_SURVIVE)
			.addOptionalTag(BlockTags.DIRT.location())
			.add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key());

		getOrCreateTagBuilder(DARK_TOWER_BERRY_BUSHES_SURVIVE)
			.add(Blocks.BLACKSTONE)
			.add(Blocks.SOUL_SAND)
			.add(Blocks.SOUL_SOIL)
			.addOptionalTag(ConventionalBlockTags.NETHERRACKS.location())
			.addOptionalTag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK.location());

		getOrCreateTagBuilder(DARK_TOWER_BERRY_BUSHES_DIE)
			.addOptionalTag(BlockTags.NYLIUM.location());

		getOrCreateTagBuilder(COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
			TFBlocks.NAGA_BOSS_SPAWNER.get(),
			TFBlocks.LICH_BOSS_SPAWNER.get(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(),
			TFBlocks.HYDRA_BOSS_SPAWNER.get(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(),
			TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(),
			TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
			TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get(),
			TFBlocks.LOCKED_VANISHING_BLOCK.get(),
			TFBlocks.PINK_FORCE_FIELD.get(),
			TFBlocks.ORANGE_FORCE_FIELD.get(),
			TFBlocks.GREEN_FORCE_FIELD.get(),
			TFBlocks.BLUE_FORCE_FIELD.get(),
			TFBlocks.VIOLET_FORCE_FIELD.get(),
			TFBlocks.SKULL_CHEST.get(),
			TFBlocks.KEEPSAKE_CASKET.get(),
			TFBlocks.TROPHY_PEDESTAL.get()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
			Blocks.BARRIER,
			Blocks.BEDROCK,
			Blocks.END_PORTAL,
			Blocks.END_PORTAL_FRAME,
			Blocks.END_GATEWAY,
			Blocks.COMMAND_BLOCK,
			Blocks.REPEATING_COMMAND_BLOCK,
			Blocks.CHAIN_COMMAND_BLOCK,
			Blocks.STRUCTURE_BLOCK,
			Blocks.JIGSAW,
			Blocks.MOVING_PISTON,
			Blocks.LIGHT,
			Blocks.REINFORCED_DEEPSLATE
		);

		getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.get(), TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		getOrCreateTagBuilder(CARMINITE_REACTOR_IMMUNE).addTag(COMMON_PROTECTIONS);

		getOrCreateTagBuilder(CARMINITE_REACTOR_ORES).add(Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE);

		getOrCreateTagBuilder(DEADROCK).add(TFBlocks.DEADROCK.get(), TFBlocks.CRACKED_DEADROCK.get(), TFBlocks.WEATHERED_DEADROCK.get());

		getOrCreateTagBuilder(ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
			.add(Blocks.NETHER_PORTAL)
			.addTag(DEADROCK)
			.add(TFBlocks.CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.WORN_CASTLE_BRICK.get())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_RUNE_BRICK.get())
			.add(TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(), TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(), TFBlocks.VIOLET_FORCE_FIELD.get())
			.add(TFBlocks.BROWN_THORNS.get(), TFBlocks.GREEN_THORNS.get());

		getOrCreateTagBuilder(ANTIBUILDER_IGNORES).add(
			Blocks.REDSTONE_LAMP,
			Blocks.TNT,
			Blocks.WATER,
			TFBlocks.ANTIBUILDER.get(),
			TFBlocks.CARMINITE_BUILDER.get(),
			TFBlocks.BUILT_BLOCK.get(),
			TFBlocks.REACTOR_DEBRIS.get(),
			TFBlocks.CARMINITE_REACTOR.get(),
			TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.GHAST_TRAP.get(),
			TFBlocks.FAKE_DIAMOND.get(),
			TFBlocks.FAKE_GOLD.get()
		).addTag(COMMON_PROTECTIONS).addOptional(ResourceLocation.parse("gravestone:gravestone"));

		getOrCreateTagBuilder(STRUCTURE_BANNED_INTERACTIONS).add(Blocks.LEVER).add(TFBlocks.ANTIBUILDER.get()).addOptionalTag(BlockTags.BUTTONS.location()).addOptionalTag(ConventionalBlockTags.CHESTS.location());

		// 添加更多坟墓模组到标签
		getOrCreateTagBuilder(PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.get())
			.add(TFBlocks.KEEPSAKE_CASKET.get())
			.addOptional(ResourceLocation.fromNamespaceAndPath("gravestone", "gravestone"));

		getOrCreateTagBuilder(CANNOT_TROLL_CAVE_HOLLOW)
			.add(Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.BROWN_MUSHROOM_BLOCK)
			.add(TFBlocks.HUGE_MUSHGLOOM.get());

		getOrCreateTagBuilder(ORE_MAGNET_SAFE_REPLACE_BLOCK)
			.addOptionalTag(BlockTags.DIRT.location())
			.addOptionalTag(ConventionalBlockTags.GRAVELS.location())
			.addOptionalTag(ConventionalBlockTags.SANDS.location())
			.addOptionalTag(BlockTags.NYLIUM.location())
			.addOptionalTag(BlockTags.BASE_STONE_OVERWORLD.location())
			.addOptionalTag(BlockTags.BASE_STONE_NETHER.location())
			.addOptionalTag(ConventionalBlockTags.END_STONES.location())
			.addOptionalTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES.location())
			.addOptionalTag(BlockTags.STONE_ORE_REPLACEABLES.location())
			.addTag(ROOT_GROUND);

		getOrCreateTagBuilder(ORE_MAGNET_IGNORE).addOptionalTag(BlockTags.COAL_ORES.location());
		getOrCreateTagBuilder(MINING_CORE_EXCLUDED).addOptionalTag(BlockTags.COAL_ORES.location());

		getOrCreateTagBuilder(ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.get());
		getOrCreateTagBuilder(ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.get());

		getOrCreateTagBuilder(CLOUDS).add(TFBlocks.FLUFFY_CLOUD.get(), TFBlocks.WISPY_CLOUD.get(), TFBlocks.RAINY_CLOUD.get(), TFBlocks.SNOWY_CLOUD.get());

		getOrCreateTagBuilder(TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.SORTING_CHEST.get());

		getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS).addTag(CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());

		getOrCreateTagBuilder(BlockTags.SMALL_DRIPLEAF_PLACEABLE).add(TFBlocks.UBEROUS_SOIL.get());

		getOrCreateTagBuilder(BlockTags.FEATURES_CANNOT_REPLACE).addTag(COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.MANGROVE_ROOT.get(), TFBlocks.SINISTER_SPAWNER.get());
		// For anything that permits replacement during Worldgen
		getOrCreateTagBuilder(WORLDGEN_REPLACEABLES)
			.addOptionalTag(BlockTags.LUSH_GROUND_REPLACEABLE.location())
			.addOptionalTag(BlockTags.REPLACEABLE_BY_TREES.location());

		getOrCreateTagBuilder(ROOT_TRACE_SKIP).addTag(BlockTags.LOGS).add(TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.MANGROVE_ROOT.get(), TFBlocks.TIME_WOOD.get()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);

		getOrCreateTagBuilder(DRUID_PROJECTILE_REPLACEABLE)
			.addOptionalTag(BlockTags.LEAVES.location())
			.addOptionalTag(BlockTags.LOGS.location())
			.addOptionalTag(BlockTags.PLANKS.location())
			.addOptionalTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES.location())
			.addOptionalTag(BlockTags.NETHER_CARVER_REPLACEABLES.location())
			.addOptionalTag(BlockTags.REPLACEABLE_BY_TREES.location())
			.addOptionalTag(BlockTags.LUSH_GROUND_REPLACEABLE.location())
			.addOptionalTag(BlockTags.SCULK_REPLACEABLE.location())
			.addOptionalTag(ConventionalBlockTags.ORES.location());

		getOrCreateTagBuilder(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(TFBlocks.TROLLSTEINN.get());

		getOrCreateTagBuilder(TIME_CORE_EXCLUDED).add(Blocks.NETHER_PORTAL);

		getOrCreateTagBuilder(ORE_METER_TARGETABLE)
			.add(Blocks.BUDDING_AMETHYST)
			.add(Blocks.CALCITE)
			.add(Blocks.SOUL_SAND)
			.add(Blocks.SOUL_SOIL)
			.addOptionalTag(ConventionalBlockTags.ORES.location())
			.addOptionalTag(BlockTags.BASE_STONE_OVERWORLD.location())
			.addOptionalTag(BlockTags.BASE_STONE_NETHER.location())
			.addOptionalTag(BlockTags.DIRT.location())
			.addOptionalTag(ConventionalBlockTags.SANDS.location())
			.addOptionalTag(ConventionalBlockTags.SANDSTONE_BLOCKS.location())
			.addOptionalTag(BlockTags.TERRACOTTA.location())
			.addOptionalTag(ConventionalBlockTags.GRAVELS.location())
			.addOptionalTag(BlockTags.NYLIUM.location())
			.addTag(ROOT_ORES);

		getOrCreateTagBuilder(PENGUINS_SPAWNABLE_ON).addOptionalTag(BlockTags.ICE.location());
		getOrCreateTagBuilder(GIANTS_SPAWNABLE_ON).addTag(CLOUDS);

		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE).add(
			TFBlocks.HEDGE.get(),
			TFBlocks.ROOT_BLOCK.get(),
			TFBlocks.LIVEROOT_BLOCK.get(),
			TFBlocks.MANGROVE_ROOT.get(),
			TFBlocks.UNCRAFTING_TABLE.get(),
			TFBlocks.ENCASED_SMOKER.get(),
			TFBlocks.ENCASED_FIRE_JET.get(),
			TFBlocks.TIME_LOG_CORE.get(),
			TFBlocks.TRANSFORMATION_LOG_CORE.get(),
			TFBlocks.MINING_LOG_CORE.get(),
			TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.VANISHING_BLOCK.get(),
			TFBlocks.ANTIBUILDER.get(),
			TFBlocks.CARMINITE_REACTOR.get(),
			TFBlocks.CARMINITE_BUILDER.get(),
			TFBlocks.GHAST_TRAP.get(),
			TFBlocks.HUGE_STALK.get(),
			TFBlocks.HUGE_MUSHGLOOM.get(),
			TFBlocks.HUGE_MUSHGLOOM_STEM.get(),
			TFBlocks.CINDER_LOG.get(),
			TFBlocks.CINDER_WOOD.get(),
			TFBlocks.IRONWOOD_BLOCK.get(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.get(),
			TFBlocks.CANOPY_BOOKSHELF.get(),
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.SORTING_CHEST.get(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get(),
			TFBlocks.HUGE_LILY_PAD.get(),
			TFBlocks.ENCASED_TOWERWOOD.get()
		).addTags(BANISTERS, HOLLOW_LOGS, TOWERWOOD, DRYING_RACKS);

		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE).add(
			//vanilla doesnt use the leaves tag
			TFBlocks.TWILIGHT_OAK_LEAVES.get(),
			TFBlocks.CANOPY_LEAVES.get(),
			TFBlocks.MANGROVE_LEAVES.get(),
			TFBlocks.DARK_LEAVES.get(),
			TFBlocks.RAINBOW_OAK_LEAVES.get(),
			TFBlocks.TIME_LEAVES.get(),
			TFBlocks.TRANSFORMATION_LEAVES.get(),
			TFBlocks.MINING_LEAVES.get(),
			TFBlocks.SORTING_LEAVES.get(),
			TFBlocks.THORN_LEAVES.get(),
			TFBlocks.THORN_ROSE.get(),
			TFBlocks.BEANSTALK_LEAVES.get(),
			TFBlocks.STEELEAF_BLOCK.get(),
			TFBlocks.ARCTIC_FUR_BLOCK.get()
		);

		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
			TFBlocks.NAGASTONE.get(),
			TFBlocks.NAGASTONE_HEAD.get(),
			TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.TROPHY_PEDESTAL.get(),
			TFBlocks.AURORA_PILLAR.get(),
			TFBlocks.AURORA_SLAB.get(),
			TFBlocks.UNDERBRICK.get(),
			TFBlocks.MOSSY_UNDERBRICK.get(),
			TFBlocks.CRACKED_UNDERBRICK.get(),
			TFBlocks.UNDERBRICK_FLOOR.get(),
			TFBlocks.TROLLSTEINN.get(),
			TFBlocks.GIANT_LEAVES.get(),
			TFBlocks.GIANT_OBSIDIAN.get(),
			TFBlocks.GIANT_COBBLESTONE.get(),
			TFBlocks.GIANT_LOG.get(),
			TFBlocks.CINDER_FURNACE.get(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.get(),
			//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.get(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
			TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(),
			TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(),
			//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.get(),
			//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.get(),
			TFBlocks.KNIGHTMETAL_BLOCK.get(),
			TFBlocks.IRONWOOD_BLOCK.get(),
			TFBlocks.FIERY_BLOCK.get(),
			TFBlocks.CARMINITE_BLOCK.get(),
			TFBlocks.SPIRAL_BRICKS.get(),
			TFBlocks.ETCHED_NAGASTONE.get(),
			TFBlocks.NAGASTONE_PILLAR.get(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.get(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.IRON_LADDER.get(),
			TFBlocks.CANDELABRA.get(),
			TFBlocks.TWISTED_STONE.get(),
			TFBlocks.TWISTED_STONE_PILLAR.get(),
			TFBlocks.SKULL_CHEST.get(),
			TFBlocks.KEEPSAKE_CASKET.get(),
			TFBlocks.BOLD_STONE_PILLAR.get(),
			TFBlocks.TERRORCOTTA_CURVES.value(),
			TFBlocks.TERRORCOTTA_LINES.value(),
			TFBlocks.TERRORCOTTA_ARCS.value(),
			TFBlocks.SINISTER_SPAWNER.value()
		).addTags(MAZESTONE, CASTLE_BLOCKS, DEADROCK);

		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL).add(
			TFBlocks.SMOKER.get(),
			TFBlocks.FIRE_JET.get(),
			TFBlocks.UBEROUS_SOIL.get()
		);

		getOrCreateTagBuilder(Tags.Blocks.NEEDS_WOOD_TOOL).add(
			TFBlocks.NAGASTONE.get(),
			TFBlocks.NAGASTONE_HEAD.get(),
			TFBlocks.ETCHED_NAGASTONE.get(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.get(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
			TFBlocks.NAGASTONE_PILLAR.get(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.get(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.get(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
			TFBlocks.SPIRAL_BRICKS.get(),
			TFBlocks.TWISTED_STONE.get(),
			TFBlocks.TWISTED_STONE_PILLAR.get(),
			TFBlocks.BOLD_STONE_PILLAR.get(),
			TFBlocks.TERRORCOTTA_CURVES.value(),
			TFBlocks.TERRORCOTTA_LINES.value(),
			TFBlocks.TERRORCOTTA_ARCS.value(),
			TFBlocks.AURORA_PILLAR.get(),
			TFBlocks.AURORA_SLAB.get(),
			TFBlocks.TROLLSTEINN.get()
		);

		getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).add(
			TFBlocks.UNDERBRICK.get(),
			TFBlocks.CRACKED_UNDERBRICK.get(),
			TFBlocks.MOSSY_UNDERBRICK.get(),
			TFBlocks.UNDERBRICK_FLOOR.get(),
			TFBlocks.IRON_LADDER.get()
		);

		getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(
			TFBlocks.FIERY_BLOCK.get(),
			TFBlocks.KNIGHTMETAL_BLOCK.get()
		);

		getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(TFBlocks.AURORA_BLOCK.get()).addTags(CASTLE_BLOCKS, MAZESTONE, DEADROCK);

		getOrCreateTagBuilder(BlockTags.MUSHROOM_GROW_BLOCK).add(TFBlocks.UBEROUS_SOIL.get());

		getOrCreateTagBuilder(BlockTags.MOSS_REPLACEABLE).add(TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.TROLLSTEINN.get());

		getOrCreateTagBuilder(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.get());

		getOrCreateTagBuilder(ConventionalBlockTags.RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.get(), TFBlocks.STRONGHOLD_SHIELD.get(),
			TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get(),
			TFBlocks.MINING_LOG_CORE.get(),
			TFBlocks.ANTIBUILDER.get(), TFBlocks.BUILT_BLOCK.get(),
			TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get(),
			TFBlocks.REACTOR_DEBRIS.get(), TFBlocks.LOCKED_VANISHING_BLOCK.get(), TFBlocks.VANISHING_BLOCK.get(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get(), TFBlocks.REAPPEARING_BLOCK.get(),
			TFBlocks.BEANSTALK_GROWER.get(), TFBlocks.GIANT_COBBLESTONE.get(),
			TFBlocks.GIANT_LOG.get(), TFBlocks.GIANT_LEAVES.get(),
			TFBlocks.GIANT_OBSIDIAN.get(), TFBlocks.BROWN_THORNS.get(),
			TFBlocks.GREEN_THORNS.get(), TFBlocks.BURNT_THORNS.get(),
			TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(),
			TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(),
			TFBlocks.VIOLET_FORCE_FIELD.get(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
			TFBlocks.NAGA_BOSS_SPAWNER.get(), TFBlocks.LICH_BOSS_SPAWNER.get(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), TFBlocks.HYDRA_BOSS_SPAWNER.get(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get());

		getOrCreateTagBuilder(SUPPORTS_STALAGMITES).addTag(DEADROCK).add(Blocks.PACKED_ICE);

		getOrCreateTagBuilder(CARVER_REPLACEABLES).addTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(Blocks.SNOW_BLOCK);

		getOrCreateTagBuilder(INCORRECT_FOR_IRONWOOD_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_IRON_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_FIERY_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_STEELEAF_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_KNIGHTMETAL_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_GIANT_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_STONE_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_ICE_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL.location());
		getOrCreateTagBuilder(INCORRECT_FOR_GLASS_TOOL).addOptionalTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL.location());

		getOrCreateTagBuilder(ConventionalBlockTags.GLASS_BLOCKS).add(TFBlocks.AURORALIZED_GLASS.get());
		getOrCreateTagBuilder(ConventionalBlockTags.PLAYER_WORKSTATIONS_CRAFTING_TABLES).add(TFBlocks.UNCRAFTING_TABLE.get());
		getOrCreateTagBuilder(ConventionalBlockTags.ROPES).add(TFBlocks.ROPE.get());

		getOrCreateTagBuilder(MINEABLE_WITH_BLOCK_AND_CHAIN).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE,
			BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);

		getOrCreateTagBuilder(BLOCK_AND_CHAIN_NEVER_BREAKS).addTags(MAZESTONE, CASTLE_BLOCKS, DEADROCK, BlockTags.WITHER_IMMUNE)
			.add(TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(), TFBlocks.MINING_LOG_CORE.getKey())
			.add(TFBlocks.SORTING_LOG_CORE.getKey())
			.add(TFBlocks.GIANT_OBSIDIAN.getKey());

		getOrCreateTagBuilder(SMALL_LAKES_DONT_REPLACE).addTags(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LOGS, BlockTags.LEAVES)
			.add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), Blocks.MUSHROOM_STEM.builtInRegistryHolder().key());

		getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
			.add(TFBlocks.HUGE_LILY_PAD.get());

		getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
			.add(TFBlocks.HUGE_LILY_PAD.get());

		getOrCreateTagBuilder(ConventionalBlockTags.BOOKSHELVES)
			.add(TFBlocks.CANOPY_BOOKSHELF.get());

		getOrCreateTagBuilder(BlockTags.WOOL_CARPETS)
			.add(TFBlocks.CORONATION_CARPET.get());

		getOrCreateTagBuilder(BlockTags.FIRE)
			.add(TFBlocks.OMINOUS_FIRE.get());

		getOrCreateTagBuilder(DRYING_RACKS).add(
			TFBlocks.OAK_DRYING_RACK.get(), TFBlocks.SPRUCE_DRYING_RACK.get(),
			TFBlocks.BIRCH_DRYING_RACK.get(), TFBlocks.JUNGLE_DRYING_RACK.get(),
			TFBlocks.ACACIA_DRYING_RACK.get(), TFBlocks.DARK_OAK_DRYING_RACK.get(),
			TFBlocks.CRIMSON_DRYING_RACK.get(), TFBlocks.WARPED_DRYING_RACK.get(),
			TFBlocks.VANGROVE_DRYING_RACK.get(), TFBlocks.BAMBOO_DRYING_RACK.get(),
			TFBlocks.CHERRY_DRYING_RACK.get(),
			TFBlocks.TWILIGHT_OAK_DRYING_RACK.get(), TFBlocks.CANOPY_DRYING_RACK.get(),
			TFBlocks.MANGROVE_DRYING_RACK.get(), TFBlocks.DARK_DRYING_RACK.get(),
			TFBlocks.TIME_DRYING_RACK.get(), TFBlocks.TRANSFORMATION_DRYING_RACK.get(),
			TFBlocks.MINING_DRYING_RACK.get(), TFBlocks.SORTING_DRYING_RACK.get()
		);

		getOrCreateTagBuilder(ConventionalBlockTags.STRIPPED_LOGS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(),
			TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(),
			TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(),
			TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get());

		getOrCreateTagBuilder(ConventionalBlockTags.STRIPPED_WOODS).add(
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get(),
			TFBlocks.STRIPPED_TIME_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(),
			TFBlocks.STRIPPED_MINING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());

		getOrCreateTagBuilder(ConventionalBlockTags.STORAGE_BLOCKS_SLIME).add(TFBlocks.MAZE_SLIME_BLOCK.get());
	}

	public static TagKey<Block> create(String tagName) {
		return TagKey.create(Registries.BLOCK, TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Block> makeCommonTag(String tagName) {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", tagName));
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}
