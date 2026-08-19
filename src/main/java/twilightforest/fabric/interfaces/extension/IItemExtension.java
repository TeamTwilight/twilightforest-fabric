package twilightforest.fabric.interfaces.extension;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public interface IItemExtension {
	default int twilightforest$getMaxStackSize(ItemStack stack) {
		return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
	}
}