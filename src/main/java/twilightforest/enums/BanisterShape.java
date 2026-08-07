package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum BanisterShape implements StringRepresentable {
	SHORT,
	TALL,
	CONNECTED;
	// TILT_RIGHT,
	// TILT_LEFT;

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
