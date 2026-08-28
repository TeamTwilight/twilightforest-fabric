package twilightforest.asm.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDimension;

/**
 * Recreates NeoForge's RenderFrameEvent.Pre handling for the twilight forest:
 * the vignette is never shown in the twilight dimension.
 */
@Mixin(Gui.class)
public class GuiVignetteMixin {

	@Inject(method = "updateVignetteBrightness(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
	private void twilightforest$disableVignette(Entity entity, CallbackInfo ci) {
		if (TFDimension.DIMENSION_KEY.equals(entity.level().dimension())) {
			((Gui) (Object) this).vignetteBrightness = 0.0F;
		}
	}
}
