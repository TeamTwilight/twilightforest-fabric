package twilightforest.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import twilightforest.TFMain;
import twilightforest.block.*;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.enums.BossVariant;
import twilightforest.enums.FireJetVariant;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.loot.TFLootTables;
import twilightforest.util.woods.TFWoodTypes;
import twilightforest.world.components.feature.trees.growers.TFTreeGrowers;

import java.util.function.Function;
import java.util.function.Supplier;

public class TFBlocks {

	public static final TFPortalBlock TWILIGHT_PORTAL = register("twilight_portal", TFPortalBlock::new, () -> BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel((state) -> 11).noCollision().noOcclusion().noLootTable());

	//misc.
	public static final Block HEDGE = registerWithItem("hedge", HedgeBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(2.0F, 6.0F));
	public static final MasonJarBlock MASON_JAR = register("mason_jar", MasonJarBlock::new, () -> BlockBehaviour.Properties.of().noOcclusion().noTerrainParticles().randomTicks().sound(TFSoundTypes.JAR).strength(0.3F, 3.0F));
	public static final JarBlock FIREFLY_JAR = register("firefly_jar", FireflyJarBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().noTerrainParticles().sound(TFSoundTypes.JAR).strength(0.3F, 3.0F));
	public static final Block FIREFLY_SPAWNER = registerWithItem("firefly_particle_spawner", FireflySpawnerBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().noTerrainParticles().sound(TFSoundTypes.JAR).strength(0.3F, 3.0F));
	public static final JarBlock CICADA_JAR = register("cicada_jar", CicadaJarBlock::new, () -> BlockBehaviour.Properties.of().noOcclusion().noTerrainParticles().randomTicks().sound(TFSoundTypes.JAR).strength(0.3F, 3.0F));
	public static final Block MOSS_PATCH = registerWithItem("moss_patch", MossPatchBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.MOSS));
	public static final Block MAYAPPLE = registerWithItem("mayapple", MayappleBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS));
	public static final Block CLOVER_PATCH = registerWithItem("clover_patch", PatchBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().noCollision().noOcclusion().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS));
	public static final Block FIDDLEHEAD = registerWithItem("fiddlehead", FiddleheadBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.GRASS));
	public static final Block MUSHGLOOM = registerWithItem("mushgloom", MushgloomBlock::new, () -> BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 3).noCollision().noOcclusion().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.FUNGUS));
	public static final Block TORCHBERRY_PLANT = registerWithItem("torchberry_plant", TorchberryPlantBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().lightLevel(value -> value.getValue(TorchberryPlantBlock.HAS_BERRIES) ? 7 : 1).noCollision().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS));
	public static final Block ROOT_STRAND = registerWithItem("root_strand", RootStrandBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS));
	public static final Block FALLEN_LEAVES = register("fallen_leaves", FallenLeavesBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().replaceable().pushReaction(PushReaction.DESTROY).sound(SoundType.AZALEA_LEAVES));
	public static final Block ROOT_BLOCK = registerWithItem("root", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F));
	public static final Block LIVEROOT_BLOCK = registerWithItem("liveroot_block", LiverootBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD).strength(2.0F, 3.0F));
	public static final Block UNCRAFTING_TABLE = registerWithItem("uncrafting_table", UncraftingTableBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.FIRE).sound(SoundType.WOOD).strength(2.5F));
	public static final Block SMOKER = registerWithItem("smoker", TFSmokerBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.GRASS).strength(1.5F, 6.0F));
	public static final Block ENCASED_SMOKER = registerWithItem("encased_smoker", EncasedSmokerBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F));
	public static final Block FIRE_JET = registerWithItem("fire_jet", FireJetBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15).mapColor(MapColor.GRASS).randomTicks().sound(SoundType.GRASS).strength(1.5F, 6.0F));
	public static final Block ENCASED_FIRE_JET = registerWithItem("encased_fire_jet", EncasedFireJetBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F));
	public static final Block FIREFLY = registerWithItem("firefly", FireflyBlock::new, () -> BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).noCollision().noTerrainParticles().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK));
	public static final Block CICADA = registerWithItem("cicada", CicadaBlock::new, () -> BlockBehaviour.Properties.of().instabreak().noCollision().noTerrainParticles().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK));
	public static final Block MOONWORM = registerWithItem("moonworm", MoonwormBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOff().instabreak().lightLevel((state) -> 14).noCollision().noTerrainParticles().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK));
	public static final Block HUGE_LILY_PAD = register("huge_lily_pad", HugeLilyPadBlock::new, () -> BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.LILY_PAD));
	public static final Block HUGE_WATER_LILY = register("huge_water_lily", HugeWaterLilyBlock::new, () -> BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.LILY_PAD));
	public static final Block SLIDER = registerWithItem("slider", SliderBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).noLootTable().noOcclusion().randomTicks().strength(2.0F, 10.0F));
	public static final Block IRON_LADDER = registerWithItem("iron_ladder", IronLadderBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOff().noOcclusion().pushReaction(PushReaction.DESTROY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F));
	public static final Block ROPE = register("rope", RopeBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOff().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.3F, 3.0F));
	public static final TransparentBlock CANOPY_WINDOW = registerWithItem("canopy_window", TransparentBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((pState, pLevel, pPos, pValue) -> false).isRedstoneConductor((pState, pLevel, pPos) -> false).isSuffocating((pState, pLevel, pPos) -> false).isViewBlocking((pState, pLevel, pPos) -> false));
	public static final IronBarsBlock CANOPY_WINDOW_PANE = registerWithItem("canopy_window_pane", IronBarsBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion());
	public static final Block SINISTER_SPAWNER = registerWithItem("sinister_spawner", SinisterSpawnerBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER).noLootTable());
	public static final Block BRAZIER = registerWithItem("brazier", BrazierBlock::new, () -> BlockBehaviour.Properties.of().sound(SoundType.WOOD).lightLevel(state -> state.getValue(BrazierBlock.HALF) == DoubleBlockHalf.UPPER ? state.getValue(BrazierBlock.LIGHT).getLight() : 0).pushReaction(PushReaction.DESTROY));

	// bushes
	public static final Block IRON_OREBERRY_BUSH = registerWithItem("iron_oreberry_bush", properties -> new OreBerryBushBlock(false, TFLootTables.IRON_OREBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block GOLD_OREBERRY_BUSH = registerWithItem("gold_oreberry_bush", properties -> new OreBerryBushBlock(false, TFLootTables.GOLD_OREBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block COPPER_OREBERRY_BUSH = registerWithItem("copper_oreberry_bush", properties -> new OreBerryBushBlock(false, TFLootTables.COPPER_OREBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block ESSENCE_OREBERRY_BUSH = registerWithItem("essence_oreberry_bush", properties -> new OreBerryBushBlock(true, TFLootTables.ESSENCE_BERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block RASPBERRY_BUSH = registerWithItem("raspberry_bush", properties -> new BerryBushBlock(TFLootTables.RASPBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block BLUEBERRY_BUSH = registerWithItem("blueberry_bush", properties -> new BerryBushBlock(TFLootTables.BLUEBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block BLACKBERRY_BUSH = registerWithItem("blackberry_bush", properties -> new BerryBushBlock(TFLootTables.BLACKBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block MALOBERRY_BUSH = registerWithItem("maloberry_bush", properties -> new BerryBushBlock(TFLootTables.MALOBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block BLIGHTBERRY_BUSH = registerWithItem("blightberry_bush", properties -> new DarkTowerBerryBushBlock(TFLootTables.BLIGHTBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block DUSKBERRY_BUSH = registerWithItem("duskberry_bush", properties -> new DarkTowerBerryBushBlock(TFLootTables.DUSKBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block SKYBERRY_BUSH = registerWithItem("skyberry_bush", properties -> new DarkTowerBerryBushBlock(TFLootTables.SKYBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());
	public static final Block STINGBERRY_BUSH = registerWithItem("stingberry_bush", properties -> new DarkTowerBerryBushBlock(TFLootTables.STINGBERRY_BUSH_DROPS, properties), () -> BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion());

	//naga courtyard
	public static final Block NAGASTONE_HEAD = registerWithItem("nagastone_head", TFHorizontalBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block NAGASTONE = registerWithItem("nagastone", NagastoneBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block SPIRAL_BRICKS = registerWithItem("spiral_bricks", SpiralBrickBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block ETCHED_NAGASTONE = registerWithItem("etched_nagastone", EtchedNagastoneBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block NAGASTONE_PILLAR = registerWithItem("nagastone_pillar", DirectionalRotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final StairBlock NAGASTONE_STAIRS_LEFT = registerWithItem("nagastone_stairs_left", properties -> new StairBlock(ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(ETCHED_NAGASTONE));
	public static final StairBlock NAGASTONE_STAIRS_RIGHT = registerWithItem("nagastone_stairs_right", properties -> new StairBlock(ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(ETCHED_NAGASTONE));
	public static final Block MOSSY_ETCHED_NAGASTONE = registerWithItem("mossy_etched_nagastone", EtchedNagastoneBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block MOSSY_NAGASTONE_PILLAR = registerWithItem("mossy_nagastone_pillar", DirectionalRotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final StairBlock MOSSY_NAGASTONE_STAIRS_LEFT = registerWithItem("mossy_nagastone_stairs_left", properties -> new StairBlock(MOSSY_ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MOSSY_ETCHED_NAGASTONE));
	public static final StairBlock MOSSY_NAGASTONE_STAIRS_RIGHT = registerWithItem("mossy_nagastone_stairs_right", properties -> new StairBlock(MOSSY_ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MOSSY_ETCHED_NAGASTONE));
	public static final Block CRACKED_ETCHED_NAGASTONE = registerWithItem("cracked_etched_nagastone", EtchedNagastoneBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block CRACKED_NAGASTONE_PILLAR = registerWithItem("cracked_nagastone_pillar", DirectionalRotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final StairBlock CRACKED_NAGASTONE_STAIRS_LEFT = registerWithItem("cracked_nagastone_stairs_left", properties -> new StairBlock(CRACKED_ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CRACKED_ETCHED_NAGASTONE));
	public static final StairBlock CRACKED_NAGASTONE_STAIRS_RIGHT = registerWithItem("cracked_nagastone_stairs_right", properties -> new StairBlock(CRACKED_ETCHED_NAGASTONE.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CRACKED_ETCHED_NAGASTONE));

	//lich tower
	public static final RotatedPillarBlock TWISTED_STONE = registerWithItem("twisted_stone", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block TWISTED_STONE_PILLAR = registerWithItem("twisted_stone_pillar", properties -> new WallPillarBlock(12, 16, properties), () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block SKULL_CHEST = registerWithItem("skull_chest", SkullChestBlock::new, () -> BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA ? 15 : 0).mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(3.0F, 100.0F));
	public static final Block KEEPSAKE_CASKET = register("keepsake_casket", KeepsakeCasketBlock::new, () -> BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA ? 15 : 0).mapColor(MapColor.COLOR_BLACK).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 1200.0F));
	public static final RotatedPillarBlock BOLD_STONE_PILLAR = registerWithItem("bold_stone_pillar", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F));
	public static final Block CHISELED_CANOPY_BOOKSHELF = registerWithItem("chiseled_canopy_bookshelf", ChiseledCanopyShelfBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN).sound(SoundType.CHISELED_BOOKSHELF).strength(2.5F));
	public static final Block CANDELABRA = registerWithItem("candelabra", CandelabraBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F));
	public static final AbstractSkullCandleBlock ZOMBIE_SKULL_CANDLE = register("zombie_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.ZOMBIE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ZOMBIE_HEAD));
	public static final AbstractSkullCandleBlock ZOMBIE_WALL_SKULL_CANDLE = register("zombie_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.ZOMBIE, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(ZOMBIE_SKULL_CANDLE.getLootTable()).overrideDescription(ZOMBIE_SKULL_CANDLE.getDescriptionId()));
	public static final AbstractSkullCandleBlock SKELETON_SKULL_CANDLE = register("skeleton_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.SKELETON, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SKELETON_SKULL));
	public static final AbstractSkullCandleBlock SKELETON_WALL_SKULL_CANDLE = register("skeleton_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.SKELETON, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(SKELETON_SKULL_CANDLE.getLootTable()).overrideDescription(SKELETON_SKULL_CANDLE.getDescriptionId()));
	public static final AbstractSkullCandleBlock WITHER_SKELE_SKULL_CANDLE = register("wither_skeleton_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WITHER_SKELETON_SKULL));
	public static final AbstractSkullCandleBlock WITHER_SKELE_WALL_SKULL_CANDLE = register("wither_skeleton_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(WITHER_SKELE_SKULL_CANDLE.getLootTable()).overrideDescription(WITHER_SKELE_SKULL_CANDLE.getDescriptionId()));
	public static final AbstractSkullCandleBlock CREEPER_SKULL_CANDLE = register("creeper_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.CREEPER, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CREEPER_HEAD));
	public static final AbstractSkullCandleBlock CREEPER_WALL_SKULL_CANDLE = register("creeper_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.CREEPER, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(CREEPER_SKULL_CANDLE.getLootTable()).overrideDescription(CREEPER_SKULL_CANDLE.getDescriptionId()));
	public static final AbstractSkullCandleBlock PLAYER_SKULL_CANDLE = register("player_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.PLAYER, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.PLAYER_HEAD));
	public static final AbstractSkullCandleBlock PLAYER_WALL_SKULL_CANDLE = register("player_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.PLAYER, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(PLAYER_SKULL_CANDLE.getLootTable()).overrideDescription(PLAYER_SKULL_CANDLE.getDescriptionId()));
	public static final AbstractSkullCandleBlock PIGLIN_SKULL_CANDLE = register("piglin_skull_candle", properties -> new SkullCandleBlock(SkullBlock.Types.PIGLIN, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.PIGLIN_HEAD));
	public static final AbstractSkullCandleBlock PIGLIN_WALL_SKULL_CANDLE = register("piglin_wall_skull_candle", properties -> new WallSkullCandleBlock(SkullBlock.Types.PIGLIN, properties), () -> BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).overrideLootTable(PIGLIN_SKULL_CANDLE.getLootTable()).overrideDescription(PIGLIN_SKULL_CANDLE.getDescriptionId()));
	public static final WroughtIronFenceBlock WROUGHT_IRON_FENCE = register("wrought_iron_fence", WroughtIronFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(8.0F, 20.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	public static final RotatedPillarBlock TERRORCOTTA_ARCS = registerWithItem("terrorcotta_arcs", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
	public static final GlazedTerracottaBlock TERRORCOTTA_CURVES = registerWithItem("terrorcotta_curves", GlazedTerracottaBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
	public static final BinaryRotatedBlock TERRORCOTTA_LINES = registerWithItem("terrorcotta_lines", BinaryRotatedBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
	public static final CarpetBlock CORONATION_CARPET = registerWithItem("coronation_carpet", properties -> new WoolCarpetBlock(DyeColor.RED, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CARPET).isValidSpawn(Blocks::always));

	//ominous
	public static final OminousFireBlock OMINOUS_FIRE = register("ominous_fire", OminousFireBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY));
	public static final OminousCandleBlock OMINOUS_CANDLE = ominousCandle("ominous_candle", MapColor.SAND, Blocks.CANDLE);
	public static final OminousCandleBlock OMINOUS_WHITE_CANDLE = ominousCandle("ominous_white_candle", MapColor.WOOL, Blocks.WHITE_CANDLE);
	public static final OminousCandleBlock OMINOUS_ORANGE_CANDLE = ominousCandle("ominous_orange_candle", MapColor.COLOR_ORANGE, Blocks.ORANGE_CANDLE);
	public static final OminousCandleBlock OMINOUS_MAGENTA_CANDLE = ominousCandle("ominous_magenta_candle", MapColor.COLOR_MAGENTA, Blocks.MAGENTA_CANDLE);
	public static final OminousCandleBlock OMINOUS_LIGHT_BLUE_CANDLE = ominousCandle("ominous_light_blue_candle", MapColor.COLOR_LIGHT_BLUE, Blocks.LIGHT_BLUE_CANDLE);
	public static final OminousCandleBlock OMINOUS_YELLOW_CANDLE = ominousCandle("ominous_yellow_candle", MapColor.COLOR_YELLOW, Blocks.YELLOW_CANDLE);
	public static final OminousCandleBlock OMINOUS_LIME_CANDLE = ominousCandle("ominous_lime_candle", MapColor.COLOR_LIGHT_GREEN, Blocks.LIME_CANDLE);
	public static final OminousCandleBlock OMINOUS_PINK_CANDLE = ominousCandle("ominous_pink_candle", MapColor.COLOR_PINK, Blocks.PINK_CANDLE);
	public static final OminousCandleBlock OMINOUS_GRAY_CANDLE = ominousCandle("ominous_gray_candle", MapColor.COLOR_GRAY, Blocks.GRAY_CANDLE);
	public static final OminousCandleBlock OMINOUS_LIGHT_GRAY_CANDLE = ominousCandle("ominous_light_gray_candle", MapColor.COLOR_LIGHT_GRAY, Blocks.LIGHT_GRAY_CANDLE);
	public static final OminousCandleBlock OMINOUS_CYAN_CANDLE = ominousCandle("ominous_cyan_candle", MapColor.COLOR_CYAN, Blocks.CYAN_CANDLE);
	public static final OminousCandleBlock OMINOUS_PURPLE_CANDLE = ominousCandle("ominous_purple_candle", MapColor.COLOR_PURPLE, Blocks.PURPLE_CANDLE);
	public static final OminousCandleBlock OMINOUS_BLUE_CANDLE = ominousCandle("ominous_blue_candle", MapColor.COLOR_BLUE, Blocks.BLUE_CANDLE);
	public static final OminousCandleBlock OMINOUS_BROWN_CANDLE = ominousCandle("ominous_brown_candle", MapColor.COLOR_BROWN, Blocks.BROWN_CANDLE);
	public static final OminousCandleBlock OMINOUS_GREEN_CANDLE = ominousCandle("ominous_green_candle", MapColor.COLOR_GREEN, Blocks.GREEN_CANDLE);
	public static final OminousCandleBlock OMINOUS_RED_CANDLE = ominousCandle("ominous_red_candle", MapColor.COLOR_RED, Blocks.RED_CANDLE);
	public static final OminousCandleBlock OMINOUS_BLACK_CANDLE = ominousCandle("ominous_black_candle", MapColor.COLOR_BLACK, Blocks.BLACK_CANDLE);

	//labyrinth
	public static final Block MAZESTONE = registerWithItem("mazestone", Block::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 5.0F));
	public static final Block MAZESTONE_BRICK = registerWithItem("mazestone_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block CUT_MAZESTONE = registerWithItem("cut_mazestone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block DECORATIVE_MAZESTONE = registerWithItem("decorative_mazestone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block CRACKED_MAZESTONE = registerWithItem("cracked_mazestone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block MOSSY_MAZESTONE = registerWithItem("mossy_mazestone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block MAZESTONE_MOSAIC = registerWithItem("mazestone_mosaic", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block MAZESTONE_BORDER = registerWithItem("mazestone_border", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(MAZESTONE));
	public static final Block RED_THREAD = registerWithItem("red_thread", RedThreadBlock::new, () -> BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.FIRE).isValidSpawn(TFBlocks::noSpawning).noCollision().noOcclusion().noTerrainParticles().pushReaction(PushReaction.DESTROY));
	public static final Block MAZE_SLIME_BLOCK = registerWithItem("maze_slime_block", MazeSlimeBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(MapColor.STONE));

	//stronghold
	public static final Block STRONGHOLD_SHIELD = registerWithItem("stronghold_shield", StrongholdShieldBlock::new, () -> BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(-1.0F, 6000000.0F));
	public static final Block TROPHY_PEDESTAL = registerWithItem("trophy_pedestal", TrophyPedestalBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 2000.0F));
	public static final Block UNDERBRICK = registerWithItem("underbrick", Block::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_BRICKS).strength(1.5F, 6.0F));
	public static final Block MOSSY_UNDERBRICK = registerWithItem("mossy_underbrick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(UNDERBRICK));
	public static final Block CRACKED_UNDERBRICK = registerWithItem("cracked_underbrick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(UNDERBRICK));
	public static final Block UNDERBRICK_FLOOR = registerWithItem("underbrick_floor", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(UNDERBRICK));

	//dark tower
	public static final Block TOWERWOOD = registerWithItem("towerwood", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(40.0F, 6.0F).sound(SoundType.WOOD));
	public static final Block ENCASED_TOWERWOOD = registerWithItem("encased_towerwood", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).mapColor(MapColor.SAND));
	public static final Block CRACKED_TOWERWOOD = registerWithItem("cracked_towerwood", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(TOWERWOOD));
	public static final Block MOSSY_TOWERWOOD = registerWithItem("mossy_towerwood", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(TOWERWOOD));
	public static final Block INFESTED_TOWERWOOD = registerWithItem("infested_towerwood", InfestedTowerwoodBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).instrument(NoteBlockInstrument.FLUTE).noLootTable().strength(2.0F, 6.0F));
	public static final Block REAPPEARING_BLOCK = registerWithItem("reappearing_block", ReappearingBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOn().lightLevel((state) -> 4).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 35.0F));
	public static final Block VANISHING_BLOCK = registerWithItem("vanishing_block", VanishingBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(VanishingBlock.ACTIVE) ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(10.0F, 35.0F));
	public static final Block UNBREAKABLE_VANISHING_BLOCK = register("unbreakable_vanishing_block", VanishingBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(VANISHING_BLOCK).noLootTable().strength(-1.0F, 6000000.0F));
	public static final Block LOCKED_VANISHING_BLOCK = registerWithItem("locked_vanishing_block", LockedVanishingBlock::new, () -> BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).sound(SoundType.WOOD).strength(-1.0F, 2000.0F));
	public static final Block CARMINITE_BUILDER = registerWithItem("carminite_builder", BuilderBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(BuilderBlock.STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F));
	public static final Block BUILT_BLOCK = register("built_block", TranslucentBuiltBlock::new, () -> BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F));
	public static final Block ANTIBUILDER = registerWithItem("antibuilder", AntibuilderBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 10).noLootTable().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F));
	public static final Block ANTIBUILT_BLOCK = register("antibuilt_block", Block::new, () -> BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(0.3F, 2000.0F));
	public static final GhastTrapBlock GHAST_TRAP = registerWithItem("ghast_trap", GhastTrapBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(GhastTrapBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F));
	public static final Block CARMINITE_REACTOR = registerWithItem("carminite_reactor", CarminiteReactorBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(CarminiteReactorBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F));
	public static final Block REACTOR_DEBRIS = register("reactor_debris", ReactorDebrisBlock::new, () -> BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.ANCIENT_DEBRIS).strength(0.3F, 2000.0F));
	public static final Block FAKE_GOLD = register("fake_gold", Block::new, () -> BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F));
	public static final Block FAKE_DIAMOND = register("fake_diamond", Block::new, () -> BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F));
	public static final Block EXPERIMENT_115 = register("experiment_115", Experiment115Block::new, () -> BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.WOOL).strength(0.5F));

	//aurora palace
	public static final Block AURORA_BLOCK = registerWithItem("aurora_block", AuroraBrickBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).strength(10.0F, 6.0F));
	public static final RotatedPillarBlock AURORA_PILLAR = registerWithItem("aurora_pillar", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F));
	public static final Block AURORA_SLAB = registerWithItem("aurora_slab", SlabBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F));
	public static final Block AURORALIZED_GLASS = registerWithItem("auroralized_glass", AuroralizedGlassBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));

	//highlands/thornlands
	public static final Block BROWN_THORNS = registerWithItem("brown_thorns", ThornsBlock::new, () -> BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PODZOL).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F));
	public static final Block GREEN_THORNS = registerWithItem("green_thorns", ThornsBlock::new, () -> BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PLANT).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F));
	public static final Block BURNT_THORNS = registerWithItem("burnt_thorns", BurntThornsBlock::new, () -> BlockBehaviour.Properties.of().instabreak().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.DESTROY).sound(SoundType.SAND));
	public static final Block THORN_ROSE = registerWithItem("thorn_rose", ThornRoseBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(10.0F, 0.0F));
	public static final Block THORN_LEAVES = registerWithItem("thorn_leaves", properties -> new SpecialStemLeavesBlock(state -> state.is(TFBlocks.BROWN_THORNS) || state.is(TFBlocks.GREEN_THORNS), properties), () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block BEANSTALK_LEAVES = registerWithItem("beanstalk_leaves", properties -> new SpecialStemLeavesBlock(state -> state.is(TFBlocks.HUGE_STALK), properties), () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block DEADROCK = registerWithItem("deadrock", Block::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 6000000.0F));
	public static final Block CRACKED_DEADROCK = registerWithItem("cracked_deadrock", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(DEADROCK));
	public static final Block WEATHERED_DEADROCK = registerWithItem("weathered_deadrock", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(DEADROCK));
	public static final Block TROLLSTEINN = registerWithItem("trollsteinn", TrollsteinnBlock::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).randomTicks().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 6.0F));

	public static final Block WISPY_CLOUD = registerWithItem("wispy_cloud", properties -> new WispyCloudBlock(Biome.Precipitation.NONE, properties), () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.SNOW).noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.WOOL).strength(0.3F, 0.0F).forceSolidOff());
	public static final Block FLUFFY_CLOUD = registerWithItem("fluffy_cloud", properties -> new CloudBlock(null, properties), () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks());
	public static final Block RAINY_CLOUD = registerWithItem("rainy_cloud", properties -> new CloudBlock(Biome.Precipitation.RAIN, properties), () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks());
	public static final Block SNOWY_CLOUD = registerWithItem("snowy_cloud", properties -> new CloudBlock(Biome.Precipitation.SNOW, properties), () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks());

	public static final Block GIANT_COBBLESTONE = registerWithItem("giant_cobblestone", GiantBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 50.0F));
	public static final Block GIANT_LOG = registerWithItem("giant_log", GiantBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 30.0F));
	public static final Block GIANT_LEAVES = registerWithItem("giant_leaves", GiantLeavesBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.AZALEA_LEAVES).strength(0.2F * 64.0F, 15.0F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isValidSpawn(TFBlocks::noSpawning).isRedstoneConductor((state, level, pos) -> false));
	public static final Block GIANT_OBSIDIAN = registerWithItem("giant_obsidian", GiantBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(50.0F * 64.0F * 64.0F, 2000.0F * 64.0F * 64.0F).isValidSpawn(TFBlocks::noSpawning));
	public static final Block UBEROUS_SOIL = registerWithItem("uberous_soil", UberousSoilBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F));
	public static final RotatedPillarBlock HUGE_STALK = registerWithItem("huge_stalk", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PLANT).sound(SoundType.STEM).strength(1.5F, 3.0F));
	public static final Block BEANSTALK_GROWER = register("beanstalk_grower", GrowingBeanstalkBlock::new, () -> BlockBehaviour.Properties.of().noCollision().noLootTable().noOcclusion().noTerrainParticles().strength(-1.0F, 6000000.0F));
	public static final Block HUGE_MUSHGLOOM = registerWithItem("huge_mushgloom", HugeMushroomBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.SHROOMLIGHT).strength(0.2F));
	public static final Block HUGE_MUSHGLOOM_STEM = registerWithItem("huge_mushgloom_stem", HugeMushroomBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NYLIUM).strength(0.2F));
	public static final Block TROLLVIDR = registerWithItem("trollvidr", TrollRootBlock::new, () -> BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA));
	public static final Block UNRIPE_TROLLBER = registerWithItem("unripe_trollber", UnripeTorchClusterBlock::new, () -> BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.FLOWERING_AZALEA));
	public static final Block TROLLBER = registerWithItem("trollber", TrollRootBlock::new, () -> BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA));

	//plateau castle
	public static final Block CASTLE_BRICK = registerWithItem("castle_brick", Block::new, () -> BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 50.0F));
	public static final Block WORN_CASTLE_BRICK = registerWithItem("worn_castle_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block CRACKED_CASTLE_BRICK = registerWithItem("cracked_castle_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block CASTLE_ROOF_TILE = registerWithItem("castle_roof_tile", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(MapColor.COLOR_GRAY));
	public static final Block MOSSY_CASTLE_BRICK = registerWithItem("mossy_castle_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block THICK_CASTLE_BRICK = registerWithItem("thick_castle_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block ENCASED_CASTLE_BRICK_PILLAR = registerWithItem("encased_castle_brick_pillar", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block ENCASED_CASTLE_BRICK_TILE = registerWithItem("encased_castle_brick_tile", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block BOLD_CASTLE_BRICK_PILLAR = registerWithItem("bold_castle_brick_pillar", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final Block BOLD_CASTLE_BRICK_TILE = registerWithItem("bold_castle_brick_tile", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final StairBlock CASTLE_BRICK_STAIRS = registerWithItem("castle_brick_stairs", properties -> new StairBlock(CASTLE_BRICK.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK));
	public static final StairBlock WORN_CASTLE_BRICK_STAIRS = registerWithItem("worn_castle_brick_stairs", properties -> new StairBlock(WORN_CASTLE_BRICK.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(WORN_CASTLE_BRICK));
	public static final StairBlock CRACKED_CASTLE_BRICK_STAIRS = registerWithItem("cracked_castle_brick_stairs", properties -> new StairBlock(CRACKED_CASTLE_BRICK.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CRACKED_CASTLE_BRICK));
	public static final StairBlock MOSSY_CASTLE_BRICK_STAIRS = registerWithItem("mossy_castle_brick_stairs", properties -> new StairBlock(MOSSY_CASTLE_BRICK.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MOSSY_CASTLE_BRICK));
	public static final StairBlock ENCASED_CASTLE_BRICK_STAIRS = registerWithItem("encased_castle_brick_stairs", properties -> new StairBlock(ENCASED_CASTLE_BRICK_PILLAR.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(ENCASED_CASTLE_BRICK_PILLAR));
	public static final StairBlock BOLD_CASTLE_BRICK_STAIRS = registerWithItem("bold_castle_brick_stairs", properties -> new StairBlock(BOLD_CASTLE_BRICK_PILLAR.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(BOLD_CASTLE_BRICK_PILLAR));
	public static final Block PINK_CASTLE_RUNE_BRICK = registerWithItem("pink_castle_rune_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.MAGENTA));
	public static final Block BLUE_CASTLE_RUNE_BRICK = registerWithItem("blue_castle_rune_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.LIGHT_BLUE));
	public static final Block YELLOW_CASTLE_RUNE_BRICK = registerWithItem("yellow_castle_rune_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.YELLOW));
	public static final Block VIOLET_CASTLE_RUNE_BRICK = registerWithItem("violet_castle_rune_brick", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.PURPLE));
	public static final Block VIOLET_FORCE_FIELD = registerWithItem("violet_force_field", ForceFieldBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.PURPLE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F));
	public static final Block PINK_FORCE_FIELD = registerWithItem("pink_force_field", ForceFieldBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.MAGENTA).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F));
	public static final Block ORANGE_FORCE_FIELD = registerWithItem("orange_force_field", ForceFieldBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.ORANGE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F));
	public static final Block GREEN_FORCE_FIELD = registerWithItem("green_force_field", ForceFieldBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.GREEN).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F));
	public static final Block BLUE_FORCE_FIELD = registerWithItem("blue_force_field", ForceFieldBlock::new, () -> BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.LIGHT_BLUE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F));
	public static final Block CINDER_FURNACE = registerWithItem("cinder_furnace", CinderFurnaceBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(7.0F).lightLevel((state) -> 15));
	public static final RotatedPillarBlock CINDER_LOG = registerWithItem("cinder_log", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F));
	public static final Block CINDER_WOOD = registerWithItem("cinder_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F));
	public static final Block YELLOW_CASTLE_DOOR = registerWithItem("yellow_castle_door", CastleDoorBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.YELLOW.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F));
	public static final Block VIOLET_CASTLE_DOOR = registerWithItem("violet_castle_door", CastleDoorBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.LIGHT_BLUE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F));
	public static final Block PINK_CASTLE_DOOR = registerWithItem("pink_castle_door", CastleDoorBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.MAGENTA.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F));
	public static final Block BLUE_CASTLE_DOOR = registerWithItem("blue_castle_door", CastleDoorBlock::new, () -> BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.PURPLE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F));

	//mini structures
	public static final Block TWILIGHT_PORTAL_MINIATURE_STRUCTURE = registerWithItem("twilight_portal_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.of().noCollision().noOcclusion().requiresCorrectToolForDrops().strength(0.75F));
//		public static final Block HEDGE_MAZE_MINIATURE_STRUCTURE = register("hedge_maze_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block HOLLOW_HILL_MINIATURE_STRUCTURE = register("hollow_hill_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block QUEST_GROVE_MINIATURE_STRUCTURE = register("quest_grove_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block MUSHROOM_TOWER_MINIATURE_STRUCTURE = register("mushroom_tower_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
	public static final Block NAGA_COURTYARD_MINIATURE_STRUCTURE = registerWithItem("naga_courtyard_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
	public static final Block LICH_TOWER_MINIATURE_STRUCTURE = registerWithItem("lich_tower_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
	public static final Block MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE = registerWithItem("minotaur_labyrinth_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block HYDRA_LAIR_MINIATURE_STRUCTURE = register("hydra_lair_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE = register("goblin_stronghold_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
	public static final Block DARK_TOWER_MINIATURE_STRUCTURE = registerWithItem("dark_tower_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block YETI_CAVE_MINIATURE_STRUCTURE = register("yeti_cave_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block AURORA_PALACE_MINIATURE_STRUCTURE = register("aurora_palace_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE = register("troll_cave_cottage_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//	public static final Block FINAL_CASTLE_MINIATURE_STRUCTURE = register("final_castle_miniature_structure", MiniatureStructureBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE));

	//storage blocks
	public static final Block KNIGHTMETAL_BLOCK = registerWithItem("knightmetal_block", KnightmetalBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 40.0F));
	public static final Block IRONWOOD_BLOCK = registerWithItem("ironwood_block", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(5.0F, 6.0F));
	public static final Block FIERY_BLOCK = registerWithItem("fiery_block", FieryBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F).emissiveRendering((state, world, pos) -> true), () -> new Item.Properties().fireResistant());
	public static final Block STEELEAF_BLOCK = registerWithItem("steeleaf_block", Block::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 6.0F));
	public static final Block ARCTIC_FUR_BLOCK = registerWithItem("arctic_fur_block", ArcticFurBlock::new, () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(0.8F));
	public static final Block CARMINITE_BLOCK = registerWithItem("carminite_block", Block::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(1.5F, 10.0F).requiresCorrectToolForDrops().sound(SoundType.METAL));

	//boss trophies and spawners
	public static final Block NAGA_BOSS_SPAWNER = registerWithItem("naga_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.NAGA, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block LICH_BOSS_SPAWNER = registerWithItem("lich_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.LICH, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block HYDRA_BOSS_SPAWNER = registerWithItem("hydra_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.HYDRA, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block UR_GHAST_BOSS_SPAWNER = registerWithItem("ur_ghast_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.UR_GHAST, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block KNIGHT_PHANTOM_BOSS_SPAWNER = registerWithItem("knight_phantom_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.KNIGHT_PHANTOM, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block SNOW_QUEEN_BOSS_SPAWNER = registerWithItem("snow_queen_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.SNOW_QUEEN, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block MINOSHROOM_BOSS_SPAWNER = registerWithItem("minoshroom_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.MINOSHROOM, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block ALPHA_YETI_BOSS_SPAWNER = registerWithItem("alpha_yeti_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.ALPHA_YETI, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final Block FINAL_BOSS_BOSS_SPAWNER = registerWithItem("final_boss_boss_spawner", properties -> new BossSpawnerBlock(BossVariant.FINAL_BOSS, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F));
	public static final TrophyBlock NAGA_TROPHY = register("naga_trophy", properties -> new TrophyBlock(BossVariant.NAGA, 5, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock LICH_TROPHY = register("lich_trophy", properties -> new TrophyBlock(BossVariant.LICH, 6, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock HYDRA_TROPHY = register("hydra_trophy", properties -> new TrophyBlock(BossVariant.HYDRA, 12, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock UR_GHAST_TROPHY = register("ur_ghast_trophy", properties -> new TrophyBlock(BossVariant.UR_GHAST, 13, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock KNIGHT_PHANTOM_TROPHY = register("knight_phantom_trophy", properties -> new TrophyBlock(BossVariant.KNIGHT_PHANTOM, 8, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock SNOW_QUEEN_TROPHY = register("snow_queen_trophy", properties -> new TrophyBlock(BossVariant.SNOW_QUEEN, 14, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock MINOSHROOM_TROPHY = register("minoshroom_trophy", properties -> new TrophyBlock(BossVariant.MINOSHROOM, 7, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock ALPHA_YETI_TROPHY = register("alpha_yeti_trophy", properties -> new TrophyBlock(BossVariant.ALPHA_YETI, 9, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyBlock QUEST_RAM_TROPHY = register("quest_ram_trophy", properties -> new TrophyBlock(BossVariant.QUEST_RAM, 1, properties), () -> BlockBehaviour.Properties.of().instabreak());
	public static final TrophyWallBlock NAGA_WALL_TROPHY = register("naga_wall_trophy", properties -> new TrophyWallBlock(BossVariant.NAGA, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(NAGA_TROPHY.getLootTable()).overrideDescription(NAGA_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock LICH_WALL_TROPHY = register("lich_wall_trophy", properties -> new TrophyWallBlock(BossVariant.LICH, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(LICH_TROPHY.getLootTable()).overrideDescription(LICH_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock HYDRA_WALL_TROPHY = register("hydra_wall_trophy", properties -> new TrophyWallBlock(BossVariant.HYDRA, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(HYDRA_TROPHY.getLootTable()).overrideDescription(HYDRA_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock UR_GHAST_WALL_TROPHY = register("ur_ghast_wall_trophy", properties -> new TrophyWallBlock(BossVariant.UR_GHAST, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(UR_GHAST_TROPHY.getLootTable()).overrideDescription(UR_GHAST_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock KNIGHT_PHANTOM_WALL_TROPHY = register("knight_phantom_wall_trophy", properties -> new TrophyWallBlock(BossVariant.KNIGHT_PHANTOM, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(KNIGHT_PHANTOM_TROPHY.getLootTable()).overrideDescription(KNIGHT_PHANTOM_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock SNOW_QUEEN_WALL_TROPHY = register("snow_queen_wall_trophy", properties -> new TrophyWallBlock(BossVariant.SNOW_QUEEN, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(SNOW_QUEEN_TROPHY.getLootTable()).overrideDescription(SNOW_QUEEN_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock MINOSHROOM_WALL_TROPHY = register("minoshroom_wall_trophy", properties -> new TrophyWallBlock(BossVariant.MINOSHROOM, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(MINOSHROOM_TROPHY.getLootTable()).overrideDescription(MINOSHROOM_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock ALPHA_YETI_WALL_TROPHY = register("alpha_yeti_wall_trophy", properties -> new TrophyWallBlock(BossVariant.ALPHA_YETI, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(ALPHA_YETI_TROPHY.getLootTable()).overrideDescription(ALPHA_YETI_TROPHY.getDescriptionId()));
	public static final TrophyWallBlock QUEST_RAM_WALL_TROPHY = register("quest_ram_wall_trophy", properties -> new TrophyWallBlock(BossVariant.QUEST_RAM, properties), () -> BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).overrideLootTable(QUEST_RAM_TROPHY.getLootTable()).overrideDescription(QUEST_RAM_TROPHY.getDescriptionId()));

	// TODO Enumify all of the dang tree stuff

	//all tree related stuff
	public static final BanisterBlock OAK_BANISTER = registerWithItem("oak_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
	public static final BanisterBlock SPRUCE_BANISTER = registerWithItem("spruce_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS));
	public static final BanisterBlock BIRCH_BANISTER = registerWithItem("birch_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS));
	public static final BanisterBlock JUNGLE_BANISTER = registerWithItem("jungle_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS));
	public static final BanisterBlock ACACIA_BANISTER = registerWithItem("acacia_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS));
	public static final BanisterBlock DARK_OAK_BANISTER = registerWithItem("dark_oak_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS));
	public static final BanisterBlock CRIMSON_BANISTER = registerWithItem("crimson_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS));
	public static final BanisterBlock WARPED_BANISTER = registerWithItem("warped_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS));
	public static final BanisterBlock VANGROVE_BANISTER = registerWithItem("vangrove_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS));
	public static final BanisterBlock BAMBOO_BANISTER = registerWithItem("bamboo_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));
	public static final BanisterBlock CHERRY_BANISTER = registerWithItem("cherry_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS));
	public static final BanisterBlock PALE_OAK_BANISTER = registerWithItem("pale_oak_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_PLANKS));

	public static final DryingRackBlock OAK_DRYING_RACK = registerWithItem("oak_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.OAK_SLAB, 0.5F));
	public static final DryingRackBlock SPRUCE_DRYING_RACK = registerWithItem("spruce_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.SPRUCE_SLAB, 0.5F));
	public static final DryingRackBlock BIRCH_DRYING_RACK = registerWithItem("birch_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.BIRCH_SLAB, 0.5F));
	public static final DryingRackBlock JUNGLE_DRYING_RACK = registerWithItem("jungle_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.JUNGLE_SLAB, 0.5F));
	public static final DryingRackBlock ACACIA_DRYING_RACK = registerWithItem("acacia_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.ACACIA_SLAB, 0.5F));
	public static final DryingRackBlock DARK_OAK_DRYING_RACK = registerWithItem("dark_oak_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.DARK_OAK_SLAB, 0.5F));
	public static final DryingRackBlock CRIMSON_DRYING_RACK = registerWithItem("crimson_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.CRIMSON_SLAB, 0.5F));
	public static final DryingRackBlock WARPED_DRYING_RACK = registerWithItem("warped_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.WARPED_SLAB, 0.5F));
	public static final DryingRackBlock VANGROVE_DRYING_RACK = registerWithItem("vangrove_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.MANGROVE_SLAB, 0.5F));
	public static final DryingRackBlock BAMBOO_DRYING_RACK = registerWithItem("bamboo_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.BAMBOO_SLAB, 0.5F));
	public static final DryingRackBlock CHERRY_DRYING_RACK = registerWithItem("cherry_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.CHERRY_SLAB, 0.5F));
	public static final DryingRackBlock PALE_OAK_DRYING_RACK = registerWithItem("pale_oak_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(Blocks.CHERRY_SLAB, 0.5F));

	public static final BlockBehaviour.Properties TWILIGHT_OAK_LOG_PROPS = logProperties(MapColor.WOOD, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_LOG_PROPS = logProperties(MapColor.PODZOL, MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_LOG_PROPS = logProperties(MapColor.DIRT, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_LOG_PROPS = logProperties(MapColor.COLOR_BROWN, MapColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_LOG_PROPS = logProperties(MapColor.DIRT, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_LOG_PROPS = logProperties(MapColor.WOOD, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_LOG_PROPS = logProperties(MapColor.SAND, MapColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_LOG_PROPS = logProperties(MapColor.PODZOL, MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_BARK_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_BARK_PROPS = logProperties(MapColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_BARK_PROPS = logProperties(MapColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_BARK_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_STRIPPED_PROPS = logProperties(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_STRIPPED_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_STRIPPED_PROPS = logProperties(MapColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_STRIPPED_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_STRIPPED_PROPS = logProperties(MapColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_STRIPPED_PROPS = logProperties(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_STRIPPED_PROPS = logProperties(MapColor.SAND).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_STRIPPED_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);

	public static final RotatedPillarBlock TWILIGHT_OAK_LOG = registerWithItem("twilight_oak_log", RotatedPillarBlock::new, () -> TWILIGHT_OAK_LOG_PROPS);
	public static final RotatedPillarBlock CANOPY_LOG = registerWithItem("canopy_log", RotatedPillarBlock::new, () -> CANOPY_LOG_PROPS);
	public static final RotatedPillarBlock MANGROVE_LOG = registerWithItem("mangrove_log", RotatedPillarBlock::new, () -> MANGROVE_LOG_PROPS);
	public static final RotatedPillarBlock DARK_LOG = registerWithItem("dark_log", RotatedPillarBlock::new, () -> DARK_LOG_PROPS);
	public static final RotatedPillarBlock TIME_LOG = registerWithItem("time_log", RotatedPillarBlock::new, () -> TIME_LOG_PROPS);
	public static final RotatedPillarBlock TRANSFORMATION_LOG = registerWithItem("transformation_log", RotatedPillarBlock::new, () -> TRANSFORMATION_LOG_PROPS);
	public static final RotatedPillarBlock MINING_LOG = registerWithItem("mining_log", RotatedPillarBlock::new, () -> MINING_LOG_PROPS);
	public static final RotatedPillarBlock SORTING_LOG = registerWithItem("sorting_log", RotatedPillarBlock::new, () -> SORTING_LOG_PROPS);

	public static final HorizontalHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL = registerCustomID("hollow_twilight_oak_log_horizontal", HorizontalHollowLogBlock::new, () -> TWILIGHT_OAK_BARK_PROPS, "hollow_twilight_oak_log");
	public static final HorizontalHollowLogBlock HOLLOW_CANOPY_LOG_HORIZONTAL = registerCustomID("hollow_canopy_log_horizontal", HorizontalHollowLogBlock::new, () -> CANOPY_BARK_PROPS, "hollow_canopy_log");
	public static final HorizontalHollowLogBlock HOLLOW_MANGROVE_LOG_HORIZONTAL = registerCustomID("hollow_mangrove_log_horizontal", HorizontalHollowLogBlock::new, () -> MANGROVE_BARK_PROPS, "hollow_mangrove_log");
	public static final HorizontalHollowLogBlock HOLLOW_DARK_LOG_HORIZONTAL = registerCustomID("hollow_dark_log_horizontal", HorizontalHollowLogBlock::new, () -> DARK_BARK_PROPS, "hollow_dark_log");
	public static final HorizontalHollowLogBlock HOLLOW_TIME_LOG_HORIZONTAL = registerCustomID("hollow_time_log_horizontal", HorizontalHollowLogBlock::new, () -> TIME_BARK_PROPS, "hollow_time_log");
	public static final HorizontalHollowLogBlock HOLLOW_TRANSFORMATION_LOG_HORIZONTAL = registerCustomID("hollow_transformation_log_horizontal", HorizontalHollowLogBlock::new, () -> TRANSFORMATION_BARK_PROPS, "hollow_transformation_log");
	public static final HorizontalHollowLogBlock HOLLOW_MINING_LOG_HORIZONTAL = registerCustomID("hollow_mining_log_horizontal", HorizontalHollowLogBlock::new, () -> MINING_BARK_PROPS, "hollow_mining_log");
	public static final HorizontalHollowLogBlock HOLLOW_SORTING_LOG_HORIZONTAL = registerCustomID("hollow_sorting_log_horizontal", HorizontalHollowLogBlock::new, () -> SORTING_BARK_PROPS, "hollow_sorting_log");

	public static final VerticalHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_VERTICAL = registerCustomID("hollow_twilight_oak_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, properties), () -> TWILIGHT_OAK_STRIPPED_PROPS, "hollow_twilight_oak_log");
	public static final VerticalHollowLogBlock HOLLOW_CANOPY_LOG_VERTICAL = registerCustomID("hollow_canopy_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, properties), () -> CANOPY_STRIPPED_PROPS, "hollow_canopy_log");
	public static final VerticalHollowLogBlock HOLLOW_MANGROVE_LOG_VERTICAL = registerCustomID("hollow_mangrove_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, properties), () -> MANGROVE_STRIPPED_PROPS, "hollow_mangrove_log");
	public static final VerticalHollowLogBlock HOLLOW_DARK_LOG_VERTICAL = registerCustomID("hollow_dark_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, properties), () -> DARK_STRIPPED_PROPS, "hollow_dark_log");
	public static final VerticalHollowLogBlock HOLLOW_TIME_LOG_VERTICAL = registerCustomID("hollow_time_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, properties), () -> TIME_STRIPPED_PROPS, "hollow_time_log");
	public static final VerticalHollowLogBlock HOLLOW_TRANSFORMATION_LOG_VERTICAL = registerCustomID("hollow_transformation_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, properties), () -> TRANSFORMATION_STRIPPED_PROPS, "hollow_transformation_log");
	public static final VerticalHollowLogBlock HOLLOW_MINING_LOG_VERTICAL = registerCustomID("hollow_mining_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, properties), () -> MINING_STRIPPED_PROPS, "hollow_mining_log");
	public static final VerticalHollowLogBlock HOLLOW_SORTING_LOG_VERTICAL = registerCustomID("hollow_sorting_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, properties), () -> SORTING_STRIPPED_PROPS, "hollow_sorting_log");

	public static final ClimbableHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE = registerCustomID("hollow_twilight_oak_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, properties), () -> TWILIGHT_OAK_STRIPPED_PROPS, "hollow_twilight_oak_log");
	public static final ClimbableHollowLogBlock HOLLOW_CANOPY_LOG_CLIMBABLE = registerCustomID("hollow_canopy_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, properties), () -> CANOPY_STRIPPED_PROPS, "hollow_canopy_log");
	public static final ClimbableHollowLogBlock HOLLOW_MANGROVE_LOG_CLIMBABLE = registerCustomID("hollow_mangrove_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, properties), () -> MANGROVE_STRIPPED_PROPS, "hollow_mangrove_log");
	public static final ClimbableHollowLogBlock HOLLOW_DARK_LOG_CLIMBABLE = registerCustomID("hollow_dark_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_DARK_LOG_VERTICAL, properties), () -> DARK_STRIPPED_PROPS, "hollow_dark_log");
	public static final ClimbableHollowLogBlock HOLLOW_TIME_LOG_CLIMBABLE = registerCustomID("hollow_time_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_TIME_LOG_VERTICAL, properties), () -> TIME_STRIPPED_PROPS, "hollow_time_log");
	public static final ClimbableHollowLogBlock HOLLOW_TRANSFORMATION_LOG_CLIMBABLE = registerCustomID("hollow_transformation_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, properties), () -> TRANSFORMATION_STRIPPED_PROPS, "hollow_transformation_log");
	public static final ClimbableHollowLogBlock HOLLOW_MINING_LOG_CLIMBABLE = registerCustomID("hollow_mining_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_MINING_LOG_VERTICAL, properties), () -> MINING_STRIPPED_PROPS, "hollow_mining_log");
	public static final ClimbableHollowLogBlock HOLLOW_SORTING_LOG_CLIMBABLE = registerCustomID("hollow_sorting_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, properties), () -> SORTING_STRIPPED_PROPS, "hollow_sorting_log");

	public static final HorizontalHollowLogBlock HOLLOW_OAK_LOG_HORIZONTAL = registerCustomID("hollow_oak_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD), "hollow_twilight_oak_log");
	public static final HorizontalHollowLogBlock HOLLOW_SPRUCE_LOG_HORIZONTAL = registerCustomID("hollow_spruce_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_WOOD), "hollow_spruce_log");
	public static final HorizontalHollowLogBlock HOLLOW_BIRCH_LOG_HORIZONTAL = registerCustomID("hollow_birch_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_WOOD), "hollow_birch_log");
	public static final HorizontalHollowLogBlock HOLLOW_JUNGLE_LOG_HORIZONTAL = registerCustomID("hollow_jungle_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_WOOD), "hollow_jungle_log");
	public static final HorizontalHollowLogBlock HOLLOW_ACACIA_LOG_HORIZONTAL = registerCustomID("hollow_acacia_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD), "hollow_acacia_log");
	public static final HorizontalHollowLogBlock HOLLOW_DARK_OAK_LOG_HORIZONTAL = registerCustomID("hollow_dark_oak_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_WOOD), "hollow_dark_oak_log");
	public static final HorizontalHollowLogBlock HOLLOW_CRIMSON_STEM_HORIZONTAL = registerCustomID("hollow_crimson_stem_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_HYPHAE), "hollow_crimson_stem");
	public static final HorizontalHollowLogBlock HOLLOW_WARPED_STEM_HORIZONTAL = registerCustomID("hollow_warped_stem_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE), "hollow_warped_stem");
	public static final HorizontalHollowLogBlock HOLLOW_VANGROVE_LOG_HORIZONTAL = registerCustomID("hollow_vangrove_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_WOOD), "hollow_vangrove_log");
	public static final HorizontalHollowLogBlock HOLLOW_CHERRY_LOG_HORIZONTAL = registerCustomID("hollow_cherry_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD), "hollow_cherry_log");
	public static final HorizontalHollowLogBlock HOLLOW_PALE_OAK_LOG_HORIZONTAL = registerCustomID("hollow_pale_oak_log_horizontal", HorizontalHollowLogBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_WOOD), "hollow_pale_oak_log");

	public static final VerticalHollowLogBlock HOLLOW_OAK_LOG_VERTICAL = registerCustomID("hollow_oak_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD), "hollow_twilight_oak_log");
	public static final VerticalHollowLogBlock HOLLOW_SPRUCE_LOG_VERTICAL = registerCustomID("hollow_spruce_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD), "hollow_spruce_log");
	public static final VerticalHollowLogBlock HOLLOW_BIRCH_LOG_VERTICAL = registerCustomID("hollow_birch_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD), "hollow_birch_log");
	public static final VerticalHollowLogBlock HOLLOW_JUNGLE_LOG_VERTICAL = registerCustomID("hollow_jungle_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD), "hollow_jungle_log");
	public static final VerticalHollowLogBlock HOLLOW_ACACIA_LOG_VERTICAL = registerCustomID("hollow_acacia_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD), "hollow_acacia_log");
	public static final VerticalHollowLogBlock HOLLOW_DARK_OAK_LOG_VERTICAL = registerCustomID("hollow_dark_oak_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD), "hollow_dark_oak_log");
	public static final VerticalHollowLogBlock HOLLOW_CRIMSON_STEM_VERTICAL = registerCustomID("hollow_crimson_stem_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE), "hollow_crimson_stem");
	public static final VerticalHollowLogBlock HOLLOW_WARPED_STEM_VERTICAL = registerCustomID("hollow_warped_stem_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE), "hollow_warped_stem");
	// wanna see a funny crash? Use () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD) instead of the BlockBehaviour.Properties.of(...)
	// I still legit have no idea why it happens but it does
	public static final VerticalHollowLogBlock HOLLOW_VANGROVE_LOG_VERTICAL = registerCustomID("hollow_vangrove_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD), "hollow_vangrove_log");
	public static final VerticalHollowLogBlock HOLLOW_CHERRY_LOG_VERTICAL = registerCustomID("hollow_cherry_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD), "hollow_cherry_log");
	public static final VerticalHollowLogBlock HOLLOW_PALE_OAK_LOG_VERTICAL = registerCustomID("hollow_pale_oak_log_vertical", properties -> new VerticalHollowLogBlock(TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_WOOD), "hollow_pale_oak_log");

	public static final ClimbableHollowLogBlock HOLLOW_OAK_LOG_CLIMBABLE = registerCustomID("hollow_oak_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_OAK_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD), "hollow_twilight_oak_log");
	public static final ClimbableHollowLogBlock HOLLOW_SPRUCE_LOG_CLIMBABLE = registerCustomID("hollow_spruce_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD), "hollow_spruce_log");
	public static final ClimbableHollowLogBlock HOLLOW_BIRCH_LOG_CLIMBABLE = registerCustomID("hollow_birch_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD), "hollow_birch_log");
	public static final ClimbableHollowLogBlock HOLLOW_JUNGLE_LOG_CLIMBABLE = registerCustomID("hollow_jungle_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD), "hollow_jungle_log");
	public static final ClimbableHollowLogBlock HOLLOW_ACACIA_LOG_CLIMBABLE = registerCustomID("hollow_acacia_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD), "hollow_acacia_log");
	public static final ClimbableHollowLogBlock HOLLOW_DARK_OAK_LOG_CLIMBABLE = registerCustomID("hollow_dark_oak_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD), "hollow_dark_oak_log");
	public static final ClimbableHollowLogBlock HOLLOW_CRIMSON_STEM_CLIMBABLE = registerCustomID("hollow_crimson_stem_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE), "hollow_crimson_stem");
	public static final ClimbableHollowLogBlock HOLLOW_WARPED_STEM_CLIMBABLE = registerCustomID("hollow_warped_stem_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE), "hollow_warped_stem");
	public static final ClimbableHollowLogBlock HOLLOW_VANGROVE_LOG_CLIMBABLE = registerCustomID("hollow_vangrove_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD), "hollow_vangrove_log");
	public static final ClimbableHollowLogBlock HOLLOW_CHERRY_LOG_CLIMBABLE = registerCustomID("hollow_cherry_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD), "hollow_cherry_log");
	public static final ClimbableHollowLogBlock HOLLOW_PALE_OAK_LOG_CLIMBABLE = registerCustomID("hollow_pale_oak_log_climbable", properties -> new ClimbableHollowLogBlock(TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_WOOD), "hollow_pale_oak_log");

	public static final RotatedPillarBlock STRIPPED_TWILIGHT_OAK_LOG = registerWithItem("stripped_twilight_oak_log", RotatedPillarBlock::new, () -> TWILIGHT_OAK_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_CANOPY_LOG = registerWithItem("stripped_canopy_log", RotatedPillarBlock::new, () -> CANOPY_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_MANGROVE_LOG = registerWithItem("stripped_mangrove_log", RotatedPillarBlock::new, () -> MANGROVE_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_DARK_LOG = registerWithItem("stripped_dark_log", RotatedPillarBlock::new, () -> DARK_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_TIME_LOG = registerWithItem("stripped_time_log", RotatedPillarBlock::new, () -> TIME_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_TRANSFORMATION_LOG = registerWithItem("stripped_transformation_log", RotatedPillarBlock::new, () -> TRANSFORMATION_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_MINING_LOG = registerWithItem("stripped_mining_log", RotatedPillarBlock::new, () -> MINING_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_SORTING_LOG = registerWithItem("stripped_sorting_log", RotatedPillarBlock::new, () -> SORTING_STRIPPED_PROPS);

	public static final RotatedPillarBlock TWILIGHT_OAK_WOOD = registerWithItem("twilight_oak_wood", RotatedPillarBlock::new, () -> TWILIGHT_OAK_BARK_PROPS);
	public static final RotatedPillarBlock CANOPY_WOOD = registerWithItem("canopy_wood", RotatedPillarBlock::new, () -> CANOPY_BARK_PROPS);
	public static final RotatedPillarBlock MANGROVE_WOOD = registerWithItem("mangrove_wood", RotatedPillarBlock::new, () -> MANGROVE_BARK_PROPS);
	public static final RotatedPillarBlock DARK_WOOD = registerWithItem("dark_wood", RotatedPillarBlock::new, () -> DARK_BARK_PROPS);
	public static final RotatedPillarBlock TIME_WOOD = registerWithItem("time_wood", RotatedPillarBlock::new, () -> TIME_BARK_PROPS);
	public static final RotatedPillarBlock TRANSFORMATION_WOOD = registerWithItem("transformation_wood", RotatedPillarBlock::new, () -> TRANSFORMATION_BARK_PROPS);
	public static final RotatedPillarBlock MINING_WOOD = registerWithItem("mining_wood", RotatedPillarBlock::new, () -> MINING_BARK_PROPS);
	public static final RotatedPillarBlock SORTING_WOOD = registerWithItem("sorting_wood", RotatedPillarBlock::new, () -> SORTING_BARK_PROPS);

	public static final RotatedPillarBlock STRIPPED_TWILIGHT_OAK_WOOD = registerWithItem("stripped_twilight_oak_wood", RotatedPillarBlock::new, () -> TWILIGHT_OAK_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_CANOPY_WOOD = registerWithItem("stripped_canopy_wood", RotatedPillarBlock::new, () -> CANOPY_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_MANGROVE_WOOD = registerWithItem("stripped_mangrove_wood", RotatedPillarBlock::new, () -> MANGROVE_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_DARK_WOOD = registerWithItem("stripped_dark_wood", RotatedPillarBlock::new, () -> DARK_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_TIME_WOOD = registerWithItem("stripped_time_wood", RotatedPillarBlock::new, () -> TIME_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_TRANSFORMATION_WOOD = registerWithItem("stripped_transformation_wood", RotatedPillarBlock::new, () -> TRANSFORMATION_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_MINING_WOOD = registerWithItem("stripped_mining_wood", RotatedPillarBlock::new, () -> MINING_STRIPPED_PROPS);
	public static final RotatedPillarBlock STRIPPED_SORTING_WOOD = registerWithItem("stripped_sorting_wood", RotatedPillarBlock::new, () -> SORTING_STRIPPED_PROPS);

	public static final Block TIME_LOG_CORE = registerWithItem("time_log_core", TimeLogCoreBlock::new, () -> TIME_LOG_PROPS);
	public static final Block TRANSFORMATION_LOG_CORE = registerWithItem("transformation_log_core", TransLogCoreBlock::new, () -> TRANSFORMATION_LOG_PROPS);
	public static final Block MINING_LOG_CORE = registerWithItem("mining_log_core", MineLogCoreBlock::new, () -> MINING_LOG_PROPS);
	public static final Block SORTING_LOG_CORE = registerWithItem("sorting_log_core", SortLogCoreBlock::new, () -> SORTING_LOG_PROPS);

	public static final Block MANGROVE_ROOT = registerWithItem("mangrove_root", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.STONE).sound(SoundType.WOOD).strength(2.0F));

	public static final Block TWILIGHT_OAK_LEAVES = registerWithItem("twilight_oak_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block CANOPY_LEAVES = registerWithItem("canopy_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block MANGROVE_LEAVES = registerWithItem("mangrove_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block DARK_LEAVES = registerWithItem("dark_leaves", DarkLeavesBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block HARDENED_DARK_LEAVES = registerWithItem("hardened_dark_leaves", HardenedDarkLeavesBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES).isValidSpawn(TFBlocks::noSpawning).isRedstoneConductor((state, level, pos) -> false));
	public static final Block RAINBOW_OAK_LEAVES = registerWithItem("rainbow_oak_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block TIME_LEAVES = registerWithItem("time_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block TRANSFORMATION_LEAVES = registerWithItem("transformation_leaves", TransformationLeavesBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block MINING_LEAVES = registerWithItem("mining_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
	public static final Block SORTING_LEAVES = registerWithItem("sorting_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false));

	public static final SaplingBlock TWILIGHT_OAK_SAPLING = registerWithItem("twilight_oak_sapling", properties -> new SaplingBlock(TFTreeGrowers.TWILIGHT_OAK, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock CANOPY_SAPLING = registerWithItem("canopy_sapling", properties -> new SaplingBlock(TFTreeGrowers.CANOPY, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock MANGROVE_SAPLING = registerWithItem("mangrove_sapling", properties -> new MangroveSaplingBlock(TFTreeGrowers.MANGROVE, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock DARKWOOD_SAPLING = registerWithItem("darkwood_sapling", properties -> new SaplingBlock(TFTreeGrowers.DARK, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock HOLLOW_OAK_SAPLING = registerWithItem("hollow_oak_sapling", properties -> new SaplingBlock(TFTreeGrowers.HOLLOW_OAK, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock TIME_SAPLING = registerWithItem("time_sapling", properties -> new SaplingBlock(TFTreeGrowers.TIME, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock TRANSFORMATION_SAPLING = registerWithItem("transformation_sapling", properties -> new SaplingBlock(TFTreeGrowers.TRANSFORMATION, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock MINING_SAPLING = registerWithItem("mining_sapling", properties -> new SaplingBlock(TFTreeGrowers.MINING, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock SORTING_SAPLING = registerWithItem("sorting_sapling", properties -> new SaplingBlock(TFTreeGrowers.SORTING, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());
	public static final SaplingBlock RAINBOW_OAK_SAPLING = registerWithItem("rainbow_oak_sapling", properties -> new SaplingBlock(TFTreeGrowers.RAINBOW_OAK, properties), () -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks());

	public static final Block TWILIGHT_OAK_PLANKS = registerWithItem("twilight_oak_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock TWILIGHT_OAK_STAIRS = registerWithItem("twilight_oak_stairs", properties -> new StairBlock(TWILIGHT_OAK_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS));
	public static final Block TWILIGHT_OAK_SLAB = registerWithItem("twilight_oak_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS));
	public static final Block TWILIGHT_OAK_BUTTON = registerWithItem("twilight_oak_button", properties -> new ButtonBlock(TFWoodTypes.TWILIGHT_OAK_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(0.5F));
	public static final Block TWILIGHT_OAK_FENCE = registerWithItem("twilight_oak_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS));
	public static final Block TWILIGHT_OAK_GATE = registerWithItem("twilight_oak_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).forceSolidOn());
	public static final Block TWILIGHT_OAK_PLATE = registerWithItem("twilight_oak_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.TWILIGHT_OAK_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock TWILIGHT_OAK_DOOR = registerWithItem("twilight_oak_door", properties -> new DoorBlock(TFWoodTypes.TWILIGHT_OAK_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion());
	public static final TrapDoorBlock TWILIGHT_OAK_TRAPDOOR = registerWithItem("twilight_oak_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.TWILIGHT_OAK_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock TWILIGHT_OAK_SIGN = register("twilight_oak_sign", properties -> new StandingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion().noCollision());
	public static final WallSignBlock TWILIGHT_WALL_SIGN = register("twilight_wall_sign", properties -> new WallSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion().noCollision().overrideLootTable(TWILIGHT_OAK_SIGN.getLootTable()).overrideDescription(TWILIGHT_OAK_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock TWILIGHT_OAK_HANGING_SIGN = register("twilight_oak_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock TWILIGHT_OAK_WALL_HANGING_SIGN = register("twilight_oak_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(1.0F).overrideLootTable(TWILIGHT_OAK_HANGING_SIGN.getLootTable()).overrideDescription(TWILIGHT_OAK_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock TWILIGHT_OAK_BANISTER = registerWithItem("twilight_oak_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS));
	public static final DryingRackBlock TWILIGHT_OAK_DRYING_RACK = registerWithItem("twilight_oak_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(TWILIGHT_OAK_SLAB, 0.5F));

	public static final Block CANOPY_PLANKS = registerWithItem("canopy_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock CANOPY_STAIRS = registerWithItem("canopy_stairs", properties -> new StairBlock(CANOPY_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS));
	public static final Block CANOPY_SLAB = registerWithItem("canopy_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS));
	public static final Block CANOPY_BUTTON = registerWithItem("canopy_button", properties -> new ButtonBlock(TFWoodTypes.CANOPY_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(0.5F));
	public static final Block CANOPY_FENCE = registerWithItem("canopy_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS));
	public static final Block CANOPY_GATE = registerWithItem("canopy_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.CANOPY_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).forceSolidOn());
	public static final Block CANOPY_PLATE = registerWithItem("canopy_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.CANOPY_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock CANOPY_DOOR = registerWithItem("canopy_door", properties -> new DoorBlock(TFWoodTypes.CANOPY_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(3.0F).noOcclusion());
	public static final TrapDoorBlock CANOPY_TRAPDOOR = registerWithItem("canopy_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.CANOPY_WOOD_SET, properties), () -> BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).strength(3.0F).sound(SoundType.WOOD).noOcclusion());
	public static final StandingSignBlock CANOPY_SIGN = register("canopy_sign", properties -> new StandingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock CANOPY_WALL_SIGN = register("canopy_wall_sign", properties -> new WallSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(CANOPY_SIGN.getLootTable()).overrideDescription(CANOPY_SIGN.getDescriptionId()));
	public static final Block CANOPY_BOOKSHELF = registerWithItem("canopy_bookshelf", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.5F));
	public static final CeilingHangingSignBlock CANOPY_HANGING_SIGN = register("canopy_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock CANOPY_WALL_HANGING_SIGN = register("canopy_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(1.0F).overrideLootTable(CANOPY_HANGING_SIGN.getLootTable()).overrideDescription(CANOPY_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock CANOPY_BANISTER = registerWithItem("canopy_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS));
	public static final DryingRackBlock CANOPY_DRYING_RACK = registerWithItem("canopy_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(CANOPY_SLAB, 0.5F));

	public static final Block MANGROVE_PLANKS = registerWithItem("mangrove_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock MANGROVE_STAIRS = registerWithItem("mangrove_stairs", properties -> new StairBlock(MANGROVE_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS));
	public static final Block MANGROVE_SLAB = registerWithItem("mangrove_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS));
	public static final Block MANGROVE_BUTTON = registerWithItem("mangrove_button", properties -> new ButtonBlock(TFWoodTypes.MANGROVE_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(0.5F));
	public static final Block MANGROVE_FENCE = registerWithItem("mangrove_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS));
	public static final Block MANGROVE_GATE = registerWithItem("mangrove_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).forceSolidOn());
	public static final Block MANGROVE_PLATE = registerWithItem("mangrove_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.MANGROVE_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock MANGROVE_DOOR = registerWithItem("mangrove_door", properties -> new DoorBlock(TFWoodTypes.MANGROVE_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(3.0F).noOcclusion());
	public static final TrapDoorBlock MANGROVE_TRAPDOOR = registerWithItem("mangrove_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.MANGROVE_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock MANGROVE_SIGN = register("mangrove_sign", properties -> new StandingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock MANGROVE_WALL_SIGN = register("mangrove_wall_sign", properties -> new WallSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(MANGROVE_SIGN.getLootTable()).overrideDescription(MANGROVE_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock MANGROVE_HANGING_SIGN = register("mangrove_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock MANGROVE_WALL_HANGING_SIGN = register("mangrove_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(1.0F).overrideLootTable(MANGROVE_HANGING_SIGN.getLootTable()).overrideDescription(MANGROVE_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock MANGROVE_BANISTER = registerWithItem("mangrove_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS));
	public static final DryingRackBlock MANGROVE_DRYING_RACK = registerWithItem("mangrove_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(MANGROVE_SLAB, 0.5F));

	public static final Block DARK_PLANKS = registerWithItem("dark_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock DARK_STAIRS = registerWithItem("dark_stairs", properties -> new StairBlock(DARK_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS));
	public static final Block DARK_SLAB = registerWithItem("dark_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).sound(SoundType.WOOD));
	public static final Block DARK_BUTTON = registerWithItem("dark_button", properties -> new ButtonBlock(TFWoodTypes.DARK_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(0.5F));
	public static final Block DARK_FENCE = registerWithItem("dark_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS));
	public static final Block DARK_GATE = registerWithItem("dark_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.DARK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).forceSolidOn());
	public static final Block DARK_PLATE = registerWithItem("dark_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.DARK_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock DARK_DOOR = registerWithItem("dark_door", properties -> new DoorBlock(TFWoodTypes.DARK_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion());
	public static final TrapDoorBlock DARK_TRAPDOOR = registerWithItem("dark_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.DARK_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock DARK_SIGN = register("dark_sign", properties -> new StandingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock DARK_WALL_SIGN = register("dark_wall_sign", properties -> new WallSignBlock(TFWoodTypes.DARK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(DARK_SIGN.getLootTable()).overrideDescription(DARK_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock DARK_HANGING_SIGN = register("dark_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock DARK_WALL_HANGING_SIGN = register("dark_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(1.0F).overrideLootTable(DARK_HANGING_SIGN.getLootTable()).overrideDescription(DARK_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock DARK_BANISTER = registerWithItem("dark_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS));
	public static final DryingRackBlock DARK_DRYING_RACK = registerWithItem("dark_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(DARK_SLAB, 0.5F));

	public static final Block TIME_PLANKS = registerWithItem("time_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock TIME_STAIRS = registerWithItem("time_stairs", properties -> new StairBlock(TIME_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS));
	public static final Block TIME_SLAB = registerWithItem("time_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).sound(SoundType.WOOD));
	public static final Block TIME_BUTTON = registerWithItem("time_button", properties -> new ButtonBlock(TFWoodTypes.TIME_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(0.5F));
	public static final Block TIME_FENCE = registerWithItem("time_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS));
	public static final Block TIME_GATE = registerWithItem("time_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.TIME_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).forceSolidOn());
	public static final Block TIME_PLATE = registerWithItem("time_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.TIME_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock TIME_DOOR = registerWithItem("time_door", properties -> new DoorBlock(TFWoodTypes.TIME_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion());
	public static final TrapDoorBlock TIME_TRAPDOOR = registerWithItem("time_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.TIME_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock TIME_SIGN = register("time_sign", properties -> new StandingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock TIME_WALL_SIGN = register("time_wall_sign", properties -> new WallSignBlock(TFWoodTypes.TIME_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(TIME_SIGN.getLootTable()).overrideDescription(TIME_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock TIME_HANGING_SIGN = register("time_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock TIME_WALL_HANGING_SIGN = register("time_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(1.0F).overrideLootTable(TIME_HANGING_SIGN.getLootTable()).overrideDescription(TIME_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock TIME_BANISTER = registerWithItem("time_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS));
	public static final DryingRackBlock TIME_DRYING_RACK = registerWithItem("time_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(TIME_SLAB, 0.5F));

	public static final Block TRANSFORMATION_PLANKS = registerWithItem("transformation_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock TRANSFORMATION_STAIRS = registerWithItem("transformation_stairs", properties -> new StairBlock(TRANSFORMATION_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS));
	public static final Block TRANSFORMATION_SLAB = registerWithItem("transformation_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS));
	public static final Block TRANSFORMATION_BUTTON = registerWithItem("transformation_button", properties -> new ButtonBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(0.5F));
	public static final Block TRANSFORMATION_FENCE = registerWithItem("transformation_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS));
	public static final Block TRANSFORMATION_GATE = registerWithItem("transformation_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).forceSolidOn());
	public static final Block TRANSFORMATION_PLATE = registerWithItem("transformation_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock TRANSFORMATION_DOOR = registerWithItem("transformation_door", properties -> new DoorBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(3.0F).noOcclusion());
	public static final TrapDoorBlock TRANSFORMATION_TRAPDOOR = registerWithItem("transformation_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock TRANSFORMATION_SIGN = register("transformation_sign", properties -> new StandingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock TRANSFORMATION_WALL_SIGN = register("transformation_wall_sign", properties -> new WallSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(TRANSFORMATION_SIGN.getLootTable()).overrideDescription(TRANSFORMATION_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock TRANSFORMATION_HANGING_SIGN = register("transformation_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock TRANSFORMATION_WALL_HANGING_SIGN = register("transformation_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(1.0F).overrideLootTable(TRANSFORMATION_HANGING_SIGN.getLootTable()).overrideDescription(TRANSFORMATION_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock TRANSFORMATION_BANISTER = registerWithItem("transformation_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS));
	public static final DryingRackBlock TRANSFORMATION_DRYING_RACK = registerWithItem("transformation_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(TRANSFORMATION_SLAB, 0.5F));

	public static final Block MINING_PLANKS = registerWithItem("mining_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock MINING_STAIRS = registerWithItem("mining_stairs", properties -> new StairBlock(MINING_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS));
	public static final Block MINING_SLAB = registerWithItem("mining_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS));
	public static final Block MINING_BUTTON = registerWithItem("mining_button", properties -> new ButtonBlock(TFWoodTypes.MINING_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(0.5F));
	public static final Block MINING_FENCE = registerWithItem("mining_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS));
	public static final Block MINING_GATE = registerWithItem("mining_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.MINING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).forceSolidOn());
	public static final Block MINING_PLATE = registerWithItem("mining_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.MINING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock MINING_DOOR = registerWithItem("mining_door", properties -> new DoorBlock(TFWoodTypes.MINING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(3.0F).noOcclusion());
	public static final TrapDoorBlock MINING_TRAPDOOR = registerWithItem("mining_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.MINING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock MINING_SIGN = register("mining_sign", properties -> new StandingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock MINING_WALL_SIGN = register("mining_wall_sign", properties -> new WallSignBlock(TFWoodTypes.MINING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(MINING_SIGN.getLootTable()).overrideDescription(MINING_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock MINING_HANGING_SIGN = register("mining_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock MINING_WALL_HANGING_SIGN = register("mining_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(1.0F).overrideLootTable(MINING_HANGING_SIGN.getLootTable()).overrideDescription(MINING_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock MINING_BANISTER = registerWithItem("mining_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS));
	public static final DryingRackBlock MINING_DRYING_RACK = registerWithItem("mining_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(MINING_SLAB, 0.5F));

	public static final Block SORTING_PLANKS = registerWithItem("sorting_planks", Block::new, () -> BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final StairBlock SORTING_STAIRS = registerWithItem("sorting_stairs", properties -> new StairBlock(SORTING_PLANKS.defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS));
	public static final Block SORTING_SLAB = registerWithItem("sorting_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS));
	public static final Block SORTING_BUTTON = registerWithItem("sorting_button", properties -> new ButtonBlock(TFWoodTypes.SORTING_WOOD_SET, 30, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(0.5F));
	public static final Block SORTING_FENCE = registerWithItem("sorting_fence", FenceBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS));
	public static final Block SORTING_GATE = registerWithItem("sorting_fence_gate", properties -> new FenceGateBlock(TFWoodTypes.SORTING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).forceSolidOn());
	public static final Block SORTING_PLATE = registerWithItem("sorting_pressure_plate", properties -> new PressurePlateBlock(TFWoodTypes.SORTING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).forceSolidOn().noCollision().strength(0.5F));
	public static final DoorBlock SORTING_DOOR = registerWithItem("sorting_door", properties -> new DoorBlock(TFWoodTypes.SORTING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(3.0F).noOcclusion());
	public static final TrapDoorBlock SORTING_TRAPDOOR = registerWithItem("sorting_trapdoor", properties -> new TrapDoorBlock(TFWoodTypes.SORTING_WOOD_SET, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(3.0F).noOcclusion());
	public static final StandingSignBlock SORTING_SIGN = register("sorting_sign", properties -> new StandingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(1.0F).noOcclusion().noCollision());
	public static final WallSignBlock SORTING_WALL_SIGN = register("sorting_wall_sign", properties -> new WallSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(1.0F).noOcclusion().noCollision().overrideLootTable(SORTING_SIGN.getLootTable()).overrideDescription(SORTING_SIGN.getDescriptionId()));
	public static final CeilingHangingSignBlock SORTING_HANGING_SIGN = register("sorting_hanging_sign", properties -> new CeilingHangingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(1.0F));
	public static final WallHangingSignBlock SORTING_WALL_HANGING_SIGN = register("sorting_wall_hanging_sign", properties -> new WallHangingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(1.0F).overrideLootTable(SORTING_HANGING_SIGN.getLootTable()).overrideDescription(SORTING_HANGING_SIGN.getDescriptionId()));
	public static final BanisterBlock SORTING_BANISTER = registerWithItem("sorting_banister", BanisterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS));
	public static final DryingRackBlock SORTING_DRYING_RACK = registerWithItem("sorting_drying_rack", DryingRackBlock::new, () -> copyAndScaleProperties(SORTING_SLAB, 0.5F));

	public static final TFChestBlock TWILIGHT_OAK_CHEST = registerWithItem("twilight_oak_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(2.5F));
	public static final TFChestBlock CANOPY_CHEST = registerWithItem("canopy_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(2.5F));
	public static final TFChestBlock MANGROVE_CHEST = registerWithItem("mangrove_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(2.5F));
	public static final TFChestBlock DARK_CHEST = registerWithItem("dark_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(2.5F));
	public static final TFChestBlock TIME_CHEST = registerWithItem("time_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(2.5F));
	public static final TFChestBlock TRANSFORMATION_CHEST = registerWithItem("transformation_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(2.5F));
	public static final TFChestBlock MINING_CHEST = registerWithItem("mining_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(2.5F));
	public static final TFChestBlock SORTING_CHEST = registerWithItem("sorting_chest", TFChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(2.5F));

	public static final TFTrappedChestBlock TWILIGHT_OAK_TRAPPED_CHEST = registerWithItem("twilight_oak_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock CANOPY_TRAPPED_CHEST = registerWithItem("canopy_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock MANGROVE_TRAPPED_CHEST = registerWithItem("mangrove_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock DARK_TRAPPED_CHEST = registerWithItem("dark_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock TIME_TRAPPED_CHEST = registerWithItem("time_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock TRANSFORMATION_TRAPPED_CHEST = registerWithItem("transformation_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock MINING_TRAPPED_CHEST = registerWithItem("mining_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(2.5F));
	public static final TFTrappedChestBlock SORTING_TRAPPED_CHEST = registerWithItem("sorting_trapped_chest", TFTrappedChestBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(2.5F));

	//Flower Pots
	public static final FlowerPotBlock POTTED_TWILIGHT_OAK_SAPLING = register("potted_twilight_oak_sapling", properties -> new FlowerPotBlock(TWILIGHT_OAK_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_CANOPY_SAPLING = register("potted_canopy_sapling", properties -> new FlowerPotBlock(CANOPY_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_MANGROVE_SAPLING = register("potted_mangrove_sapling", properties -> new FlowerPotBlock(MANGROVE_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_DARKWOOD_SAPLING = register("potted_darkwood_sapling", properties -> new FlowerPotBlock(DARKWOOD_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_HOLLOW_OAK_SAPLING = register("potted_hollow_oak_sapling", properties -> new FlowerPotBlock(HOLLOW_OAK_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_RAINBOW_OAK_SAPLING = register("potted_rainbow_oak_sapling", properties -> new FlowerPotBlock(RAINBOW_OAK_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_TIME_SAPLING = register("potted_time_sapling", properties -> new FlowerPotBlock(TIME_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_TRANSFORMATION_SAPLING = register("potted_transformation_sapling", properties -> new FlowerPotBlock(TRANSFORMATION_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_MINING_SAPLING = register("potted_mining_sapling", properties -> new FlowerPotBlock(MINING_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_SORTING_SAPLING = register("potted_sorting_sapling", properties -> new FlowerPotBlock(SORTING_SAPLING, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_MAYAPPLE = register("potted_mayapple", properties -> new FlowerPotBlock(MAYAPPLE, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_FIDDLEHEAD = register("potted_fiddlehead", properties -> new FlowerPotBlock(FIDDLEHEAD, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_MUSHGLOOM = register("potted_mushgloom", properties -> new FlowerPotBlock(MUSHGLOOM, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_THORN = register("potted_thorn", properties -> new SpecialFlowerPotBlock(BROWN_THORNS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_GREEN_THORN = register("potted_green_thorn", properties -> new SpecialFlowerPotBlock(GREEN_THORNS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
	public static final FlowerPotBlock POTTED_DEAD_THORN = register("potted_dead_thorn", properties -> new SpecialFlowerPotBlock(BURNT_THORNS, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));

	public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		return Registry.register(BuiltInRegistries.BLOCK, TFMain.prefix(name), block.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK, TFMain.prefix(name)))));
	}

	public static <T extends Block> T registerCustomID(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties, String id) {
		return Registry.register(BuiltInRegistries.BLOCK, TFMain.prefix(name), block.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK, TFMain.prefix(name))).overrideDescription("block.twilightforest." + id)));
	}

	public static <T extends Block> T registerWithItem(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		return registerWithItem(name, block, properties, Item.Properties::new);
	}

	public static <T extends Block> T registerWithItem(String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties, Supplier<Item.Properties> itemProperties) {
		T ret = register(name, block, properties);
		TFItems.register(name, itemProps -> new BlockItem(ret, itemProps.useBlockDescriptionPrefix()), itemProperties);
		return ret;
	}

	public static OminousCandleBlock ominousCandle(String name, MapColor mapColor, Block candle) {
		return Registry.register(
			BuiltInRegistries.BLOCK,
			TFMain.prefix(name),
			new OminousCandleBlock(candle,
				BlockBehaviour.Properties.of()
					.mapColor(mapColor)
					.noOcclusion()
					.strength(0.1F)
					.sound(SoundType.CANDLE)
					.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
					.pushReaction(PushReaction.DESTROY)
					.setId(ResourceKey.create(Registries.BLOCK, TFMain.prefix(name)))
			)
		);
	}

	private static BlockBehaviour.Properties logProperties(MapColor color) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(color);
	}

	private static BlockBehaviour.Properties logProperties(MapColor top, MapColor side) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side);
	}

	public static BlockBehaviour.Properties copyAndScaleProperties(Block block, float scale) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(block);
		return properties.destroyTime(block.defaultDestroyTime() * scale).explosionResistance(block.getExplosionResistance() * scale);
	}

	private static boolean noSpawning(BlockState pState, BlockGetter pLevel, BlockPos pPos, EntityType<?> pValue) {
		return false;
	}
}
