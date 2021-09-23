package twilightforest.tileentity;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import twilightforest.TFConstants;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.ISTER;
import twilightforest.client.renderer.tileentity.*;
import twilightforest.tileentity.spawner.*;

import java.util.List;

public class TFTileEntities {

	//public static final DeferredRegister<BlockEntityType<?> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TwilightForestMod.ID);

	public static final BlockEntityType<AntibuilderTileEntity> ANTIBUILDER               = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":antibuilder",
			BlockEntityType.Builder.of(AntibuilderTileEntity::new, TFBlocks.antibuilder).build(null));
	public static final BlockEntityType<CinderFurnaceTileEntity> CINDER_FURNACE          = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":cinder_furnace",
			BlockEntityType.Builder.of(CinderFurnaceTileEntity::new, TFBlocks.cinder_furnace).build(null));
	public static final BlockEntityType<ActiveCarminiteReactorTileEntity> CARMINITE_REACTOR      = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":carminite_reactor",
			BlockEntityType.Builder.of(ActiveCarminiteReactorTileEntity::new, TFBlocks.carminite_reactor).build(null));
	public static final BlockEntityType<FireJetTileEntity> FLAME_JET                    = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":flame_jet",
			BlockEntityType.Builder.of(FireJetTileEntity::new, TFBlocks.fire_jet, TFBlocks.encased_fire_jet).build(null));
	public static final BlockEntityType<ActiveGhastTrapTileEntity> GHAST_TRAP = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":ghast_trap",
			BlockEntityType.Builder.of(ActiveGhastTrapTileEntity::new, TFBlocks.ghast_trap).build(null));
	public static final BlockEntityType<TFSmokerTileEntity> SMOKER                           = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":smoker",
			BlockEntityType.Builder.of(TFSmokerTileEntity::new, TFBlocks.smoker, TFBlocks.encased_smoker).build(null));
	public static final BlockEntityType<CarminiteBuilderTileEntity> TOWER_BUILDER            = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":tower_builder",
			BlockEntityType.Builder.of(CarminiteBuilderTileEntity::new, TFBlocks.carminite_builder).build(null));
	public static final BlockEntityType<TrophyTileEntity> TROPHY                         = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":trophy",
			BlockEntityType.Builder.of(TrophyTileEntity::new, TFBlocks.naga_trophy, TFBlocks.lich_trophy, TFBlocks.minoshroom_trophy,
					TFBlocks.hydra_trophy, TFBlocks.knight_phantom_trophy, TFBlocks.ur_ghast_trophy, TFBlocks.yeti_trophy,
					TFBlocks.snow_queen_trophy, TFBlocks.quest_ram_trophy, TFBlocks.naga_wall_trophy, TFBlocks.lich_wall_trophy,
					TFBlocks.minoshroom_wall_trophy, TFBlocks.hydra_wall_trophy, TFBlocks.knight_phantom_wall_trophy, TFBlocks.ur_ghast_wall_trophy,
					TFBlocks.yeti_wall_trophy, TFBlocks.snow_queen_wall_trophy, TFBlocks.quest_ram_wall_trophy).build(null));
	public static final BlockEntityType<AlphaYetiSpawnerTileEntity> ALPHA_YETI_SPAWNER     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":alpha_yeti_spawner",
			BlockEntityType.Builder.of(AlphaYetiSpawnerTileEntity::new, TFBlocks.boss_spawner_alpha_yeti).build(null));
	public static final BlockEntityType<FinalBossSpawnerTileEntity> FINAL_BOSS_SPAWNER     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":final_boss_spawner",
			BlockEntityType.Builder.of(FinalBossSpawnerTileEntity::new, TFBlocks.boss_spawner_final_boss).build(null));
	public static final BlockEntityType<HydraSpawnerTileEntity> HYDRA_SPAWNER          = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":hydra_boss_spawner",
			BlockEntityType.Builder.of(HydraSpawnerTileEntity::new, TFBlocks.boss_spawner_hydra).build(null));
	public static final BlockEntityType<KnightPhantomSpawnerTileEntity> KNIGHT_PHANTOM_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":knight_phantom_spawner",
			BlockEntityType.Builder.of(KnightPhantomSpawnerTileEntity::new, TFBlocks.boss_spawner_knight_phantom).build(null));
	public static final BlockEntityType<LichSpawnerTileEntity> LICH_SPAWNER           = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":lich_spawner",
			BlockEntityType.Builder.of(LichSpawnerTileEntity::new, TFBlocks.boss_spawner_lich).build(null));
	public static final BlockEntityType<MinoshroomSpawnerTileEntity> MINOSHROOM_SPAWNER     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":minoshroom_spawner",
			BlockEntityType.Builder.of(MinoshroomSpawnerTileEntity::new, TFBlocks.boss_spawner_minoshroom).build(null));
	public static final BlockEntityType<NagaSpawnerTileEntity> NAGA_SPAWNER           = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":naga_spawner",
			BlockEntityType.Builder.of(NagaSpawnerTileEntity::new, TFBlocks.boss_spawner_naga).build(null));
	public static final BlockEntityType<SnowQueenSpawnerTileEntity> SNOW_QUEEN_SPAWNER     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":snow_queen_spawner",
			BlockEntityType.Builder.of(SnowQueenSpawnerTileEntity::new, TFBlocks.boss_spawner_snow_queen).build(null));
	public static final BlockEntityType<UrGhastSpawnerTileEntity> UR_GHAST_SPAWNER     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":tower_boss_spawner",
			BlockEntityType.Builder.of(UrGhastSpawnerTileEntity::new, TFBlocks.boss_spawner_ur_ghast).build(null));

	public static final BlockEntityType<CicadaTileEntity> CICADA     = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":cicada",
			BlockEntityType.Builder.of(CicadaTileEntity::new, TFBlocks.cicada).build(null));
	public static final BlockEntityType<FireflyTileEntity> FIREFLY   = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":firefly",
			BlockEntityType.Builder.of(FireflyTileEntity::new, TFBlocks.firefly).build(null));
	public static final BlockEntityType<MoonwormTileEntity> MOONWORM = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":moonworm",
			BlockEntityType.Builder.of(MoonwormTileEntity::new, TFBlocks.moonworm).build(null));

	public static final BlockEntityType<KeepsakeCasketTileEntity> KEEPSAKE_CASKET          = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":keepsake_casket",
			BlockEntityType.Builder.of(KeepsakeCasketTileEntity::new, TFBlocks.keepsake_casket).build(null));

	public static final BlockEntityType<TFSignTileEntity> TF_SIGN = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":tf_sign",
			BlockEntityType.Builder.of(TFSignTileEntity::new,
					TFBlocks.twilight_oak_sign, TFBlocks.twilight_wall_sign,
					TFBlocks.canopy_sign, TFBlocks.canopy_wall_sign,
					TFBlocks.mangrove_sign, TFBlocks.mangrove_wall_sign,
					TFBlocks.darkwood_sign, TFBlocks.darkwood_wall_sign,
					TFBlocks.time_sign, TFBlocks.time_wall_sign,
					TFBlocks.trans_sign, TFBlocks.trans_wall_sign,
					TFBlocks.mine_sign, TFBlocks.mine_wall_sign,
					TFBlocks.sort_sign, TFBlocks.sort_wall_sign).build(null));

	public static final BlockEntityType<SkullCandleTileEntity> SKULL_CANDLE = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":skull_candle",
			BlockEntityType.Builder.of(SkullCandleTileEntity::new,
					TFBlocks.zombie_skull_candle, TFBlocks.zombie_wall_skull_candle,
					TFBlocks.skeleton_skull_candle, TFBlocks.skeleton_wall_skull_candle,
					TFBlocks.wither_skele_skull_candle, TFBlocks.wither_skele_wall_skull_candle,
					TFBlocks.creeper_skull_candle, TFBlocks.creeper_wall_skull_candle,
					TFBlocks.player_skull_candle, TFBlocks.player_wall_skull_candle).build(null));

	public static final BlockEntityType<TomeSpawnerTileEntity> TOME_SPAWNER = Registry.register(Registry.BLOCK_ENTITY_TYPE, TFConstants.ID+":tome_spawner",
			BlockEntityType.Builder.of(TomeSpawnerTileEntity::new, TFBlocks.tome_spawner).build(null));

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void registerTileEntityRenders() {
		// tile entities
		//Fabric has a api for this but I already made the aw
		BlockEntityRendererRegistry.INSTANCE.register(FIREFLY, FireflyTileEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(CICADA, CicadaTileEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(MOONWORM, MoonwormTileEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(TROPHY, TrophyTileEntityRenderer::new);
		BlockEntityRenderers.register(TF_SIGN, SignRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(KEEPSAKE_CASKET, CasketTileEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(SKULL_CANDLE, SkullCandleTileEntityRenderer::new);

	}

	public static void registerTileEntitysItemRenders(){
		List<AbstractTrophyBlock> TROPHYS = List.of(TFBlocks.naga_trophy, TFBlocks.lich_trophy, TFBlocks.minoshroom_trophy,
				TFBlocks.hydra_trophy, TFBlocks.knight_phantom_trophy, TFBlocks.ur_ghast_trophy, TFBlocks.yeti_trophy,
				TFBlocks.snow_queen_trophy, TFBlocks.quest_ram_trophy);

		List<AbstractSkullCandleBlock> SKULLCANDLES = List.of(TFBlocks.zombie_skull_candle, TFBlocks.skeleton_skull_candle,
				TFBlocks.wither_skele_skull_candle, TFBlocks.creeper_skull_candle, TFBlocks.player_skull_candle);

		for(AbstractTrophyBlock block : TROPHYS){
			BuiltinItemRendererRegistry.INSTANCE.register(block, (itemStack, transform, stack, source, light, overlay) -> {
				new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.TROPHY)).renderByItem(itemStack, transform, stack, source, light, overlay);
			});
		}

		for(AbstractSkullCandleBlock block : SKULLCANDLES){
			BuiltinItemRendererRegistry.INSTANCE.register(block, (itemStack, transform, stack, source, light, overlay) -> {
				new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.SKULL_CANDLE)).renderByItem(itemStack, transform, stack, source, light, overlay);
			});
		}

		BuiltinItemRendererRegistry.INSTANCE.register(TFBlocks.cicada, (itemStack, transform, stack, source, light, overlay) -> {
			new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.CICADA)).renderByItem(itemStack, transform, stack, source, light, overlay);
		});

		BuiltinItemRendererRegistry.INSTANCE.register(TFBlocks.firefly, (itemStack, transform, stack, source, light, overlay) -> {
			new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.FIREFLY)).renderByItem(itemStack, transform, stack, source, light, overlay);
		});

		BuiltinItemRendererRegistry.INSTANCE.register(TFBlocks.moonworm, (itemStack, transform, stack, source, light, overlay) -> {
			new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.MOONWORM)).renderByItem(itemStack, transform, stack, source, light, overlay);
		});

		BuiltinItemRendererRegistry.INSTANCE.register(TFBlocks.keepsake_casket, (itemStack, transform, stack, source, light, overlay) -> {
			new ISTER(Registry.BLOCK_ENTITY_TYPE.getKey(TFTileEntities.KEEPSAKE_CASKET)).renderByItem(itemStack, transform, stack, source, light, overlay);
		});




	}

}
