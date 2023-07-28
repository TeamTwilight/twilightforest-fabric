package twilightforest.fabric.models;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import twilightforest.client.TFClientEvents;

public enum TFModelLoadingPlugin implements ModelLoadingPlugin {
	INSTANCE;

	@Override
	public void onInitializeModelLoader(Context ctx) {
		TFClientEvents.ModBusEvents.registerLoaders(ctx.resolveModel()::register);
		TFClientEvents.ModBusEvents.registerModels(ctx::addModels);
	}
}
