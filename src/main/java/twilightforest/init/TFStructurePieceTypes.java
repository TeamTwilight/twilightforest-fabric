package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.TFMain;
import twilightforest.world.components.structures.*;
import twilightforest.world.components.structures.courtyard.*;
import twilightforest.world.components.structures.darktower.*;
import twilightforest.world.components.structures.fallentrunk.FallenTrunkPiece;
import twilightforest.world.components.structures.finalcastle.*;
import twilightforest.world.components.structures.hollowtree.*;
import twilightforest.world.components.structures.icetower.*;
import twilightforest.world.components.structures.lichtower.*;
import twilightforest.world.components.structures.lichtowerrevamp.*;
import twilightforest.world.components.structures.minotaurmaze.*;
import twilightforest.world.components.structures.mushroomtower.*;
import twilightforest.world.components.structures.stronghold.*;
import twilightforest.world.components.structures.trollcave.*;

public class TFStructurePieceTypes {

	// Single-Piece Structures
	//IStructurePieceTypes that can be referred to
	public static final StructurePieceType TFHill = registerPieceType("TFHill", HollowHillComponent::new);
	public static final StructurePieceType TFHedge = registerPieceType("TFHedge", HedgeMazeComponent::new);
	public static final StructurePieceType TFQuestGrove = registerPieceType("TFQuest1", QuestGrove::new);
	public static final StructurePieceType TFHydra = registerPieceType("TFHydra", HydraLairComponent::new);
	public static final StructurePieceType TFYeti = registerPieceType("TFYeti", YetiCaveComponent::new);
	public static final StructurePieceType TFFallenTrunk = registerPieceType("TFFallenTrunk", FallenTrunkPiece::new);
	public static final StructurePieceType TFUtilityPiece = registerPieceType("TFUtilityPiece", UtilityPiece::new);
	public static final StructurePieceType TFJigsawTemplate = registerPieceType("TFJigsawTemplate", TwilightJigsawPiece::defaultDeserialize);

	// Hollow Tree
	public static final StructurePieceType TFHTLB = registerPieceType("TFHTLB", HollowTreeLargeBranch::new);
	public static final StructurePieceType TFHTMB = registerPieceType("TFHTMB", HollowTreeMedBranch::new);
	public static final StructurePieceType TFHTSB = registerPieceType("TFHTSB", HollowTreeSmallBranch::new);
	public static final StructurePieceType TFHTTr = registerPieceType("TFHTTr", HollowTreeTrunk::new);
	public static final StructurePieceType TFHTRo = registerPieceType("TFHTRo", HollowTreeRoot::new);
	public static final StructurePieceType TFHTLD = registerPieceType("TFHTLD", HollowTreeLeafDungeon::new);

	// Mushroom Castle
	//public static final StructurePieceType TFMT = registerPieceType("TFMT", StructureStartMushroomTower::new);
	public static final StructurePieceType TFMTMai = registerPieceType("TFMTMai", MushroomTowerMainComponent::new);
	public static final StructurePieceType TFMTWin = registerPieceType("TFMTWin", MushroomTowerWingComponent::new);
	public static final StructurePieceType TFMTBri = registerPieceType("TFMTBri", MushroomTowerBridgeComponent::new);
	public static final StructurePieceType TFMTMB = registerPieceType("TFMTMB", MushroomTowerMainBridgeComponent::new);
	public static final StructurePieceType TFMTRoofMush = registerPieceType("TFMTRoofMush", TowerRoofMushroomComponent::new);

