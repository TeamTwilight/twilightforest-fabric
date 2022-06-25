package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.surface_rules.TFSurfaceRules;

import java.util.List;
import java.util.OptionalLong;

//does this need a better name? Does it need to be split up, or can we possibly add more dimension related registries here?
public class TFDimensionSettings {

	public static final LazyRegistrar<NoiseGeneratorSettings> NOISE_GENERATORS = LazyRegistrar.create(BuiltinRegistries.NOISE_GENERATOR_SETTINGS, TwilightForestMod.ID);
	public static final LazyRegistrar<DimensionType> DIMENSION_TYPES = LazyRegistrar.create(Registry.DIMENSION_TYPE_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = NOISE_GENERATORS.register("twilight_noise_gen", TFDimensionSettings::tfDefault);
	public static final RegistryObject<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = NOISE_GENERATORS.register("skylight_noise_gen", TFDimensionSettings::skylight);

	public static final RegistryObject<DimensionType> TWILIGHT_DIM_TYPE = DIMENSION_TYPES.register("twilight_forest_type", TFDimensionSettings::twilightDimType);

	private static DimensionType twilightDimType() {
		return new DimensionType(
				OptionalLong.of(13000L), //fixed time TODO Kill the celestial bodies
				true, //skylight
				false, //ceiling
				false, //ultrawarm
				true, //natural
				0.125D, //coordinate scale
				true, //bed works
				true, //respawn anchor works
				-32, // Minimum Y Level
				32 + 256, // Height + Min Y = Max Y
				32 + 256, // Logical Height
				BlockTags.INFINIBURN_OVERWORLD, //infiburn
				TwilightForestMod.prefix("renderer"), // DimensionRenderInfo
				0f, // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
				new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)
		);
	}


	public static NoiseGeneratorSettings tfDefault() {
		NoiseSettings tfNoise = NoiseSettings.create(
				-32, //TODO Deliberate over this. For now it'll be -32
				256,
				1,
				2
		);

		return new NoiseGeneratorSettings(
				tfNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				new NoiseRouter(
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero()
				),
				TFSurfaceRules.tfSurface(),
				List.of(),
				0,
				false,
				false,
				false,
				false
		);
	}

	public static NoiseGeneratorSettings skylight() {
		NoiseSettings skylightNoise = NoiseSettings.create(
				-32, //min height
				256, // height
				2, // size_horizontal
				1 // size_vertical
		);

		// Problem island at /tp 9389.60 90.00 11041.66
		return new NoiseGeneratorSettings(
				// https://misode.github.io/worldgen/noise-settings/
				// So far this looks great! We just need to raise the island levels to sea level. Otherwise is generates flat-flakey islands that really show the roots on dirt bottoms from trees
				skylightNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				new NoiseRouter(
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero()
				),
				TFSurfaceRules.tfSurface(),
				List.of(),
				0,
				false,
				false,
				false,
				false
		);
	}
}
