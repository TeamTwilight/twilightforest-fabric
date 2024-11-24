package twilightforest.asm;

import net.minecraft.world.level.biome.Biome;

public class GrassColorModifierDarkForestCenter extends GrassColorModifierMixin {
	@Override
	public int modifyColor(double x, double z, int grassColor) {
		double d0 = Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false); //TODO: Check
		return d0 < -0.2D ? 0x667540 : 0x554114;
	}
}
