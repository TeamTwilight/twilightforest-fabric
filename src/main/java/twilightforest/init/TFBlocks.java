package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import twilightforest.TwilightForestMod;
import twilightforest.block.CodexActiveBlock;
import twilightforest.block.CodexBlock;
import twilightforest.block.CodexCarminiteBuilderBlock;
import twilightforest.block.CodexCastleDoorBlock;
import twilightforest.block.CodexChestBlock;
import twilightforest.block.CodexDirectionalBlock;
import twilightforest.block.CodexFenceBlock;
import twilightforest.block.CodexFenceGateBlock;
import twilightforest.block.CodexHollowOakSaplingBlock;
import twilightforest.block.CodexSlabBlock;
import twilightforest.block.CodexTrapDoorBlock;
import twilightforest.block.CodexHugeLilyPadBlock;
import twilightforest.block.CodexLockedBlock;
import twilightforest.block.CodexNagastonePillarBlock;
import twilightforest.block.CodexLeavesBlock;
import twilightforest.block.CodexRotatedPillarBlock;
import twilightforest.block.CodexSaplingBlock;
import twilightforest.block.CodexReappearingBlock;
import twilightforest.block.CodexSnowLayerBlock;
import twilightforest.block.CodexSixWayBlock;
import twilightforest.block.CodexSpawnerBlock;
import twilightforest.block.AmbientJarBlock;
import twilightforest.block.MasonJarBlock;
import twilightforest.block.CodexStairBlock;
import twilightforest.block.CodexThornsBlock;
import twilightforest.block.CodexTorchberryPlantBlock;
import twilightforest.block.UncraftingTableBlock;
import twilightforest.item.TrophyItem;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.feature.trees.growers.TFTreeGrowers;

/**
 * Alias table — each TFBlocks.X.get() returns the vanilla equivalent that
 * stands in for that TF block on this server. Nothing here registers a new
 * block; the entries exist solely so source files copied from TF compile
 * unchanged, and so structure/feature code that does
 * {@code TFBlocks.MAZESTONE.get().defaultBlockState()} produces a sensible
 * vanilla state at runtime. To promote any of these to a real paired-client
 * custom block later, replace the {@code block(Blocks.X)} call with a real
 * {@code Registry.register(...)} call modeled on TFEntities#entity.
 */
