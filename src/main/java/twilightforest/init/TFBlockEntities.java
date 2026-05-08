package twilightforest.init;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.components.block.ChiseledCanopyBookshelfWrapper;

/**
 * Q33 minimal {@link BlockEntityType} registry. Currently only carries the
 * {@link MasonJarBlockEntity} type. Add new types here as paired-client-block-entities
 * land. Uses Fabric's {@code FabricBlockEntityTypeBuilder} to dodge the
 * Mojang-mappings access restriction on {@code BlockEntitySupplier}.
 */
public final class TFBlockEntities {
    public static final BlockEntityType<twilightforest.block.entity.AntibuilderBlockEntity> ANTIBUILDER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("antibuilder"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.AntibuilderBlockEntity::new,
                    TFBlocks.ANTIBUILDER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.CarminiteBuilderBlockEntity> TOWER_BUILDER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("tower_builder"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.CarminiteBuilderBlockEntity::new,
                    TFBlocks.CARMINITE_BUILDER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.CinderFurnaceBlockEntity> CINDER_FURNACE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("cinder_furnace"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.CinderFurnaceBlockEntity::new,
                    TFBlocks.CINDER_FURNACE.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.GhastTrapBlockEntity> GHAST_TRAP = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("ghast_trap"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.GhastTrapBlockEntity::new,
                    TFBlocks.GHAST_TRAP.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.GrowingBeanstalkBlockEntity> BEANSTALK_GROWER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("beanstalk_grower"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.GrowingBeanstalkBlockEntity::new,
                    TFBlocks.BEANSTALK_GROWER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.SkullChestBlockEntity> SKULL_CHEST = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("skull_chest"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.SkullChestBlockEntity::new,
                    TFBlocks.SKULL_CHEST.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.KeepsakeCasketBlockEntity> KEEPSAKE_CASKET = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("keepsake_casket"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.KeepsakeCasketBlockEntity::new,
                    TFBlocks.KEEPSAKE_CASKET.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.OminousCandleBlockEntity> OMINOUS_CANDLE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("ominous_candle"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.OminousCandleBlockEntity::new,
                    TFBlocks.OMINOUS_CANDLE.get(),
                    TFBlocks.OMINOUS_WHITE_CANDLE.get(),
                    TFBlocks.OMINOUS_ORANGE_CANDLE.get(),
                    TFBlocks.OMINOUS_MAGENTA_CANDLE.get(),
                    TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE.get(),
                    TFBlocks.OMINOUS_YELLOW_CANDLE.get(),
                    TFBlocks.OMINOUS_LIME_CANDLE.get(),
                    TFBlocks.OMINOUS_PINK_CANDLE.get(),
                    TFBlocks.OMINOUS_GRAY_CANDLE.get(),
                    TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE.get(),
                    TFBlocks.OMINOUS_CYAN_CANDLE.get(),
                    TFBlocks.OMINOUS_PURPLE_CANDLE.get(),
                    TFBlocks.OMINOUS_BLUE_CANDLE.get(),
                    TFBlocks.OMINOUS_BROWN_CANDLE.get(),
                    TFBlocks.OMINOUS_GREEN_CANDLE.get(),
                    TFBlocks.OMINOUS_RED_CANDLE.get(),
                    TFBlocks.OMINOUS_BLACK_CANDLE.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.CicadaBlockEntity> CICADA = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("cicada"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.CicadaBlockEntity::new,
                    TFBlocks.CICADA.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.FireflyBlockEntity> FIREFLY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("firefly"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.FireflyBlockEntity::new,
                    TFBlocks.FIREFLY.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.MoonwormBlockEntity> MOONWORM = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("moonworm"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.MoonwormBlockEntity::new,
                    TFBlocks.MOONWORM.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.JarBlockEntity> JAR = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("jar"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.JarBlockEntity::new,
                    TFBlocks.FIREFLY_JAR.get(),
                    TFBlocks.CICADA_JAR.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.AlphaYetiSpawnerBlockEntity> ALPHA_YETI_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("alpha_yeti_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.AlphaYetiSpawnerBlockEntity::new,
                    TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.FinalBossSpawnerBlockEntity> FINAL_BOSS_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("final_boss_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.FinalBossSpawnerBlockEntity::new,
                    TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.HydraSpawnerBlockEntity> HYDRA_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("hydra_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.HydraSpawnerBlockEntity::new,
                    TFBlocks.HYDRA_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.KnightPhantomSpawnerBlockEntity> KNIGHT_PHANTOM_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("knight_phantom_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.KnightPhantomSpawnerBlockEntity::new,
                    TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.LichSpawnerBlockEntity> LICH_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("lich_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.LichSpawnerBlockEntity::new,
                    TFBlocks.LICH_BOSS_SPAWNER.get(),
                    TFBlocks.LICH_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.MinoshroomSpawnerBlockEntity> MINOSHROOM_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("minoshroom_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.MinoshroomSpawnerBlockEntity::new,
                    TFBlocks.MINOSHROOM_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.NagaSpawnerBlockEntity> NAGA_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("naga_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.NagaSpawnerBlockEntity::new,
                    TFBlocks.NAGA_BOSS_SPAWNER.get(),
                    TFBlocks.NAGA_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.SnowQueenSpawnerBlockEntity> SNOW_QUEEN_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("snow_queen_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.SnowQueenSpawnerBlockEntity::new,
                    TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.UrGhastSpawnerBlockEntity> UR_GHAST_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("ur_ghast_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.UrGhastSpawnerBlockEntity::new,
                    TFBlocks.UR_GHAST_BOSS_SPAWNER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity> SINISTER_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("sinister_spawner"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity::new,
                    TFBlocks.SINISTER_SPAWNER.get())
                    .build());

    public static final BlockEntityType<MasonJarBlockEntity> MASON_JAR = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("mason_jar"),
            FabricBlockEntityTypeBuilder.create(MasonJarBlockEntity::new, TFBlocks.MASON_JAR.get()).build());

    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("drying_rack"),
            FabricBlockEntityTypeBuilder.create(DryingRackBlockEntity::new,
                    TFBlocks.OAK_DRYING_RACK.get(),
                    TFBlocks.SPRUCE_DRYING_RACK.get(),
                    TFBlocks.BIRCH_DRYING_RACK.get(),
                    TFBlocks.JUNGLE_DRYING_RACK.get(),
                    TFBlocks.ACACIA_DRYING_RACK.get(),
                    TFBlocks.DARK_OAK_DRYING_RACK.get(),
                    TFBlocks.MANGROVE_DRYING_RACK.get(),
                    TFBlocks.CHERRY_DRYING_RACK.get(),
                    TFBlocks.BAMBOO_DRYING_RACK.get(),
                    TFBlocks.CRIMSON_DRYING_RACK.get(),
                    TFBlocks.WARPED_DRYING_RACK.get(),
                    TFBlocks.TWILIGHT_OAK_DRYING_RACK.get(),
                    TFBlocks.CANOPY_DRYING_RACK.get(),
                    TFBlocks.DARK_DRYING_RACK.get())
                    .build());

    public static final BlockEntityType<ChiseledCanopyShelfBlockEntity> CHISELED_CANOPY_BOOKSHELF = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("chiseled_canopy_bookshelf"),
            FabricBlockEntityTypeBuilder.create(ChiseledCanopyShelfBlockEntity::new,
                    TFBlocks.CHISELED_CANOPY_BOOKSHELF.get())
                    .build());

    public static final BlockEntityType<TrophyBlockEntity> TROPHY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("trophy"),
            FabricBlockEntityTypeBuilder.create(TrophyBlockEntity::new,
                    TFBlocks.NAGA_TROPHY.get(),
                    TFBlocks.LICH_TROPHY.get(),
                    TFBlocks.MINOSHROOM_TROPHY.get(),
                    TFBlocks.HYDRA_TROPHY.get(),
                    TFBlocks.KNIGHT_PHANTOM_TROPHY.get(),
                    TFBlocks.UR_GHAST_TROPHY.get(),
                    TFBlocks.ALPHA_YETI_TROPHY.get(),
                    TFBlocks.SNOW_QUEEN_TROPHY.get(),
                    TFBlocks.QUEST_RAM_TROPHY.get(),
                    TFBlocks.NAGA_WALL_TROPHY.get(),
                    TFBlocks.LICH_WALL_TROPHY.get(),
                    TFBlocks.MINOSHROOM_WALL_TROPHY.get(),
                    TFBlocks.HYDRA_WALL_TROPHY.get(),
                    TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(),
                    TFBlocks.UR_GHAST_WALL_TROPHY.get(),
                    TFBlocks.ALPHA_YETI_WALL_TROPHY.get(),
                    TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(),
                    TFBlocks.QUEST_RAM_WALL_TROPHY.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.RedThreadBlockEntity> RED_THREAD = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("red_thread"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.RedThreadBlockEntity::new,
                    TFBlocks.RED_THREAD.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.FireJetBlockEntity> FLAME_JET = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("flame_jet"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.FireJetBlockEntity::new,
                    TFBlocks.FIRE_JET.get(),
                    TFBlocks.ENCASED_FIRE_JET.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.TFSmokerBlockEntity> SMOKER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("smoker"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.TFSmokerBlockEntity::new,
                    TFBlocks.SMOKER.get(),
                    TFBlocks.ENCASED_SMOKER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.CandelabraBlockEntity> CANDELABRA = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("candelabra"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.CandelabraBlockEntity::new,
                    TFBlocks.CANDELABRA.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.BrazierBlockEntity> BRAZIER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("brazier"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.BrazierBlockEntity::new,
                    TFBlocks.BRAZIER.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.CarminiteReactorBlockEntity> CARMINITE_REACTOR = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("carminite_reactor"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.CarminiteReactorBlockEntity::new,
                    TFBlocks.CARMINITE_REACTOR.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.ReactorDebrisBlockEntity> REACTOR_DEBRIS = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("reactor_debris"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.ReactorDebrisBlockEntity::new,
                    TFBlocks.REACTOR_DEBRIS.get())
                    .build());

    public static final BlockEntityType<twilightforest.block.entity.SkullCandleBlockEntity> SKULL_CANDLE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            TwilightForestMod.prefix("skull_candle"),
            FabricBlockEntityTypeBuilder.create(twilightforest.block.entity.SkullCandleBlockEntity::new,
                    TFBlocks.CREEPER_SKULL_CANDLE.get(),
                    TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(),
                    TFBlocks.SKELETON_SKULL_CANDLE.get(),
                    TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(),
                    TFBlocks.WITHER_SKELETON_SKULL_CANDLE.get(),
                    TFBlocks.WITHER_SKELETON_WALL_SKULL_CANDLE.get(),
                    TFBlocks.ZOMBIE_SKULL_CANDLE.get(),
                    TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(),
                    TFBlocks.PLAYER_SKULL_CANDLE.get(),
                    TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(),
                    TFBlocks.PIGLIN_SKULL_CANDLE.get(),
                    TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get())
                    .build());

    private TFBlockEntities() {}

    public static void bootstrap() {
        includeVanillaSpawnerBlocks(
                TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(),
                TFBlocks.HYDRA_BOSS_SPAWNER.get(),
                TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(),
                TFBlocks.LICH_BOSS_SPAWNER.get(),
                TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(),
                TFBlocks.NAGA_BOSS_SPAWNER.get(),
                TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(),
                TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
                TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
                TFBlocks.NAGA_SPAWNER.get(),
                TFBlocks.LICH_SPAWNER.get(),
                TFBlocks.SINISTER_SPAWNER.get());
        ChiseledCanopyBookshelfWrapper.register();
    }

    private static void includeVanillaSpawnerBlocks(Block... blocks) {
        Set<Block> validBlocks = new HashSet<>(BlockEntityType.MOB_SPAWNER.validBlocks);
        boolean changed = false;
        for (Block block : blocks) {
            changed |= validBlocks.add(block);
        }
        if (changed) {
            BlockEntityType.MOB_SPAWNER.validBlocks = Set.copyOf(validBlocks);
        }
    }
}
