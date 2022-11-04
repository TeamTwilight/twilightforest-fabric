package twilightforest.client.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.legacy.*;
import twilightforest.compat.trinkets.model.CharmOfKeepingModel;
import twilightforest.compat.trinkets.model.CharmOfLife1NecklaceModel;
import twilightforest.compat.trinkets.model.CharmOfLife2NecklaceModel;
import twilightforest.client.model.tileentity.*;
import twilightforest.client.model.tileentity.legacy.*;
import twilightforest.client.renderer.tileentity.CasketTileEntityRenderer;

public class TFLayerDefinitions {

	public static void registerLayers() {

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

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_HYDRA_TROPHY, HydraTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_MINOSHROOM_TROPHY, MinoshroomTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_QUEST_RAM_TROPHY, QuestRamTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_SNOW_QUEEN_TROPHY, SnowQueenTrophyLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_UR_GHAST_TROPHY, UrGhastTrophyLegacyModel::create);

		event.registerLayerDefinition(TFModelLayers.ADHERENT, AdherentModel::create);
		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		event.registerLayerDefinition(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP, BighornModel::create);
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_FUR, BighornFurLayer::create);
		event.registerLayerDefinition(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR, BoarModel::create);
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, DeerModel::create);
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, HostileWolfModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA, HydraModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_NECK, HydraNeckModel::create);
		event.registerLayerDefinition(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		event.registerLayerDefinition(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		event.registerLayerDefinition(TFModelLayers.KOBOLD, KoboldModel::create);
		event.registerLayerDefinition(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.LICH, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOTAUR, MinotaurModel::create);
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, WolfModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
		event.registerLayerDefinition(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.RAVEN, RavenModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP, RedcapModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		event.registerLayerDefinition(TFModelLayers.SQUIRREL, SquirrelModel::create);
		event.registerLayerDefinition(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TINY_BIRD, TinyBirdModel::create);
		event.registerLayerDefinition(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TROLL, TrollModel::create);
		event.registerLayerDefinition(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST, UrGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, WolfModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_BIGHORN_SHEEP, BighornLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_BLOCKCHAIN_GOBLIN, BlockChainGoblinLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_BOAR, BoarLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_DEER, DeerLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_FIRE_BEETLE, FireBeetleLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_HELMET_CRAB, HelmetCrabLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_HYDRA, HydraLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_HYDRA_HEAD, HydraHeadLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_HYDRA_NECK, HydraNeckLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_KOBOLD, KoboldLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_LOWER_GOBLIN_KNIGHT, LowerGoblinKnightLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_MINOSHROOM, MinoshroomLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_MINOTAUR, MinotaurLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_NAGA, NagaLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_NAGA_BODY, NagaLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_PINCH_BEETLE, PinchBeetleLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_QUEST_RAM, QuestRamLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_RAVEN, RavenLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_REDCAP, RedcapLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_SLIME_BEETLE, SlimeBeetleLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_SLIME_BEETLE_TAIL, SlimeBeetleLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_SNOW_QUEEN, SnowQueenLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_SQUIRREL, SquirrelLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_TINY_BIRD, TinyBirdLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_TROLL, TrollLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_UPPER_GOBLIN_KNIGHT, UpperGoblinKnightLegacyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LEGACY_UR_GHAST, UrGhastLegacyModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, CasketTileEntityRenderer::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);

		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHARM_OF_LIFE_1, CharmOfLife1NecklaceModel::create);
			EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHARM_OF_LIFE_2, CharmOfLife2NecklaceModel::create);
			EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHARM_OF_KEEPING, CharmOfKeepingModel::create);
		}
	}
}
