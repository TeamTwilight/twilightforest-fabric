package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;

public class TFStructurePlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registry.STRUCTURE_PLACEMENT_TYPE_REGISTRY, TwilightForestMod.ID);

    public static final RegistryObject<StructurePlacementType<BiomeForcedLandmarkPlacement>> FORCED_LANDMARK_PLACEMENT_TYPE = STRUCTURE_PLACEMENT_TYPES.register("forced_landmark", () -> () -> BiomeForcedLandmarkPlacement.CODEC);
}
