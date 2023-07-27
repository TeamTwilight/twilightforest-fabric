package twilightforest.world.registration.biomes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.floats.Float2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectSortedMap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFLandmark;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class BiomeMaker extends BiomeHelper {

	public static final Map<ResourceKey<Biome>, ImmutableSet<TFLandmark>> BIOME_FEATURES_SETS = new ImmutableMap.Builder<ResourceKey<Biome>, ImmutableSet<TFLandmark>>()
			//.put(TFBiomes.DENSE_MUSHROOM_FOREST, Set.of(MUSHROOM_TOWER))
			.put(TFBiomes.ENCHANTED_FOREST, ImmutableSet.of(TFLandmark.QUEST_GROVE))
			//.put(TFBiomes.LAKE, ImmutableSet.of(TFLandmark.QUEST_ISLAND))
			.put(TFBiomes.SWAMP, ImmutableSet.of(TFLandmark.LABYRINTH))
			.put(TFBiomes.FIRE_SWAMP, ImmutableSet.of(TFLandmark.HYDRA_LAIR))
			.put(TFBiomes.DARK_FOREST, ImmutableSet.of(TFLandmark.KNIGHT_STRONGHOLD))
			.put(TFBiomes.DARK_FOREST_CENTER, ImmutableSet.of(TFLandmark.DARK_TOWER))
			.put(TFBiomes.SNOWY_FOREST, ImmutableSet.of(TFLandmark.YETI_CAVE))
			.put(TFBiomes.GLACIER, ImmutableSet.of(TFLandmark.ICE_TOWER))
			.put(TFBiomes.HIGHLANDS, ImmutableSet.of(TFLandmark.TROLL_CAVE))
			.put(TFBiomes.FINAL_PLATEAU, ImmutableSet.of(TFLandmark.FINAL_CASTLE))
			.build();

	public static List<TerrainColumn> makeBiomeList(HolderGetter<Biome> biomeRegistry, Holder<Biome> undergroundBiome) {
		return List.of(
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.1F, 0.2F, biomeRegistry, TFBiomes.DENSE_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.0625F, 0.05F, biomeRegistry, TFBiomes.FIREFLY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.005F, 0.005F, biomeRegistry, TFBiomes.CLEARING, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.1F, biomeRegistry, TFBiomes.OAK_SAVANNAH, undergroundBiome),
				biomeColumnWithUnderground(-1.65F, 0.25F, biomeRegistry, TFBiomes.STREAM, undergroundBiome),
				biomeColumnWithUnderground(-1.97F, 0.0F, biomeRegistry, TFBiomes.LAKE, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.MUSHROOM_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.05F, 0.05F, biomeRegistry, TFBiomes.DENSE_MUSHROOM_FOREST, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.ENCHANTED_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.SPOOKY_FOREST, undergroundBiome),

				biomeColumnWithUnderground(-0.9F, 0.15F, biomeRegistry, TFBiomes.SWAMP, undergroundBiome),
				biomeColumnWithUnderground(-0.2F, 0.05F, biomeRegistry, TFBiomes.FIRE_SWAMP, undergroundBiome),

				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, TFBiomes.DARK_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.005F, biomeRegistry, TFBiomes.DARK_FOREST_CENTER, undergroundBiome),

				biomeColumnWithUnderground(0.05F, 0.15F, biomeRegistry, TFBiomes.SNOWY_FOREST, undergroundBiome),
				biomeColumnWithUnderground(0.025F, 0.05F, biomeRegistry, TFBiomes.GLACIER, undergroundBiome),

				biomeColumnWithUnderground(3.0F, 0.25F, biomeRegistry, TFBiomes.HIGHLANDS, undergroundBiome),
				biomeColumnToBedrock(7.0F, 0.1F, biomeRegistry, TFBiomes.THORNLANDS),
				biomeColumnToBedrock(13.75F, 0.025F, biomeRegistry, TFBiomes.FINAL_PLATEAU)
		);
	}

	private static TerrainColumn biomeColumnWithUnderground(float noiseDepth, float noiseScale, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key, Holder<Biome> undergroundBiome) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(noiseDepth, noiseScale, biomeHolder, treeMap -> {
			// This will put the transition boundary around Y-8
			treeMap.put(Math.min(noiseDepth - 1, -1), biomeHolder);
			treeMap.put(Math.min(noiseDepth - 3, -3), undergroundBiome);
		});
	}

	private static TerrainColumn biomeColumnToBedrock(float noiseDepth, float noiseScale, HolderGetter<Biome> biomeRegistry, ResourceKey<Biome> key) {
		Holder.Reference<Biome> biomeHolder = biomeRegistry.getOrThrow(key);

		biomeHolder.bindKey(key);

		return makeColumn(noiseDepth, noiseScale, biomeHolder, treeMap -> treeMap.put(0, biomeHolder));
	}

	private static TerrainColumn makeColumn(float noiseDepth, float noiseScale, Holder<Biome> biomeHolder, Consumer<Float2ObjectSortedMap<Holder<Biome>>> layerBuilder) {
		return new TerrainColumn(biomeHolder, Util.make(new Float2ObjectAVLTreeMap<>(), layerBuilder), noiseDepth, noiseScale);
	}
}
