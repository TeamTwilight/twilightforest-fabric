package twilightforest.block;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import twilightforest.client.ISTER;
import twilightforest.item.*;
import twilightforest.tileentity.TFTileEntities;

public class TFBlockItems {

	public static void registerBlockItems() {
		trophyBlock(TFBlocks.naga_trophy, TFBlocks.naga_wall_trophy);
		trophyBlock(TFBlocks.lich_trophy, TFBlocks.lich_wall_trophy);
		trophyBlock(TFBlocks.minoshroom_trophy, TFBlocks.minoshroom_wall_trophy);
		trophyBlock(TFBlocks.hydra_trophy, TFBlocks.hydra_wall_trophy);
		trophyBlock(TFBlocks.knight_phantom_trophy, TFBlocks.knight_phantom_wall_trophy);
		trophyBlock(TFBlocks.ur_ghast_trophy, TFBlocks.ur_ghast_wall_trophy);
		trophyBlock(TFBlocks.yeti_trophy, TFBlocks.yeti_wall_trophy);
		trophyBlock(TFBlocks.snow_queen_trophy, TFBlocks.snow_queen_wall_trophy);
		trophyBlock(TFBlocks.quest_ram_trophy, TFBlocks.quest_ram_wall_trophy);

		blockItem(TFBlocks.twilight_portal_miniature_structure);
//      blockItem(TFBlocks.hedge_maze_miniature_structure);
//      blockItem(TFBlocks.hollow_hill_miniature_structure);
//      blockItem(TFBlocks.mushroom_tower_miniature_structure);
//      blockItem(TFBlocks.quest_grove_miniature_structure);
		blockItem(TFBlocks.naga_courtyard_miniature_structure);
		blockItem(TFBlocks.lich_tower_miniature_structure);
//      blockItem(TFBlocks.minotaur_labyrinth_miniature_structure);
//      blockItem(TFBlocks.hydra_lair_miniature_structure);
//      blockItem(TFBlocks.goblin_stronghold_miniature_structure);
//      blockItem(TFBlocks.dark_tower_miniature_structure);
//      blockItem(TFBlocks.yeti_cave_miniature_structure);
//      blockItem(TFBlocks.aurora_palace_miniature_structure);
//      blockItem(TFBlocks.troll_cave_cottage_miniature_structure);
//      blockItem(TFBlocks.final_castle_miniature_structure);

		blockItem(TFBlocks.boss_spawner_naga);
		blockItem(TFBlocks.boss_spawner_lich);
		blockItem(TFBlocks.boss_spawner_minoshroom);
		blockItem(TFBlocks.boss_spawner_hydra);
		blockItem(TFBlocks.boss_spawner_knight_phantom);
		blockItem(TFBlocks.boss_spawner_ur_ghast);
		blockItem(TFBlocks.boss_spawner_alpha_yeti);
		blockItem(TFBlocks.boss_spawner_snow_queen);
		blockItem(TFBlocks.boss_spawner_final_boss);

		blockItem(TFBlocks.etched_nagastone);
		blockItem(TFBlocks.etched_nagastone_weathered);
		blockItem(TFBlocks.etched_nagastone_mossy);
		blockItem(TFBlocks.nagastone_pillar);
		blockItem(TFBlocks.nagastone_pillar_weathered);
		blockItem(TFBlocks.nagastone_pillar_mossy);
		blockItem(TFBlocks.nagastone_stairs_left);
		blockItem(TFBlocks.nagastone_stairs_weathered_left);
		blockItem(TFBlocks.nagastone_stairs_mossy_left);
		blockItem(TFBlocks.nagastone_stairs_right);
		blockItem(TFBlocks.nagastone_stairs_weathered_right);
		blockItem(TFBlocks.nagastone_stairs_mossy_right);
		blockItem(TFBlocks.naga_stone_head);
		blockItem(TFBlocks.naga_stone);
		blockItem(TFBlocks.spiral_bricks);
//      blockItem(TFBlocks.terrorcotta_circle);
//      blockItem(TFBlocks.terrorcotta_diagonal);
//      blockItem(TFBlocks.lapis_block);
		blockItem(TFBlocks.stone_twist);
		blockItem(TFBlocks.stone_twist_thin);
		makeBlockItem(new BlockItem(TFBlocks.keepsake_casket, defaultBuilder()) {
//			//TODO: PORT
//			@Override
//			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//				consumer.accept(new IItemRenderProperties() {
//					@Override
//					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//						return new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.KEEPSAKE_CASKET));
//					}
//				});
//			}
		}, TFBlocks.keepsake_casket);
		blockItem(TFBlocks.stone_bold);
		blockItem(TFBlocks.tome_spawner);
		blockItem(TFBlocks.empty_bookshelf);
		skullCandleItem(TFBlocks.zombie_skull_candle, TFBlocks.zombie_wall_skull_candle);
		skullCandleItem(TFBlocks.skeleton_skull_candle, TFBlocks.skeleton_wall_skull_candle);
		skullCandleItem(TFBlocks.wither_skele_skull_candle, TFBlocks.wither_skele_wall_skull_candle);
		skullCandleItem(TFBlocks.creeper_skull_candle, TFBlocks.creeper_wall_skull_candle);
		skullCandleItem(TFBlocks.player_skull_candle, TFBlocks.player_wall_skull_candle);
		makeBlockItem(new HugeWaterLilyItem(TFBlocks.huge_waterlily, defaultBuilder()), TFBlocks.huge_waterlily);
		makeBlockItem(new HugeLilyPadItem(TFBlocks.huge_lilypad, defaultBuilder()), TFBlocks.huge_lilypad);
		blockItem(TFBlocks.maze_stone);
		blockItem(TFBlocks.maze_stone_brick);
		blockItem(TFBlocks.maze_stone_cracked);
		blockItem(TFBlocks.maze_stone_mossy);
		blockItem(TFBlocks.maze_stone_decorative);
		blockItem(TFBlocks.maze_stone_chiseled);
		blockItem(TFBlocks.maze_stone_border);
		blockItem(TFBlocks.maze_stone_mosaic);
		blockItem(TFBlocks.smoker);
		blockItem(TFBlocks.encased_smoker);
		blockItem(TFBlocks.fire_jet);
		blockItem(TFBlocks.encased_fire_jet);
		blockItem(TFBlocks.stronghold_shield);
		blockItem(TFBlocks.trophy_pedestal);
		blockItem(TFBlocks.underbrick);
		blockItem(TFBlocks.underbrick_cracked);
		blockItem(TFBlocks.underbrick_mossy);
		blockItem(TFBlocks.underbrick_floor);
		blockItem(TFBlocks.tower_wood);
		blockItem(TFBlocks.tower_wood_cracked);
		blockItem(TFBlocks.tower_wood_mossy);
		blockItem(TFBlocks.tower_wood_infested);
		blockItem(TFBlocks.tower_wood_encased);
		blockItem(TFBlocks.vanishing_block);
		blockItem(TFBlocks.reappearing_block);
		blockItem(TFBlocks.locked_vanishing_block);
		blockItem(TFBlocks.carminite_builder);
		blockItem(TFBlocks.antibuilder);
		blockItem(TFBlocks.carminite_reactor);
		blockItem(TFBlocks.ghast_trap);
		blockItem(TFBlocks.aurora_block);
		blockItem(TFBlocks.aurora_pillar);
		blockItem(TFBlocks.aurora_slab);
		blockItem(TFBlocks.auroralized_glass);
		blockItem(TFBlocks.trollsteinn);
		blockItem(TFBlocks.trollvidr);
		blockItem(TFBlocks.unripe_trollber);
		blockItem(TFBlocks.trollber);
		blockItem(TFBlocks.huge_mushgloom);
		blockItem(TFBlocks.huge_mushgloom_stem);
		blockItem(TFBlocks.uberous_soil);
		blockItem(TFBlocks.huge_stalk);
		blockItem(TFBlocks.beanstalk_leaves);
		blockItem(TFBlocks.wispy_cloud);
		blockItem(TFBlocks.fluffy_cloud);
		blockItem(TFBlocks.giant_cobblestone);
		blockItem(TFBlocks.giant_log);
		blockItem(TFBlocks.giant_leaves);
		blockItem(TFBlocks.giant_obsidian);
		blockItem(TFBlocks.deadrock);
		blockItem(TFBlocks.deadrock_cracked);
		blockItem(TFBlocks.deadrock_weathered);
		blockItem(TFBlocks.brown_thorns);
		blockItem(TFBlocks.green_thorns);
		blockItem(TFBlocks.burnt_thorns);
		blockItem(TFBlocks.thorn_rose);
		blockItem(TFBlocks.thorn_leaves);
		blockItem(TFBlocks.castle_brick);
		blockItem(TFBlocks.castle_brick_worn);
		blockItem(TFBlocks.castle_brick_cracked);
		blockItem(TFBlocks.castle_brick_mossy);
		blockItem(TFBlocks.castle_brick_frame);
		blockItem(TFBlocks.castle_brick_roof);
		blockItem(TFBlocks.castle_pillar_encased);
		blockItem(TFBlocks.castle_pillar_encased_tile);
		blockItem(TFBlocks.castle_pillar_bold);
		blockItem(TFBlocks.castle_pillar_bold_tile);
		blockItem(TFBlocks.castle_stairs_brick);
		blockItem(TFBlocks.castle_stairs_worn);
		blockItem(TFBlocks.castle_stairs_cracked);
		blockItem(TFBlocks.castle_stairs_mossy);
		blockItem(TFBlocks.castle_stairs_encased);
		blockItem(TFBlocks.castle_stairs_bold);
		blockItem(TFBlocks.castle_rune_brick_pink);
		blockItem(TFBlocks.castle_rune_brick_yellow);
		blockItem(TFBlocks.castle_rune_brick_blue);
		blockItem(TFBlocks.castle_rune_brick_purple);
		blockItem(TFBlocks.castle_door_pink);
		blockItem(TFBlocks.castle_door_yellow);
		blockItem(TFBlocks.castle_door_blue);
		blockItem(TFBlocks.castle_door_purple);
		blockItem(TFBlocks.force_field_pink);
		blockItem(TFBlocks.force_field_orange);
		blockItem(TFBlocks.force_field_green);
		blockItem(TFBlocks.force_field_blue);
		blockItem(TFBlocks.force_field_purple);

		blockItem(TFBlocks.uncrafting_table);
		blockItem(TFBlocks.cinder_furnace);
		blockItem(TFBlocks.cinder_log);
		blockItem(TFBlocks.cinder_wood);
		blockItem(TFBlocks.slider);
		blockItem(TFBlocks.iron_ladder);

		blockItem(TFBlocks.ironwood_block);
		blockItem(TFBlocks.steeleaf_block);
		blockItem(TFBlocks.fiery_block);
		blockItem(TFBlocks.knightmetal_block);
		blockItem(TFBlocks.carminite_block);
		blockItem(TFBlocks.arctic_fur_block);

		blockItem(TFBlocks.moss_patch);
		blockItem(TFBlocks.mayapple);
		blockItem(TFBlocks.clover_patch);
		blockItem(TFBlocks.fiddlehead);
		blockItem(TFBlocks.mushgloom);
		blockItem(TFBlocks.torchberry_plant);
		blockItem(TFBlocks.root_strand);
		blockItem(TFBlocks.fallen_leaves);
		wearableBlock(TFBlocks.firefly, TFTileEntities.FIREFLY);
		wearableBlock(TFBlocks.cicada, TFTileEntities.CICADA);
		wearableBlock(TFBlocks.moonworm, TFTileEntities.MOONWORM);
		blockItem(TFBlocks.firefly_jar);
		blockItem(TFBlocks.firefly_spawner);
		blockItem(TFBlocks.cicada_jar);
		blockItem(TFBlocks.hedge);
		blockItem(TFBlocks.root);
		blockItem(TFBlocks.liveroot_block);

		blockItem(TFBlocks.oak_leaves);
		blockItem(TFBlocks.canopy_leaves);
		blockItem(TFBlocks.mangrove_leaves);
		blockItem(TFBlocks.dark_leaves);
		blockItem(TFBlocks.time_leaves);
		blockItem(TFBlocks.transformation_leaves);
		blockItem(TFBlocks.mining_leaves);
		blockItem(TFBlocks.sorting_leaves);
		blockItem(TFBlocks.rainboak_leaves);
		blockItem(TFBlocks.oak_log);
		blockItem(TFBlocks.canopy_log);
		blockItem(TFBlocks.mangrove_log);
		blockItem(TFBlocks.dark_log);
		blockItem(TFBlocks.time_log);
		blockItem(TFBlocks.transformation_log);
		blockItem(TFBlocks.mining_log);
		blockItem(TFBlocks.sorting_log);
		blockItem(TFBlocks.stripped_oak_log);
		blockItem(TFBlocks.stripped_canopy_log);
		blockItem(TFBlocks.stripped_mangrove_log);
		blockItem(TFBlocks.stripped_dark_log);
		blockItem(TFBlocks.stripped_time_log);
		blockItem(TFBlocks.stripped_transformation_log);
		blockItem(TFBlocks.stripped_mining_log);
		blockItem(TFBlocks.stripped_sorting_log);
		blockItem(TFBlocks.oak_wood);
		blockItem(TFBlocks.canopy_wood);
		blockItem(TFBlocks.mangrove_wood);
		blockItem(TFBlocks.dark_wood);
		blockItem(TFBlocks.time_wood);
		blockItem(TFBlocks.transformation_wood);
		blockItem(TFBlocks.mining_wood);
		blockItem(TFBlocks.sorting_wood);
		blockItem(TFBlocks.stripped_oak_wood);
		blockItem(TFBlocks.stripped_canopy_wood);
		blockItem(TFBlocks.stripped_mangrove_wood);
		blockItem(TFBlocks.stripped_dark_wood);
		blockItem(TFBlocks.stripped_time_wood);
		blockItem(TFBlocks.stripped_transformation_wood);
		blockItem(TFBlocks.stripped_mining_wood);
		blockItem(TFBlocks.stripped_sorting_wood);
		blockItem(TFBlocks.time_log_core);
		blockItem(TFBlocks.transformation_log_core);
		blockItem(TFBlocks.mining_log_core);
		blockItem(TFBlocks.sorting_log_core);
		blockItem(TFBlocks.oak_sapling);
		blockItem(TFBlocks.canopy_sapling);
		blockItem(TFBlocks.mangrove_sapling);
		blockItem(TFBlocks.darkwood_sapling);
		blockItem(TFBlocks.hollow_oak_sapling);
		blockItem(TFBlocks.time_sapling);
		blockItem(TFBlocks.transformation_sapling);
		blockItem(TFBlocks.mining_sapling);
		blockItem(TFBlocks.sorting_sapling);
		blockItem(TFBlocks.rainboak_sapling);
		blockItem(TFBlocks.twilight_oak_planks);
		blockItem(TFBlocks.twilight_oak_stairs);
		blockItem(TFBlocks.twilight_oak_slab);
		blockItem(TFBlocks.twilight_oak_button);
		burningItem(TFBlocks.twilight_oak_fence, 300);
		burningItem(TFBlocks.twilight_oak_gate, 300);
		blockItem(TFBlocks.twilight_oak_plate);
		blockItem(TFBlocks.twilight_oak_trapdoor);
		tallBlock(TFBlocks.twilight_oak_door);
		signBlock(TFBlocks.twilight_oak_sign, TFBlocks.twilight_wall_sign);
		blockItem(TFBlocks.canopy_planks);
		blockItem(TFBlocks.canopy_stairs);
		blockItem(TFBlocks.canopy_slab);
		blockItem(TFBlocks.canopy_button);
		burningItem(TFBlocks.canopy_fence, 300);
		burningItem(TFBlocks.canopy_gate, 300);
		blockItem(TFBlocks.canopy_plate);
		blockItem(TFBlocks.canopy_trapdoor);
		tallBlock(TFBlocks.canopy_door);
		signBlock(TFBlocks.canopy_sign, TFBlocks.canopy_wall_sign);
		blockItem(TFBlocks.canopy_bookshelf);
		blockItem(TFBlocks.mangrove_planks);
		blockItem(TFBlocks.mangrove_stairs);
		blockItem(TFBlocks.mangrove_slab);
		blockItem(TFBlocks.mangrove_button);
		burningItem(TFBlocks.mangrove_fence, 300);
		burningItem(TFBlocks.mangrove_gate, 300);
		blockItem(TFBlocks.mangrove_plate);
		blockItem(TFBlocks.mangrove_trapdoor);
		tallBlock(TFBlocks.mangrove_door);
		signBlock(TFBlocks.mangrove_sign, TFBlocks.mangrove_wall_sign);
		blockItem(TFBlocks.dark_planks);
		blockItem(TFBlocks.dark_stairs);
		blockItem(TFBlocks.dark_slab);
		blockItem(TFBlocks.dark_button);
		burningItem(TFBlocks.dark_fence, 300);
		burningItem(TFBlocks.dark_gate, 300);
		blockItem(TFBlocks.dark_plate);
		blockItem(TFBlocks.dark_trapdoor);
		tallBlock(TFBlocks.dark_door);
		signBlock(TFBlocks.darkwood_sign, TFBlocks.darkwood_wall_sign);
		blockItem(TFBlocks.time_planks);
		blockItem(TFBlocks.time_stairs);
		blockItem(TFBlocks.time_slab);
		blockItem(TFBlocks.time_button);
		burningItem(TFBlocks.time_fence, 300);
		burningItem(TFBlocks.time_gate, 300);
		blockItem(TFBlocks.time_plate);
		blockItem(TFBlocks.time_trapdoor);
		tallBlock(TFBlocks.time_door);
		signBlock(TFBlocks.time_sign, TFBlocks.time_wall_sign);
		blockItem(TFBlocks.trans_planks);
		blockItem(TFBlocks.trans_stairs);
		blockItem(TFBlocks.trans_slab);
		blockItem(TFBlocks.trans_button);
		burningItem(TFBlocks.trans_fence, 300);
		burningItem(TFBlocks.trans_gate, 300);
		blockItem(TFBlocks.trans_plate);
		blockItem(TFBlocks.trans_trapdoor);
		tallBlock(TFBlocks.trans_door);
		signBlock(TFBlocks.trans_sign, TFBlocks.trans_wall_sign);
		blockItem(TFBlocks.mine_planks);
		blockItem(TFBlocks.mine_stairs);
		blockItem(TFBlocks.mine_slab);
		blockItem(TFBlocks.mine_button);
		burningItem(TFBlocks.mine_fence, 300);
		burningItem(TFBlocks.mine_gate, 300);
		blockItem(TFBlocks.mine_plate);
		blockItem(TFBlocks.mine_trapdoor);
		tallBlock(TFBlocks.mine_door);
		signBlock(TFBlocks.mine_sign, TFBlocks.mine_wall_sign);
		blockItem(TFBlocks.sort_planks);
		blockItem(TFBlocks.sort_stairs);
		blockItem(TFBlocks.sort_slab);
		blockItem(TFBlocks.sort_button);
		burningItem(TFBlocks.sort_fence, 300);
		burningItem(TFBlocks.sort_gate, 300);
		blockItem(TFBlocks.sort_plate);
		blockItem(TFBlocks.sort_trapdoor);
		tallBlock(TFBlocks.sort_door);
		signBlock(TFBlocks.sort_sign, TFBlocks.sort_wall_sign);
	}

