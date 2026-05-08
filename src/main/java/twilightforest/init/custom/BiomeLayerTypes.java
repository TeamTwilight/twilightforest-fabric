package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRegistryObject;
import twilightforest.world.components.layer.BorderLayer;
import twilightforest.world.components.layer.CompanionBiomesLayer;
import twilightforest.world.components.layer.FilteredBiomeLayer;
import twilightforest.world.components.layer.KeyBiomesLayer;
import twilightforest.world.components.layer.MedianLayer;
import twilightforest.world.components.layer.RandomBiomeLayer;
import twilightforest.world.components.layer.SeamLayer;
import twilightforest.world.components.layer.StabilizeLayer;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.SmoothLayer;
import twilightforest.world.components.layer.vanillalegacy.ZoomLayer;

/**
 * Fabric translation of TF's NeoForge {@code BiomeLayerTypes} (DeferredRegister/DeferredHolder pattern):
 * we register each {@link BiomeLayerType} into {@link TFRegistries#BIOME_LAYER_TYPE} via vanilla
 * {@link Registry#register} and expose a lazy dispatch {@link Codec}.
 */
public final class BiomeLayerTypes {
    public static final Codec<BiomeLayerType> CODEC = Codec.lazyInitialized(TFRegistries.BIOME_LAYER_TYPE::byNameCodec);

    public static final TFRegistryObject<BiomeLayerType> RANDOM_BIOMES = registerType("random_biomes", () -> RandomBiomeLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> KEY_BIOMES = registerType("key_biomes", () -> KeyBiomesLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> COMPANION_BIOMES = registerType("companion_biomes", () -> CompanionBiomesLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> ZOOM = registerType("zoom", () -> ZoomLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> STABILIZE = registerType("stabilize", () -> StabilizeLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> BORDER = registerType("border", () -> BorderLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> SEAM = registerType("seam", () -> SeamLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> SMOOTH = registerType("smooth", () -> SmoothLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> FILTERED = registerType("filtered", () -> FilteredBiomeLayer.Factory.CODEC);
    public static final TFRegistryObject<BiomeLayerType> MEDIAN = registerType("median", () -> MedianLayer.Factory.CODEC);

    private BiomeLayerTypes() {
    }

    public static void bootstrap() {
        RANDOM_BIOMES.get();
        KEY_BIOMES.get();
        COMPANION_BIOMES.get();
        ZOOM.get();
        STABILIZE.get();
        BORDER.get();
        SEAM.get();
        SMOOTH.get();
        FILTERED.get();
        MEDIAN.get();
    }

    private static TFRegistryObject<BiomeLayerType> registerType(String name, BiomeLayerType type) {
        BiomeLayerType registered = Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix(name), type);
        return new TFRegistryObject<>(registered);
    }
}
