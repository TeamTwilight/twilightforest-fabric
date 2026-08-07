package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;

import twilightforest.TFRegistries;
import twilightforest.data.custom.*;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.custom.structuredefinitions.CampStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.FinalCastleStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.LichTowerStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.NagaCourtyardStructureDefinitionGenerator;
import twilightforest.data.tags.*;
import twilightforest.init.*;
import twilightforest.init.custom.*;

import java.util.Optional;

public class DataGenerators implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ExistingFileHelper helper = getExistingFileHelper();

		// client generators
		pack.addProvider((output, registries) -> new BlockstateGenerator(output, helper));
		pack.addProvider((output, registries) -> new ItemModelGenerator(output, helper));
		pack.addProvider((output, registries) -> new ParticleGenerator(output, helper));
		pack.addProvider((output, registries) -> new SoundGenerator(output, helper));

		pack.addProvider(RegistryDataGenerator::new);
		pack.addProvider(BiomeTagGenerator::new);
		pack.addProvider((output, registries) -> new CustomTagGenerator.BannerPatternTagGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.DimensionTypeTagGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.WoodPaletteTagGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.PaintingVariantTagGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new DamageTypeTagGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new StructureTagGenerator(output, registries, helper));
		pack.addProvider(AdvancementGenerator::new);
		pack.addProvider(LootGenerator::new);

		// server generators
		pack.addProvider(DataMapGenerator::new);
		pack.addProvider((output, registries) -> new StalactiteGenerator(output));
		pack.addProvider((output, registries) -> new TFStructureUpdater("structures", output, helper));

		// normal tags
		BlockTagGenerator blockTags = pack.addProvider(BlockTagGenerator::new);
		pack.addProvider((output, registries) -> new CustomTagGenerator.BlockEntityTagGenerator(output, registries, helper));
		pack.addProvider(FluidTagGenerator::new);
		pack.addProvider((output, registries) -> new ItemTagGenerator(output, registries, blockTags));
		pack.addProvider(EntityTagGenerator::new);
		pack.addProvider(CraftingGenerator::new);
		pack.addProvider(LootModifierGenerator::new);

		pack.addProvider((output, registries) -> new CampStructureDefinitionGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new FinalCastleStructureDefinitionGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new LichTowerStructureDefinitionGenerator(output, registries, helper));
		pack.addProvider((output, registries) -> new NagaCourtyardStructureDefinitionGenerator(output, registries, helper));

		// these have to go last due to magic paintings
		// when magic paintings are registered their atlas and lang content is too
		pack.addProvider((output, registries) -> new AtlasGenerator(output, registries, helper));
		pack.addProvider(LangGenerator::new);

		pack.addProvider((output, registries) -> new QuestGenerator(output));

		// pack.mcmeta
		pack.addProvider((output, registries) -> new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
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

	/**
	 * Creates an ExistingFileHelper for data generation.
	 * Tries to use the JVM arg {@code -Dporting_lib.datagen.existing_resources=<path>} first,
	 * falling back to a disabled helper if the arg is not set.
	 */
	private static ExistingFileHelper getExistingFileHelper() {
		try {
			return ExistingFileHelper.withResourcesFromArg();
		} catch (Exception e) {
			// Fallback: create a disabled helper that doesn't validate file existence
			return new ExistingFileHelper(
				java.util.List.of(),
				java.util.Set.of(),
				false,
				null,
				null
			);
		}
	}
}