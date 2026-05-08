package com.codex.twilight.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.armor.ArcticArmorModel;
import twilightforest.client.model.armor.FieryArmorModel;
import twilightforest.client.model.armor.KnightmetalArmorModel;
import twilightforest.client.model.armor.PhantomArmorModel;
import twilightforest.client.model.armor.TravellersGearModels;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.client.model.armor.YetiArmorModel;
import twilightforest.client.model.entity.AlphaYetiModel;
import twilightforest.client.model.entity.BighornModel;
import twilightforest.client.model.entity.BoarModel;
import twilightforest.client.model.entity.BlockChainGoblinModel;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.client.model.entity.ChainModel;
import twilightforest.client.model.entity.AdherentModel;
import twilightforest.client.model.entity.DeerModel;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.FireBeetleModel;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.client.model.entity.CubeOfAnnihilationModel;
import twilightforest.client.model.entity.CarminiteGolemModel;
import twilightforest.client.model.entity.HarbingerCubeModel;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.model.entity.HydraModel;
import twilightforest.client.model.entity.HydraMortarModel;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.model.entity.IceCrystalModel;
import twilightforest.client.model.entity.KnightmetalShieldModel;
import twilightforest.client.model.entity.KoboldModel;
import twilightforest.client.model.entity.KnightPhantomModel;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.model.entity.LichMinionModel;
import twilightforest.client.model.entity.LoyalZombieModel;
import twilightforest.client.model.entity.LowerGoblinKnightModel;
import twilightforest.client.model.entity.MosquitoSwarmModel;
import twilightforest.client.model.entity.MinotaurModel;
import twilightforest.client.model.entity.MinoshroomModel;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.model.entity.PenguinModel;
import twilightforest.client.model.entity.PinchBeetleModel;
import twilightforest.client.model.entity.ProtectionBoxModel;
import twilightforest.client.model.entity.QuestRamModel;
import twilightforest.client.model.entity.RavenModel;
import twilightforest.client.model.entity.RedThreadModel;
import twilightforest.client.model.entity.RedcapModel;
import twilightforest.client.model.entity.RisingZombieModel;
import twilightforest.client.model.entity.SkeletonDruidModel;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.client.model.entity.SnowQueenModel;
import twilightforest.client.model.entity.SpikeBlockModel;
import twilightforest.client.model.entity.StableIceCoreModel;
import twilightforest.client.model.entity.SquirrelModel;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.client.model.entity.TrollModel;
import twilightforest.client.model.entity.UrGhastModel;
import twilightforest.client.model.entity.UnstableIceCoreModel;
import twilightforest.client.model.entity.UpperGoblinKnightModel;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.client.model.entity.YetiModel;
import twilightforest.client.renderer.block.SkullChestRenderer;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;

/**
 * F2.1b — central registry of {@link ModelLayerLocation}s used by codex-twilight
 * client-side renderers. Mirrors upstream Twilight Forest's model layer table but
 * registered through Fabric's {@link EntityModelLayerRegistry} instead of the
 * NeoForge event bus.
 *
 * <p>Each migrated entity below owns its upstream Twilight Forest model layer.
 */
public final class CodexModelLayers {

