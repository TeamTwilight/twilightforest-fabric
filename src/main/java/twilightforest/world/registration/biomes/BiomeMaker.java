package twilightforest.world.registration.biomes;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectSortedMap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import twilightforest.init.BiomeKeys;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class BiomeMaker extends BiomeHelper {
	public static final Map<ResourceKey<Biome>, Biome> BIOMES = generateBiomes();

	public static List<TerrainColumn> makeBiomeList(Registry<Biome> biomeRegistry, Holder<Biome> undergroundBiome) {
		return List.of(
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, BiomeKeys.FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.1F, 0.2F, biomeRegistry, BiomeKeys.DENSE_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.0625F, 0.05F, biomeRegistry, BiomeKeys.FIREFLY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.005F, 0.005F, biomeRegistry, BiomeKeys.CLEARING, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.1F, biomeRegistry, BiomeKeys.OAK_SAVANNAH, undergroundBiome),
				biomeColumnWithUnderground(-1.65F, 0.25F, biomeRegistry, BiomeKeys.STREAM, undergroundBiome),
				biomeColumnWithUnderground(-1.97F, 0.0F, biomeRegistry, BiomeKeys.LAKE, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, BiomeKeys.MUSHROOM_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.05F, biomeRegistry, BiomeKeys.DENSE_MUSHROOM_FOREST, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, BiomeKeys.ENCHANTED_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, BiomeKeys.SPOOKY_FOREST, undergroundBiome),

				biomeColumnWithUnderground(-0.9F, 0.15F, biomeRegistry, BiomeKeys.SWAMP, undergroundBiome),
				biomeColumnWithUnderground(-0.2F, 0.05F, biomeRegistry, BiomeKeys.FIRE_SWAMP, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, BiomeKeys.DARK_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, BiomeKeys.DARK_FOREST_CENTER, undergroundBiome),

				biomeColumnWithUnderground(0.05F, 0.15F, biomeRegistry, BiomeKeys.SNOWY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, BiomeKeys.GLACIER, undergroundBiome),

				biomeColumnWithUnderground(3.0F, 0.25F, biomeRegistry, BiomeKeys.HIGHLANDS, undergroundBiome),
				biomeColumnToBedrock(7.0F, 0.1F, biomeRegistry, BiomeKeys.THORNLANDS),
				biomeColumnToBedrock(13.75F, 0.025F, biomeRegistry, BiomeKeys.FINAL_PLATEAU)
		);
	}

	private static TerrainColumn biomeColumnWithUnderground(float noiseDepth, float noiseScale, Registry<Biome> biomeRegistry, ResourceKey<Biome> key, Holder<Biome> undergroundBiome) {
		Holder.Reference<Biome> biomeHolder = Holder.Reference.createStandAlone(biomeRegistry, key);

		biomeHolder.bind(key, BIOMES.get(key));

		return makeColumn(noiseDepth, noiseScale, biomeHolder, treeMap -> {
			// This will put the transition boundary around Y-8
			treeMap.put(Math.min(noiseDepth - 1, -1), biomeHolder);
			treeMap.put(Math.min(noiseDepth - 3, -3), undergroundBiome);
		});
	}

	private static TerrainColumn biomeColumnToBedrock(float noiseDepth, float noiseScale, Registry<Biome> biomeRegistry, ResourceKey<Biome> key) {
		Holder.Reference<Biome> biomeHolder = Holder.Reference.createStandAlone(biomeRegistry, key);

		biomeHolder.bind(key, BIOMES.get(key));

		return makeColumn(noiseDepth, noiseScale, biomeHolder, treeMap -> treeMap.put(0, biomeHolder));
	}

	private static TerrainColumn makeColumn(float noiseDepth, float noiseScale, Holder<Biome> biomeHolder, Consumer<Float2ObjectSortedMap<Holder<Biome>>> layerBuilder) {
		return new TerrainColumn(biomeHolder, Util.make(new Float2ObjectAVLTreeMap<>(), layerBuilder), noiseDepth, noiseScale);
	}

	public static Holder<Biome> registerUnderground(Registry<Biome> registry, boolean fullRegister) {
		Holder.Reference<Biome> holder = Holder.Reference.createStandAlone(registry, BiomeKeys.UNDERGROUND);
		Biome underground = biomeWithDefaults(
				defaultAmbientBuilder(),
				undergroundMobSpawning(),
				undergroundGen()
		)
				.temperature(0.7F)
				.downfall(0.0F)
				.build();

		if (fullRegister)
			Registry.register(registry, BiomeKeys.UNDERGROUND, underground);

		holder.bind(BiomeKeys.UNDERGROUND, underground);

		return holder;
	}

	private static Map<ResourceKey<Biome>, Biome> generateBiomes() {
		final ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes = ImmutableMap.builder();

		commonBiomes(biomes);
		mushroomBiomes(biomes);
		rareBiomes(biomes);
		swampBiomes(biomes);
		darkForestBiomes(biomes);
		snowRegionBiomes(biomes);
		highlandsBiomes(biomes);

		return biomes.build();
	}

	private static void commonBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.FOREST,
				biomeWithDefaults(
						fireflyParticles(defaultAmbientBuilder()),
						defaultMobSpawning(),
						twilightForestGen()
				)
						.build()
		);

		biomes.put(BiomeKeys.DENSE_FOREST,
				biomeWithDefaults(
						fireflyParticles(defaultAmbientBuilder())
								.waterColor(0x005522),
						defaultMobSpawning(),
						denseForestGen()
				)
						.temperature(0.7F)
						.downfall(0.8F)
						.build()
		);

		biomes.put(BiomeKeys.FIREFLY_FOREST,
				biomeWithDefaults(
						fireflyForestParticles(defaultAmbientBuilder()),
						defaultMobSpawning(),
						fireflyForestGen()
				)
						.temperature(0.5F)
						.downfall(1)
						.build()
		);

		biomes.put(BiomeKeys.CLEARING,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						defaultMobSpawning(),
						clearingGen()
				)
						.temperature(0.8F)
						.downfall(0.4F)
						.build()
		);

		biomes.put(BiomeKeys.OAK_SAVANNAH,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						defaultMobSpawning(),
						oakSavannaGen()
				)
						.temperature(0.9F)
						.downfall(0)
						.build()
		);
	}

	private static void mushroomBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.MUSHROOM_FOREST,
				biomeWithDefaults(
						fireflyParticles(defaultAmbientBuilder()),
						defaultMobSpawning(),
						mushroomForestGen()
				)
						.temperature(0.8F)
						.downfall(0.8F)
						.build()
		);

		biomes.put(BiomeKeys.DENSE_MUSHROOM_FOREST,
				biomeWithDefaults(
						fireflyParticles(defaultAmbientBuilder()),
						defaultMobSpawning(),
						denseMushroomForestGen()
				)
						.temperature(0.8F)
						.downfall(1)
						.build()
		);
	}

	private static void rareBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SPOOKY_FOREST,
				biomeWithDefaults(
						defaultAmbientBuilder()
								.grassColorOverride(0xC45123)
								.foliageColorOverride(0xFF8501)
								.waterColor(0xBC8857)
								.grassColorModifier(BiomeGrassColors.SPOOKY_FOREST),
						spookSpawning(),
						spookyForestGen()
				)
						.temperature(0.5F)
						.downfall(1)
						.build()
		);

		biomes.put(BiomeKeys.ENCHANTED_FOREST,
				biomeWithDefaults(fireflyParticles(
								defaultAmbientBuilder())
								.foliageColorOverride(0x00FFFF)
								.grassColorOverride(0x00FFFF)
								.grassColorModifier(BiomeGrassColors.ENCHANTED_FOREST),
						defaultMobSpawning(),
						enchantedForestGen()
				)
						.precipitation(Biome.Precipitation.NONE)
						.build()
		);

		biomes.put(BiomeKeys.STREAM,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						defaultMobSpawning(),
						streamsAndLakes(false))
						.temperature(0.5F)
						.downfall(0.1F)
						.build()
		);

		biomes.put(BiomeKeys.LAKE,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						defaultMobSpawning(),
						streamsAndLakes(true)
				)
						.temperature(0.66F)
						.downfall(1)
						.build()
		);
	}

	private static void swampBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SWAMP,
				biomeWithDefaults(
						defaultAmbientBuilder()
								.skyColor(0x002112)
								.fogColor(0x003F21)
								.grassColorOverride(0x5C694E)
								.foliageColorOverride(0x496137)
								.waterColor(0x95B55F)
								.grassColorModifier(BiomeGrassColors.SWAMP),
						swampSpawning(),
						swampGen()
				)
						.temperature(0.8F)
						.downfall(0.9F)
						.build()
		);

		biomes.put(BiomeKeys.FIRE_SWAMP,
				biomeWithDefaults(
						whiteAshParticles(defaultAmbientBuilder())
								.waterColor(0x2D0700)
								.fogColor(0x380A00)
								.grassColorOverride(0x572E23)
								.foliageColorOverride(0x64260F)
								.waterColor(0x6C2C2C),
						new MobSpawnSettings.Builder(),
						fireSwampGen()
				)
						.precipitation(Biome.Precipitation.NONE)
						.temperature(1)
						.downfall(0.4F)
						.build()
		);
	}

	private static void darkForestBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.DARK_FOREST,
				biomeWithDefaults(defaultAmbientBuilder()
								.skyColor(0x000000)
								.fogColor(0x000000)
								.grassColorOverride(0x4B6754).
								foliageColorOverride(0x3B5E3F)
								.grassColorModifier(BiomeGrassColors.DARK_FOREST),
						darkForestSpawning(),
						darkForestGen()
				)
						.temperature(0.7F)
						.downfall(0.8F)
						.build()
		);

		biomes.put(BiomeKeys.DARK_FOREST_CENTER,
				biomeWithDefaults(
						defaultAmbientBuilder()
								.skyColor(0x000000)
								.fogColor(0x493000)
								.grassColorOverride(0x667540)
								.foliageColorOverride(0xF9821E)
								.grassColorModifier(BiomeGrassColors.DARK_FOREST_CENTER),
						new MobSpawnSettings.Builder(),
						darkForestCenterGen()
				)
						.build()
		);
	}

	private static void snowRegionBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SNOWY_FOREST,
				biomeWithDefaults(
						defaultAmbientBuilder()
								.skyColor(0x808080)
								.fogColor(0xFFFFFF)
								.foliageColorOverride(0xFFFFFF)
								.grassColorOverride(0xFFFFFF),
						snowForestSpawning(),
						snowyForestGen()
				)
						.precipitation(Biome.Precipitation.SNOW)
						.temperature(0.09F)
						.downfall(0.9F)
						.build()
		);

		biomes.put(BiomeKeys.GLACIER,
				biomeWithDefaults(
						defaultAmbientBuilder()
								.skyColor(0x130D28)
								.fogColor(0x361F88),
						penguinSpawning(),
						glacierGen()
				)
						.temperature(0.8F)
						.downfall(0.1F)
						.precipitation(Biome.Precipitation.SNOW)
						.build()
		);
	}

	private static void highlandsBiomes(ImmutableMap.Builder<ResourceKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.HIGHLANDS,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						defaultMobSpawning(),
						highlandsGen()
				)
						.temperature(0.4F)
						.downfall(0.7F)
						.build()
		);

		biomes.put(BiomeKeys.THORNLANDS,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						new MobSpawnSettings.Builder(),
						thornlandsGen()
				)
						.temperature(0.3F)
						.downfall(0.2F)
						.build()
		);

		biomes.put(BiomeKeys.FINAL_PLATEAU,
				biomeWithDefaults(
						defaultAmbientBuilder(),
						ravenSpawning(),
						plateauGen()
				)
						.temperature(1.0F)
						.downfall(0.2F)
						.build()
		);
	}
}
