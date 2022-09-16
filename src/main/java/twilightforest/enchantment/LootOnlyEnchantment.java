package twilightforest.enchantment;

import io.github.fabricators_of_create.porting_lib.enchant.CreativeModeTabPresentEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import twilightforest.init.TFItems;

public class LootOnlyEnchantment extends Enchantment implements CreativeModeTabPresentEnchantment {

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
		return CreativeModeTabPresentEnchantment.super.allowedInCreativeTab(book, tab) || TFItems.creativeTab.equals(tab);
	}
}
