package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.*;
import twilightforest.world.components.structures.courtyard.*;
import twilightforest.world.components.structures.darktower.*;
import twilightforest.world.components.structures.finalcastle.*;
import twilightforest.world.components.structures.icetower.*;
import twilightforest.world.components.structures.lichtower.*;
import twilightforest.world.components.structures.lichtowerrevamp.*;
import twilightforest.world.components.structures.minotaurmaze.*;
import twilightforest.world.components.structures.mushroomtower.*;
import twilightforest.world.components.structures.stronghold.*;
import twilightforest.world.components.structures.trollcave.*;

import java.util.Locale;

public class TFStructurePieceTypes {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, TwilightForestMod.ID);

    // Single-Piece Structures
    //IStructurePieceTypes that can be referred to
    public static final RegistryObject<StructurePieceType> TFHill = registerPieceType("TFHill", HollowHillComponent::new);
    public static final RegistryObject<StructurePieceType> TFHedge = registerPieceType("TFHedge", HedgeMazeComponent::new);
    public static final RegistryObject<StructurePieceType> TFQuestGrove = registerPieceType("TFQuest1", QuestGrove::new);
    public static final RegistryObject<StructurePieceType> TFHydra = registerPieceType("TFHydra", HydraLairComponent::new);
    public static final RegistryObject<StructurePieceType> TFYeti = registerPieceType("TFYeti", YetiCaveComponent::new);

    // Mushroom Castle
    //public static final RegistryObject<StructurePieceType> TFMT = registerPieceType("TFMT", StructureStartMushroomTower::new);
    public static final RegistryObject<StructurePieceType> TFMTMai = registerPieceType("TFMTMai", MushroomTowerMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFMTWin = registerPieceType("TFMTWin", MushroomTowerWingComponent::new);
    public static final RegistryObject<StructurePieceType> TFMTBri = registerPieceType("TFMTBri", MushroomTowerBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFMTMB = registerPieceType("TFMTMB", MushroomTowerMainBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFMTRoofMush = registerPieceType("TFMTRoofMush", TowerRoofMushroomComponent::new);

    // Naga Courtyard
    //public static final RegistryObject<StructurePieceType> TFNC = registerPieceType("TFNC", StructureStartCourtyard::new);
    public static final RegistryObject<StructurePieceType> TFNCMn = registerPieceType("TFNCMn", CourtyardMain::new);
    public static final RegistryObject<StructurePieceType> TFNCCp = registerPieceType("TFNCCp", NagaCourtyardHedgeCapComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCCpP = registerPieceType("TFNCCpP", NagaCourtyardHedgeCapPillarComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCCr = registerPieceType("TFNCCr", NagaCourtyardHedgeCornerComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCLn = registerPieceType("TFNCLn", NagaCourtyardHedgeLineComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCT = registerPieceType("TFNCT", NagaCourtyardHedgeTJunctionComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCIs = registerPieceType("TFNCIs", NagaCourtyardHedgeIntersectionComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCPd = registerPieceType("TFNCPd", NagaCourtyardHedgePadderComponent::new);
    public static final RegistryObject<StructurePieceType> TFNCTr = registerPieceType("TFNCTr", CourtyardTerraceBrazier::new);
    public static final RegistryObject<StructurePieceType> TFNCDu = registerPieceType("TFNCDu", CourtyardTerraceDuct::new);
    public static final RegistryObject<StructurePieceType> TFNCSt = registerPieceType("TFNCSt", CourtyardTerraceStatue::new);
    public static final RegistryObject<StructurePieceType> TFNCPa = registerPieceType("TFNCPa", CourtyardPathPiece::new);
    public static final RegistryObject<StructurePieceType> TFNCWl = registerPieceType("TFNCWl", CourtyardWall::new);
    public static final RegistryObject<StructurePieceType> TFNCWP = registerPieceType("TFNCWP", CourtyardWallPadder::new);
    public static final RegistryObject<StructurePieceType> TFNCWC = registerPieceType("TFNCWC", CourtyardWallCornerOuter::new);
    public static final RegistryObject<StructurePieceType> TFNCWA = registerPieceType("TFNCWA", CourtyardWallCornerInner::new);

    // Old Lich Tower
    //public static final IStructurePieceType TFLT = TFFeature.registerPiece("TFLT", StructureStartLichTower::new);
    public static final RegistryObject<StructurePieceType> TFLTBea = registerPieceType("TFLTBea", TowerBeardComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTBA = registerPieceType("TFLTBA", TowerBeardAttachedComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTBri = registerPieceType("TFLTBri", TowerBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTMai = registerPieceType("TFLTMai", TowerMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTOut = registerPieceType("TFLTOut", TowerOutbuildingComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRoo = registerPieceType("TFLTRoo", TowerRoofComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRAS = registerPieceType("TFLTRAS", TowerRoofAttachedSlabComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRF = registerPieceType("TFLTRF", TowerRoofFenceComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRGF = registerPieceType("TFLTRGF", TowerRoofGableForwardsComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRP = registerPieceType("TFLTRP", TowerRoofPointyComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRPO = registerPieceType("TFLTRPO", TowerRoofPointyOverhangComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRS = registerPieceType("TFLTRS", TowerRoofSlabComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRSF = registerPieceType("TFLTRSF", TowerRoofSlabForwardsComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRSt = registerPieceType("TFLTRSt", TowerRoofStairsComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTRStO = registerPieceType("TFLTRStO", TowerRoofStairsOverhangComponent::new);
    public static final RegistryObject<StructurePieceType> TFLTWin = registerPieceType("TFLTWin", TowerWingComponent::new);

    // New Lich Tower
    public static final RegistryObject<StructurePieceType> TOWER_FOYER = registerPieceType("TFLT" + "TFoy", TowerFoyer::new);
    public static final RegistryObject<StructurePieceType> CENTRAL_TOWER = registerPieceType("TFLT" + "CTSeg", CentralTowerSegment::new);
    public static final RegistryObject<StructurePieceType> CENTRAL_TO_SIDE_TOWER = registerPieceType("TFLT" + "C2ST", CentralTowerAttachment::new);
    public static final RegistryObject<StructurePieceType> SIDE_TOWER_ROOM = registerPieceType("TFLT" + "STRm", SideTowerRoom::new);

    // Labyrinth
    //public static final RegistryObject<StructurePieceType> TFLr = registerPieceType("TFLr", StructureStartLabyrinth::new);
    public static final RegistryObject<StructurePieceType> TFMMC = registerPieceType("TFMMC", MazeCorridorComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMCIF = registerPieceType("TFMMCIF", MazeCorridorIronFenceComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMCR = registerPieceType("TFMMCR", MazeCorridorRootsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMCS = registerPieceType("TFMMCS", MazeCorridorShroomsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDE = registerPieceType("TFMMDE", MazeDeadEndComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDEC = registerPieceType("TFMMDEC", MazeDeadEndChestComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDEF = registerPieceType("TFMMDEF", MazeDeadEndFountainComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDEFL = registerPieceType("TFMMDEFL", MazeDeadEndFountainLavaComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDEP = registerPieceType("TFMMDEP", MazeDeadEndPaintingComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDER = registerPieceType("TFMMDER", MazeDeadEndRootsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDES = registerPieceType("TFMMDES", MazeDeadEndShroomsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDET = registerPieceType("TFMMDET", MazeDeadEndTorchesComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDETrC = registerPieceType("TFMMDETrC", MazeDeadEndTrappedChestComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMDETC = registerPieceType("TFMMDETC", MazeDeadEndTripwireChestComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMES = registerPieceType("TFMMES", MazeEntranceShaftComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMMound = registerPieceType("TFMMMound", MazeMoundComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMMR = registerPieceType("TFMMMR", MazeMushRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMR = registerPieceType("TFMMR", MazeRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRB = registerPieceType("TFMMRB", MazeRoomBossComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRC = registerPieceType("TFMMRC", MazeRoomCollapseComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRE = registerPieceType("TFMMRE", MazeRoomExitComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRF = registerPieceType("TFMMRF", MazeRoomFountainComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRSC = registerPieceType("TFMMRSC", MazeRoomSpawnerChestsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRV = registerPieceType("TFMMRV", MazeRoomVaultComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMRuins = registerPieceType("TFMMRuins", MazeRuinsComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMUE = registerPieceType("TFMMUE", MazeUpperEntranceComponent::new);
    public static final RegistryObject<StructurePieceType> TFMMaze = registerPieceType("TFMMaze", MinotaurMazeComponent::new);

    // Knight Stronghold
    //public static final RegistryObject<StructurePieceType> TFKSt = registerPieceType("TFKSt", StructureStartKnightStronghold::new);
    public static final RegistryObject<StructurePieceType> TFSSH = registerPieceType("TFSSH", StrongholdSmallHallwayComponent::new);
    public static final RegistryObject<StructurePieceType> TFSLT = registerPieceType("TFSLT", StrongholdLeftTurnComponent::new);
    public static final RegistryObject<StructurePieceType> TFSCr = registerPieceType("TFSCr", StrongholdCrossingComponent::new);
    public static final RegistryObject<StructurePieceType> TFSRT = registerPieceType("TFSRT", StrongholdRightTurnComponent::new);
    public static final RegistryObject<StructurePieceType> TFSDE = registerPieceType("TFSDE", StrongholdDeadEndComponent::new);
    public static final RegistryObject<StructurePieceType> TFSBalR = registerPieceType("TFSBalR", StrongholdBalconyRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFSTR = registerPieceType("TFSTR", StrongholdTrainingRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFSSS = registerPieceType("TFSSS", StrongholdSmallStairsComponent::new);
    public static final RegistryObject<StructurePieceType> TFSTC = registerPieceType("TFSTC", StrongholdTreasureCorridorComponent::new);
    public static final RegistryObject<StructurePieceType> TFSAt = registerPieceType("TFSAt", StrongholdAtriumComponent::new);
    public static final RegistryObject<StructurePieceType> TFSFo = registerPieceType("TFSFo", StrongholdFoundryComponent::new);
    public static final RegistryObject<StructurePieceType> TFTreaR = registerPieceType("TFTreaR", StrongholdTreasureRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFSBR = registerPieceType("TFSBR", StrongholdBossRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFSAC = registerPieceType("TFSAC", StrongholdAccessChamberComponent::new);
    public static final RegistryObject<StructurePieceType> TFSEnter = registerPieceType("TFSEnter", StrongholdEntranceComponent::new);
    public static final RegistryObject<StructurePieceType> TFSUA = registerPieceType("TFSUA", StrongholdUpperAscenderComponent::new);
    public static final RegistryObject<StructurePieceType> TFSULT = registerPieceType("TFSULT", StrongholdUpperLeftTurnComponent::new);
    public static final RegistryObject<StructurePieceType> TFSURT = registerPieceType("TFSURT", StrongholdUpperRightTurnComponent::new);
    public static final RegistryObject<StructurePieceType> TFSUCo = registerPieceType("TFSUCo", StrongholdUpperCorridorComponent::new);
    public static final RegistryObject<StructurePieceType> TFSUTI = registerPieceType("TFSUTI", StrongholdUpperTIntersectionComponent::new);
    public static final RegistryObject<StructurePieceType> TFSShield = registerPieceType("TFSShield", StrongholdShieldStructure::new);

    // Dark Tower
    //public static final RegistryObject<StructurePieceType> TFDT = registerPieceType("TFDT", StructureStartDarkTower::new);
    public static final RegistryObject<StructurePieceType> TFDTBal = registerPieceType("TFDTBal", DarkTowerBalconyComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTBea = registerPieceType("TFDTBea", DarkTowerBeardComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTBB = registerPieceType("TFDTBB", DarkTowerBossBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTBT = registerPieceType("TFDTBT", DarkTowerBossTrapComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTBri = registerPieceType("TFDTBri", DarkTowerBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTEnt = registerPieceType("TFDTEnt", DarkTowerEntranceComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTEB = registerPieceType("TFDTEB", DarkTowerEntranceBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTMai = registerPieceType("TFDTMai", DarkTowerMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTMB = registerPieceType("TFDTMB", DarkTowerMainBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTRooS = registerPieceType("TFDTRooS", DarkTowerRoofComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTRA = registerPieceType("TFDTRA", DarkTowerRoofAntennaComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTRC = registerPieceType("TFDTRC", DarkTowerRoofCactusComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTRFP = registerPieceType("TFDTRFP", DarkTowerRoofFourPostComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTRR = registerPieceType("TFDTRR", DarkTowerRoofRingsComponent::new);
    public static final RegistryObject<StructurePieceType> TFDTWin = registerPieceType("TFDTWin", DarkTowerWingComponent::new);

    // Aurora Palace
    //public static final RegistryObject<StructurePieceType> TFAP = registerPieceType("TFAP", StructureStartAuroraPalace::new);
    public static final RegistryObject<StructurePieceType> TFITMai = registerPieceType("TFITMai", IceTowerMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFITWin = registerPieceType("TFITWin", IceTowerWingComponent::new);
    public static final RegistryObject<StructurePieceType> TFITRoof = registerPieceType("TFITRoof", IceTowerRoofComponent::new);
    public static final RegistryObject<StructurePieceType> TFITBea = registerPieceType("TFITBea", IceTowerBeardComponent::new);
    public static final RegistryObject<StructurePieceType> TFITBoss = registerPieceType("TFITBoss", IceTowerBossWingComponent::new);
    public static final RegistryObject<StructurePieceType> TFITEnt = registerPieceType("TFITEnt", IceTowerEntranceComponent::new);
    public static final RegistryObject<StructurePieceType> TFITBri = registerPieceType("TFITBri", IceTowerBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFITSt = registerPieceType("TFITSt", IceTowerStairsComponent::new);

    // Troll Cave
    //public static final RegistryObject<StructurePieceType> TFTC = registerPieceType("TFTC", StructureStartTrollCave::new);
    public static final RegistryObject<StructurePieceType> TFTCMai = registerPieceType("TFTCMai", TrollCaveMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFTCCon = registerPieceType("TFTCCon", TrollCaveConnectComponent::new);
    public static final RegistryObject<StructurePieceType> TFTCGard = registerPieceType("TFTCGard", TrollCaveGardenComponent::new);
    public static final RegistryObject<StructurePieceType> TFTCloud = registerPieceType("TFTCloud", TrollCloudComponent::new);
    public static final RegistryObject<StructurePieceType> TFClCa = registerPieceType("TFClCa", CloudCastleComponent::new);
    public static final RegistryObject<StructurePieceType> TFClTr = registerPieceType("TFClTr", CloudTreeComponent::new);
    public static final RegistryObject<StructurePieceType> TFTCVa = registerPieceType("TFTCVa", TrollVaultComponent::new);

    // Final Castle
    //public static final RegistryObject<StructurePieceType> TFFC = registerPieceType("TFFC", StructureStartFinalCastle::new);
    public static final RegistryObject<StructurePieceType> TFFCMain = registerPieceType("TFFCMain", FinalCastleMainComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCStTo = registerPieceType("TFFCStTo", FinalCastleStairTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCLaTo = registerPieceType("TFFCLaTo", FinalCastleLargeTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCMur = registerPieceType("TFFCMur", FinalCastleMuralComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCToF48 = registerPieceType("TFFCToF48", FinalCastleFoundation48Component::new);
    public static final RegistryObject<StructurePieceType> TFFCRo48Cr = registerPieceType("TFFCRo48Cr", FinalCastleRoof48CrenellatedComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCBoGaz = registerPieceType("TFFCBoGaz", FinalCastleBossGazeboComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCSiTo = registerPieceType("TFFCSiTo", FinalCastleMazeTower13Component::new);
    public static final RegistryObject<StructurePieceType> TFFCDunSt = registerPieceType("TFFCDunSt", FinalCastleDungeonStepsComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCDunEn = registerPieceType("TFFCDunEn", FinalCastleDungeonEntranceComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCDunR31 = registerPieceType("TFFCDunR31", FinalCastleDungeonRoom31Component::new);
    public static final RegistryObject<StructurePieceType> TFFCDunEx = registerPieceType("TFFCDunEx", FinalCastleDungeonExitComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCDunBoR = registerPieceType("TFFCDunBoR", FinalCastleDungeonForgeRoomComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCRo9Cr = registerPieceType("TFFCRo9Cr", FinalCastleRoof9CrenellatedComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCRo13Cr = registerPieceType("TFFCRo13Cr", FinalCastleRoof13CrenellatedComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCRo13Con = registerPieceType("TFFCRo13Con", FinalCastleRoof13ConicalComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCRo13Pk = registerPieceType("TFFCRo13Pk", FinalCastleRoof13PeakedComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCEnTo = registerPieceType("TFFCEnTo", FinalCastleEntranceTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCEnSiTo = registerPieceType("TFFCEnSiTo", FinalCastleEntranceSideTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCEnBoTo = registerPieceType("TFFCEnBoTo", FinalCastleEntranceBottomTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCEnSt = registerPieceType("TFFCEnSt", FinalCastleEntranceStairsComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCBelTo = registerPieceType("TFFCBelTo", FinalCastleBellTower21Component::new);
    public static final RegistryObject<StructurePieceType> TFFCBri = registerPieceType("TFFCBri", FinalCastleBridgeComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCToF13 = registerPieceType("TFFCToF13", FinalCastleFoundation13Component::new);
    public static final RegistryObject<StructurePieceType> TFFCBeF21 = registerPieceType("TFFCBeF21", FinalCastleBellFoundation21Component::new);
    public static final RegistryObject<StructurePieceType> TFFCFTh21 = registerPieceType("TFFCFTh21", FinalCastleFoundation13ComponentThorns::new);
    public static final RegistryObject<StructurePieceType> TFFCDamT = registerPieceType("TFFCDamT", FinalCastleDamagedTowerComponent::new);
    public static final RegistryObject<StructurePieceType> TFFCWrT = registerPieceType("TFFCWrT", FinalCastleWreckedTowerComponent::new);

    private static RegistryObject<StructurePieceType> registerPieceType(String name, StructurePieceType structurePieceType) {
        return TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(name.toLowerCase(Locale.ROOT), () -> structurePieceType);
    }
}
