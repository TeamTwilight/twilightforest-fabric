package twilightforest.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.PlayerHooks;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

	@Inject(
		method = "getFieldOfViewModifier()F",
		at = @At("HEAD")
	)
	private void twilightforest$straightAheadNullify(CallbackInfoReturnable<Float> cir) {
		PlayerHooks.straightAheadNullify((AbstractClientPlayer) (Object) this);
	}

	@Inject(
		method = "getFieldOfViewModifier()F",
		at = @At("RETURN")
	)
	private void twilightforest$straightAheadRestore(CallbackInfoReturnable<Float> cir) {
		PlayerHooks.straightAheadRestore((AbstractClientPlayer) (Object) this);
	}
}
