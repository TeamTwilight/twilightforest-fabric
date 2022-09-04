package twilightforest.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TFEventListener;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "die", at = @At("HEAD"))
	private void twilightforest$onDeath(DamageSource damageSource, CallbackInfo ci) {
		TFEventListener.onPlayerActuallyDead((ServerPlayer) (Object) this);
	}
}
