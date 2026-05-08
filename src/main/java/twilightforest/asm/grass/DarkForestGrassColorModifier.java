package twilightforest.asm.grass;

import twilightforest.world.components.BiomeGrassColors;

public final class DarkForestGrassColorModifier extends GrassColorModifierStructMixin {
    public int modifyColor(double x, double z, int color) {
        return BiomeGrassColors.darkForest(BiomeGrassColors.Type.GRASS);
    }
}
