package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import twilightforest.TwilightForestMod;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilightForest;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilightSky;
import twilightforest.world.components.TFBiomeDistributor;
import twilightforest.world.components.TFBiomeProvider;

public class TFDimensions {
	// Find a different way to validate if a world is passible as a "Twilight Forest" instead of hardcoding Dim ID (Instanceof check for example)
	//public static final RegistryKey<World> twilightForest = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get()));

	public static void init() {
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("smart_distribution"), TFBiomeDistributor.TF_CODEC);
		// TODO legacy
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("grid"), TFBiomeProvider.TF_CODEC);

		// For now use minecraft:noise until we need to terraform for features
		Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("featured_noise"), ChunkGeneratorTwilightForest.CODEC);
		// TODO Do we even need this? Or can we fold it into the featured_noise because its elasticity to handle skyworld generation
		Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("sky_noise"), ChunkGeneratorTwilightSky.CODEC);
	}
}