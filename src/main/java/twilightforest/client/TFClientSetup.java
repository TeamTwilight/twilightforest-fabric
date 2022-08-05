package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.legacy.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.legacy.*;
import twilightforest.compat.TrinketsCompat;
import twilightforest.init.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

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
		registerEntityRenderer();
//		EntityAddedLayerCallback.EVENT.register(TFClientSetup::attachRenderLayers);
	}

	public static class FabricEvents {

		private static boolean optifineWarningShown = false;

		public static void showOptifineWarning(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
			if (optifinePresent && !optifineWarningShown && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get() && screen instanceof TitleScreen) {
				optifineWarningShown = true;
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
			}
		}

	}

	private static void registerWoodType(WoodType woodType) {
		Sheets.SIGN_MATERIALS.put(woodType, Sheets.createSignMaterial(woodType));
	}

    public static void clientSetup() {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

        RenderLayerRegistration.init();
        TFBlockEntities.registerTileEntityRenders();
        TFMenuTypes.renderScreens();

//        evt.enqueueWork(() -> {
            registerWoodType(TFBlocks.TWILIGHT_OAK);
            registerWoodType(TFBlocks.CANOPY);
            registerWoodType(TFBlocks.MANGROVE);
            registerWoodType(TFBlocks.DARKWOOD);
            registerWoodType(TFBlocks.TIMEWOOD);
            registerWoodType(TFBlocks.TRANSFORMATION);
            registerWoodType(TFBlocks.MINING);
            registerWoodType(TFBlocks.SORTING);

			if(FabricLoader.getInstance().isModLoaded("trinkets")) {
				ClientLifecycleEvents.CLIENT_STARTED.register(TrinketsCompat::registerCurioRenderers);
			}
//        });

    }

	@SuppressWarnings("unchecked")
	public static void attachRenderLayers(final Map<EntityType<?>, EntityRenderer<?>> renderers, final Map<String, EntityRenderer<? extends Player>> skins) {
		skins.forEach((id, renderer) -> {
			LivingEntityRenderer<Player, EntityModel<Player>> skin = (LivingEntityRenderer<Player, EntityModel<Player>>) skins.get(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
		renderers.values().stream().
				filter(LivingEntityRenderer.class::isInstance).map(LivingEntityRenderer.class::cast).forEach(TFClientSetup::attachRenderLayers);
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}

	@Environment(EnvType.CLIENT)
	public static void registerEntityRenderer() {
		BooleanSupplier legacy = () -> Minecraft.getInstance().getResourcePackRepository().getSelectedIds().contains("builtin/twilight_forest_legacy_resources");
		EntityRendererRegistry.register(TFEntities.BOAR.get(), m -> legacy.getAsBoolean() ? new LegacyBoarRenderer(m, new BoarLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_BOAR))) : new BoarRenderer(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		EntityRendererRegistry.register(TFEntities.BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(legacy.getAsBoolean() ? TFModelLayers.LEGACY_BIGHORN_SHEEP : TFModelLayers.BIGHORN_SHEEP)), new BighornFurLayer(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP_FUR)), 0.7F));
		EntityRendererRegistry.register(TFEntities.DEER.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new DeerLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_DEER)) : new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		EntityRendererRegistry.register(TFEntities.REDCAP.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new RedcapLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_REDCAP)) : new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		EntityRendererRegistry.register(TFEntities.SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_INNER_ARMOR)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_OUTER_ARMOR)), 0.5F, "skeletondruid.png"));
		EntityRendererRegistry.register(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		EntityRendererRegistry.register(TFEntities.HYDRA.get(), m -> legacy.getAsBoolean() ? new LegacyHydraRenderer(m, new HydraLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_HYDRA)), 4.0F) : new HydraRenderer(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		EntityRendererRegistry.register(TFEntities.LICH.get(), m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		EntityRendererRegistry.register(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		EntityRendererRegistry.register(TFEntities.LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.TINY_BIRD.get(), m -> legacy.getAsBoolean() ? new LegacyTinyBirdRenderer(m, new TinyBirdLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_TINY_BIRD)), 0.3F) :  new TinyBirdRenderer(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		EntityRendererRegistry.register(TFEntities.SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new SquirrelLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SQUIRREL)) : new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		EntityRendererRegistry.register(TFEntities.DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		EntityRendererRegistry.register(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, legacy.getAsBoolean() ? new RavenLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_RAVEN)) : new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRendererRegistry.register(TFEntities.QUEST_RAM.get(), m -> legacy.getAsBoolean() ? new LegacyQuestRamRenderer(m, new QuestRamLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_QUEST_RAM))) : new QuestRamRenderer(m, new QuestRamModel(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		EntityRendererRegistry.register(TFEntities.KOBOLD.get(), m -> legacy.getAsBoolean() ? new LegacyKoboldRenderer(m, new KoboldLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_KOBOLD)), 0.4F, "kobold.png") : new KoboldRenderer(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//EntityRendererRegistry.register(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		EntityRendererRegistry.register(TFEntities.MOSQUITO_SWARM.get(), m -> new TFGenericMobRenderer<>(m, new MosquitoSwarmModel(m.bakeLayer(TFModelLayers.MOSQUITO_SWARM)), 0.0F, "mosquitoswarm.png"));
		EntityRendererRegistry.register(TFEntities.DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		EntityRendererRegistry.register(TFEntities.MINOTAUR.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new MinotaurLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_MINOTAUR)) : new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		EntityRendererRegistry.register(TFEntities.MINOSHROOM.get(), m -> legacy.getAsBoolean() ? new LegacyMinoshroomRenderer(m, new MinoshroomLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_MINOSHROOM)), 0.625F) : new MinoshroomRenderer(m, new MinoshroomModel(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new FireBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_FIRE_BEETLE)) : new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		EntityRendererRegistry.register(TFEntities.SLIME_BEETLE.get(), m -> legacy.getAsBoolean() ? new LegacySlimeBeetleRenderer(m, new SlimeBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SLIME_BEETLE)), 0.6F) : new SlimeBeetleRenderer(m, new SlimeBeetleModel(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), 0.6F));
		EntityRendererRegistry.register(TFEntities.PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new PinchBeetleLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_PINCH_BEETLE)) : new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		EntityRendererRegistry.register(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		EntityRendererRegistry.register(TFEntities.TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		EntityRendererRegistry.register(TFEntities.UR_GHAST.get(), m -> legacy.getAsBoolean() ? new LegacyUrGhastRenderer(m, new UrGhastLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_UR_GHAST)), 8.0F, 24F) : new UrGhastRenderer(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24F));
		EntityRendererRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), m -> legacy.getAsBoolean() ? new LegacyBlockChainGoblinRenderer<>(m, new BlockChainGoblinLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_BLOCKCHAIN_GOBLIN)), 0.4F) : new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		EntityRendererRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), m -> legacy.getAsBoolean() ? new LegacyUpperGoblinKnightRenderer(m, new UpperGoblinKnightLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_UPPER_GOBLIN_KNIGHT)), 0.625F) : new UpperGoblinKnightRenderer(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		EntityRendererRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new LowerGoblinKnightLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_LOWER_GOBLIN_KNIGHT)) : new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		EntityRendererRegistry.register(TFEntities.HELMET_CRAB.get(), m -> new TFGenericMobRenderer<>(m, legacy.getAsBoolean() ? new HelmetCrabLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_HELMET_CRAB)) : new HelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		EntityRendererRegistry.register(TFEntities.KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.NAGA.get(), m -> legacy.getAsBoolean() ? new LegacyNagaRenderer<>(m, new NagaLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_NAGA)), 1.45F) : new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		EntityRendererRegistry.register(TFEntities.SWARM_SPIDER.get(), SwarmSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.KING_SPIDER.get(), KingSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.CARMINITE_BROODLING.get(), CarminiteBroodlingRenderer::new);
		EntityRendererRegistry.register(TFEntities.HEDGE_SPIDER.get(), HedgeSpiderRenderer::new);
		EntityRendererRegistry.register(TFEntities.REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new RedcapLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_REDCAP)) : new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		EntityRendererRegistry.register(TFEntities.MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		EntityRendererRegistry.register(TFEntities.YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		EntityRendererRegistry.register(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		EntityRendererRegistry.register(TFEntities.ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		EntityRendererRegistry.register(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		EntityRendererRegistry.register(TFEntities.STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.SNOW_QUEEN.get(), m -> legacy.getAsBoolean() ? new LegacySnowQueenRenderer(m, new SnowQueenLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_SNOW_QUEEN))) : new SnowQueenRenderer(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		EntityRendererRegistry.register(TFEntities.TROLL.get(), m -> new TFBipedRenderer<>(m, legacy.getAsBoolean() ? new TrollLegacyModel(m.bakeLayer(TFModelLayers.LEGACY_TROLL)) : new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		EntityRendererRegistry.register(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		EntityRendererRegistry.register(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		EntityRendererRegistry.register(TFEntities.HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		EntityRendererRegistry.register(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.RISING_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		//EntityRendererRegistry.register(TFEntities.CASTLE_GUARDIAN.get(), m -> new RenderTFCastleGuardian(m, new ModelTFCastleGuardian(), 2.0F, "finalcastle/castle_guardian.png"));
		EntityRendererRegistry.register(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		EntityRendererRegistry.register(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/items/twilight_orb.png")));
		EntityRendererRegistry.register(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/items/twilight_orb.png")));
		EntityRendererRegistry.register(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.CICADA_SHOT.get(), CicadaShotRenderer::new);
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

		if(FabricLoader.getInstance().isModLoaded("undergarden")) {
//			UndergardenCompat.registerSlingshotRenders();
		}
	}
}
