package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.OreMeterItem;

/**
 * Makes TF custom map items (MagicMapItem, MazeMapItem) render as maps in-hand.
 * In 1.21.1, ItemInHandRenderer checks stack.is(Items.FILLED_MAP) to decide
 * whether to render a map in the player's hand. We extend this to also match
 * any MapItem subclass.
 */
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private ItemStack mainHandItem;

	@Shadow
	private ItemStack offHandItem;

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

	/**
	 * Prevents the "item re-equip" animation from playing when an Ore Meter's data
	 * components change while held.
	 *
	 * <p>The Ore Meter updates its loading progress (and other scanning data) on the
	 * server every tick, which syncs the held slot to the client with a fresh
	 * ItemStack instance every tick. In 1.21.1 ItemInHandRenderer#tick decides
	 * whether to drop/raise the held item animation by comparing the cached stack
	 * reference against the current one, so any new instance (even of the same
	 * item) makes the item look like it is being picked up again over and over.
	 * This mirrors the NeoForge port, which overrides shouldCauseReequipAnimation
	 * to only animate when the slot changes or the item type changes.
	 */
	@Inject(method = "tick", at = @At("HEAD"))
	private void twilightforest$preventOreMeterReequipAnimation(CallbackInfo ci) {
		Player player = this.minecraft.player;
		if (player == null)
			return;

		ItemStack handStack = player.getMainHandItem();
		if (isOreMeter(handStack) && isOreMeter(this.mainHandItem)) {
			this.mainHandItem = handStack;
		}

		ItemStack offHandStack = player.getOffhandItem();
		if (isOreMeter(offHandStack) && isOreMeter(this.offHandItem)) {
			this.offHandItem = offHandStack;
		}
	}

	private static boolean isOreMeter(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof OreMeterItem;
	}
}