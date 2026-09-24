package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.datagen.data.*;
import twilightforest.datagen.data.custom.*;
import twilightforest.datagen.data.worldgen.*;
import twilightforest.init.TFRegistries;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;

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
		registryBuilder.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatureGenerator::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, TFPlacedFeatureGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigGenerator::bootstrap);
		registryBuilder.add(Registries.STRUCTURE, TFStructureGenerator::bootstrap);
		registryBuilder.add(Registries.STRUCTURE_SET, TFStructureSetGenerator::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_CARVER, TFCaveCarverGenerator::bootstrap);
		registryBuilder.add(Registries.DENSITY_FUNCTION, TFDensityFunctionGenerator::bootstrap);
		registryBuilder.add(Registries.NOISE_SETTINGS, TFDimensionGenerator::bootstrapNoise);
		registryBuilder.add(TFRegistries.Keys.BIOME_STACK, TFBiomeLayerGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, TFBiomeLayerGenerator::bootstrapData);
		registryBuilder.add(Registries.DIMENSION_TYPE, TFDimensionGenerator::bootstrapType);
		registryBuilder.add(Registries.LEVEL_STEM, TFDimensionGenerator::bootstrapStem);
		registryBuilder.add(Registries.BIOME, TFBiomeGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.WOOD_PALETTES, WoodPaletteGenerator::bootstrap);
		registryBuilder.add(Registries.DAMAGE_TYPE, TFDamageTypeGenerator::bootstrap);
		registryBuilder.add(Registries.TRIM_MATERIAL, TFTrimMaterialGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.RESTRICTIONS, RestrictionGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariantGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessorGenerator::bootstrap);
		registryBuilder.add(Registries.BANNER_PATTERN, TFBannerPatternGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariantGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifierGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariantGenerator::bootstrap);
		registryBuilder.add(Registries.JUKEBOX_SONG, TFJukeboxSongGenerator::bootstrap);
		registryBuilder.add(Registries.ENCHANTMENT, TFEnchantmentsGenerator::bootstrap);
		registryBuilder.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerGenerator::bootstrap);
	}
}