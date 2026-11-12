package twilightforest.asm.mixin.coremod;

import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.coremod.RenderHooks;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {

	@Inject(
		method = "prepareModel(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V",
			shift = At.Shift.AFTER
		)
	)
	private <S> void twilightforest$applyHeadVisibility(
		ModelFeatureRenderer.Submit<S> submit,
		CallbackInfo ci
	) {
		RenderHooks.applyHeadVisibility(submit);
	}

	@Inject(
		method = "prepareModel(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V",
		at = @At("TAIL")
	)
	private <S> void twilightforest$restoreHeadVisibility(
		ModelFeatureRenderer.Submit<S> submit,
		CallbackInfo ci
	) {
		RenderHooks.restoreHeadVisibility(submit);
	}
}