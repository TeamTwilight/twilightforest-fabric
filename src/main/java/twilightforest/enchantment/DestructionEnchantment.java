package twilightforest.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.item.ChainBlockItem;

public class DestructionEnchantment extends LootOnlyEnchantment {

	public DestructionEnchantment(Rarity rarity) {
		super(rarity, TFEnchantments.BLOCK_AND_CHAIN, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMinCost(int level) {
		return 5 + (level - 1) * 9;
	}

	@Override
	public int getMaxCost(int level) {
		return this.getMinCost(level) + 15;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ChainBlockItem;
	}

	@Override
	public float getDamageBonus(int level, MobType type, ItemStack item) {
		return -level * 1.5F;
	}
}
