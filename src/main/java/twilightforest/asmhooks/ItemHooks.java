package twilightforest.asmhooks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;

public class ItemHooks {

	public static Component modifyWrittenBookName(Component component, ItemStack stack) {
		if (stack.has(TFDataComponents.TRANSLATABLE_BOOK.get())) {
			return Component.translatable(component.getString());
		} else return component;
	}
}
