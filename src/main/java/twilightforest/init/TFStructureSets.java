package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;

import java.util.function.Supplier;

public class TFStructureSets {
    public static final LazyRegistrar<StructureSet> STRUCTURE_SETS = LazyRegistrar.create(BuiltinRegistries.STRUCTURE_SETS, TwilightForestMod.ID);

    public static final RegistryObject<StructureSet> HEDGE_MAZE = register(TFStructures.HEDGE_MAZE, () -> TFLandmark.HEDGE_MAZE);
    public static final RegistryObject<StructureSet> QUEST_GROVE = register(TFStructures.QUEST_GROVE, () -> TFLandmark.QUEST_GROVE);
    public static final RegistryObject<StructureSet> MUSHROOM_TOWER = register(TFStructures.MUSHROOM_TOWER, () -> TFLandmark.MUSHROOM_TOWER);
    public static final RegistryObject<StructureSet> HOLLOW_HILL_SMALL = register(TFStructures.HOLLOW_HILL_SMALL, () -> TFLandmark.SMALL_HILL);
    public static final RegistryObject<StructureSet> HOLLOW_HILL_MEDIUM = register(TFStructures.HOLLOW_HILL_MEDIUM, () -> TFLandmark.MEDIUM_HILL);
    public static final RegistryObject<StructureSet> HOLLOW_HILL_LARGE = register(TFStructures.HOLLOW_HILL_LARGE, () -> TFLandmark.LARGE_HILL);
    public static final RegistryObject<StructureSet> NAGA_COURTYARD = register(TFStructures.NAGA_COURTYARD, () -> TFLandmark.NAGA_COURTYARD);
    public static final RegistryObject<StructureSet> LICH_TOWER = register(TFStructures.LICH_TOWER, () -> TFLandmark.LICH_TOWER);
    public static final RegistryObject<StructureSet> LABYRINTH = register(TFStructures.LABYRINTH, () -> TFLandmark.LABYRINTH);
    public static final RegistryObject<StructureSet> HYDRA_LAIR = register(TFStructures.HYDRA_LAIR, () -> TFLandmark.HYDRA_LAIR);
    public static final RegistryObject<StructureSet> KNIGHT_STRONGHOLD = register(TFStructures.KNIGHT_STRONGHOLD, () -> TFLandmark.KNIGHT_STRONGHOLD);
    public static final RegistryObject<StructureSet> DARK_TOWER = register(TFStructures.DARK_TOWER, () -> TFLandmark.DARK_TOWER);
    public static final RegistryObject<StructureSet> YETI_CAVE = register(TFStructures.YETI_CAVE, () -> TFLandmark.YETI_CAVE);
    public static final RegistryObject<StructureSet> AURORA_PALACE = register(TFStructures.AURORA_PALACE, () -> TFLandmark.ICE_TOWER);
    public static final RegistryObject<StructureSet> TROLL_CAVE = register(TFStructures.TROLL_CAVE, () -> TFLandmark.TROLL_CAVE);
    public static final RegistryObject<StructureSet> FINAL_CASTLE = register(TFStructures.FINAL_CASTLE, () -> TFLandmark.FINAL_CASTLE);

    private static RegistryObject<StructureSet> register(RegistryObject<? extends Structure> structure, Supplier<TFLandmark> landmark) {
        return TFStructureSets.STRUCTURE_SETS.register(structure.getId().getPath(), () -> new StructureSet(structure.getHolder().map(Holder::<Structure>hackyErase).get(), new BiomeForcedLandmarkPlacement(landmark.get(), 256)));
    }
}
