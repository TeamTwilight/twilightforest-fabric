package twilightforest.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.TFEntities;

import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;

public class TFEntityRenderers {
    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderer() {
        EntityRendererRegistry.register(TFEntities.wild_boar, m -> new BoarRenderer(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
        EntityRendererRegistry.register(TFEntities.bighorn_sheep, m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP)), new BighornFurLayer(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP_FUR)), 0.7F));
        EntityRendererRegistry.register(TFEntities.deer, m -> new TFGenericMobRenderer<>(m, new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
        EntityRendererRegistry.register(TFEntities.redcap, m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
        EntityRendererRegistry.register(TFEntities.skeleton_druid, m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_INNER_ARMOR)), new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID_OUTER_ARMOR)), 0.5F, "skeletondruid.png"));
        EntityRendererRegistry.register(TFEntities.hostile_wolf, WolfRenderer::new);
        EntityRendererRegistry.register(TFEntities.wraith, m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
        EntityRendererRegistry.register(TFEntities.hydra, m -> new HydraRenderer(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
        EntityRendererRegistry.register(TFEntities.lich, m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
        EntityRendererRegistry.register(TFEntities.penguin, m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
        EntityRendererRegistry.register(TFEntities.lich_minion, m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
        EntityRendererRegistry.register(TFEntities.loyal_zombie, m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
        EntityRendererRegistry.register(TFEntities.tiny_bird, m -> new TinyBirdRenderer(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
        EntityRendererRegistry.register(TFEntities.squirrel, m -> new TFGenericMobRenderer<>(m, new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
        EntityRendererRegistry.register(TFEntities.bunny, m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
        EntityRendererRegistry.register(TFEntities.raven, m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
        EntityRendererRegistry.register(TFEntities.quest_ram, m -> new QuestRamRenderer(m, new QuestRamModel(m.bakeLayer(TFModelLayers.QUEST_RAM))));
        EntityRendererRegistry.register(TFEntities.kobold, m -> new KoboldRenderer(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
        //EntityRendererRegistry.register(TFEntities.boggard, m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
        EntityRendererRegistry.register(TFEntities.mosquito_swarm, m -> new TFGenericMobRenderer<>(m, new MosquitoSwarmModel(m.bakeLayer(TFModelLayers.MOSQUITO_SWARM)), 0.0F, "mosquitoswarm.png"));
        EntityRendererRegistry.register(TFEntities.death_tome, m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
        EntityRendererRegistry.register(TFEntities.minotaur, m -> new TFBipedRenderer<>(m, new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
        EntityRendererRegistry.register(TFEntities.minoshroom, m -> new MinoshroomRenderer(m, new MinoshroomModel(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
        EntityRendererRegistry.register(TFEntities.fire_beetle, m -> new TFGenericMobRenderer<>(m, new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
        EntityRendererRegistry.register(TFEntities.slime_beetle, m -> new SlimeBeetleRenderer(m, new SlimeBeetleModel(m.bakeLayer(TFModelLayers.SLIME_BEETLE), false), 0.6F));
        EntityRendererRegistry.register(TFEntities.pinch_beetle, m -> new TFGenericMobRenderer<>(m, new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
        EntityRendererRegistry.register(TFEntities.mist_wolf, MistWolfRenderer::new);
        EntityRendererRegistry.register(TFEntities.mini_ghast, m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
        EntityRendererRegistry.register(TFEntities.tower_golem, m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
        EntityRendererRegistry.register(TFEntities.tower_termite, m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
        EntityRendererRegistry.register(TFEntities.tower_ghast, m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
        EntityRendererRegistry.register(TFEntities.ur_ghast, m -> new UrGhastRenderer(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24F));
        EntityRendererRegistry.register(TFEntities.blockchain_goblin, m -> new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
        EntityRendererRegistry.register(TFEntities.goblin_knight_upper, m -> new UpperGoblinKnightRenderer(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
        EntityRendererRegistry.register(TFEntities.goblin_knight_lower, m -> new TFBipedRenderer<>(m, new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
        EntityRendererRegistry.register(TFEntities.helmet_crab, m -> new TFGenericMobRenderer<>(m, new HelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
        EntityRendererRegistry.register(TFEntities.knight_phantom, m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
        EntityRendererRegistry.register(TFEntities.naga, m -> new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
        EntityRendererRegistry.register(TFEntities.swarm_spider, SwarmSpiderRenderer::new);
        EntityRendererRegistry.register(TFEntities.king_spider, KingSpiderRenderer::new);
        EntityRendererRegistry.register(TFEntities.tower_broodling, CarminiteBroodlingRenderer::new);
        EntityRendererRegistry.register(TFEntities.hedge_spider, HedgeSpiderRenderer::new);
        EntityRendererRegistry.register(TFEntities.redcap_sapper, m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
        EntityRendererRegistry.register(TFEntities.maze_slime, m -> new MazeSlimeRenderer(m, 0.625F));
        EntityRendererRegistry.register(TFEntities.yeti, m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
        EntityRendererRegistry.register(TFEntities.protection_box, ProtectionBoxRenderer::new);
        EntityRendererRegistry.register(TFEntities.yeti_alpha, m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
        EntityRendererRegistry.register(TFEntities.winter_wolf, WinterWolfRenderer::new);
        EntityRendererRegistry.register(TFEntities.snow_guardian, m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
        EntityRendererRegistry.register(TFEntities.stable_ice_core, m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
        EntityRendererRegistry.register(TFEntities.unstable_ice_core, m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
        EntityRendererRegistry.register(TFEntities.snow_queen, m -> new SnowQueenRenderer(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
        EntityRendererRegistry.register(TFEntities.troll, m -> new TFBipedRenderer<>(m, new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
        EntityRendererRegistry.register(TFEntities.giant_miner, TFGiantRenderer::new);
        EntityRendererRegistry.register(TFEntities.armored_giant, TFGiantRenderer::new);
        EntityRendererRegistry.register(TFEntities.ice_crystal, IceCrystalRenderer::new);
        EntityRendererRegistry.register(TFEntities.chain_block, BlockChainRenderer::new);
        EntityRendererRegistry.register(TFEntities.cube_of_annihilation, CubeOfAnnihilationRenderer::new);
        EntityRendererRegistry.register(TFEntities.harbinger_cube, HarbingerCubeRenderer::new);
        EntityRendererRegistry.register(TFEntities.adherent, AdherentRenderer::new);
        EntityRendererRegistry.register(TFEntities.roving_cube, RovingCubeRenderer::new);
        EntityRendererRegistry.register(TFEntities.rising_zombie, m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
        //EntityRendererRegistry.register(TFEntities.castle_guardian, m -> new RenderTFCastleGuardian(m, new ModelTFCastleGuardian(), 2.0F, "finalcastle/castle_guardian.png"));
        EntityRendererRegistry.register(TFEntities.plateau_boss, NoopRenderer::new);

        // projectiles
        EntityRendererRegistry.register(TFEntities.nature_bolt, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.lich_bolt, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.wand_bolt, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.tome_bolt, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.hydra_mortar, HydraMortarRenderer::new);
        EntityRendererRegistry.register(TFEntities.slime_blob, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.cicada_shot, CicadaShotRenderer::new);
        EntityRendererRegistry.register(TFEntities.moonworm_shot, MoonwormShotRenderer::new);
        EntityRendererRegistry.register(TFEntities.charm_effect, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.lich_bomb, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.thrown_wep, ThrownWepRenderer::new);
        EntityRendererRegistry.register(TFEntities.falling_ice, FallingIceRenderer::new);
        EntityRendererRegistry.register(TFEntities.thrown_ice, ThrownIceRenderer::new);
        EntityRendererRegistry.register(TFEntities.ice_snowball, ThrownItemRenderer::new);
        EntityRendererRegistry.register(TFEntities.slider, SlideBlockRenderer::new);
        EntityRendererRegistry.register(TFEntities.seeker_arrow, DefaultArrowRenderer::new);
        EntityRendererRegistry.register(TFEntities.ice_arrow, DefaultArrowRenderer::new);
    }
}
