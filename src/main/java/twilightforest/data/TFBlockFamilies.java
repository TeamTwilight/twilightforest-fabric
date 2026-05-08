package twilightforest.data;

import net.minecraft.data.BlockFamily;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TFBlockFamilies {
	private TFBlockFamilies() {
	}

	public static void verifyFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		List<BlockFamily.Variant> missing = findMissingFamilyShapes(family, required);

		if (!missing.isEmpty()) {
			TwilightForestMod.LOGGER.warn("BlockFamily " + family + " for " + family.getBaseBlock() + " is missing variants for " + missing);
		}
	}

	public static List<BlockFamily.Variant> findMissingFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		ArrayList<BlockFamily.Variant> available = new ArrayList<>(Arrays.asList(required));
		available.removeAll(family.getVariants().keySet());
		return available;
	}
}