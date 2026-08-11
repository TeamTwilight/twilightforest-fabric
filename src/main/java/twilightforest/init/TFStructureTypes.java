package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.TFMain;
import twilightforest.world.components.structures.type.*;

public class TFStructureTypes {

	public static final StructureType<LandmarkWrappedStructure> LANDMARK_WRAPPED = registerType("landmark", () -> LandmarkWrappedStructure.CODEC);
	public static final StructureType<ProgressionWrappedStructure> PROGRESSION_WRAPPED = registerType("progression", () -> ProgressionWrappedStructure.CODEC);
	public static final StructureType<FallenTrunkStructure> FALLEN_TRUNK = registerType("fallen_trunk", () -> FallenTrunkStructure.CODEC);
	public static final StructureType<HollowTreeStructure> HOLLOW_TREE = registerType("hollow_tree", () -> HollowTreeStructure.CODEC);
	public static final StructureType<CampStructure> CAMP = registerType("camp", () -> CampStructure.CODEC);
	public static final StructureType<HedgeMazeStructure> HEDGE_MAZE = registerType("hedge_maze", () -> HedgeMazeStructure.CODEC);
	public static final StructureType<HollowHillStructure> HOLLOW_HILL = registerType("hollow_hill", () -> HollowHillStructure.CODEC);
	public static final StructureType<QuestGroveStructure> QUEST_GROVE = registerType("quest_grove", () -> QuestGroveStructure.CODEC);
	public static final StructureType<MushroomTowerStructure> MUSHROOM_TOWER = registerType("mushroom_tower", () -> MushroomTowerStructure.CODEC);
	public static final StructureType<NagaCourtyardStructure> NAGA_COURTYARD = registerType("naga_courtyard", () -> NagaCourtyardStructure.CODEC);
	public static final StructureType<LichTowerStructure> LICH_TOWER = registerType("lich_tower", () -> LichTowerStructure.CODEC);
	public static final StructureType<LabyrinthStructure> LABYRINTH = registerType("labyrinth", () -> LabyrinthStructure.CODEC);
	public static final StructureType<HydraLairStructure> HYDRA_LAIR = registerType("hydra_lair", () -> HydraLairStructure.CODEC);
	public static final StructureType<KnightStrongholdStructure> KNIGHT_STRONGHOLD = registerType("knight_stronghold", () -> KnightStrongholdStructure.CODEC);
	public static final StructureType<DarkTowerStructure> DARK_TOWER = registerType("dark_tower", () -> DarkTowerStructure.CODEC);
	public static final StructureType<YetiCaveStructure> YETI_CAVE = registerType("yeti_cave", () -> YetiCaveStructure.CODEC);
	public static final StructureType<AuroraPalaceStructure> AURORA_PALACE = registerType("aurora_palace", () -> AuroraPalaceStructure.CODEC);
	public static final StructureType<TrollCaveStructure> TROLL_CAVE = registerType("troll_cave", () -> TrollCaveStructure.CODEC);
	public static final StructureType<GiantHouseStructure> GIANT_HOUSE = registerType("giant_house", () -> GiantHouseStructure.CODEC);
	public static final StructureType<FinalCastleStructure> FINAL_CASTLE = registerType("final_castle", () -> FinalCastleStructure.CODEC);

	private static <P extends Structure> StructureType<P> registerType(String name, StructureType<P> factory) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_TYPE,
			TFMain.prefix(name),
			factory
		);
	}
}