	// Naga Courtyard
	//public static final StructurePieceType TFNC = registerPieceType("TFNC", StructureStartCourtyard::new);
	public static final StructurePieceType TFNCMn = registerPieceType("TFNCMn", CourtyardMain::new);
	public static final StructurePieceType TFNCCp = registerPieceType("TFNCCp", NagaCourtyardHedgeCapComponent::new);
	public static final StructurePieceType TFNCCpP = registerPieceType("TFNCCpP", NagaCourtyardHedgeCapPillarComponent::new);
	public static final StructurePieceType TFNCCr = registerPieceType("TFNCCr", NagaCourtyardHedgeCornerComponent::new);
	public static final StructurePieceType TFNCLn = registerPieceType("TFNCLn", NagaCourtyardHedgeLineComponent::new);
	public static final StructurePieceType TFNCT = registerPieceType("TFNCT", NagaCourtyardHedgeTJunctionComponent::new);
	public static final StructurePieceType TFNCIs = registerPieceType("TFNCIs", NagaCourtyardHedgeIntersectionComponent::new);
	public static final StructurePieceType TFNCPd = registerPieceType("TFNCPd", NagaCourtyardHedgePadderComponent::new);
	public static final StructurePieceType TFNCTe = registerPieceType("TFNCTe", CourtyardTerrace::new);
	public static final StructurePieceType TFNCHe = registerPieceType("TFNCHe", CourtyardTerraceHedge::new);
	public static final StructurePieceType TFNCPa = registerPieceType("TFNCPa", CourtyardPathPiece::new);
	public static final StructurePieceType TFNCWl = registerPieceType("TFNCWl", CourtyardWall::new);
	public static final StructurePieceType TFNCWP = registerPieceType("TFNCWP", CourtyardWallPadder::new);
	public static final StructurePieceType TFNCWC = registerPieceType("TFNCWC", CourtyardWallCornerOuter::new);
	public static final StructurePieceType TFNCWA = registerPieceType("TFNCWA", CourtyardWallCornerInner::new);

	// Old Lich Tower
	//public static final IStructurePieceType TFLT = TFFeature.registerPiece("TFLT", StructureStartLichTower::new);
	public static final StructurePieceType TFLTBea = registerPieceType("TFLTBea", TowerBeardComponent::new);
	public static final StructurePieceType TFLTBA = registerPieceType("TFLTBA", TowerBeardAttachedComponent::new);
	public static final StructurePieceType TFLTBri = registerPieceType("TFLTBri", TowerBridgeComponent::new);
	public static final StructurePieceType TFLTMai = registerPieceType("TFLTMai", TowerMainComponent::new);
	public static final StructurePieceType TFLTOut = registerPieceType("TFLTOut", TowerOutbuildingComponent::new);
	public static final StructurePieceType TFLTRoo = registerPieceType("TFLTRoo", TowerRoofComponent::new);
	public static final StructurePieceType TFLTRAS = registerPieceType("TFLTRAS", TowerRoofAttachedSlabComponent::new);
	public static final StructurePieceType TFLTRF = registerPieceType("TFLTRF", TowerRoofFenceComponent::new);
	public static final StructurePieceType TFLTRGF = registerPieceType("TFLTRGF", TowerRoofGableForwardsComponent::new);
	public static final StructurePieceType TFLTRP = registerPieceType("TFLTRP", TowerRoofPointyComponent::new);
	public static final StructurePieceType TFLTRPO = registerPieceType("TFLTRPO", TowerRoofPointyOverhangComponent::new);
	public static final StructurePieceType TFLTRS = registerPieceType("TFLTRS", TowerRoofSlabComponent::new);
	public static final StructurePieceType TFLTRSF = registerPieceType("TFLTRSF", TowerRoofSlabForwardsComponent::new);
	public static final StructurePieceType TFLTRSt = registerPieceType("TFLTRSt", TowerRoofStairsComponent::new);
	public static final StructurePieceType TFLTRStO = registerPieceType("TFLTRStO", TowerRoofStairsOverhangComponent::new);
	public static final StructurePieceType TFLTWin = registerPieceType("TFLTWin", TowerWingComponent::new);

