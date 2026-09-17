package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.ArmorHooks;
import twilightforest.asm.hooks.coremod.BlockHooks;

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
}