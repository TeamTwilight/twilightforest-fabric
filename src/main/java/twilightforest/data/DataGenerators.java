package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.custom.UncraftingRecipeGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;
import twilightforest.init.*;

public class DataGenerators implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();

		pack.addProvider(TFAdvancementProvider::new);
		pack.addProvider((output, provider) -> new BlockstateGenerator(output, helper));
		pack.addProvider((output, provider) -> new ItemModelGenerator(output, helper));
		pack.addProvider((output, provider) -> new AtlasGenerator(output));
		pack.addProvider(BiomeTagGenerator::new);
		pack.addProvider(CustomTagGenerator.BannerPatternTagGenerator::new);
		BlockTagGenerator blocktags = pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(FluidTagGenerator::new);
		pack.addProvider((output, provider) -> new ItemTagGenerator(output, provider, blocktags));
		pack.addProvider(EntityTagGenerator::new);
		pack.addProvider(CustomTagGenerator.EnchantmentTagGenerator::new);
		pack.addProvider(LootGenerator::new);
		pack.addProvider(CraftingGenerator::new);
		pack.addProvider(LootModifierGenerator::new);
		WorldGenerator.addProviders(pack, helper);

		pack.addProvider((output, provider) -> new CrumbleHornGenerator(output, helper));
		pack.addProvider((output, provider) -> new TransformationPowderGenerator(output, helper));
		pack.addProvider((output, provider) -> new UncraftingRecipeGenerator(output, helper));
		pack.addProvider(StalactiteGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
				.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
				.add(Registries.STRUCTURE, TFStructures::bootstrap)
				.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
				.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
				.add(Registries.NOISE_SETTINGS, TFDimensionSettings::bootstrapNoise)
				.add(Registries.DIMENSION_TYPE, TFDimensionSettings::bootstrapType)
				.add(Registries.LEVEL_STEM, TFDimensionSettings::bootstrapStem)
				.add(Registries.BIOME, TFBiomes::bootstrap);
	}
}
