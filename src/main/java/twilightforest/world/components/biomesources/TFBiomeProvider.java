package twilightforest.world.components.biomesources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import twilightforest.init.TFFeatureModifiers;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;
import twilightforest.world.components.layer.*;
import twilightforest.world.components.layer.vanillalegacy.Layer;
import twilightforest.world.components.layer.vanillalegacy.SmoothLayer;
import twilightforest.world.components.layer.vanillalegacy.ZoomLayer;
import twilightforest.world.components.layer.vanillalegacy.area.Area;
import twilightforest.world.components.layer.vanillalegacy.area.AreaFactory;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

@Deprecated
public class TFBiomeProvider extends BiomeSource {
	public static final Codec<TFBiomeProvider> TF_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.LONG.fieldOf("seed").stable().orElseGet(() -> TFFeatureModifiers.seed).forGetter(o -> o.seed),
			RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(o -> o.registry),
			Biome.CODEC.fieldOf("underground_biome").forGetter(o -> o.undergroundBiome),
			TerrainColumn.CODEC.listOf().fieldOf("biome_landscape").xmap(l -> l.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), m -> List.copyOf(m.values())).forGetter(o -> o.biomeList),
			Codec.FLOAT.fieldOf("base_offset").forGetter(o -> o.baseOffset),
			Codec.FLOAT.fieldOf("base_factor").forGetter(o -> o.baseFactor)
	).apply(instance, instance.stable(TFBiomeProvider::new)));

	private final Registry<Biome> registry;
	private final Holder<Biome> undergroundBiome;
	private final Map<ResourceKey<Biome>, TerrainColumn> biomeList;
	private final Layer genBiomes;
	private final long seed;
	private final float baseOffset;
	private final float baseFactor;

	public TFBiomeProvider(long seed, Registry<Biome> registryIn, Holder<Biome> undergroundBiome, List<TerrainColumn> list, float offset, float factor) {
		this(seed, registryIn, undergroundBiome, list.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), offset, factor);
	}

	public TFBiomeProvider(long seed, Registry<Biome> registryIn, Holder<Biome> undergroundBiome, Map<ResourceKey<Biome>, TerrainColumn> list, float offset, float factor) {
		super(list.values().stream().flatMap(TerrainColumn::getBiomes));

		this.seed = seed;
		this.baseOffset = offset;
		this.baseFactor = factor;

		this.registry = registryIn;
		this.undergroundBiome = undergroundBiome;
		this.biomeList = list;
		this.genBiomes = makeLayers(seed, registryIn);
	}

	public static int getBiomeId(ResourceKey<Biome> biome, Registry<Biome> registry) {
		return registry.getId(registry.get(biome));
	}

	private static <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry, long rawSeed) {
 		AreaFactory<T> biomes = GenLayerTFBiomes.INSTANCE.setup(registry).run(seed.apply(1L));
		biomes = GenLayerTFKeyBiomes.INSTANCE.setup(registry, rawSeed).run(seed.apply(1000L), biomes);
		biomes = GenLayerTFCompanionBiomes.INSTANCE.setup(registry).run(seed.apply(1000L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerTFBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = GenLayerTFThornBorder.INSTANCE.setup(registry).run(seed.apply(500L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1003), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1004), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1005), biomes);

		AreaFactory<T> riverLayer = GenLayerTFStream.INSTANCE.setup(registry).run(seed.apply(1L), biomes);
		riverLayer = SmoothLayer.INSTANCE.run(seed.apply(7000L), riverLayer);
		biomes = GenLayerTFRiverMix.INSTANCE.setup(registry).run(seed.apply(100L), biomes, riverLayer);

		return biomes;
	}
	
	public static Layer makeLayers(long seed, Registry<Biome> registry) {
		AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context), registry, seed);
		// Debug code to render an image of the biome layout within the ide
		/*final java.util.Map<Integer, Integer> remapColors = new java.util.HashMap<>();
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.LAKE, registry), 0x0000FF);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.FOREST, registry), 0x00FF00);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.DENSE_FOREST, registry), 0x00AA00);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.HIGHLANDS, registry), 0xCC6900);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.MUSHROOM_FOREST, registry), 0xcc008b);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.SWAMP, registry), 0x00ccbb);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.STREAM, registry), 0x0000FF);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.SNOWY_FOREST, registry), 0xFFFFFF);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.GLACIER, registry), 0x82bff5);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.CLEARING, registry), 0x84f582);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.OAK_SAVANNAH, registry), 0xeff582);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.FIREFLY_FOREST, registry), 0x58fc66);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.DENSE_MUSHROOM_FOREST, registry), 0xb830b8);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.DARK_FOREST, registry), 0x193d0d);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.ENCHANTED_FOREST, registry), 0x00FFFF);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.FIRE_SWAMP, registry), 0xFF0000);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.DARK_FOREST_CENTER, registry), 0xFFFF00);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.FINAL_PLATEAU, registry), 0x000000);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.THORNLANDS, registry), 0x3d250d);
		remapColors.put(getBiomeId(twilightforest.world.registration.biomes.BiomeKeys.SPOOKY_FOREST, registry), 0x7700FF);
		final int size = 2048;
		final int rad = size / 2;
		final int ox = 0;
		final int oz = 0;
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics2D display = image.createGraphics();
		LazyArea area = areaFactory.make();
		java.util.function.BiPredicate<Integer, Integer> line = (i, mod) -> {
			for (int j = -5; j < 5; j++) {
				if ((i + j) % mod == 0)
					return true;
			}
			return false;
		};
		for (int x = -rad; x < rad - 1; x++) {
			for (int z = -rad; z < rad - 1; z++) {
				int xx = x + (ox * 64);
				int zz = z + (oz * 64);
				int c = area.get(x, z);
				display.setColor(line.test(xx, 512) || line.test(zz, 512) ? new java.awt.Color(0xFF0000) : new java.awt.Color(remapColors.getOrDefault(c, c)));
				display.drawRect(x + rad, z + rad, 1, 1);
			}
		}
 		System.out.println("breakpoint");*/
		return new Layer(areaFactory) {
			@Override
			public Holder<Biome> get(Registry<Biome> registry, int p_242936_2_, int p_242936_3_) {
				int i = this.area.get(p_242936_2_, p_242936_3_);
				Optional<Holder<Biome>> biome = registry.getHolder(i);
				if (biome.isEmpty())
					throw new IllegalStateException("Unknown biome id emitted by layers: " + i);
				return biome.get();
			}
		};
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
		return this.getBiomeDepth(this.genBiomes.get(this.registry, x, z));
	}

	public float getBiomeDepth(Holder<Biome> biome) {
		return this.getBiomeValue(biome, TerrainColumn::depth, 0f);
	}

	public Optional<TerrainColumn> getTerrainColumn(int x, int z) {
		return this.getTerrainColumn(this.genBiomes.get(this.registry, x, z));
	}

	public Optional<TerrainColumn> getTerrainColumn(Holder<Biome> biome) {
		return this.biomeList.values().stream().filter(p -> p.is(biome)).findFirst();
	}

	public <T> T getBiomeValue(Holder<Biome> biome, Function<TerrainColumn, T> function, T other) {
		return this.getTerrainColumn(biome).map(function).orElse(other);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
		// TODO Further positional-smoothing, see TFTerrainWarp.fillNoiseColumn's use of TFTerrainWarp.BIOME_WEIGHTS
		//  That method already calls this method, resulting in search-space duplication

		// FIXME Hacky double-dipping of biomes
		Holder<Biome> columnBiome = this.genBiomes.get(this.registry, x, z);
		return this.biomeList.get(columnBiome.unwrapKey().get()).getBiome(y, columnBiome);
	}
}
