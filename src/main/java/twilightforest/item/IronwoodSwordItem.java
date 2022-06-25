package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;

public class IronwoodSwordItem extends SwordItem {

	public IronwoodSwordItem(Tier material, Properties properties) {
		super(material, 3, -2.4F, properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.enchant(Enchantments.KNOCKBACK, 1);
			list.add(stack);
		}
	}
}