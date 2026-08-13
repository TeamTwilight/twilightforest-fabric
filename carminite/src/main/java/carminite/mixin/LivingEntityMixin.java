package carminite.mixin;

import carminite.block.ISpecialLandingEffectsBlock;
import carminite.block.ISpecialScaffoldingBlock;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@WrapOperation(
		method = "checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"
		)
	)
	private <T extends ParticleOptions> int carminite$addLandingEffects(
		ServerLevel instance,
		T particle,
		double x,
		double y,
		double z,
		int count,
		double xDist,
		double yDist,
		double zDist,
		double speed,
		Operation<Integer> original,
		@Local(argsOnly = true, name = "onState") BlockState onState,
		@Local(argsOnly = true, name = "pos") BlockPos pos
	) {
		if (onState.getBlock() instanceof ISpecialLandingEffectsBlock specialLandingEffectsBlock) {
			if (!specialLandingEffectsBlock.addLandingEffects(onState, instance, pos, onState, (LivingEntity) (Object) this, count)) {
				return original.call(instance, particle, x, y, z, count, xDist, yDist, zDist, speed);
			} else {
				return 0;
			}
		}
		return original.call(instance, particle, x, y, z, count, xDist, yDist, zDist, speed);
	}

	@ModifyExpressionValue(
		method = "handleOnClimbable(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"
		)
	)
	private boolean customScaffoldingMovement(boolean original) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		BlockState state = livingEntity.getInBlockState();
		if (state.getBlock() instanceof ISpecialScaffoldingBlock specialScaffoldingBlock) {
			return specialScaffoldingBlock.isScaffolding(state, livingEntity.level(), livingEntity.blockPosition(), livingEntity);
		}
		return original;
	}
}