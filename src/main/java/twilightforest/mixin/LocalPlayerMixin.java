package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.EntityHooks;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

	@ModifyExpressionValue(
		method = "aiStep()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"
		)
	)
	private boolean twilightforest$unrestrainedSprintingInWater(
		boolean original
	) {
		return EntityHooks.unrestrainedSprintingInWater(
			original,
			(LocalPlayer)(Object)this
		);
	}
}
