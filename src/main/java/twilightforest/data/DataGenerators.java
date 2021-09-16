package twilightforest.data;

import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.nio.file.Paths;

import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.server.Bootstrap;

public class DataGenerators {

	public static void gatherData(DataGeneratorHandler handler) {
		handler.install(new AdvancementProvider(handler.getDataGenerator()));
        //handler.install(new BlockstateGenerator(generator, helper));
        //handler.install(new ItemModelGenerator(handler.getDataGenerator()));
		BlockTagsProvider blocktags = new BlockTagGenerator(handler.getDataGenerator());
        handler.install(blocktags);
        handler.install(new FluidTagGenerator(handler.getDataGenerator()));
        handler.install(new ItemTagGenerator(handler.getDataGenerator(), blocktags));
        handler.install(new EntityTagGenerator(handler.getDataGenerator()));
        handler.install(new LootGenerator(handler.getDataGenerator()));
        handler.install(new StonecuttingGenerator(handler.getDataGenerator()));
        //handler.install(new CraftingGenerator(handler.getDataGenerator()));
        handler.install(new TwilightWorldDataCompiler(handler.getDataGenerator()));
	}

	public static int run() {
		try {
			SharedConstants.tryDetectVersion();
			Bootstrap.bootStrap();
			DataGeneratorHandler handler = DataGeneratorHandler.create(Paths.get("../src/generated/resource"));
			gatherData(handler);
			handler.run();
			return 0;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return -1;
			//System.exit(1);
		}
//		System.exit(0);
	}
}
