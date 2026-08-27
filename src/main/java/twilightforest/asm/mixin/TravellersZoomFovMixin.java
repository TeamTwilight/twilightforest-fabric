package twilightforest.asm.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.event.TravellersClientEvents;

/**
 * Applies the travellers' goggles zoom to the FOV modifier (formerly
 * ComputeFovModifierEvent).
 */
@Mixin(AbstractClientPlayer.class)
public class TravellersZoomFovMixin {

	@Inject(method = "getFieldOfViewModifier(ZF)F", at = @At("RETURN"), cancellable = true)
	private void twilightforest$gogglesZoom(boolean usingFOV, float partialTick, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(TravellersClientEvents.updateZoomState(cir.getReturnValue()));
	}
}
