package twilightforest.asm;

import net.minecraft.world.level.GrassColor;

public class GrassColorModifierSwamp extends GrassColorModifierMixin {
	@Override
	public int modifyColor(double x, double z, int grassColor) {
		return ((GrassColor.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2;
	}
}
