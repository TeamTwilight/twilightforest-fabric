package twilightforest.data;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.tag.TagRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import java.util.Set;
import java.util.function.Predicate;

public class BlockTagGenerator extends BlockTagsProvider {
    //Should use fabric tag api but I wanna see how this goes
    public static final Tag.Named<Block> TOWERWOOD = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("towerwood"));

    public static final Tag.Named<Block> TWILIGHT_OAK_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("twilight_oak_logs"));
    public static final Tag.Named<Block> CANOPY_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("canopy_logs"));
    public static final Tag.Named<Block> MANGROVE_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("mangrove_logs"));
    public static final Tag.Named<Block> DARKWOOD_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("darkwood_logs"));
    public static final Tag.Named<Block> TIME_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("timewood_logs"));
    public static final Tag.Named<Block> TRANSFORMATION_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("transwood_logs"));
    public static final Tag.Named<Block> MINING_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("mining_logs"));
    public static final Tag.Named<Block> SORTING_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("sortwood_logs"));

    public static final Tag.Named<Block> TF_LOGS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("logs"));
    public static final Tag.Named<Block> TF_FENCES = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("fences"));
    public static final Tag.Named<Block> TF_FENCE_GATES = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("fence_gates"));
    public static final Tag.Named<Block> TF_LOGS = BlockTags.bind(TwilightForestMod.prefix("logs").toString());
    public static final Tag.Named<Block> TF_FENCES = BlockTags.bind(TwilightForestMod.prefix("fences").toString());
    public static final Tag.Named<Block> TF_FENCE_GATES = BlockTags.bind(TwilightForestMod.prefix("fence_gates").toString());
    public static final Tag.Named<Block> BANISTERS = BlockTags.bind(TwilightForestMod.prefix("banisters").toString());

    public static final Tag.Named<Block> STORAGE_BLOCKS_ARCTIC_FUR = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:arctic_fur_storage_block"));
    public static final Tag.Named<Block> STORAGE_BLOCKS_CARMINITE = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:carminite_storage_block"));
    public static final Tag.Named<Block> STORAGE_BLOCKS_FIERY = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:fiery_storage_block"));
    public static final Tag.Named<Block> STORAGE_BLOCKS_IRONWOOD = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:ironwood_storage_block"));
    public static final Tag.Named<Block> STORAGE_BLOCKS_KNIGHTMETAL = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:knightmetal_storage_block"));
    public static final Tag.Named<Block> STORAGE_BLOCKS_STEELEAF = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:steeleaf_storage_block"));

    public static final Tag.Named<Block> ORES_IRONWOOD = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:ironwood_ores"));
    public static final Tag.Named<Block> ORES_KNIGHTMETAL = (Tag.Named<Block>) TagRegistry.block(new ResourceLocation("c:knightmetal_ores"));

    public static final Tag.Named<Block> PORTAL_EDGE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("portal/edge"));
    public static final Tag.Named<Block> PORTAL_POOL = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("portal/fluid"));
    public static final Tag.Named<Block> PORTAL_DECO = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("portal/decoration"));

    public static final Tag.Named<Block> SPECIAL_POTS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("dark_tower_excluded_pots"));
    public static final Tag.Named<Block> TROPHIES = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("trophies"));
    public static final Tag.Named<Block> FIRE_JET_FUEL = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("fire_jet_fuel"));

    public static final Tag.Named<Block> COMMON_PROTECTIONS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("common_protections"));
    public static final Tag.Named<Block> ANNIHILATION_INCLUSIONS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("annihilation_inclusions"));
    public static final Tag.Named<Block> ANTIBUILDER_IGNORES = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("antibuilder_ignores"));
    public static final Tag.Named<Block> CARMINITE_REACTOR_IMMUNE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("carminite_reactor_immune"));
    public static final Tag.Named<Block> STRUCTURE_BANNED_INTERACTIONS = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("structure_banned_interactions"));

    public static final Tag.Named<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/ore_safe_replace_block"));
    public static final Tag.Named<Block> ORE_MAGNET_BLOCK_REPLACE_ORE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/block_replace_ore"));
    public static final Tag.Named<Block> ORE_MAGNET_STONE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/minecraft/stone"));
    public static final Tag.Named<Block> ORE_MAGNET_NETHERRACK = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/minecraft/netherrack"));
    public static final Tag.Named<Block> ORE_MAGNET_END_STONE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/minecraft/end_stone"));
    public static final Tag.Named<Block> ORE_MAGNET_ROOT = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/" + TwilightForestMod.ID + "/" + Registry.BLOCK.getKey(TFBlocks.root).getPath()));
    public static final Tag.Named<Block> ORE_MAGNET_DEEPSLATE = (Tag.Named<Block>) TagRegistry.block(TwilightForestMod.prefix("ore_magnet/minecraft/deepslate"));

    public BlockTagGenerator(DataGenerator generator) {
        super(generator);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        tag(TWILIGHT_OAK_LOGS)
                .add(TFBlocks.oak_log, TFBlocks.stripped_oak_log, TFBlocks.oak_wood, TFBlocks.stripped_oak_wood);
        tag(CANOPY_LOGS)
                .add(TFBlocks.canopy_log, TFBlocks.stripped_canopy_log, TFBlocks.canopy_wood, TFBlocks.stripped_canopy_wood);
        tag(MANGROVE_LOGS)
                .add(TFBlocks.mangrove_log, TFBlocks.stripped_mangrove_log, TFBlocks.mangrove_wood, TFBlocks.stripped_mangrove_wood);
        tag(DARKWOOD_LOGS)
                .add(TFBlocks.dark_log, TFBlocks.stripped_dark_log, TFBlocks.dark_wood, TFBlocks.stripped_dark_wood);
        tag(TIME_LOGS)
                .add(TFBlocks.time_log, TFBlocks.stripped_time_log, TFBlocks.time_wood, TFBlocks.stripped_time_wood);
        tag(TRANSFORMATION_LOGS)
                .add(TFBlocks.transformation_log, TFBlocks.stripped_transformation_log, TFBlocks.transformation_wood, TFBlocks.stripped_transformation_wood);
        tag(MINING_LOGS)
                .add(TFBlocks.mining_log, TFBlocks.stripped_mining_log, TFBlocks.mining_wood, TFBlocks.stripped_mining_wood);
        tag(SORTING_LOGS)
                .add(TFBlocks.sorting_log, TFBlocks.stripped_sorting_log, TFBlocks.sorting_wood, TFBlocks.stripped_sorting_wood);

        tag(TF_LOGS).addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS).addTag(DARKWOOD_LOGS).addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);
        tag(BlockTags.LOGS)
                .addTag(TF_LOGS);

        tag(BlockTags.LOGS_THAT_BURN).addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS).addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

        tag(BlockTags.SAPLINGS)
                .add(TFBlocks.oak_sapling, TFBlocks.canopy_sapling, TFBlocks.mangrove_sapling, TFBlocks.darkwood_sapling)
                .add(TFBlocks.time_sapling, TFBlocks.transformation_sapling, TFBlocks.mining_sapling, TFBlocks.sorting_sapling)
                .add(TFBlocks.hollow_oak_sapling, TFBlocks.rainboak_sapling);
        tag(BlockTags.LEAVES)
                .add(TFBlocks.rainboak_leaves, TFBlocks.oak_leaves, TFBlocks.canopy_leaves, TFBlocks.mangrove_leaves, TFBlocks.dark_leaves)
                .add(TFBlocks.time_leaves, TFBlocks.transformation_leaves, TFBlocks.mining_leaves, TFBlocks.sorting_leaves)
                .add(TFBlocks.thorn_leaves, TFBlocks.beanstalk_leaves/*, TFBlocks.giant_leaves*/);

        tag(BlockTags.PLANKS)
                .add(TFBlocks.twilight_oak_planks, TFBlocks.canopy_planks, TFBlocks.mangrove_planks, TFBlocks.dark_planks)
                .add(TFBlocks.time_planks, TFBlocks.trans_planks, TFBlocks.mine_planks, TFBlocks.sort_planks)
                .add(TFBlocks.tower_wood, TFBlocks.tower_wood_encased, TFBlocks.tower_wood_cracked, TFBlocks.tower_wood_mossy, TFBlocks.tower_wood_infested);

        tag(TF_FENCES)
                .add(TFBlocks.twilight_oak_fence, TFBlocks.canopy_fence, TFBlocks.mangrove_fence, TFBlocks.dark_fence)
                .add(TFBlocks.time_fence, TFBlocks.trans_fence, TFBlocks.mine_fence, TFBlocks.sort_fence);
        tag(TF_FENCE_GATES)
                .add(TFBlocks.twilight_oak_gate, TFBlocks.canopy_gate, TFBlocks.mangrove_gate, TFBlocks.dark_gate)
                .add(TFBlocks.time_gate, TFBlocks.trans_gate, TFBlocks.mine_gate, TFBlocks.sort_gate);
        tag(BlockTags.WOODEN_FENCES)
                .add(TFBlocks.twilight_oak_fence, TFBlocks.canopy_fence, TFBlocks.mangrove_fence, TFBlocks.dark_fence)
                .add(TFBlocks.time_fence, TFBlocks.trans_fence, TFBlocks.mine_fence, TFBlocks.sort_fence);
        tag(BlockTags.FENCE_GATES)
                .add(TFBlocks.twilight_oak_gate, TFBlocks.canopy_gate, TFBlocks.mangrove_gate, TFBlocks.dark_gate)
                .add(TFBlocks.time_gate, TFBlocks.trans_gate, TFBlocks.mine_gate, TFBlocks.sort_gate);
