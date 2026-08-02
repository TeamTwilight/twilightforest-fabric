package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;

import twilightforest.data.custom.*;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.custom.structuredefinitions.CampStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.FinalCastleStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.LichTowerStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.NagaCourtyardStructureDefinitionGenerator;
import twilightforest.data.tags.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DataGenerators implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ExistingFileHelper helper = getExistingFileHelper();

		// client generators
		pack.addProvider((output, registries) -> new BlockstateGenerator(output, helper));
		pack.addProvider((output, registries) -> new ItemModelGenerator(output, helper));
		//		pack.addProvider((output, registries) -> new ParticleGenerator(output, helper));
		pack.addProvider((output, registries) -> new SoundGenerator(output, helper));

		// registry-based stuff - create first to get the lookupProvider for subsequent generators
		RegistryDataGenerator datapackProvider = pack.addProvider(RegistryDataGenerator::new);
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();

		pack.addProvider((output, registries) -> new BiomeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.BannerPatternTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.DimensionTypeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.PaintingVariantTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new DamageTypeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new StructureTagGenerator(output, lookupProvider, helper));
		//		pack.addProvider((output, registries) -> new TFAdvancementProvider(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new LootGenerator(output, lookupProvider));

		// server generators
		pack.addProvider((output, registries) -> new DataMapGenerator(output, lookupProvider));
		pack.addProvider((output, registries) -> new StalactiteGenerator(output));
		pack.addProvider((output, registries) -> new TFStructureUpdater("structures", output, helper));

		// normal tags
		BlockTagGenerator blocktags = pack.addProvider((output, registries) -> new BlockTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CustomTagGenerator.BlockEntityTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new FluidTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new ItemTagGenerator(output, lookupProvider, blocktags.contentsGetter(), helper));
		pack.addProvider((output, registries) -> new EntityTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new CraftingGenerator(output, lookupProvider));
		pack.addProvider((output, registries) -> new LootModifierGenerator(output, lookupProvider));

		pack.addProvider((output, registries) -> new CampStructureDefinitionGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new FinalCastleStructureDefinitionGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new LichTowerStructureDefinitionGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new NagaCourtyardStructureDefinitionGenerator(output, lookupProvider, helper));

		// these have to go last due to magic paintings
		// when magic paintings are registered their atlas and lang content is too
		pack.addProvider((output, registries) -> new AtlasGenerator(output, lookupProvider, helper));
		pack.addProvider((output, registries) -> new LangGenerator(output, lookupProvider));

		pack.addProvider((output, registries) -> new QuestGenerator(output));

		// pack.mcmeta
		pack.addProvider((output, registries) -> new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
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