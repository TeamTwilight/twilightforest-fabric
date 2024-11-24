package twilightforest.asm;

import net.minecraft.world.item.Item;
import twilightforest.item.ChainBlockItem;

public class EnchantmentCategoryBlockAndChain extends EnchantmentCategoryMixin {
	@Override
	public boolean canEnchant(Item item) {
		return item instanceof ChainBlockItem;
	}
}