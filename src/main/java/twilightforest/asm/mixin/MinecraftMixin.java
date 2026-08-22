package twilightforest.asm.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.hooks.CommonHooks;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Inject(
		method = "startAttack()Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V",
			shift = At.Shift.AFTER
		)
	)
	private void twilightforest$leftClickEmpty(CallbackInfoReturnable<Boolean> cir) {
		CommonHooks.onEmptyLeftClick(this.player);
	}
}