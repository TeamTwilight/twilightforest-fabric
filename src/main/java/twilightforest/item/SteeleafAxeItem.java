package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;

public class SteeleafAxeItem extends AxeItem {

	public SteeleafAxeItem(Tier material, Properties props) {
		super(material, 6F, material.getSpeed() * 0.05f - 3.4f, props);
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