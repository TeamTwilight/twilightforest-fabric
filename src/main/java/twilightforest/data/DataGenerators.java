package twilightforest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.tags.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		ExistingFileHelper helper = evt.getExistingFileHelper();

		generator.addProvider(true, new AdvancementGenerator(generator, helper));
		generator.addProvider(true, new PatchouliAdvancementGenerator(generator, helper));
		generator.addProvider(true, new BlockstateGenerator(generator, helper));
		generator.addProvider(true, new ItemModelGenerator(generator, helper));
		generator.addProvider(true, new BiomeTagGenerator(generator, helper));
		generator.addProvider(true, new CustomTagGenerator.BannerPatternTagGenerator(generator, helper));
		BlockTagsProvider blocktags = new BlockTagGenerator(generator, helper);
		generator.addProvider(true, blocktags);
		generator.addProvider(true, new FluidTagGenerator(generator, helper));
		generator.addProvider(true, new ItemTagGenerator(generator, blocktags, helper));
		generator.addProvider(true, new EntityTagGenerator(generator, helper));
		generator.addProvider(true, new CustomTagGenerator.EnchantmentTagGenerator(generator, helper));
		generator.addProvider(true, new LootGenerator(generator));
		generator.addProvider(true, new StonecuttingGenerator(generator));
		generator.addProvider(true, new CraftingGenerator(generator));
		generator.addProvider(true, new WorldGenerator(generator));

		generator.addProvider(true, new CrumbleHornGenerator(generator, helper));
		generator.addProvider(true, new TransformationPowderGenerator(generator, helper));

//		if(ModList.get().isLoaded(TFCompat.TCON_ID)) {
//			TConCompat.tConDatagen(evt);
//		}
	}
}
