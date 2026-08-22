package twilightforest.asm.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.CommonHooks;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {

	@Inject(
		method = "executeRidersJump(FLnet/minecraft/world/phys/Vec3;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;needsSync:Z",
			shift = At.Shift.AFTER,
			opcode = Opcodes.PUTFIELD
		)
	)
	private void twilightforest$livingJump(CallbackInfo ci) {
		CommonHooks.onLivingJump((LivingEntity) (Object) this);
	}
}