package twilightforest.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;

public class TFModelLoadingPlugin implements ModelLoadingPlugin {

	@Override
	public void initialize(Context pluginContext) {
		registerExtraModels(pluginContext);
	}

	private static void registerExtraModels(Context pluginContext) {
		pluginContext.addModel(ShieldLayer.SHIELD_MODEL, new SimpleUnbakedExtraModel<>(ShieldLayer.LOC, (resolvedModel, modelBaker) ->
			resolvedModel.bakeTopGeometry(resolvedModel.getTopTextureSlots(), modelBaker, BlockModelRotation.IDENTITY)
		));
		pluginContext.addModel(JarRenderer.JAR_MODEL, new SimpleUnbakedExtraModel<>(JarRenderer.JAR_MODEL_LOCATION, (_, modelBaker) ->
			SimpleModelWrapper.bake(modelBaker, JarRenderer.JAR_MODEL_LOCATION, BlockModelRotation.IDENTITY)
		));

		for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
			ExtraModelKey<BlockStateModelPart> key = JarRenderer.LIDS.get().get(lid.lid());
			if (key != null) {
				pluginContext.addModel(key, new SimpleUnbakedExtraModel<>(lid.modelLocation(), (_, modelBaker) ->
					SimpleModelWrapper.bake(modelBaker, lid.modelLocation(), BlockModelRotation.IDENTITY)
				));
			}
		}
	}
}