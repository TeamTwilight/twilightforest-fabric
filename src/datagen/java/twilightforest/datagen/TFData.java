package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;

public class TFData implements DataGeneratorEntrypoint {
	private final AssetsGenerator assetsGenerator = AssetsGenerator.INSTANCE;
	private final DataGenerator dataGenerator = DataGenerator.INSTANCE;

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		assetsGenerator.generate(pack);
		dataGenerator.generate(pack);
	}
}