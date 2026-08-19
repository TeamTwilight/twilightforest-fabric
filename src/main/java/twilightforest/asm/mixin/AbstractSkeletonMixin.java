package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.interfaces.marker.ICustomArrowItem;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {

	@ModifyExpressionValue(
		method = "performRangedAttack(Lnet/minecraft/world/entity/LivingEntity;F)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/skeleton/AbstractSkeleton;getArrow(Lnet/minecraft/world/item/ItemStack;FLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;"
		)
	)
	private AbstractArrow twilightforest$customArrow(
		AbstractArrow value,
		@Local(name = "bowItem") ItemStack bowItem,
		@Local(name = "projectile") ItemStack projectile
	) {
		if (bowItem.getItem() instanceof ICustomArrowItem customArrowItem) {
			return customArrowItem.customArrow(value, projectile, bowItem);
		}
		return value;
	}
}