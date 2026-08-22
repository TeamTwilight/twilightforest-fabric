package twilightforest.fabric.interfaces.extension;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IItemExtension {
	default int twilightforest$getMaxStackSize(ItemStack stack) {
		return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
	}

	default boolean twilightforest$onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		return false;
	}
}