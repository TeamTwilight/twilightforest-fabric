package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import twilightforest.TwilightForestMod;

public final class CodexTwilightDataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		DataGenerators.addToPack(pack);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		DataGenerators.addToRegistryBuilder(registryBuilder);
	}

	@Override
	public String getEffectiveModId() {
		return TwilightForestMod.ID;
	}

}
