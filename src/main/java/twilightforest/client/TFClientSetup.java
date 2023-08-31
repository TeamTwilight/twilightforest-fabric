package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.newmodels.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.newmodels.*;
import twilightforest.compat.trinkets.TrinketsCompat;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFMenuTypes;
import twilightforest.init.TFParticleType;
import twilightforest.util.TFWoodTypes;

import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.BooleanSupplier;

import static twilightforest.init.TFEntities.*;

public class TFClientSetup implements ClientModInitializer {

	public static boolean optifinePresent = false;

	@Override
	public void onInitializeClient() {
		TFShaders.init();

		TFLayerDefinitions.registerLayers();
		ColorHandler.registerBlockColors();
		ColorHandler.registerItemColors();
		TFClientEvents.init();
		ScreenEvents.AFTER_INIT.register(FabricEvents::showOptifineWarning);
		FogHandler.init();
		TFParticleType.registerFactories();
		ClientTickEvents.END_CLIENT_TICK.register(LockedBiomeListener::clientTick);
		clientSetup();
		registerEntityRenderers();
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(TFClientSetup::attachRenderLayers);
		BuiltinItemRendererRegistry.INSTANCE.register(TFItems.KNIGHTMETAL_SHIELD.get(), ISTER.INSTANCE.get());
		addJappaPackListener();
	}

	public static class FabricEvents {

		private static boolean firstTitleScreenShown = false;

		public static void showOptifineWarning(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
			if (firstTitleScreenShown || !(screen instanceof TitleScreen)) return;

			// Registering this resource listener earlier than the main screen will cause a crash
			// Yes, crashing happens if registered to RegisterClientReloadListenersEvent
			if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager resourceManager) {
				resourceManager.registerReloadListener(ISTER.INSTANCE.get());
				TwilightForestMod.LOGGER.debug("Registered ISTER listener");
			}

			if (optifinePresent && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get()) {
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
			}