	// Lich Tower
	public static final StructurePieceType LICH_TOWER_FOYER = registerPieceType("TFLT" + "TFoy", LichTowerFoyer::new);
	public static final StructurePieceType LICH_TOWER_BASE = registerPieceType("TFLT" + "CTBase", LichTowerBase::new);
	public static final StructurePieceType LICH_TOWER_BASE_TRIM = registerPieceType("TFLT" + "CTTrim", LichTowerBaseTrim::new);
	public static final StructurePieceType LICH_TOWER_SEGMENT = registerPieceType("TFLT" + "CTSeg", LichTowerSegment::new);
	public static final StructurePieceType LICH_SPAWNER_BRIDGE = registerPieceType("TFLT" + "MobBridge", LichTowerSpawnerBridge::new);
	public static final StructurePieceType LICH_WING_BRIDGE = registerPieceType("TFLT" + "Bridge", LichTowerWingBridge::new);
	public static final StructurePieceType LICH_WING_ROOF = registerPieceType("TFLT" + "TRoof", LichTowerWingRoof::new);
	public static final StructurePieceType LICH_WING_BEARD = registerPieceType("TFLT" + "TBeard", LichTowerWingBeard::new);
	public static final StructurePieceType LICH_WING_ROOM = registerPieceType("TFLT" + "TRoom", LichTowerWingRoom::new);
	public static final StructurePieceType LICH_TOWER_DECOR = registerPieceType("TFLT" + "TDecor", LichTowerRoomDecor::new);
	public static final StructurePieceType LICH_MAGIC_GALLERY = registerPieceType("TFLT" + "TGallery", LichTowerMagicGallery::new);
	public static final StructurePieceType LICH_FOYER_DECORATION = registerPieceType("TFLT" + "TFoyD", LichTowerFoyerDecor::new);
	public static final StructurePieceType LICH_BOSS_ROOM = registerPieceType("TFLT" + "TBoss", LichBossRoom::new);
	public static final StructurePieceType LICH_BOSS_ROOF = registerPieceType("TFLT" + "TBossRoof", LichBossRoof::new);
	public static final StructurePieceType LICH_PERIMETER_FENCE = registerPieceType("TFLT" + "Fence", LichPerimeterFence::new);
	public static final StructurePieceType LICH_YARD_PATH = registerPieceType("TFLT" + "Path", LichYardBox::new);
	public static final StructurePieceType LICH_YARD_GRAVE = registerPieceType("TFLT" + "Grave", LichYardGrave::new);
	public static final StructurePieceType LICH_YARD_LIGHTS = registerPieceType("TFLT" + "Light", LichYardLights::new);

	// Labyrinth
	//public static final StructurePieceType TFLr = registerPieceType("TFLr", StructureStartLabyrinth::new);
	public static final StructurePieceType TFMMC = registerPieceType("TFMMC", MazeCorridorComponent::new);
	public static final StructurePieceType TFMMCIF = registerPieceType("TFMMCIF", MazeCorridorIronFenceComponent::new);
	public static final StructurePieceType TFMMCR = registerPieceType("TFMMCR", MazeCorridorRootsComponent::new);
	public static final StructurePieceType TFMMCS = registerPieceType("TFMMCS", MazeCorridorShroomsComponent::new);
	public static final StructurePieceType TFMMDE = registerPieceType("TFMMDE", MazeDeadEndComponent::new);
	public static final StructurePieceType TFMMDEC = registerPieceType("TFMMDEC", MazeDeadEndChestComponent::new);
	public static final StructurePieceType TFMMDEF = registerPieceType("TFMMDEF", MazeDeadEndFountainComponent::new);
	public static final StructurePieceType TFMMDEFL = registerPieceType("TFMMDEFL", MazeDeadEndFountainLavaComponent::new);
	public static final StructurePieceType TFMMDEP = registerPieceType("TFMMDEP", MazeDeadEndPaintingComponent::new);
	public static final StructurePieceType TFMMDER = registerPieceType("TFMMDER", MazeDeadEndRootsComponent::new);
	public static final StructurePieceType TFMMDES = registerPieceType("TFMMDES", MazeDeadEndShroomsComponent::new);
	public static final StructurePieceType TFMMDET = registerPieceType("TFMMDET", MazeDeadEndTorchesComponent::new);
	public static final StructurePieceType TFMMDETrC = registerPieceType("TFMMDETrC", MazeDeadEndTrappedChestComponent::new);
	public static final StructurePieceType TFMMDETC = registerPieceType("TFMMDETC", MazeDeadEndTripwireChestComponent::new);
	public static final StructurePieceType TFMMES = registerPieceType("TFMMES", MazeEntranceShaftComponent::new);
	public static final StructurePieceType TFMMMound = registerPieceType("TFMMMound", MazeMoundComponent::new);
	public static final StructurePieceType TFMMMR = registerPieceType("TFMMMR", MazeMushRoomComponent::new);
	public static final StructurePieceType TFMMR = registerPieceType("TFMMR", MazeRoomComponent::new);
	public static final StructurePieceType TFMMRB = registerPieceType("TFMMRB", MazeRoomBossComponent::new);
	public static final StructurePieceType TFMMRC = registerPieceType("TFMMRC", MazeRoomCollapseComponent::new);
	public static final StructurePieceType TFMMRE = registerPieceType("TFMMRE", MazeRoomExitComponent::new);
	public static final StructurePieceType TFMMRF = registerPieceType("TFMMRF", MazeRoomFountainComponent::new);
	public static final StructurePieceType TFMMRSC = registerPieceType("TFMMRSC", MazeRoomSpawnerChestsComponent::new);
	public static final StructurePieceType TFMMRV = registerPieceType("TFMMRV", MazeRoomVaultComponent::new);
	public static final StructurePieceType TFMMRuins = registerPieceType("TFMMRuins", MazeRuinsComponent::new);
	public static final StructurePieceType TFMMUE = registerPieceType("TFMMUE", MazeUpperEntranceComponent::new);
	public static final StructurePieceType TFMMaze = registerPieceType("TFMMaze", MinotaurMazeComponent::new);

