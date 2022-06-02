package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.compat.TConCompat;
import twilightforest.compat.TFCompat;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.tags.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class DataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var existingData = System.getProperty("twilightforest.data.existingData").split(";");
		var helper = new ExistingFileHelper(Arrays.stream(existingData).map(Paths::get).toList(), Collections.emptySet(),
				true, null, null);

		generator.addProvider(new AdvancementGenerator(generator));
		generator.addProvider(new PatchouliAdvancementGenerator(generator));
		generator.addProvider(new BlockstateGenerator(generator, helper));
		generator.addProvider(new ItemModelGenerator(generator, helper));
		generator.addProvider(new BiomeTagGenerator(generator));
		FabricTagProvider.BlockTagProvider blocktags = new BlockTagGenerator(generator);
		generator.addProvider(blocktags);
		generator.addProvider(new FluidTagGenerator(generator));
		generator.addProvider(new ItemTagGenerator(generator, blocktags));
		generator.addProvider(new EntityTagGenerator(generator));
		generator.addProvider(new CustomTagGenerator.EnchantmentTagGenerator(generator));
		generator.addProvider(new LootGenerator(generator));
		generator.addProvider(new StonecuttingGenerator(generator));
		generator.addProvider(new CraftingGenerator(generator));
		generator.addProvider(new WorldGenerator(generator));

		generator.addProvider(new CrumbleHornGenerator(generator, helper));
		generator.addProvider(new TransformationPowderGenerator(generator, helper));

		if(FabricLoader.getInstance().isModLoaded(TFCompat.TCON_ID)) {
			TConCompat.tConDatagen(generator);
		}
	}
}
