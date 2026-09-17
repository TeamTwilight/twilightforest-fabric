package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.ArmorHooks;
import twilightforest.asm.hooks.coremod.BlockHooks;
import twilightforest.asm.hooks.coremod.EntityHooks;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@ModifyExpressionValue(
		method = "getVisibilityPercent(Lnet/minecraft/world/entity/Entity;)D",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;getArmorCoverPercentage()F"
		)
	)
	private float twilightforest$modifyArmorVisibility(float original) {
		return ArmorHooks.modifyArmorVisibility(original, (LivingEntity) (Object) this);
	}

	@ModifyExpressionValue(
		method = "travelInAir(Lnet/minecraft/world/phys/Vec3;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/Block;getFriction()F"
		)
	)
	private float twilightforest$resetBlockFrictionWithUnrestrained(float original) {
		return BlockHooks.resetBlockFrictionWithUnrestrained(original, (LivingEntity) (Object) this);
	}

	@ModifyReturnValue(
		method = "getLiquidCollisionShape()Lnet/minecraft/world/phys/shapes/VoxelShape;",
		at = @At("RETURN")
	)
	private VoxelShape twilightforest$processLiquidCollisionShape(VoxelShape original) {
		return EntityHooks.processLiquidCollisionShape(original, (LivingEntity) (Object) this);
	}

	@ModifyReturnValue(
		method = "canStandOnFluid",
		at = @At("RETURN")
	)
	private boolean twilightforest$processWaterWalking(
		boolean original,
		@Local(argsOnly = true, name = "fluid") FluidState fluid
	) {
		return EntityHooks.processWaterWalking(original, (LivingEntity) (Object) this, fluid);
	}
}