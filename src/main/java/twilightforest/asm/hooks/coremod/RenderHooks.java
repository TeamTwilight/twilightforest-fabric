package twilightforest.asm.hooks.coremod;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;

public final class RenderHooks {
	public static final RenderStateDataKey<Boolean> HIDE_HEAD_KEY = RenderStateDataKey.create(() -> "hide_head");

	public static void applyHeadVisibility(ModelFeatureRenderer.Submit<?> submit) {
		setHeadVisibility(submit, false);
	}

	public static void restoreHeadVisibility(ModelFeatureRenderer.Submit<?> submit) {
		setHeadVisibility(submit, true);
	}

	private static void setHeadVisibility(ModelFeatureRenderer.Submit<?> submit, boolean visible) {
		if (!(submit.model() instanceof HeadedModel headed) || !(submit.state() instanceof EntityRenderState state) || !Boolean.TRUE.equals(state.getData(HIDE_HEAD_KEY)))
			return;

		headed.getHead().visible = visible;
	}
}