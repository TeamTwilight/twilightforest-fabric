package twilightforest.world.components;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

/**
 * Helper math for the 5 Twilight Forest custom {@code GrassColorModifier} entries.
 *
 * <p>The actual enum values are added at runtime by {@link twilightforest.asm.CodexGrassColorEarlyRiser}
 * via Manningham Mills {@code ClassTinkerers.enumBuilder}. This class only exposes the
 * pure-math helpers used by the enum-subclass struct classes, so we keep the formulas
 * 1:1 with NeoForge Twilight Forest while staying server-side only.</p>
 */
public final class BiomeGrassColors {

    private BiomeGrassColors() {
    }

    public static int enchanted(int originalColor, int x, int z) {
        BlockPos center = LegacyLandmarkPlacements.getNearestCenterXZ(x / 16, z / 16);
        int cx = center.getX();
        int cz = center.getZ();

        int dist = (int) Mth.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
        int color = dist * 16;
        color %= 512;

        if (color > 255) {
            color = 511 - color;
        }

        color = 255 - color;
        return (originalColor & 0xFFFF00) + color;
    }

    public static int getEnchantedColor(int x, int z) {
        return enchanted(0, x, z) & 0xFF;
    }

    public static int swamp(Type modifierType) {
        int modifiedColor = switch (modifierType) {
            case GRASS -> GrassColor.get(0.8F, 0.9F);
            case FOLIAGE -> FoliageColor.get(0.8F, 0.9F);
        };
        return ((modifiedColor & 0xFEFEFE) + 0x4E0E4E) / 2;
    }

    public static int darkForest(Type modifierType) {
        int modifiedColor = switch (modifierType) {
            case GRASS -> GrassColor.get(0.7F, 0.8F);
            case FOLIAGE -> FoliageColor.get(0.7F, 0.8F);
        };
        return ((modifiedColor & 0xFEFEFE) + 0x1E0E4E) / 2;
    }

    public static int darkForestCenterGrass(double x, double z) {
        double noise = Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
        return noise < -0.2D ? 0x667540 : 0x554114;
    }

    public static double spookyNoise(double x, double z) {
        return (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
    }

    public static int spookyGrass(double x, double z) {
        double noise = spookyNoise(x, z);
        return blendColors(0xc43323, 0x5BC423, noise > 0.6D ? noise * 0.1D : noise);
    }

    public static int blendColors(int a, int b, double ratio) {
        int mask1 = 0x00FF00FF;
        int mask2 = 0xFF00FF00;

        int f2 = (int) (256 * ratio);
        int f1 = 256 - f2;

        return (((((a & mask1) * f1) + ((b & mask1) * f2)) >> 8) & mask1)
                | (((((a & mask2) * f1) + ((b & mask2) * f2)) >> 8) & mask2);
    }

    public enum Type {
        GRASS,
        FOLIAGE
    }
}
