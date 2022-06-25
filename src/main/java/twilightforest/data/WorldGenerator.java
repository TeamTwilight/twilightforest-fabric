package twilightforest.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.slf4j.Logger;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructureSets;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.chunkgenerators.warp.TerrainPoint;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.BiomeKeys;
import twilightforest.world.registration.biomes.BiomeMaker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record WorldGenerator(DataGenerator generator) implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();

	public void run(CachedOutput cache) {
		Path path = this.generator.getOutputFolder();
		RegistryAccess registryaccess = BuiltinRegistries.ACCESS;
		DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, registryaccess);

		Optional<HolderSet<StructureSet>> structureOverrides = this.makeStructureList();

		Registry<LevelStem> twilight = this.registerTFSettings(registryaccess, structureOverrides);
		//Registry<LevelStem> skylight = this.registerSkylightSettings(registryaccess);
		//TODO void world

		WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		Map<ResourceLocation, Biome> biomes = this.getBiomes();
		biomes.forEach((rl, biome) -> biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental()));

		StreamSupport.stream(RegistryAccess.knownRegistries().spliterator(), false)
				.filter(r -> registryaccess.ownedRegistry(r.key()).isPresent() && !r.key().equals(Registry.BIOME_REGISTRY))
				.forEach((data) -> dumpRegistryCap(cache, path, registryaccess, dynamicops, data));

		LOGGER.info("Dumping real BIOME_REGISTRY");
		dumpRegistry(path, cache, dynamicops, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);

		LOGGER.info("Dumping real LEVEL_STEM_REGISTRY");
		dumpRegistry(path, cache, dynamicops, Registry.LEVEL_STEM_REGISTRY, twilight, LevelStem.CODEC);
	}

	private Optional<HolderSet<StructureSet>> makeStructureList() {
		return Optional.of(HolderSet.direct(
				Function.identity(),
				TFStructureSets.STRUCTURE_SETS
						.getEntries()
						.stream()
						.map(RegistryObject::getHolder)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList()
		));
	}

	private static <T> void dumpRegistryCap(CachedOutput cache, Path path, RegistryAccess access, DynamicOps<JsonElement> ops, RegistryAccess.RegistryData<T> data) {
		LOGGER.info("Dumping: {}", data.key());
		dumpRegistry(path, cache, ops, data.key(), access.ownedRegistryOrThrow(data.key()), data.codec());
	}

	private Registry<LevelStem> registerTFSettings(RegistryAccess access, Optional<HolderSet<StructureSet>> structureOverrides) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), -1.25F, 2.5F),
						noiseGenSettings
				);

		writableregistry.register(TFGenerationSettings.WORLDGEN_KEY, new LevelStem(TFDimensionSettings.TWILIGHT_DIM_TYPE.getHolder().get(), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), structureOverrides, noiseGenSettings, true, true, Optional.of(12))), Lifecycle.experimental());
		return writableregistry;
	}

	private Registry<LevelStem> registerSkylightSettings(RegistryAccess access, Optional<HolderSet<StructureSet>> structureOverrides) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(TFDimensionSettings.SKYLIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), -1.25F, 2.5F),
						noiseGenSettings
				);

		writableregistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, TwilightForestMod.prefix("skylight_forest")), new LevelStem(TFDimensionSettings.TWILIGHT_DIM_TYPE.getHolder().get(), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), structureOverrides, noiseGenSettings, true, true, Optional.of(12))), Lifecycle.stable());
		return writableregistry;
	}

	private static <E, T extends Registry<E>> void dumpRegistry(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, ResourceKey<? extends T> key, T registry, Encoder<E> encoder) {
		for (Map.Entry<ResourceKey<E>, E> entry : registry.entrySet()) {
			if (entry.getKey().location().getNamespace().equals(TwilightForestMod.ID)) {
				LOGGER.info("\t\t{}", entry.getKey().location().getPath());
				Path otherPath = createPath(path, key.location(), entry.getKey().location());
				dumpValue(otherPath, cache, ops, encoder, entry.getValue());
			}
		}

	}

	private static <E> void dumpValue(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, Encoder<E> encoder, E entry) {
		try {
			Optional<JsonElement> optional = encoder.encodeStart(ops, entry).resultOrPartial((p_206405_) -> {
				LOGGER.error("Couldn't serialize element {}: {}", path, p_206405_);
			});
			if (optional.isPresent()) {
				if (optional.get().isJsonObject()) {
					JsonObject object = optional.get().getAsJsonObject();
					if (object.has("generator") && object.get("generator").isJsonObject()) {
						JsonObject generator = object.getAsJsonObject("generator");
						if (generator.has("use_overworld_seed")) {
							generator.remove("use_overworld_seed");
							generator.addProperty("use_overworld_seed", true);
						}
						if (generator.has("wrapped_generator")) {
							JsonObject wrapped_generator = generator.getAsJsonObject("wrapped_generator");
							if (wrapped_generator.has("biome_source"))
								wrapped_generator.getAsJsonObject("biome_source").remove("seed");
						}
					}
				}
				DataProvider.saveStable(cache, optional.get(), path);
			}
		} catch (IOException ioexception) {
			LOGGER.error("Couldn't save element {}", path, ioexception);
		}

	}

	private static Path createPath(Path path, ResourceLocation registry, ResourceLocation entry) {
		return path.resolve("data").resolve(entry.getNamespace()).resolve(registry.getPath()).resolve(entry.getPath() + ".json");
	}

	public String getName() {
		return "Twilight Forest Worldgen";
	}

	private Map<ResourceLocation, Biome> getBiomes() {
		return BiomeMaker
				.BIOMES
				.entrySet()
				.stream()
				//.peek(registryKeyBiomeEntry -> ForgeRegistries.BIOMES.register(registryKeyBiomeEntry.getKey().location(), registryKeyBiomeEntry.getValue()))
				.collect(Collectors.toMap(entry -> entry.getKey().location(), Map.Entry::getValue));
	}

	private List<Pair<TerrainPoint, Holder<Biome>>> makeBiomeList(Registry<Biome> registry) {
		return List.of(
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.FOREST),
				pairBiome(registry, 0.1F, 0.2F, BiomeKeys.DENSE_FOREST),
				pairBiome(registry, 0.0625F, 0.05F, BiomeKeys.FIREFLY_FOREST),
				pairBiome(registry, 0.005F, 0.005F, BiomeKeys.CLEARING),
				pairBiome(registry, 0.05F, 0.1F, BiomeKeys.OAK_SAVANNAH),
				pairBiome(registry, -1.65F, 0.25F, BiomeKeys.STREAM),
				pairBiome(registry, -1.97F, 0.0F, BiomeKeys.LAKE),

				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.MUSHROOM_FOREST),
				pairBiome(registry, 0.05F, 0.05F, BiomeKeys.DENSE_MUSHROOM_FOREST),

				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.ENCHANTED_FOREST),
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.SPOOKY_FOREST),

				pairBiome(registry, -0.9F, 0.15F, BiomeKeys.SWAMP),
				pairBiome(registry, -0.2F, 0.05F, BiomeKeys.FIRE_SWAMP),

				pairBiome(registry, 0.025F, 0.005F, BiomeKeys.DARK_FOREST),
				pairBiome(registry, 0.025F, 0.005F, BiomeKeys.DARK_FOREST_CENTER),

				pairBiome(registry, 0.05F, 0.15F, BiomeKeys.SNOWY_FOREST),
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.GLACIER),

				pairBiome(registry, 3.0F, 0.25F, BiomeKeys.HIGHLANDS),
				pairBiome(registry, 7.0F, 0.1F, BiomeKeys.THORNLANDS),
				pairBiome(registry, 13.75F, 0.025F, BiomeKeys.FINAL_PLATEAU)
		);
	}

	private Pair<TerrainPoint, Holder<Biome>> pairBiome(Registry<Biome> registry, float depth, float scale, ResourceKey<Biome> key) {
		return Pair.of(new TerrainPoint(depth, scale), Holder.Reference.createStandAlone(registry, key));
	}
}