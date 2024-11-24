package twilightforest.init.custom;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.util.MagicPaintingVariant;
import twilightforest.util.Restriction;
import twilightforest.util.WoodPalette;

/**
 * An extension of the {@link RegistriesDatapackGenerator} which properly handles
 * referencing existing dynamic registry objects within another dynamic registry
 * object. It's properly handling the existing twilight registrys now (since fabric doesn't do it
 * automatically like forge).
 */
public class TFDatapackBuiltinEntriesProvider extends RegistriesDatapackGenerator {
  private final PackOutput output;
  private final CompletableFuture<HolderLookup.Provider> registries;
  private final java.util.function.Predicate<String> namespacePredicate;

  /**
   * Constructs a new datapack provider which generates all registry objects
   * from the provided mods using the holder.
   *
   * @param output the target directory of the data generator
   * @param registries a future of a lookup for registries and their objects
   * @param modIds a set of mod ids to generate the dynamic registry objects of
   */
  public TFDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
    super(output, registries);
    this.namespacePredicate = modIds == null ? namespace -> true : modIds::contains;
    this.registries = registries;
    this.output = output;
  }

  /**
   * Constructs a new datapack provider which generates all registry objects
   * from the provided mods using the holder. All entries that need to be
   * bootstrapped are provided within the {@link RegistrySetBuilder}.
   *
   * @param output the target directory of the data generator
   * @param registries a future of a lookup for registries and their objects
   * @param datapackEntriesBuilder a builder containing the dynamic registry objects added by this provider
   * @param modIds a set of mod ids to generate the dynamic registry objects of
   */
  public TFDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
    this(output, registries.thenApply(r -> constructRegistries(r, datapackEntriesBuilder)), modIds);
  }

  /**
   * A method used to construct empty bootstraps for all registries this provider
   * did not touch such that existing dynamic registry objects do not get inlined.
   *
   * @param original a future of a lookup for registries and their objects
   * @param datapackEntriesBuilder a builder containing the dynamic registry objects added by this provider
   * @return a new lookup containing the existing and to be generated registries and their objects
   */
  private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder) {
    var builderKeys = new HashSet<>(datapackEntriesBuilder.entries.stream().map(RegistrySetBuilder.RegistryStub::key).toList());
    getDataPackRegistriesWithDimensions().filter(data -> !builderKeys.contains(data.key())).forEach(data -> datapackEntriesBuilder.add(data.key(), context -> {}));

    var registryAccess = new TFRegistryAccess();
    return datapackEntriesBuilder.buildPatch(registryAccess, original);
  }

  @Override
  public CompletableFuture<?> run(CachedOutput output) {
    return this.registries.thenCompose((provider) -> {
      DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, provider);
      return CompletableFuture.allOf(getDataPackRegistriesWithDimensions().flatMap((registryData) -> {
        return this.dumpRegistryCap(output, provider, dynamicops, registryData).stream();
      }).toArray(CompletableFuture[]::new));
    });
  }

  private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput p_256502_, HolderLookup.Provider p_256492_, DynamicOps<JsonElement> p_256000_, RegistryDataLoader.RegistryData<T> p_256449_) {
    ResourceKey<? extends Registry<T>> resourcekey = p_256449_.key();
    return p_256492_.lookup(resourcekey).map((registryLookup) -> {
      PackOutput.PathProvider packoutput$pathprovider = this.output.createPathProvider(PackOutput.Target.DATA_PACK, prefixNamespace(resourcekey.location()));
      return CompletableFuture.allOf(registryLookup.listElements().filter(holder -> this.namespacePredicate.test(holder.key().location().getNamespace())).map((p_256105_) -> {
        return dumpValue(packoutput$pathprovider.json(p_256105_.key().location()), p_256502_, p_256000_, p_256449_.elementCodec(), p_256105_.value());
      }).toArray(CompletableFuture[]::new));
    });
  }

  public static Stream<RegistryDataLoader.RegistryData<?>> getDataPackRegistriesWithDimensions() {
    Stream<RegistryDataLoader.RegistryData<?>> defaultRegistries = Stream.concat(
            RegistryDataLoader.WORLDGEN_REGISTRIES.stream(),
            RegistryDataLoader.DIMENSION_REGISTRIES.stream()
    );

    Stream<RegistryDataLoader.RegistryData<?>> customRegistries = Stream.of(
            new RegistryDataLoader.RegistryData<>(BiomeLayerStack.BIOME_STACK_KEY, BiomeLayerStack.DISPATCH_CODEC),
            new RegistryDataLoader.RegistryData<>(WoodPalettes.WOOD_PALETTE_TYPE_KEY, WoodPalette.CODEC),
            new RegistryDataLoader.RegistryData<>(MagicPaintingVariants.REGISTRY_KEY, MagicPaintingVariant.CODEC),
            new RegistryDataLoader.RegistryData<>(Restrictions.RESTRICTION_KEY, Restriction.CODEC)
    );

    return Stream.concat(defaultRegistries, customRegistries);
  }

  public static String prefixNamespace(ResourceLocation registryKey) {
    return registryKey.getNamespace().equals("minecraft") ? registryKey.getPath() : registryKey.getNamespace() +  "/"  + registryKey.getPath();
  }

  public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
    return registries;
  }
}
