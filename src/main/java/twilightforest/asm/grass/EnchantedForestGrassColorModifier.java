package twilightforest.asm.grass;

import twilightforest.world.components.BiomeGrassColors;

public final class EnchantedForestGrassColorModifier extends GrassColorModifierStructMixin {
    public int modifyColor(double x, double z, int color) {
        return BiomeGrassColors.enchanted(color, (int) x, (int) z);
    }
}
