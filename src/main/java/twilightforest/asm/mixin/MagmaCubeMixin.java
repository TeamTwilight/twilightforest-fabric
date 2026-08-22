package twilightforest.asm.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.CommonHooks;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

	@Inject(
		method = "jumpFromGround()V",
		at = @At("TAIL")
	)
	private void twilightforest$livingJump(CallbackInfo ci) {
		CommonHooks.onLivingJump((LivingEntity) (Object) this);
	}
}