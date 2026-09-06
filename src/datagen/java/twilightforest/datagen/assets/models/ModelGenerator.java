package twilightforest.datagen.assets.models;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class ModelGenerator extends FabricModelProvider {
	public ModelGenerator(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
		new BlockModelGenerator(
			blockModelGenerators.blockStateOutput,
			blockModelGenerators.itemModelOutput,
			blockModelGenerators.modelOutput
		).run();
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerators) {
		new ItemModelGenerator(
			itemModelGenerators.itemModelOutput,
			itemModelGenerators.modelOutput
		).run();
	}
}