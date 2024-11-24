package twilightforest.asm;

import net.minecraft.world.level.biome.Biome;

import static twilightforest.world.components.BiomeGrassColors.blendColors;

public class GrassColorModifierSpookyForest extends GrassColorModifierMixin {
	@Override
	public int modifyColor(double x, double z, int grassColor) {
		double noise = (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
		return blendColors(0xc43323, 0x5BC423, noise > 0.6D ? noise * 0.1D : noise);
	}
}
