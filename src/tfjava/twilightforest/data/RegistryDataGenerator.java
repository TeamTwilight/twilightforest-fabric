package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import twilightforest.TFRegistries;
import twilightforest.init.TFBannerPatterns;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFCaveCarvers;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDensityFunctions;
import twilightforest.init.TFDimensionData;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFJukeboxSongs;
import twilightforest.init.TFPlacedFeatures;
import twilightforest.init.TFStructureSets;
import twilightforest.init.TFStructures;
import twilightforest.init.TFTrimMaterials;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.init.custom.DwarfRabbitVariants;
import twilightforest.init.custom.MagicPaintingVariants;
import twilightforest.init.custom.Restrictions;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.init.custom.TinyBirdVariants;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.init.custom.WoodPalettes;

import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends RegistriesDatapackGenerator {
	public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	public static void addToBuilder(RegistrySetBuilder builder) {
		builder
			.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigs::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.DENSITY_FUNCTION, TFDensityFunctions::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionData::bootstrapNoise)
			.add(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack::bootstrap)
			.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeLayerStack::bootstrapData)
			.add(Registries.DIMENSION_TYPE, TFDimensionData::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionData::bootstrapStem)
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(TFRegistries.Keys.WOOD_PALETTES, WoodPalettes::bootstrap)
			.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap)
			.add(TFRegistries.Keys.RESTRICTIONS, Restrictions::bootstrap)
			.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariants::bootstrap)
			.add(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors::bootstrap)
			.add(Registries.BANNER_PATTERN, TFBannerPatterns::bootstrap)
			.add(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariants::bootstrap)
			.add(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifiersManager::bootstrap)
			.add(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariants::bootstrap)
			.add(Registries.JUKEBOX_SONG, TFJukeboxSongs::bootstrap)
			.add(Registries.ENCHANTMENT, TFEnchantments::bootstrap)
			.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlers::bootstrap);
	}
}
