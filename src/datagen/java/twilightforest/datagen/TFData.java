package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.init.TFRegistries;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;
import twilightforest.init.*;
import twilightforest.init.custom.*;

public class TFData implements DataGeneratorEntrypoint {
	private final AssetsGenerator assetsGenerator = AssetsGenerator.INSTANCE;
	private final DataGenerator dataGenerator = DataGenerator.INSTANCE;

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		assetsGenerator.generate(pack);
		dataGenerator.generate(pack);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap);
		registryBuilder.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigs::bootstrap);
		registryBuilder.add(Registries.STRUCTURE, TFStructures::bootstrap);
		registryBuilder.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap);
		registryBuilder.add(Registries.DENSITY_FUNCTION, TFDensityFunctions::bootstrap);
		registryBuilder.add(Registries.NOISE_SETTINGS, TFDimensionData::bootstrapNoise);
		registryBuilder.add(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack::bootstrap);
		registryBuilder.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeLayerStack::bootstrapData);
		registryBuilder.add(Registries.DIMENSION_TYPE, TFDimensionData::bootstrapType);
		registryBuilder.add(Registries.LEVEL_STEM, TFDimensionData::bootstrapStem);
		registryBuilder.add(Registries.BIOME, TFBiomes::bootstrap);
		registryBuilder.add(TFRegistries.Keys.WOOD_PALETTES, WoodPalettes::bootstrap);
		registryBuilder.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap);
		registryBuilder.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap);
		registryBuilder.add(TFRegistries.Keys.RESTRICTIONS, Restrictions::bootstrap);
		registryBuilder.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariants::bootstrap);
		registryBuilder.add(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors::bootstrap);
		registryBuilder.add(Registries.BANNER_PATTERN, TFBannerPatterns::bootstrap);
		registryBuilder.add(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariants::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifiersManager::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariants::bootstrap);
		registryBuilder.add(Registries.JUKEBOX_SONG, TFJukeboxSongs::bootstrap);
		registryBuilder.add(Registries.ENCHANTMENT, TFEnchantments::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlers::bootstrap);
	}
}