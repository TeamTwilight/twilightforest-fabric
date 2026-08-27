package twilightforest.client;

import com.google.common.collect.MapMaker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.HashMap;
import java.util.Map;

public final class FoliageColorHandler {

	public static final FoliageColorHandler INSTANCE = new FoliageColorHandler();

	private final BiomeColorAlgorithms biomeColorAlgorithms = BiomeColorAlgorithms.INSTANCE;

	private final Map<ResourceKey<Biome>, Handler> REGISTRY = new HashMap<>() {{
		put(TFBiomes.SPOOKY_FOREST, (o, x, z) -> biomeColorAlgorithms.spookyFoliage(x, z));
		put(TFBiomes.ENCHANTED_FOREST, (o, x, z) -> biomeColorAlgorithms.enchanted(o, (int) x, (int) z));
		put(TFBiomes.DARK_FOREST_CENTER, (o, x, z) -> biomeColorAlgorithms.darkForestCenterFoliage(x, z));
		put(TFBiomes.DARK_FOREST, (o, x, z) -> biomeColorAlgorithms.darkForest(BiomeColorAlgorithms.Type.Foliage));
		put(TFBiomes.SWAMP, (o, x, z) -> biomeColorAlgorithms.swamp(BiomeColorAlgorithms.Type.Foliage));
	}};

	private final Map<Biome, Handler> HANDLES = new MapMaker().weakKeys().makeMap(); // Concurrent + Weak + Hash

	public void init() {
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> HANDLES.clear());
	}

	public int get(int o, Biome biome, double x, double z) {
		Handler handler = HANDLES.get(biome);
		if (handler == null) {
			handler = REGISTRY.getOrDefault(
				Minecraft.getInstance().level == null ? null :
					Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME).getResourceKey(biome).orElse(null),
				Handler.DEFAULT);
			HANDLES.put(biome, handler);
		}
		return handler.apply(o, x, z);
	}

	@FunctionalInterface
	private interface Handler {
		Handler DEFAULT = (o, x, z) -> o;

		int apply(int o, double x, double z);
	}
}
