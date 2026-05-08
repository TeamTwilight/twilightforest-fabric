package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.BiomeSource;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.biomesources.TFBiomeProvider;

/**
 * Registers TF custom BiomeSource codec types into BuiltInRegistries.BIOME_SOURCE.
 *
 * Required so that dimension JSONs with {@code "biome_source": {"type": "twilightforest:twilight_biomes", ...}}
 * resolve to {@link TFBiomeProvider}, which queries our ported BiomeDensitySource for biome assignment
 * via the vanillalegacy layer system instead of vanilla MultiNoise.
 */
public final class TFBiomeSources {
    public static final TFRegistryObject<MapCodec<? extends BiomeSource>> TWILIGHT_BIOMES =
            register("twilight_biomes", TFBiomeProvider.TF_CODEC);

    private TFBiomeSources() {
    }

    public static void bootstrap() {
        TWILIGHT_BIOMES.get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static TFRegistryObject<MapCodec<? extends BiomeSource>> register(String path, MapCodec<? extends BiomeSource> codec) {
        MapCodec<? extends BiomeSource> registered = (MapCodec<? extends BiomeSource>) Registry.register(
                BuiltInRegistries.BIOME_SOURCE,
                TwilightForestMod.prefix(path),
                (MapCodec) codec);
        return new TFRegistryObject(registered);
    }
}