	// Knight Stronghold
	//public static final StructurePieceType TFKSt = registerPieceType("TFKSt", StructureStartKnightStronghold::new);
	public static final StructurePieceType TFSSH = registerPieceType("TFSSH", StrongholdSmallHallwayComponent::new);
	public static final StructurePieceType TFSLT = registerPieceType("TFSLT", StrongholdLeftTurnComponent::new);
	public static final StructurePieceType TFSCr = registerPieceType("TFSCr", StrongholdCrossingComponent::new);
	public static final StructurePieceType TFSRT = registerPieceType("TFSRT", StrongholdRightTurnComponent::new);
	public static final StructurePieceType TFSDE = registerPieceType("TFSDE", StrongholdDeadEndComponent::new);
	public static final StructurePieceType TFSBalR = registerPieceType("TFSBalR", StrongholdBalconyRoomComponent::new);
	public static final StructurePieceType TFSTR = registerPieceType("TFSTR", StrongholdTrainingRoomComponent::new);
	public static final StructurePieceType TFSSS = registerPieceType("TFSSS", StrongholdSmallStairsComponent::new);
	public static final StructurePieceType TFSTC = registerPieceType("TFSTC", StrongholdTreasureCorridorComponent::new);
	public static final StructurePieceType TFSAt = registerPieceType("TFSAt", StrongholdAtriumComponent::new);
	public static final StructurePieceType TFSFo = registerPieceType("TFSFo", StrongholdFoundryComponent::new);
	public static final StructurePieceType TFTreaR = registerPieceType("TFTreaR", StrongholdTreasureRoomComponent::new);
	public static final StructurePieceType TFSBR = registerPieceType("TFSBR", StrongholdBossRoomComponent::new);
	public static final StructurePieceType TFSAC = registerPieceType("TFSAC", StrongholdAccessChamberComponent::new);
	public static final StructurePieceType TFSEnter = registerPieceType("TFSEnter", StrongholdEntranceComponent::new);
	public static final StructurePieceType TFSUA = registerPieceType("TFSUA", StrongholdUpperAscenderComponent::new);
	public static final StructurePieceType TFSULT = registerPieceType("TFSULT", StrongholdUpperLeftTurnComponent::new);
	public static final StructurePieceType TFSURT = registerPieceType("TFSURT", StrongholdUpperRightTurnComponent::new);
	public static final StructurePieceType TFSUCo = registerPieceType("TFSUCo", StrongholdUpperCorridorComponent::new);
	public static final StructurePieceType TFSUTI = registerPieceType("TFSUTI", StrongholdUpperTIntersectionComponent::new);
	public static final StructurePieceType TFSShield = registerPieceType("TFSShield", StrongholdShieldStructure::new);

