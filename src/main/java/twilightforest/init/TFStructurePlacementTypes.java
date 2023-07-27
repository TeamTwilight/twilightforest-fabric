package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;

import java.util.function.Supplier;

public class TFStructurePlacementTypes {
	public static final LazyRegistrar<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = LazyRegistrar.create(Registries.STRUCTURE_PLACEMENT, TwilightForestMod.ID);

	public static final RegistryObject<StructurePlacementType<BiomeForcedLandmarkPlacement>> FORCED_LANDMARK_PLACEMENT_TYPE = registerPlacer("forced_landmark", () -> () -> BiomeForcedLandmarkPlacement.CODEC);

    private static <P extends StructurePlacement> RegistryObject<StructurePlacementType<P>> registerPlacer(String name, Supplier<StructurePlacementType<P>> factory) {
        return STRUCTURE_PLACEMENT_TYPES.register(name, factory);
    }
}
