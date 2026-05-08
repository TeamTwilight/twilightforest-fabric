package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum TowerDeviceVariant implements StringRepresentable {
	BUILDER_INACTIVE,
	BUILDER_ACTIVE,
	BUILDER_TIMEOUT;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
