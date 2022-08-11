package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.HollowLogClimbable;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.HollowLogVertical;
import twilightforest.client.ISTER;
import twilightforest.item.*;

import java.util.Objects;
import java.util.function.Consumer;

//TODO move this out of the register event. I would ideally like to register these alongside blocks
public class TFBlockItems {

	public static void registerBlockItems() {
			register(trophyBlock(TFBlocks.NAGA_TROPHY, TFBlocks.NAGA_WALL_TROPHY));
			register(trophyBlock(TFBlocks.LICH_TROPHY, TFBlocks.LICH_WALL_TROPHY));
			register(trophyBlock(TFBlocks.MINOSHROOM_TROPHY, TFBlocks.MINOSHROOM_WALL_TROPHY));
			register(trophyBlock(TFBlocks.HYDRA_TROPHY, TFBlocks.HYDRA_WALL_TROPHY));
			register(trophyBlock(TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY));
			register(trophyBlock(TFBlocks.UR_GHAST_TROPHY, TFBlocks.UR_GHAST_WALL_TROPHY));
			register(trophyBlock(TFBlocks.ALPHA_YETI_TROPHY, TFBlocks.ALPHA_YETI_WALL_TROPHY));
			register(trophyBlock(TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY));
			register(trophyBlock(TFBlocks.QUEST_RAM_TROPHY, TFBlocks.QUEST_RAM_WALL_TROPHY));

			register(blockItem(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE));
			register(blockItem(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE));
			register(blockItem(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE));
//      register(blockItem(TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE));

			register(blockItem(TFBlocks.NAGA_BOSS_SPAWNER));
			register(blockItem(TFBlocks.LICH_BOSS_SPAWNER));
			register(blockItem(TFBlocks.MINOSHROOM_BOSS_SPAWNER));
			register(blockItem(TFBlocks.HYDRA_BOSS_SPAWNER));
			register(blockItem(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER));
			register(blockItem(TFBlocks.UR_GHAST_BOSS_SPAWNER));
			register(blockItem(TFBlocks.ALPHA_YETI_BOSS_SPAWNER));
			register(blockItem(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER));
			register(blockItem(TFBlocks.FINAL_BOSS_BOSS_SPAWNER));

			register(blockItem(TFBlocks.ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.CRACKED_ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.MOSSY_ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.NAGASTONE_HEAD));
			register(blockItem(TFBlocks.NAGASTONE));
			register(blockItem(TFBlocks.SPIRAL_BRICKS));
//      register(blockItem(TFBlocks.TERRORCOTTA_CIRCLE));
//      register(blockItem(TFBlocks.TERRORCOTTA_DIAGONAL));
//      register(blockItem(TFBlocks.LAPIS_BLOCK));
			register(blockItem(TFBlocks.TWISTED_STONE));
			register(blockItem(TFBlocks.TWISTED_STONE_PILLAR));
			ISTERItemRegistry.register(register(new BlockItem(TFBlocks.KEEPSAKE_CASKET.get(), TFItems.defaultBuilder().fireResistant())));
			register(blockItem(TFBlocks.CANDELABRA));
			register(blockItem(TFBlocks.BOLD_STONE_PILLAR));
			register(blockItem(TFBlocks.DEATH_TOME_SPAWNER));
			register(blockItem(TFBlocks.EMPTY_CANOPY_BOOKSHELF));
			register(skullCandleItem(TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE));
			register(new HugeWaterLilyItem(TFBlocks.HUGE_WATER_LILY.get(), TFItems.defaultBuilder()));
			register(new HugeLilyPadItem(TFBlocks.HUGE_LILY_PAD.get(), TFItems.defaultBuilder()));
			register(blockItem(TFBlocks.MAZESTONE));
			register(blockItem(TFBlocks.MAZESTONE_BRICK));
			register(blockItem(TFBlocks.CRACKED_MAZESTONE));
			register(blockItem(TFBlocks.MOSSY_MAZESTONE));
			register(blockItem(TFBlocks.DECORATIVE_MAZESTONE));
			register(blockItem(TFBlocks.CUT_MAZESTONE));
			register(blockItem(TFBlocks.MAZESTONE_BORDER));
			register(blockItem(TFBlocks.MAZESTONE_MOSAIC));
			register(blockItem(TFBlocks.SMOKER));
			register(blockItem(TFBlocks.ENCASED_SMOKER));
			register(blockItem(TFBlocks.FIRE_JET));
			register(blockItem(TFBlocks.ENCASED_FIRE_JET));
			register(blockItem(TFBlocks.STRONGHOLD_SHIELD));
			register(blockItem(TFBlocks.TROPHY_PEDESTAL));
			register(blockItem(TFBlocks.UNDERBRICK));
			register(blockItem(TFBlocks.CRACKED_UNDERBRICK));
			register(blockItem(TFBlocks.MOSSY_UNDERBRICK));
			register(blockItem(TFBlocks.UNDERBRICK_FLOOR));
			register(blockItem(TFBlocks.TOWERWOOD));
			register(blockItem(TFBlocks.CRACKED_TOWERWOOD));
			register(blockItem(TFBlocks.MOSSY_TOWERWOOD));
			register(blockItem(TFBlocks.INFESTED_TOWERWOOD));
			register(blockItem(TFBlocks.ENCASED_TOWERWOOD));
			register(blockItem(TFBlocks.VANISHING_BLOCK));
			register(blockItem(TFBlocks.REAPPEARING_BLOCK));
			register(blockItem(TFBlocks.LOCKED_VANISHING_BLOCK));
			register(blockItem(TFBlocks.CARMINITE_BUILDER));
			register(blockItem(TFBlocks.ANTIBUILDER));
			register(blockItem(TFBlocks.CARMINITE_REACTOR));
			register(blockItem(TFBlocks.GHAST_TRAP));
			register(blockItem(TFBlocks.AURORA_BLOCK));
			register(blockItem(TFBlocks.AURORA_PILLAR));
			register(blockItem(TFBlocks.AURORA_SLAB));
			register(blockItem(TFBlocks.AURORALIZED_GLASS));
			register(blockItem(TFBlocks.TROLLSTEINN));
			register(blockItem(TFBlocks.TROLLVIDR));
			register(blockItem(TFBlocks.UNRIPE_TROLLBER));
			register(blockItem(TFBlocks.TROLLBER));
			register(blockItem(TFBlocks.HUGE_MUSHGLOOM));
			register(blockItem(TFBlocks.HUGE_MUSHGLOOM_STEM));
			register(blockItem(TFBlocks.UBEROUS_SOIL));
			register(blockItem(TFBlocks.HUGE_STALK));
			register(blockItem(TFBlocks.BEANSTALK_LEAVES));
			register(blockItem(TFBlocks.WISPY_CLOUD));
			register(blockItem(TFBlocks.FLUFFY_CLOUD));
			register(blockItem(TFBlocks.GIANT_COBBLESTONE));
			register(blockItem(TFBlocks.GIANT_LOG));
			register(blockItem(TFBlocks.GIANT_LEAVES));
			register(blockItem(TFBlocks.GIANT_OBSIDIAN));
			register(blockItem(TFBlocks.DEADROCK));
			register(blockItem(TFBlocks.CRACKED_DEADROCK));
			register(blockItem(TFBlocks.WEATHERED_DEADROCK));
			register(blockItem(TFBlocks.BROWN_THORNS));
			register(blockItem(TFBlocks.GREEN_THORNS));
			register(blockItem(TFBlocks.BURNT_THORNS));
			register(blockItem(TFBlocks.THORN_ROSE));
			register(blockItem(TFBlocks.THORN_LEAVES));
			register(blockItem(TFBlocks.CASTLE_BRICK));
			register(blockItem(TFBlocks.WORN_CASTLE_BRICK));
			register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK));
			register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK));
			register(blockItem(TFBlocks.THICK_CASTLE_BRICK));
			register(blockItem(TFBlocks.CASTLE_ROOF_TILE));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE));
			register(blockItem(TFBlocks.CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.WORN_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.PINK_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.YELLOW_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.BLUE_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.VIOLET_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.PINK_CASTLE_DOOR));
			register(blockItem(TFBlocks.YELLOW_CASTLE_DOOR));
			register(blockItem(TFBlocks.BLUE_CASTLE_DOOR));
			register(blockItem(TFBlocks.VIOLET_CASTLE_DOOR));
			register(blockItem(TFBlocks.PINK_FORCE_FIELD));
			register(blockItem(TFBlocks.ORANGE_FORCE_FIELD));
			register(blockItem(TFBlocks.GREEN_FORCE_FIELD));
			register(blockItem(TFBlocks.BLUE_FORCE_FIELD));
			register(blockItem(TFBlocks.VIOLET_FORCE_FIELD));

			register(blockItem(TFBlocks.ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.CRACKED_ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.MOSSY_ETCHED_NAGASTONE));
			register(blockItem(TFBlocks.NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_PILLAR));
			register(blockItem(TFBlocks.NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT));
			register(blockItem(TFBlocks.NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT));
			register(blockItem(TFBlocks.NAGASTONE_HEAD));
			register(blockItem(TFBlocks.NAGASTONE));
			register(blockItem(TFBlocks.SPIRAL_BRICKS));
