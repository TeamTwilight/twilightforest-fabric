package twilightforest.asm;

import static twilightforest.world.components.BiomeGrassColors.getEnchantedColor;

public class GrassColorModifierEnchantedForest extends GrassColorModifierMixin {
	@Override
	public int modifyColor(double x, double z, int color) {
		return (color & 0xFFFF00) + getEnchantedColor((int) x, (int) z); //TODO
	}
}
