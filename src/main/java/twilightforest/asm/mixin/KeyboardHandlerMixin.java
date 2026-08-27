package twilightforest.asm.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.TravellersClientEvents;

/**
 * Recreates NeoForge's InputEvent.Key for the travellers' gear keybinds,
 * after the vanilla key mappings have consumed the press.
 */
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

	@Inject(method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V", at = @At("TAIL"))
	private void twilightforest$travellersKeyEvents(long window, int action, KeyEvent event, CallbackInfo ci) {
		TravellersClientEvents.handleKeyEvent(action, event.key(), event.scancode());
	}
}
