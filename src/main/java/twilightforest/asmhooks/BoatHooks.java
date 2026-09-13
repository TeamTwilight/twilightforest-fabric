package twilightforest.asmhooks;

import net.minecraft.world.entity.vehicle.Boat;
import twilightforest.util.TFBoatTypes;

import java.util.Map;

public final class BoatHooks {
	public static final Map<Boat.Type, String> TWILIGHTFOREST_BOAT_TEXTURES = Map.of(
		TFBoatTypes.TWILIGHT_OAK, "twilight_oak",
		TFBoatTypes.CANOPY, "canopy",
		TFBoatTypes.MANGROVE_TYPE, "mangrove",
		TFBoatTypes.DARK, "dark",
		TFBoatTypes.TIME, "time",
		TFBoatTypes.TRANSFORMATION, "transformation",
		TFBoatTypes.MINING, "mining",
		TFBoatTypes.SORTING, "sorting"
	);
}