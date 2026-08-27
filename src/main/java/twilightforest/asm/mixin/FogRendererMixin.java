package twilightforest.asm.mixin;

import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.FogHandler;

import java.util.List;

/**
 * Registers the twilight forest fog environment into the vanilla fog pipeline.
 */
@Mixin(FogRenderer.class)
public class FogRendererMixin {

	@Shadow
	@Final
	private static List<FogEnvironment> FOG_ENVIRONMENTS;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void twilightforest$registerFogEnvironment(CallbackInfo ci) {
		FOG_ENVIRONMENTS.add(new FogHandler());
	}
}
