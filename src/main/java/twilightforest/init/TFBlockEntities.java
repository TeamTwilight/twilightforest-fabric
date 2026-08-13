package twilightforest.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TFMain;
import twilightforest.block.entity.*;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.block.entity.spawner.*;

public class TFBlockEntities {

	public static final BlockEntityType<AntibuilderBlockEntity> ANTIBUILDER = register("antibuilder", AntibuilderBlockEntity::new, TFBlocks.ANTIBUILDER);
	public static final BlockEntityType<CinderFurnaceBlockEntity> CINDER_FURNACE = register("cinder_furnace", CinderFurnaceBlockEntity::new, TFBlocks.CINDER_FURNACE);
	public static final BlockEntityType<CarminiteReactorBlockEntity> CARMINITE_REACTOR = register("carminite_reactor", CarminiteReactorBlockEntity::new, TFBlocks.CARMINITE_REACTOR);
	public static final BlockEntityType<ReactorDebrisBlockEntity> REACTOR_DEBRIS = register("reactor_debris", ReactorDebrisBlockEntity::new, TFBlocks.REACTOR_DEBRIS);
	public static final BlockEntityType<FireJetBlockEntity> FLAME_JET = register("flame_jet", FireJetBlockEntity::new, TFBlocks.FIRE_JET, TFBlocks.ENCASED_FIRE_JET);
	public static final BlockEntityType<GhastTrapBlockEntity> GHAST_TRAP = register("ghast_trap", GhastTrapBlockEntity::new, TFBlocks.GHAST_TRAP);
	public static final BlockEntityType<TFSmokerBlockEntity> SMOKER = register("smoker", TFSmokerBlockEntity::new, TFBlocks.SMOKER, TFBlocks.ENCASED_SMOKER);
	public static final BlockEntityType<CarminiteBuilderBlockEntity> TOWER_BUILDER = register("tower_builder", CarminiteBuilderBlockEntity::new, TFBlocks.CARMINITE_BUILDER);

	public static final BlockEntityType<TrophyBlockEntity> TROPHY = register("trophy", TrophyBlockEntity::new,
		TFBlocks.NAGA_TROPHY, TFBlocks.LICH_TROPHY, TFBlocks.MINOSHROOM_TROPHY, TFBlocks.HYDRA_TROPHY,
		TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.UR_GHAST_TROPHY, TFBlocks.ALPHA_YETI_TROPHY,
		TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.QUEST_RAM_TROPHY, TFBlocks.NAGA_WALL_TROPHY, TFBlocks.LICH_WALL_TROPHY,
		TFBlocks.MINOSHROOM_WALL_TROPHY, TFBlocks.HYDRA_WALL_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY,
		TFBlocks.UR_GHAST_WALL_TROPHY, TFBlocks.ALPHA_YETI_WALL_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY,
		TFBlocks.QUEST_RAM_WALL_TROPHY);

	public static final BlockEntityType<AlphaYetiSpawnerBlockEntity> ALPHA_YETI_SPAWNER = register("alpha_yeti_spawner", AlphaYetiSpawnerBlockEntity::new, TFBlocks.ALPHA_YETI_BOSS_SPAWNER);
	public static final BlockEntityType<FinalBossSpawnerBlockEntity> FINAL_BOSS_SPAWNER = register("final_boss_spawner", FinalBossSpawnerBlockEntity::new, TFBlocks.FINAL_BOSS_BOSS_SPAWNER);
	public static final BlockEntityType<HydraSpawnerBlockEntity> HYDRA_SPAWNER = register("hydra_boss_spawner", HydraSpawnerBlockEntity::new, TFBlocks.HYDRA_BOSS_SPAWNER);
	public static final BlockEntityType<KnightPhantomSpawnerBlockEntity> KNIGHT_PHANTOM_SPAWNER = register("knight_phantom_spawner", KnightPhantomSpawnerBlockEntity::new, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER);
	public static final BlockEntityType<LichSpawnerBlockEntity> LICH_SPAWNER = register("lich_spawner", LichSpawnerBlockEntity::new, TFBlocks.LICH_BOSS_SPAWNER);
	public static final BlockEntityType<MinoshroomSpawnerBlockEntity> MINOSHROOM_SPAWNER = register("minoshroom_spawner", MinoshroomSpawnerBlockEntity::new, TFBlocks.MINOSHROOM_BOSS_SPAWNER);
	public static final BlockEntityType<NagaSpawnerBlockEntity> NAGA_SPAWNER = register("naga_spawner", NagaSpawnerBlockEntity::new, TFBlocks.NAGA_BOSS_SPAWNER);
	public static final BlockEntityType<SnowQueenSpawnerBlockEntity> SNOW_QUEEN_SPAWNER = register("snow_queen_spawner", SnowQueenSpawnerBlockEntity::new, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER);
	public static final BlockEntityType<UrGhastSpawnerBlockEntity> UR_GHAST_SPAWNER = register("tower_boss_spawner", UrGhastSpawnerBlockEntity::new, TFBlocks.UR_GHAST_BOSS_SPAWNER);

	public static final BlockEntityType<CicadaBlockEntity> CICADA = register("cicada", CicadaBlockEntity::new, TFBlocks.CICADA);
	public static final BlockEntityType<FireflyBlockEntity> FIREFLY = register("firefly", FireflyBlockEntity::new, TFBlocks.FIREFLY);
	public static final BlockEntityType<MoonwormBlockEntity> MOONWORM = register("moonworm", MoonwormBlockEntity::new, TFBlocks.MOONWORM);

