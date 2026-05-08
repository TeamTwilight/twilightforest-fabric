package twilightforest.util;

import net.minecraft.core.Direction;

/**
 * 1:1 port of upstream {@code twilightforest.util.AxisUtil}.
 */
public final class AxisUtil {
	public static Direction getAxisDirectionPositive(Direction.Axis axis) {
		return switch (axis) {
			case X -> Direction.EAST;
			case Y -> Direction.UP;
			case Z -> Direction.SOUTH;
		};
	}

	public static Direction getAxisDirectionNegative(Direction.Axis axis) {
		return switch (axis) {
			case X -> Direction.WEST;
			case Y -> Direction.DOWN;
			case Z -> Direction.NORTH;
		};
	}

	private AxisUtil() {
	}
}
