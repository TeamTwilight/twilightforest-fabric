package twilightforest.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;

/**
 * Keeps the old mixin hook harmless while GrassColorModifier is now a real MM
 * enum-subclass extension. Decode and encode both delegate unchanged so
 * {@code twilightforest:*} names round-trip 1:1 on paired modded clients.
 */
public final class TFGrassColorModifierCodec {

    private TFGrassColorModifierCodec() {
    }

    public static Codec<GrassColorModifier> wrap(Codec<GrassColorModifier> vanilla) {
        return new Codec<>() {
            @Override
            public <T> DataResult<Pair<GrassColorModifier, T>> decode(DynamicOps<T> ops, T input) {
                return vanilla.decode(ops, input);
            }

            @Override
            public <T> DataResult<T> encode(GrassColorModifier value, DynamicOps<T> ops, T prefix) {
                return vanilla.encode(value, ops, prefix);
            }
        };
    }
}
