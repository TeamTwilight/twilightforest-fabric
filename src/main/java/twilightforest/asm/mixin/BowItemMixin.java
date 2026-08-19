package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(BowItem.class)
public class BowItemMixin {

	@Inject(
		method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"
		),
		cancellable = true
	)
	private void twilightforest$onArrowLoose(
		ItemStack itemStack,
		Level level,
		LivingEntity entity,
		int remainingTime,
		CallbackInfoReturnable<Boolean> cir,
		@Local(name = "player") Player player,
		@Local(name = "projectile") ItemStack projectile,
		@Local(name = "timeHeld") LocalIntRef timeHeld
	) {
		int modifiedTimeHeld = EventHooks.onArrowLoose(
			itemStack,
			level,
			player,
			timeHeld.get(),
			!projectile.isEmpty()
		);

		if (modifiedTimeHeld < 0) {
			cir.setReturnValue(false);
			return;
		}

		timeHeld.set(modifiedTimeHeld);
	}
}