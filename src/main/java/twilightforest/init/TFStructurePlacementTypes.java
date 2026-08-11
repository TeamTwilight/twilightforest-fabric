package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.TFMain;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

public class TFStructurePlacementTypes {

	public static final StructurePlacementType<LandmarkGridPlacement> GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("landmark_grid", () -> LandmarkGridPlacement.CODEC);
	public static final StructurePlacementType<AvoidLandmarkGridPlacement> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("avoid_landmark_grid", () -> AvoidLandmarkGridPlacement.CODEC);

	private static <P extends StructurePlacement> StructurePlacementType<P> registerPlacer(String name, StructurePlacementType<P> factory) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_PLACEMENT,
			TFMain.prefix(name),
			factory
		);
	}
}