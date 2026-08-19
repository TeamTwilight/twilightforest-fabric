package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.interfaces.marker.ICustomArrowItem;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

	@ModifyReturnValue(
		method = "createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;",
		at = @At("RETURN")
	)
	private Projectile twilightforest$customArrow(
		Projectile original,
		Level level,
		LivingEntity shooter,
		ItemStack weapon,
		ItemStack projectile
	) {
		if (this instanceof ICustomArrowItem customArrowItem && original instanceof AbstractArrow arrow) {
			return customArrowItem.customArrow(arrow, projectile, weapon);
		}
		return original;
	}
}