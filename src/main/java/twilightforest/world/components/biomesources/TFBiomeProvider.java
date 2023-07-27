package twilightforest.world.components.biomesources;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import twilightforest.init.TFDimensionSettings;
import twilightforest.util.ComparableResourceKey;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated // Pending rename
public class TFBiomeProvider extends BiomeSource {
	public static final Codec<TFBiomeProvider> TF_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			TerrainColumn.CODEC.listOf().fieldOf("biome_landscape").xmap(l -> l.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), m -> m.values().stream().sorted(Comparator.comparing(terrainColumn -> ComparableResourceKey.of(terrainColumn.getResourceKey()))).toList()).forGetter(o -> o.biomeList),
			Codec.FLOAT.fieldOf("base_offset").forGetter(o -> o.baseOffset),
			Codec.FLOAT.fieldOf("base_factor").forGetter(o -> o.baseFactor),
			BiomeLayerStack.HOLDER_CODEC.fieldOf("biome_layer_config").orElseGet((String s) -> TwilightForestMod.LOGGER.warn(s), BiomeLayerStack::buildDefault).forGetter(TFBiomeProvider::getBiomeConfig)
	).apply(instance, instance.stable(TFBiomeProvider::new)));

	private final Map<ResourceKey<Biome>, TerrainColumn> biomeList;
	private final float baseOffset;
	private final float baseFactor;

	private final Holder<BiomeLayerFactory> genBiomeConfig;
	private final Supplier<LazyArea> genBiomes;

	public TFBiomeProvider(List<TerrainColumn> list, float offset, float factor, Holder<BiomeLayerFactory> biomeLayerFactory) {
		this(list.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), offset, factor, biomeLayerFactory);
	}

	public TFBiomeProvider(Map<ResourceKey<Biome>, TerrainColumn> list, float offset, float factor, Holder<BiomeLayerFactory> biomeLayerFactory) {
		super();

		//this.genBiomes = buildLayers((salt) -> new LazyAreaContext(25, salt));
		this.genBiomeConfig = biomeLayerFactory;
		this.genBiomes = Suppliers.memoize(() -> this.genBiomeConfig.get().build(salt -> new LazyAreaContext(25, salt)));

		this.baseOffset = offset;
		this.baseFactor = factor;

		this.biomeList = list;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.biomeList.values().stream().flatMap(TerrainColumn::getBiomes);
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return TF_CODEC;
	}

	public float getBaseOffset() {
		return this.baseOffset;
	}

	public float getBaseFactor() {
		return this.baseFactor;
	}

	public float getBiomeDepth(int x, int z) {
		return this.getBiomeDepth(this.genBiomes.get().getBiome(x, z));
	}

	public float getBiomeDepth(ResourceKey<Biome> biome) {
		return this.getBiomeValue(biome, TerrainColumn::depth, 0f);
	}

	public Optional<TerrainColumn> getTerrainColumn(int x, int z) {
		return this.getTerrainColumn(this.genBiomes.get().getBiome(x, z));
	}

	public Optional<TerrainColumn> getTerrainColumn(ResourceKey<Biome> biome) {
		return this.biomeList.values().stream().filter(p -> p.is(biome)).findFirst();
	}

	public <T> T getBiomeValue(ResourceKey<Biome> biome, Function<TerrainColumn, T> function, T other) {
		return this.getTerrainColumn(biome).map(function).orElse(other);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
		return this.biomeList.get(this.genBiomes.get().getBiome(x, z)).getBiome(y);
	}

	private Holder<BiomeLayerFactory> getBiomeConfig() {
		return this.genBiomeConfig;
	}
}
