package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.biomes.BiomeMaker;
import twilightforest.world.registration.surface_rules.TFSurfaceRules;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class TFDimensionSettings {

	public static long seed; //Minecraft Overworld seed - used for seed ASM

	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("skylight_noise_gen"));

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM =  ResourceKey.create(Registries.LEVEL_STEM, TwilightForestMod.prefix("twilight_forest"));

	private static DimensionType twilightDimType() {
		return new DimensionType(
				OptionalLong.of(13000L), //fixed time
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

	public static void bootstrapNoise(BootstapContext<NoiseGeneratorSettings> context) {
		context.register(TWILIGHT_NOISE_GEN, tfDefault());
		context.register(SKYLIGHT_NOISE_GEN, skylight());
	}

	public static void bootstrapType(BootstapContext<DimensionType> context) {
		context.register(TWILIGHT_DIM_TYPE, twilightDimType());
	}

	public static void bootstrapStem(BootstapContext<LevelStem> context) {
		HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

		NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
				new TFBiomeProvider(
						BiomeMaker.makeBiomeList(biomeRegistry, biomeRegistry.getOrThrow(TFBiomes.UNDERGROUND)),
						-1.25F,
						2.5F,
						context.lookup(BiomeLayerStack.BIOME_STACK_KEY).getOrThrow(BiomeLayerStack.BIOMES_ALONG_STREAMS)
				),
				noiseGenSettings.getOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN));

		LevelStem stem = new LevelStem(
				dimTypes.getOrThrow(TFDimensionSettings.TWILIGHT_DIM_TYPE),
				new ChunkGeneratorTwilight(
						wrappedChunkGenerator,
						noiseGenSettings.getOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN),
						true,
						Optional.of(19),
						BiomeMaker.BIOME_FEATURES_SETS));

		context.register(TWILIGHT_LEVEL_STEM, stem);
	}
}
