package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.RegistryDependentFactory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

import java.util.concurrent.CompletableFuture;

public class DataGenerators implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator gen) {
		Pack pack = gen.createPack();
		ExistingFileHelper efh = ExistingFileHelper.withResourcesFromArg();
		ProviderProvider generator = new ProviderProvider(pack, efh);

		generator.addProvider(TFAdvancementGenerator::new);
		generator.addProvider(BlockstateGenerator::new);
		generator.addProvider(ItemModelGenerator::new);
		generator.addProvider(SoundGenerator::new);
		generator.addProvider(CustomTagGenerator.BannerPatternTagGenerator::new);
		BlockTagGenerator blocktags = generator.addProvider(BlockTagGenerator::new);
		generator.addProvider(CustomTagGenerator.BlockEntityTagGenerator::new);
		generator.addProvider(FluidTagGenerator::new);
		generator.addProvider((output, provider, helper) -> new ItemTagGenerator(output, provider, blocktags));
		generator.addProvider(EntityTagGenerator::new);
		generator.addProvider(CustomTagGenerator.EnchantmentTagGenerator::new);
		generator.addProvider(LootGenerator::new);
		generator.addProvider(CraftingGenerator::new);
		generator.addProvider(LootModifierGenerator::new);

		RegistryDataGenerator datapackProvider = generator.addProvider(RegistryDataGenerator::new);
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		generator.addProvider((output, provider, helper) -> new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider, helper));
		generator.addProvider((output, provider, helper) -> new BiomeTagGenerator(output, lookupProvider, helper));
		generator.addProvider((output, provider, helper) -> new DamageTypeTagGenerator(output, lookupProvider, helper));
		generator.addProvider((output) -> new StructureTagGenerator(output, lookupProvider));

		generator.addProvider(CrumbleHornGenerator::new);
		generator.addProvider(TransformationPowderGenerator::new);
		generator.addProvider(StalactiteGenerator::new);

		generator.addProvider(AtlasGenerator::new);
		generator.addProvider(LangGenerator::new);
	}

	private record ProviderProvider(Pack pack, ExistingFileHelper helper) {
		public <T extends DataProvider> T addProvider(Factory<T> factory) {
			return pack.addProvider(factory);
		}

		public <T extends DataProvider> T addProvider(RegistryDependentFactory<T> factory) {
			return pack.addProvider(factory);
		}

		public <T extends DataProvider> T addProvider(EFHDependentFactory<T> factory) {
			return pack.addProvider((FabricDataOutput output) -> factory.create(output, helper));
		}

		public <T extends DataProvider> T addProvider(EFHRegistryDependentFactory<T> factory) {
			return pack.addProvider((output, registries) -> factory.create(output, registries, helper));
		}
	}

	@FunctionalInterface
	public interface EFHDependentFactory<T extends DataProvider> {
		T create(FabricDataOutput output, ExistingFileHelper helper);
	}

	@FunctionalInterface
	public interface EFHRegistryDependentFactory<T extends DataProvider> {
		T create(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper);
	}
}
