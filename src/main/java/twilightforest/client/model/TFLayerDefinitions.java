package twilightforest.client.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.newmodels.NewBighornModel;
import twilightforest.client.model.entity.newmodels.NewBlockChainGoblinModel;
import twilightforest.client.model.entity.newmodels.NewBoarModel;
import twilightforest.client.model.entity.newmodels.NewDeerModel;
import twilightforest.client.model.entity.newmodels.NewFireBeetleModel;
import twilightforest.client.model.entity.newmodels.NewHelmetCrabModel;
import twilightforest.client.model.entity.newmodels.NewHydraHeadModel;
import twilightforest.client.model.entity.newmodels.NewHydraModel;
import twilightforest.client.model.entity.newmodels.NewHydraNeckModel;
import twilightforest.client.model.entity.newmodels.NewKoboldModel;
import twilightforest.client.model.entity.newmodels.NewLowerGoblinKnightModel;
import twilightforest.client.model.entity.newmodels.NewMinoshroomModel;
import twilightforest.client.model.entity.newmodels.NewMinotaurModel;
import twilightforest.client.model.entity.newmodels.NewNagaModel;
import twilightforest.client.model.entity.newmodels.NewPinchBeetleModel;
import twilightforest.client.model.entity.newmodels.NewQuestRamModel;
import twilightforest.client.model.entity.newmodels.NewRavenModel;
import twilightforest.client.model.entity.newmodels.NewRedcapModel;
import twilightforest.client.model.entity.newmodels.NewSlimeBeetleModel;
import twilightforest.client.model.entity.newmodels.NewSnowQueenModel;
import twilightforest.client.model.entity.newmodels.NewSquirrelModel;
import twilightforest.client.model.entity.newmodels.NewTinyBirdModel;
import twilightforest.client.model.entity.newmodels.NewTrollModel;
import twilightforest.client.model.entity.newmodels.NewUpperGoblinKnightModel;
import twilightforest.client.model.entity.newmodels.NewUrGhastModel;
import twilightforest.client.model.tileentity.*;
import twilightforest.client.model.tileentity.legacy.*;
import twilightforest.client.renderer.entity.TwilightBoatRenderer;
import twilightforest.client.renderer.tileentity.CasketTileEntityRenderer;
import twilightforest.entity.TwilightBoat;

public class TFLayerDefinitions {

	public static void registerLayers() {

		for(TwilightBoat.Type boatType : TwilightBoat.Type.values()) {
			EntityModelLayerRegistry.registerModelLayer(TwilightBoatRenderer.createBoatModelName(boatType), BoatModel::createBodyModel);
			EntityModelLayerRegistry.registerModelLayer(TwilightBoatRenderer.createChestBoatModelName(boatType), ChestBoatModel::createBodyModel);
		}

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.setup(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.setup(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenTrophyModel::createHead);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastTrophyModel::createHead);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_HYDRA_TROPHY, HydraTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_MINOSHROOM_TROPHY, MinoshroomTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_QUEST_RAM_TROPHY, QuestRamTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_SNOW_QUEEN_TROPHY, SnowQueenTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_UR_GHAST_TROPHY, UrGhastTrophyLegacyModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, NewBighornModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_FUR, BighornFurLayer::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, NewBlockChainGoblinModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, NewBoarModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, NewDeerModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, NewFireBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, NewHelmetCrabModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF, HostileWolfModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, NewHydraHeadModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, NewHydraModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, NewHydraNeckModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, NewKoboldModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, NewLowerGoblinKnightModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, NewMinoshroomModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, NewMinotaurModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF, WolfModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NewNagaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NewNagaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, NewPinchBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, NewQuestRamModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, NewRavenModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, NewRedcapModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, NewSlimeBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, NewSlimeBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, NewSnowQueenModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, NewSquirrelModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, NewTinyBirdModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, NewTrollModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, NewUpperGoblinKnightModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, NewUrGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF, WolfModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_BIGHORN_SHEEP, BighornModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_BOAR, BoarModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_DEER, DeerModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_FIRE_BEETLE, FireBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_HELMET_CRAB, HelmetCrabModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_HYDRA, HydraModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_HYDRA_HEAD, HydraHeadModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_HYDRA_NECK, HydraNeckModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_KOBOLD, KoboldModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_MINOSHROOM, MinoshroomModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_MINOTAUR, MinotaurModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_NAGA, NagaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_NAGA_BODY, NagaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_PINCH_BEETLE, PinchBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_QUEST_RAM, QuestRamModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_RAVEN, RavenModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_REDCAP, RedcapModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_SLIME_BEETLE, SlimeBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_SNOW_QUEEN, SnowQueenModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_SQUIRREL, SquirrelModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_TINY_BIRD, TinyBirdModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_TROLL, TrollModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NEW_UR_GHAST, UrGhastModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, CasketTileEntityRenderer::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}
}
