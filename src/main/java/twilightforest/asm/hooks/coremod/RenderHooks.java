package twilightforest.asm.hooks.coremod;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public final class RenderHooks {
	public static final RenderStateDataKey<Boolean> HIDE_HEAD_KEY = RenderStateDataKey.create(() -> "hide_head");

	public static void applyHeadVisibility(SubmitNodeStorage.ModelSubmit<?> submit) {
		if (!(submit.model() instanceof HeadedModel headed) || !(submit.state() instanceof EntityRenderState state))
			return;

		headed.getHead().visible = !Boolean.TRUE.equals(state.getData(HIDE_HEAD_KEY));
	}
}