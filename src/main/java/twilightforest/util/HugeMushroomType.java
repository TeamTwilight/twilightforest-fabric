package twilightforest.util;

public enum HugeMushroomType {
	CENTER(true, false, false, false, false, false),
	NORTH(true, false, true, false, false, false),
	SOUTH(true, false, false, true, false, false),
	EAST(true, false, false, false, true, false),
	WEST(true, false, false, false, false, true),
	NORTH_WEST(true, false, true, false, false, true),
	NORTH_EAST(true, false, true, false, true, false),
	SOUTH_WEST(true, false, false, true, false, true),
	SOUTH_EAST(true, false, false, true, true, false);

	private final boolean top;
	private final boolean bottom;
	private final boolean north;
	private final boolean south;
	private final boolean east;
	private final boolean west;

	HugeMushroomType(boolean t, boolean b, boolean n, boolean s, boolean e, boolean w) {
		this.top = t;
		this.bottom = b;
		this.north = n;
		this.south = s;
		this.east = e;
		this.west = w;
	}

	public boolean isTop() {
		return top;
	}

	public boolean isBottom() {
		return bottom;
	}

	public boolean isNorth() {
		return north;
	}

	public boolean isSouth() {
		return south;
	}

	public boolean isEast() {
		return east;
	}

	public boolean isWest() {
		return west;
	}
}