package twilightforest.world.components.chunkgenerators.warp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.Float2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectSortedMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import twilightforest.util.Codecs;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public final class TerrainColumn implements Comparable<TerrainColumn> {
    public static final Codec<TerrainColumn> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryFixedCodec.create(Registries.BIOME).fieldOf("key_biome").forGetter(o -> o.keyBiome),
                    Codecs.floatTreeCodec(Biome.CODEC).fieldOf("biome_layers").forGetter(o -> o.biomes),
                    Codec.FLOAT.fieldOf("depth").forGetter(o -> o.noiseDepth),
                    Codec.FLOAT.fieldOf("scale").forGetter(o -> o.noiseScale)
            ).apply(instance, TerrainColumn::new));
    private final ResourceKey<Biome> resourceKey;
    private final Holder<Biome> keyBiome;
    private final Float2ObjectSortedMap<Holder<Biome>> biomes;
    private final float noiseDepth;
    private final float noiseScale;

    public TerrainColumn(Holder<Biome> keyBiome, Float2ObjectSortedMap<Holder<Biome>> biomes, float noiseDepth, float noiseScale) {
        this.keyBiome = keyBiome;
        this.resourceKey = this.keyBiome.unwrapKey().get();
        this.biomes = biomes;
        this.noiseDepth = noiseDepth;
        this.noiseScale = noiseScale;

        if (biomes instanceof Float2ObjectAVLTreeMap<Holder<Biome>> treeMap)
            treeMap.defaultReturnValue(this.keyBiome);
    }

    public Stream<Holder<Biome>> getBiomes() {
        return this.biomes.float2ObjectEntrySet().stream().map(Map.Entry::getValue);
    }

    public boolean is(Holder<Biome> biome) {
        return this.keyBiome.value().equals(biome.value());
    }

    public boolean is(ResourceKey<Biome> biome) {
        return this.keyBiome.is(biome);
    }

    public Holder<Biome> getBiome(int biomeElevation) {
        return this.reduce((a, b) -> {
            float aDelta = a.getFloatKey() - biomeElevation;
            float bDelta = b.getFloatKey() - biomeElevation;

            return Math.abs(aDelta) <= Math.abs(bDelta) ? a : b;
        }, this.keyBiome);
    }

    private Holder<Biome> reduce(BinaryOperator<Float2ObjectMap.Entry<Holder<Biome>>> reducer, Holder<Biome> other) {
        return this.biomes.float2ObjectEntrySet().stream().reduce(reducer).map(Map.Entry::getValue).orElse(other);
    }

    public float depth() {
        return this.noiseDepth;
    }

    public float scale() {
        return this.noiseScale;
    }

    public ResourceKey<Biome> getResourceKey() {
        return this.resourceKey;
    }

    @Override
    public int compareTo(@NotNull TerrainColumn o) {
        return (int) (this.noiseDepth - o.noiseDepth);
    }
}
