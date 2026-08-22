package twilightforest.asm.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.CommonHooks;

@Mixin(Slime.class)
public class SlimeMixin {

	@Inject(
		method = "jumpFromGround()V",
		at = @At("TAIL")
	)
	private void twilightforest$livingJump(CallbackInfo ci) {
		CommonHooks.onLivingJump((LivingEntity) (Object) this);
	}
}