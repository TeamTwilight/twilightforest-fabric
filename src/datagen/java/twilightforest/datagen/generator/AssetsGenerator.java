package twilightforest.datagen.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import twilightforest.datagen.assets.*;
import twilightforest.datagen.assets.models.ModelGenerator;

public class AssetsGenerator {
	public static final AssetsGenerator INSTANCE = new AssetsGenerator();

	public void generate(FabricDataGenerator.Pack pack) {
		pack.addProvider(AtlasGenerator::new);
		pack.addProvider(EquipmentAssetsGenerator::new);
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(ParticleGenerator::new);
		pack.addProvider(SoundGenerator::new);
		pack.addProvider(LangGenerator::new);
	}
}