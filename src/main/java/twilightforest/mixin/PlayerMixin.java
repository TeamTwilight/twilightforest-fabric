package twilightforest.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import twilightforest.asmhooks.PlayerHooks;

@Mixin(Player.class)
public class PlayerMixin {

	@ModifyVariable(
		method = "causeFoodExhaustion",
		at = @At("HEAD"),
		argsOnly = true
	)
	private float twilightforest$modifyFoodExhaustion(float exhaustion) {
		return PlayerHooks.getFoodExhaustion(exhaustion, (Player) (Object) this);
	}
}