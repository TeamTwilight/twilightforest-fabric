package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

/** 1:1 port of upstream {@code twilightforest.enums.FireJetVariant}. */
public enum FireJetVariant implements StringRepresentable {
	IDLE,
	POPPING,
	FLAME,
	TIMEOUT;

	@Override
	public String toString() {
		return getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
