package twilightforest.asm.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.TravellersClientEvents;

/**
 * Recreates NeoForge's MovementInputUpdateEvent for the travellers' gear
 * movement modifiers, applied right after the client input is ticked.
 */
@Mixin(LocalPlayer.class)
public class LocalPlayerInputMixin {

	@Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = At.Shift.AFTER))
	private void twilightforest$applyTravellersInput(CallbackInfo ci) {
		LocalPlayer player = (LocalPlayer) (Object) this;
		ClientInput input = player.input;
		Vec2 modified = TravellersClientEvents.applyTravellersInput(input.getMoveVector());
		((ClientInputAccessor) input).twilightforest$setMoveVector(modified);
	}
}