	public static final BlockEntityType<SkullChestBlockEntity> SKULL_CHEST = register("skull_chest", SkullChestBlockEntity::new, TFBlocks.SKULL_CHEST);
	public static final BlockEntityType<KeepsakeCasketBlockEntity> KEEPSAKE_CASKET = register("keepsake_casket", KeepsakeCasketBlockEntity::new, TFBlocks.KEEPSAKE_CASKET);
	public static final BlockEntityType<BrazierBlockEntity> BRAZIER = register("brazier", BrazierBlockEntity::new, TFBlocks.BRAZIER);

	public static final BlockEntityType<TFChestBlockEntity> TF_CHEST = register("chest", TFChestBlockEntity::new,
		TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.CANOPY_CHEST, TFBlocks.MANGROVE_CHEST, TFBlocks.DARK_CHEST,
		TFBlocks.TIME_CHEST, TFBlocks.TRANSFORMATION_CHEST, TFBlocks.MINING_CHEST, TFBlocks.SORTING_CHEST);

	public static final BlockEntityType<TFTrappedChestBlockEntity> TF_TRAPPED_CHEST = register("trapped_chest", TFTrappedChestBlockEntity::new,
		TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.DARK_TRAPPED_CHEST,
		TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.SORTING_TRAPPED_CHEST);

	public static final BlockEntityType<SkullCandleBlockEntity> SKULL_CANDLE = register("skull_candle", SkullCandleBlockEntity::new,
		TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE, TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE,
		TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE, TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE,
		TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE, TFBlocks.PIGLIN_SKULL_CANDLE, TFBlocks.PIGLIN_WALL_SKULL_CANDLE);

	public static final BlockEntityType<ChiseledCanopyShelfBlockEntity> CHISELED_CANOPY_BOOKSHELF = register("chiseled_canopy_bookshelf", ChiseledCanopyShelfBlockEntity::new, TFBlocks.CHISELED_CANOPY_BOOKSHELF);
	public static final BlockEntityType<GrowingBeanstalkBlockEntity> BEANSTALK_GROWER = register("beanstalk_grower", GrowingBeanstalkBlockEntity::new, TFBlocks.BEANSTALK_GROWER);
	public static final BlockEntityType<RedThreadBlockEntity> RED_THREAD = register("red_thread", RedThreadBlockEntity::new, TFBlocks.RED_THREAD);
	public static final BlockEntityType<CandelabraBlockEntity> CANDELABRA = register("candelabra", CandelabraBlockEntity::new, TFBlocks.CANDELABRA);
	public static final BlockEntityType<JarBlockEntity> JAR = register("jar", JarBlockEntity::new, TFBlocks.FIREFLY_JAR, TFBlocks.CICADA_JAR);
	public static final BlockEntityType<MasonJarBlockEntity> MASON_JAR = register("mason_jar", MasonJarBlockEntity::new, TFBlocks.MASON_JAR);
	public static final BlockEntityType<SinisterSpawnerBlockEntity> SINISTER_SPAWNER = register("sinister_spawner", SinisterSpawnerBlockEntity::new, TFBlocks.SINISTER_SPAWNER);

	public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK = register("drying_rack", DryingRackBlockEntity::new,
		TFBlocks.OAK_DRYING_RACK, TFBlocks.SPRUCE_DRYING_RACK, TFBlocks.BIRCH_DRYING_RACK, TFBlocks.JUNGLE_DRYING_RACK,
		TFBlocks.ACACIA_DRYING_RACK, TFBlocks.DARK_OAK_DRYING_RACK, TFBlocks.CRIMSON_DRYING_RACK, TFBlocks.WARPED_DRYING_RACK,
		TFBlocks.VANGROVE_DRYING_RACK, TFBlocks.BAMBOO_DRYING_RACK, TFBlocks.CHERRY_DRYING_RACK, TFBlocks.PALE_OAK_DRYING_RACK,
		TFBlocks.TWILIGHT_OAK_DRYING_RACK, TFBlocks.CANOPY_DRYING_RACK, TFBlocks.MANGROVE_DRYING_RACK, TFBlocks.DARK_DRYING_RACK,
		TFBlocks.TIME_DRYING_RACK, TFBlocks.TRANSFORMATION_DRYING_RACK, TFBlocks.MINING_DRYING_RACK, TFBlocks.SORTING_DRYING_RACK);

	public static final BlockEntityType<OminousCandleBlockEntity> OMINOUS_CANDLE = register("ominous_candle", OminousCandleBlockEntity::new,
		TFBlocks.OMINOUS_CANDLE, TFBlocks.OMINOUS_WHITE_CANDLE, TFBlocks.OMINOUS_ORANGE_CANDLE, TFBlocks.OMINOUS_MAGENTA_CANDLE,
		TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE, TFBlocks.OMINOUS_YELLOW_CANDLE, TFBlocks.OMINOUS_LIME_CANDLE, TFBlocks.OMINOUS_PINK_CANDLE,
		TFBlocks.OMINOUS_GRAY_CANDLE, TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE, TFBlocks.OMINOUS_CYAN_CANDLE, TFBlocks.OMINOUS_PURPLE_CANDLE,
		TFBlocks.OMINOUS_BLUE_CANDLE, TFBlocks.OMINOUS_BROWN_CANDLE, TFBlocks.OMINOUS_GREEN_CANDLE, TFBlocks.OMINOUS_RED_CANDLE,
		TFBlocks.OMINOUS_BLACK_CANDLE);

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TFMain.prefix(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing block entities...");
	}
}