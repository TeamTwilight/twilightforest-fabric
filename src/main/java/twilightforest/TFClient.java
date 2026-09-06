package twilightforest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.silverfish.SilverfishModel;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.client.MagicPaintingAtlasInfo;
import twilightforest.client.TextureGeneratorReloadListener;
import twilightforest.client.UncraftingScreen;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.entity.*;
import twilightforest.client.particle.*;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.client.renderer.map.MapDecorationManager;
import twilightforest.client.renderer.special.*;
import twilightforest.init.*;
import twilightforest.item.mapdata.MapDataManager;
import twilightforest.network.*;

public final class TFClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		TFClientEvents.init();
		MapDataManager.init();
		TFTintSources.init();

		registerPackets();
		registerSpecialModelRenderers();
		registerAtlases();
		registerClientReloadListeners();
		registerScreens();
		registerEntityRenderers();
		registerBlockEntityRenderers();
		registerLayerDefinitions();
		registerParticleFactories();
		registerMapDecorators();
		registerRenderLayers();
	}

	private static void registerPackets() {
		ClientPlayNetworking.registerGlobalReceiver(AreaProtectionPacket.TYPE, AreaProtectionPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MagicMapPacket.TYPE, MagicMapPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MazeMapPacket.TYPE, MazeMapPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MovePlayerPacket.TYPE, MovePlayerPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(ParticlePacket.TYPE, ParticlePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handleClient);
		ClientPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handleClient);
		ClientPlayNetworking.registerGlobalReceiver(SpawnCharmPacket.TYPE, SpawnCharmPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(StructureProtectionPacket.TYPE, StructureProtectionPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateThrownPacket.TYPE, UpdateThrownPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(LifedrainParticlePacket.TYPE, LifedrainParticlePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncQuestsPacket.TYPE, SyncQuestsPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket::handle);
	}

	private static void registerSpecialModelRenderers() {
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("candelabra"), CandelabraSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("cicada"), CicadaSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("firefly"), FireflySpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("keepsake_casket"), KeepsakeCasketSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("knightmetal_shield"), KnightmetalShieldSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("mason_jar"), MasonJarSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("moonworm"), MoonwormSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("mystic_crown"), MysticCrownSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("skull_candle"), SkullCandleSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("skull_chest"), SkullChestSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TFMain.prefix("trophy"), TrophySpecialRenderer.Unbaked.MAP_CODEC);
	}

	private static void registerAtlases() {
		AtlasRegistry.register(new AtlasManager.AtlasConfig(MagicPaintingAtlasInfo.ATLAS_LOCATION, MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION, false));
	}

	private static void registerClientReloadListeners() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(TFMain.prefix("texture_generator"), TextureGeneratorReloadListener.INSTANCE);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(TFMain.prefix("armor_cache"), new TFArmorRenderer.ResourceReloadListener());
	}

	private static void registerScreens() {
		MenuScreens.register(TFMenuTypes.UNCRAFTING, UncraftingScreen::new);
	}

	private static void registerEntityRenderers() {
		EntityRenderers.register(TFEntities.BOAR, BoarRenderer::new);
		EntityRenderers.register(TFEntities.BIGHORN_SHEEP, BighornRenderer::new);
		EntityRenderers.register(TFEntities.DEER, DeerRenderer::new);
		EntityRenderers.register(TFEntities.REDCAP, RedcapRenderer::new);
		EntityRenderers.register(TFEntities.SKELETON_DRUID, SkeletonDruidRenderer::new);
		EntityRenderers.register(TFEntities.HOSTILE_WOLF, HostileWolfRenderer::new);
		EntityRenderers.register(TFEntities.WRAITH, WraithRenderer::new);
		EntityRenderers.register(TFEntities.HYDRA, HydraRenderer::new);
		EntityRenderers.register(TFEntities.LICH, LichRenderer::new);
		EntityRenderers.register(TFEntities.PENGUIN, m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN_BABY)), 0.375F, "penguin.png"));
		EntityRenderers.register(TFEntities.LICH_MINION, LichMinionRenderer::new);
		EntityRenderers.register(TFEntities.LOYAL_ZOMBIE, LoyalZombieRenderer::new);
		EntityRenderers.register(TFEntities.TINY_BIRD, TinyBirdRenderer::new);
		EntityRenderers.register(TFEntities.SQUIRREL, SquirrelRenderer::new);
		EntityRenderers.register(TFEntities.DWARF_RABBIT, BunnyRenderer::new);
		EntityRenderers.register(TFEntities.RAVEN, m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRenderers.register(TFEntities.QUEST_RAM, QuestRamRenderer::new);
		EntityRenderers.register(TFEntities.KOBOLD, KoboldRenderer::new);
		EntityRenderers.register(TFEntities.MOSQUITO_SWARM, MosquitoSwarmRenderer::new);
		EntityRenderers.register(TFEntities.DEATH_TOME, DeathTomeRenderer::new);
		EntityRenderers.register(TFEntities.MINOTAUR, MinotaurRenderer::new);
		EntityRenderers.register(TFEntities.MINOSHROOM, MinoshroomRenderer::new);
		EntityRenderers.register(TFEntities.FIRE_BEETLE, FireBeetleRenderer::new);
		EntityRenderers.register(TFEntities.SLIME_BEETLE, SlimeBeetleRenderer::new);
		EntityRenderers.register(TFEntities.PINCH_BEETLE, PinchBeetleRenderer::new);
		EntityRenderers.register(TFEntities.MIST_WOLF, MistWolfRenderer::new);
		EntityRenderers.register(TFEntities.CARMINITE_GHASTLING, m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRenderers.register(TFEntities.CARMINITE_GOLEM, CarminiteGolemRenderer::new);
		EntityRenderers.register(TFEntities.TOWERWOOD_BORER, TowerwoodBorerRenderer::new);
		EntityRenderers.register(TFEntities.CARMINITE_GHASTGUARD, CarminiteGhastRenderer::new);
		EntityRenderers.register(TFEntities.UR_GHAST, UrGhastRenderer::new);
		EntityRenderers.register(TFEntities.BLOCKCHAIN_GOBLIN, BlockChainGoblinRenderer::new);
		EntityRenderers.register(TFEntities.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightRenderer::new);
		EntityRenderers.register(TFEntities.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightRenderer::new);
		EntityRenderers.register(TFEntities.HELMET_CRAB, HelmetCrabRenderer::new);
		EntityRenderers.register(TFEntities.KNIGHT_PHANTOM, KnightPhantomRenderer::new);
		EntityRenderers.register(TFEntities.NAGA, NagaRenderer::new);
		EntityRenderers.register(TFEntities.SWARM_SPIDER, m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		EntityRenderers.register(TFEntities.KING_SPIDER, m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		EntityRenderers.register(TFEntities.CARMINITE_BROODLING, m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		EntityRenderers.register(TFEntities.HEDGE_SPIDER, m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		EntityRenderers.register(TFEntities.REDCAP_SAPPER, RedcapSapperRenderer::new);
		EntityRenderers.register(TFEntities.MAZE_SLIME, MazeSlimeRenderer::new);
		EntityRenderers.register(TFEntities.YETI, YetiRenderer::new);
		EntityRenderers.register(TFEntities.PROTECTION_BOX, ProtectionBoxRenderer::new);
		EntityRenderers.register(TFEntities.MAGIC_PAINTING, MagicPaintingRenderer::new);
		EntityRenderers.register(TFEntities.ALPHA_YETI, AlphaYetiRenderer::new);
		EntityRenderers.register(TFEntities.WINTER_WOLF, WinterWolfRenderer::new);
		EntityRenderers.register(TFEntities.SNOW_GUARDIAN, SnowGuardianRenderer::new);
		EntityRenderers.register(TFEntities.STABLE_ICE_CORE, StableIceCoreRenderer::new);
		EntityRenderers.register(TFEntities.UNSTABLE_ICE_CORE, UnstableIceCoreRenderer::new);
		EntityRenderers.register(TFEntities.SNOW_QUEEN, SnowQueenRenderer::new);
		EntityRenderers.register(TFEntities.TROLL, TrollRenderer::new);
		EntityRenderers.register(TFEntities.GIANT_MINER, TFGiantRenderer::new);
		EntityRenderers.register(TFEntities.ARMORED_GIANT, TFGiantRenderer::new);
		EntityRenderers.register(TFEntities.ICE_CRYSTAL, IceCrystalRenderer::new);
		EntityRenderers.register(TFEntities.CHAIN_BLOCK, BlockChainRenderer::new);
		EntityRenderers.register(TFEntities.CUBE_OF_ANNIHILATION, CubeOfAnnihilationRenderer::new);
		EntityRenderers.register(TFEntities.HARBINGER_CUBE, HarbingerCubeRenderer::new);
		EntityRenderers.register(TFEntities.ADHERENT, AdherentRenderer::new);
		EntityRenderers.register(TFEntities.ROVING_CUBE, RovingCubeRenderer::new);
		EntityRenderers.register(TFEntities.RISING_ZOMBIE, RisingZombieRenderer::new);
		EntityRenderers.register(TFEntities.PLATEAU_BOSS, NoopRenderer::new);
		EntityRenderers.register(TFEntities.NATURE_BOLT, ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.LICH_BOLT, c -> new CustomProjectileTextureRenderer(c, TFMain.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRenderers.register(TFEntities.WAND_BOLT, c -> new CustomProjectileTextureRenderer(c, TFMain.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRenderers.register(TFEntities.LICH_BOMB, c -> new CustomProjectileTextureRenderer(c, Identifier.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		EntityRenderers.register(TFEntities.TOME_BOLT, ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.HYDRA_MORTAR, HydraMortarRenderer::new);
		EntityRenderers.register(TFEntities.SLIME_BLOB, ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.MOONWORM_SHOT, MoonwormShotRenderer::new);
		EntityRenderers.register(TFEntities.CHARM_EFFECT, ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_WEP, ThrownWepRenderer::new);
		EntityRenderers.register(TFEntities.FALLING_ICE, FallingIceRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_ICE, ThrownIceRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_BLOCK, ThrownBlockRenderer::new);
		EntityRenderers.register(TFEntities.ICE_SNOWBALL, ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.SLIDER, SlideBlockRenderer::new);
		EntityRenderers.register(TFEntities.SEEKER_ARROW, DefaultArrowRenderer::new);
		EntityRenderers.register(TFEntities.ICE_ARROW, DefaultArrowRenderer::new);
	}

	private static void registerBlockEntityRenderers() {
		BlockEntityRenderers.register(TFBlockEntities.FIREFLY, FireflyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CICADA, CicadaRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MOONWORM, MoonwormRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TROPHY, TrophyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_CHEST, TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_TRAPPED_CHEST, TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CHEST, SkullChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.KEEPSAKE_CASKET, KeepsakeCasketRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CANDLE, SkullCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.REACTOR_DEBRIS, ReactorDebrisRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.RED_THREAD, RedThreadRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CANDELABRA, CandelabraRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.JAR, JarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MASON_JAR, JarRenderer.MasonJarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.OMINOUS_CANDLE, OminousCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SINISTER_SPAWNER, SinisterSpawnerRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.BRAZIER, BrazierRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.DRYING_RACK, DryingRackRenderer::new);
	}

	private static void registerLayerDefinitions() {
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), false), 64, 32)); // TODO: reduce to 0.25F (+ dx?) without z-fighting in the player's inventory view
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), true), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.25F));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, BighornModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_BABY, () -> BighornModel.create().apply(BighornModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, BoarModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR_BABY, () -> BoarModel.create().apply(BoarModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, DeerModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER_BABY, () -> DeerModel.create().apply(DeerModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, HydraModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, HydraNeckModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, KoboldModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, MinoshroomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, MinotaurModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN_BABY, () -> PenguinModel.create().apply(PenguinModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, QuestRamModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, RavenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, RedcapModel::create);
		ArmorModelSet<LayerDefinition> redcapArmor = RedcapModel.createArmorLayerSet();
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.head(), redcapArmor::head);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.chest(), redcapArmor::chest);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.legs(), redcapArmor::legs);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.feet(), redcapArmor::feet);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, SquirrelModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, TinyBirdModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, TrollModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, UrGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, () -> KeepsakeCasketModel.create(true));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKULL_CHEST, () -> KeepsakeCasketModel.create(false));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BRAZIER, BrazierModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}

	private static void registerParticleFactories() {
		ParticleProviderRegistry.getInstance().register(TFParticleType.LARGE_FLAME, LargeFlameParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.LEAF_RUNE, LeafRuneParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.BOSS_TEAR, new GhastTearParticle.Factory());
		ParticleProviderRegistry.getInstance().register(TFParticleType.GHAST_TRAP, GhastTrapParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.PROTECTION, ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW, SnowParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW_WARNING, SnowWarningParticle.SimpleFactory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.EXTENDED_SNOW_WARNING, SnowWarningParticle.ExtendedFactory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.ICE_BEAM, IceBeamParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.ANNIHILATE, AnnihilateParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.PERFECT_DODGE, PerfectDodgeParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.DOUBLE_JUMP, DoubleJumpParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.FIREFLY, FireflyParticle.StationaryProvider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY, FireflyParticle.WanderingProvider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.PARTICLE_SPAWNER_FIREFLY, FireflyParticle.ParticleSpawnerProvider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.FALLEN_LEAF, LeafParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.DIM_FLAME, FlameParticle.SmallFlameProvider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.OMINOUS_FLAME, FlameParticle.SmallFlameProvider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.SORTING_PARTICLE, SortingParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.TRANSFORMATION_PARTICLE, TransformationParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.LOG_CORE_PARTICLE, LogCoreParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.CLOUD_PUFF, CloudPuffParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.DRYING_RACK, DryingRackParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.MAGIC_EFFECT, MagicEffectParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.ANGRY_LICH, AngryLichParticle.Factory::new);
		ParticleProviderRegistry.getInstance().register(TFParticleType.TWILIGHT_ORB, (FabricSpriteSet sprite) -> new CustomTextureParticle.Factory(sprite, true));
		ParticleProviderRegistry.getInstance().register(TFParticleType.SHIELD_BREAK, CustomTextureParticle.ShieldBreak::new);
	}

	private static void registerMapDecorators() {
		MapDecorationManager.addDecoration(MapDecorationTypes.PLAYER.value(), new MagicMapPlayerIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.QUEST_GROVE, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.NAGA_COURTYARD, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.LICH_TOWER, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.LABYRINTH, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.HYDRA_LAIR, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.KNIGHT_STRONGHOLD, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.DARK_TOWER, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.YETI_LAIR, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.AURORA_PALACE, new ConqueredMapIconRenderer());
		MapDecorationManager.addDecoration(TFMapDecorations.FINAL_CASTLE, new ConqueredMapIconRenderer());
	}

	private static boolean bakedMultiPartRenderers = false;
	private static void registerRenderLayers() {
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((_, renderer, registrationHelper, context) -> {
			if (!bakedMultiPartRenderers) {
				BakedMultiPartRenderers.bakeMultiPartRenderers(context);
				bakedMultiPartRenderers = true;
			}

			attachLivingRenderLayers(renderer, registrationHelper);
		});
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void attachLivingRenderLayers(LivingEntityRenderer<?, ?, ?> renderer, LivingEntityRenderLayerRegistrationCallback.RegistrationHelper registrationHelper) {
		registrationHelper.register(new ShieldLayer(renderer));
		registrationHelper.register(new IceLayer(renderer));
	}
}