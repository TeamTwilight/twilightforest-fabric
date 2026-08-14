package carminite.item;

import net.minecraft.world.item.ItemStack;

public interface IContinuousUseItem {
	default boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return oldStack != newStack;
	}

	default boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		if (oldStack == newStack) {
			return true;
		} else {
			return !oldStack.isEmpty() && !newStack.isEmpty() && ItemStack.isSameItem(newStack, oldStack);
		}
	}
}