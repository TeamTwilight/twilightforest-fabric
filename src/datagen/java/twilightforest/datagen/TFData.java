package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;

public final class TFData implements DataGeneratorEntrypoint {

	/*
	@Autowired
	private AssetsGenerator assetsGenerator;

	@Autowired
	private DataGenerator dataGenerator;

	@PostConstruct
	private void register(IEventBus bus) {
		bus.addListener(GatherDataEvent.Client.class, event -> {
			this.dataGenerator.generate(event);
			this.assetsGenerator.generate(event);
		});
	}
	 */

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
	}
}
