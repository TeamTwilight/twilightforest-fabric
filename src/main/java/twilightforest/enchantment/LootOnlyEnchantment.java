package twilightforest.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import twilightforest.init.TFItems;

public class LootOnlyEnchantment extends Enchantment {

	protected LootOnlyEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots) {
		super(rarity, category, applicableSlots);
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	public boolean allowedInCreativeTab(Item book, CreativeModeTab tab) {
		return super.allowedInCreativeTab(book, tab) || TFItems.creativeTab.equals(tab);
	}
}
