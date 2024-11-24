package twilightforest.asm;

import net.minecraft.world.level.GrassColor;

public class GrassColorModifierDarkForest extends GrassColorModifierMixin {
	@Override
	public int modifyColor(double x, double z, int grassColor) {
		return ((GrassColor.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E) / 2;
	}
}
