package twilightforest.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
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
import twilightforest.init.BiomeKeys;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureSets;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.biomes.BiomeMaker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record WorldGenerator(DataGenerator generator) implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final Map<ResourceKey<Biome>, ImmutableSet<TFLandmark>> BIOME_FEATURES_SETS = new ImmutableMap.Builder<ResourceKey<Biome>, ImmutableSet<TFLandmark>>()
			//.put(BiomeKeys.DENSE_MUSHROOM_FOREST, Set.of(MUSHROOM_TOWER))
			.put(BiomeKeys.ENCHANTED_FOREST, ImmutableSet.of(TFLandmark.QUEST_GROVE))
			//.put(BiomeKeys.LAKE, ImmutableSet.of(TFLandmark.QUEST_ISLAND))
			.put(BiomeKeys.SWAMP, ImmutableSet.of(TFLandmark.LABYRINTH))
			.put(BiomeKeys.FIRE_SWAMP, ImmutableSet.of(TFLandmark.HYDRA_LAIR))
			.put(BiomeKeys.DARK_FOREST, ImmutableSet.of(TFLandmark.KNIGHT_STRONGHOLD))
			.put(BiomeKeys.DARK_FOREST_CENTER, ImmutableSet.of(TFLandmark.DARK_TOWER))
			.put(BiomeKeys.SNOWY_FOREST, ImmutableSet.of(TFLandmark.YETI_CAVE))
			.put(BiomeKeys.GLACIER, ImmutableSet.of(TFLandmark.ICE_TOWER))
			.put(BiomeKeys.HIGHLANDS, ImmutableSet.of(TFLandmark.TROLL_CAVE))
			.put(BiomeKeys.FINAL_PLATEAU, ImmutableSet.of(TFLandmark.FINAL_CASTLE))
			.build();

	public void run(CachedOutput cache) {
		Path path = this.generator.getOutputFolder();
		RegistryAccess registryaccess = BuiltinRegistries.ACCESS;
		DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, registryaccess);

		HolderSet<StructureSet> structureOverrides = this.makeStructureList();

		Registry<LevelStem> twilight = this.registerTFSettings(registryaccess, structureOverrides);
		//Registry<LevelStem> skylight = this.registerSkylightSettings(registryaccess);
		//TODO void world

		WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		BiomeMaker.registerUnderground(biomeRegistry, true);
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

	private HolderSet<StructureSet> makeStructureList() {
		return HolderSet.direct(
				Function.identity(),
				TFStructureSets.STRUCTURE_SETS
						.getEntries()
						.stream()
						.map(RegistryObject::getHolder)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList()
		);
	}

	private static <T> void dumpRegistryCap(CachedOutput cache, Path path, RegistryAccess access, DynamicOps<JsonElement> ops, RegistryAccess.RegistryData<T> data) {
		LOGGER.info("Dumping: {}", data.key());
		dumpRegistry(path, cache, ops, data.key(), access.ownedRegistryOrThrow(data.key()), data.codec());
	}

	private Registry<LevelStem> registerTFSettings(RegistryAccess access, HolderSet<StructureSet> structureOverrides) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		Holder<Biome> undergroundHolder = BiomeMaker.registerUnderground(biomeRegistry, false);
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, undergroundHolder, BiomeMaker.makeBiomeList(biomeRegistry, undergroundHolder), -1.25F, 2.5F),
						noiseGenSettings
				);

		writableregistry.register(TFGenerationSettings.WORLDGEN_KEY, new LevelStem(TFDimensionSettings.TWILIGHT_DIM_TYPE.getHolder().get(), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), structureOverrides, noiseGenSettings, true, Optional.of(19), BIOME_FEATURES_SETS)), Lifecycle.experimental());
		return writableregistry;
	}

	private Registry<LevelStem> registerSkylightSettings(RegistryAccess access, HolderSet<StructureSet> structureOverrides) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		Holder<Biome> undergroundHolder = BiomeMaker.registerUnderground(biomeRegistry, false); // FIXME Hmmm...
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(TFDimensionSettings.SKYLIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, undergroundHolder, BiomeMaker.makeBiomeList(biomeRegistry, undergroundHolder), -1.25F, 2.5F),
						noiseGenSettings
				);

		writableregistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, TwilightForestMod.prefix("skylight_forest")), new LevelStem(TFDimensionSettings.TWILIGHT_DIM_TYPE.getHolder().get(), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), structureOverrides, noiseGenSettings, true, Optional.of(16), BIOME_FEATURES_SETS)), Lifecycle.stable());
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
}