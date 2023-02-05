package twilightforest.data;

import com.mojang.serialization.Lifecycle;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.biome.Biomes;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.custom.UncraftingRecipeGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

import java.util.concurrent.CompletableFuture;

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
		generator.addProvider((output, provider) -> new ItemTagGenerator(output, provider, blocktags));
		generator.addProvider(EntityTagGenerator::new);
		generator.addProvider(event.includeServer(), new CustomTagGenerator.EnchantmentTagGenerator(output, provider, helper));
		pack.addProvider(LootGenerator::new);
		generator.addProvider(event.includeServer(), new CraftingGenerator(output));
		generator.addProvider(event.includeServer(), new LootModifierGenerator(output));
		pack.addProvider(WorldGenerator::new);

		generator.addProvider(event.includeServer(), new CrumbleHornGenerator(output, helper));
		generator.addProvider(event.includeServer(), new TransformationPowderGenerator(output, helper));
		generator.addProvider(event.includeServer(), new UncraftingRecipeGenerator(output, helper));
		generator.addProvider(event.includeServer(), new StalactiteGenerator(output));
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.BIOME, Lifecycle.stable(), Biomes::bootstrap);
	}
}
