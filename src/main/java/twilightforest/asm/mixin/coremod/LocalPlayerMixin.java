package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.EntityHooks;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

	@ModifyExpressionValue(
		method = "shouldStopSwimSprinting()Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"
		)
	)
	private boolean twilightforest$unrestrainedSprintingInWater(boolean original) {
		return EntityHooks.unrestrainedSprintingInWater(original, (LocalPlayer) (Object) this);
	}

	@ModifyExpressionValue(
		method = "aiStep()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;shouldStopSwimSprinting()Z"
		)
	)
	private boolean twilightforest$unrestrainedSwimPredicate(boolean original) {
		return EntityHooks.unrestrainedSprintingInWater(original, (LivingEntity) (Object) this);
	}
}