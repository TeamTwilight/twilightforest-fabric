package twilightforest.fabric.models;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import net.minecraft.client.resources.model.UnbakedModel;

public interface TFModelResolver extends ModelResolver {
	@Override
	@Nullable
	default UnbakedModel resolveModel(Context context) {
		try {
			return tryResolveModel(context);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Error loading model " + context.id(), e);
			return null;
		}
	}

	@Nullable
	UnbakedModel tryResolveModel(Context context) throws Exception;
}
