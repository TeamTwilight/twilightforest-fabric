package twilightforest.asm.mixin.coremod;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.coremod.RenderHooks;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {

	@Inject(
		method = "renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V",
			shift = At.Shift.AFTER
		)
	)
	private <S> void twilightforest$applyHeadVisibility(
		SubmitNodeStorage.ModelSubmit<S> submit,
		RenderType renderType,
		VertexConsumer buffer,
		OutlineBufferSource outlineBufferSource,
		MultiBufferSource.BufferSource crumblingBufferSource,
		CallbackInfo ci
	) {
		RenderHooks.applyHeadVisibility(submit);
	}

	@Inject(
		method = "renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"
		)
	)
	private <S> void twilightforest$restoreHeadVisibility(
		SubmitNodeStorage.ModelSubmit<S> submit,
		RenderType renderType,
		VertexConsumer buffer,
		OutlineBufferSource outlineBufferSource,
		MultiBufferSource.BufferSource crumblingBufferSource,
		CallbackInfo ci
	) {
		RenderHooks.restoreHeadVisibility(submit);
	}
}