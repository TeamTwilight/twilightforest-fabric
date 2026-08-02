package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum BanisterShape implements StringRepresentable {
	SHORT,
	TALL,
	CONNECTED;
	// 待重新添加：模型问题已解决，需要 voxel shapes 和 axe 循环集成
	// TILT_RIGHT,
	// TILT_LEFT;

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
