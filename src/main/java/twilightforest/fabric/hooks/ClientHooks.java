package twilightforest.fabric.hooks;

import twilightforest.fabric.interfaces.marker.IContinuousUseItem;
import net.minecraft.world.item.ItemStack;

public class ClientHooks {
	private static int slotMainHand = 0;

	public static boolean shouldCauseReequipAnimation(ItemStack from, ItemStack to, int slot) {
		boolean fromInvalid = from.isEmpty();
		boolean toInvalid = to.isEmpty();

		if (fromInvalid && toInvalid) return false;
		if (fromInvalid || toInvalid) return true;

		boolean changed = false;
		if (slot != -1) {
			changed = slot != slotMainHand;
			slotMainHand = slot;
		}

		return ((IContinuousUseItem) from.getItem()).shouldCauseReequipAnimation(from, to, changed);
	}
}