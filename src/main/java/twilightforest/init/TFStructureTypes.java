package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.*;

import java.util.function.Supplier;

public class TFStructureTypes {
	public static final LazyRegistrar<StructureType<?>> STRUCTURE_TYPES = LazyRegistrar.create(Registries.STRUCTURE_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<StructureType<LegacyStructure>> LEGACY_LANDMARK = registerType("legacy_landmark", () -> () -> LegacyStructure.CODEC);

	public static final RegistryObject<StructureType<HedgeMazeStructure>> HEDGE_MAZE = registerType("hedge_maze", () -> () -> HedgeMazeStructure.CODEC);
	public static final RegistryObject<StructureType<HollowHillStructure>> HOLLOW_HILL = registerType("hollow_hill", () -> () -> HollowHillStructure.CODEC);
	public static final RegistryObject<StructureType<QuestGroveStructure>> QUEST_GROVE = registerType("quest_grove", () -> () -> QuestGroveStructure.CODEC);
	public static final RegistryObject<StructureType<MushroomTowerStructure>> MUSHROOM_TOWER = registerType("mushroom_tower", () -> () -> MushroomTowerStructure.CODEC);
	public static final RegistryObject<StructureType<NagaCourtyardStructure>> NAGA_COURTYARD = registerType("naga_courtyard", () -> () -> NagaCourtyardStructure.CODEC);
	public static final RegistryObject<StructureType<LichTowerStructure>> LICH_TOWER = registerType("lich_tower", () -> () -> LichTowerStructure.CODEC);
	public static final RegistryObject<StructureType<LabyrinthStructure>> LABYRINTH = registerType("labyrinth", () -> () -> LabyrinthStructure.CODEC);
	public static final RegistryObject<StructureType<HydraLairStructure>> HYDRA_LAIR = registerType("hydra_lair", () -> () -> HydraLairStructure.CODEC);
	public static final RegistryObject<StructureType<KnightStrongholdStructure>> KNIGHT_STRONGHOLD = registerType("knight_stronghold", () -> () -> KnightStrongholdStructure.CODEC);
	public static final RegistryObject<StructureType<DarkTowerStructure>> DARK_TOWER = registerType("dark_tower", () -> () -> DarkTowerStructure.CODEC);
	public static final RegistryObject<StructureType<YetiCaveStructure>> YETI_CAVE = registerType("yeti_cave", () -> () -> YetiCaveStructure.CODEC);
	public static final RegistryObject<StructureType<AuroraPalaceStructure>> AURORA_PALACE = registerType("aurora_palace", () -> () -> AuroraPalaceStructure.CODEC);
	public static final RegistryObject<StructureType<TrollCaveStructure>> TROLL_CAVE = registerType("troll_cave", () -> () -> TrollCaveStructure.CODEC);
	public static final RegistryObject<StructureType<FinalCastleStructure>> FINAL_CASTLE = registerType("final_castle", () -> () -> FinalCastleStructure.CODEC);

	private static <P extends Structure> RegistryObject<StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
		return STRUCTURE_TYPES.register(name, factory);
	}
}
