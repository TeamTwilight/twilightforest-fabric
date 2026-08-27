package twilightforest.asm.mixin;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.client.event.TravellersClientEvents;

/**
 * Recreates NeoForge's CalculatePlayerTurnEvent: while the goggles zoom is
 * active the mouse sensitivity is scaled down.
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Redirect(method = "turnPlayer(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
	private Object twilightforest$zoomSensitivity(OptionInstance<?> instance) {
		Object value = instance.get();
		if (value instanceof Double sensitivity) {
			return TravellersClientEvents.slowZoomSensitivity(sensitivity);
		}
		return value;
	}
}