//      register(event, blockItem(TFBlocks.TERRORCOTTA_CIRCLE));
//      register(event, blockItem(TFBlocks.TERRORCOTTA_DIAGONAL));
//      register(event, blockItem(TFBlocks.LAPIS_BLOCK));
			register(blockItem(TFBlocks.TWISTED_STONE));
			register(blockItem(TFBlocks.TWISTED_STONE_PILLAR));
			ISTERItemRegistry.register(register(new BlockItem(TFBlocks.KEEPSAKE_CASKET.get(), TFItems.defaultBuilder().fireResistant())));
			register(blockItem(TFBlocks.CANDELABRA));
			register(blockItem(TFBlocks.BOLD_STONE_PILLAR));
			register(blockItem(TFBlocks.DEATH_TOME_SPAWNER));
			register(blockItem(TFBlocks.EMPTY_CANOPY_BOOKSHELF));
			register(skullCandleItem(TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE));
			register(skullCandleItem(TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE));
			register(new HugeWaterLilyItem(TFBlocks.HUGE_WATER_LILY.get(), TFItems.defaultBuilder()));
			register(new HugeLilyPadItem(TFBlocks.HUGE_LILY_PAD.get(), TFItems.defaultBuilder()));
			register(blockItem(TFBlocks.MAZESTONE));
			register(blockItem(TFBlocks.MAZESTONE_BRICK));
			register(blockItem(TFBlocks.CRACKED_MAZESTONE));
			register(blockItem(TFBlocks.MOSSY_MAZESTONE));
			register(blockItem(TFBlocks.DECORATIVE_MAZESTONE));
			register(blockItem(TFBlocks.CUT_MAZESTONE));
			register(blockItem(TFBlocks.MAZESTONE_BORDER));
			register(blockItem(TFBlocks.MAZESTONE_MOSAIC));
			register(blockItem(TFBlocks.SMOKER));
			register(blockItem(TFBlocks.ENCASED_SMOKER));
			register(blockItem(TFBlocks.FIRE_JET));
			register(blockItem(TFBlocks.ENCASED_FIRE_JET));
			register(blockItem(TFBlocks.STRONGHOLD_SHIELD));
			register(blockItem(TFBlocks.TROPHY_PEDESTAL));
			register(blockItem(TFBlocks.UNDERBRICK));
			register(blockItem(TFBlocks.CRACKED_UNDERBRICK));
			register(blockItem(TFBlocks.MOSSY_UNDERBRICK));
			register(blockItem(TFBlocks.UNDERBRICK_FLOOR));
			register(blockItem(TFBlocks.TOWERWOOD));
			register(blockItem(TFBlocks.CRACKED_TOWERWOOD));
			register(blockItem(TFBlocks.MOSSY_TOWERWOOD));
			register(blockItem(TFBlocks.INFESTED_TOWERWOOD));
			register(blockItem(TFBlocks.ENCASED_TOWERWOOD));
			register(blockItem(TFBlocks.VANISHING_BLOCK));
			register(blockItem(TFBlocks.REAPPEARING_BLOCK));
			register(blockItem(TFBlocks.LOCKED_VANISHING_BLOCK));
			register(blockItem(TFBlocks.CARMINITE_BUILDER));
			register(blockItem(TFBlocks.ANTIBUILDER));
			register(blockItem(TFBlocks.CARMINITE_REACTOR));
			register(blockItem(TFBlocks.GHAST_TRAP));
			register(blockItem(TFBlocks.AURORA_BLOCK));
			register(blockItem(TFBlocks.AURORA_PILLAR));
			register(blockItem(TFBlocks.AURORA_SLAB));
			register(blockItem(TFBlocks.AURORALIZED_GLASS));
			register(blockItem(TFBlocks.TROLLSTEINN));
			register(blockItem(TFBlocks.TROLLVIDR));
			register(blockItem(TFBlocks.UNRIPE_TROLLBER));
			register(blockItem(TFBlocks.TROLLBER));
			register(blockItem(TFBlocks.HUGE_MUSHGLOOM));
			register(blockItem(TFBlocks.HUGE_MUSHGLOOM_STEM));
			register(blockItem(TFBlocks.UBEROUS_SOIL));
			register(blockItem(TFBlocks.HUGE_STALK));
			register(blockItem(TFBlocks.BEANSTALK_LEAVES));
			register(blockItem(TFBlocks.WISPY_CLOUD));
			register(blockItem(TFBlocks.FLUFFY_CLOUD));
			register(blockItem(TFBlocks.GIANT_COBBLESTONE));
			register(blockItem(TFBlocks.GIANT_LOG));
			register(blockItem(TFBlocks.GIANT_LEAVES));
			register(blockItem(TFBlocks.GIANT_OBSIDIAN));
			register(blockItem(TFBlocks.DEADROCK));
			register(blockItem(TFBlocks.CRACKED_DEADROCK));
			register(blockItem(TFBlocks.WEATHERED_DEADROCK));
			register(blockItem(TFBlocks.BROWN_THORNS));
			register(blockItem(TFBlocks.GREEN_THORNS));
			register(blockItem(TFBlocks.BURNT_THORNS));
			register(blockItem(TFBlocks.THORN_ROSE));
			register(blockItem(TFBlocks.THORN_LEAVES));
			register(blockItem(TFBlocks.CASTLE_BRICK));
			register(blockItem(TFBlocks.WORN_CASTLE_BRICK));
			register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK));
			register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK));
			register(blockItem(TFBlocks.THICK_CASTLE_BRICK));
			register(blockItem(TFBlocks.CASTLE_ROOF_TILE));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE));
			register(blockItem(TFBlocks.CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.WORN_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.BOLD_CASTLE_BRICK_STAIRS));
			register(blockItem(TFBlocks.PINK_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.YELLOW_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.BLUE_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.VIOLET_CASTLE_RUNE_BRICK));
			register(blockItem(TFBlocks.PINK_CASTLE_DOOR));
			register(blockItem(TFBlocks.YELLOW_CASTLE_DOOR));
			register(blockItem(TFBlocks.BLUE_CASTLE_DOOR));
			register(blockItem(TFBlocks.VIOLET_CASTLE_DOOR));
			register(blockItem(TFBlocks.PINK_FORCE_FIELD));
			register(blockItem(TFBlocks.ORANGE_FORCE_FIELD));
			register(blockItem(TFBlocks.GREEN_FORCE_FIELD));
			register(blockItem(TFBlocks.BLUE_FORCE_FIELD));
			register(blockItem(TFBlocks.VIOLET_FORCE_FIELD));

		register(blockItem(TFBlocks.UNCRAFTING_TABLE));
		register(blockItem(TFBlocks.CINDER_FURNACE));
		register(blockItem(TFBlocks.CINDER_LOG));
		register(blockItem(TFBlocks.CINDER_WOOD));
		register(blockItem(TFBlocks.SLIDER));
		register(blockItem(TFBlocks.IRON_LADDER));

			register(blockItem(TFBlocks.IRONWOOD_BLOCK));
			register(blockItem(TFBlocks.STEELEAF_BLOCK));
			register(fireImmuneBlock(TFBlocks.FIERY_BLOCK));
			register(blockItem(TFBlocks.KNIGHTMETAL_BLOCK));
			register(blockItem(TFBlocks.CARMINITE_BLOCK));
			register(blockItem(TFBlocks.ARCTIC_FUR_BLOCK));

			register(blockItem(TFBlocks.MOSS_PATCH));
			register(blockItem(TFBlocks.MAYAPPLE));
			register(blockItem(TFBlocks.CLOVER_PATCH));
			register(blockItem(TFBlocks.FIDDLEHEAD));
			register(blockItem(TFBlocks.MUSHGLOOM));
			register(blockItem(TFBlocks.TORCHBERRY_PLANT));
			register(blockItem(TFBlocks.ROOT_STRAND));
			register(placeOnWaterBlockItem(TFBlocks.FALLEN_LEAVES));
			register(wearableBlock(TFBlocks.FIREFLY, TFBlockEntities.FIREFLY));
			register(wearableBlock(TFBlocks.CICADA, TFBlockEntities.CICADA));
			register(wearableBlock(TFBlocks.MOONWORM, TFBlockEntities.MOONWORM));
			register(blockItem(TFBlocks.FIREFLY_JAR));
			register(blockItem(TFBlocks.FIREFLY_SPAWNER));
			register(blockItem(TFBlocks.CICADA_JAR));
			register(blockItem(TFBlocks.HEDGE));
			register(blockItem(TFBlocks.ROOT_BLOCK));
			register(blockItem(TFBlocks.LIVEROOT_BLOCK));
			register(blockItem(TFBlocks.MANGROVE_ROOT));

			register(blockItem(TFBlocks.TWILIGHT_OAK_LEAVES));
			register(blockItem(TFBlocks.CANOPY_LEAVES));
			register(blockItem(TFBlocks.MANGROVE_LEAVES));
			register(blockItem(TFBlocks.DARK_LEAVES));
			register(blockItem(TFBlocks.TIME_LEAVES));
			register(blockItem(TFBlocks.TRANSFORMATION_LEAVES));
			register(blockItem(TFBlocks.MINING_LEAVES));
			register(blockItem(TFBlocks.SORTING_LEAVES));
			register(blockItem(TFBlocks.RAINBOW_OAK_LEAVES));
			register(blockItem(TFBlocks.TWILIGHT_OAK_LOG));
			register(blockItem(TFBlocks.CANOPY_LOG));
			register(blockItem(TFBlocks.MANGROVE_LOG));
			register(blockItem(TFBlocks.DARK_LOG));
			register(blockItem(TFBlocks.TIME_LOG));
			register(blockItem(TFBlocks.TRANSFORMATION_LOG));
			register(blockItem(TFBlocks.MINING_LOG));
			register(blockItem(TFBlocks.SORTING_LOG));
			register(blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG));
			register(blockItem(TFBlocks.STRIPPED_CANOPY_LOG));
			register(blockItem(TFBlocks.STRIPPED_MANGROVE_LOG));
			register(blockItem(TFBlocks.STRIPPED_DARK_LOG));
			register(blockItem(TFBlocks.STRIPPED_TIME_LOG));
			register(blockItem(TFBlocks.STRIPPED_TRANSFORMATION_LOG));
			register(blockItem(TFBlocks.STRIPPED_MINING_LOG));
			register(blockItem(TFBlocks.STRIPPED_SORTING_LOG));
			register(blockItem(TFBlocks.TWILIGHT_OAK_WOOD));
			register(blockItem(TFBlocks.CANOPY_WOOD));
			register(blockItem(TFBlocks.MANGROVE_WOOD));
			register(blockItem(TFBlocks.DARK_WOOD));
			register(blockItem(TFBlocks.TIME_WOOD));
			register(blockItem(TFBlocks.TRANSFORMATION_WOOD));
			register(blockItem(TFBlocks.MINING_WOOD));
			register(blockItem(TFBlocks.SORTING_WOOD));
			register(blockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD));
			register(blockItem(TFBlocks.STRIPPED_CANOPY_WOOD));
			register(blockItem(TFBlocks.STRIPPED_MANGROVE_WOOD));
			register(blockItem(TFBlocks.STRIPPED_DARK_WOOD));
			register(blockItem(TFBlocks.STRIPPED_TIME_WOOD));
			register(blockItem(TFBlocks.STRIPPED_TRANSFORMATION_WOOD));
			register(blockItem(TFBlocks.STRIPPED_MINING_WOOD));
			register(blockItem(TFBlocks.STRIPPED_SORTING_WOOD));

			register(blockItem(TFBlocks.TIME_LOG_CORE));
			register(blockItem(TFBlocks.TRANSFORMATION_LOG_CORE));
			register(blockItem(TFBlocks.MINING_LOG_CORE));
			register(blockItem(TFBlocks.SORTING_LOG_CORE));
			register(blockItem(TFBlocks.TWILIGHT_OAK_SAPLING));
			register(blockItem(TFBlocks.CANOPY_SAPLING));
			register(blockItem(TFBlocks.MANGROVE_SAPLING));
			register(blockItem(TFBlocks.DARKWOOD_SAPLING));
			register(blockItem(TFBlocks.HOLLOW_OAK_SAPLING));
			register(blockItem(TFBlocks.TIME_SAPLING));
			register(blockItem(TFBlocks.TRANSFORMATION_SAPLING));
			register(blockItem(TFBlocks.MINING_SAPLING));
			register(blockItem(TFBlocks.SORTING_SAPLING));
			register(blockItem(TFBlocks.RAINBOW_OAK_SAPLING));

			register(burningItem(TFBlocks.OAK_BANISTER, 300));
			register(burningItem(TFBlocks.SPRUCE_BANISTER, 300));
			register(burningItem(TFBlocks.BIRCH_BANISTER, 300));
			register(burningItem(TFBlocks.JUNGLE_BANISTER, 300));
			register(burningItem(TFBlocks.ACACIA_BANISTER, 300));
			register(burningItem(TFBlocks.DARK_OAK_BANISTER, 300));
			register(burningItem(TFBlocks.CRIMSON_BANISTER, 300));
			register(burningItem(TFBlocks.WARPED_BANISTER, 300));
			register(burningItem(TFBlocks.VANGROVE_BANISTER, 300));

			register(hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, "hollow_oak_log"));
			register(hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, "hollow_spruce_log"));
			register(hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, "hollow_birch_log"));
			register(hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, "hollow_jungle_log"));
			register(hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, "hollow_acacia_log"));
			register(hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, "hollow_dark_oak_log"));
			register(hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, "hollow_crimson_stem"));
			register(hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, "hollow_warped_stem"));
			register(hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, "hollow_vangrove_log"));

			register(hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, "hollow_twilight_oak_log"));
			register(hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, "hollow_canopy_log"));
			register(hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, "hollow_mangrove_log"));
			register(hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, "hollow_dark_log"));
			register(hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, "hollow_time_log"));
			register(hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, "hollow_transformation_log"));
			register(hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, "hollow_mining_log"));
			register(hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, "hollow_sorting_log"));

			register(blockItem(TFBlocks.TWILIGHT_OAK_PLANKS));
			register(blockItem(TFBlocks.TWILIGHT_OAK_STAIRS));
			register(blockItem(TFBlocks.TWILIGHT_OAK_SLAB));
			register(blockItem(TFBlocks.TWILIGHT_OAK_BUTTON));
			register(burningItem(TFBlocks.TWILIGHT_OAK_FENCE, 300));
			register(burningItem(TFBlocks.TWILIGHT_OAK_GATE, 300));
			register(blockItem(TFBlocks.TWILIGHT_OAK_PLATE));
			register(blockItem(TFBlocks.TWILIGHT_OAK_TRAPDOOR));
			register(tallBlock(TFBlocks.TWILIGHT_OAK_DOOR));
			register(signBlock(TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN));
			register(burningItem(TFBlocks.TWILIGHT_OAK_BANISTER, 300));
			register(blockItem(TFBlocks.CANOPY_PLANKS));
			register(blockItem(TFBlocks.CANOPY_STAIRS));
			register(blockItem(TFBlocks.CANOPY_SLAB));
			register(blockItem(TFBlocks.CANOPY_BUTTON));
			register(burningItem(TFBlocks.CANOPY_FENCE, 300));
			register(burningItem(TFBlocks.CANOPY_GATE, 300));
			register(blockItem(TFBlocks.CANOPY_PLATE));
			register(blockItem(TFBlocks.CANOPY_TRAPDOOR));
			register(tallBlock(TFBlocks.CANOPY_DOOR));
			register(signBlock(TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN));
			register(burningItem(TFBlocks.CANOPY_BANISTER, 300));
			register(blockItem(TFBlocks.CANOPY_BOOKSHELF));
			register(blockItem(TFBlocks.MANGROVE_PLANKS));
			register(blockItem(TFBlocks.MANGROVE_STAIRS));
			register(blockItem(TFBlocks.MANGROVE_SLAB));
			register(blockItem(TFBlocks.MANGROVE_BUTTON));
			register(burningItem(TFBlocks.MANGROVE_FENCE, 300));
			register(burningItem(TFBlocks.MANGROVE_GATE, 300));
			register(blockItem(TFBlocks.MANGROVE_PLATE));
			register(blockItem(TFBlocks.MANGROVE_TRAPDOOR));
			register(tallBlock(TFBlocks.MANGROVE_DOOR));
			register(signBlock(TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN));
			register(burningItem(TFBlocks.MANGROVE_BANISTER, 300));
			register(blockItem(TFBlocks.DARK_PLANKS));
			register(blockItem(TFBlocks.DARK_STAIRS));
			register(blockItem(TFBlocks.DARK_SLAB));
			register(blockItem(TFBlocks.DARK_BUTTON));
			register(burningItem(TFBlocks.DARK_FENCE, 300));
			register(burningItem(TFBlocks.DARK_GATE, 300));
			register(blockItem(TFBlocks.DARK_PLATE));
			register(blockItem(TFBlocks.DARK_TRAPDOOR));
			register(tallBlock(TFBlocks.DARK_DOOR));
			register(signBlock(TFBlocks.DARKWOOD_SIGN, TFBlocks.DARKWOOD_WALL_SIGN));
			register(burningItem(TFBlocks.DARKWOOD_BANISTER, 300));
			register(blockItem(TFBlocks.TIME_PLANKS));
			register(blockItem(TFBlocks.TIME_STAIRS));
			register(blockItem(TFBlocks.TIME_SLAB));
			register(blockItem(TFBlocks.TIME_BUTTON));
			register(burningItem(TFBlocks.TIME_FENCE, 300));
			register(burningItem(TFBlocks.TIME_GATE, 300));
			register(blockItem(TFBlocks.TIME_PLATE));
			register(blockItem(TFBlocks.TIME_TRAPDOOR));
			register(tallBlock(TFBlocks.TIME_DOOR));
			register(signBlock(TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN));
			register(burningItem(TFBlocks.TIME_BANISTER, 300));
			register(blockItem(TFBlocks.TRANSFORMATION_PLANKS));
			register(blockItem(TFBlocks.TRANSFORMATION_STAIRS));
			register(blockItem(TFBlocks.TRANSFORMATION_SLAB));
			register(blockItem(TFBlocks.TRANSFORMATION_BUTTON));
			register(burningItem(TFBlocks.TRANSFORMATION_FENCE, 300));
			register(burningItem(TFBlocks.TRANSFORMATION_GATE, 300));
			register(blockItem(TFBlocks.TRANSFORMATION_PLATE));
			register(blockItem(TFBlocks.TRANSFORMATION_TRAPDOOR));
			register(tallBlock(TFBlocks.TRANSFORMATION_DOOR));
			register(signBlock(TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN));
			register(burningItem(TFBlocks.TRANSFORMATION_BANISTER, 300));
			register(blockItem(TFBlocks.MINING_PLANKS));
			register(blockItem(TFBlocks.MINING_STAIRS));
			register(blockItem(TFBlocks.MINING_SLAB));
			register(blockItem(TFBlocks.MINING_BUTTON));
			register(burningItem(TFBlocks.MINING_FENCE, 300));
			register(burningItem(TFBlocks.MINING_GATE, 300));
			register(blockItem(TFBlocks.MINING_PLATE));
			register(blockItem(TFBlocks.MINING_TRAPDOOR));
			register(tallBlock(TFBlocks.MINING_DOOR));
			register(signBlock(TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN));
			register(burningItem(TFBlocks.MINING_BANISTER, 300));
			register(blockItem(TFBlocks.SORTING_PLANKS));
			register(blockItem(TFBlocks.SORTING_STAIRS));
			register(blockItem(TFBlocks.SORTING_SLAB));
			register(blockItem(TFBlocks.SORTING_BUTTON));
			register(burningItem(TFBlocks.SORTING_FENCE, 300));
			register(burningItem(TFBlocks.SORTING_GATE, 300));
			register(blockItem(TFBlocks.SORTING_PLATE));
			register(blockItem(TFBlocks.SORTING_TRAPDOOR));
			register(tallBlock(TFBlocks.SORTING_DOOR));
			register(signBlock(TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN));
			register(burningItem(TFBlocks.SORTING_BANISTER, 300));

			makeBEWLRItem(TFBlocks.TWILIGHT_OAK_CHEST);
			makeBEWLRItem(TFBlocks.CANOPY_CHEST);
			makeBEWLRItem(TFBlocks.MANGROVE_CHEST);
			makeBEWLRItem(TFBlocks.DARKWOOD_CHEST);
			makeBEWLRItem(TFBlocks.TIME_CHEST);
			makeBEWLRItem(TFBlocks.TRANSFORMATION_CHEST);
			makeBEWLRItem(TFBlocks.MINING_CHEST);
			makeBEWLRItem(TFBlocks.SORTING_CHEST);
	}

	private static <B extends Block> BlockItem hollowLog(RegistryObject<HollowLogHorizontal> horizontalLog, RegistryObject<HollowLogVertical> verticalLog, RegistryObject<HollowLogClimbable> climbable, String name) {
		return new HollowLogItem(horizontalLog, verticalLog, climbable, TFItems.defaultBuilder());
	}

	private static <B extends Block> BlockItem blockItem(RegistryObject<B> block) {
		return new BlockItem(block.get(), TFItems.defaultBuilder());
	}

	private static <B extends Block> BlockItem placeOnWaterBlockItem(RegistryObject<B> block) {
		return new PlaceOnWaterBlockItem(block.get(), TFItems.defaultBuilder());
	}

	private static <B extends Block> BlockItem fireImmuneBlock(RegistryObject<B> block) {
		return new BlockItem(block.get(), TFItems.defaultBuilder().fireResistant());
	}

	private static <B extends AbstractSkullCandleBlock> BlockItem skullCandleItem(RegistryObject<B> floor, RegistryObject<B> wall) {
		return ISTERItemRegistry.register(new SkullCandleItem(floor.get(), wall.get(), TFItems.defaultBuilder().rarity(Rarity.UNCOMMON)));
	}

	private static <B extends Block> BlockItem burningItem(RegistryObject<B> block, int burntime) {
		return new FurnaceFuelItem(block.get(), TFItems.defaultBuilder(), burntime);
	}

	private static <B extends Block, W extends Block> BlockItem trophyBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return ISTERItemRegistry.register(new TrophyItem(block.get(), wallblock.get(), TFItems.defaultBuilder().rarity(TwilightForestMod.getRarity())));
	}

	private static <T extends Block, E extends BlockEntity> BlockItem wearableBlock(RegistryObject<T> block, RegistryObject<BlockEntityType<E>> tileentity) {
		return ISTERItemRegistry.register(new WearableItem(block.get(), TFItems.defaultBuilder()));
	}

	private static <B extends Block> BlockItem tallBlock(RegistryObject<B> block) {
		return new DoubleHighBlockItem(block.get(), TFItems.defaultBuilder());
	}

	private static <B extends Block, W extends Block> BlockItem signBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return new SignItem(TFItems.defaultBuilder().stacksTo(16), block.get(), wallblock.get());
	}

	private static void makeBEWLRItem(RegistryObject<? extends Block> block) {
		register(ISTERItemRegistry.register(new BlockItem(block.get(), TFItems.defaultBuilder())));
	}

	private static BlockItem register(BlockItem item) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getKey(item.getBlock()), item);
	}
}