//        tag(Tags.Blocks.FENCES)
//                .add(TFBlocks.twilight_oak_fence, TFBlocks.canopy_fence, TFBlocks.mangrove_fence, TFBlocks.dark_fence)
//                .add(TFBlocks.time_fence, TFBlocks.trans_fence, TFBlocks.mine_fence, TFBlocks.sort_fence);
//        tag(Tags.Blocks.FENCE_GATES)
//                .add(TFBlocks.twilight_oak_gate, TFBlocks.canopy_gate, TFBlocks.mangrove_gate, TFBlocks.dark_gate)
//                .add(TFBlocks.time_gate, TFBlocks.trans_gate, TFBlocks.mine_gate, TFBlocks.sort_gate);
//        tag(Tags.Blocks.FENCES_WOODEN)
//                .add(TFBlocks.twilight_oak_fence, TFBlocks.canopy_fence, TFBlocks.mangrove_fence, TFBlocks.dark_fence)
//                .add(TFBlocks.time_fence, TFBlocks.trans_fence, TFBlocks.mine_fence, TFBlocks.sort_fence);
//        tag(Tags.Blocks.FENCE_GATES_WOODEN)
//                .add(TFBlocks.twilight_oak_gate, TFBlocks.canopy_gate, TFBlocks.mangrove_gate, TFBlocks.dark_gate)
//                .add(TFBlocks.time_gate, TFBlocks.trans_gate, TFBlocks.mine_gate, TFBlocks.sort_gate);

        tag(BlockTags.WOODEN_SLABS)
                .add(TFBlocks.twilight_oak_slab, TFBlocks.canopy_slab, TFBlocks.mangrove_slab, TFBlocks.dark_slab)
                .add(TFBlocks.time_slab, TFBlocks.trans_slab, TFBlocks.mine_slab, TFBlocks.sort_slab);
        tag(BlockTags.SLABS)
                .add(TFBlocks.aurora_slab);
        tag(BlockTags.WOODEN_STAIRS)
                .add(TFBlocks.twilight_oak_stairs, TFBlocks.canopy_stairs, TFBlocks.mangrove_stairs, TFBlocks.dark_stairs)
                .add(TFBlocks.time_stairs, TFBlocks.trans_stairs, TFBlocks.mine_stairs, TFBlocks.sort_stairs);
        tag(BlockTags.STAIRS)
                .add(TFBlocks.castle_stairs_brick, TFBlocks.castle_stairs_worn, TFBlocks.castle_stairs_cracked, TFBlocks.castle_stairs_mossy, TFBlocks.castle_stairs_encased, TFBlocks.castle_stairs_bold)
                .add(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right, TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right, TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right);

        tag(BlockTags.WOODEN_BUTTONS)
                .add(TFBlocks.twilight_oak_button, TFBlocks.canopy_button, TFBlocks.mangrove_button, TFBlocks.dark_button)
                .add(TFBlocks.time_button, TFBlocks.trans_button, TFBlocks.mine_button, TFBlocks.sort_button);
        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(TFBlocks.twilight_oak_plate, TFBlocks.canopy_plate, TFBlocks.mangrove_plate, TFBlocks.dark_plate)
                .add(TFBlocks.time_plate, TFBlocks.trans_plate, TFBlocks.mine_plate, TFBlocks.sort_plate);

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(TFBlocks.twilight_oak_trapdoor, TFBlocks.canopy_trapdoor, TFBlocks.mangrove_trapdoor, TFBlocks.dark_trapdoor)
                .add(TFBlocks.time_trapdoor, TFBlocks.trans_trapdoor, TFBlocks.mine_trapdoor, TFBlocks.sort_trapdoor);
        tag(BlockTags.WOODEN_DOORS)
                .add(TFBlocks.twilight_oak_door, TFBlocks.canopy_door, TFBlocks.mangrove_door, TFBlocks.dark_door)
                .add(TFBlocks.time_door, TFBlocks.trans_door, TFBlocks.mine_door, TFBlocks.sort_door);
        tag(BlockTags.FLOWER_POTS)
                .add(TFBlocks.potted_twilight_oak_sapling, TFBlocks.potted_canopy_sapling, TFBlocks.potted_mangrove_sapling, TFBlocks.potted_darkwood_sapling, TFBlocks.potted_rainboak_sapling)
                .add(TFBlocks.potted_hollow_oak_sapling, TFBlocks.potted_time_sapling, TFBlocks.potted_trans_sapling, TFBlocks.potted_mine_sapling, TFBlocks.potted_sort_sapling)
                .add(TFBlocks.potted_mayapple, TFBlocks.potted_fiddlehead, TFBlocks.potted_mushgloom, TFBlocks.potted_thorn, TFBlocks.potted_green_thorn, TFBlocks.potted_dead_thorn);

        tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.fiery_block);
        tag(BlockTags.PORTALS).add(TFBlocks.twilight_portal);
        tag(BlockTags.CLIMBABLE).add(TFBlocks.iron_ladder, TFBlocks.root_strand);
        tag(BANISTERS).add(
                TFBlocks.oak_banister.get(),
                TFBlocks.spruce_banister.get(),
                TFBlocks.birch_banister.get(),
                TFBlocks.jungle_banister.get(),
                TFBlocks.acacia_banister.get(),
                TFBlocks.dark_oak_banister.get(),
                TFBlocks.crimson_banister.get(),
                TFBlocks.warped_banister.get(),
                TFBlocks.twilight_oak_banister.get(),
                TFBlocks.canopy_banister.get(),
                TFBlocks.mangrove_banister.get(),
                TFBlocks.darkwood_banister.get(),
                TFBlocks.time_banister.get(),
                TFBlocks.trans_banister.get(),
                TFBlocks.mine_banister.get(),
                TFBlocks.sort_banister.get()
        );

        tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.fiery_block.get());
        tag(BlockTags.PORTALS).add(TFBlocks.twilight_portal.get());
        tag(BlockTags.CLIMBABLE).add(TFBlocks.iron_ladder.get(), TFBlocks.root_strand.get());

        tag(BlockTags.STANDING_SIGNS).add(TFBlocks.twilight_oak_sign, TFBlocks.canopy_sign,
                TFBlocks.mangrove_sign, TFBlocks.darkwood_sign,
                TFBlocks.time_sign, TFBlocks.trans_sign,
                TFBlocks.mine_sign, TFBlocks.sort_sign);

        tag(BlockTags.WALL_SIGNS).add(TFBlocks.twilight_wall_sign, TFBlocks.canopy_wall_sign,
                TFBlocks.mangrove_wall_sign, TFBlocks.darkwood_wall_sign,
                TFBlocks.time_wall_sign, TFBlocks.trans_wall_sign,
                TFBlocks.mine_wall_sign, TFBlocks.sort_wall_sign);

        tag(TOWERWOOD).add(TFBlocks.tower_wood, TFBlocks.tower_wood_mossy, TFBlocks.tower_wood_cracked, TFBlocks.tower_wood_infested);

        tag(STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.arctic_fur_block);
        tag(STORAGE_BLOCKS_CARMINITE).add(TFBlocks.carminite_block);
        tag(STORAGE_BLOCKS_FIERY).add(TFBlocks.fiery_block);
        tag(STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.ironwood_block);
        tag(STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.knightmetal_block);
        tag(STORAGE_BLOCKS_STEELEAF).add(TFBlocks.steeleaf_block);

        tag(BlockTags.BEACON_BASE_BLOCKS)
                .addTag(STORAGE_BLOCKS_ARCTIC_FUR)
                .addTag(STORAGE_BLOCKS_CARMINITE)
                .addTag(STORAGE_BLOCKS_FIERY)
                .addTag(STORAGE_BLOCKS_IRONWOOD)
                .addTag(STORAGE_BLOCKS_KNIGHTMETAL)
                .addTag(STORAGE_BLOCKS_STEELEAF
        );
