package twilightforest.data;

import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.nio.file.Paths;

import net.minecraft.SharedConstants;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.server.Bootstrap;

public class DataGenerators implements PreLaunchEntrypoint {

	public static void gatherData(DataGeneratorHandler handler) {
		handler.install(new AdvancementProvider(handler.getDataGenerator()));
//        handler.install(new BlockstateGenerator(generator, helper));
//        handler.install(new ItemModelGenerator(generator, helper));
		BlockTagsProvider blocktags = new BlockTagGenerator(handler.getDataGenerator());
        handler.install(blocktags);
        handler.install(new FluidTagGenerator(handler.getDataGenerator()));
        handler.install(new ItemTagGenerator(handler.getDataGenerator(), blocktags));
//        handler.install(new EntityTagGenerator(generator, helper));
//        handler.install(new LootGenerator(generator));
//        handler.install(new StonecuttingGenerator(generator));
//        handler.install(new CraftingGenerator(generator));
        handler.install(new TwilightWorldDataCompiler(handler.getDataGenerator()));
	}

	@Override
	public void onPreLaunch() {
		try {
			SharedConstants.tryDetectVersion();
			Bootstrap.bootStrap();
			DataGeneratorHandler handler = DataGeneratorHandler.create(Paths.get("../src/generated/resource"));
			gatherData(handler);
			handler.run();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			//System.exit(1);
		}
//		System.exit(0);
	}
}
