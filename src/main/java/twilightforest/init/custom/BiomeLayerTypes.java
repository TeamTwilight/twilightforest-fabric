package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import twilightforest.TFRegistries;
import twilightforest.TFMain;
import twilightforest.world.components.layer.*;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.SmoothLayer;
import twilightforest.world.components.layer.vanillalegacy.ZoomLayer;

public class BiomeLayerTypes {
	public static final Codec<BiomeLayerType> CODEC = Codec.lazyInitialized(TFRegistries.BIOME_LAYER_TYPE::byNameCodec);

	public static final BiomeLayerType RANDOM_BIOMES = registerType("random_biomes", () -> RandomBiomeLayer.Factory.CODEC);
	public static final BiomeLayerType KEY_BIOMES = registerType("key_biomes", () -> KeyBiomesLayer.Factory.CODEC);
	public static final BiomeLayerType COMPANION_BIOMES = registerType("companion_biomes", () -> CompanionBiomesLayer.Factory.CODEC);
	public static final BiomeLayerType ZOOM = registerType("zoom", () -> ZoomLayer.Factory.CODEC);
	public static final BiomeLayerType STABILIZE = registerType("stabilize", () -> StabilizeLayer.Factory.CODEC);
	public static final BiomeLayerType BORDER = registerType("border", () -> BorderLayer.Factory.CODEC);
	public static final BiomeLayerType SEAM = registerType("seam", () -> SeamLayer.Factory.CODEC);
	public static final BiomeLayerType SMOOTH = registerType("smooth", () -> SmoothLayer.Factory.CODEC);
	public static final BiomeLayerType FILTERED = registerType("filtered", () -> FilteredBiomeLayer.Factory.CODEC);
	public static final BiomeLayerType MEDIAN = registerType("median", () -> MedianLayer.Factory.CODEC);

	private static BiomeLayerType registerType(String name, BiomeLayerType type) {
		return Registry.register(
			TFRegistries.BIOME_LAYER_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing biome layer types...");
	}
}