    public static final ModelLayerLocation KOBOLD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("codex_twilight", "kobold"),
            "main");

    public static final ModelLayerLocation BOAR = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "boar"),
            "main");

    public static final ModelLayerLocation BUNNY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "bunny"),
            "main");

    public static final ModelLayerLocation DEER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "deer"),
            "main");

    public static final ModelLayerLocation PENGUIN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "penguin"),
            "main");

    public static final ModelLayerLocation SQUIRREL = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "squirrel"),
            "main");

    public static final ModelLayerLocation SKELETON_DRUID = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "skeleton_druid"),
            "main");

    public static final ModelLayerLocation REDCAP = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "redcap"),
            "main");

    public static final ModelLayerLocation REDCAP_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "redcap_armor_inner"),
            "main");

    public static final ModelLayerLocation REDCAP_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "redcap_armor_outer"),
            "main");

    public static final ModelLayerLocation DEATH_TOME = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "death_tome"),
            "main");

    public static final ModelLayerLocation FIRE_BEETLE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "fire_beetle"),
            "main");

    public static final ModelLayerLocation PINCH_BEETLE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "pinch_beetle"),
            "main");

    public static final ModelLayerLocation HELMET_CRAB = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "helmet_crab"),
            "main");

    public static final ModelLayerLocation SLIME_BEETLE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "slime_beetle"),
            "main");

    public static final ModelLayerLocation SLIME_BEETLE_TAIL = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "slime_beetle_tail"),
            "main");

    public static final ModelLayerLocation MAZE_SLIME = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "maze_slime"),
            "main");

    public static final ModelLayerLocation MAZE_SLIME_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "maze_slime"),
            "outer");

    public static final ModelLayerLocation HARBINGER_CUBE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "harbinger_cube"),
            "main");

    public static final ModelLayerLocation CUBE_OF_ANNIHILATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "cube_of_annihilation"),
            "main");

    public static final ModelLayerLocation HOSTILE_WOLF = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hostile_wolf"),
            "main");

    public static final ModelLayerLocation HEDGE_SPIDER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hedge_spider"),
            "main");

    public static final ModelLayerLocation KING_SPIDER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "king_spider"),
            "main");

    public static final ModelLayerLocation SWARM_SPIDER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "swarm_spider"),
            "main");

    public static final ModelLayerLocation CARMINITE_BROODLING = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "carminite_broodling"),
            "main");

    public static final ModelLayerLocation ICE_CRYSTAL = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "ice_crystal"),
            "main");

    public static final ModelLayerLocation STABLE_ICE_CORE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "stable_ice_core"),
            "main");

    public static final ModelLayerLocation UNSTABLE_ICE_CORE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "unstable_ice_core"),
            "main");

    public static final ModelLayerLocation NOOP = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "noop"),
            "main");

    public static final ModelLayerLocation PROTECTION_BOX = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "protection_box"),
            "main");

    public static final ModelLayerLocation MOSQUITO_SWARM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "mosquito_swarm"),
            "main");

    public static final ModelLayerLocation TOWERWOOD_BORER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "towerwood_borer"),
            "main");

    public static final ModelLayerLocation WRAITH = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "wraith"),
            "main");

    public static final ModelLayerLocation CARMINITE_GHASTGUARD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "carminite_ghastguard"),
            "main");

    public static final ModelLayerLocation CARMINITE_GHASTLING = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "carminite_ghastling"),
            "main");

    public static final ModelLayerLocation CARMINITE_GOLEM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "carminite_golem"),
            "main");

    public static final ModelLayerLocation MINOTAUR = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "minotaur"),
            "main");

    public static final ModelLayerLocation MINOSHROOM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "minoshroom"),
            "main");

    public static final ModelLayerLocation BLOCKCHAIN_GOBLIN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "blockchain_goblin"),
            "main");

    public static final ModelLayerLocation CHAIN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "chain"),
            "main");

    public static final ModelLayerLocation CHAIN_BLOCK = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "chain_block"),
            "main");

    public static final ModelLayerLocation LOYAL_ZOMBIE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "loyal_zombie"),
            "main");

    public static final ModelLayerLocation RISING_ZOMBIE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "rising_zombie"),
            "main");

    public static final ModelLayerLocation ADHERENT = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "adherent"),
            "main");

    public static final ModelLayerLocation YETI = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "yeti"),
            "main");

    public static final ModelLayerLocation ALPHA_YETI = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "alpha_yeti"),
            "main");

    public static final ModelLayerLocation KNIGHT_PHANTOM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "knight_phantom"),
            "main");

    public static final ModelLayerLocation SNOW_QUEEN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "snow_queen"),
            "main");

    public static final ModelLayerLocation LICH = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "lich"),
            "main");

    public static final ModelLayerLocation NAGA = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "naga"),
            "main");

    public static final ModelLayerLocation NAGA_BODY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "naga_body"),
            "main");

    public static final ModelLayerLocation HYDRA = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hydra"),
            "main");

    public static final ModelLayerLocation HYDRA_HEAD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hydra_head"),
            "main");

    public static final ModelLayerLocation HYDRA_NECK = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hydra_neck"),
            "main");

    public static final ModelLayerLocation HYDRA_MORTAR = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hydra_mortar"),
            "main");

    public static final ModelLayerLocation MOONWORM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "moonworm"),
            "main");

    public static final ModelLayerLocation CICADA = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "cicada"),
            "main");

    public static final ModelLayerLocation FIREFLY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "firefly"),
            "main");

    public static final ModelLayerLocation UR_GHAST = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "ur_ghast"),
            "main");

    public static final ModelLayerLocation TROLL = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "troll"),
            "main");

    public static final ModelLayerLocation LOWER_GOBLIN_KNIGHT = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "lower_goblin_knight"),
            "main");

    public static final ModelLayerLocation UPPER_GOBLIN_KNIGHT = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "upper_goblin_knight"),
            "main");

    public static final ModelLayerLocation LICH_MINION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "lich_minion"),
            "main");

    public static final ModelLayerLocation BIGHORN_SHEEP = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "bighorn_sheep"),
            "main");

    public static final ModelLayerLocation QUEST_RAM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "quest_ram"),
            "main");

    public static final ModelLayerLocation RAVEN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "raven"),
            "main");

    public static final ModelLayerLocation TINY_BIRD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "tiny_bird"),
            "main");

    public static final ModelLayerLocation BRAZIER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "brazier"),
            "main");

    public static final ModelLayerLocation NAGA_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "naga_trophy"),
            "main");

    public static final ModelLayerLocation LICH_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "lich_trophy"),
            "main");

    public static final ModelLayerLocation MINOSHROOM_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "minoshroom_trophy"),
            "main");

    public static final ModelLayerLocation HYDRA_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "hydra_trophy"),
            "main");

    public static final ModelLayerLocation KNIGHT_PHANTOM_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "knight_phantom_trophy"),
            "main");

    public static final ModelLayerLocation UR_GHAST_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "ur_ghast_trophy"),
            "main");

    public static final ModelLayerLocation ALPHA_YETI_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "alpha_yeti_trophy"),
            "main");

    public static final ModelLayerLocation SNOW_QUEEN_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "snow_queen_trophy"),
            "main");

    public static final ModelLayerLocation QUEST_RAM_TROPHY = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "quest_ram_trophy"),
            "main");

    public static final ModelLayerLocation SKULL_CHEST = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "skull_chest"),
            "main");

    public static final ModelLayerLocation KEEPSAKE_CASKET = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "keepsake_casket"),
            "main");

    public static final ModelLayerLocation RED_THREAD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "red_thread"),
            "main");

    public static final ModelLayerLocation KNIGHTMETAL_SHIELD = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "knightmetal_shield"),
            "main");

    public static final ModelLayerLocation CHARM_OF_LIFE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "charm_of_life"),
            "main");

    public static final ModelLayerLocation ARCTIC_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "arctic_armor"),
            "inner");

    public static final ModelLayerLocation ARCTIC_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "arctic_armor"),
            "outer");

    public static final ModelLayerLocation FIERY_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "fiery_armor"),
            "inner");

    public static final ModelLayerLocation FIERY_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "fiery_armor"),
            "outer");

    public static final ModelLayerLocation KNIGHTMETAL_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "knightmetal_armor"),
            "inner");

    public static final ModelLayerLocation KNIGHTMETAL_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "knightmetal_armor"),
            "outer");

    public static final ModelLayerLocation PHANTOM_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "phantom_armor"),
            "inner");

    public static final ModelLayerLocation PHANTOM_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "phantom_armor"),
            "outer");

    public static final ModelLayerLocation YETI_ARMOR_INNER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "yeti_armor"),
            "inner");

    public static final ModelLayerLocation YETI_ARMOR_OUTER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "yeti_armor"),
            "outer");

    public static final ModelLayerLocation TRAVELLERS_ARMOR_HELMET = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "travellers_armor"),
            "helmet");

    public static final ModelLayerLocation TRAVELLERS_ARMOR_CHEST_GLOVES = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "travellers_armor"),
            "chest_gloves");

    public static final ModelLayerLocation TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "travellers_armor"),
            "chest_gloves_slim");

    public static final ModelLayerLocation TRAVELLERS_ARMOR_LEGGINGS = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "travellers_armor"),
            "leggings");

    public static final ModelLayerLocation TRAVELLERS_ARMOR_BOOTS = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("twilightforest", "travellers_armor"),
            "boots");

    private CodexModelLayers() {
    }

    public static void bootstrap() {
        EntityModelLayerRegistry.registerModelLayer(KOBOLD, KoboldModel::checkForPack);
        EntityModelLayerRegistry.registerModelLayer(BOAR, BoarModel::checkForPack);
        EntityModelLayerRegistry.registerModelLayer(BUNNY, BunnyModel::create);
        EntityModelLayerRegistry.registerModelLayer(DEER, DeerModel::checkForPack);
        EntityModelLayerRegistry.registerModelLayer(PENGUIN, PenguinModel::create);
        EntityModelLayerRegistry.registerModelLayer(SQUIRREL, SquirrelModel::checkForPack);
        EntityModelLayerRegistry.registerModelLayer(SKELETON_DRUID, SkeletonDruidModel::create);
        EntityModelLayerRegistry.registerModelLayer(REDCAP, RedcapModel::checkForPack);
        EntityModelLayerRegistry.registerModelLayer(REDCAP_ARMOR_INNER, () ->
            LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(REDCAP_ARMOR_OUTER, () ->
            LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
        EntityModelLayerRegistry.registerModelLayer(DEATH_TOME, DeathTomeModel::create);
                EntityModelLayerRegistry.registerModelLayer(FIRE_BEETLE, FireBeetleModel::checkForPack);
                EntityModelLayerRegistry.registerModelLayer(PINCH_BEETLE, PinchBeetleModel::checkForPack);
                EntityModelLayerRegistry.registerModelLayer(HELMET_CRAB, HelmetCrabModel::checkForPack);
                EntityModelLayerRegistry.registerModelLayer(SLIME_BEETLE, SlimeBeetleModel::checkForPack);
                EntityModelLayerRegistry.registerModelLayer(SLIME_BEETLE_TAIL, SlimeBeetleModel::checkForPack);
                EntityModelLayerRegistry.registerModelLayer(MAZE_SLIME, SlimeModel::createInnerBodyLayer);
                EntityModelLayerRegistry.registerModelLayer(MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
                EntityModelLayerRegistry.registerModelLayer(HARBINGER_CUBE, HarbingerCubeModel::create);
                EntityModelLayerRegistry.registerModelLayer(CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
                EntityModelLayerRegistry.registerModelLayer(HOSTILE_WOLF, () ->
                        LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
                                EntityModelLayerRegistry.registerModelLayer(HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
                                EntityModelLayerRegistry.registerModelLayer(KING_SPIDER, SpiderModel::createSpiderBodyLayer);
                                EntityModelLayerRegistry.registerModelLayer(SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
                                EntityModelLayerRegistry.registerModelLayer(CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
                                EntityModelLayerRegistry.registerModelLayer(ICE_CRYSTAL, IceCrystalModel::create);
                                EntityModelLayerRegistry.registerModelLayer(STABLE_ICE_CORE, StableIceCoreModel::create);
                                EntityModelLayerRegistry.registerModelLayer(UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
                                EntityModelLayerRegistry.registerModelLayer(NOOP, () ->
                                        LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
                                EntityModelLayerRegistry.registerModelLayer(PROTECTION_BOX, () ->
                                        LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
                                                                EntityModelLayerRegistry.registerModelLayer(MOSQUITO_SWARM, MosquitoSwarmModel::create);
                                                                EntityModelLayerRegistry.registerModelLayer(TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
                                                                EntityModelLayerRegistry.registerModelLayer(WRAITH, WraithModel::create);
                                                                EntityModelLayerRegistry.registerModelLayer(CARMINITE_GHASTGUARD, TFGhastModel::create);
                                                                EntityModelLayerRegistry.registerModelLayer(CARMINITE_GHASTLING, TFGhastModel::create);
                                                                EntityModelLayerRegistry.registerModelLayer(CARMINITE_GOLEM, CarminiteGolemModel::create);
                                                                EntityModelLayerRegistry.registerModelLayer(MINOTAUR, MinotaurModel::checkForPack);
                                                                EntityModelLayerRegistry.registerModelLayer(MINOSHROOM, MinoshroomModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(CHAIN, ChainModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(CHAIN_BLOCK, SpikeBlockModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(LOYAL_ZOMBIE, () ->
                                                                                                                                        LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(RISING_ZOMBIE, () ->
                                                                                                                                        LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(ADHERENT, AdherentModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(YETI, YetiModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(ALPHA_YETI, AlphaYetiModel::create);
                                                                                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(KNIGHT_PHANTOM, KnightPhantomModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(SNOW_QUEEN, SnowQueenModel::checkForPack);
                                                                                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(LICH, LichModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(NAGA, NagaModel::checkForPack);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(NAGA_BODY, NagaModel::checkForPack);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(HYDRA, HydraModel::checkForPack);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(HYDRA_HEAD, HydraHeadModel::checkForPack);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(HYDRA_NECK, HydraNeckModel::checkForPack);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(HYDRA_MORTAR, HydraMortarModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(MOONWORM, MoonwormModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(CICADA, CicadaModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(FIREFLY, FireflyModel::create);
                                                                                                                                                                                                EntityModelLayerRegistry.registerModelLayer(UR_GHAST, UrGhastModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(TROLL, TrollModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(LICH_MINION, () ->
                                                                                                                                        LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(BIGHORN_SHEEP, BighornModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(QUEST_RAM, QuestRamModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(RAVEN, RavenModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(TINY_BIRD, TinyBirdModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(BRAZIER, BrazierModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(NAGA_TROPHY, NagaModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(LICH_TROPHY, LichModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(MINOSHROOM_TROPHY, MinoshroomModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(HYDRA_TROPHY, HydraHeadModel::checkForPack);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(UR_GHAST_TROPHY, UrGhastModel::create);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
                                                                                                                                EntityModelLayerRegistry.registerModelLayer(SNOW_QUEEN_TROPHY, SnowQueenModel::checkForPack);
                                                                                                                               EntityModelLayerRegistry.registerModelLayer(QUEST_RAM_TROPHY, QuestRamModel::checkForPackTrophyEdition);
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(SKULL_CHEST, () -> SkullChestRenderer.create(false));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(KEEPSAKE_CASKET, () -> SkullChestRenderer.create(true));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(RED_THREAD, RedThreadModel::create);
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(CHARM_OF_LIFE, CharmOfLifeNecklaceModel::create);
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(new CubeDeformation(0.25F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(new CubeDeformation(0.5F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(FIERY_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(FIERY_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(new CubeDeformation(0.25F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(new CubeDeformation(0.5F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(new CubeDeformation(0.25F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(new CubeDeformation(0.5F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(new CubeDeformation(0.25F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(new CubeDeformation(0.5F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(new CubeDeformation(0.5F)), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.5F), false), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.5F), true), 64, 32));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.5F));
                                                                                                                              EntityModelLayerRegistry.registerModelLayer(TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
    }
}
