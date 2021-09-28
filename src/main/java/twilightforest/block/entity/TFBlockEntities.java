package twilightforest.block.entity;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.renderer.tileentity.*;
import twilightforest.block.entity.spawner.*;

public class TFBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TwilightForestMod.ID);

	public static final RegistryObject<BlockEntityType<AntibuilderBlockEntity>> ANTIBUILDER               = TILE_ENTITIES.register("antibuilder", () ->
			BlockEntityType.Builder.of(AntibuilderBlockEntity::new, TFBlocks.antibuilder.get()).build(null));
	public static final RegistryObject<BlockEntityType<CinderFurnaceBlockEntity>> CINDER_FURNACE          = TILE_ENTITIES.register("cinder_furnace", () ->
			BlockEntityType.Builder.of(CinderFurnaceBlockEntity::new, TFBlocks.cinder_furnace.get()).build(null));
	public static final RegistryObject<BlockEntityType<CarminiteReactorBlockEntity>> CARMINITE_REACTOR      = TILE_ENTITIES.register("carminite_reactor", () ->
			BlockEntityType.Builder.of(CarminiteReactorBlockEntity::new, TFBlocks.carminite_reactor.get()).build(null));
	public static final RegistryObject<BlockEntityType<FireJetBlockEntity>> FLAME_JET                    = TILE_ENTITIES.register("flame_jet", () ->
			BlockEntityType.Builder.of(FireJetBlockEntity::new, TFBlocks.fire_jet.get(), TFBlocks.encased_fire_jet.get()).build(null));
	public static final RegistryObject<BlockEntityType<GhastTrapBlockEntity>> GHAST_TRAP = TILE_ENTITIES.register("ghast_trap", () ->
			BlockEntityType.Builder.of(GhastTrapBlockEntity::new, TFBlocks.ghast_trap.get()).build(null));
	public static final RegistryObject<BlockEntityType<TFSmokerBlockEntity>> SMOKER                           = TILE_ENTITIES.register("smoker", () ->
			BlockEntityType.Builder.of(TFSmokerBlockEntity::new, TFBlocks.smoker.get(), TFBlocks.encased_smoker.get()).build(null));
	public static final RegistryObject<BlockEntityType<CarminiteBuilderBlockEntity>> TOWER_BUILDER            = TILE_ENTITIES.register("tower_builder", () ->
			BlockEntityType.Builder.of(CarminiteBuilderBlockEntity::new, TFBlocks.carminite_builder.get()).build(null));
	public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY                         = TILE_ENTITIES.register("trophy", () ->
			BlockEntityType.Builder.of(TrophyBlockEntity::new, TFBlocks.naga_trophy.get(), TFBlocks.lich_trophy.get(), TFBlocks.minoshroom_trophy.get(),
					TFBlocks.hydra_trophy.get(), TFBlocks.knight_phantom_trophy.get(), TFBlocks.ur_ghast_trophy.get(), TFBlocks.yeti_trophy.get(),
					TFBlocks.snow_queen_trophy.get(), TFBlocks.quest_ram_trophy.get(), TFBlocks.naga_wall_trophy.get(), TFBlocks.lich_wall_trophy.get(),
					TFBlocks.minoshroom_wall_trophy.get(), TFBlocks.hydra_wall_trophy.get(), TFBlocks.knight_phantom_wall_trophy.get(), TFBlocks.ur_ghast_wall_trophy.get(),
					TFBlocks.yeti_wall_trophy.get(), TFBlocks.snow_queen_wall_trophy.get(), TFBlocks.quest_ram_wall_trophy.get()).build(null));
	public static final RegistryObject<BlockEntityType<AlphaYetiSpawnerBlockEntity>> ALPHA_YETI_SPAWNER     = TILE_ENTITIES.register("alpha_yeti_spawner", () ->
			BlockEntityType.Builder.of(AlphaYetiSpawnerBlockEntity::new, TFBlocks.boss_spawner_alpha_yeti.get()).build(null));
	public static final RegistryObject<BlockEntityType<FinalBossSpawnerBlockEntity>> FINAL_BOSS_SPAWNER     = TILE_ENTITIES.register("final_boss_spawner", () ->
			BlockEntityType.Builder.of(FinalBossSpawnerBlockEntity::new, TFBlocks.boss_spawner_final_boss.get()).build(null));
	public static final RegistryObject<BlockEntityType<HydraSpawnerBlockEntity>> HYDRA_SPAWNER          = TILE_ENTITIES.register("hydra_boss_spawner", () ->
			BlockEntityType.Builder.of(HydraSpawnerBlockEntity::new, TFBlocks.boss_spawner_hydra.get()).build(null));
	public static final RegistryObject<BlockEntityType<KnightPhantomSpawnerBlockEntity>> KNIGHT_PHANTOM_SPAWNER = TILE_ENTITIES.register("knight_phantom_spawner", () ->
			BlockEntityType.Builder.of(KnightPhantomSpawnerBlockEntity::new, TFBlocks.boss_spawner_knight_phantom.get()).build(null));
	public static final RegistryObject<BlockEntityType<LichSpawnerBlockEntity>> LICH_SPAWNER           = TILE_ENTITIES.register("lich_spawner", () ->
			BlockEntityType.Builder.of(LichSpawnerBlockEntity::new, TFBlocks.boss_spawner_lich.get()).build(null));
	public static final RegistryObject<BlockEntityType<MinoshroomSpawnerBlockEntity>> MINOSHROOM_SPAWNER     = TILE_ENTITIES.register("minoshroom_spawner", () ->
			BlockEntityType.Builder.of(MinoshroomSpawnerBlockEntity::new, TFBlocks.boss_spawner_minoshroom.get()).build(null));
	public static final RegistryObject<BlockEntityType<NagaSpawnerBlockEntity>> NAGA_SPAWNER           = TILE_ENTITIES.register("naga_spawner", () ->
			BlockEntityType.Builder.of(NagaSpawnerBlockEntity::new, TFBlocks.boss_spawner_naga.get()).build(null));
	public static final RegistryObject<BlockEntityType<SnowQueenSpawnerBlockEntity>> SNOW_QUEEN_SPAWNER     = TILE_ENTITIES.register("snow_queen_spawner", () ->
			BlockEntityType.Builder.of(SnowQueenSpawnerBlockEntity::new, TFBlocks.boss_spawner_snow_queen.get()).build(null));
	public static final RegistryObject<BlockEntityType<UrGhastSpawnerBlockEntity>> UR_GHAST_SPAWNER     = TILE_ENTITIES.register("tower_boss_spawner", () ->
			BlockEntityType.Builder.of(UrGhastSpawnerBlockEntity::new, TFBlocks.boss_spawner_ur_ghast.get()).build(null));

	public static final RegistryObject<BlockEntityType<CicadaBlockEntity>> CICADA     = TILE_ENTITIES.register("cicada", () ->
			BlockEntityType.Builder.of(CicadaBlockEntity::new, TFBlocks.cicada.get()).build(null));
	public static final RegistryObject<BlockEntityType<FireflyBlockEntity>> FIREFLY   = TILE_ENTITIES.register("firefly", () ->
			BlockEntityType.Builder.of(FireflyBlockEntity::new, TFBlocks.firefly.get()).build(null));
	public static final RegistryObject<BlockEntityType<MoonwormBlockEntity>> MOONWORM = TILE_ENTITIES.register("moonworm", () ->
			BlockEntityType.Builder.of(MoonwormBlockEntity::new, TFBlocks.moonworm.get()).build(null));

	public static final RegistryObject<BlockEntityType<KeepsakeCasketBlockEntity>> KEEPSAKE_CASKET          = TILE_ENTITIES.register("keepsake_casket", () ->
			BlockEntityType.Builder.of(KeepsakeCasketBlockEntity::new, TFBlocks.keepsake_casket.get()).build(null));

	public static final RegistryObject<BlockEntityType<TFSignBlockEntity>> TF_SIGN = TILE_ENTITIES.register("tf_sign", () ->
			BlockEntityType.Builder.of(TFSignBlockEntity::new,
					TFBlocks.twilight_oak_sign.get(), TFBlocks.twilight_wall_sign.get(),
					TFBlocks.canopy_sign.get(), TFBlocks.canopy_wall_sign.get(),
					TFBlocks.mangrove_sign.get(), TFBlocks.mangrove_wall_sign.get(),
					TFBlocks.darkwood_sign.get(), TFBlocks.darkwood_wall_sign.get(),
					TFBlocks.time_sign.get(), TFBlocks.time_wall_sign.get(),
					TFBlocks.trans_sign.get(), TFBlocks.trans_wall_sign.get(),
					TFBlocks.mine_sign.get(), TFBlocks.mine_wall_sign.get(),
					TFBlocks.sort_sign.get(), TFBlocks.sort_wall_sign.get()).build(null));

	public static final RegistryObject<BlockEntityType<SkullCandleBlockEntity>> SKULL_CANDLE = TILE_ENTITIES.register("skull_candle", () ->
			BlockEntityType.Builder.of(SkullCandleBlockEntity::new,
					TFBlocks.zombie_skull_candle.get(), TFBlocks.zombie_wall_skull_candle.get(),
					TFBlocks.skeleton_skull_candle.get(), TFBlocks.skeleton_wall_skull_candle.get(),
					TFBlocks.wither_skele_skull_candle.get(), TFBlocks.wither_skele_wall_skull_candle.get(),
					TFBlocks.creeper_skull_candle.get(), TFBlocks.creeper_wall_skull_candle.get(),
					TFBlocks.player_skull_candle.get(), TFBlocks.player_wall_skull_candle.get()).build(null));

	public static final RegistryObject<BlockEntityType<TomeSpawnerBlockEntity>> TOME_SPAWNER = TILE_ENTITIES.register("tome_spawner", () ->
			BlockEntityType.Builder.of(TomeSpawnerBlockEntity::new, TFBlocks.tome_spawner.get()).build(null));

	@OnlyIn(Dist.CLIENT)
	public static void registerTileEntityRenders() {
		// tile entities
		BlockEntityRenderers.register(FIREFLY.get(), FireflyTileEntityRenderer::new);
		BlockEntityRenderers.register(CICADA.get(), CicadaTileEntityRenderer::new);
		BlockEntityRenderers.register(MOONWORM.get(), MoonwormTileEntityRenderer::new);
		BlockEntityRenderers.register(TROPHY.get(), TrophyTileEntityRenderer::new);
		BlockEntityRenderers.register(TF_SIGN.get(), SignRenderer::new);
		BlockEntityRenderers.register(KEEPSAKE_CASKET.get(), CasketTileEntityRenderer::new);
		BlockEntityRenderers.register(SKULL_CANDLE.get(), SkullCandleTileEntityRenderer::new);
	}
}
