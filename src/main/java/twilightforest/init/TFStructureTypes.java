package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.AuroraPalaceStructure;
import twilightforest.world.components.structures.type.CampStructure;
import twilightforest.world.components.structures.type.DarkTowerStructure;
import twilightforest.world.components.structures.type.FallenTrunkStructure;
import twilightforest.world.components.structures.type.FinalCastleStructure;
import twilightforest.world.components.structures.type.GiantHouseStructure;
import twilightforest.world.components.structures.type.HedgeMazeStructure;
import twilightforest.world.components.structures.type.HollowHillStructure;
import twilightforest.world.components.structures.type.HollowTreeStructure;
import twilightforest.world.components.structures.type.HydraLairStructure;
import twilightforest.world.components.structures.type.KnightStrongholdStructure;
import twilightforest.world.components.structures.type.LabyrinthStructure;
import twilightforest.world.components.structures.type.LandmarkWrappedStructure;
import twilightforest.world.components.structures.type.LichTowerStructure;
import twilightforest.world.components.structures.type.MushroomTowerStructure;
import twilightforest.world.components.structures.type.NagaCourtyardStructure;
import twilightforest.world.components.structures.type.ProgressionWrappedStructure;
import twilightforest.world.components.structures.type.QuestGroveStructure;
import twilightforest.world.components.structures.type.TrollCaveStructure;
import twilightforest.world.components.structures.type.YetiCaveStructure;

public final class TFStructureTypes {
    public static final TFRegistryObject<StructureType<AuroraPalaceStructure>> AURORA_PALACE = structure("aurora_palace", AuroraPalaceStructure.CODEC);
    public static final TFRegistryObject<StructureType<CampStructure>> CAMP = structure("camp", CampStructure.CODEC);
    public static final TFRegistryObject<StructureType<DarkTowerStructure>> DARK_TOWER = structure("dark_tower", DarkTowerStructure.CODEC);
    public static final TFRegistryObject<StructureType<FallenTrunkStructure>> FALLEN_TRUNK = structure("fallen_trunk", FallenTrunkStructure.CODEC);
    public static final TFRegistryObject<StructureType<GiantHouseStructure>> GIANT_HOUSE = structure("giant_house", GiantHouseStructure.CODEC);
    public static final TFRegistryObject<StructureType<HedgeMazeStructure>> HEDGE_MAZE = structure("hedge_maze", HedgeMazeStructure.CODEC);
    public static final TFRegistryObject<StructureType<HollowHillStructure>> HOLLOW_HILL = structure("hollow_hill", HollowHillStructure.CODEC);
    public static final TFRegistryObject<StructureType<HollowTreeStructure>> HOLLOW_TREE = structure("hollow_tree", HollowTreeStructure.CODEC);
    public static final TFRegistryObject<StructureType<HydraLairStructure>> HYDRA_LAIR = structure("hydra_lair", HydraLairStructure.CODEC);
    public static final TFRegistryObject<StructureType<KnightStrongholdStructure>> KNIGHT_STRONGHOLD = structure("knight_stronghold", KnightStrongholdStructure.CODEC);
    public static final TFRegistryObject<StructureType<LabyrinthStructure>> LABYRINTH = structure("labyrinth", LabyrinthStructure.CODEC);
    public static final TFRegistryObject<StructureType<LandmarkWrappedStructure>> LANDMARK_WRAPPED = structure("landmark_wrapped", LandmarkWrappedStructure.CODEC);
    public static final TFRegistryObject<StructureType<LichTowerStructure>> LICH_TOWER = structure("lich_tower", LichTowerStructure.CODEC);
    public static final TFRegistryObject<StructureType<MushroomTowerStructure>> MUSHROOM_TOWER = structure("mushroom_tower", MushroomTowerStructure.CODEC);
    public static final TFRegistryObject<StructureType<NagaCourtyardStructure>> NAGA_COURTYARD = structure("naga_courtyard", NagaCourtyardStructure.CODEC);
    public static final TFRegistryObject<StructureType<FinalCastleStructure>> FINAL_CASTLE = structure("final_castle", FinalCastleStructure.CODEC);
    public static final TFRegistryObject<StructureType<ProgressionWrappedStructure>> PROGRESSION_WRAPPED = structure("progression_wrapped", ProgressionWrappedStructure.CODEC);
    public static final TFRegistryObject<StructureType<QuestGroveStructure>> QUEST_GROVE = structure("quest_grove", QuestGroveStructure.CODEC);
    public static final TFRegistryObject<StructureType<TrollCaveStructure>> TROLL_CAVE = structure("troll_cave", TrollCaveStructure.CODEC);
    public static final TFRegistryObject<StructureType<YetiCaveStructure>> YETI_CAVE = structure("yeti_cave", YetiCaveStructure.CODEC);

    private TFStructureTypes() {
    }

    public static void bootstrap() {
        AURORA_PALACE.get();
        CAMP.get();
        DARK_TOWER.get();
        FALLEN_TRUNK.get();
        GIANT_HOUSE.get();
        HEDGE_MAZE.get();
        HOLLOW_HILL.get();
        HOLLOW_TREE.get();
        HYDRA_LAIR.get();
        KNIGHT_STRONGHOLD.get();
        LABYRINTH.get();
        LANDMARK_WRAPPED.get();
        LICH_TOWER.get();
        MUSHROOM_TOWER.get();
        NAGA_COURTYARD.get();
        FINAL_CASTLE.get();
        PROGRESSION_WRAPPED.get();
        QUEST_GROVE.get();
        TROLL_CAVE.get();
        YETI_CAVE.get();
    }

    private static <S extends Structure> TFRegistryObject<StructureType<S>> structure(String path, MapCodec<S> codec) {
        StructureType<S> type = () -> codec;
        ResourceKey<StructureType<?>> key = ResourceKey.create(BuiltInRegistries.STRUCTURE_TYPE.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        StructureType<S> registered = (StructureType<S>) Registry.register(BuiltInRegistries.STRUCTURE_TYPE, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<StructureType<S>> holder = new TFRegistryObject(registered, key);
        return holder;
    }
}
