package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;

public class SteeleafShovelItem extends ShovelItem {

	public SteeleafShovelItem(Tier toolMaterial, Properties props) {
		super(toolMaterial, 1.5F, -3.0F, props);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.enchant(Enchantments.BLOCK_EFFICIENCY, 2);
			items.add(stack);
		}
	}
}