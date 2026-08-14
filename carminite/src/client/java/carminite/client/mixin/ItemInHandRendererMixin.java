package carminite.client.mixin;

import carminite.client.hooks.ClientHooks;
import carminite.item.IContinuousUseItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
			ordinal = 0
		)
	)
	private boolean carminite$shouldCauseReequipAnimationFirst(
		ItemInHandRenderer instance,
		ItemStack currentlyVisibleItem,
		ItemStack expectedItem,
		Operation<Boolean> original,
		@Local(name = "player") LocalPlayer player
	) {
		if (!(currentlyVisibleItem.getItem() instanceof IContinuousUseItem)) {
			return original.call(instance, currentlyVisibleItem, expectedItem);
		}
		return !ClientHooks.shouldCauseReequipAnimation(
			currentlyVisibleItem,
			expectedItem,
			player.getInventory().getSelectedSlot()
		);
	}

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
			ordinal = 1
		)
	)
	private boolean carminite$shouldCauseReequipAnimationSecond(
		ItemInHandRenderer instance,
		ItemStack currentlyVisibleItem,
		ItemStack expectedItem,
		Operation<Boolean> original
	) {
		if (!(currentlyVisibleItem.getItem() instanceof IContinuousUseItem)) {
			return original.call(instance, currentlyVisibleItem, expectedItem);
		}
		return !ClientHooks.shouldCauseReequipAnimation(
			currentlyVisibleItem,
			expectedItem,
			-1
		);
	}
}