package twilightforest.util;

import net.minecraft.core.Direction;

public class DirectionUtil {
	public static final DirectionUtil INSTANCE = new DirectionUtil();

	public Direction horizontalOrElse(Direction horizontal, Direction orElse) {
		return horizontal.getAxis().isHorizontal() ? horizontal : horizontalOrElse(orElse, Direction.NORTH);
	}

	public static Direction fromStringOrElse(String label, Direction orElse) {
		return switch (label) {
			case "up" -> Direction.UP;
			case "down" -> Direction.DOWN;
			case "north" -> Direction.NORTH;
			case "south" -> Direction.SOUTH;
			case "west" -> Direction.WEST;
			case "east" -> Direction.EAST;
			default -> orElse;
		};
	}
}
