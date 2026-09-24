package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import twilightforest.TFCommon;
import twilightforest.world.components.chunkgenerators.*;

@SuppressWarnings("unused")
public class TFDensityFunctions {

	public static final MapCodec<TerrainDensityRouter> BIOME_DRIVEN_TERRAIN = register("biome_driven_terrain", TerrainDensityRouter.CODEC);
	public static final MapCodec<NoiseDensityRouter> BIOME_DRIVEN_NOISE = register("biome_driven_noise", NoiseDensityRouter.CODEC);
	public static final MapCodec<FocusedDensityFunction> FOCUSED = register("focused", FocusedDensityFunction.CODEC);
	public static final MapCodec<HollowHillFunction> HOLLOW_HILL = register("hollow_hill", HollowHillFunction.CODEC);
	public static final MapCodec<AbsoluteDifferenceFunction.Min> COORD_MIN = register("coord_min", AbsoluteDifferenceFunction.Min.CODEC);
	public static final MapCodec<AbsoluteDifferenceFunction.Max> COORD_MAX = register("coord_max", AbsoluteDifferenceFunction.Max.CODEC);
	public static final MapCodec<SqrtDensityFunction> SQRT = register("sqrt", SqrtDensityFunction.CODEC);

	public static final ResourceKey<DensityFunction> BIOME_TERRAIN_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TFCommon.prefix("raw_biome_terrain"));
	public static final ResourceKey<DensityFunction> BIOME_NOISE_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TFCommon.prefix("raw_biome_noise"));
	public static final ResourceKey<DensityFunction> FORESTED_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TFCommon.prefix("forested_terrain"));
	public static final ResourceKey<DensityFunction> SKYLIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TFCommon.prefix("skylight_terrain"));

	private static <T extends DensityFunction> MapCodec<T> register(String name, MapCodec<T> keyCodec) {
		return Registry.register(
			BuiltInRegistries.DENSITY_FUNCTION_TYPE,
			TFCommon.prefix(name),
			keyCodec
		);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing density function types...");
	}
}
