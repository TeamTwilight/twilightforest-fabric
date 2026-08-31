package twilightforest.asm.mixin.event;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(Player.class)
public class PlayerMixin {

	@Inject(
		method = "tick()V",
		at = @At("HEAD")
	)
	private void twilightforest$playerTickPre(CallbackInfo ci) {
		Player self = (Player) (Object) this;

		// PlayerTickEvent.Pre events go here...
		TravellersGearEventHooks.tickMovementModifiers(self);
		TravellersGearEventHooks.disableHighStepWhileSneaking(self);
	}

	@Inject(
		method = "tick()V",
		at = @At("TAIL")
	)
	private void twilightforest$playerTickPost(CallbackInfo ci) {
		Player self = (Player) (Object) this;

		// PlayerTickEvent.Post events go here...
		TravellersGearEventHooks.performStealth(self);
	}
}