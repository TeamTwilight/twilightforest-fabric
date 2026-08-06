package twilightforest.util;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import twilightforest.init.TFBlocks;
import twilightforest.mixin.BoatTypeAccessor;

@SuppressWarnings("all")
public class TFBoatTypes {

	public static Boat.Type TWILIGHT_OAK;
	public static Boat.Type CANOPY;
	public static Boat.Type MANGROVE_TYPE;
	public static Boat.Type DARK;
	public static Boat.Type TIME;
	public static Boat.Type TRANSFORMATION;
	public static Boat.Type MINING;
	public static Boat.Type SORTING;

	public static Boat.Type getOrFallback(Boat.Type type) {
		return type != null ? type : Boat.Type.OAK;
	}

	public static void init() {
		TWILIGHT_OAK = patch("TWILIGHTFOREST_TWILIGHT_OAK", TFBlocks.TWILIGHT_OAK_PLANKS.get());
		CANOPY = patch("TWILIGHTFOREST_CANOPY", TFBlocks.CANOPY_PLANKS.get());
		MANGROVE_TYPE = patch("TWILIGHTFOREST_MANGROVE", TFBlocks.MANGROVE_PLANKS.get());
		DARK = patch("TWILIGHTFOREST_DARK", TFBlocks.DARK_PLANKS.get());
		TIME = patch("TWILIGHTFOREST_TIME", TFBlocks.TIME_PLANKS.get());
		TRANSFORMATION = patch("TWILIGHTFOREST_TRANSFORMATION", TFBlocks.TRANSFORMATION_PLANKS.get());
		MINING = patch("TWILIGHTFOREST_MINING", TFBlocks.MINING_PLANKS.get());
		SORTING = patch("TWILIGHTFOREST_SORTING", TFBlocks.SORTING_PLANKS.get());
	}

	private static Boat.Type patch(String name, Block realPlanks) {
		Boat.Type type = Boat.Type.valueOf(name);
		((BoatTypeAccessor) (Object) type).twilightforest$setPlanks(realPlanks);
		return type;
	}
}