//TODO: PORT
//        tag(Tags.Blocks.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_FIERY,  STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_STEELEAF);

//        tag(Tags.Blocks.ORES).addTags(ORES_IRONWOOD, ORES_KNIGHTMETAL);
        tag(ORES_IRONWOOD); // Intentionally blank
        tag(ORES_KNIGHTMETAL); // Intentionally blank

        tag(BlockTags.DIRT).add(TFBlocks.uberous_soil);
        tag(PORTAL_EDGE).addTag(BlockTags.DIRT);
        // So yes, we could do fluid tags for the portal pool but the problem is that we're -replacing- the block, effectively replacing what would be waterlogged, with the portal block
        // In the future if we can "portal log" blocks then we can re-explore doing it as a fluid
        tag(PORTAL_POOL).add(Blocks.WATER);
        tag(PORTAL_DECO)
                .addTag(BlockTags.FLOWERS).addTag(BlockTags.LEAVES).addTag(BlockTags.SAPLINGS).addTag(BlockTags.CROPS)
                .add(Blocks.BAMBOO)
                .add(getAllMinecraftOrTwilightBlocks(b -> (b.material == Material.PLANT || b.material == Material.REPLACEABLE_PLANT || b.material == Material.LEAVES) && !plants.contains(b)));

        tag(SPECIAL_POTS)
                .add(TFBlocks.potted_thorn, TFBlocks.potted_green_thorn, TFBlocks.potted_dead_thorn)
                .add(TFBlocks.potted_hollow_oak_sapling, TFBlocks.potted_time_sapling, TFBlocks.potted_trans_sapling)
                .add(TFBlocks.potted_mine_sapling, TFBlocks.potted_sort_sapling);

        tag(TROPHIES)
                .add(TFBlocks.naga_trophy, TFBlocks.naga_wall_trophy)
                .add(TFBlocks.lich_trophy, TFBlocks.lich_wall_trophy)
                .add(TFBlocks.minoshroom_trophy, TFBlocks.minoshroom_wall_trophy)
                .add(TFBlocks.hydra_trophy, TFBlocks.hydra_wall_trophy)
                .add(TFBlocks.knight_phantom_trophy, TFBlocks.knight_phantom_wall_trophy)
                .add(TFBlocks.ur_ghast_trophy, TFBlocks.ur_ghast_wall_trophy)
                .add(TFBlocks.yeti_trophy, TFBlocks.yeti_wall_trophy)
                .add(TFBlocks.snow_queen_trophy, TFBlocks.snow_queen_wall_trophy)
                .add(TFBlocks.quest_ram_trophy, TFBlocks.quest_ram_wall_trophy);

        tag(FIRE_JET_FUEL).add(Blocks.LAVA);

        tag(COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
                TFBlocks.boss_spawner_naga,
                TFBlocks.boss_spawner_lich,
                TFBlocks.boss_spawner_minoshroom,
                TFBlocks.boss_spawner_hydra,
                TFBlocks.boss_spawner_knight_phantom,
                TFBlocks.boss_spawner_ur_ghast,
                TFBlocks.boss_spawner_alpha_yeti,
                TFBlocks.boss_spawner_snow_queen,
                TFBlocks.boss_spawner_final_boss,
                TFBlocks.stronghold_shield,
                TFBlocks.vanishing_block,
                TFBlocks.locked_vanishing_block,
                TFBlocks.force_field_pink,
                TFBlocks.force_field_orange,
                TFBlocks.force_field_green,
                TFBlocks.force_field_blue,
                TFBlocks.force_field_purple,
                TFBlocks.keepsake_casket
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
                Blocks.MOVING_PISTON
        );

        // TODO Test behavior with giant blocks for immunity stuffs
        tag(BlockTags.DRAGON_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.giant_obsidian, TFBlocks.fake_diamond,  TFBlocks.fake_gold);

        tag(BlockTags.WITHER_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.fake_diamond, TFBlocks.fake_gold);

        tag(CARMINITE_REACTOR_IMMUNE).addTag(COMMON_PROTECTIONS);

        tag(ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
                .add(TFBlocks.deadrock, TFBlocks.deadrock_cracked, TFBlocks.deadrock_weathered)
                .add(TFBlocks.castle_brick, TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_frame, TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_roof, TFBlocks.castle_brick_worn)
                .add(TFBlocks.castle_rune_brick_blue, TFBlocks.castle_rune_brick_purple, TFBlocks.castle_rune_brick_yellow, TFBlocks.castle_rune_brick_pink)
                .add(TFBlocks.force_field_pink, TFBlocks.force_field_orange, TFBlocks.force_field_green, TFBlocks.force_field_blue, TFBlocks.force_field_purple)
                .add(TFBlocks.brown_thorns, TFBlocks.green_thorns);

        tag(ANTIBUILDER_IGNORES).addTag(COMMON_PROTECTIONS).addOptional(new ResourceLocation("gravestone:gravestone")).add(
                Blocks.REDSTONE_LAMP,
                Blocks.TNT,
                Blocks.WATER,
                TFBlocks.antibuilder,
                TFBlocks.carminite_builder,
                TFBlocks.built_block,
                TFBlocks.reactor_debris,
                TFBlocks.carminite_reactor,
                TFBlocks.reappearing_block,
                TFBlocks.ghast_trap,
                TFBlocks.fake_diamond,
                TFBlocks.fake_gold
        );

        tag(STRUCTURE_BANNED_INTERACTIONS)
                .addTag(BlockTags.BUTTONS)/*, Tags.Blocks.CHESTS)*/.add(Blocks.LEVER)
                .add(TFBlocks.antibuilder);

        tag(ORE_MAGNET_SAFE_REPLACE_BLOCK)
                .addTag(ORE_MAGNET_BLOCK_REPLACE_ORE/*, Tags.Blocks.DIRT, Tags.Blocks.GRAVEL, Tags.Blocks.SAND*/);

        tag(ORE_MAGNET_BLOCK_REPLACE_ORE)
                .add(Blocks.STONE, Blocks.NETHERRACK, Blocks.END_STONE, TFBlocks.root, Blocks.DEEPSLATE);

        // Don't add general ore taglists here, since those will include non-stone ores
        tag(ORE_MAGNET_STONE).add(
                Blocks.GOLD_ORE,
                Blocks.IRON_ORE,
                Blocks.COAL_ORE,
                Blocks.LAPIS_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.EMERALD_ORE,
                Blocks.COPPER_ORE
        );

        tag(ORE_MAGNET_NETHERRACK).add(Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);

        // Using a Quark ore as an example filler
        tag(ORE_MAGNET_END_STONE).addOptional(new ResourceLocation("quark", "biotite_ore"));

        tag(ORE_MAGNET_DEEPSLATE).add(
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.DEEPSLATE_COPPER_ORE
        );

        tag(ORE_MAGNET_ROOT).add(TFBlocks.liveroot_block);

        tag(BlockTags.MINEABLE_WITH_AXE).add(
                TFBlocks.hedge,
                TFBlocks.root,
                TFBlocks.liveroot_block,
                TFBlocks.uncrafting_table,
                TFBlocks.encased_smoker,
                TFBlocks.encased_fire_jet,
                TFBlocks.time_log_core,
                TFBlocks.transformation_log_core,
                TFBlocks.mining_log_core,
                TFBlocks.sorting_log_core,
                TFBlocks.tower_wood,
                TFBlocks.tower_wood_mossy,
                TFBlocks.tower_wood_cracked,
                TFBlocks.tower_wood_infested,
                TFBlocks.tower_wood_encased,
                TFBlocks.reappearing_block,
                TFBlocks.antibuilder,
                TFBlocks.carminite_reactor,
                TFBlocks.carminite_builder,
                TFBlocks.ghast_trap,
                TFBlocks.huge_stalk,
                TFBlocks.huge_mushgloom,
                TFBlocks.huge_mushgloom_stem,
                TFBlocks.cinder_log,
                TFBlocks.cinder_wood,
                TFBlocks.ironwood_block,
                TFBlocks.tome_spawner,
                TFBlocks.empty_bookshelf,
                TFBlocks.canopy_bookshelf
        tag(BlockTags.MINEABLE_WITH_AXE).addTag(BANISTERS).add(
                TFBlocks.hedge.get(),
                TFBlocks.root.get(),
                TFBlocks.liveroot_block.get(),
                TFBlocks.uncrafting_table.get(),
                TFBlocks.encased_smoker.get(),
                TFBlocks.encased_fire_jet.get(),
                TFBlocks.time_log_core.get(),
                TFBlocks.transformation_log_core.get(),
                TFBlocks.mining_log_core.get(),
                TFBlocks.sorting_log_core.get(),
                TFBlocks.tower_wood.get(),
                TFBlocks.tower_wood_mossy.get(),
                TFBlocks.tower_wood_cracked.get(),
                TFBlocks.tower_wood_infested.get(),
                TFBlocks.tower_wood_encased.get(),
                TFBlocks.reappearing_block.get(),
                TFBlocks.antibuilder.get(),
                TFBlocks.carminite_reactor.get(),
                TFBlocks.carminite_builder.get(),
                TFBlocks.ghast_trap.get(),
                TFBlocks.huge_stalk.get(),
                TFBlocks.huge_mushgloom.get(),
                TFBlocks.huge_mushgloom_stem.get(),
                TFBlocks.cinder_log.get(),
                TFBlocks.cinder_wood.get(),
                TFBlocks.ironwood_block.get(),
                TFBlocks.tome_spawner.get(),
                TFBlocks.empty_bookshelf.get(),
                TFBlocks.canopy_bookshelf.get()
        );

        tag(BlockTags.MINEABLE_WITH_HOE).add(
                //vanilla doesnt use the leaves tag
                TFBlocks.oak_leaves,
                TFBlocks.canopy_leaves,
                TFBlocks.mangrove_leaves,
                TFBlocks.dark_leaves,
                TFBlocks.time_leaves,
                TFBlocks.transformation_leaves,
                TFBlocks.mining_leaves,
                TFBlocks.sorting_leaves,
                TFBlocks.thorn_leaves,
                TFBlocks.thorn_rose,
                TFBlocks.beanstalk_leaves,
                TFBlocks.steeleaf_block,
                TFBlocks.arctic_fur_block
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                TFBlocks.maze_stone,
                TFBlocks.maze_stone_border,
                TFBlocks.maze_stone_brick,
                TFBlocks.maze_stone_chiseled,
                TFBlocks.maze_stone_cracked,
                TFBlocks.maze_stone_decorative,
                TFBlocks.maze_stone_mosaic,
                TFBlocks.maze_stone_mossy,
                TFBlocks.naga_stone,
                TFBlocks.naga_stone_head,
                TFBlocks.stronghold_shield,
                TFBlocks.trophy_pedestal,
                TFBlocks.aurora_pillar,
                TFBlocks.aurora_slab,
                TFBlocks.underbrick,
                TFBlocks.underbrick_mossy,
                TFBlocks.underbrick_cracked,
                TFBlocks.underbrick_floor,
                TFBlocks.deadrock,
                TFBlocks.deadrock_cracked,
                TFBlocks.deadrock_weathered,
                TFBlocks.trollsteinn,
                TFBlocks.giant_leaves,
                TFBlocks.giant_obsidian,
                TFBlocks.giant_cobblestone,
                TFBlocks.giant_log,
                TFBlocks.castle_brick,
                TFBlocks.castle_brick_worn,
                TFBlocks.castle_brick_cracked,
                TFBlocks.castle_brick_roof,
                TFBlocks.castle_brick_mossy,
                TFBlocks.castle_brick_frame,
                TFBlocks.castle_pillar_encased,
                TFBlocks.castle_pillar_encased_tile,
                TFBlocks.castle_pillar_bold,
                TFBlocks.castle_pillar_bold_tile,
                TFBlocks.castle_stairs_brick,
                TFBlocks.castle_stairs_worn,
                TFBlocks.castle_stairs_cracked,
                TFBlocks.castle_stairs_mossy,
                TFBlocks.castle_stairs_encased,
                TFBlocks.castle_stairs_bold,
                TFBlocks.castle_rune_brick_pink,
                TFBlocks.castle_rune_brick_yellow,
                TFBlocks.castle_rune_brick_blue,
                TFBlocks.castle_rune_brick_purple,
                TFBlocks.castle_door_pink,
                TFBlocks.castle_door_yellow,
                TFBlocks.castle_door_blue,
                TFBlocks.castle_door_purple,
                TFBlocks.cinder_furnace,
                TFBlocks.twilight_portal_miniature_structure,
                //TFBlocks.hedge_maze_miniature_structure,
                //TFBlocks.hollow_hill_miniature_structure,
                //TFBlocks.quest_grove_miniature_structure,
                //TFBlocks.mushroom_tower_miniature_structure,
                TFBlocks.naga_courtyard_miniature_structure,
                TFBlocks.lich_tower_miniature_structure,
                //TFBlocks.minotaur_labyrinth_miniature_structure,
                //TFBlocks.hydra_lair_miniature_structure,
                //TFBlocks.goblin_stronghold_miniature_structure,
                //TFBlocks.dark_tower_miniature_structure,
                //TFBlocks.yeti_cave_miniature_structure,
                //TFBlocks.aurora_palace_miniature_structure,
                //TFBlocks.troll_cave_cottage_miniature_structure,
                //TFBlocks.final_castle_miniature_structure,
                TFBlocks.knightmetal_block,
                TFBlocks.ironwood_block,
                TFBlocks.fiery_block,
                TFBlocks.spiral_bricks,
                TFBlocks.etched_nagastone,
                TFBlocks.nagastone_pillar,
                TFBlocks.nagastone_stairs_left,
                TFBlocks.nagastone_stairs_right,
                TFBlocks.etched_nagastone_mossy,
                TFBlocks.nagastone_pillar_mossy,
                TFBlocks.nagastone_stairs_mossy_left,
                TFBlocks.nagastone_stairs_mossy_right,
                TFBlocks.etched_nagastone_weathered,
                TFBlocks.nagastone_pillar_weathered,
                TFBlocks.nagastone_stairs_weathered_left,
                TFBlocks.nagastone_stairs_weathered_right,
                TFBlocks.iron_ladder,
                //TFBlocks.terrorcotta_circle,
                //TFBlocks.terrorcotta_diagonal,
                TFBlocks.stone_twist,
                TFBlocks.stone_twist_thin,
                //TFBlocks.lapis_block,
                TFBlocks.keepsake_casket,
                TFBlocks.stone_bold
        );

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                TFBlocks.smoker,
                TFBlocks.fire_jet,
                TFBlocks.uberous_soil
        );
    }

    private static Block[] getAllMinecraftOrTwilightBlocks(Predicate<Block> predicate) {
        return Registry.BLOCK.stream()
                .filter(b -> Registry.BLOCK.getKey(b) != null && (Registry.BLOCK.getKey(b).getNamespace().equals(TwilightForestMod.ID) || Registry.BLOCK.getKey(b).getNamespace().equals("minecraft")) && predicate.test(b))
                .toArray(Block[]::new);
    }

    private static final Set<Block> plants;
    static {
        plants = ImmutableSet.<Block>builder().add(
                Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, // BlockTags.SMALL_FLOWERS
                Blocks.SUNFLOWER, Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH, // BlockTags.TALL_FLOWERS
                Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, // BlockTags.FLOWERS
                Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, // BlockTags.LEAVES
                Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, // BlockTags.SAPLINGS
                Blocks.BEETROOTS, Blocks.CARROTS, Blocks.POTATOES, Blocks.WHEAT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM // BlockTags.CROPS
        ).add( // TF addons of above taglists
                TFBlocks.oak_sapling,
                TFBlocks.canopy_sapling,
                TFBlocks.mangrove_sapling,
                TFBlocks.darkwood_sapling,
                TFBlocks.time_sapling,
                TFBlocks.transformation_sapling,
                TFBlocks.mining_sapling,
                TFBlocks.sorting_sapling,
                TFBlocks.hollow_oak_sapling,
                TFBlocks.rainboak_sapling,
                TFBlocks.rainboak_leaves,
                TFBlocks.oak_leaves,
                TFBlocks.canopy_leaves,
                TFBlocks.mangrove_leaves,
                TFBlocks.dark_leaves,
                TFBlocks.time_leaves,
                TFBlocks.transformation_leaves,
                TFBlocks.mining_leaves,
                TFBlocks.sorting_leaves,
                TFBlocks.thorn_leaves,
                TFBlocks.beanstalk_leaves
        ).build();
    }
}
