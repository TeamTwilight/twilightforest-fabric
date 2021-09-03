package twilightforest.world.components;

import net.minecraft.util.Mth;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import twilightforest.TwilightForestMod;

import java.util.Random;

public class BiomeGrassColors {
	private static final Random COLOR_RNG = new Random();
	public static int getEnchantedColor(int x, int z) {
		// center of the biome is at % 256 - 8
		int cx = 256 * Math.round((x - 8) / 256F) + 8;
		int cz = 256 * Math.round((z - 8) / 256F) + 8;

		int dist = (int) Mth.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
		int color = dist * 64;
		color %= 512;

		if (color > 255) {
			color = 511 - color;
		}

		color = 255 - color;

		// FIXME Biome colors are cached on chunk model build since like 1.7, we should be doing this differently. Maybe perlin based
		int randomFlicker = COLOR_RNG.nextInt(32) - 16;

		if (0 < color + randomFlicker && color + randomFlicker > 255) {
			color += randomFlicker;
		}

		return color;
	}

	//public static final GrassColorModifier ENCHANTED_FOREST = GrassColorModifier.valueOf(TwilightForestMod.prefix("enchanted_forest").toString());

	// FIXME Flat color, resolve
	//public static final GrassColorModifier SWAMP = GrassColorModifier.valueOf(TwilightForestMod.prefix("swamp").toString());
	// FIXME Flat color, resolve
	//public static final GrassColorModifier DARK_FOREST = GrassColorModifier.valueOf(TwilightForestMod.prefix("dark_forest").toString());
	//public static final GrassColorModifier DARK_FOREST_CENTER = GrassColorModifier.valueOf(TwilightForestMod.prefix("dark_forest_center").toString());

}
