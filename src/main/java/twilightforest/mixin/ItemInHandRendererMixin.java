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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.OreMeterItem;

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
	private boolean twilightforest$isMapItem(
		ItemStack stack,
		Item item,
		Operation<Boolean> original
	) {
		if (item == Items.FILLED_MAP) {
			return original.call(stack, item) || stack.getItem() instanceof MapItem;
		}
		return original.call(stack, item);
	}

	@Inject(
		method = "tick",
		at = @At("HEAD")
	)
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

	@Unique
	private static boolean isOreMeter(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof OreMeterItem;
	}
}