package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

public final class TFStructurePlacementTypes {
    public static final TFRegistryObject<StructurePlacementType<LandmarkGridPlacement>> GRID_LANDMARK_PLACEMENT_TYPE = placement("landmark_grid", () -> LandmarkGridPlacement.CODEC);
    public static final TFRegistryObject<StructurePlacementType<LandmarkGridPlacement>> FORCED_LANDMARK_PLACEMENT_TYPE = placement("forced_landmark", () -> LandmarkGridPlacement.CODEC);
    public static final TFRegistryObject<StructurePlacementType<AvoidLandmarkGridPlacement>> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = placement("avoid_landmark_grid", () -> AvoidLandmarkGridPlacement.CODEC);

    private TFStructurePlacementTypes() {
    }

    public static void bootstrap() {
        GRID_LANDMARK_PLACEMENT_TYPE.get();
        FORCED_LANDMARK_PLACEMENT_TYPE.get();
        AVOID_GRID_LANDMARK_PLACEMENT_TYPE.get();
    }

    private static <P extends StructurePlacement> TFRegistryObject<StructurePlacementType<P>> placement(String path, StructurePlacementType<P> type) {
        ResourceKey<StructurePlacementType<?>> key = ResourceKey.create(BuiltInRegistries.STRUCTURE_PLACEMENT.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        StructurePlacementType<P> registered = (StructurePlacementType<P>) Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, key.location(), type);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<StructurePlacementType<P>> holder = new TFRegistryObject(registered, key);
        return holder;
    }
}
