package twilightforest.asm.grass;

import twilightforest.world.components.BiomeGrassColors;

public final class DarkForestCenterGrassColorModifier extends GrassColorModifierStructMixin {
    public int modifyColor(double x, double z, int color) {
        return BiomeGrassColors.darkForestCenterGrass(x, z);
    }
}
