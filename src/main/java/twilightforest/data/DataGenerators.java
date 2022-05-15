package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.tags.*;

public class DataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		ExistingFileHelper helper = null;//evt.getExistingFileHelper();

		generator.addProvider(new AdvancementGenerator(generator, helper));
		generator.addProvider(new PatchouliAdvancementGenerator(generator));
		generator.addProvider(new BlockstateGenerator(generator, helper));
		generator.addProvider(new ItemModelGenerator(generator, helper));
		generator.addProvider(new BiomeTagGenerator(generator, helper));
		BlockTagsProvider blocktags = new BlockTagGenerator(generator, helper);
		generator.addProvider(blocktags);
		generator.addProvider(new FluidTagGenerator(generator, helper));
		generator.addProvider(new ItemTagGenerator(generator, blocktags, helper));
		generator.addProvider(new EntityTagGenerator(generator, helper));
		generator.addProvider(new CustomTagGenerator.EnchantmentTagGenerator(generator, helper));
		generator.addProvider(new LootGenerator(generator));
		generator.addProvider(new StonecuttingGenerator(generator));
		generator.addProvider(new CraftingGenerator(generator));
		generator.addProvider(new WorldGenerator(generator));

		generator.addProvider(new CrumbleHornGenerator(generator, helper));
		generator.addProvider(new TransformationPowderGenerator(generator, helper));

		if(ModList.get().isLoaded(TFCompat.TCON_ID)) {
			TConCompat.tConDatagen(evt);
		}
	}
}