		r.register(blockItem(TFBlocks.oak_leaves));
		r.register(blockItem(TFBlocks.canopy_leaves));
		r.register(blockItem(TFBlocks.mangrove_leaves));
		r.register(blockItem(TFBlocks.dark_leaves));
		r.register(blockItem(TFBlocks.time_leaves));
		r.register(blockItem(TFBlocks.transformation_leaves));
		r.register(blockItem(TFBlocks.mining_leaves));
		r.register(blockItem(TFBlocks.sorting_leaves));
		r.register(blockItem(TFBlocks.rainboak_leaves));
		r.register(blockItem(TFBlocks.oak_log));
		r.register(blockItem(TFBlocks.canopy_log));
		r.register(blockItem(TFBlocks.mangrove_log));
		r.register(blockItem(TFBlocks.dark_log));
		r.register(blockItem(TFBlocks.time_log));
		r.register(blockItem(TFBlocks.transformation_log));
		r.register(blockItem(TFBlocks.mining_log));
		r.register(blockItem(TFBlocks.sorting_log));
		r.register(blockItem(TFBlocks.stripped_oak_log));
		r.register(blockItem(TFBlocks.stripped_canopy_log));
		r.register(blockItem(TFBlocks.stripped_mangrove_log));
		r.register(blockItem(TFBlocks.stripped_dark_log));
		r.register(blockItem(TFBlocks.stripped_time_log));
		r.register(blockItem(TFBlocks.stripped_transformation_log));
		r.register(blockItem(TFBlocks.stripped_mining_log));
		r.register(blockItem(TFBlocks.stripped_sorting_log));
		r.register(blockItem(TFBlocks.oak_wood));
		r.register(blockItem(TFBlocks.canopy_wood));
		r.register(blockItem(TFBlocks.mangrove_wood));
		r.register(blockItem(TFBlocks.dark_wood));
		r.register(blockItem(TFBlocks.time_wood));
		r.register(blockItem(TFBlocks.transformation_wood));
		r.register(blockItem(TFBlocks.mining_wood));
		r.register(blockItem(TFBlocks.sorting_wood));
		r.register(blockItem(TFBlocks.stripped_oak_wood));
		r.register(blockItem(TFBlocks.stripped_canopy_wood));
		r.register(blockItem(TFBlocks.stripped_mangrove_wood));
		r.register(blockItem(TFBlocks.stripped_dark_wood));
		r.register(blockItem(TFBlocks.stripped_time_wood));
		r.register(blockItem(TFBlocks.stripped_transformation_wood));
		r.register(blockItem(TFBlocks.stripped_mining_wood));
		r.register(blockItem(TFBlocks.stripped_sorting_wood));
		r.register(blockItem(TFBlocks.time_log_core));
		r.register(blockItem(TFBlocks.transformation_log_core));
		r.register(blockItem(TFBlocks.mining_log_core));
		r.register(blockItem(TFBlocks.sorting_log_core));
		r.register(blockItem(TFBlocks.oak_sapling));
		r.register(blockItem(TFBlocks.canopy_sapling));
		r.register(blockItem(TFBlocks.mangrove_sapling));
		r.register(blockItem(TFBlocks.darkwood_sapling));
		r.register(blockItem(TFBlocks.hollow_oak_sapling));
		r.register(blockItem(TFBlocks.time_sapling));
		r.register(blockItem(TFBlocks.transformation_sapling));
		r.register(blockItem(TFBlocks.mining_sapling));
		r.register(blockItem(TFBlocks.sorting_sapling));
		r.register(blockItem(TFBlocks.rainboak_sapling));