	// Dark Tower
	//public static final StructurePieceType TFDT = registerPieceType("TFDT", StructureStartDarkTower::new);
	public static final StructurePieceType TFDTBal = registerPieceType("TFDTBal", DarkTowerBalconyComponent::new);
	public static final StructurePieceType TFDTBea = registerPieceType("TFDTBea", DarkTowerBeardComponent::new);
	public static final StructurePieceType TFDTBB = registerPieceType("TFDTBB", DarkTowerBossBridgeComponent::new);
	public static final StructurePieceType TFDTBT = registerPieceType("TFDTBT", DarkTowerBossTrapComponent::new);
	public static final StructurePieceType TFDTBri = registerPieceType("TFDTBri", DarkTowerBridgeComponent::new);
	public static final StructurePieceType TFDTEnt = registerPieceType("TFDTEnt", DarkTowerEntranceComponent::new);
	public static final StructurePieceType TFDTEB = registerPieceType("TFDTEB", DarkTowerEntranceBridgeComponent::new);
	public static final StructurePieceType TFDTMai = registerPieceType("TFDTMai", DarkTowerMainComponent::new);
	public static final StructurePieceType TFDTMB = registerPieceType("TFDTMB", DarkTowerMainBridgeComponent::new);
	public static final StructurePieceType TFDTRooS = registerPieceType("TFDTRooS", DarkTowerRoofComponent::new);
	public static final StructurePieceType TFDTRA = registerPieceType("TFDTRA", DarkTowerRoofAntennaComponent::new);
	public static final StructurePieceType TFDTRC = registerPieceType("TFDTRC", DarkTowerRoofCactusComponent::new);
	public static final StructurePieceType TFDTRFP = registerPieceType("TFDTRFP", DarkTowerRoofFourPostComponent::new);
	public static final StructurePieceType TFDTRR = registerPieceType("TFDTRR", DarkTowerRoofRingsComponent::new);
	public static final StructurePieceType TFDTWin = registerPieceType("TFDTWin", DarkTowerWingComponent::new);

	// Aurora Palace
	//public static final StructurePieceType TFAP = registerPieceType("TFAP", StructureStartAuroraPalace::new);
	public static final StructurePieceType TFITMai = registerPieceType("TFITMai", IceTowerMainComponent::new);
	public static final StructurePieceType TFITWin = registerPieceType("TFITWin", IceTowerWingComponent::new);
	public static final StructurePieceType TFITRoof = registerPieceType("TFITRoof", IceTowerRoofComponent::new);
	public static final StructurePieceType TFITBea = registerPieceType("TFITBea", IceTowerBeardComponent::new);
	public static final StructurePieceType TFITBoss = registerPieceType("TFITBoss", IceTowerBossWingComponent::new);
	public static final StructurePieceType TFITEnt = registerPieceType("TFITEnt", IceTowerEntranceComponent::new);
	public static final StructurePieceType TFITBri = registerPieceType("TFITBri", IceTowerBridgeComponent::new);
	public static final StructurePieceType TFITSt = registerPieceType("TFITSt", IceTowerStairsComponent::new);

	// Troll Cave
	//public static final StructurePieceType TFTC = registerPieceType("TFTC", StructureStartTrollCave::new);
	public static final StructurePieceType TFTCMai = registerPieceType("TFTCMai", TrollCaveMainComponent::new);
	public static final StructurePieceType TFTCCon = registerPieceType("TFTCCon", TrollCaveConnectComponent::new);
	public static final StructurePieceType TFTCGard = registerPieceType("TFTCGard", TrollCaveGardenComponent::new);
	public static final StructurePieceType TFTCloud = registerPieceType("TFTCloud", TrollCloudComponent::new);
	public static final StructurePieceType TFClCa = registerPieceType("TFClCa", CloudCastleComponent::new);
	public static final StructurePieceType TFClTr = registerPieceType("TFClTr", CloudTreeComponent::new);
	public static final StructurePieceType TFTCVa = registerPieceType("TFTCVa", TrollVaultComponent::new);
	public static final StructurePieceType TFCloud = registerPieceType("TFCloud", CloudComponent::new);

