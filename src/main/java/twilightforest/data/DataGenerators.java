package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.custom.UncraftingRecipeGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

public class DataGenerators implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();

		pack.addProvider((output, provider) -> new BlockstateGenerator(output, helper));
		pack.addProvider((output, provider) -> new ItemModelGenerator(output, helper));
		pack.addProvider(AtlasGenerator::new);
		pack.addProvider(CustomTagGenerator.BannerPatternTagGenerator::new);
		BlockTagGenerator blocktags = pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(FluidTagGenerator::new);
		pack.addProvider((output, provider) -> new ItemTagGenerator(output, provider, blocktags));
		pack.addProvider(EntityTagGenerator::new);
		pack.addProvider(CustomTagGenerator.EnchantmentTagGenerator::new);
		pack.addProvider(LootGenerator::new);
		pack.addProvider(CraftingGenerator::new);
		pack.addProvider(LootModifierGenerator::new);
		RegistryDataGenerator.addProviders(pack, helper);
		pack.addProvider(StructureTagGenerator::new);

		pack.addProvider((output, provider) -> new CrumbleHornGenerator(output, helper));
		pack.addProvider((output, provider) -> new TransformationPowderGenerator(output, helper));
		pack.addProvider((output, provider) -> new UncraftingRecipeGenerator(output, helper));
		pack.addProvider((output, provider) -> new StalactiteGenerator(output));
		pack.addProvider((output, provider) -> new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
						Component.literal("Resources for Twilight Forest"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES))));
	}
}
