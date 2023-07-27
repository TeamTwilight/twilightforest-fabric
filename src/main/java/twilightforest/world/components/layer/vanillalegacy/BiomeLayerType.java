package twilightforest.world.components.layer.vanillalegacy;

import com.mojang.serialization.Codec;

@FunctionalInterface
public interface BiomeLayerType {
    Codec<? extends BiomeLayerFactory> getCodec();
}