	// Final Castle
	//public static final StructurePieceType TFFC = registerPieceType("TFFC", StructureStartFinalCastle::new);
	public static final StructurePieceType TFFCMain = registerPieceType("TFFCMain", FinalCastleMainComponent::new);
	public static final StructurePieceType TFFCStTo = registerPieceType("TFFCStTo", FinalCastleStairTowerComponent::new);
	public static final StructurePieceType TFFCLaTo = registerPieceType("TFFCLaTo", FinalCastleLargeTowerComponent::new);
	public static final StructurePieceType TFFCMur = registerPieceType("TFFCMur", FinalCastleMuralComponent::new);
	public static final StructurePieceType TFFCToF48 = registerPieceType("TFFCToF48", FinalCastleFoundation48Component::new);
	public static final StructurePieceType TFFCRo48Cr = registerPieceType("TFFCRo48Cr", FinalCastleRoof48CrenellatedComponent::new);
	public static final StructurePieceType TFFCBoGaz = registerPieceType("TFFCBoGaz", FinalCastleBossGazeboComponent::new);
	public static final StructurePieceType TFFCSiTo = registerPieceType("TFFCSiTo", FinalCastleMazeTower13Component::new);
	public static final StructurePieceType TFFCDunSt = registerPieceType("TFFCDunSt", FinalCastleDungeonStepsComponent::new);
	public static final StructurePieceType TFFCDunEn = registerPieceType("TFFCDunEn", FinalCastleDungeonEntranceComponent::new);
	public static final StructurePieceType TFFCDunR31 = registerPieceType("TFFCDunR31", FinalCastleDungeonRoom31Component::new);
	public static final StructurePieceType TFFCDunEx = registerPieceType("TFFCDunEx", FinalCastleDungeonExitComponent::new);
	public static final StructurePieceType TFFCDunBoR = registerPieceType("TFFCDunBoR", FinalCastleDungeonForgeRoomComponent::new);
	public static final StructurePieceType TFFCRo9Cr = registerPieceType("TFFCRo9Cr", FinalCastleRoof9CrenellatedComponent::new);
	public static final StructurePieceType TFFCRo13Cr = registerPieceType("TFFCRo13Cr", FinalCastleRoof13CrenellatedComponent::new);
	public static final StructurePieceType TFFCRo13Con = registerPieceType("TFFCRo13Con", FinalCastleRoof13ConicalComponent::new);
	public static final StructurePieceType TFFCRo13Pk = registerPieceType("TFFCRo13Pk", FinalCastleRoof13PeakedComponent::new);
	public static final StructurePieceType TFFCEnTo = registerPieceType("TFFCEnTo", FinalCastleEntranceTowerComponent::new);
	public static final StructurePieceType TFFCEnSiTo = registerPieceType("TFFCEnSiTo", FinalCastleEntranceSideTowerComponent::new);
	public static final StructurePieceType TFFCEnBoTo = registerPieceType("TFFCEnBoTo", FinalCastleEntranceBottomTowerComponent::new);
	public static final StructurePieceType TFFCEnSt = registerPieceType("TFFCEnSt", FinalCastleEntranceStairsComponent::new);
	public static final StructurePieceType TFFCBelTo = registerPieceType("TFFCBelTo", FinalCastleBellTower21Component::new);
	public static final StructurePieceType TFFCBri = registerPieceType("TFFCBri", FinalCastleBridgeComponent::new);
	public static final StructurePieceType TFFCToF13 = registerPieceType("TFFCToF13", FinalCastleFoundation13Component::new);
	public static final StructurePieceType TFFCBeF21 = registerPieceType("TFFCBeF21", FinalCastleBellFoundation21Component::new);
	public static final StructurePieceType TFFCFTh21 = registerPieceType("TFFCFTh21", FinalCastleFoundation13ComponentThorns::new);
	public static final StructurePieceType TFFCDamT = registerPieceType("TFFCDamT", FinalCastleDamagedTowerComponent::new);
	public static final StructurePieceType TFFCWrT = registerPieceType("TFFCWrT", FinalCastleWreckedTowerComponent::new);

	private static StructurePieceType registerPieceType(String name, StructurePieceType structurePieceType) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_PIECE,
			TFMain.prefix(name),
			structurePieceType
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing structure piece types...");
	}
}
