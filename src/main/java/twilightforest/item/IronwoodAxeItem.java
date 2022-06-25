package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;

public class IronwoodAxeItem extends AxeItem {

	public IronwoodAxeItem(Tier material, Properties properties) {
		super(material, 6F, material.getSpeed() * 0.05f - 3.4f, properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.enchant(Enchantments.BLOCK_FORTUNE, 1);
			list.add(stack);
		}
	}
}