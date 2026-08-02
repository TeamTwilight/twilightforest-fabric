package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Makes TF custom map items (MagicMapItem, MazeMapItem) render as maps in-hand.
 * In 1.21.1, ItemInHandRenderer checks stack.is(Items.FILLED_MAP) to decide
 * whether to render a map in the player's hand. We extend this to also match
 * any MapItem subclass.
 */
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@WrapOperation(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
		)
	)
	private boolean twilightforest$isMapItem(ItemStack stack, Item item, Operation<Boolean> original) {
		if (item == Items.FILLED_MAP) {
			return original.call(stack, item) || stack.getItem() instanceof MapItem;
		}
		return original.call(stack, item);
	}
}