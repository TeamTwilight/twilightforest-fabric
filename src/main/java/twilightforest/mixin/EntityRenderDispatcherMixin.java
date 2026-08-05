package twilightforest.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.BakedMultiPartRenderers;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Inject(
		method = "onResourceManagerReload",
		at = @At("TAIL")
	)
	private void twilightforest$bakeMultipartRenderers(
		ResourceManager resourceManager,
		CallbackInfo ci,
		@Local EntityRendererProvider.Context context
	) {
		BakedMultiPartRenderers.bakeMultiPartRenderers(context);
	}
}
