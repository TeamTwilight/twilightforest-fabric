package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum HugeLilypadPiece implements StringRepresentable {
	NW,
	NE,
	SE,
	SW;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
