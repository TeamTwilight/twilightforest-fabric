package twilightforest.util;

import net.minecraft.core.Direction;

/**
 * 1:1 port of upstream {@code twilightforest.util.DirectionUtil} (NeoForge → Fabric:
 * upstream used the {@code tamaized.beanification} {@code @Component} DI to provide a
 * single shared instance; codex doesn't pull beanification, so the helpers are exposed
 * as static methods directly. Behaviour is byte-identical — only the call style changes
 * from {@code directionUtil.horizontalOrElse(...)} to {@code DirectionUtil.horizontalOrElse(...)}.).
 */
public final class DirectionUtil {

	public static Direction horizontalOrElse(Direction horizontal, Direction orElse) {
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

	private DirectionUtil() {
	}
}
