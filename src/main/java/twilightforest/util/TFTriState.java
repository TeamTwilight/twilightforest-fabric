package twilightforest.util;

/**
 * Fabric 兼容的 TriState 枚举。
 */
public enum TFTriState {
	TRUE,
	FALSE,
	DEFAULT;

	public boolean isDefault() {
		return this == DEFAULT;
	}

	public boolean isTrue() {
		return this == TRUE;
	}

	public boolean isFalse() {
		return this == FALSE;
	}

	public static TFTriState of(boolean value) {
		return value ? TRUE : FALSE;
	}
}