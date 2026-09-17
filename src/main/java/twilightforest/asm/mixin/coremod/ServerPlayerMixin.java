package twilightforest.asm.mixin.coremod;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asm.hooks.coremod.PlayerHooks;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

	@ModifyArg(
		method = {
			"checkMovementStatistics(DDD)V",
			"jumpFromGround()V"
		},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"
		)
	)
	private float twilightforest$getFoodExhaustion(float par1) {
		return PlayerHooks.getFoodExhaustion(par1, (ServerPlayer) (Object) this);
	}
}