package twilightforest.client.model.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.models.TransformTypeDependentItemBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Item model for giant tools (pickaxe, sword).
 * GUI: renders using the JSON model's element definitions (2D plane with zoomed UVs).
 * Other perspectives: delegates to the wrapped model with display transforms.
 */
public class GiantToolItemModel extends ForwardingBakedModel implements TransformTypeDependentItemBakedModel {

	public GiantToolItemModel(BakedModel originalModel) {
		this.wrapped = originalModel;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.wrapped.getTransforms();
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform, DefaultTransform transform) {
		if (transformType != ItemDisplayContext.GUI) {
			transform.apply(this);
		}
		// GUI: no rotation in code, JSON model handles display via its own elements
		return this;
	}

	// emitItemQuads: delegated to wrapped model (JSON model with correct UVs)
}