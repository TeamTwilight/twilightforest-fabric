package twilightforest.datagen.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.Minecraft;
import twilightforest.datagen.data.*;
import twilightforest.datagen.data.custom.QuestGenerator;
import twilightforest.datagen.data.custom.stalactites.StalactiteGenerator;
import twilightforest.datagen.data.loot.LootGenerator;
import twilightforest.datagen.data.recipes.CraftingGeneratorRunner;
import twilightforest.datagen.data.tags.*;

public class DataGenerator {
	public static final DataGenerator INSTANCE = new DataGenerator();

	public void generate(FabricDataGenerator.Pack pack) {
		pack.addProvider(BannerPatternTagGenerator::new);
		pack.addProvider(BiomeTagGenerator::new);
		pack.addProvider(BlockEntityTypeTagGenerator::new);
		pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(DamageTypeTagGenerator::new);
		pack.addProvider(DimensionTypeTagGenerator::new);
		pack.addProvider(EntityTypeTagGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(PaintingVariantTagGenerator::new);
		pack.addProvider(StructureTagGenerator::new);
		pack.addProvider(WoodPaletteTagGenerator::new);

		pack.addProvider(CraftingGeneratorRunner::new);
		pack.addProvider(LootGenerator::new);
		pack.addProvider(QuestGenerator::new);
		pack.addProvider(StalactiteGenerator::new);
		pack.addProvider(TFAdvancementProvider::new);
		pack.addProvider((output, _) -> new TFStructureUpdater("structures", output, Minecraft.getInstance().getResourceManager()));
	}
}