			firstTitleScreenShown = true;
		}
	}

	private static void registerWoodType(WoodType woodType) {
		Sheets.SIGN_MATERIALS.put(woodType, Sheets.createSignMaterial(woodType));
	}

    public static void clientSetup() {
		optifinePresent = FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("indium");

        TFBlockEntities.registerTileEntityRenders();
        TFMenuTypes.renderScreens();

//        evt.enqueueWork(() -> {
            registerWoodType(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE);
			registerWoodType(TFWoodTypes.CANOPY_WOOD_TYPE);
			registerWoodType(TFWoodTypes.MANGROVE_WOOD_TYPE);
			registerWoodType(TFWoodTypes.DARK_WOOD_TYPE);
			registerWoodType(TFWoodTypes.TIME_WOOD_TYPE);
			registerWoodType(TFWoodTypes.TRANSFORMATION_WOOD_TYPE);
			registerWoodType(TFWoodTypes.MINING_WOOD_TYPE);
			registerWoodType(TFWoodTypes.SORTING_WOOD_TYPE);
//        });

		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			ClientLifecycleEvents.CLIENT_STARTED.register(TrinketsCompat::registerCurioRenderers);
		}
    }

	public static void addJappaPackListener() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(JappaPackReloadListener.INSTANCE);
		JappaPackReloadListener.clientSetup();
		// TODO PORT 1.20 add MagicPaintingTextureManager
	}

	public static void registerEntityRenderers() {
		BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
		EntityRendererRegistry.register(TFEntities.BOAT.get(), m -> new TwilightBoatRenderer(m, false));
		EntityRendererRegistry.register(TFEntities.CHEST_BOAT.get(), m -> new TwilightBoatRenderer(m, true));
		EntityRendererRegistry.register(TFEntities.BOAR.get(), m -> !jappa.getAsBoolean() ? new BoarRenderer(m, new BoarModel<>(m.bakeLayer(TFModelLayers.NEW_BOAR))) : new NewBoarRenderer(m, new NewBoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		EntityRendererRegistry.register(TFEntities.BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new NewBighornModel<>(m.bakeLayer(!jappa.getAsBoolean() ? TFModelLayers.NEW_BIGHORN_SHEEP : TFModelLayers.BIGHORN_SHEEP)), new BighornFurLayer(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP_FUR)), 0.7F));
		EntityRendererRegistry.register(TFEntities.DEER.get(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new DeerModel(m.bakeLayer(TFModelLayers.NEW_DEER)) : new NewDeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		EntityRendererRegistry.register(TFEntities.REDCAP.get(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new RedcapModel<>(m.bakeLayer(TFModelLayers.NEW_REDCAP)) : new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		EntityRendererRegistry.register(TFEntities.SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F, "skeletondruid.png"));
		EntityRendererRegistry.register(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		EntityRendererRegistry.register(TFEntities.HYDRA.get(), m -> !jappa.getAsBoolean() ? new HydraRenderer(m, new HydraModel(m.bakeLayer(TFModelLayers.NEW_HYDRA)), 4.0F) : new NewHydraRenderer(m, new NewHydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		EntityRendererRegistry.register(TFEntities.LICH.get(), m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		EntityRendererRegistry.register(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		EntityRendererRegistry.register(TFEntities.LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.TINY_BIRD.get(), m -> !jappa.getAsBoolean() ? new TinyBirdRenderer(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.NEW_TINY_BIRD)), 0.3F) : new NewTinyBirdRenderer(m, new NewTinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		EntityRendererRegistry.register(TFEntities.SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new SquirrelModel(m.bakeLayer(TFModelLayers.NEW_SQUIRREL)) : new NewSquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		EntityRendererRegistry.register(TFEntities.DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		EntityRendererRegistry.register(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, !jappa.getAsBoolean() ? new RavenModel(m.bakeLayer(TFModelLayers.NEW_RAVEN)) : new NewRavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRendererRegistry.register(TFEntities.QUEST_RAM.get(), m -> !jappa.getAsBoolean() ? new QuestRamRenderer(m, new QuestRamModel(m.bakeLayer(TFModelLayers.NEW_QUEST_RAM))) : new NewQuestRamRenderer(m, new NewQuestRamModel(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		EntityRendererRegistry.register(TFEntities.KOBOLD.get(), m -> !jappa.getAsBoolean() ? new KoboldRenderer(m, new KoboldModel(m.bakeLayer(TFModelLayers.NEW_KOBOLD)), 0.4F, "kobold.png") : new NewKoboldRenderer(m, new NewKoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//EntityRendererRegistry.register(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		EntityRendererRegistry.register(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		EntityRendererRegistry.register(TFEntities.DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		EntityRendererRegistry.register(TFEntities.MINOTAUR.get(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new MinotaurModel(m.bakeLayer(TFModelLayers.NEW_MINOTAUR)) : new NewMinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		EntityRendererRegistry.register(TFEntities.MINOSHROOM.get(), m -> !jappa.getAsBoolean() ? new MinoshroomRenderer(m, new MinoshroomModel(m.bakeLayer(TFModelLayers.NEW_MINOSHROOM)), 0.625F) : new NewMinoshroomRenderer(m, new NewMinoshroomModel(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new FireBeetleModel(m.bakeLayer(TFModelLayers.NEW_FIRE_BEETLE)) : new NewFireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		EntityRendererRegistry.register(TFEntities.SLIME_BEETLE.get(), m -> !jappa.getAsBoolean() ? new SlimeBeetleRenderer(m, new SlimeBeetleModel(m.bakeLayer(TFModelLayers.NEW_SLIME_BEETLE)), 0.6F) : new NewSlimeBeetleRenderer(m, new NewSlimeBeetleModel(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), 0.6F));
		EntityRendererRegistry.register(TFEntities.PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new PinchBeetleModel(m.bakeLayer(TFModelLayers.NEW_PINCH_BEETLE)) : new NewPinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		EntityRendererRegistry.register(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		EntityRendererRegistry.register(TFEntities.TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		EntityRendererRegistry.register(TFEntities.UR_GHAST.get(), m -> !jappa.getAsBoolean() ? new UrGhastRenderer(m, new UrGhastModel(m.bakeLayer(TFModelLayers.NEW_UR_GHAST)), 8.0F, 24F) : new NewUrGhastRenderer(m, new NewUrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24F));
		EntityRendererRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), m -> !jappa.getAsBoolean() ? new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.NEW_BLOCKCHAIN_GOBLIN)), 0.4F) : new NewBlockChainGoblinRenderer<>(m, new NewBlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		EntityRendererRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), m -> !jappa.getAsBoolean() ? new UpperGoblinKnightRenderer(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.NEW_UPPER_GOBLIN_KNIGHT)), 0.625F) : new NewUpperGoblinKnightRenderer(m, new NewUpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		EntityRendererRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.NEW_LOWER_GOBLIN_KNIGHT)) : new NewLowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		EntityRendererRegistry.register(TFEntities.HELMET_CRAB.get(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new HelmetCrabModel(m.bakeLayer(TFModelLayers.NEW_HELMET_CRAB)) : new NewHelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		EntityRendererRegistry.register(TFEntities.KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.NAGA.get(), m -> !jappa.getAsBoolean() ? new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NEW_NAGA)), 1.45F) : new NewNagaRenderer<>(m, new NewNagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		EntityRendererRegistry.register(TFEntities.SWARM_SPIDER.get(), SwarmSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.KING_SPIDER.get(), KingSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.CARMINITE_BROODLING.get(), CarminiteBroodlingRenderer::new);
		EntityRendererRegistry.register(TFEntities.HEDGE_SPIDER.get(), HedgeSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new RedcapModel<>(m.bakeLayer(TFModelLayers.NEW_REDCAP)) : new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		EntityRendererRegistry.register(TFEntities.MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		EntityRendererRegistry.register(TFEntities.YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		EntityRendererRegistry.register(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		EntityRendererRegistry.register(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		EntityRendererRegistry.register(TFEntities.ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		EntityRendererRegistry.register(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		EntityRendererRegistry.register(TFEntities.STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.SNOW_QUEEN.get(), m -> !jappa.getAsBoolean() ? new SnowQueenRenderer(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.NEW_SNOW_QUEEN))) : new NewSnowQueenRenderer(m, new NewSnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		EntityRendererRegistry.register(TFEntities.TROLL.get(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new TrollModel(m.bakeLayer(TFModelLayers.NEW_TROLL)) : new NewTrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		EntityRendererRegistry.register(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		EntityRendererRegistry.register(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		EntityRendererRegistry.register(TFEntities.HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		EntityRendererRegistry.register(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.RISING_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		EntityRendererRegistry.register(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		EntityRendererRegistry.register(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		EntityRendererRegistry.register(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.LICH_BOMB.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		EntityRendererRegistry.register(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);
	}

	@Environment(EnvType.CLIENT)
	@SuppressWarnings("deprecation")
	public static class BakedMultiPartRenderers {

		private static final Map<ResourceLocation, LazyLoadedValue<EntityRenderer<?>>> renderers = new HashMap<>();

		public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
			BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
			renderers.put(TFPart.RENDERER, new LazyLoadedValue<>(() -> new NoopRenderer<>(context)));
			renderers.put(HydraHead.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraHeadRenderer(context) : new NewHydraHeadRenderer(context)));
			renderers.put(HydraNeck.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraNeckRenderer(context) : new NewHydraNeckRenderer(context)));
			renderers.put(SnowQueenIceShield.RENDERER, new LazyLoadedValue<>(() -> new SnowQueenIceShieldLayer<>(context)));
			renderers.put(NagaSegment.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new NagaSegmentRenderer<>(context) : new NewNagaSegmentRenderer<>(context)));
		}

		public static EntityRenderer<?> lookup(ResourceLocation location) {
			return renderers.get(location).get();
		}
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> renderer, RegistrationHelper registrationHelper, EntityRendererProvider.Context context) {
		registrationHelper.register(new ShieldLayer<>(renderer));
		registrationHelper.register(new IceLayer<>(renderer));
	}
}
