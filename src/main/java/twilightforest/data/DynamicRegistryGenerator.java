package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import twilightforest.TFRegistries;

import java.util.concurrent.CompletableFuture;

public class DynamicRegistryGenerator extends FabricDynamicRegistryProvider {
	public DynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS));
		entries.addAll(registries.lookupOrThrow(Registries.STRUCTURE));
		entries.addAll(registries.lookupOrThrow(Registries.STRUCTURE_SET));
		entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_CARVER));
		entries.addAll(registries.lookupOrThrow(Registries.DENSITY_FUNCTION));
		entries.addAll(registries.lookupOrThrow(Registries.NOISE_SETTINGS));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.BIOME_STACK));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.BIOME_TERRAIN_DATA));
		entries.addAll(registries.lookupOrThrow(Registries.DIMENSION_TYPE));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.WOOD_PALETTES));
		entries.addAll(registries.lookupOrThrow(Registries.BIOME));
		entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
		entries.addAll(registries.lookupOrThrow(Registries.TRIM_MATERIAL));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.RESTRICTIONS));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS));
		entries.addAll(registries.lookupOrThrow(Registries.BANNER_PATTERN));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.TINY_BIRD_VARIANT));
		entries.addAll(registries.lookupOrThrow(Registries.JUKEBOX_SONG));
		entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
		entries.addAll(registries.lookupOrThrow(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST));
	}

	@Override
	public String getName() {
		return "TwilightForest Dynamic Registries";
	}
}