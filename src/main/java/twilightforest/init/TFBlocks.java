package twilightforest.init;

import java.util.function.Supplier;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.ISTER;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.enums.BossVariant;
import twilightforest.enums.FireJetVariant;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.item.FurnaceFuelItem;
import twilightforest.util.TFWoodTypes;
import twilightforest.world.components.feature.trees.growers.*;

import javax.annotation.Nonnull;

@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
@Nonnull
public class TFBlocks {
	public static final LazyRegistrar<Block> BLOCKS = LazyRegistrar.create(Registries.BLOCK, TwilightForestMod.ID);

	public static final RegistryObject<TFPortalBlock> TWILIGHT_PORTAL = BLOCKS.register("twilight_portal", () -> new TFPortalBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel((state) -> 11).noCollission().noOcclusion().noLootTable()));

	//misc.
	public static final RegistryObject<Block> HEDGE = register("hedge", () -> new HedgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> FIREFLY_JAR = register("firefly_jar", () -> new JarBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().noParticlesOnBreak().sound(SoundType.BONE_BLOCK).strength(0.3F, 3.0F)));
	public static final RegistryObject<Block> FIREFLY_SPAWNER = register("firefly_particle_spawner", () -> new FireflySpawnerBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().noParticlesOnBreak().sound(SoundType.BONE_BLOCK).strength(0.3F, 3.0F)));
	public static final RegistryObject<Block> CICADA_JAR = register("cicada_jar", () -> new JarBlock(BlockBehaviour.Properties.of().noOcclusion().noParticlesOnBreak().randomTicks().sound(SoundType.BONE_BLOCK).strength(0.3F, 3.0F)));
	public static final RegistryObject<Block> MOSS_PATCH = register("moss_patch", () -> new MossPatchBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollission().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.MOSS)));
	public static final RegistryObject<Block> MAYAPPLE = register("mayapple", () -> new MayappleBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollission().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> CLOVER_PATCH = register("clover_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().ignitedByLava().noCollission().noOcclusion().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FIDDLEHEAD = register("fiddlehead", () -> new FiddleheadBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollission().noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> MUSHGLOOM = register("mushgloom", () -> new MushgloomBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 3).noCollission().noOcclusion().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.FUNGUS)));
	public static final RegistryObject<Block> TORCHBERRY_PLANT = register("torchberry_plant", () -> new TorchberryPlantBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().noCollission().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS)));
	public static final RegistryObject<Block> ROOT_STRAND = register("root_strand", () -> new RootStrandBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollission().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS)));
	public static final RegistryObject<Block> FALLEN_LEAVES = BLOCKS.register("fallen_leaves", () -> new FallenLeavesBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollission().noOcclusion().replaceable().pushReaction(PushReaction.DESTROY).sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> ROOT_BLOCK = register("root", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
	public static final RegistryObject<Block> LIVEROOT_BLOCK = register("liveroot_block", () -> new LiverootBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD).strength(2.0F, 3.0F)));
	public static final RegistryObject<Block> UNCRAFTING_TABLE = register("uncrafting_table", () -> new UncraftingTableBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.FIRE).sound(SoundType.WOOD).strength(2.5F)));
	public static final RegistryObject<Block> SMOKER = register("smoker", () -> new TFSmokerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.WOOD).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> ENCASED_SMOKER = register("encased_smoker", () -> new EncasedSmokerBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> FIRE_JET = register("fire_jet", () -> new FireJetBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15).mapColor(MapColor.GRASS).randomTicks().sound(SoundType.WOOD).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> ENCASED_FIRE_JET = register("encased_fire_jet", () -> new EncasedFireJetBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 15).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> FIREFLY = BLOCKS.register("firefly", () -> new FireflyBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).noCollission().noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> CICADA = BLOCKS.register("cicada", () -> new CicadaBlock(BlockBehaviour.Properties.of().instabreak().noCollission().noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> MOONWORM = BLOCKS.register("moonworm", () -> new MoonwormBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 14).noCollission().noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<HugeLilyPadBlock> HUGE_LILY_PAD = BLOCKS.register("huge_lily_pad", () -> new HugeLilyPadBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> HUGE_WATER_LILY = BLOCKS.register("huge_water_lily", () -> new HugeWaterLilyBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> SLIDER = register("slider", () -> new SliderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).noLootTable().noOcclusion().randomTicks().strength(2.0F, 10.0F)));
	public static final RegistryObject<Block> IRON_LADDER = register("iron_ladder", () -> new IronLadderBlock(BlockBehaviour.Properties.of().forceSolidOff().noOcclusion().pushReaction(PushReaction.DESTROY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));

	//naga courtyard
	public static final RegistryObject<Block> NAGASTONE_HEAD = register("nagastone_head", () -> new TFHorizontalBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> NAGASTONE = register("nagastone", () -> new NagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> SPIRAL_BRICKS = register("spiral_bricks", () -> new SpiralBrickBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> ETCHED_NAGASTONE = register("etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> NAGASTONE_PILLAR = register("nagastone_pillar", () -> new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> NAGASTONE_STAIRS_LEFT = register("nagastone_stairs_left", () -> new StairBlock(() -> ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> NAGASTONE_STAIRS_RIGHT = register("nagastone_stairs_right", () -> new StairBlock(() -> ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(ETCHED_NAGASTONE.get())));
	public static final RegistryObject<Block> MOSSY_ETCHED_NAGASTONE = register("mossy_etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> MOSSY_NAGASTONE_PILLAR = register("mossy_nagastone_pillar", () -> new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> MOSSY_NAGASTONE_STAIRS_LEFT = register("mossy_nagastone_stairs_left", () -> new StairBlock(() -> MOSSY_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> MOSSY_NAGASTONE_STAIRS_RIGHT = register("mossy_nagastone_stairs_right", () -> new StairBlock(() -> MOSSY_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<Block> CRACKED_ETCHED_NAGASTONE = register("cracked_etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> CRACKED_NAGASTONE_PILLAR = register("cracked_nagastone_pillar", () -> new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> CRACKED_NAGASTONE_STAIRS_LEFT = register("cracked_nagastone_stairs_left", () -> new StairBlock(() -> CRACKED_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> CRACKED_NAGASTONE_STAIRS_RIGHT = register("cracked_nagastone_stairs_right", () -> new StairBlock(() -> CRACKED_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_ETCHED_NAGASTONE.get())));

	//lich tower
	public static final RegistryObject<RotatedPillarBlock> TWISTED_STONE = register("twisted_stone", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> TWISTED_STONE_PILLAR = register("twisted_stone_pillar", () -> new WallPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F), 12, 16));
	public static final RegistryObject<Block> KEEPSAKE_CASKET = registerBEWLR("keepsake_casket", () -> new KeepsakeCasketBlock(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA ? 15 : 0).mapColor(MapColor.COLOR_BLACK).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(50.0F, 1200.0F)));
	public static final RegistryObject<RotatedPillarBlock> BOLD_STONE_PILLAR = register("bold_stone_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> DEATH_TOME_SPAWNER = register("death_tome_spawner", () -> new TomeSpawnerBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.STONE).noLootTable().sound(SoundType.WOOD).strength(2.5F)));
	public static final RegistryObject<Block> EMPTY_CANOPY_BOOKSHELF = register("empty_canopy_bookshelf", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.STONE).sound(SoundType.WOOD).strength(1.5F)));
	public static final RegistryObject<Block> CANDELABRA = register("candelabra", () -> new CandelabraBlock(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(LightableBlock.LIGHTING) == LightableBlock.Lighting.NORMAL ? 15 : state.getValue(LightableBlock.LIGHTING) == LightableBlock.Lighting.OMINOUS ? 7 : 0).mapColor(MapColor.STONE).strength(1.5F)));
	public static final RegistryObject<AbstractSkullCandleBlock> ZOMBIE_SKULL_CANDLE = BLOCKS.register("zombie_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> ZOMBIE_WALL_SKULL_CANDLE = BLOCKS.register("zombie_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_WALL_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> SKELETON_SKULL_CANDLE = BLOCKS.register("skeleton_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL)));
	public static final RegistryObject<AbstractSkullCandleBlock> SKELETON_WALL_SKULL_CANDLE = BLOCKS.register("skeleton_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.copy(Blocks.SKELETON_WALL_SKULL)));
	public static final RegistryObject<AbstractSkullCandleBlock> WITHER_SKELE_SKULL_CANDLE = BLOCKS.register("wither_skeleton_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.copy(Blocks.WITHER_SKELETON_SKULL)));
	public static final RegistryObject<AbstractSkullCandleBlock> WITHER_SKELE_WALL_SKULL_CANDLE = BLOCKS.register("wither_skeleton_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.copy(Blocks.WITHER_SKELETON_WALL_SKULL)));
	public static final RegistryObject<AbstractSkullCandleBlock> CREEPER_SKULL_CANDLE = BLOCKS.register("creeper_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.copy(Blocks.CREEPER_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> CREEPER_WALL_SKULL_CANDLE = BLOCKS.register("creeper_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.copy(Blocks.CREEPER_WALL_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PLAYER_SKULL_CANDLE = BLOCKS.register("player_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PLAYER_WALL_SKULL_CANDLE = BLOCKS.register("player_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.copy(Blocks.PLAYER_WALL_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PIGLIN_SKULL_CANDLE = BLOCKS.register("piglin_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.PIGLIN, BlockBehaviour.Properties.copy(Blocks.PIGLIN_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PIGLIN_WALL_SKULL_CANDLE = BLOCKS.register("piglin_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.PIGLIN, BlockBehaviour.Properties.copy(Blocks.PIGLIN_WALL_HEAD)));
	public static final RegistryObject<WroughtIronFenceBlock> WROUGHT_IRON_FENCE = register("wrought_iron_fence", () -> new WroughtIronFenceBlock(BlockBehaviour.Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
	public static final RegistryObject<Block> WROUGHT_IRON_FINIAL = register("wrought_iron_finial", () -> new WroughtIronFinialBlock(BlockBehaviour.Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));

	//labyrinth
	public static final RegistryObject<Block> MAZESTONE = register("mazestone", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 5.0F)));
	public static final RegistryObject<Block> MAZESTONE_BRICK = register("mazestone_brick", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> CUT_MAZESTONE = register("cut_mazestone", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> DECORATIVE_MAZESTONE = register("decorative_mazestone", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> CRACKED_MAZESTONE = register("cracked_mazestone", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> MOSSY_MAZESTONE = register("mossy_mazestone", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> MAZESTONE_MOSAIC = register("mazestone_mosaic", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> MAZESTONE_BORDER = register("mazestone_border", () -> new Block(BlockBehaviour.Properties.copy(MAZESTONE.get())));
	public static final RegistryObject<Block> RED_THREAD = register("red_thread", () -> new RedThreadBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.FIRE).noCollission().noOcclusion().noParticlesOnBreak().pushReaction(PushReaction.DESTROY)));

	//stronghold
	public static final RegistryObject<Block> STRONGHOLD_SHIELD = register("stronghold_shield", () -> new StrongholdShieldBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(-1.0F, 6000000.0F)));
	public static final RegistryObject<Block> TROPHY_PEDESTAL = register("trophy_pedestal", () -> new TrophyPedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 2000.0F)));
	public static final RegistryObject<Block> UNDERBRICK = register("underbrick", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_BRICKS).strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> MOSSY_UNDERBRICK = register("mossy_underbrick", () -> new Block(BlockBehaviour.Properties.copy(UNDERBRICK.get())));
	public static final RegistryObject<Block> CRACKED_UNDERBRICK = register("cracked_underbrick", () -> new Block(BlockBehaviour.Properties.copy(UNDERBRICK.get())));
	public static final RegistryObject<Block> UNDERBRICK_FLOOR = register("underbrick_floor", () -> new Block(BlockBehaviour.Properties.copy(UNDERBRICK.get())));

	//dark tower
	public static final RegistryObject<Block> TOWERWOOD = register("towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(40.0F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> ENCASED_TOWERWOOD = register("encased_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.copy(TOWERWOOD.get()).mapColor(MapColor.SAND)));
	public static final RegistryObject<Block> CRACKED_TOWERWOOD = register("cracked_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.copy(TOWERWOOD.get())));
	public static final RegistryObject<Block> MOSSY_TOWERWOOD = register("mossy_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.copy(TOWERWOOD.get())));
	public static final RegistryObject<Block> INFESTED_TOWERWOOD = register("infested_towerwood", () -> new InfestedTowerwoodBlock(1, 0, BlockBehaviour.Properties.copy(TOWERWOOD.get()).instrument(NoteBlockInstrument.FLUTE).noLootTable().strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> REAPPEARING_BLOCK = register("reappearing_block", () -> new ReappearingBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 4).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 35.0F)));
	public static final RegistryObject<Block> VANISHING_BLOCK = register("vanishing_block", () -> new VanishingBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(VanishingBlock.ACTIVE) ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(10.0F, 35.0F)));
	public static final RegistryObject<Block> UNBREAKABLE_VANISHING_BLOCK = BLOCKS.register("unbreakable_vanishing_block", () -> new VanishingBlock(BlockBehaviour.Properties.copy(VANISHING_BLOCK.get()).noLootTable().strength(-1.0F, 6000000.0F)));
	public static final RegistryObject<Block> LOCKED_VANISHING_BLOCK = register("locked_vanishing_block", () -> new LockedVanishingBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).sound(SoundType.WOOD).strength(-1.0F, 2000.0F)));
	public static final RegistryObject<Block> CARMINITE_BUILDER = register("carminite_builder", () -> new BuilderBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(BuilderBlock.STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F)));
	public static final RegistryObject<Block> BUILT_BLOCK = BLOCKS.register("built_block", () -> new TranslucentBuiltBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F)));
	public static final RegistryObject<Block> ANTIBUILDER = register("antibuilder", () -> new AntibuilderBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 10).noLootTable().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F)));
	public static final RegistryObject<Block> ANTIBUILT_BLOCK = BLOCKS.register("antibuilt_block", () -> new Block(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(0.3F, 2000.0F)));
	public static final RegistryObject<GhastTrapBlock> GHAST_TRAP = register("ghast_trap", () -> new GhastTrapBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(GhastTrapBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F)));
	public static final RegistryObject<Block> CARMINITE_REACTOR = register("carminite_reactor", () -> new CarminiteReactorBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(CarminiteReactorBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F)));
	public static final RegistryObject<Block> REACTOR_DEBRIS = BLOCKS.register("reactor_debris", () -> new Block(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.ANCIENT_DEBRIS).strength(0.3F, 2000.0F)));
	public static final RegistryObject<Block> FAKE_GOLD = BLOCKS.register("fake_gold", () -> new Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F)));
	public static final RegistryObject<Block> FAKE_DIAMOND = BLOCKS.register("fake_diamond", () -> new Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F)));
	public static final RegistryObject<Block> EXPERIMENT_115 = BLOCKS.register("experiment_115", () -> new Experiment115Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.WOOL).strength(0.5F)));

	//aurora palace
	public static final RegistryObject<Block> AURORA_BLOCK = register("aurora_block", () -> new AuroraBrickBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).strength(10.0F, 6.0F)));
	public static final RegistryObject<RotatedPillarBlock> AURORA_PILLAR = register("aurora_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> AURORA_SLAB = register("aurora_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> AURORALIZED_GLASS = register("auroralized_glass", () -> new AuroralizedGlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));

	//highlands/thornlands
	public static final RegistryObject<Block> BROWN_THORNS = register("brown_thorns", () -> new ThornsBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PODZOL).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F)));
	public static final RegistryObject<Block> GREEN_THORNS = register("green_thorns", () -> new ThornsBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PLANT).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F)));
	public static final RegistryObject<Block> BURNT_THORNS = register("burnt_thorns", () -> new BurntThornsBlock(BlockBehaviour.Properties.of().instabreak().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.DESTROY).sound(SoundType.SAND)));
	public static final RegistryObject<Block> THORN_ROSE = register("thorn_rose", () -> new ThornRoseBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(10.0F, 0.0F)));
	public static final RegistryObject<Block> THORN_LEAVES = register("thorn_leaves", () -> new ThornLeavesBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> BEANSTALK_LEAVES = register("beanstalk_leaves", () -> new BeanstalkLeavesBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> DEADROCK = register("deadrock", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 6000000.0F)));
	public static final RegistryObject<Block> CRACKED_DEADROCK = register("cracked_deadrock", () -> new Block(BlockBehaviour.Properties.copy(DEADROCK.get())));
	public static final RegistryObject<Block> WEATHERED_DEADROCK = register("weathered_deadrock", () -> new Block(BlockBehaviour.Properties.copy(DEADROCK.get())));
	public static final RegistryObject<Block> TROLLSTEINN = register("trollsteinn", () -> new TrollsteinnBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).randomTicks().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 6.0F)));

	public static final RegistryObject<Block> WISPY_CLOUD = register("wispy_cloud", () -> new WispyCloudBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.SNOW).noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.WOOL).strength(0.3F, 0.0F).forceSolidOff(), Biome.Precipitation.NONE));
	public static final RegistryObject<Block> FLUFFY_CLOUD = register("fluffy_cloud", () -> new CloudBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks(), null));
	public static final RegistryObject<Block> RAINY_CLOUD = register("rainy_cloud", () -> new CloudBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks(), Biome.Precipitation.RAIN));
	public static final RegistryObject<Block> SNOWY_CLOUD = register("snowy_cloud", () -> new CloudBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks(), Biome.Precipitation.SNOW));

	public static final RegistryObject<Block> GIANT_COBBLESTONE = register("giant_cobblestone", () -> new GiantBlock(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 50.0F)));
	public static final RegistryObject<Block> GIANT_LOG = register("giant_log", () -> new GiantBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 30.0F)));
	public static final RegistryObject<Block> GIANT_LEAVES = register("giant_leaves", () -> new GiantLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.AZALEA_LEAVES).strength(0.2F * 64.0F, 15.0F)));
	public static final RegistryObject<Block> GIANT_OBSIDIAN = register("giant_obsidian", () -> new GiantBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(50.0F * 64.0F * 64.0F, 2000.0F * 64.0F * 64.0F).isValidSpawn((state, getter, pos, type) -> false)));
	public static final RegistryObject<Block> UBEROUS_SOIL = register("uberous_soil", () -> new UberousSoilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F)));
	public static final RegistryObject<RotatedPillarBlock> HUGE_STALK = register("huge_stalk", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PLANT).sound(SoundType.STEM).strength(1.5F, 3.0F)));
	public static final RegistryObject<Block> BEANSTALK_GROWER = BLOCKS.register("beanstalk_grower", () -> new GrowingBeanstalkBlock(BlockBehaviour.Properties.of().noCollission().noLootTable().noOcclusion().noParticlesOnBreak().strength(-1.0F, 6000000.0F)));
	public static final RegistryObject<Block> HUGE_MUSHGLOOM = register("huge_mushgloom", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.SHROOMLIGHT).strength(0.2F)));
	public static final RegistryObject<Block> HUGE_MUSHGLOOM_STEM = register("huge_mushgloom_stem", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NYLIUM).strength(0.2F)));
	public static final RegistryObject<Block> TROLLVIDR = register("trollvidr", () -> new TrollRootBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA)));
	public static final RegistryObject<Block> UNRIPE_TROLLBER = register("unripe_trollber", () -> new UnripeTorchClusterBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.FLOWERING_AZALEA)));
	public static final RegistryObject<Block> TROLLBER = register("trollber", () -> new TrollRootBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).mapColor(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA)));

	//plateau castle
	public static final RegistryObject<Block> CASTLE_BRICK = register("castle_brick", () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 50.0F)));
	public static final RegistryObject<Block> WORN_CASTLE_BRICK = register("worn_castle_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> CRACKED_CASTLE_BRICK = register("cracked_castle_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> CASTLE_ROOF_TILE = register("castle_roof_tile", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get()).mapColor(MapColor.COLOR_GRAY)));
	public static final RegistryObject<Block> MOSSY_CASTLE_BRICK = register("mossy_castle_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> THICK_CASTLE_BRICK = register("thick_castle_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> ENCASED_CASTLE_BRICK_PILLAR = register("encased_castle_brick_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> ENCASED_CASTLE_BRICK_TILE = register("encased_castle_brick_tile", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> BOLD_CASTLE_BRICK_PILLAR = register("bold_castle_brick_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<Block> BOLD_CASTLE_BRICK_TILE = register("bold_castle_brick_tile", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> CASTLE_BRICK_STAIRS = register("castle_brick_stairs", () -> new StairBlock(() -> CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> WORN_CASTLE_BRICK_STAIRS = register("worn_castle_brick_stairs", () -> new StairBlock(() -> WORN_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(WORN_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> CRACKED_CASTLE_BRICK_STAIRS = register("cracked_castle_brick_stairs", () -> new StairBlock(() -> CRACKED_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> MOSSY_CASTLE_BRICK_STAIRS = register("mossy_castle_brick_stairs", () -> new StairBlock(() -> MOSSY_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> ENCASED_CASTLE_BRICK_STAIRS = register("encased_castle_brick_stairs", () -> new StairBlock(() -> ENCASED_CASTLE_BRICK_PILLAR.get().defaultBlockState(), BlockBehaviour.Properties.copy(ENCASED_CASTLE_BRICK_PILLAR.get())));
	public static final RegistryObject<StairBlock> BOLD_CASTLE_BRICK_STAIRS = register("bold_castle_brick_stairs", () -> new StairBlock(() -> BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState(), BlockBehaviour.Properties.copy(BOLD_CASTLE_BRICK_PILLAR.get())));
	public static final RegistryObject<Block> PINK_CASTLE_RUNE_BRICK = register("pink_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get()).mapColor(DyeColor.MAGENTA)));
	public static final RegistryObject<Block> BLUE_CASTLE_RUNE_BRICK = register("blue_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get()).mapColor(DyeColor.LIGHT_BLUE)));
	public static final RegistryObject<Block> YELLOW_CASTLE_RUNE_BRICK = register("yellow_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get()).mapColor(DyeColor.YELLOW)));
	public static final RegistryObject<Block> VIOLET_CASTLE_RUNE_BRICK = register("violet_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.copy(CASTLE_BRICK.get()).mapColor(DyeColor.PURPLE)));
	public static final RegistryObject<Block> VIOLET_FORCE_FIELD = register("violet_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.PURPLE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F)));
	public static final RegistryObject<Block> PINK_FORCE_FIELD = register("pink_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.MAGENTA).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F)));
	public static final RegistryObject<Block> ORANGE_FORCE_FIELD = register("orange_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.ORANGE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F)));
	public static final RegistryObject<Block> GREEN_FORCE_FIELD = register("green_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.GREEN).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F)));
	public static final RegistryObject<Block> BLUE_FORCE_FIELD = register("blue_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.LIGHT_BLUE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F)));
	public static final RegistryObject<Block> CINDER_FURNACE = register("cinder_furnace", () -> new CinderFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(7.0F).lightLevel((state) -> 15)));
	public static final RegistryObject<RotatedPillarBlock> CINDER_LOG = register("cinder_log", () -> new TFLogBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F)));
	public static final RegistryObject<Block> CINDER_WOOD = register("cinder_wood", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F)));
	public static final RegistryObject<Block> YELLOW_CASTLE_DOOR = register("yellow_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.YELLOW.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F)));
	public static final RegistryObject<Block> VIOLET_CASTLE_DOOR = register("violet_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.LIGHT_BLUE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F)));
	public static final RegistryObject<Block> PINK_CASTLE_DOOR = register("pink_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.MAGENTA.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F)));
	public static final RegistryObject<Block> BLUE_CASTLE_DOOR = register("blue_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.PURPLE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F)));

	//mini structures
	public static final RegistryObject<Block> TWILIGHT_PORTAL_MINIATURE_STRUCTURE = register("twilight_portal_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().requiresCorrectToolForDrops().strength(0.75F)));
//	public static final RegistryObject<Block> HEDGE_MAZE_MINIATURE_STRUCTURE = register("hedge_maze_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> HOLLOW_HILL_MINIATURE_STRUCTURE = register("hollow_hill_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> QUEST_GROVE_MINIATURE_STRUCTURE = register("quest_grove_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> MUSHROOM_TOWER_MINIATURE_STRUCTURE = register("mushroom_tower_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
	public static final RegistryObject<Block> NAGA_COURTYARD_MINIATURE_STRUCTURE = register("naga_courtyard_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
	public static final RegistryObject<Block> LICH_TOWER_MINIATURE_STRUCTURE = register("lich_tower_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE = register("minotaur_labyrinth_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> HYDRA_LAIR_MINIATURE_STRUCTURE = register("hydra_lair_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE = register("goblin_stronghold_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> DARK_TOWER_MINIATURE_STRUCTURE = register("dark_tower_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> YETI_CAVE_MINIATURE_STRUCTURE = register("yeti_cave_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> AURORA_PALACE_MINIATURE_STRUCTURE = register("aurora_palace_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE = register("troll_cave_cottage_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));
//	public static final RegistryObject<Block> FINAL_CASTLE_MINIATURE_STRUCTURE = register("final_castle_miniature_structure", () -> new MiniatureStructureBlock(BlockBehaviour.Properties.copy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())));

	//storage blocks
	public static final RegistryObject<Block> KNIGHTMETAL_BLOCK = register("knightmetal_block", () -> new KnightmetalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 40.0F)));
	public static final RegistryObject<Block> IRONWOOD_BLOCK = register("ironwood_block", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(5.0F, 6.0F)));
	public static final RegistryObject<Block> FIERY_BLOCK = registerFireResistantItem("fiery_block", () -> new FieryBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F).emissiveRendering((state, world, pos) -> true)));
	public static final RegistryObject<Block> STEELEAF_BLOCK = register("steeleaf_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.MOSS).strength(5.0F, 6.0F)));
	public static final RegistryObject<Block> ARCTIC_FUR_BLOCK = register("arctic_fur_block", () -> new ArcticFurBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(0.8F)));
	public static final RegistryObject<Block> CARMINITE_BLOCK = register("carminite_block", () -> new CarminiteBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.COLOR_RED).sound(SoundType.SLIME_BLOCK)));

	//boss trophies and spawners
	public static final RegistryObject<Block> NAGA_BOSS_SPAWNER = register("naga_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.NAGA));
	public static final RegistryObject<Block> LICH_BOSS_SPAWNER = register("lich_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.LICH));
	public static final RegistryObject<Block> HYDRA_BOSS_SPAWNER = register("hydra_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.HYDRA));
	public static final RegistryObject<Block> UR_GHAST_BOSS_SPAWNER = register("ur_ghast_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.UR_GHAST));
	public static final RegistryObject<Block> KNIGHT_PHANTOM_BOSS_SPAWNER = register("knight_phantom_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<Block> SNOW_QUEEN_BOSS_SPAWNER = register("snow_queen_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.SNOW_QUEEN));
	public static final RegistryObject<Block> MINOSHROOM_BOSS_SPAWNER = register("minoshroom_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.MINOSHROOM));
	public static final RegistryObject<Block> ALPHA_YETI_BOSS_SPAWNER = register("alpha_yeti_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.ALPHA_YETI));
	public static final RegistryObject<Block> FINAL_BOSS_BOSS_SPAWNER = register("final_boss_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().noOcclusion().strength(-1.0F, 3600000.8F), BossVariant.FINAL_BOSS));
	public static final RegistryObject<TrophyBlock> NAGA_TROPHY = BLOCKS.register("naga_trophy", () -> new TrophyBlock(BossVariant.NAGA, 5));
	public static final RegistryObject<TrophyBlock> LICH_TROPHY = BLOCKS.register("lich_trophy", () -> new TrophyBlock(BossVariant.LICH, 6));
	public static final RegistryObject<TrophyBlock> HYDRA_TROPHY = BLOCKS.register("hydra_trophy", () -> new TrophyBlock(BossVariant.HYDRA, 12));
	public static final RegistryObject<TrophyBlock> UR_GHAST_TROPHY = BLOCKS.register("ur_ghast_trophy", () -> new TrophyBlock(BossVariant.UR_GHAST, 13));
	public static final RegistryObject<TrophyBlock> KNIGHT_PHANTOM_TROPHY = BLOCKS.register("knight_phantom_trophy", () -> new TrophyBlock(BossVariant.KNIGHT_PHANTOM, 8));
	public static final RegistryObject<TrophyBlock> SNOW_QUEEN_TROPHY = BLOCKS.register("snow_queen_trophy", () -> new TrophyBlock(BossVariant.SNOW_QUEEN, 14));
	public static final RegistryObject<TrophyBlock> MINOSHROOM_TROPHY = BLOCKS.register("minoshroom_trophy", () -> new TrophyBlock(BossVariant.MINOSHROOM, 7));
	public static final RegistryObject<TrophyBlock> ALPHA_YETI_TROPHY = BLOCKS.register("alpha_yeti_trophy", () -> new TrophyBlock(BossVariant.ALPHA_YETI, 9));
	public static final RegistryObject<TrophyBlock> QUEST_RAM_TROPHY = BLOCKS.register("quest_ram_trophy", () -> new TrophyBlock(BossVariant.QUEST_RAM, 1));
	public static final RegistryObject<TrophyWallBlock> NAGA_WALL_TROPHY = BLOCKS.register("naga_wall_trophy", () -> new TrophyWallBlock(BossVariant.NAGA));
	public static final RegistryObject<TrophyWallBlock> LICH_WALL_TROPHY = BLOCKS.register("lich_wall_trophy", () -> new TrophyWallBlock(BossVariant.LICH));
	public static final RegistryObject<TrophyWallBlock> HYDRA_WALL_TROPHY = BLOCKS.register("hydra_wall_trophy", () -> new TrophyWallBlock(BossVariant.HYDRA));
	public static final RegistryObject<TrophyWallBlock> UR_GHAST_WALL_TROPHY = BLOCKS.register("ur_ghast_wall_trophy", () -> new TrophyWallBlock(BossVariant.UR_GHAST));
	public static final RegistryObject<TrophyWallBlock> KNIGHT_PHANTOM_WALL_TROPHY = BLOCKS.register("knight_phantom_wall_trophy", () -> new TrophyWallBlock(BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<TrophyWallBlock> SNOW_QUEEN_WALL_TROPHY = BLOCKS.register("snow_queen_wall_trophy", () -> new TrophyWallBlock(BossVariant.SNOW_QUEEN));
	public static final RegistryObject<TrophyWallBlock> MINOSHROOM_WALL_TROPHY = BLOCKS.register("minoshroom_wall_trophy", () -> new TrophyWallBlock(BossVariant.MINOSHROOM));
	public static final RegistryObject<TrophyWallBlock> ALPHA_YETI_WALL_TROPHY = BLOCKS.register("alpha_yeti_wall_trophy", () -> new TrophyWallBlock(BossVariant.ALPHA_YETI));
	public static final RegistryObject<TrophyWallBlock> QUEST_RAM_WALL_TROPHY = BLOCKS.register("quest_ram_wall_trophy", () -> new TrophyWallBlock(BossVariant.QUEST_RAM));

	// TODO Enumify all of the dang tree stuff

	//all tree related stuff
	public static final RegistryObject<BanisterBlock> OAK_BANISTER = registerBurningItem("oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<BanisterBlock> SPRUCE_BANISTER = registerBurningItem("spruce_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<BanisterBlock> BIRCH_BANISTER = registerBurningItem("birch_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<BanisterBlock> JUNGLE_BANISTER = registerBurningItem("jungle_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<BanisterBlock> ACACIA_BANISTER = registerBurningItem("acacia_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<BanisterBlock> DARK_OAK_BANISTER = registerBurningItem("dark_oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<BanisterBlock> CRIMSON_BANISTER = registerBurningItem("crimson_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<BanisterBlock> WARPED_BANISTER = registerBurningItem("warped_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<BanisterBlock> VANGROVE_BANISTER = registerBurningItem("vangrove_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_PLANKS)));
	public static final RegistryObject<BanisterBlock> BAMBOO_BANISTER = registerBurningItem("bamboo_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
	public static final RegistryObject<BanisterBlock> CHERRY_BANISTER = registerBurningItem("cherry_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_PLANKS)));

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

	public static final RegistryObject<RotatedPillarBlock> TWILIGHT_OAK_LOG = register("twilight_oak_log", () -> new TFLogBlock(TWILIGHT_OAK_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> CANOPY_LOG = register("canopy_log", () -> new TFLogBlock(CANOPY_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MANGROVE_LOG = register("mangrove_log", () -> new TFLogBlock(MANGROVE_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> DARK_LOG = register("dark_log", () -> new TFLogBlock(DARK_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TIME_LOG = register("time_log", () -> new TFLogBlock(TIME_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TRANSFORMATION_LOG = register("transformation_log", () -> new TFLogBlock(TRANSFORMATION_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MINING_LOG = register("mining_log", () -> new TFLogBlock(MINING_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> SORTING_LOG = register("sorting_log", () -> new TFLogBlock(SORTING_LOG_PROPS));

	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_twilight_oak_log_horizontal", () -> new HollowLogHorizontal(TWILIGHT_OAK_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_CANOPY_LOG_HORIZONTAL = BLOCKS.register("hollow_canopy_log_horizontal", () -> new HollowLogHorizontal(CANOPY_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_MANGROVE_LOG_HORIZONTAL = BLOCKS.register("hollow_mangrove_log_horizontal", () -> new HollowLogHorizontal(MANGROVE_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_DARK_LOG_HORIZONTAL = BLOCKS.register("hollow_dark_log_horizontal", () -> new HollowLogHorizontal(DARK_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TIME_LOG_HORIZONTAL = BLOCKS.register("hollow_time_log_horizontal", () -> new HollowLogHorizontal(TIME_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TRANSFORMATION_LOG_HORIZONTAL = BLOCKS.register("hollow_transformation_log_horizontal", () -> new HollowLogHorizontal(TRANSFORMATION_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_MINING_LOG_HORIZONTAL = BLOCKS.register("hollow_mining_log_horizontal", () -> new HollowLogHorizontal(MINING_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_SORTING_LOG_HORIZONTAL = BLOCKS.register("hollow_sorting_log_horizontal", () -> new HollowLogHorizontal(SORTING_BARK_PROPS));

	public static final RegistryObject<HollowLogVertical> HOLLOW_TWILIGHT_OAK_LOG_VERTICAL = BLOCKS.register("hollow_twilight_oak_log_vertical", () -> new HollowLogVertical(TWILIGHT_OAK_STRIPPED_PROPS, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_CANOPY_LOG_VERTICAL = BLOCKS.register("hollow_canopy_log_vertical", () -> new HollowLogVertical(CANOPY_STRIPPED_PROPS, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_MANGROVE_LOG_VERTICAL = BLOCKS.register("hollow_mangrove_log_vertical", () -> new HollowLogVertical(MANGROVE_STRIPPED_PROPS, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_DARK_LOG_VERTICAL = BLOCKS.register("hollow_dark_log_vertical", () -> new HollowLogVertical(DARK_STRIPPED_PROPS, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_TIME_LOG_VERTICAL = BLOCKS.register("hollow_time_log_vertical", () -> new HollowLogVertical(TIME_STRIPPED_PROPS, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_TRANSFORMATION_LOG_VERTICAL = BLOCKS.register("hollow_transformation_log_vertical", () -> new HollowLogVertical(TRANSFORMATION_STRIPPED_PROPS, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_MINING_LOG_VERTICAL = BLOCKS.register("hollow_mining_log_vertical", () -> new HollowLogVertical(MINING_STRIPPED_PROPS, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_SORTING_LOG_VERTICAL = BLOCKS.register("hollow_sorting_log_vertical", () -> new HollowLogVertical(SORTING_STRIPPED_PROPS, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE));

	public static final RegistryObject<HollowLogClimbable> HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_twilight_oak_log_climbable", () -> new HollowLogClimbable(TWILIGHT_OAK_STRIPPED_PROPS, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_CANOPY_LOG_CLIMBABLE = BLOCKS.register("hollow_canopy_log_climbable", () -> new HollowLogClimbable(CANOPY_STRIPPED_PROPS, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_MANGROVE_LOG_CLIMBABLE = BLOCKS.register("hollow_mangrove_log_climbable", () -> new HollowLogClimbable(MANGROVE_STRIPPED_PROPS, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_DARK_LOG_CLIMBABLE = BLOCKS.register("hollow_dark_log_climbable", () -> new HollowLogClimbable(DARK_STRIPPED_PROPS, TFBlocks.HOLLOW_DARK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_TIME_LOG_CLIMBABLE = BLOCKS.register("hollow_time_log_climbable", () -> new HollowLogClimbable(TIME_STRIPPED_PROPS, TFBlocks.HOLLOW_TIME_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_TRANSFORMATION_LOG_CLIMBABLE = BLOCKS.register("hollow_transformation_log_climbable", () -> new HollowLogClimbable(TRANSFORMATION_STRIPPED_PROPS, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_MINING_LOG_CLIMBABLE = BLOCKS.register("hollow_mining_log_climbable", () -> new HollowLogClimbable(MINING_STRIPPED_PROPS, TFBlocks.HOLLOW_MINING_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_SORTING_LOG_CLIMBABLE = BLOCKS.register("hollow_sorting_log_climbable", () -> new HollowLogClimbable(SORTING_STRIPPED_PROPS, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL));

	public static final RegistryObject<HollowLogHorizontal> HOLLOW_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_oak_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_SPRUCE_LOG_HORIZONTAL = BLOCKS.register("hollow_spruce_log_horizontal", () -> new HollowLogHorizontal((BlockBehaviour.Properties.copy(Blocks.SPRUCE_WOOD))));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_BIRCH_LOG_HORIZONTAL = BLOCKS.register("hollow_birch_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.BIRCH_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_JUNGLE_LOG_HORIZONTAL = BLOCKS.register("hollow_jungle_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.JUNGLE_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_ACACIA_LOG_HORIZONTAL = BLOCKS.register("hollow_acacia_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_DARK_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_dark_oak_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_CRIMSON_STEM_HORIZONTAL = BLOCKS.register("hollow_crimson_stem_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.CRIMSON_HYPHAE)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_WARPED_STEM_HORIZONTAL = BLOCKS.register("hollow_warped_stem_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.WARPED_HYPHAE)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_VANGROVE_LOG_HORIZONTAL = BLOCKS.register("hollow_vangrove_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.MANGROVE_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_CHERRY_LOG_HORIZONTAL = BLOCKS.register("hollow_cherry_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.CHERRY_WOOD)));

	public static final RegistryObject<HollowLogVertical> HOLLOW_OAK_LOG_VERTICAL = BLOCKS.register("hollow_oak_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_SPRUCE_LOG_VERTICAL = BLOCKS.register("hollow_spruce_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_BIRCH_LOG_VERTICAL = BLOCKS.register("hollow_birch_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_JUNGLE_LOG_VERTICAL = BLOCKS.register("hollow_jungle_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_WOOD), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_ACACIA_LOG_VERTICAL = BLOCKS.register("hollow_acacia_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_ACACIA_WOOD), TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_DARK_OAK_LOG_VERTICAL = BLOCKS.register("hollow_dark_oak_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_CRIMSON_STEM_VERTICAL = BLOCKS.register("hollow_crimson_stem_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_HYPHAE), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_WARPED_STEM_VERTICAL = BLOCKS.register("hollow_warped_stem_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_HYPHAE), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE));
	// wanna see a funny crash? Use BlockBehaviour.Properties.copy(Blocks.STRIPPED_MANGROVE_WOOD) instead of the BlockBehaviour.Properties.of(...)
	// I still legit have no idea why it happens but it does
	public static final RegistryObject<HollowLogVertical> HOLLOW_VANGROVE_LOG_VERTICAL = BLOCKS.register("hollow_vangrove_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD), TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_CHERRY_LOG_VERTICAL = BLOCKS.register("hollow_cherry_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CHERRY_WOOD), TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE));

	public static final RegistryObject<HollowLogClimbable> HOLLOW_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_oak_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD), TFBlocks.HOLLOW_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_SPRUCE_LOG_CLIMBABLE = BLOCKS.register("hollow_spruce_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD), TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_BIRCH_LOG_CLIMBABLE = BLOCKS.register("hollow_birch_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD), TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_JUNGLE_LOG_CLIMBABLE = BLOCKS.register("hollow_jungle_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_WOOD), TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_ACACIA_LOG_CLIMBABLE = BLOCKS.register("hollow_acacia_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_ACACIA_WOOD), TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_DARK_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_dark_oak_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD), TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_CRIMSON_STEM_CLIMBABLE = BLOCKS.register("hollow_crimson_stem_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_HYPHAE), TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_WARPED_STEM_CLIMBABLE = BLOCKS.register("hollow_warped_stem_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_HYPHAE), TFBlocks.HOLLOW_WARPED_STEM_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_VANGROVE_LOG_CLIMBABLE = BLOCKS.register("hollow_vangrove_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD), TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_CHERRY_LOG_CLIMBABLE = BLOCKS.register("hollow_cherry_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CHERRY_WOOD), TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL));

	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TWILIGHT_OAK_LOG = register("stripped_twilight_oak_log", () -> new TFLogBlock(TWILIGHT_OAK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CANOPY_LOG = register("stripped_canopy_log", () -> new TFLogBlock(CANOPY_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_LOG = register("stripped_mangrove_log", () -> new TFLogBlock(MANGROVE_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DARK_LOG = register("stripped_dark_log", () -> new TFLogBlock(DARK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TIME_LOG = register("stripped_time_log", () -> new TFLogBlock(TIME_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TRANSFORMATION_LOG = register("stripped_transformation_log", () -> new TFLogBlock(TRANSFORMATION_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MINING_LOG = register("stripped_mining_log", () -> new TFLogBlock(MINING_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_SORTING_LOG = register("stripped_sorting_log", () -> new TFLogBlock(SORTING_STRIPPED_PROPS));

	public static final RegistryObject<RotatedPillarBlock> TWILIGHT_OAK_WOOD = register("twilight_oak_wood", () -> new TFLogBlock(TWILIGHT_OAK_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> CANOPY_WOOD = register("canopy_wood", () -> new TFLogBlock(CANOPY_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MANGROVE_WOOD = register("mangrove_wood", () -> new TFLogBlock(MANGROVE_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> DARK_WOOD = register("dark_wood", () -> new TFLogBlock(DARK_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TIME_WOOD = register("time_wood", () -> new TFLogBlock(TIME_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TRANSFORMATION_WOOD = register("transformation_wood", () -> new TFLogBlock(TRANSFORMATION_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MINING_WOOD = register("mining_wood", () -> new TFLogBlock(MINING_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> SORTING_WOOD = register("sorting_wood", () -> new TFLogBlock(SORTING_BARK_PROPS));

	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TWILIGHT_OAK_WOOD = register("stripped_twilight_oak_wood", () -> new TFLogBlock(TWILIGHT_OAK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CANOPY_WOOD = register("stripped_canopy_wood", () -> new TFLogBlock(CANOPY_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_WOOD = register("stripped_mangrove_wood", () -> new TFLogBlock(MANGROVE_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DARK_WOOD = register("stripped_dark_wood", () -> new TFLogBlock(DARK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TIME_WOOD = register("stripped_time_wood", () -> new TFLogBlock(TIME_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TRANSFORMATION_WOOD = register("stripped_transformation_wood", () -> new TFLogBlock(TRANSFORMATION_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MINING_WOOD = register("stripped_mining_wood", () -> new TFLogBlock(MINING_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_SORTING_WOOD = register("stripped_sorting_wood", () -> new TFLogBlock(SORTING_STRIPPED_PROPS));

	public static final RegistryObject<Block> TIME_LOG_CORE = register("time_log_core", () -> new TimeLogCoreBlock(TIME_LOG_PROPS));
	public static final RegistryObject<Block> TRANSFORMATION_LOG_CORE = register("transformation_log_core", () -> new TransLogCoreBlock(TRANSFORMATION_LOG_PROPS));
	public static final RegistryObject<Block> MINING_LOG_CORE = register("mining_log_core", () -> new MineLogCoreBlock(MINING_LOG_PROPS));
	public static final RegistryObject<Block> SORTING_LOG_CORE = register("sorting_log_core", () -> new SortLogCoreBlock(SORTING_LOG_PROPS));

	public static final RegistryObject<Block> MANGROVE_ROOT = register("mangrove_root", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.STONE).sound(SoundType.WOOD).strength(2.0F)));

	public static final RegistryObject<Block> TWILIGHT_OAK_LEAVES = register("twilight_oak_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> CANOPY_LEAVES = register("canopy_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> MANGROVE_LEAVES = register("mangrove_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> DARK_LEAVES = register("dark_leaves", () -> new DarkLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> HARDENED_DARK_LEAVES = BLOCKS.register("hardened_dark_leaves", () -> new HardenedDarkLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> RAINBOW_OAK_LEAVES = register("rainbow_oak_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> TIME_LEAVES = register("time_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> TRANSFORMATION_LEAVES = register("transformation_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> MINING_LEAVES = register("mining_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));
	public static final RegistryObject<Block> SORTING_LEAVES = register("sorting_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false)));

	public static final RegistryObject<SaplingBlock> TWILIGHT_OAK_SAPLING = register("twilight_oak_sapling", () -> new SaplingBlock(new SmallOakTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> CANOPY_SAPLING = register("canopy_sapling", () -> new SaplingBlock(new CanopyTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> MANGROVE_SAPLING = register("mangrove_sapling", () -> new MangroveSaplingBlock(new MangroveTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> DARKWOOD_SAPLING = register("darkwood_sapling", () -> new SaplingBlock(new DarkCanopyTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> HOLLOW_OAK_SAPLING = register("hollow_oak_sapling", () -> new SaplingBlock(new HollowTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> TIME_SAPLING = register("time_sapling", () -> new SaplingBlock(new TimeTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> TRANSFORMATION_SAPLING = register("transformation_sapling", () -> new SaplingBlock(new TransformationTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> MINING_SAPLING = register("mining_sapling", () -> new SaplingBlock(new MiningTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> SORTING_SAPLING = register("sorting_sapling", () -> new SaplingBlock(new SortingTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> RAINBOW_OAK_SAPLING = register("rainbow_oak_sapling", () -> new SaplingBlock(new RainboakTreeGrower(), BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollission().randomTicks()));

	public static final RegistryObject<Block> TWILIGHT_OAK_PLANKS = register("twilight_oak_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TWILIGHT_OAK_STAIRS = register("twilight_oak_stairs", () -> new StairBlock(() -> TWILIGHT_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_SLAB = register("twilight_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_BUTTON = register("twilight_oak_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TWILIGHT_OAK_SET, 30, true));
	public static final RegistryObject<Block> TWILIGHT_OAK_FENCE = registerBurningItem("twilight_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_GATE = registerBurningItem("twilight_oak_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()), TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE));
	public static final RegistryObject<Block> TWILIGHT_OAK_PLATE = register("twilight_oak_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TWILIGHT_OAK_SET));
	public static final RegistryObject<DoorBlock> TWILIGHT_OAK_DOOR = registerDoubleBlockItem("twilight_oak_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), TFWoodTypes.TWILIGHT_OAK_SET));
	public static final RegistryObject<TrapDoorBlock> TWILIGHT_OAK_TRAPDOOR = register("twilight_oak_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.TWILIGHT_OAK_SET));
	public static final RegistryObject<StandingSignBlock> TWILIGHT_OAK_SIGN = BLOCKS.register("twilight_oak_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).noOcclusion().noCollission(), TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> TWILIGHT_WALL_SIGN = BLOCKS.register("twilight_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).noOcclusion().noCollission(), TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> TWILIGHT_OAK_HANGING_SIGN = BLOCKS.register("twilight_oak_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> TWILIGHT_OAK_WALL_HANGING_SIGN = BLOCKS.register("twilight_oak_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> TWILIGHT_OAK_BANISTER = registerBurningItem("twilight_oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));

	public static final RegistryObject<Block> CANOPY_PLANKS = register("canopy_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> CANOPY_STAIRS = register("canopy_stairs", () -> new StairBlock(() -> CANOPY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_SLAB = register("canopy_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_BUTTON = register("canopy_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.CANOPY_WOOD_SET, 30, true));
	public static final RegistryObject<Block> CANOPY_FENCE = registerBurningItem("canopy_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_GATE = registerBurningItem("canopy_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()), TFWoodTypes.CANOPY_WOOD_TYPE));
	public static final RegistryObject<Block> CANOPY_PLATE = register("canopy_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.CANOPY_WOOD_SET));
	public static final RegistryObject<DoorBlock> CANOPY_DOOR = registerDoubleBlockItem("canopy_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.CANOPY_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> CANOPY_TRAPDOOR = register("canopy_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), TFWoodTypes.CANOPY_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> CANOPY_SIGN = BLOCKS.register("canopy_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.CANOPY_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> CANOPY_WALL_SIGN = BLOCKS.register("canopy_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.CANOPY_WOOD_TYPE));
	public static final RegistryObject<Block> CANOPY_BOOKSHELF = register("canopy_bookshelf", () -> new BookshelfBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(1.5F)));
	public static final RegistryObject<CeilingHangingSignBlock> CANOPY_HANGING_SIGN = BLOCKS.register("canopy_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.CANOPY_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> CANOPY_WALL_HANGING_SIGN = BLOCKS.register("canopy_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.CANOPY_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> CANOPY_BANISTER = registerBurningItem("canopy_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));

	public static final RegistryObject<Block> MANGROVE_PLANKS = register("mangrove_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> MANGROVE_STAIRS = register("mangrove_stairs", () -> new StairBlock(() -> MANGROVE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_SLAB = register("mangrove_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_BUTTON = register("mangrove_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.MANGROVE_WOOD_SET, 30, true));
	public static final RegistryObject<Block> MANGROVE_FENCE = registerBurningItem("mangrove_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_GATE = registerBurningItem("mangrove_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()), TFWoodTypes.MANGROVE_WOOD_TYPE));
	public static final RegistryObject<Block> MANGROVE_PLATE = register("mangrove_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.MANGROVE_WOOD_SET));
	public static final RegistryObject<DoorBlock> MANGROVE_DOOR = registerDoubleBlockItem("mangrove_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.MANGROVE_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> MANGROVE_TRAPDOOR = register("mangrove_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.MANGROVE_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> MANGROVE_SIGN = BLOCKS.register("mangrove_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.MANGROVE_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> MANGROVE_WALL_SIGN = BLOCKS.register("mangrove_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.MANGROVE_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> MANGROVE_HANGING_SIGN = BLOCKS.register("mangrove_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.MANGROVE_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> MANGROVE_WALL_HANGING_SIGN = BLOCKS.register("mangrove_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.MANGROVE_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> MANGROVE_BANISTER = registerBurningItem("mangrove_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));

	public static final RegistryObject<Block> DARK_PLANKS = register("dark_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> DARK_STAIRS = register("dark_stairs", () -> new StairBlock(() -> DARK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> DARK_SLAB = register("dark_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DARK_BUTTON = register("dark_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.DARK_WOOD_SET, 30, true));
	public static final RegistryObject<Block> DARK_FENCE = registerBurningItem("dark_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> DARK_GATE = registerBurningItem("dark_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()), TFWoodTypes.DARK_WOOD_TYPE));
	public static final RegistryObject<Block> DARK_PLATE = register("dark_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.DARK_WOOD_SET));
	public static final RegistryObject<DoorBlock> DARK_DOOR = registerDoubleBlockItem("dark_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), TFWoodTypes.DARK_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> DARK_TRAPDOOR = register("dark_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.DARK_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> DARK_SIGN = BLOCKS.register("dark_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.DARK_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> DARK_WALL_SIGN = BLOCKS.register("dark_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.DARK_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> DARK_HANGING_SIGN = BLOCKS.register("dark_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.DARK_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> DARK_WALL_HANGING_SIGN = BLOCKS.register("dark_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.DARK_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> DARK_BANISTER = registerBurningItem("dark_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get())));

	public static final RegistryObject<Block> TIME_PLANKS = register("time_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TIME_STAIRS = register("time_stairs", () -> new StairBlock(() -> TIME_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TIME_SLAB = register("time_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_BUTTON = register("time_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TIME_WOOD_SET, 30, true));
	public static final RegistryObject<Block> TIME_FENCE = registerBurningItem("time_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TIME_GATE = registerBurningItem("time_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()), TFWoodTypes.TIME_WOOD_TYPE));
	public static final RegistryObject<Block> TIME_PLATE = register("time_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TIME_WOOD_SET));
	public static final RegistryObject<DoorBlock> TIME_DOOR = registerDoubleBlockItem("time_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), TFWoodTypes.TIME_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> TIME_TRAPDOOR = register("time_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.TIME_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> TIME_SIGN = BLOCKS.register("time_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.TIME_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> TIME_WALL_SIGN = BLOCKS.register("time_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.TIME_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> TIME_HANGING_SIGN = BLOCKS.register("time_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TIME_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> TIME_WALL_HANGING_SIGN = BLOCKS.register("time_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TIME_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> TIME_BANISTER = registerBurningItem("time_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get())));

	public static final RegistryObject<Block> TRANSFORMATION_PLANKS = register("transformation_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TRANSFORMATION_STAIRS = register("transformation_stairs", () -> new StairBlock(() -> TRANSFORMATION_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_SLAB = register("transformation_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_BUTTON = register("transformation_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TRANSFORMATION_WOOD_SET, 30, true));
	public static final RegistryObject<Block> TRANSFORMATION_FENCE = registerBurningItem("transformation_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_GATE = registerBurningItem("transformation_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()), TFWoodTypes.TRANSFORMATION_WOOD_TYPE));
	public static final RegistryObject<Block> TRANSFORMATION_PLATE = register("transformation_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.TRANSFORMATION_WOOD_SET));
	public static final RegistryObject<DoorBlock> TRANSFORMATION_DOOR = registerDoubleBlockItem("transformation_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.TRANSFORMATION_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> TRANSFORMATION_TRAPDOOR = register("transformation_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.TRANSFORMATION_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> TRANSFORMATION_SIGN = BLOCKS.register("transformation_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.TRANSFORMATION_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> TRANSFORMATION_WALL_SIGN = BLOCKS.register("transformation_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.TRANSFORMATION_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> TRANSFORMATION_HANGING_SIGN = BLOCKS.register("transformation_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TRANSFORMATION_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> TRANSFORMATION_WALL_HANGING_SIGN = BLOCKS.register("transformation_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.TRANSFORMATION_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> TRANSFORMATION_BANISTER = registerBurningItem("transformation_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));

	public static final RegistryObject<Block> MINING_PLANKS = register("mining_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> MINING_STAIRS = register("mining_stairs", () -> new StairBlock(() -> MINING_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_SLAB = register("mining_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_BUTTON = register("mining_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.MINING_WOOD_SET, 30, true));
	public static final RegistryObject<Block> MINING_FENCE = registerBurningItem("mining_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_GATE = registerBurningItem("mining_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()), TFWoodTypes.MINING_WOOD_TYPE));
	public static final RegistryObject<Block> MINING_PLATE = register("mining_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.MINING_WOOD_SET));
	public static final RegistryObject<DoorBlock> MINING_DOOR = registerDoubleBlockItem("mining_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.MINING_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> MINING_TRAPDOOR = register("mining_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.MINING_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> MINING_SIGN = BLOCKS.register("mining_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.MINING_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> MINING_WALL_SIGN = BLOCKS.register("mining_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.MINING_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> MINING_HANGING_SIGN = BLOCKS.register("mining_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.MINING_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> MINING_WALL_HANGING_SIGN = BLOCKS.register("mining_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.MINING_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> MINING_BANISTER = registerBurningItem("mining_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));

	public static final RegistryObject<Block> SORTING_PLANKS = register("sorting_planks", () -> new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> SORTING_STAIRS = register("sorting_stairs", () -> new StairBlock(() -> SORTING_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_SLAB = register("sorting_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_BUTTON = register("sorting_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.SORTING_WOOD_SET, 30, true));
	public static final RegistryObject<Block> SORTING_FENCE = registerBurningItem("sorting_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_GATE = registerBurningItem("sorting_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()), TFWoodTypes.SORTING_WOOD_TYPE));
	public static final RegistryObject<Block> SORTING_PLATE = register("sorting_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(0.5F), TFWoodTypes.SORTING_WOOD_SET));
	public static final RegistryObject<DoorBlock> SORTING_DOOR = registerDoubleBlockItem("sorting_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.SORTING_WOOD_SET));
	public static final RegistryObject<TrapDoorBlock> SORTING_TRAPDOOR = register("sorting_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).noOcclusion(), TFWoodTypes.SORTING_WOOD_SET));
	public static final RegistryObject<StandingSignBlock> SORTING_SIGN = BLOCKS.register("sorting_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.SORTING_WOOD_TYPE));
	public static final RegistryObject<WallSignBlock> SORTING_WALL_SIGN = BLOCKS.register("sorting_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(1.0F).noOcclusion().noCollission(), TFWoodTypes.SORTING_WOOD_TYPE));
	public static final RegistryObject<CeilingHangingSignBlock> SORTING_HANGING_SIGN = BLOCKS.register("sorting_hanging_sign", () -> new TFCeilingHangingSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.SORTING_WOOD_TYPE));
	public static final RegistryObject<WallHangingSignBlock> SORTING_WALL_HANGING_SIGN = BLOCKS.register("sorting_wall_hanging_sign", () -> new TFWallHangingSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(1.0F), TFWoodTypes.SORTING_WOOD_TYPE));
	public static final RegistryObject<BanisterBlock> SORTING_BANISTER = registerBurningItem("sorting_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));

	public static final RegistryObject<TFChestBlock> TWILIGHT_OAK_CHEST = registerBEWLR("twilight_oak_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> CANOPY_CHEST = registerBEWLR("canopy_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> MANGROVE_CHEST = registerBEWLR("mangrove_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> DARK_CHEST = registerBEWLR("dark_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> TIME_CHEST = registerBEWLR("time_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> TRANSFORMATION_CHEST = registerBEWLR("transformation_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> MINING_CHEST = registerBEWLR("mining_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(2.5F)));
	public static final RegistryObject<TFChestBlock> SORTING_CHEST = registerBEWLR("sorting_chest", () -> new TFChestBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(2.5F)));

	//Flower Pots
	public static final RegistryObject<FlowerPotBlock> POTTED_TWILIGHT_OAK_SAPLING = BLOCKS.register("potted_twilight_oak_sapling", () -> new FlowerPotBlock(TWILIGHT_OAK_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_CANOPY_SAPLING = BLOCKS.register("potted_canopy_sapling", () -> new FlowerPotBlock(CANOPY_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MANGROVE_SAPLING = BLOCKS.register("potted_mangrove_sapling", () -> new FlowerPotBlock(MANGROVE_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_DARKWOOD_SAPLING = BLOCKS.register("potted_darkwood_sapling", () -> new FlowerPotBlock(DARKWOOD_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_HOLLOW_OAK_SAPLING = BLOCKS.register("potted_hollow_oak_sapling", () -> new FlowerPotBlock(HOLLOW_OAK_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_RAINBOW_OAK_SAPLING = BLOCKS.register("potted_rainbow_oak_sapling", () -> new FlowerPotBlock(RAINBOW_OAK_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_TIME_SAPLING = BLOCKS.register("potted_time_sapling", () -> new FlowerPotBlock(TIME_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_TRANSFORMATION_SAPLING = BLOCKS.register("potted_transformation_sapling", () -> new FlowerPotBlock(TRANSFORMATION_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MINING_SAPLING = BLOCKS.register("potted_mining_sapling", () -> new FlowerPotBlock(MINING_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_SORTING_SAPLING = BLOCKS.register("potted_sorting_sapling", () -> new FlowerPotBlock(SORTING_SAPLING.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MAYAPPLE = BLOCKS.register("potted_mayapple", () -> new FlowerPotBlock(MAYAPPLE.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_FIDDLEHEAD = BLOCKS.register("potted_fiddlehead", () -> new FlowerPotBlock(FIDDLEHEAD.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MUSHGLOOM = BLOCKS.register("potted_mushgloom", () -> new FlowerPotBlock(MUSHGLOOM.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_THORN = BLOCKS.register("potted_thorn", () -> new SpecialFlowerPotBlock(BROWN_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_GREEN_THORN = BLOCKS.register("potted_green_thorn", () -> new SpecialFlowerPotBlock(GREEN_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_DEAD_THORN = BLOCKS.register("potted_dead_thorn", () -> new SpecialFlowerPotBlock(BURNT_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));


	public static <T extends Block> RegistryObject<T> register(String name, Supplier<Block> block) {
		RegistryObject<? extends Block> ret = BLOCKS.register(name, block);
		TFItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties()));
		return (RegistryObject<T>) ret;
	}

	public static <T extends Block> RegistryObject<T> registerFireResistantItem(String name, Supplier<Block> block) {
		RegistryObject<? extends Block> ret = BLOCKS.register(name, block);
		TFItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().fireResistant()));
		return (RegistryObject<T>) ret;
	}

	public static <T extends Block> RegistryObject<T> registerBEWLR(String name, Supplier<Block> block) {
		RegistryObject<? extends Block> ret = BLOCKS.register(name, block);
		TFItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties()) {
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(ISTER.CLIENT_ITEM_EXTENSION);
			}
		});
		return (RegistryObject<T>) ret;
	}

	public static <T extends Block> RegistryObject<T> registerBurningItem(String name, Supplier<Block> block) {
		RegistryObject<? extends Block> ret = BLOCKS.register(name, block);
		TFItems.ITEMS.register(name, () -> new FurnaceFuelItem(ret.get(), new Item.Properties(), 300));
		return (RegistryObject<T>) ret;
	}

	public static <T extends Block> RegistryObject<T> registerDoubleBlockItem(String name, Supplier<Block> block) {
		RegistryObject<? extends Block> ret = BLOCKS.register(name, block);
		TFItems.ITEMS.register(name, () -> new DoubleHighBlockItem(ret.get(), new Item.Properties()));
		return (RegistryObject<T>) ret;
	}

	private static BlockBehaviour.Properties logProperties(MapColor color) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(color);
	}

	private static BlockBehaviour.Properties logProperties(MapColor top, MapColor side) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side);
	}
}
