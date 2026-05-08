package twilightforest.client;

import com.google.common.collect.MapMaker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.HashMap;
import java.util.Map;

public final class FoliageColorHandler {
	private static final BiomeColorAlgorithms BIOME_COLOR_ALGORITHMS = new BiomeColorAlgorithms();
	private static final Map<ResourceKey<Biome>, Handler> REGISTRY = new HashMap<>() {{
		put(TFBiomes.SPOOKY_FOREST, (o, x, z) -> BIOME_COLOR_ALGORITHMS.spookyFoliage(x, z));
		put(TFBiomes.ENCHANTED_FOREST, (o, x, z) -> BIOME_COLOR_ALGORITHMS.enchanted(o, (int) x, (int) z));
		put(TFBiomes.DARK_FOREST_CENTER, (o, x, z) -> BIOME_COLOR_ALGORITHMS.darkForestCenterFoliage(x, z));
		put(TFBiomes.DARK_FOREST, (o, x, z) -> BIOME_COLOR_ALGORITHMS.darkForest(BiomeColorAlgorithms.Type.Foliage));
		put(TFBiomes.SWAMP, (o, x, z) -> BIOME_COLOR_ALGORITHMS.swamp(BiomeColorAlgorithms.Type.Foliage));
	}};
	private static final Map<Biome, Handler> HANDLES = new MapMaker().weakKeys().makeMap();

	private FoliageColorHandler() {
	}

	public static void clearCache() {
		HANDLES.clear();
	}

	public static int get(int original, Biome biome, double x, double z) {
		Handler handler = HANDLES.get(biome);
		if (handler == null) {
			ResourceKey<Biome> key = Minecraft.getInstance().level == null ? null :
					Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biome).orElse(null);
			handler = REGISTRY.getOrDefault(key, Handler.DEFAULT);
			HANDLES.put(biome, handler);
		}
		return handler.apply(original, x, z);
	}

	@FunctionalInterface
	private interface Handler {
		Handler DEFAULT = (original, x, z) -> original;

		int apply(int original, double x, double z);
	}
}
