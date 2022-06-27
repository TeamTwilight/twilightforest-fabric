package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
		ExistingFileHelper helper = new ExistingFileHelper(Arrays.stream(existingData).map(Paths::get).toList(), Collections.emptySet(),
				true, null, null);

		generator.addProvider(true, new AdvancementGenerator(generator));
		generator.addProvider(true, new PatchouliAdvancementGenerator(generator));
		generator.addProvider(true, new BlockstateGenerator(generator, helper));
		generator.addProvider(true, new ItemModelGenerator(generator, helper));
		generator.addProvider(true, new BiomeTagGenerator(generator));
		generator.addProvider(true, new CustomTagGenerator.BannerPatternTagGenerator(generator));
		FabricTagProvider.BlockTagProvider blocktags = new BlockTagGenerator(generator);
		generator.addProvider(true, blocktags);
		generator.addProvider(true, new FluidTagGenerator(generator));
		generator.addProvider(true, new ItemTagGenerator(generator, blocktags));
		generator.addProvider(true, new EntityTagGenerator(generator));
		generator.addProvider(true, new CustomTagGenerator.EnchantmentTagGenerator(generator));
		generator.addProvider(true, new LootGenerator(generator));
		generator.addProvider(true, new StonecuttingGenerator(generator));
		generator.addProvider(true, new CraftingGenerator(generator));
		generator.addProvider(true, new WorldGenerator(generator));
		generator.addProvider(true, new LootModifierGenerator(generator));

		generator.addProvider(true, new CrumbleHornGenerator(generator, helper));
		generator.addProvider(true, new TransformationPowderGenerator(generator, helper));

//		if(ModList.get().isLoaded(TFCompat.TCON_ID)) {
//			TConCompat.tConDatagen(evt);
//		}
	}
}
