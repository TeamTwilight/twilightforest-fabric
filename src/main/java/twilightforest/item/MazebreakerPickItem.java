package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.BlockTagGenerator;

import javax.annotation.Nonnull;

public class MazebreakerPickItem extends PickaxeItem {
	public MazebreakerPickItem(Tier material, Properties properties) {
		super(material, 1, -2.8F, properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.enchant(Enchantments.BLOCK_EFFICIENCY, 4);
			stack.enchant(Enchantments.UNBREAKING, 3);
			stack.enchant(Enchantments.BLOCK_FORTUNE, 2);
			items.add(stack);
		}
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		return state.is(BlockTagGenerator.MAZEBREAKER_ACCELERATED) ? destroySpeed * 16.0F : destroySpeed;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}
}