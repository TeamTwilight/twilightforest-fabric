package twilightforest.asm.mixin.event;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.CloudBlock;

/**
 * Recreates NeoForge's LivingJumpEvent: when a player jumps off a cloud block
 * the cloud's movement particles are spawned.
 */
@Mixin(LivingEntity.class)
public class LivingJumpMixin {

	@Inject(method = "jumpFromGround()V", at = @At("RETURN"))
	private void twilightforest$cloudJump(CallbackInfo ci) {
		LivingEntity living = (LivingEntity) (Object) this;
		if (living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++)
				CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
		}
	}
}