public final class TFBlocks {
    public static final TFRegistryObject<Block> ALPHA_YETI_BOSS_SPAWNER = bossSpawnerBlock("alpha_yeti_boss_spawner", twilightforest.enums.BossVariant.ALPHA_YETI);
    public static final TFRegistryObject<Block> ANTIBUILDER = antibuilderBlock("antibuilder", Blocks.REINFORCED_DEEPSLATE);
    public static final TFRegistryObject<Block> AURORA_BLOCK = auroraBrickBlock("aurora_block", Blocks.BLUE_STAINED_GLASS);
    public static final TFRegistryObject<Block> AURORA_PILLAR = pillarBlock("aurora_pillar", Blocks.PURPUR_PILLAR);
    public static final TFRegistryObject<Block> BLIGHTBERRY_BUSH = berryBushBlock("blightberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> BLUE_FORCE_FIELD = forceFieldBlock("blue_force_field", Blocks.BLUE_STAINED_GLASS);
    public static final TFRegistryObject<Block> BOLD_CASTLE_BRICK_PILLAR = pillarBlock("bold_castle_brick_pillar", Blocks.POLISHED_BASALT);
    public static final TFRegistryObject<Block> BLUE_CASTLE_DOOR = castleDoorBlock("blue_castle_door", Blocks.BLUE_STAINED_GLASS_PANE);
    public static final TFRegistryObject<Block> BLUE_CASTLE_RUNE_BRICK = solidBlock("blue_castle_rune_brick", Blocks.BLUE_GLAZED_TERRACOTTA);
    public static final TFRegistryObject<Block> BROWN_THORNS = thornsBlock("brown_thorns", Blocks.BROWN_STAINED_GLASS);
    public static final TFRegistryObject<Block> CARMINITE_BUILDER = carminiteBuilderBlock("carminite_builder", Blocks.REDSTONE_BLOCK);
    public static final TFRegistryObject<Block> CARMINITE_REACTOR = carminiteReactorBlock("carminite_reactor");
    public static final TFRegistryObject<Block> CASTLE_BRICK = solidBlock("castle_brick", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> CASTLE_BRICK_STAIRS = stairBlock("castle_brick_stairs", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> CASTLE_ROOF_TILE = solidBlock("castle_roof_tile", Blocks.PURPUR_BLOCK);
    public static final TFRegistryObject<Block> CICADA = cicadaBlock("cicada");
    public static final TFRegistryObject<Block> CRACKED_CASTLE_BRICK = solidBlock("cracked_castle_brick", Blocks.CRACKED_STONE_BRICKS);
    public static final TFRegistryObject<Block> CRACKED_DEADROCK = solidBlock("cracked_deadrock", Blocks.CRACKED_DEEPSLATE_BRICKS);
    public static final TFRegistryObject<Block> CRACKED_ETCHED_NAGASTONE = etchedNagastoneBlock("cracked_etched_nagastone", Blocks.CRACKED_STONE_BRICKS);
    public static final TFRegistryObject<Block> CRACKED_MAZESTONE = solidBlock("cracked_mazestone", Blocks.CRACKED_STONE_BRICKS);
    public static final TFRegistryObject<Block> CRACKED_NAGASTONE_PILLAR = nagastonePillarBlock("cracked_nagastone_pillar", Blocks.CRACKED_STONE_BRICKS);
    public static final TFRegistryObject<Block> CRACKED_NAGASTONE_STAIRS_LEFT = stairBlock("cracked_nagastone_stairs_left", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> CRACKED_NAGASTONE_STAIRS_RIGHT = stairBlock("cracked_nagastone_stairs_right", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> CRACKED_TOWERWOOD = solidBlock("cracked_towerwood", Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> CRACKED_UNDERBRICK = solidBlock("cracked_underbrick", Blocks.CRACKED_DEEPSLATE_BRICKS);
    public static final TFRegistryObject<Block> CUT_MAZESTONE = solidBlock("cut_mazestone", Blocks.SMOOTH_STONE);
    public static final TFRegistryObject<Block> DARK_LOG = pillarBlock("dark_log", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> DEADROCK = solidBlock("deadrock", Blocks.DEEPSLATE_BRICKS);
    public static final TFRegistryObject<Block> DECORATIVE_MAZESTONE = solidBlock("decorative_mazestone", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> DUSKBERRY_BUSH = berryBushBlock("duskberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> ENCASED_TOWERWOOD = solidBlock("encased_towerwood", Blocks.DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> ETCHED_NAGASTONE = etchedNagastoneBlock("etched_nagastone", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> FIREFLY = fireflyBlock("firefly");
    public static final TFRegistryObject<Block> FLUFFY_CLOUD = cloudBlock("fluffy_cloud", Blocks.WHITE_WOOL, null);
    public static final TFRegistryObject<Block> GHAST_TRAP = ghastTrapBlock("ghast_trap");
    public static final TFRegistryObject<Block> GIANT_COBBLESTONE = giantBlock("giant_cobblestone", Blocks.COBBLESTONE);
    public static final TFRegistryObject<Block> GIANT_LEAVES = giantLeavesBlock("giant_leaves", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> GIANT_LOG = giantBlock("giant_log", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> GIANT_OBSIDIAN = giantBlock("giant_obsidian", Blocks.OBSIDIAN);
    public static final TFRegistryObject<Block> GREEN_FORCE_FIELD = forceFieldBlock("green_force_field", Blocks.GREEN_STAINED_GLASS);
    public static final TFRegistryObject<Block> GREEN_THORNS = thornsBlock("green_thorns", Blocks.GREEN_STAINED_GLASS);
    public static final TFRegistryObject<Block> HARDENED_DARK_LEAVES = hardenedDarkLeavesBlock("hardened_dark_leaves", Blocks.DARK_OAK_LEAVES);
    public static final TFRegistryObject<Block> HEDGE = hedgeBlock("hedge", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> HUGE_MUSHGLOOM = sixWayBlock("huge_mushgloom", Blocks.RED_MUSHROOM_BLOCK);
    public static final TFRegistryObject<Block> HYDRA_BOSS_SPAWNER = bossSpawnerBlock("hydra_boss_spawner", twilightforest.enums.BossVariant.HYDRA);
    public static final TFRegistryObject<Block> INFESTED_TOWERWOOD = infestedTowerwoodBlock("infested_towerwood", Blocks.INFESTED_STONE_BRICKS);
    public static final TFRegistryObject<Block> KNIGHT_PHANTOM_BOSS_SPAWNER = bossSpawnerBlock("knight_phantom_boss_spawner", twilightforest.enums.BossVariant.KNIGHT_PHANTOM);
    public static final TFRegistryObject<Block> LICH_BOSS_SPAWNER = bossSpawnerBlock("lich_boss_spawner", twilightforest.enums.BossVariant.LICH);
    public static final TFRegistryObject<Block> LOCKED_VANISHING_BLOCK = lockedVanishingBlock("locked_vanishing_block", Blocks.BARRIER);
    public static final TFRegistryObject<Block> MAZESTONE = solidBlock("mazestone", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> MAZESTONE_BORDER = solidBlock("mazestone_border", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> MAZESTONE_BRICK = solidBlock("mazestone_brick", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> MAZESTONE_MOSAIC = solidBlock("mazestone_mosaic", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> MINOSHROOM_BOSS_SPAWNER = bossSpawnerBlock("minoshroom_boss_spawner", twilightforest.enums.BossVariant.MINOSHROOM);
    public static final TFRegistryObject<Block> MOSS_PATCH = mossPatchBlock("moss_patch", Blocks.MOSS_BLOCK);
    public static final TFRegistryObject<Block> MOSSY_ETCHED_NAGASTONE = etchedNagastoneBlock("mossy_etched_nagastone", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> MOSSY_MAZESTONE = solidBlock("mossy_mazestone", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> MOSSY_NAGASTONE_PILLAR = nagastonePillarBlock("mossy_nagastone_pillar", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> MOSSY_NAGASTONE_STAIRS_LEFT = stairBlock("mossy_nagastone_stairs_left", Blocks.MOSSY_STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> MOSSY_NAGASTONE_STAIRS_RIGHT = stairBlock("mossy_nagastone_stairs_right", Blocks.MOSSY_STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> MOSSY_TOWERWOOD = solidBlock("mossy_towerwood", Blocks.MOSSY_COBBLESTONE);
    public static final TFRegistryObject<Block> MOSSY_UNDERBRICK = solidBlock("mossy_underbrick", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGA_BOSS_SPAWNER = bossSpawnerBlock("naga_boss_spawner", twilightforest.enums.BossVariant.NAGA);
    public static final TFRegistryObject<Block> ORANGE_FORCE_FIELD = forceFieldBlock("orange_force_field", Blocks.ORANGE_STAINED_GLASS);
    public static final TFRegistryObject<Block> PINK_FORCE_FIELD = forceFieldBlock("pink_force_field", Blocks.PINK_STAINED_GLASS);
    public static final TFRegistryObject<Block> PINK_CASTLE_DOOR = castleDoorBlock("pink_castle_door", Blocks.PINK_STAINED_GLASS_PANE);
    public static final TFRegistryObject<Block> PINK_CASTLE_RUNE_BRICK = solidBlock("pink_castle_rune_brick", Blocks.PINK_GLAZED_TERRACOTTA);
    public static final TFRegistryObject<Block> NAGASTONE_PILLAR = nagastonePillarBlock("nagastone_pillar", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGASTONE_STAIRS_LEFT = stairBlock("nagastone_stairs_left", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> NAGASTONE_STAIRS_RIGHT = stairBlock("nagastone_stairs_right", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> REAPPEARING_BLOCK = reappearingBlock("reappearing_block", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> ROOT_BLOCK = solidBlock("root_block", Blocks.ROOTED_DIRT);
    public static final TFRegistryObject<Block> ROOT_STRAND = rootStrandBlock("root_strand", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> SKYBERRY_BUSH = berryBushBlock("skyberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> SNOW_QUEEN_BOSS_SPAWNER = bossSpawnerBlock("snow_queen_boss_spawner", twilightforest.enums.BossVariant.SNOW_QUEEN);
    public static final TFRegistryObject<Block> STINGBERRY_BUSH = berryBushBlock("stingberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> STRONGHOLD_SHIELD = strongholdShieldBlock("stronghold_shield", Blocks.REINFORCED_DEEPSLATE);
    public static final TFRegistryObject<Block> THICK_CASTLE_BRICK = solidBlock("thick_castle_brick", Blocks.POLISHED_ANDESITE);
    public static final TFRegistryObject<Block> TOWERWOOD = solidBlock("towerwood", Blocks.DARK_OAK_PLANKS);
    public static final TFRegistryObject<Block> TROLLSTEINN = trollsteinnBlock("trollsteinn", Blocks.TUFF);
    public static final TFRegistryObject<Block> TROPHY_PEDESTAL = trophyPedestalBlock("trophy_pedestal", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_CHEST = chestBlock("twilight_oak_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_LEAVES = leavesBlock("twilight_oak_leaves", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_LOG = pillarBlock("twilight_oak_log", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_WOOD = pillarBlock("twilight_oak_wood", Blocks.OAK_WOOD);
    public static final TFRegistryObject<Block> UBEROUS_SOIL = uberousSoilBlock("uberous_soil", Blocks.ROOTED_DIRT);
    public static final TFRegistryObject<Block> UNBREAKABLE_VANISHING_BLOCK = vanishingBlock("unbreakable_vanishing_block", Blocks.BEDROCK);
    public static final TFRegistryObject<Block> UNDERBRICK = solidBlock("underbrick", Blocks.DEEPSLATE_BRICKS);
    public static final TFRegistryObject<Block> UR_GHAST_BOSS_SPAWNER = bossSpawnerBlock("ur_ghast_boss_spawner", twilightforest.enums.BossVariant.UR_GHAST);
    public static final TFRegistryObject<Block> VIOLET_CASTLE_DOOR = castleDoorBlock("violet_castle_door", Blocks.PURPLE_STAINED_GLASS_PANE);
    public static final TFRegistryObject<Block> VIOLET_CASTLE_RUNE_BRICK = solidBlock("violet_castle_rune_brick", Blocks.PURPLE_GLAZED_TERRACOTTA);
    public static final TFRegistryObject<Block> VIOLET_FORCE_FIELD = forceFieldBlock("violet_force_field", Blocks.PURPLE_STAINED_GLASS);
    public static final TFRegistryObject<Block> CANOPY_FENCE = fenceBlock("canopy_fence", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> CANOPY_SAPLING = saplingBlock("canopy_sapling", Blocks.DARK_OAK_SAPLING, TFTreeGrowers.CANOPY);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_SAPLING = saplingBlock("twilight_oak_sapling", Blocks.OAK_SAPLING, TFTreeGrowers.TWILIGHT_OAK);
    public static final TFRegistryObject<Block> SNOWY_OAK_SAPLING = saplingBlock("snowy_oak_sapling", Blocks.OAK_SAPLING, TreeGrower.OAK);
    public static final TFRegistryObject<Block> RAINBOW_OAK_SAPLING = saplingBlock("rainbow_oak_sapling", Blocks.OAK_SAPLING, TFTreeGrowers.RAINBOW_OAK);
    public static final TFRegistryObject<Block> DARKWOOD_SAPLING = saplingBlock("darkwood_sapling", Blocks.DARK_OAK_SAPLING, TFTreeGrowers.DARK);
    public static final TFRegistryObject<Block> FALLEN_LEAVES = fallenLeavesBlock("fallen_leaves", Blocks.SNOW);
    public static final TFRegistryObject<Block> HOLLOW_LOG_HORIZONTAL = pillarBlock("hollow_log_horizontal", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HUGE_LILY_PAD = hugeLilyPadBlock("huge_lily_pad", Blocks.LILY_PAD);
    public static final TFRegistryObject<Block> HUGE_WATER_LILY = hugeWaterLilyBlock("huge_water_lily", Blocks.LILY_PAD);
    public static final TFRegistryObject<Block> MAYAPPLE = mayappleBlock("mayapple", Blocks.SHORT_GRASS);
    public static final TFRegistryObject<Block> RAINBOW_OAK_LEAVES = leavesBlock("rainbow_oak_leaves", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> THORN_LEAVES = leavesBlock("thorn_leaves", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> THORN_ROSE = thornRoseBlock("thorn_rose", Blocks.WITHER_ROSE);
    public static final TFRegistryObject<Block> THORNS = thornsBlock("thorns", Blocks.GREEN_STAINED_GLASS);
    public static final TFRegistryObject<Block> TORCHBERRY_PLANT = torchberryPlantBlock("torchberry_plant", Blocks.GLOW_LICHEN);
    public static final TFRegistryObject<Block> TROLLVIDR = trollRootBlock("trollvidr", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> UNRIPE_TROLLBER = unripeTorchClusterBlock("unripe_trollber", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> RIPE_TROLLBER = trollRootBlock("ripe_trollber", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> MUSHGLOOM = mushgloomBlock("mushgloom", Blocks.WARPED_FUNGUS);
    public static final TFRegistryObject<Block> LIVEROOT_BLOCK = liverootBlock("liveroot_block", Blocks.ROOTED_DIRT);
    public static final TFRegistryObject<Block> ROOT = plantBlock("root", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> WEATHERED_DEADROCK = solidBlock("weathered_deadrock", Blocks.POLISHED_DEEPSLATE);
    public static final TFRegistryObject<Block> WISPY_CLOUD = wispyCloudBlock("wispy_cloud", Blocks.LIGHT_BLUE_WOOL);
    public static final TFRegistryObject<Block> WORN_CASTLE_BRICK = solidBlock("worn_castle_brick", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> YELLOW_CASTLE_DOOR = castleDoorBlock("yellow_castle_door", Blocks.YELLOW_STAINED_GLASS_PANE);
    public static final TFRegistryObject<Block> YELLOW_CASTLE_RUNE_BRICK = solidBlock("yellow_castle_rune_brick", Blocks.YELLOW_GLAZED_TERRACOTTA);

    // Jars: client-fallback approximates the lit-light look; full block-entity port deferred.
    // Q33: jars now have BlockEntity / random-tick behaviour — see AmbientJarBlock + MasonJarBlock.
    public static final TFRegistryObject<Block> MASON_JAR = masonJarBlock("mason_jar", Blocks.HONEY_BLOCK);
    public static final TFRegistryObject<Block> FIREFLY_JAR = fireflyJarBlock("firefly_jar");
    public static final TFRegistryObject<Block> CICADA_JAR = cicadaJarBlock("cicada_jar");
    // Rope: hanging strand; vanilla CHAIN gives a believable visual.
    public static final TFRegistryObject<Block> ROPE = ropeBlock("rope", Blocks.CHAIN);
    // Experiment 115: edible cake-style block.
    public static final TFRegistryObject<Block> EXPERIMENT_115 = experiment115Block("experiment_115");

    // TrophyItem binds each standing trophy to its wall trophy, matching upstream's
    // one-item/two-placement behavior while paired clients supply the official models.
    public static final TFRegistryObject<Block> NAGA_TROPHY = trophyBlock("naga_trophy", twilightforest.enums.BossVariant.NAGA, 5);
    public static final TFRegistryObject<Block> NAGA_WALL_TROPHY = trophyWallBlock("naga_wall_trophy", twilightforest.enums.BossVariant.NAGA, NAGA_TROPHY);
    public static final TFRegistryObject<Block> LICH_TROPHY = trophyBlock("lich_trophy", twilightforest.enums.BossVariant.LICH, 6);
    public static final TFRegistryObject<Block> LICH_WALL_TROPHY = trophyWallBlock("lich_wall_trophy", twilightforest.enums.BossVariant.LICH, LICH_TROPHY);
    public static final TFRegistryObject<Block> HYDRA_TROPHY = trophyBlock("hydra_trophy", twilightforest.enums.BossVariant.HYDRA, 12);
    public static final TFRegistryObject<Block> HYDRA_WALL_TROPHY = trophyWallBlock("hydra_wall_trophy", twilightforest.enums.BossVariant.HYDRA, HYDRA_TROPHY);
    public static final TFRegistryObject<Block> UR_GHAST_TROPHY = trophyBlock("ur_ghast_trophy", twilightforest.enums.BossVariant.UR_GHAST, 13);
    public static final TFRegistryObject<Block> UR_GHAST_WALL_TROPHY = trophyWallBlock("ur_ghast_wall_trophy", twilightforest.enums.BossVariant.UR_GHAST, UR_GHAST_TROPHY);
    public static final TFRegistryObject<Block> KNIGHT_PHANTOM_TROPHY = trophyBlock("knight_phantom_trophy", twilightforest.enums.BossVariant.KNIGHT_PHANTOM, 8);
    public static final TFRegistryObject<Block> KNIGHT_PHANTOM_WALL_TROPHY = trophyWallBlock("knight_phantom_wall_trophy", twilightforest.enums.BossVariant.KNIGHT_PHANTOM, KNIGHT_PHANTOM_TROPHY);
    public static final TFRegistryObject<Block> SNOW_QUEEN_TROPHY = trophyBlock("snow_queen_trophy", twilightforest.enums.BossVariant.SNOW_QUEEN, 14);
    public static final TFRegistryObject<Block> SNOW_QUEEN_WALL_TROPHY = trophyWallBlock("snow_queen_wall_trophy", twilightforest.enums.BossVariant.SNOW_QUEEN, SNOW_QUEEN_TROPHY);
    public static final TFRegistryObject<Block> MINOSHROOM_TROPHY = trophyBlock("minoshroom_trophy", twilightforest.enums.BossVariant.MINOSHROOM, 7);
    public static final TFRegistryObject<Block> MINOSHROOM_WALL_TROPHY = trophyWallBlock("minoshroom_wall_trophy", twilightforest.enums.BossVariant.MINOSHROOM, MINOSHROOM_TROPHY);
    public static final TFRegistryObject<Block> ALPHA_YETI_TROPHY = trophyBlock("alpha_yeti_trophy", twilightforest.enums.BossVariant.ALPHA_YETI, 9);
    public static final TFRegistryObject<Block> ALPHA_YETI_WALL_TROPHY = trophyWallBlock("alpha_yeti_wall_trophy", twilightforest.enums.BossVariant.ALPHA_YETI, ALPHA_YETI_TROPHY);
    public static final TFRegistryObject<Block> QUEST_RAM_TROPHY = trophyBlock("quest_ram_trophy", twilightforest.enums.BossVariant.QUEST_RAM, 1);
    public static final TFRegistryObject<Block> QUEST_RAM_WALL_TROPHY = trophyWallBlock("quest_ram_wall_trophy", twilightforest.enums.BossVariant.QUEST_RAM, QUEST_RAM_TROPHY);

    // Each family mirrors a vanilla wood family stat-wise; paired-client RP supplies
    // the official TF model. Skipping signs/buttons/pressure_plates/banisters in this
    // batch — those need additional helpers (block-entity wiring for signs, button face
    // attachment for buttons). They can be added in a follow-up batch.

    // -- Twilight Oak family (vanilla fallback: oak) — log/wood/leaves/sapling already in earlier tiers
    public static final TFRegistryObject<Block> TWILIGHT_OAK_PLANKS = solidBlock("twilight_oak_planks", Blocks.OAK_PLANKS);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_SLAB = slabBlock("twilight_oak_slab", Blocks.OAK_SLAB);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_STAIRS = stairBlock("twilight_oak_stairs", Blocks.OAK_STAIRS);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_FENCE = fenceBlock("twilight_oak_fence", Blocks.OAK_FENCE);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_FENCE_GATE = fenceGateBlock("twilight_oak_fence_gate", Blocks.OAK_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.OAK);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_TRAPDOOR = trapDoorBlock("twilight_oak_trapdoor", Blocks.OAK_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.OAK);
    public static final TFRegistryObject<Block> STRIPPED_TWILIGHT_OAK_LOG = pillarBlock("stripped_twilight_oak_log", Blocks.STRIPPED_OAK_LOG);
    public static final TFRegistryObject<Block> STRIPPED_TWILIGHT_OAK_WOOD = pillarBlock("stripped_twilight_oak_wood", Blocks.STRIPPED_OAK_WOOD);

    // -- Canopy family (vanilla fallback: dark_oak)
    public static final TFRegistryObject<Block> CANOPY_PLANKS = solidBlock("canopy_planks", Blocks.DARK_OAK_PLANKS);
    public static final TFRegistryObject<Block> CANOPY_SLAB = slabBlock("canopy_slab", Blocks.DARK_OAK_SLAB);
    public static final TFRegistryObject<Block> CANOPY_STAIRS = stairBlock("canopy_stairs", Blocks.DARK_OAK_STAIRS);
    public static final TFRegistryObject<Block> CANOPY_FENCE_GATE = fenceGateBlock("canopy_fence_gate", Blocks.DARK_OAK_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> CANOPY_TRAPDOOR = trapDoorBlock("canopy_trapdoor", Blocks.DARK_OAK_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> CANOPY_LOG = pillarBlock("canopy_log", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> CANOPY_WOOD = pillarBlock("canopy_wood", Blocks.DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_CANOPY_LOG = pillarBlock("stripped_canopy_log", Blocks.STRIPPED_DARK_OAK_LOG);
    public static final TFRegistryObject<Block> STRIPPED_CANOPY_WOOD = pillarBlock("stripped_canopy_wood", Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> CANOPY_LEAVES = leavesBlock("canopy_leaves", Blocks.DARK_OAK_LEAVES);

    // -- Mangrove family (TF "mangrove" block ids; vanilla fallback: mangrove)
    public static final TFRegistryObject<Block> MANGROVE_PLANKS = solidBlock("mangrove_planks", Blocks.MANGROVE_PLANKS);
    public static final TFRegistryObject<Block> MANGROVE_SLAB = slabBlock("mangrove_slab", Blocks.MANGROVE_SLAB);
    public static final TFRegistryObject<Block> MANGROVE_STAIRS = stairBlock("mangrove_stairs", Blocks.MANGROVE_STAIRS);
    public static final TFRegistryObject<Block> MANGROVE_FENCE = fenceBlock("mangrove_fence", Blocks.MANGROVE_FENCE);
    public static final TFRegistryObject<Block> MANGROVE_FENCE_GATE = fenceGateBlock("mangrove_fence_gate", Blocks.MANGROVE_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_TRAPDOOR = trapDoorBlock("mangrove_trapdoor", Blocks.MANGROVE_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_LOG = pillarBlock("mangrove_log", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> MANGROVE_WOOD = pillarBlock("mangrove_wood", Blocks.MANGROVE_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_MANGROVE_LOG = pillarBlock("stripped_mangrove_log", Blocks.STRIPPED_MANGROVE_LOG);
    public static final TFRegistryObject<Block> STRIPPED_MANGROVE_WOOD = pillarBlock("stripped_mangrove_wood", Blocks.STRIPPED_MANGROVE_WOOD);
    public static final TFRegistryObject<Block> MANGROVE_LEAVES = leavesBlock("mangrove_leaves", Blocks.MANGROVE_LEAVES);
    public static final TFRegistryObject<Block> MANGROVE_SAPLING = mangroveSaplingBlock("mangrove_sapling", Blocks.OAK_SAPLING, TFTreeGrowers.MANGROVE);

    // -- Dark/Darkwood family (TF "dark_*" block ids — distinct from canopy; vanilla fallback: dark_oak)
    public static final TFRegistryObject<Block> DARK_PLANKS = solidBlock("dark_planks", Blocks.DARK_OAK_PLANKS);
    public static final TFRegistryObject<Block> DARK_SLAB = slabBlock("dark_slab", Blocks.DARK_OAK_SLAB);
    public static final TFRegistryObject<Block> DARK_STAIRS = stairBlock("dark_stairs", Blocks.DARK_OAK_STAIRS);
    public static final TFRegistryObject<Block> DARK_FENCE = fenceBlock("dark_fence", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> DARK_FENCE_GATE = fenceGateBlock("dark_fence_gate", Blocks.DARK_OAK_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_TRAPDOOR = trapDoorBlock("dark_trapdoor", Blocks.DARK_OAK_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_WOOD = pillarBlock("dark_wood", Blocks.DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_DARK_LOG = pillarBlock("stripped_dark_log", Blocks.STRIPPED_DARK_OAK_LOG);
    public static final TFRegistryObject<Block> STRIPPED_DARK_WOOD = pillarBlock("stripped_dark_wood", Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> DARK_LEAVES = darkLeavesBlock("dark_leaves", Blocks.DARK_OAK_LEAVES);

    // -- Mining family (TF "mining_*"; vanilla fallback: birch)
    public static final TFRegistryObject<Block> MINING_PLANKS = solidBlock("mining_planks", Blocks.BIRCH_PLANKS);
    public static final TFRegistryObject<Block> MINING_SLAB = slabBlock("mining_slab", Blocks.BIRCH_SLAB);
    public static final TFRegistryObject<Block> MINING_STAIRS = stairBlock("mining_stairs", Blocks.BIRCH_STAIRS);
    public static final TFRegistryObject<Block> MINING_FENCE = fenceBlock("mining_fence", Blocks.BIRCH_FENCE);
    public static final TFRegistryObject<Block> MINING_FENCE_GATE = fenceGateBlock("mining_fence_gate", Blocks.BIRCH_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.BIRCH);
    public static final TFRegistryObject<Block> MINING_TRAPDOOR = trapDoorBlock("mining_trapdoor", Blocks.BIRCH_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.BIRCH);
    public static final TFRegistryObject<Block> MINING_LOG = pillarBlock("mining_log", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> MINING_WOOD = pillarBlock("mining_wood", Blocks.BIRCH_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_MINING_LOG = pillarBlock("stripped_mining_log", Blocks.STRIPPED_BIRCH_LOG);
    public static final TFRegistryObject<Block> STRIPPED_MINING_WOOD = pillarBlock("stripped_mining_wood", Blocks.STRIPPED_BIRCH_WOOD);
    public static final TFRegistryObject<Block> MINING_LEAVES = leavesBlock("mining_leaves", Blocks.BIRCH_LEAVES);
    public static final TFRegistryObject<Block> MINING_SAPLING = saplingBlock("mining_sapling", Blocks.BIRCH_SAPLING, TFTreeGrowers.MINING);

    // -- Time family (TF "time_*"; vanilla fallback: spruce)
    public static final TFRegistryObject<Block> TIME_PLANKS = solidBlock("time_planks", Blocks.SPRUCE_PLANKS);
    public static final TFRegistryObject<Block> TIME_SLAB = slabBlock("time_slab", Blocks.SPRUCE_SLAB);
    public static final TFRegistryObject<Block> TIME_STAIRS = stairBlock("time_stairs", Blocks.SPRUCE_STAIRS);
    public static final TFRegistryObject<Block> TIME_FENCE = fenceBlock("time_fence", Blocks.SPRUCE_FENCE);
    public static final TFRegistryObject<Block> TIME_FENCE_GATE = fenceGateBlock("time_fence_gate", Blocks.SPRUCE_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE);
    public static final TFRegistryObject<Block> TIME_TRAPDOOR = trapDoorBlock("time_trapdoor", Blocks.SPRUCE_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.SPRUCE);
    public static final TFRegistryObject<Block> TIME_LOG = pillarBlock("time_log", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> TIME_WOOD = pillarBlock("time_wood", Blocks.SPRUCE_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_TIME_LOG = pillarBlock("stripped_time_log", Blocks.STRIPPED_SPRUCE_LOG);
    public static final TFRegistryObject<Block> STRIPPED_TIME_WOOD = pillarBlock("stripped_time_wood", Blocks.STRIPPED_SPRUCE_WOOD);
    public static final TFRegistryObject<Block> TIME_LEAVES = leavesBlock("time_leaves", Blocks.SPRUCE_LEAVES);
    public static final TFRegistryObject<Block> TIME_SAPLING = saplingBlock("time_sapling", Blocks.SPRUCE_SAPLING, TFTreeGrowers.TIME);

    // -- Transformation family (TF "transformation_*"; vanilla fallback: jungle)
    public static final TFRegistryObject<Block> TRANSFORMATION_PLANKS = solidBlock("transformation_planks", Blocks.JUNGLE_PLANKS);
    public static final TFRegistryObject<Block> TRANSFORMATION_SLAB = slabBlock("transformation_slab", Blocks.JUNGLE_SLAB);
    public static final TFRegistryObject<Block> TRANSFORMATION_STAIRS = stairBlock("transformation_stairs", Blocks.JUNGLE_STAIRS);
    public static final TFRegistryObject<Block> TRANSFORMATION_FENCE = fenceBlock("transformation_fence", Blocks.JUNGLE_FENCE);
    public static final TFRegistryObject<Block> TRANSFORMATION_FENCE_GATE = fenceGateBlock("transformation_fence_gate", Blocks.JUNGLE_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_TRAPDOOR = trapDoorBlock("transformation_trapdoor", Blocks.JUNGLE_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_LOG = pillarBlock("transformation_log", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> TRANSFORMATION_WOOD = pillarBlock("transformation_wood", Blocks.JUNGLE_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_TRANSFORMATION_LOG = pillarBlock("stripped_transformation_log", Blocks.STRIPPED_JUNGLE_LOG);
    public static final TFRegistryObject<Block> STRIPPED_TRANSFORMATION_WOOD = pillarBlock("stripped_transformation_wood", Blocks.STRIPPED_JUNGLE_WOOD);
    public static final TFRegistryObject<Block> TRANSFORMATION_LEAVES = transformationLeavesBlock("transformation_leaves", Blocks.JUNGLE_LEAVES);
    public static final TFRegistryObject<Block> TRANSFORMATION_SAPLING = saplingBlock("transformation_sapling", Blocks.JUNGLE_SAPLING, TFTreeGrowers.TRANSFORMATION);

    // -- Sorting family (TF "sorting_*"; vanilla fallback: cherry)
    public static final TFRegistryObject<Block> SORTING_PLANKS = solidBlock("sorting_planks", Blocks.CHERRY_PLANKS);
    public static final TFRegistryObject<Block> SORTING_SLAB = slabBlock("sorting_slab", Blocks.CHERRY_SLAB);
    public static final TFRegistryObject<Block> SORTING_STAIRS = stairBlock("sorting_stairs", Blocks.CHERRY_STAIRS);
    public static final TFRegistryObject<Block> SORTING_FENCE = fenceBlock("sorting_fence", Blocks.CHERRY_FENCE);
    public static final TFRegistryObject<Block> SORTING_FENCE_GATE = fenceGateBlock("sorting_fence_gate", Blocks.CHERRY_FENCE_GATE, net.minecraft.world.level.block.state.properties.WoodType.CHERRY);
    public static final TFRegistryObject<Block> SORTING_TRAPDOOR = trapDoorBlock("sorting_trapdoor", Blocks.CHERRY_TRAPDOOR, net.minecraft.world.level.block.state.properties.BlockSetType.CHERRY);
    public static final TFRegistryObject<Block> SORTING_LOG = pillarBlock("sorting_log", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> SORTING_WOOD = pillarBlock("sorting_wood", Blocks.CHERRY_WOOD);
    public static final TFRegistryObject<Block> STRIPPED_SORTING_LOG = pillarBlock("stripped_sorting_log", Blocks.STRIPPED_CHERRY_LOG);
    public static final TFRegistryObject<Block> STRIPPED_SORTING_WOOD = pillarBlock("stripped_sorting_wood", Blocks.STRIPPED_CHERRY_WOOD);
    public static final TFRegistryObject<Block> SORTING_LEAVES = leavesBlock("sorting_leaves", Blocks.CHERRY_LEAVES);
    public static final TFRegistryObject<Block> SORTING_SAPLING = saplingBlock("sorting_sapling", Blocks.CHERRY_SAPLING, TFTreeGrowers.SORTING);


    // -- Banisters (TF custom; vanilla fence-shaped fallback per wood type)
    public static final TFRegistryObject<Block> OAK_BANISTER = banisterBlock("oak_banister", Blocks.OAK_FENCE);
    public static final TFRegistryObject<Block> SPRUCE_BANISTER = banisterBlock("spruce_banister", Blocks.SPRUCE_FENCE);
    public static final TFRegistryObject<Block> BIRCH_BANISTER = banisterBlock("birch_banister", Blocks.BIRCH_FENCE);
    public static final TFRegistryObject<Block> JUNGLE_BANISTER = banisterBlock("jungle_banister", Blocks.JUNGLE_FENCE);
    public static final TFRegistryObject<Block> ACACIA_BANISTER = banisterBlock("acacia_banister", Blocks.ACACIA_FENCE);
    public static final TFRegistryObject<Block> DARK_OAK_BANISTER = banisterBlock("dark_oak_banister", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> MANGROVE_BANISTER = banisterBlock("mangrove_banister", Blocks.MANGROVE_FENCE);
    public static final TFRegistryObject<Block> CHERRY_BANISTER = banisterBlock("cherry_banister", Blocks.CHERRY_FENCE);
    public static final TFRegistryObject<Block> BAMBOO_BANISTER = banisterBlock("bamboo_banister", Blocks.BAMBOO_FENCE);
    public static final TFRegistryObject<Block> CRIMSON_BANISTER = banisterBlock("crimson_banister", Blocks.CRIMSON_FENCE);
    public static final TFRegistryObject<Block> WARPED_BANISTER = banisterBlock("warped_banister", Blocks.WARPED_FENCE);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_BANISTER = banisterBlock("twilight_oak_banister", Blocks.OAK_FENCE);
    public static final TFRegistryObject<Block> CANOPY_BANISTER = banisterBlock("canopy_banister", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> DARK_BANISTER = banisterBlock("dark_banister", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> MINING_BANISTER = banisterBlock("mining_banister", Blocks.BIRCH_FENCE);
    public static final TFRegistryObject<Block> TIME_BANISTER = banisterBlock("time_banister", Blocks.SPRUCE_FENCE);
    public static final TFRegistryObject<Block> TRANSFORMATION_BANISTER = banisterBlock("transformation_banister", Blocks.JUNGLE_FENCE);
    public static final TFRegistryObject<Block> SORTING_BANISTER = banisterBlock("sorting_banister", Blocks.CHERRY_FENCE);

    // -- Drying racks (TF custom; vanilla fence-shape fallback)
    public static final TFRegistryObject<Block> OAK_DRYING_RACK = dryingRackBlock("oak_drying_rack", Blocks.OAK_FENCE);
    public static final TFRegistryObject<Block> SPRUCE_DRYING_RACK = dryingRackBlock("spruce_drying_rack", Blocks.SPRUCE_FENCE);
    public static final TFRegistryObject<Block> BIRCH_DRYING_RACK = dryingRackBlock("birch_drying_rack", Blocks.BIRCH_FENCE);
    public static final TFRegistryObject<Block> JUNGLE_DRYING_RACK = dryingRackBlock("jungle_drying_rack", Blocks.JUNGLE_FENCE);
    public static final TFRegistryObject<Block> ACACIA_DRYING_RACK = dryingRackBlock("acacia_drying_rack", Blocks.ACACIA_FENCE);
    public static final TFRegistryObject<Block> DARK_OAK_DRYING_RACK = dryingRackBlock("dark_oak_drying_rack", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> MANGROVE_DRYING_RACK = dryingRackBlock("mangrove_drying_rack", Blocks.MANGROVE_FENCE);
    public static final TFRegistryObject<Block> CHERRY_DRYING_RACK = dryingRackBlock("cherry_drying_rack", Blocks.CHERRY_FENCE);
    public static final TFRegistryObject<Block> BAMBOO_DRYING_RACK = dryingRackBlock("bamboo_drying_rack", Blocks.BAMBOO_FENCE);
    public static final TFRegistryObject<Block> CRIMSON_DRYING_RACK = dryingRackBlock("crimson_drying_rack", Blocks.CRIMSON_FENCE);
    public static final TFRegistryObject<Block> WARPED_DRYING_RACK = dryingRackBlock("warped_drying_rack", Blocks.WARPED_FENCE);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_DRYING_RACK = dryingRackBlock("twilight_oak_drying_rack", Blocks.OAK_FENCE);
    public static final TFRegistryObject<Block> CANOPY_DRYING_RACK = dryingRackBlock("canopy_drying_rack", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> DARK_DRYING_RACK = dryingRackBlock("dark_drying_rack", Blocks.DARK_OAK_FENCE);
    public static final TFRegistryObject<Block> MINING_DRYING_RACK = directionalBlock("mining_drying_rack", Blocks.BIRCH_FENCE);
    public static final TFRegistryObject<Block> TIME_DRYING_RACK = directionalBlock("time_drying_rack", Blocks.SPRUCE_FENCE);
    public static final TFRegistryObject<Block> TRANSFORMATION_DRYING_RACK = directionalBlock("transformation_drying_rack", Blocks.JUNGLE_FENCE);
    public static final TFRegistryObject<Block> SORTING_DRYING_RACK = directionalBlock("sorting_drying_rack", Blocks.CHERRY_FENCE);

    // -- TF wood chests (extending vanilla chest family) — uses chestBlock helper
    public static final TFRegistryObject<Block> CANOPY_CHEST = chestBlock("canopy_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> MANGROVE_CHEST = chestBlock("mangrove_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> DARK_CHEST = chestBlock("dark_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> MINING_CHEST = chestBlock("mining_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> TIME_CHEST = chestBlock("time_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> TRANSFORMATION_CHEST = chestBlock("transformation_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> SORTING_CHEST = chestBlock("sorting_chest", Blocks.CHEST);

    // -- Q12 batch: misc decoration / structural blocks ======
    // Beanstalk (Quest Ram structure)
    public static final TFRegistryObject<Block> BEANSTALK_LEAVES = leavesBlock("beanstalk_leaves", Blocks.OAK_LEAVES);
    public static final TFRegistryObject<Block> BEANSTALK_GROWER = growingBeanstalkBlock("beanstalk_grower");
    // Cinder (Ur-Ghast crater)
    public static final TFRegistryObject<Block> CINDER_LOG = pillarBlock("cinder_log", Blocks.NETHERRACK);
    public static final TFRegistryObject<Block> CINDER_WOOD = pillarBlock("cinder_wood", Blocks.NETHERRACK);
    public static final TFRegistryObject<Block> CINDER_FURNACE = cinderFurnaceBlock("cinder_furnace");
    public static final TFRegistryObject<Block> BRAZIER = brazierBlock("brazier", Blocks.SOUL_CAMPFIRE);
    public static final TFRegistryObject<Block> CANDELABRA = candelabraBlock("candelabra", Blocks.LANTERN);
    // Castle bricks (Final Castle)
    public static final TFRegistryObject<Block> BOLD_CASTLE_BRICK_STAIRS = stairBlock("bold_castle_brick_stairs", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> BOLD_CASTLE_BRICK_TILE = solidBlock("bold_castle_brick_tile", Blocks.POLISHED_BLACKSTONE_BRICKS);
    public static final TFRegistryObject<Block> BOLD_STONE_PILLAR = pillarBlock("bold_stone_pillar", Blocks.POLISHED_BASALT);
    public static final TFRegistryObject<Block> ENCASED_CASTLE_BRICK_STAIRS = stairBlock("encased_castle_brick_stairs", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> CRACKED_CASTLE_BRICK_STAIRS = stairBlock("cracked_castle_brick_stairs", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> MOSSY_CASTLE_BRICK_STAIRS = stairBlock("mossy_castle_brick_stairs", Blocks.MOSSY_STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> WORN_CASTLE_BRICK_STAIRS = stairBlock("worn_castle_brick_stairs", Blocks.STONE_BRICK_STAIRS);
    // Aurora variants
    public static final TFRegistryObject<Block> AURORA_SLAB = slabBlock("aurora_slab", Blocks.PRISMARINE_SLAB);
    public static final TFRegistryObject<Block> AURORALIZED_GLASS = auroralizedGlassBlock("auroralized_glass", Blocks.LIGHT_BLUE_STAINED_GLASS);
    // Plants & berries
    public static final TFRegistryObject<Block> CLOVER_PATCH = plantBlock("clover_patch", Blocks.SHORT_GRASS);
    public static final TFRegistryObject<Block> COPPER_OREBERRY = oreBerryBlock("copper_oreberry", Blocks.COPPER_BLOCK);
    public static final TFRegistryObject<Block> IRON_OREBERRY = oreBerryBlock("iron_oreberry", Blocks.IRON_BLOCK);
    public static final TFRegistryObject<Block> GOLD_OREBERRY = oreBerryBlock("gold_oreberry", Blocks.GOLD_BLOCK);
    public static final TFRegistryObject<Block> BLACKBERRY_BUSH = berryBushBlock("blackberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> BLUEBERRY_BUSH = berryBushBlock("blueberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> RASPBERRY_BUSH = berryBushBlock("raspberry_bush", Blocks.SWEET_BERRY_BUSH);
    // Antibuild / built / carminite
    public static final TFRegistryObject<Block> ANTIBUILT_BLOCK = solidBlock("antibuilt_block", Blocks.BARRIER);
    public static final TFRegistryObject<Block> BUILT_BLOCK = translucentBuiltBlock("built_block");
    public static final TFRegistryObject<Block> CARMINITE_BLOCK = solidBlock("carminite_block", Blocks.REDSTONE_BLOCK);
    // Fur / arctic
    public static final TFRegistryObject<Block> ARCTIC_FUR_BLOCK = arcticFurBlock("arctic_fur_block", Blocks.WHITE_WOOL);
    public static final TFRegistryObject<Block> ALPHA_YETI_FUR_BLOCK = solidBlock("alpha_yeti_fur_block", Blocks.WHITE_WOOL);
    // Burnt thorns
    public static final TFRegistryObject<Block> BURNT_THORNS = burntThornsBlock("burnt_thorns", Blocks.BLACK_STAINED_GLASS);


    // -- Hollow log variants: 16 wood types × 3 orientations (climbable/horizontal/vertical) = 48 blocks
    // All use pillarBlock with the matching vanilla log fallback to preserve AXIS state.
    public static final TFRegistryObject<Block> HOLLOW_OAK_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_oak_log_climbable", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_OAK_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_oak_log_horizontal", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_OAK_LOG_VERTICAL = hollowLogVerticalBlock("hollow_oak_log_vertical", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_SPRUCE_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_spruce_log_climbable", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_SPRUCE_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_spruce_log_horizontal", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_BIRCH_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_birch_log_climbable", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_BIRCH_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_birch_log_horizontal", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_BIRCH_LOG_VERTICAL = hollowLogVerticalBlock("hollow_birch_log_vertical", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_JUNGLE_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_jungle_log_climbable", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_JUNGLE_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_jungle_log_horizontal", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_JUNGLE_LOG_VERTICAL = hollowLogVerticalBlock("hollow_jungle_log_vertical", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_ACACIA_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_acacia_log_climbable", Blocks.ACACIA_LOG);
    public static final TFRegistryObject<Block> HOLLOW_ACACIA_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_acacia_log_horizontal", Blocks.ACACIA_LOG);
    public static final TFRegistryObject<Block> HOLLOW_ACACIA_LOG_VERTICAL = hollowLogVerticalBlock("hollow_acacia_log_vertical", Blocks.ACACIA_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_OAK_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_dark_oak_log_climbable", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_OAK_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_dark_oak_log_horizontal", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_OAK_LOG_VERTICAL = hollowLogVerticalBlock("hollow_dark_oak_log_vertical", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MANGROVE_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_mangrove_log_climbable", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MANGROVE_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_mangrove_log_horizontal", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MANGROVE_LOG_VERTICAL = hollowLogVerticalBlock("hollow_mangrove_log_vertical", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CHERRY_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_cherry_log_climbable", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CHERRY_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_cherry_log_horizontal", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CHERRY_LOG_VERTICAL = hollowLogVerticalBlock("hollow_cherry_log_vertical", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CRIMSON_STEM_CLIMBABLE = hollowLogClimbableBlock("hollow_crimson_stem_climbable", Blocks.CRIMSON_STEM);
    public static final TFRegistryObject<Block> HOLLOW_CRIMSON_STEM_HORIZONTAL = hollowLogHorizontalBlock("hollow_crimson_stem_horizontal", Blocks.CRIMSON_STEM);
    public static final TFRegistryObject<Block> HOLLOW_CRIMSON_STEM_VERTICAL = hollowLogVerticalBlock("hollow_crimson_stem_vertical", Blocks.CRIMSON_STEM);
    public static final TFRegistryObject<Block> HOLLOW_WARPED_STEM_CLIMBABLE = hollowLogClimbableBlock("hollow_warped_stem_climbable", Blocks.WARPED_STEM);
    public static final TFRegistryObject<Block> HOLLOW_WARPED_STEM_HORIZONTAL = hollowLogHorizontalBlock("hollow_warped_stem_horizontal", Blocks.WARPED_STEM);
    public static final TFRegistryObject<Block> HOLLOW_WARPED_STEM_VERTICAL = hollowLogVerticalBlock("hollow_warped_stem_vertical", Blocks.WARPED_STEM);
    public static final TFRegistryObject<Block> HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_twilight_oak_log_climbable", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_twilight_oak_log_horizontal", Blocks.OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CANOPY_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_canopy_log_climbable", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CANOPY_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_canopy_log_horizontal", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_CANOPY_LOG_VERTICAL = hollowLogVerticalBlock("hollow_canopy_log_vertical", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_dark_log_climbable", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_dark_log_horizontal", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_DARK_LOG_VERTICAL = hollowLogVerticalBlock("hollow_dark_log_vertical", Blocks.DARK_OAK_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MINING_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_mining_log_climbable", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MINING_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_mining_log_horizontal", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_MINING_LOG_VERTICAL = hollowLogVerticalBlock("hollow_mining_log_vertical", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TIME_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_time_log_climbable", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TIME_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_time_log_horizontal", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TIME_LOG_VERTICAL = hollowLogVerticalBlock("hollow_time_log_vertical", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TRANSFORMATION_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_transformation_log_climbable", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TRANSFORMATION_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_transformation_log_horizontal", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TRANSFORMATION_LOG_VERTICAL = hollowLogVerticalBlock("hollow_transformation_log_vertical", Blocks.JUNGLE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_SORTING_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_sorting_log_climbable", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_SORTING_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_sorting_log_horizontal", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_SORTING_LOG_VERTICAL = hollowLogVerticalBlock("hollow_sorting_log_vertical", Blocks.CHERRY_LOG);
    public static final TFRegistryObject<Block> HOLLOW_VANGROVE_LOG_CLIMBABLE = hollowLogClimbableBlock("hollow_vangrove_log_climbable", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_VANGROVE_LOG_HORIZONTAL = hollowLogHorizontalBlock("hollow_vangrove_log_horizontal", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_VANGROVE_LOG_VERTICAL = hollowLogVerticalBlock("hollow_vangrove_log_vertical", Blocks.MANGROVE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_OAK_SAPLING = hollowOakSaplingBlock("hollow_oak_sapling", Blocks.OAK_SAPLING);

    // -- Trapped chests (TF wood variants) — chestBlock fallback
    public static final TFRegistryObject<Block> CANOPY_TRAPPED_CHEST = chestBlock("canopy_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> MANGROVE_TRAPPED_CHEST = chestBlock("mangrove_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> DARK_TRAPPED_CHEST = chestBlock("dark_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> MINING_TRAPPED_CHEST = chestBlock("mining_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> TIME_TRAPPED_CHEST = chestBlock("time_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> TRANSFORMATION_TRAPPED_CHEST = chestBlock("transformation_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> SORTING_TRAPPED_CHEST = chestBlock("sorting_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_TRAPPED_CHEST = chestBlock("twilight_oak_trapped_chest", Blocks.TRAPPED_CHEST);
    public static final TFRegistryObject<Block> SKULL_CHEST = skullChestBlock("skull_chest");

    // -- Miniature structures (decoration; standing-skull-shaped fallback)
    public static final TFRegistryObject<Block> AURORA_PALACE_MINIATURE_STRUCTURE = miniatureStructureBlock("aurora_palace_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> DARK_TOWER_MINIATURE_STRUCTURE = miniatureStructureBlock("dark_tower_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> FINAL_CASTLE_MINIATURE_STRUCTURE = miniatureStructureBlock("final_castle_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE = miniatureStructureBlock("goblin_stronghold_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> HEDGE_MAZE_MINIATURE_STRUCTURE = miniatureStructureBlock("hedge_maze_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> HOLLOW_HILL_MINIATURE_STRUCTURE = miniatureStructureBlock("hollow_hill_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> HYDRA_LAIR_MINIATURE_STRUCTURE = miniatureStructureBlock("hydra_lair_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> LICH_TOWER_MINIATURE_STRUCTURE = miniatureStructureBlock("lich_tower_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE = miniatureStructureBlock("minotaur_labyrinth_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> MUSHROOM_TOWER_MINIATURE_STRUCTURE = miniatureStructureBlock("mushroom_tower_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> NAGA_COURTYARD_MINIATURE_STRUCTURE = miniatureStructureBlock("naga_courtyard_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> QUEST_GROVE_MINIATURE_STRUCTURE = miniatureStructureBlock("quest_grove_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE = miniatureStructureBlock("troll_cave_cottage_miniature_structure", Blocks.SKELETON_SKULL);
    public static final TFRegistryObject<Block> YETI_CAVE_MINIATURE_STRUCTURE = miniatureStructureBlock("yeti_cave_miniature_structure", Blocks.SKELETON_SKULL);

    // -- Potted saplings (decoration; vanilla flower_pot fallback)
    public static final TFRegistryObject<Block> POTTED_TWILIGHT_OAK_SAPLING = solidBlock("potted_twilight_oak_sapling", Blocks.POTTED_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_CANOPY_SAPLING = solidBlock("potted_canopy_sapling", Blocks.POTTED_DARK_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_MANGROVE_SAPLING = solidBlock("potted_mangrove_sapling", Blocks.POTTED_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_DARKWOOD_SAPLING = solidBlock("potted_darkwood_sapling", Blocks.POTTED_DARK_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_MINING_SAPLING = solidBlock("potted_mining_sapling", Blocks.POTTED_BIRCH_SAPLING);
    public static final TFRegistryObject<Block> POTTED_TIME_SAPLING = solidBlock("potted_time_sapling", Blocks.POTTED_SPRUCE_SAPLING);
    public static final TFRegistryObject<Block> POTTED_TRANSFORMATION_SAPLING = solidBlock("potted_transformation_sapling", Blocks.POTTED_JUNGLE_SAPLING);
    public static final TFRegistryObject<Block> POTTED_SORTING_SAPLING = solidBlock("potted_sorting_sapling", Blocks.POTTED_CHERRY_SAPLING);
    public static final TFRegistryObject<Block> POTTED_RAINBOW_OAK_SAPLING = solidBlock("potted_rainbow_oak_sapling", Blocks.POTTED_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_HOLLOW_OAK_SAPLING = solidBlock("potted_hollow_oak_sapling", Blocks.POTTED_OAK_SAPLING);
    public static final TFRegistryObject<Block> POTTED_DEAD_THORN = solidBlock("potted_dead_thorn", Blocks.POTTED_DEAD_BUSH);
    public static final TFRegistryObject<Block> POTTED_GREEN_THORN = solidBlock("potted_green_thorn", Blocks.POTTED_DEAD_BUSH);
    public static final TFRegistryObject<Block> POTTED_THORN = solidBlock("potted_thorn", Blocks.POTTED_DEAD_BUSH);
    public static final TFRegistryObject<Block> POTTED_FIDDLEHEAD = solidBlock("potted_fiddlehead", Blocks.POTTED_FERN);
    public static final TFRegistryObject<Block> POTTED_MAYAPPLE = solidBlock("potted_mayapple", Blocks.POTTED_FERN);
    public static final TFRegistryObject<Block> POTTED_MUSHGLOOM = specialFlowerPotBlock("potted_mushgloom", () -> MUSHGLOOM.get(), Blocks.POTTED_WARPED_FUNGUS);

    // -- Skull candles (decorative variants; vanilla skull fallback)
    public static final TFRegistryObject<Block> CREEPER_WALL_SKULL_CANDLE = wallSkullCandleBlock("creeper_wall_skull_candle", SkullBlock.Types.CREEPER, Blocks.CREEPER_WALL_HEAD);
    public static final TFRegistryObject<Block> CREEPER_SKULL_CANDLE = skullCandleBlock("creeper_skull_candle", SkullBlock.Types.CREEPER, Blocks.CREEPER_HEAD, CREEPER_WALL_SKULL_CANDLE);
    public static final TFRegistryObject<Block> SKELETON_WALL_SKULL_CANDLE = wallSkullCandleBlock("skeleton_wall_skull_candle", SkullBlock.Types.SKELETON, Blocks.SKELETON_WALL_SKULL);
    public static final TFRegistryObject<Block> SKELETON_SKULL_CANDLE = skullCandleBlock("skeleton_skull_candle", SkullBlock.Types.SKELETON, Blocks.SKELETON_SKULL, SKELETON_WALL_SKULL_CANDLE);
    public static final TFRegistryObject<Block> WITHER_SKELETON_WALL_SKULL_CANDLE = wallSkullCandleBlock("wither_skeleton_wall_skull_candle", SkullBlock.Types.WITHER_SKELETON, Blocks.WITHER_SKELETON_WALL_SKULL);
    public static final TFRegistryObject<Block> WITHER_SKELETON_SKULL_CANDLE = skullCandleBlock("wither_skeleton_skull_candle", SkullBlock.Types.WITHER_SKELETON, Blocks.WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL_CANDLE);
    public static final TFRegistryObject<Block> ZOMBIE_WALL_SKULL_CANDLE = wallSkullCandleBlock("zombie_wall_skull_candle", SkullBlock.Types.ZOMBIE, Blocks.ZOMBIE_WALL_HEAD);
    public static final TFRegistryObject<Block> ZOMBIE_SKULL_CANDLE = skullCandleBlock("zombie_skull_candle", SkullBlock.Types.ZOMBIE, Blocks.ZOMBIE_HEAD, ZOMBIE_WALL_SKULL_CANDLE);
    public static final TFRegistryObject<Block> PLAYER_WALL_SKULL_CANDLE = wallSkullCandleBlock("player_wall_skull_candle", SkullBlock.Types.PLAYER, Blocks.PLAYER_WALL_HEAD);
    public static final TFRegistryObject<Block> PLAYER_SKULL_CANDLE = skullCandleBlock("player_skull_candle", SkullBlock.Types.PLAYER, Blocks.PLAYER_HEAD, PLAYER_WALL_SKULL_CANDLE);
    public static final TFRegistryObject<Block> PIGLIN_WALL_SKULL_CANDLE = wallSkullCandleBlock("piglin_wall_skull_candle", SkullBlock.Types.PIGLIN, Blocks.PIGLIN_WALL_HEAD);
    public static final TFRegistryObject<Block> PIGLIN_SKULL_CANDLE = skullCandleBlock("piglin_skull_candle", SkullBlock.Types.PIGLIN, Blocks.PIGLIN_HEAD, PIGLIN_WALL_SKULL_CANDLE);

    // -- Q13 misc decoration / structural blocks ======
    public static final TFRegistryObject<Block> CANOPY_WINDOW = solidBlock("canopy_window", Blocks.GLASS);
    public static final TFRegistryObject<Block> CANOPY_WINDOW_PANE = ironBarsBlock("canopy_window_pane", Blocks.GLASS_PANE);
    public static final TFRegistryObject<Block> CHISELED_CANOPY_BOOKSHELF = chiseledCanopyShelfBlock("chiseled_canopy_bookshelf", Blocks.CHISELED_BOOKSHELF);
    public static final TFRegistryObject<Block> ENCASED_CASTLE_BRICK_PILLAR = pillarBlock("encased_castle_brick_pillar", Blocks.POLISHED_BASALT);
    public static final TFRegistryObject<Block> ENCASED_CASTLE_BRICK_TILE = solidBlock("encased_castle_brick_tile", Blocks.POLISHED_BLACKSTONE_BRICKS);
    public static final TFRegistryObject<Block> MOSSY_CASTLE_BRICK = solidBlock("mossy_castle_brick", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> MOSSY_CASTLE_BRICK_PILLAR = pillarBlock("mossy_castle_brick_pillar", Blocks.POLISHED_BASALT);
    public static final TFRegistryObject<Block> SLIDER = sliderBlock("slider");
    public static final TFRegistryObject<Block> TWISTED_STONE = pillarBlock("twisted_stone", Blocks.POLISHED_DEEPSLATE);
    public static final TFRegistryObject<Block> UNCRAFTING_TABLE = uncraftingTableBlock("uncrafting_table");
    public static final TFRegistryObject<Block> UNDERBRICK_FLOOR = solidBlock("underbrick_floor", Blocks.DEEPSLATE_BRICKS);
    public static final TFRegistryObject<Block> SNOWY_CLOUD = cloudBlock("snowy_cloud", Blocks.SNOW_BLOCK, Biome.Precipitation.SNOW);
    public static final TFRegistryObject<Block> OMINOUS_FIRE = ominousFireBlock("ominous_fire", Blocks.SOUL_FIRE);
    public static final TFRegistryObject<Block> RED_THREAD = redThreadBlock("red_thread", Blocks.RED_WOOL);
    public static final TFRegistryObject<Block> REACTOR_DEBRIS = reactorDebrisBlock("reactor_debris");
    public static final TFRegistryObject<Block> FAKE_DIAMOND = solidBlock("fake_diamond", Blocks.DIAMOND_BLOCK);
    public static final TFRegistryObject<Block> FAKE_GOLD = solidBlock("fake_gold", Blocks.GOLD_BLOCK);
    public static final TFRegistryObject<Block> MINING_LOG_CORE = mineLogCoreBlock("mining_log_core", Blocks.BIRCH_LOG);
    public static final TFRegistryObject<Block> TIME_LOG_CORE = timeLogCoreBlock("time_log_core", Blocks.SPRUCE_LOG);
    public static final TFRegistryObject<Block> MAZE_SLIME_BLOCK = mazeSlimeBlock("maze_slime_block", Blocks.SLIME_BLOCK);
    public static final TFRegistryObject<Block> MALOBERRY_BUSH = berryBushBlock("maloberry_bush", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> ESSENCE_OREBERRY = oreBerryBlock("essence_oreberry", Blocks.AMETHYST_BLOCK);
    public static final TFRegistryObject<Block> VANGROVE_BANISTER = fenceBlock("vangrove_banister", Blocks.MANGROVE_FENCE);
    public static final TFRegistryObject<Block> VANGROVE_DRYING_RACK = directionalBlock("vangrove_drying_rack", Blocks.MANGROVE_FENCE);
    public static final TFRegistryObject<Block> FINAL_BOSS_BOSS_SPAWNER = bossSpawnerBlock("final_boss_boss_spawner", twilightforest.enums.BossVariant.FINAL_BOSS);
    public static final TFRegistryObject<Block> FIREFLY_PARTICLE_SPAWNER = particleSpawnerBlock("firefly_particle_spawner");
    public static final TFRegistryObject<Block> FIREFLY_SPAWNER = FIREFLY_PARTICLE_SPAWNER;

    // -- Q13.5: TF wood-family buttons + pressure plates (sentinel)
    public static final TFRegistryObject<Block> DARK_BUTTON = buttonBlock("dark_button", Blocks.DARK_OAK_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_PRESSURE_PLATE = pressurePlateBlock("dark_pressure_plate", Blocks.DARK_OAK_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> MANGROVE_BUTTON = buttonBlock("mangrove_button", Blocks.MANGROVE_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_PRESSURE_PLATE = pressurePlateBlock("mangrove_pressure_plate", Blocks.MANGROVE_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.MANGROVE);
    public static final TFRegistryObject<Block> TRANSFORMATION_BUTTON = buttonBlock("transformation_button", Blocks.JUNGLE_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_PRESSURE_PLATE = pressurePlateBlock("transformation_pressure_plate", Blocks.JUNGLE_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.JUNGLE);

    public static final TFRegistryObject<Block> TWILIGHT_OAK_SIGN = standingSignBlock("twilight_oak_sign", Blocks.OAK_SIGN, net.minecraft.world.level.block.state.properties.WoodType.OAK);
    public static final TFRegistryObject<Block> TWILIGHT_WALL_SIGN = wallSignBlock("twilight_wall_sign", Blocks.OAK_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.OAK);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_HANGING_SIGN = ceilingHangingSignBlock("twilight_oak_hanging_sign", Blocks.OAK_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.OAK);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_WALL_HANGING_SIGN = wallHangingSignBlock("twilight_oak_wall_hanging_sign", Blocks.OAK_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.OAK);

    public static final TFRegistryObject<Block> CANOPY_SIGN = standingSignBlock("canopy_sign", Blocks.DARK_OAK_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> CANOPY_WALL_SIGN = wallSignBlock("canopy_wall_sign", Blocks.DARK_OAK_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> CANOPY_HANGING_SIGN = ceilingHangingSignBlock("canopy_hanging_sign", Blocks.DARK_OAK_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> CANOPY_WALL_HANGING_SIGN = wallHangingSignBlock("canopy_wall_hanging_sign", Blocks.DARK_OAK_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);

    public static final TFRegistryObject<Block> DARK_SIGN = standingSignBlock("dark_sign", Blocks.DARK_OAK_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_WALL_SIGN = wallSignBlock("dark_wall_sign", Blocks.DARK_OAK_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_HANGING_SIGN = ceilingHangingSignBlock("dark_hanging_sign", Blocks.DARK_OAK_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);
    public static final TFRegistryObject<Block> DARK_WALL_HANGING_SIGN = wallHangingSignBlock("dark_wall_hanging_sign", Blocks.DARK_OAK_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK);

    public static final TFRegistryObject<Block> MANGROVE_SIGN = standingSignBlock("mangrove_sign", Blocks.MANGROVE_SIGN, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_WALL_SIGN = wallSignBlock("mangrove_wall_sign", Blocks.MANGROVE_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_HANGING_SIGN = ceilingHangingSignBlock("mangrove_hanging_sign", Blocks.MANGROVE_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE);
    public static final TFRegistryObject<Block> MANGROVE_WALL_HANGING_SIGN = wallHangingSignBlock("mangrove_wall_hanging_sign", Blocks.MANGROVE_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE);

    public static final TFRegistryObject<Block> MINING_SIGN = standingSignBlock("mining_sign", Blocks.BIRCH_SIGN, net.minecraft.world.level.block.state.properties.WoodType.BIRCH);
    public static final TFRegistryObject<Block> MINING_WALL_SIGN = wallSignBlock("mining_wall_sign", Blocks.BIRCH_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.BIRCH);
    public static final TFRegistryObject<Block> MINING_HANGING_SIGN = ceilingHangingSignBlock("mining_hanging_sign", Blocks.BIRCH_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.BIRCH);
    public static final TFRegistryObject<Block> MINING_WALL_HANGING_SIGN = wallHangingSignBlock("mining_wall_hanging_sign", Blocks.BIRCH_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.BIRCH);

    public static final TFRegistryObject<Block> TIME_SIGN = standingSignBlock("time_sign", Blocks.SPRUCE_SIGN, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE);
    public static final TFRegistryObject<Block> TIME_WALL_SIGN = wallSignBlock("time_wall_sign", Blocks.SPRUCE_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE);
    public static final TFRegistryObject<Block> TIME_HANGING_SIGN = ceilingHangingSignBlock("time_hanging_sign", Blocks.SPRUCE_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE);
    public static final TFRegistryObject<Block> TIME_WALL_HANGING_SIGN = wallHangingSignBlock("time_wall_hanging_sign", Blocks.SPRUCE_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE);

    public static final TFRegistryObject<Block> TRANSFORMATION_SIGN = standingSignBlock("transformation_sign", Blocks.JUNGLE_SIGN, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_WALL_SIGN = wallSignBlock("transformation_wall_sign", Blocks.JUNGLE_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_HANGING_SIGN = ceilingHangingSignBlock("transformation_hanging_sign", Blocks.JUNGLE_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE);
    public static final TFRegistryObject<Block> TRANSFORMATION_WALL_HANGING_SIGN = wallHangingSignBlock("transformation_wall_hanging_sign", Blocks.JUNGLE_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE);

    public static final TFRegistryObject<Block> SORTING_SIGN = standingSignBlock("sorting_sign", Blocks.CHERRY_SIGN, net.minecraft.world.level.block.state.properties.WoodType.CHERRY);
    public static final TFRegistryObject<Block> SORTING_WALL_SIGN = wallSignBlock("sorting_wall_sign", Blocks.CHERRY_WALL_SIGN, net.minecraft.world.level.block.state.properties.WoodType.CHERRY);
    public static final TFRegistryObject<Block> SORTING_HANGING_SIGN = ceilingHangingSignBlock("sorting_hanging_sign", Blocks.CHERRY_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.CHERRY);
    public static final TFRegistryObject<Block> SORTING_WALL_HANGING_SIGN = wallHangingSignBlock("sorting_wall_hanging_sign", Blocks.CHERRY_WALL_HANGING_SIGN, net.minecraft.world.level.block.state.properties.WoodType.CHERRY);

    public static final TFRegistryObject<Block> FIERY_BLOCK = fieryBlock("fiery_block", Blocks.NETHERITE_BLOCK);
    public static final TFRegistryObject<Block> IRONWOOD_BLOCK = solidBlock("ironwood_block", Blocks.GOLD_BLOCK);
    public static final TFRegistryObject<Block> KNIGHTMETAL_BLOCK = knightmetalBlock("knightmetal_block", Blocks.IRON_BLOCK);
    public static final TFRegistryObject<Block> STEELEAF_BLOCK = solidBlock("steeleaf_block", Blocks.EMERALD_BLOCK);
    public static final TFRegistryObject<Block> RAVEN_FEATHER_BLOCK = solidBlock("raven_feather_block", Blocks.BLACK_CONCRETE);
    public static final TFRegistryObject<Block> NAGA_COURTYARD_TERRACOTTA = solidBlock("naga_courtyard_terracotta", Blocks.GREEN_TERRACOTTA);
    public static final TFRegistryObject<Block> HUGE_WATER_LILY_BLOCK = plantBlock("huge_water_lily_block", Blocks.LILY_PAD);
    public static final TFRegistryObject<Block> ROOT_STRAND_BLOCK = pillarBlock("root_strand_block", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> UNCRAFTED_ROOT_BLOCK = solidBlock("uncrafted_root_block", Blocks.DIRT);
    public static final TFRegistryObject<Block> VANISHING_BLOCK = vanishingBlock("vanishing_block", Blocks.COBBLESTONE);
    public static final TFRegistryObject<Block> AURORAL_GLASS = solidBlock("auroral_glass", Blocks.LIGHT_BLUE_STAINED_GLASS);
    public static final TFRegistryObject<Block> CASTLE_BRICK_RUNIC = solidBlock("castle_brick_runic", Blocks.CHISELED_POLISHED_BLACKSTONE);
    public static final TFRegistryObject<Block> CASTLE_BRICK_WORN = solidBlock("castle_brick_worn", Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
    public static final TFRegistryObject<Block> CASTLE_BRICK_CRACKED = solidBlock("castle_brick_cracked", Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
    public static final TFRegistryObject<Block> RAINY_CLOUD = cloudBlock("rainy_cloud", Blocks.GRAY_WOOL, Biome.Precipitation.RAIN);
    public static final TFRegistryObject<Block> SNOW_CLOUD = cloudBlock("snow_cloud", Blocks.WHITE_CONCRETE_POWDER, Biome.Precipitation.SNOW);
    public static final TFRegistryObject<Block> LICH_WALL = solidBlock("lich_wall", Blocks.STONE_BRICK_WALL);


    // -- Wood-family doors (TF original) — all use vanilla door textures via fallback
    public static final TFRegistryObject<Block> TWILIGHT_OAK_DOOR = doorBlock("twilight_oak_door", Blocks.OAK_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.OAK);
    public static final TFRegistryObject<Block> CANOPY_DOOR = doorBlock("canopy_door", Blocks.DARK_OAK_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> MINING_DOOR = doorBlock("mining_door", Blocks.BIRCH_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.BIRCH);
    public static final TFRegistryObject<Block> TIME_DOOR = doorBlock("time_door", Blocks.SPRUCE_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.SPRUCE);
    public static final TFRegistryObject<Block> SORTING_DOOR = doorBlock("sorting_door", Blocks.CHERRY_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.CHERRY);
    public static final TFRegistryObject<Block> TRANSFORMATION_DOOR_BLOCK = doorBlock("transformation_door", Blocks.JUNGLE_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.JUNGLE);
    public static final TFRegistryObject<Block> LICH_DOOR = doorBlock("lich_door", Blocks.IRON_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.IRON);

    // -- Aliases for upstream block ids referenced by advancement / loot / structure JSONs that were
    //    previously missing in this mod. Each maps to a vanilla fallback so the registry id is bound
    //    and JSON datapack loading does not reject parents (which reject children downstream).
    public static final TFRegistryObject<Block> DARK_DOOR = doorBlock("dark_door", Blocks.DARK_OAK_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> MANGROVE_DOOR = doorBlock("mangrove_door", Blocks.MANGROVE_DOOR, net.minecraft.world.level.block.state.properties.BlockSetType.MANGROVE);
    public static final TFRegistryObject<Block> CANOPY_BOOKSHELF = solidBlock("canopy_bookshelf", Blocks.BOOKSHELF);
    public static final TFRegistryObject<Block> HOLLOW_SPRUCE_LOG_VERTICAL = hollowLogVerticalBlock("hollow_spruce_log_vertical", Blocks.STRIPPED_SPRUCE_LOG);
    public static final TFRegistryObject<Block> HOLLOW_TWILIGHT_OAK_LOG_VERTICAL = hollowLogVerticalBlock("hollow_twilight_oak_log_vertical", Blocks.STRIPPED_OAK_LOG);
    public static final TFRegistryObject<Block> SORTING_LOG_CORE = sortLogCoreBlock("sorting_log_core", Blocks.STRIPPED_CHERRY_LOG);
    public static final TFRegistryObject<Block> TRANSFORMATION_LOG_CORE = transLogCoreBlock("transformation_log_core", Blocks.STRIPPED_JUNGLE_LOG);
    public static final TFRegistryObject<Block> OMINOUS_CANDLE = ominousCandleBlock("ominous_candle", Blocks.CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_WHITE_CANDLE = ominousCandleBlock("ominous_white_candle", Blocks.WHITE_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_ORANGE_CANDLE = ominousCandleBlock("ominous_orange_candle", Blocks.ORANGE_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_MAGENTA_CANDLE = ominousCandleBlock("ominous_magenta_candle", Blocks.MAGENTA_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_LIGHT_BLUE_CANDLE = ominousCandleBlock("ominous_light_blue_candle", Blocks.LIGHT_BLUE_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_YELLOW_CANDLE = ominousCandleBlock("ominous_yellow_candle", Blocks.YELLOW_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_LIME_CANDLE = ominousCandleBlock("ominous_lime_candle", Blocks.LIME_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_PINK_CANDLE = ominousCandleBlock("ominous_pink_candle", Blocks.PINK_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_GRAY_CANDLE = ominousCandleBlock("ominous_gray_candle", Blocks.GRAY_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_LIGHT_GRAY_CANDLE = ominousCandleBlock("ominous_light_gray_candle", Blocks.LIGHT_GRAY_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_CYAN_CANDLE = ominousCandleBlock("ominous_cyan_candle", Blocks.CYAN_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_PURPLE_CANDLE = ominousCandleBlock("ominous_purple_candle", Blocks.PURPLE_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_BLUE_CANDLE = ominousCandleBlock("ominous_blue_candle", Blocks.BLUE_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_BROWN_CANDLE = ominousCandleBlock("ominous_brown_candle", Blocks.BROWN_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_GREEN_CANDLE = ominousCandleBlock("ominous_green_candle", Blocks.GREEN_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_RED_CANDLE = ominousCandleBlock("ominous_red_candle", Blocks.RED_CANDLE);
    public static final TFRegistryObject<Block> OMINOUS_BLACK_CANDLE = ominousCandleBlock("ominous_black_candle", Blocks.BLACK_CANDLE);

    // -- Wood-family buttons (vanilla wood TF banister-shaped) — already had dark/mangrove/transformation
    public static final TFRegistryObject<Block> TWILIGHT_OAK_BUTTON = buttonBlock("twilight_oak_button", Blocks.OAK_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.OAK);
    public static final TFRegistryObject<Block> CANOPY_BUTTON = buttonBlock("canopy_button", Blocks.DARK_OAK_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> MINING_BUTTON = buttonBlock("mining_button", Blocks.BIRCH_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.BIRCH);
    public static final TFRegistryObject<Block> TIME_BUTTON = buttonBlock("time_button", Blocks.SPRUCE_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.SPRUCE);
    public static final TFRegistryObject<Block> SORTING_BUTTON = buttonBlock("sorting_button", Blocks.CHERRY_BUTTON, net.minecraft.world.level.block.state.properties.BlockSetType.CHERRY);
    public static final TFRegistryObject<Block> TWILIGHT_OAK_PRESSURE_PLATE = pressurePlateBlock("twilight_oak_pressure_plate", Blocks.OAK_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.OAK);
    public static final TFRegistryObject<Block> CANOPY_PRESSURE_PLATE = pressurePlateBlock("canopy_pressure_plate", Blocks.DARK_OAK_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK);
    public static final TFRegistryObject<Block> MINING_PRESSURE_PLATE = pressurePlateBlock("mining_pressure_plate", Blocks.BIRCH_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.BIRCH);
    public static final TFRegistryObject<Block> TIME_PRESSURE_PLATE = pressurePlateBlock("time_pressure_plate", Blocks.SPRUCE_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.SPRUCE);
    public static final TFRegistryObject<Block> SORTING_PRESSURE_PLATE = pressurePlateBlock("sorting_pressure_plate", Blocks.CHERRY_PRESSURE_PLATE, net.minecraft.world.level.block.state.properties.BlockSetType.CHERRY);

    // -- Castle decoration variants
    public static final TFRegistryObject<Block> CASTLE_BRICK_MOSSY = solidBlock("castle_brick_mossy", Blocks.MOSSY_COBBLESTONE);
    public static final TFRegistryObject<Block> CASTLE_BRICK_ROOF = solidBlock("castle_brick_roof", Blocks.PURPUR_BLOCK);
    public static final TFRegistryObject<Block> CASTLE_BRICK_SLAB = slabBlock("castle_brick_slab", Blocks.STONE_BRICK_SLAB);
    public static final TFRegistryObject<Block> CASTLE_PILLAR_ENCASED = pillarBlock("castle_pillar_encased", Blocks.POLISHED_BLACKSTONE);
    public static final TFRegistryObject<Block> CASTLE_PILLAR_BOLD = pillarBlock("castle_pillar_bold", Blocks.POLISHED_BLACKSTONE);
    public static final TFRegistryObject<Block> CASTLE_RUNE_BRICK_BLUE = solidBlock("castle_rune_brick_blue", Blocks.LAPIS_BLOCK);
    public static final TFRegistryObject<Block> CASTLE_RUNE_BRICK_PINK = solidBlock("castle_rune_brick_pink", Blocks.NETHER_QUARTZ_ORE);
    public static final TFRegistryObject<Block> CASTLE_RUNE_BRICK_PURPLE = solidBlock("castle_rune_brick_purple", Blocks.AMETHYST_BLOCK);
    public static final TFRegistryObject<Block> CASTLE_RUNE_BRICK_YELLOW = solidBlock("castle_rune_brick_yellow", Blocks.GILDED_BLACKSTONE);
    public static final TFRegistryObject<Block> CORONATION_CARPET = solidBlock("coronation_carpet", Blocks.RED_WOOL);

    // -- Naga / nagastone family
    public static final TFRegistryObject<Block> NAGASTONE = nagastoneBlock("nagastone", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGASTONE_HEAD = etchedNagastoneBlock("nagastone_head", Blocks.CHISELED_STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGASTONE_PILLAR_MOSSY = nagastonePillarBlock("nagastone_pillar_mossy", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGASTONE_PILLAR_WEATHERED = nagastonePillarBlock("nagastone_pillar_weathered", Blocks.CRACKED_STONE_BRICKS);
    public static final TFRegistryObject<Block> ETCHED_NAGASTONE_STAIRS = stairBlock("etched_nagastone_stairs", Blocks.STONE_BRICK_STAIRS);
    public static final TFRegistryObject<Block> NAGA_COURTYARD_MAIN = solidBlock("naga_courtyard_main", Blocks.MOSSY_STONE_BRICKS);
    public static final TFRegistryObject<Block> NAGA_COURTYARD_PILLAR = pillarBlock("naga_courtyard_pillar", Blocks.MOSSY_COBBLESTONE);

    // -- Tower wood (Dark Tower)
    public static final TFRegistryObject<Block> TOWER_WOOD = solidBlock("tower_wood", Blocks.DARK_OAK_PLANKS);
    public static final TFRegistryObject<Block> TOWER_WOOD_CRACKED = solidBlock("tower_wood_cracked", Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> TOWER_WOOD_ENCASED = solidBlock("tower_wood_encased", Blocks.DARK_OAK_WOOD);
    public static final TFRegistryObject<Block> TOWER_WOOD_INFESTED = solidBlock("tower_wood_infested", Blocks.INFESTED_STONE_BRICKS);
    public static final TFRegistryObject<Block> TOWER_WOOD_MOSSY = solidBlock("tower_wood_mossy", Blocks.MOSSY_COBBLESTONE);

    // -- Mushglooms
    public static final TFRegistryObject<Block> HUGE_MUSHGLOOM_CAP = sixWayBlock("huge_mushgloom_cap", Blocks.RED_MUSHROOM_BLOCK);
    public static final TFRegistryObject<Block> HUGE_MUSHGLOOM_STEM = hugeMushroomBlock("huge_mushgloom_stem", Blocks.MUSHROOM_STEM);
    public static final TFRegistryObject<Block> HUGE_STALK = pillarBlock("huge_stalk", Blocks.MUSHROOM_STEM);

    // -- Fire jet / smoker
    public static final TFRegistryObject<Block> FIRE_JET = fireJetBlock("fire_jet", Blocks.MAGMA_BLOCK);
    public static final TFRegistryObject<Block> SMOKER = tfSmokerBlock("smoker", Blocks.SMOKER);
    public static final TFRegistryObject<Block> ENCASED_FIRE_JET = encasedFireJetBlock("encased_fire_jet", Blocks.MAGMA_BLOCK);
    public static final TFRegistryObject<Block> ENCASED_SMOKER = encasedSmokerBlock("encased_smoker", Blocks.SMOKER);

    // -- Spiral / twisted stone
    public static final TFRegistryObject<Block> SPIRAL_BRICKS = spiralBrickBlock("spiral_bricks", Blocks.STONE_BRICKS);
    public static final TFRegistryObject<Block> STONE_TWIST_THIN = pillarBlock("stone_twist_thin", Blocks.POLISHED_DEEPSLATE);
    public static final TFRegistryObject<Block> STONE_TWIST_THICK = pillarBlock("stone_twist_thick", Blocks.POLISHED_DEEPSLATE);
    public static final TFRegistryObject<Block> TWISTED_STONE_PILLAR = wallPillarBlock("twisted_stone_pillar", Blocks.POLISHED_DEEPSLATE, 12.0D, 16.0D);

    // -- Terrorcotta variants (Lich Tower decoration)
    public static final TFRegistryObject<Block> TERRORCOTTA_ARCS = pillarBlock("terrorcotta_arcs", Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
    public static final TFRegistryObject<Block> TERRORCOTTA_CURVES = glazedTerracottaBlock("terrorcotta_curves", Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
    public static final TFRegistryObject<Block> TERRORCOTTA_LINES = binaryRotatedBlock("terrorcotta_lines", Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);

    // -- Misc
    public static final TFRegistryObject<Block> AURORAL_GLASS_PANE = solidBlock("auroral_glass_pane", Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
    public static final TFRegistryObject<Block> WROUGHT_IRON_FENCE = wroughtIronFenceBlock("wrought_iron_fence", Blocks.IRON_BARS);
    public static final TFRegistryObject<Block> IRON_LADDER = ironLadderBlock("iron_ladder", Blocks.LADDER);
    public static final TFRegistryObject<Block> KEEPSAKE_CASKET = keepsakeCasketBlock("keepsake_casket");
    public static final TFRegistryObject<Block> MOONWORM = moonwormBlock("moonworm");
    public static final TFRegistryObject<Block> TRELLIS_ROOTS = pillarBlock("tree_roots", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> MANGROVE_ROOT = pillarBlock("mangrove_root", Blocks.MANGROVE_ROOTS);
    public static final TFRegistryObject<Block> VANILLA_ROOTS = solidBlock("vanilla_roots", Blocks.MANGROVE_ROOTS);
    public static final TFRegistryObject<Block> FIDDLEHEAD = fiddleheadBlock("fiddlehead", Blocks.FERN);
    public static final TFRegistryObject<Block> TRELLIS_TROLLBER = trollRootBlock("trollber", Blocks.SWEET_BERRY_BUSH);
    public static final TFRegistryObject<Block> TROLLBER = TRELLIS_TROLLBER;
    public static final TFRegistryObject<Block> TRELLIS_TROLLVIDR_PLANT = plantBlock("trollvidr_plant", Blocks.HANGING_ROOTS);
    public static final TFRegistryObject<Block> TF_CHEST = chestBlock("tf_chest", Blocks.CHEST);
    public static final TFRegistryObject<Block> SKULL_CANDLE = SKELETON_SKULL_CANDLE;
    public static final TFRegistryObject<Block> TWILIGHT_PORTAL = portalBlock("twilight_portal");
    public static final TFRegistryObject<Block> TWILIGHT_PORTAL_MINIATURE_STRUCTURE = miniatureStructureBlock("twilight_portal_miniature_structure", Blocks.SKELETON_SKULL);

    // -- Generic spawner sentinels (Mythic uses real BOSS spawner blocks above; these are TF NBT references)
    public static final TFRegistryObject<Block> NAGA_SPAWNER = spawnerBlock("naga_spawner", Blocks.SPAWNER);
    public static final TFRegistryObject<Block> LICH_SPAWNER = spawnerBlock("lich_spawner", Blocks.SPAWNER);
    public static final TFRegistryObject<Block> SINISTER_SPAWNER = sinisterSpawnerBlock("sinister_spawner");

    // -- Castle door colour variants (Q12 promoted blue/pink/violet/yellow already; add purple alias if missing)
    // (purple alias maps to violet door — substitution stays the proper alias)

    private TFBlocks() {
    }

    private static TFRegistryObject<Block> block(Block block) {
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> tfBlock(String path, BlockBehaviour.Properties properties, BlockState templateState) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexBlock(properties, templateState));
        registerBlockItem(path, block, templateState.getBlock());
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> ambientJarBlock(String path, BlockBehaviour.Properties properties, BlockState templateState,
                                                            net.minecraft.core.particles.ParticleOptions particle, int particleCount, double particleSpread,
                                                            net.minecraft.sounds.SoundEvent ambientSound, float soundVolume, float soundPitch) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new AmbientJarBlock(properties, templateState, particle, particleCount, particleSpread, ambientSound, soundVolume, soundPitch));
        registerBlockItem(path, block, templateState.getBlock());
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> experiment115Block(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.Experiment115Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).randomTicks()));
        registerBlockItem(path, block, Blocks.CAKE);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> masonJarBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new MasonJarBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fireflyJarBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.FireflyJarBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> 15)
                        .noOcclusion()
                        .noTerrainParticles()
                        .randomTicks()
                        .sound(SoundType.GLASS)
                        .strength(0.3F, 3.0F)));
        registerBlockItem(path, block, Blocks.GLOWSTONE);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> cicadaJarBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.CicadaJarBlock(BlockBehaviour.Properties.of()
                        .noOcclusion()
                        .noTerrainParticles()
                        .randomTicks()
                        .sound(SoundType.GLASS)
                        .strength(0.3F, 3.0F)));
        registerBlockItem(path, block, Blocks.GLASS);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> solidBlock(String path, Block fallback) {
        return tfBlock(path, genericProperties(fallback), fallback.defaultBlockState());
    }

    private static TFRegistryObject<Block> arcticFurBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.ArcticFurBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> mossPatchBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.MossPatchBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> mayappleBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.MayappleBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fiddleheadBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.FiddleheadBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> trollRootBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TrollRootBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> liverootBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.LiverootBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> wispyCloudBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.WispyCloudBlock(BlockBehaviour.Properties.of()
                .instrument(NoteBlockInstrument.HAT)
                .mapColor(MapColor.SNOW)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .replaceable()
                .sound(SoundType.WOOL)
                .strength(0.3F, 0.0F)
                .forceSolidOff(), Biome.Precipitation.NONE));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> cloudBlock(String path, Block fallback, Biome.Precipitation precipitation) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CloudBlock(BlockBehaviour.Properties.of()
                .instrument(NoteBlockInstrument.HAT)
                .mapColor(MapColor.ICE)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.WOOL)
                .strength(0.8F, 0.0F)
                .randomTicks(), precipitation));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> ominousFireBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.OminousFireBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission().lightLevel(state -> 10)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> mazeSlimeBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.MazeSlimeBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> thornRoseBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.ThornRoseBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> etchedNagastoneBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.EtchedNagastoneBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> nagastoneBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.NagastoneBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> transformationLeavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TransformationLeavesBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> uncraftingTableBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new UncraftingTableBlock(BlockBehaviour.Properties.of()
                        .ignitedByLava()
                        .mapColor(MapColor.FIRE)
                        .sound(SoundType.WOOD)
                        .strength(2.5F)));
        registerBlockItem(path, block, Blocks.CRAFTING_TABLE);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> plantBlock(String path, Block fallback) {
        return tfBlock(path, genericProperties(fallback).noCollission(), fallback.defaultBlockState());
    }

    /** 1:1 upstream {@link twilightforest.block.MushgloomBlock} — MushroomBlock subclass with
     * uberous-soil survival rule and a custom 12×8×12 voxel shape. */
    private static TFRegistryObject<Block> mushgloomBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.MushgloomBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.RootStrandBlock} — TFPlantBlock that bonemeals
     * downward by extending its strand toward the next open block. */
    private static TFRegistryObject<Block> rootStrandBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.RootStrandBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.UberousSoilBlock} — soil that auto-grows
     * BonemealableBlock plants directly above and self-spreads via bonemeal. */
    private static TFRegistryObject<Block> uberousSoilBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.UberousSoilBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.UnripeTorchClusterBlock} — TrollRoot subclass
     * that ripens to TROLLBER once light level reaches 6. */
    private static TFRegistryObject<Block> unripeTorchClusterBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.UnripeTorchClusterBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission().randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.MiniatureStructureBlock} — invisible
     * collisionless host block whose only render contribution is via the paired BlockEntity
     * renderer. Replaces the older CodexDirectionalBlock stub. */
    private static TFRegistryObject<Block> miniatureStructureBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission().noOcclusion()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.SpecialFlowerPotBlock} — emptyable flower pot
     * variant. Used for POTTED_MUSHGLOOM. */
    private static TFRegistryObject<Block> specialFlowerPotBlock(String path, java.util.function.Supplier<? extends Block> flower, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SpecialFlowerPotBlock(() -> (net.minecraft.world.level.block.FlowerPotBlock) Blocks.FLOWER_POT, flower, BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> leavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexLeavesBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> pillarBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexRotatedPillarBlock(genericProperties(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> mineLogCoreBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.MineLogCoreBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> timeLogCoreBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TimeLogCoreBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> sortLogCoreBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.SortLogCoreBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> transLogCoreBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TransLogCoreBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> nagastonePillarBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexNagastonePillarBlock(genericProperties(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> directionalBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexDirectionalBlock(genericProperties(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> activeBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexActiveBlock(genericProperties(fallback).lightLevel(state -> state.getValue(CodexActiveBlock.ACTIVE) ? 15 : 0), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static BlockBehaviour.Properties genericProperties(Block fallback) {
        return BlockBehaviour.Properties.of()
                .strength(fallback.defaultDestroyTime(), fallback.getExplosionResistance())
                .sound(fallback.defaultBlockState().getSoundType());
    }

    private static TFRegistryObject<Block> lockedBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexLockedBlock(genericProperties(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> lockedVanishingBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.LockedVanishingBlock(genericProperties(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> vanishingBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.VanishingBlock(genericProperties(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> stairBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexStairBlock(fallback.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fenceBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexFenceBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.BanisterBlock} (FACING/SHAPE/EXTENDED/WATERLOGGED). */
    private static TFRegistryObject<Block> banisterBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.BanisterBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.IronLadderBlock} (LadderBlock + LEFT/RIGHT). */
    private static TFRegistryObject<Block> ironLadderBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.IronLadderBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.WroughtIronFenceBlock} (POST + 4-side enums). */
    private static TFRegistryObject<Block> wroughtIronFenceBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.WroughtIronFenceBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.WallPillarBlock} (axis + 6-direction connect). */
    private static TFRegistryObject<Block> wallPillarBlock(String path, Block fallback, double width, double height) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.WallPillarBlock(BlockBehaviour.Properties.ofFullCopy(fallback), width, height));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Vanilla {@link net.minecraft.world.level.block.HugeMushroomBlock} — upstream uses it directly for huge_mushgloom_stem. */
    private static TFRegistryObject<Block> hugeMushroomBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new net.minecraft.world.level.block.HugeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Vanilla {@link net.minecraft.world.level.block.IronBarsBlock} — upstream uses it directly for canopy_window_pane. */
    private static TFRegistryObject<Block> ironBarsBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new net.minecraft.world.level.block.IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.DryingRackBlock} (FACING + WATERLOGGED + BlockEntity ticking). */
    private static TFRegistryObject<Block> dryingRackBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.DryingRackBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.ChiseledCanopyShelfBlock} with SPAWNER + death-tome BlockEntity logic. */
    private static TFRegistryObject<Block> chiseledCanopyShelfBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.ChiseledCanopyShelfBlock(BlockBehaviour.Properties.of()
                        .ignitedByLava()
                        .instrument(NoteBlockInstrument.BASS)
                        .mapColor(MapColor.COLOR_BROWN)
                        .sound(SoundType.CHISELED_BOOKSHELF)
                        .strength(2.5F)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> snowLayerBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexSnowLayerBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fallenLeavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.FallenLeavesBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .replaceable()
                        .noCollission()
                        .noOcclusion()
                        .instabreak()
                        .sound(SoundType.AZALEA_LEAVES)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> sixWayBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexSixWayBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> thornsBlock(String path, Block fallback) {
        // 1:1 upstream ThornsBlock — axis + 6-way connect + waterlogged + thorn-burst-on-break +
        // entity damage. Replaces the older CodexThornsBlock stub.
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.ThornsBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.BurntThornsBlock} — ThornsBlock subclass that
     * dissolves living/projectile entities on contact (no damage), returns null path-type, and
     * preserves waterlog state on player-destroy. */
    private static TFRegistryObject<Block> burntThornsBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.BurntThornsBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.FieryBlock} — full-bright netherite-derived block
     * that damages entities on step (unless fire-immune or wearing fiery boots). */
    private static TFRegistryObject<Block> fieryBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.FieryBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.HedgeBlock} — cactus-damage-on-contact + tick-based
     * "swing-at-distance" prick. */
    private static TFRegistryObject<Block> hedgeBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.HedgeBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.AuroraBrickBlock} — used for {@code aurora_block};
     * faster destroy progress for players holding the {@code progress_glacier} advancement. */
    private static TFRegistryObject<Block> auroraBrickBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.AuroraBrickBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.AuroralizedGlassBlock} — beacon-beam tints by
     * ripple-fractal noise sampled at the column. */
    private static TFRegistryObject<Block> auroralizedGlassBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.AuroralizedGlassBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.KnightmetalBlock} — waterloggable spiked iron block
     * that deals 4 damage on contact (KNIGHTMETAL damage type). */
    private static TFRegistryObject<Block> knightmetalBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.KnightmetalBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.DarkLeavesBlock} — LeavesBlock subclass with full
     * block support shape and full opacity (used for Dark Tower canopy). */
    private static TFRegistryObject<Block> darkLeavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.DarkLeavesBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.HardenedDarkLeavesBlock} — Dark Tower exterior
     * block whose pick-block returns the soft DARK_LEAVES item. */
    private static TFRegistryObject<Block> hardenedDarkLeavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.HardenedDarkLeavesBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.ForceFieldBlock} — 6-way pipe-style connect with
     * corner brackets when 2 perpendicular sides connect to a sturdy face. Waterloggable. */
    private static TFRegistryObject<Block> forceFieldBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.ForceFieldBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.TFSmokerBlock} — decorative smoking vent
     * (canopy/dwarves' kitchens) backed by TFSmokerBlockEntity for the client-side smoke
     * particle ticker. */
    private static TFRegistryObject<Block> tfSmokerBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TFSmokerBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.GRASS)
                .sound(SoundType.GRASS)
                .strength(1.5F, 6.0F)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.EncasedSmokerBlock} — TFSmokerBlock subclass
     * with redstone-driven ACTIVE toggle (plays smoker_start cue on transitions). */
    private static TFRegistryObject<Block> encasedSmokerBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.EncasedSmokerBlock(BlockBehaviour.Properties.of()
                .ignitedByLava()
                .mapColor(MapColor.SAND)
                .requiresCorrectToolForDrops()
                .sound(SoundType.WOOD)
                .strength(1.5F, 6.0F)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.GiantLeavesBlock} — GiantBlock subclass with
     * empty support shape. */
    private static TFRegistryObject<Block> giantBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.GiantBlock(giantProperties(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> giantLeavesBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.GiantLeavesBlock(giantProperties(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static BlockBehaviour.Properties giantProperties(Block fallback) {
        return BlockBehaviour.Properties.ofFullCopy(fallback).mapColor(fallback.defaultMapColor());
    }

    /** 1:1 upstream {@link twilightforest.block.HugeWaterLilyBlock} — vanilla WaterlilyBlock
     * subclass with an inflated voxel shape. */
    private static TFRegistryObject<Block> hugeWaterLilyBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.HugeWaterLilyBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.StrongholdShieldBlock} — DirectionalBlock with
     * fast-mine front face. */
    private static TFRegistryObject<Block> strongholdShieldBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.StrongholdShieldBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.InfestedTowerwoodBlock} — spawns a
     * TowerwoodBorer on break (unless tool enchantment prevents it). */
    private static TFRegistryObject<Block> infestedTowerwoodBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.InfestedTowerwoodBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.TrollsteinnBlock} — purple ore-like block with
     * 6 directional lit booleans, brightness-tracking comparator output, and dim-face sparkle. */
    private static TFRegistryObject<Block> trollsteinnBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TrollsteinnBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.MangroveSaplingBlock} — vanilla SaplingBlock
     * subclass with WATERLOGGED state (placeable in water; reverts when water removed). */
    private static TFRegistryObject<Block> mangroveSaplingBlock(String path, Block fallback, net.minecraft.world.level.block.grower.TreeGrower tree) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.MangroveSaplingBlock(tree, BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.SpiralBrickBlock} (axis + diagonal + waterlogged). */
    private static TFRegistryObject<Block> spiralBrickBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SpiralBrickBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.BinaryRotatedBlock} (rotated boolean per horizontal axis). */
    private static TFRegistryObject<Block> binaryRotatedBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.BinaryRotatedBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Vanilla {@link net.minecraft.world.level.block.GlazedTerracottaBlock} (facing horizontal). Upstream uses it directly for terrorcotta_curves. */
    private static TFRegistryObject<Block> glazedTerracottaBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new net.minecraft.world.level.block.GlazedTerracottaBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.TrophyPedestalBlock} (active + waterlogged, neighbour-trigger activation). */
    private static TFRegistryObject<Block> trophyPedestalBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.TrophyPedestalBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.TrophyBlock} — floor-mounted boss trophy with
     * per-variant comparator output, ROTATION_16 + POWERED state, and TrophyBlockEntity ticker
     * (drives UR_GHAST_TROPHY animation). Properties{@code .of().instabreak()} matches upstream. */
    private static TFRegistryObject<Block> trophyBlock(String path, twilightforest.enums.BossVariant variant, int comparatorValue) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.TrophyBlock(variant, comparatorValue, BlockBehaviour.Properties.of().instabreak()));
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.TrophyWallBlock} — wall-mounted boss trophy with
     * HORIZONTAL_FACING + POWERED state. Comparator slot is unused (wall trophies pass 0).
     * Properties{@code .of().instabreak()} matches upstream. */
    private static TFRegistryObject<Block> trophyWallBlock(String path, twilightforest.enums.BossVariant variant, TFRegistryObject<Block> floorBlock) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.TrophyWallBlock(variant, BlockBehaviour.Properties.of().instabreak()));
        String itemPath = path.replace("_wall_trophy", "_trophy");
        Item blockItem = new TrophyItem(floorBlock.get(), block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(itemPath), blockItem);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> wallSkullCandleBlock(String path, SkullBlock.Type type, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.WallSkullCandleBlock(type, BlockBehaviour.Properties.ofFullCopy(fallback)
                        .lightLevel(twilightforest.block.AbstractSkullCandleBlock::lightForState)));
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> skullCandleBlock(String path, SkullBlock.Type type, Block fallback, TFRegistryObject<Block> wallBlock) {
        twilightforest.block.AbstractSkullCandleBlock block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SkullCandleBlock(type, BlockBehaviour.Properties.ofFullCopy(fallback)
                        .lightLevel(twilightforest.block.AbstractSkullCandleBlock::lightForState)));
        Item blockItem = new twilightforest.item.SkullCandleItem(block, (twilightforest.block.AbstractSkullCandleBlock) wallBlock.get(),
                new Item.Properties().rarity(Rarity.UNCOMMON));
        Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), blockItem);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.FireJetBlock} (4-state STATE EnumProperty + lava-fed jet ticker). */
    private static TFRegistryObject<Block> fireJetBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.FireJetBlock(BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.EncasedFireJetBlock} (redstone-triggered FireJetBlock subclass). */
    private static TFRegistryObject<Block> encasedFireJetBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.EncasedFireJetBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** 1:1 upstream {@link twilightforest.block.CandelabraBlock} (facing + on_wall + lighting + 3 has_candle slots + waterlogged). */
    private static TFRegistryObject<Block> candelabraBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.CandelabraBlock(BlockBehaviour.Properties.ofFullCopy(fallback)
                        .lightLevel(twilightforest.block.CandelabraBlock::lightForState)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> carminiteReactorBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.CarminiteReactorBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> state.getValue(twilightforest.block.CarminiteReactorBlock.ACTIVE) ? 15 : 0)
                        .mapColor(MapColor.SAND)
                        .pushReaction(PushReaction.BLOCK)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.WOOD)
                        .strength(10.0F, 6.0F)));
        registerBlockItem(path, block, Blocks.REDSTONE_LAMP);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> reappearingBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.ReappearingBlock(genericProperties(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> carminiteBuilderBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.BuilderBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> state.getValue(twilightforest.block.BuilderBlock.STATE) == twilightforest.enums.TowerDeviceVariant.BUILDER_ACTIVE ? 4 : 0)
                        .mapColor(MapColor.SAND)
                        .pushReaction(PushReaction.BLOCK)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.WOOD)
                        .strength(10.0F, 6.0F)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> antibuilderBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.AntibuilderBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> 10)
                        .noLootTable()
                        .pushReaction(PushReaction.BLOCK)
                        .mapColor(MapColor.SAND)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.WOOD)
                        .strength(10.0F, 6.0F)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> translucentBuiltBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.TranslucentBuiltBlock(BlockBehaviour.Properties.of()
                        .noLootTable()
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
                        .sound(SoundType.WOOD)
                        .strength(50.0F, 2000.0F)));
        registerBlockItem(path, block, Blocks.GLOWSTONE);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> ghastTrapBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.GhastTrapBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> state.getValue(twilightforest.block.GhastTrapBlock.ACTIVE) ? 15 : 0)
                        .mapColor(MapColor.SAND)
                        .pushReaction(PushReaction.BLOCK)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.WOOD)
                        .strength(10.0F, 6.0F)));
        registerBlockItem(path, block, Blocks.REDSTONE_LAMP);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> growingBeanstalkBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.GrowingBeanstalkBlock(BlockBehaviour.Properties.of()
                        .noCollission()
                        .noLootTable()
                        .noOcclusion()
                        .noTerrainParticles()
                        .strength(-1.0F, 6000000.0F)));
        registerBlockItem(path, block, Blocks.OAK_LOG);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> cinderFurnaceBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.CinderFurnaceBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .requiresCorrectToolForDrops()
                        .strength(7.0F)
                        .lightLevel(state -> 15)));
        registerBlockItem(path, block, Blocks.FURNACE);
        return new TFRegistryObject<>(block);
    }


    private static TFRegistryObject<Block> berryBushBlock(String path, Block fallback) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(fallback).randomTicks();
        Block block = switch (path) {
            case "blightberry_bush", "duskberry_bush", "skyberry_bush", "stingberry_bush" ->
                    Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.DarkTowerBerryBushBlock(berryLootTable(path), properties));
            default ->
                    Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.BerryBushBlock(berryLootTable(path), properties));
        };
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> berryLootTable(String path) {
        return switch (path) {
            case "blackberry_bush" -> TFLootTables.BLACKBERRY_BUSH_BERRIES;
            case "blueberry_bush" -> TFLootTables.BLUEBERRY_BUSH_BERRIES;
            case "raspberry_bush" -> TFLootTables.RASPBERRY_BUSH_BERRIES;
            case "maloberry_bush" -> TFLootTables.MALOBERRY_BUSH_BERRIES;
            case "blightberry_bush" -> TFLootTables.BLIGHTBERRY_BUSH_BERRIES;
            case "duskberry_bush" -> TFLootTables.DUSKBERRY_BUSH_BERRIES;
            case "skyberry_bush" -> TFLootTables.SKYBERRY_BUSH_BERRIES;
            case "stingberry_bush" -> TFLootTables.STINGBERRY_BUSH_BERRIES;
            default -> throw new IllegalArgumentException("Unknown Twilight berry bush: " + path);
        };
    }

    /** 1:1 upstream {@link twilightforest.block.TorchberryPlantBlock} — TFPlantBlock with
     * a {@code HAS_BERRIES} state that bonemeal toggles, dropping a torchberry on right-click.
     * Replaces the older CodexTorchberryPlantBlock stub. */
    private static TFRegistryObject<Block> torchberryPlantBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.TorchberryPlantBlock(BlockBehaviour.Properties.ofFullCopy(fallback).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> hugeLilyPadBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexHugeLilyPadBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> castleDoorBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CastleDoorBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> chestBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexChestBlock(BlockBehaviour.Properties.ofFullCopy(fallback), () -> BlockEntityType.CHEST, fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> skullChestBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SkullChestBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> state.getValue(twilightforest.enums.BlockLoggingEnum.MULTILOGGED) == twilightforest.enums.BlockLoggingEnum.LAVA ? 15 : 0)
                        .mapColor(MapColor.COLOR_LIGHT_GRAY)
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.NETHERITE_BLOCK)
                        .strength(3.0F, 100.0F)));
        registerBlockItem(path, block, Blocks.CHEST);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> keepsakeCasketBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.KeepsakeCasketBlock(BlockBehaviour.Properties.of()
                        .lightLevel(state -> state.getValue(twilightforest.enums.BlockLoggingEnum.MULTILOGGED) == twilightforest.enums.BlockLoggingEnum.LAVA ? 15 : 0)
                        .mapColor(MapColor.COLOR_BLACK)
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.NETHERITE_BLOCK)
                        .strength(5.0F, 1200.0F)));
        registerBlockItem(path, block, Blocks.RESPAWN_ANCHOR);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> saplingBlock(String path, Block fallback, TreeGrower treeGrower) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexSaplingBlock(treeGrower, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> hollowOakSaplingBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexHollowOakSaplingBlock(TreeGrower.OAK, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> spawnerBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexSpawnerBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> bossSpawnerBlock(String path, twilightforest.enums.BossVariant variant) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.BossSpawnerBlock(variant, BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .noLootTable()
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        .strength(-1.0F, 3600000.8F)));
        registerBlockItem(path, block, Blocks.SPAWNER);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> sinisterSpawnerBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SinisterSpawnerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER).noLootTable()));
        registerBlockItem(path, block, Blocks.SPAWNER);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link net.minecraft.world.level.block.CandleBlock} with candles (1..4) + lit properties — required so loot tables that test {@code block_state_property: candles} resolve. */
    private static TFRegistryObject<Block> candleBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new net.minecraft.world.level.block.CandleBlock(BlockBehaviour.Properties.ofFullCopy(fallback)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> ominousCandleBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.OminousCandleBlock(fallback, BlockBehaviour.Properties.of()
                        .mapColor(fallback.defaultMapColor())
                        .noOcclusion()
                        .strength(0.1F)
                        .sound(SoundType.CANDLE)
                        .lightLevel(state -> 2 * state.getValue(twilightforest.block.OminousCandleBlock.CANDLES))
                        .pushReaction(PushReaction.DESTROY)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fireflyBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.FireflyBlock(BlockBehaviour.Properties.of()
                        .instabreak()
                        .lightLevel(state -> 15)
                        .noCollission()
                        .noTerrainParticles()
                        .pushReaction(PushReaction.DESTROY)
                        .sound(SoundType.SLIME_BLOCK)));
        registerBlockItem(path, block, Blocks.LIGHT);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> cicadaBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.CicadaBlock(BlockBehaviour.Properties.of()
                        .instabreak()
                        .noCollission()
                        .noTerrainParticles()
                        .pushReaction(PushReaction.DESTROY)
                        .sound(SoundType.SLIME_BLOCK)));
        registerBlockItem(path, block, Blocks.LIGHT);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> moonwormBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.MoonwormBlock(BlockBehaviour.Properties.of()
                        .forceSolidOff()
                        .instabreak()
                        .lightLevel(state -> 14)
                        .noCollission()
                        .noTerrainParticles()
                        .pushReaction(PushReaction.DESTROY)
                        .sound(SoundType.SLIME_BLOCK)));
        registerBlockItem(path, block, Blocks.GLOW_LICHEN);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.SliderBlock} with timed SlideBlock entity spawning. */
    private static TFRegistryObject<Block> sliderBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.SliderBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .noLootTable()
                        .noOcclusion()
                        .randomTicks()
                        .strength(2.0F, 10.0F)));
        registerBlockItem(path, block, Blocks.PISTON);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.BrazierBlock} with HALF state property. */
    private static TFRegistryObject<Block> brazierBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.BrazierBlock(BlockBehaviour.Properties.of()
                        .sound(SoundType.WOOD)
                        .lightLevel(state -> state.getValue(twilightforest.block.BrazierBlock.HALF) == DoubleBlockHalf.UPPER ? state.getValue(twilightforest.block.BrazierBlock.LIGHT).getLight() : 0)
                        .pushReaction(PushReaction.DESTROY)));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> reactorDebrisBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.ReactorDebrisBlock(BlockBehaviour.Properties.of()
                        .noLootTable()
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
                        .sound(SoundType.ANCIENT_DEBRIS)
                        .strength(0.3F, 2000.0F)));
        registerBlockItem(path, block, Blocks.GRAVEL);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.RopeBlock} with X/Y/Z + WATERLOGGED state properties. */
    private static TFRegistryObject<Block> ropeBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.RopeBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(net.minecraft.world.level.block.SoundType.WOOL).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.RedThreadBlock} with N/S/E/W/U/D state properties. */
    private static TFRegistryObject<Block> redThreadBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.RedThreadBlock(BlockBehaviour.Properties.of().strength(0.1F).sound(net.minecraft.world.level.block.SoundType.WOOL).noCollission()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.OreBerryBlock} with AGE state property. */
    private static TFRegistryObject<Block> oreBerryBlock(String path, Block fallback) {
        net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> berryTable =
                net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.LOOT_TABLE,
                        TwilightForestMod.prefix(path.equals("essence_oreberry")
                                ? "blocks/essence_berry_bush_berries"
                                : "blocks/" + path + "_bush_berries"));
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.OreBerryBlock(path.equals("essence_oreberry"), berryTable,
                        BlockBehaviour.Properties.of()
                                .sound(SoundType.METAL)
                                .destroyTime(0.4F)
                                .randomTicks()
                                .dynamicShape()
                                .noOcclusion()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.FireflySpawnerBlock} wired through AbstractParticleSpawnerBlock. */
    private static TFRegistryObject<Block> particleSpawnerBlock(String path) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.FireflySpawnerBlock(BlockBehaviour.Properties.of().noCollission().lightLevel(s -> 14).noOcclusion().strength(0.0F)));
        registerBlockItem(path, block, Blocks.LIGHT);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.HorizontalHollowLogBlock} with VARIANT + HORIZONTAL_AXIS state properties.
     * Uses {@link BlockBehaviour.Properties#of()} (not {@code ofFullCopy}) so the Properties does not carry
     * the fallback's RotatedPillarBlock {@code AXIS} property into our state definition; we then manually
     * copy the fallback's strength/sound/etc. via the property setters. */
    private static TFRegistryObject<Block> hollowLogHorizontalBlock(String path, Block fallback) {
        BlockBehaviour.Properties props = hollowLogProps(fallback);
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.HorizontalHollowLogBlock(props));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.ClimbableHollowLogBlock} with VARIANT + FACING state properties.
     * The climbable variant needs a Supplier&lt;Block&gt; pointing at the matching vertical hollow log
     * (used at shears-interaction to revert). Codex's hollow-log naming is fully consistent
     * ({@code hollow_X_log_climbable} ↔ {@code hollow_X_log_vertical}), so the supplier is derived
     * from the path string and resolves lazily via {@link BuiltInRegistries#BLOCK}. */
    private static TFRegistryObject<Block> hollowLogClimbableBlock(String path, Block fallback) {
        BlockBehaviour.Properties props = hollowLogProps(fallback);
        net.minecraft.resources.ResourceLocation verticalId = TwilightForestMod.prefix(path.replaceFirst("_climbable$", "_vertical"));
        java.util.function.Supplier<Block> verticalSupplier = () -> BuiltInRegistries.BLOCK.get(verticalId);
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.ClimbableHollowLogBlock(verticalSupplier, props));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Real {@link twilightforest.block.VerticalHollowLogBlock} with WATERLOGGED state property and
     * a Supplier&lt;Block&gt; back-link to the matching climbable hollow log (used at vine/ladder
     * right-click to convert). Same lazy-resolve trick as {@link #hollowLogClimbableBlock}. */
    private static TFRegistryObject<Block> hollowLogVerticalBlock(String path, Block fallback) {
        BlockBehaviour.Properties props = hollowLogProps(fallback);
        net.minecraft.resources.ResourceLocation climbableId = TwilightForestMod.prefix(path.replaceFirst("_vertical$", "_climbable"));
        java.util.function.Supplier<Block> climbableSupplier = () -> BuiltInRegistries.BLOCK.get(climbableId);
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path),
                new twilightforest.block.VerticalHollowLogBlock(props, climbableSupplier));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    /** Stripped-down properties for hollow-log blocks — replicates the wood family's hardness/sound/material
     * without dragging in the fallback's RotatedPillarBlock-only AXIS state. */
    private static BlockBehaviour.Properties hollowLogProps(Block fallback) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .strength(2.0F)
                .sound(net.minecraft.world.level.block.SoundType.WOOD)
                .ignitedByLava();
        return props;
    }

    /** Real TFPortalBlock with DISALLOW_RETURN BlockStateProperty + dimension-transition logic. */
    private static TFRegistryObject<Block> portalBlock(String path) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL)
                .lightLevel(state -> 11)
                .strength(-1.0F, 3600000.0F)
                .noLootTable();
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.TFPortalBlock(props));
        registerBlockItem(path, block, Blocks.NETHER_PORTAL);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> slabBlock(String path, Block fallback) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexSlabBlock(BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> fenceGateBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.WoodType woodType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexFenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> trapDoorBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.BlockSetType blockSetType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new CodexTrapDoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> buttonBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.BlockSetType blockSetType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexButtonBlock(blockSetType, 30, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> pressurePlateBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.BlockSetType blockSetType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexPressurePlateBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> standingSignBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.WoodType woodType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexStandingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        // Sign block items use the standing-sign block; wall-sign block does not get its own item.
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> wallSignBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.WoodType woodType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexWallSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> ceilingHangingSignBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.WoodType woodType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexCeilingHangingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> wallHangingSignBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.WoodType woodType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexWallHangingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        return new TFRegistryObject<>(block);
    }

    private static TFRegistryObject<Block> doorBlock(String path, Block fallback, net.minecraft.world.level.block.state.properties.BlockSetType blockSetType) {
        Block block = Registry.register(BuiltInRegistries.BLOCK, TwilightForestMod.prefix(path), new twilightforest.block.CodexDoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(fallback), fallback.defaultBlockState()));
        registerBlockItem(path, block, fallback);
        return new TFRegistryObject<>(block);
    }

    private static void registerBlockItem(String path, Block block, Block fallback) {
        Item blockItem = switch (path) {
            case "keepsake_casket" -> new twilightforest.item.KeepsakeCasketItem(block, new net.minecraft.world.item.Item.Properties().rarity(net.minecraft.world.item.Rarity.UNCOMMON).component(twilightforest.init.TFDataComponents.CASKET_DAMAGE, 0));
            case "experiment_115" -> new twilightforest.item.Experiment115Item(block, new net.minecraft.world.item.Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).build()));
            case "mason_jar" -> new twilightforest.item.JarItem.MasonJarItem((twilightforest.block.JarBlock) block, new net.minecraft.world.item.Item.Properties());
            case "firefly_jar", "cicada_jar" -> new twilightforest.item.JarItem((twilightforest.block.JarBlock) block, new net.minecraft.world.item.Item.Properties());
            case "huge_lily_pad" -> new twilightforest.item.HugeLilyPadItem(block, new net.minecraft.world.item.Item.Properties());
            case "rope" -> new twilightforest.item.RopeItem(block, new net.minecraft.world.item.Item.Properties());
            case "wrought_iron_fence" -> new twilightforest.item.WroughtIronFenceItem(block, new net.minecraft.world.item.Item.Properties());
            default -> new net.minecraft.world.item.BlockItem(block, new net.minecraft.world.item.Item.Properties());
        };
        Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix(path), blockItem);
    }

    private static BlockState lightBlockState(int level) {
        return Blocks.LIGHT.defaultBlockState()
                .setValue(LightBlock.LEVEL, level)
                .setValue(BlockStateProperties.WATERLOGGED, false);
    }

}
