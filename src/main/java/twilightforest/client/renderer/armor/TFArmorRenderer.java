package twilightforest.client.renderer.armor;

import carminite.util.Lazy;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TFArmorRenderer implements ArmorRenderer {
	public static final List<TFSimpleArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Map<ModelLayerLocation, Lazy<ModelPart>> ARMOR_MODELS = new HashMap<>();
	protected final EquipmentLayerRenderer equipmentRenderer;

	public TFArmorRenderer(EntityRendererProvider.Context context, ModelLayerLocation... layerLocations) {
		this.equipmentRenderer = context.getEquipmentRenderer();
		for (ModelLayerLocation layerLocation : layerLocations) {
			this.ARMOR_MODELS.put(
				layerLocation,
				Lazy.of(() -> Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation))
			);
		}
	}

	public void resetModelCache() {
		ARMOR_MODELS.values().forEach(Lazy::invalidate);
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFSimpleArmorRenderer::resetModelCache);
	}

	protected ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.get(layerLocation).get();
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			TFArmorRenderer.resetAllModelCache();
		}
	}
}