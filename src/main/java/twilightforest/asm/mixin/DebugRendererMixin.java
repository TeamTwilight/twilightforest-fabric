package twilightforest.asm.mixin;

import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.MultipartRenderDispatcher;

import java.util.List;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

	@Shadow
	@Final
	private List<DebugRenderer.SimpleDebugRenderer> renderers;

	@Inject(
		method = "refreshRendererList()V",
		at = @At("TAIL")
	)
	private void twilightforest$addMultipartRenderDispatcher(CallbackInfo ci) {
		this.renderers.add(MultipartRenderDispatcher.INSTANCE);
	}
}