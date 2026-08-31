package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class TFArmorRenderer implements ArmorRenderer {
	public static final List<TFSimpleArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Map<ModelLayerLocation, ModelPart> ARMOR_MODELS = new HashMap<>();

	public TFArmorRenderer(ModelLayerLocation... layerLocations) {
		for (ModelLayerLocation layerLocation : layerLocations) {
			ARMOR_MODELS.put(layerLocation, bakeLayer(layerLocation));
		}
	}

	private static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
		return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
	}

	public void resetModelCache() {
		ARMOR_MODELS.replaceAll((layer, model) -> bakeLayer(layer));
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFArmorRenderer::resetModelCache);
	}

	protected ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.get(layerLocation);
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			TFArmorRenderer.resetAllModelCache();
		}
	}
}