		r.register(blockItem(TFBlocks.oak_banister));
		r.register(blockItem(TFBlocks.spruce_banister));
		r.register(blockItem(TFBlocks.birch_banister));
		r.register(blockItem(TFBlocks.jungle_banister));
		r.register(blockItem(TFBlocks.acacia_banister));
		r.register(blockItem(TFBlocks.dark_oak_banister));
		r.register(blockItem(TFBlocks.crimson_banister));
		r.register(blockItem(TFBlocks.warped_banister));

		r.register(blockItem(TFBlocks.twilight_oak_planks));
		r.register(blockItem(TFBlocks.twilight_oak_stairs));
		r.register(blockItem(TFBlocks.twilight_oak_slab));
		r.register(blockItem(TFBlocks.twilight_oak_button));
		r.register(burningItem(TFBlocks.twilight_oak_fence, 300));
		r.register(burningItem(TFBlocks.twilight_oak_gate, 300));
		r.register(blockItem(TFBlocks.twilight_oak_plate));
		r.register(blockItem(TFBlocks.twilight_oak_trapdoor));
		r.register(tallBlock(TFBlocks.twilight_oak_door));
		r.register(signBlock(TFBlocks.twilight_oak_sign, TFBlocks.twilight_wall_sign));
		r.register(blockItem(TFBlocks.twilight_oak_banister));
		r.register(blockItem(TFBlocks.canopy_planks));
		r.register(blockItem(TFBlocks.canopy_stairs));
		r.register(blockItem(TFBlocks.canopy_slab));
		r.register(blockItem(TFBlocks.canopy_button));
		r.register(burningItem(TFBlocks.canopy_fence, 300));
		r.register(burningItem(TFBlocks.canopy_gate, 300));
		r.register(blockItem(TFBlocks.canopy_plate));
		r.register(blockItem(TFBlocks.canopy_trapdoor));
		r.register(tallBlock(TFBlocks.canopy_door));
		r.register(signBlock(TFBlocks.canopy_sign, TFBlocks.canopy_wall_sign));
		r.register(blockItem(TFBlocks.canopy_banister));
		r.register(blockItem(TFBlocks.canopy_bookshelf));
		r.register(blockItem(TFBlocks.mangrove_planks));
		r.register(blockItem(TFBlocks.mangrove_stairs));
		r.register(blockItem(TFBlocks.mangrove_slab));
		r.register(blockItem(TFBlocks.mangrove_button));
		r.register(burningItem(TFBlocks.mangrove_fence, 300));
		r.register(burningItem(TFBlocks.mangrove_gate, 300));
		r.register(blockItem(TFBlocks.mangrove_plate));
		r.register(blockItem(TFBlocks.mangrove_trapdoor));
		r.register(tallBlock(TFBlocks.mangrove_door));
		r.register(signBlock(TFBlocks.mangrove_sign, TFBlocks.mangrove_wall_sign));
		r.register(blockItem(TFBlocks.mangrove_banister));
		r.register(blockItem(TFBlocks.dark_planks));
		r.register(blockItem(TFBlocks.dark_stairs));
		r.register(blockItem(TFBlocks.dark_slab));
		r.register(blockItem(TFBlocks.dark_button));
		r.register(burningItem(TFBlocks.dark_fence, 300));
		r.register(burningItem(TFBlocks.dark_gate, 300));
		r.register(blockItem(TFBlocks.dark_plate));
		r.register(blockItem(TFBlocks.dark_trapdoor));
		r.register(tallBlock(TFBlocks.dark_door));
		r.register(signBlock(TFBlocks.darkwood_sign, TFBlocks.darkwood_wall_sign));
		r.register(blockItem(TFBlocks.darkwood_banister));
		r.register(blockItem(TFBlocks.time_planks));
		r.register(blockItem(TFBlocks.time_stairs));
		r.register(blockItem(TFBlocks.time_slab));
		r.register(blockItem(TFBlocks.time_button));
		r.register(burningItem(TFBlocks.time_fence, 300));
		r.register(burningItem(TFBlocks.time_gate, 300));
		r.register(blockItem(TFBlocks.time_plate));
		r.register(blockItem(TFBlocks.time_trapdoor));
		r.register(tallBlock(TFBlocks.time_door));
		r.register(signBlock(TFBlocks.time_sign, TFBlocks.time_wall_sign));
		r.register(blockItem(TFBlocks.time_banister));
		r.register(blockItem(TFBlocks.trans_planks));
		r.register(blockItem(TFBlocks.trans_stairs));
		r.register(blockItem(TFBlocks.trans_slab));
		r.register(blockItem(TFBlocks.trans_button));
		r.register(burningItem(TFBlocks.trans_fence, 300));
		r.register(burningItem(TFBlocks.trans_gate, 300));
		r.register(blockItem(TFBlocks.trans_plate));
		r.register(blockItem(TFBlocks.trans_trapdoor));
		r.register(tallBlock(TFBlocks.trans_door));
		r.register(signBlock(TFBlocks.trans_sign, TFBlocks.trans_wall_sign));
		r.register(blockItem(TFBlocks.trans_banister));
		r.register(blockItem(TFBlocks.mine_planks));
		r.register(blockItem(TFBlocks.mine_stairs));
		r.register(blockItem(TFBlocks.mine_slab));
		r.register(blockItem(TFBlocks.mine_button));
		r.register(burningItem(TFBlocks.mine_fence, 300));
		r.register(burningItem(TFBlocks.mine_gate, 300));
		r.register(blockItem(TFBlocks.mine_plate));
		r.register(blockItem(TFBlocks.mine_trapdoor));
		r.register(tallBlock(TFBlocks.mine_door));
		r.register(signBlock(TFBlocks.mine_sign, TFBlocks.mine_wall_sign));
		r.register(blockItem(TFBlocks.mine_banister));
		r.register(blockItem(TFBlocks.sort_planks));
		r.register(blockItem(TFBlocks.sort_stairs));
		r.register(blockItem(TFBlocks.sort_slab));
		r.register(blockItem(TFBlocks.sort_button));
		r.register(burningItem(TFBlocks.sort_fence, 300));
		r.register(burningItem(TFBlocks.sort_gate, 300));
		r.register(blockItem(TFBlocks.sort_plate));
		r.register(blockItem(TFBlocks.sort_trapdoor));
		r.register(tallBlock(TFBlocks.sort_door));
		r.register(signBlock(TFBlocks.sort_sign, TFBlocks.sort_wall_sign));
		r.register(blockItem(TFBlocks.sort_banister));
	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(TwilightForestMod.CREATIVETAB);
	}

	private static <B extends Block> Item blockItem(B block) {
		return makeBlockItem(new BlockItem(block, defaultBuilder()), block);
	}

	private static <B extends AbstractSkullCandleBlock> Item skullCandleItem(B floor,B wall) {
		return makeBlockItem(new SkullCandleItem(floor, wall, defaultBuilder().rarity(Rarity.UNCOMMON)) {
			//TODO: PORT
//			@Override
//			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//				consumer.accept(new IItemRenderProperties() {
//					@Override
//					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//						return new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.SKULL_CANDLE));
//					}
//				});
//			}
		}, floor);
	}

	private static <B extends Block> Item burningItem(B block, int burntime) {
		return makeBlockItem(new FurnaceFuelItem(block, defaultBuilder(), burntime), block);
	}

	private static <B extends Block, W extends Block> Item trophyBlock(B block, W wallblock) {
		return makeBlockItem(new TrophyItem(block, wallblock, defaultBuilder().rarity(TwilightForestMod.getRarity())) {

//			//TODO: PORT
//			@Override
//			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//				consumer.accept(new IItemRenderProperties() {
//					@Override
//					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//						return new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.TROPHY));
//					}
//				});
//			}
		}, block);
	}

	private static <T extends Block, E extends BlockEntity> Item wearableBlock(T block, BlockEntityType<E> tileentity) {
		return makeBlockItem(new WearableItem(block, defaultBuilder()) {

			//TODO: PORT
//			@Override
//			public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//				consumer.accept(new IItemRenderProperties() {
//					@Override
//					public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//						return new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(tileentity));
//					}
//				});
//			}
		}, block);
	}

	private static <B extends Block> Item tallBlock(B block) {
		return makeBlockItem(new DoubleHighBlockItem(block, defaultBuilder()), block);
	}

	private static <B extends Block, W extends Block> Item signBlock(B block, W wallblock) {
		return makeBlockItem(new SignItem(defaultBuilder().stacksTo(16), block, wallblock), block);
	}

	private static <B extends Block> Item makeBlockItem(Item blockitem, B block) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getKey(block), blockitem);
	}
}
