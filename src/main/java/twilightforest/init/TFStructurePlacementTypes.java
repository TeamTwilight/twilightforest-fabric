package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;

public class TFStructurePlacementTypes {
    public static final LazyRegistrar<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = LazyRegistrar.create(Registry.STRUCTURE_PLACEMENT_TYPE, TwilightForestMod.ID);

    public static final RegistryObject<StructurePlacementType<BiomeForcedLandmarkPlacement>> FORCED_LANDMARK_PLACEMENT_TYPE = STRUCTURE_PLACEMENT_TYPES.register("forced_landmark", () -> () -> BiomeForcedLandmarkPlacement.CODEC);
}
