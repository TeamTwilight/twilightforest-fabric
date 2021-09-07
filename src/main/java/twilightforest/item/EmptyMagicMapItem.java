package twilightforest.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class EmptyMagicMapItem extends ComplexItem {
	protected EmptyMagicMapItem(Properties props) {
		super(props);
	}

	// [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		// TF - scale at 4
		ItemStack itemstack = MagicMapItem.setupNewMap(worldIn, Mth.floor(playerIn.getX()), Mth.floor(playerIn.getZ()), (byte) 4, true, false);
		ItemStack itemstack1 = playerIn.getItemInHand(handIn);
		if (!playerIn.getAbilities().instabuild) {
			itemstack1.shrink(1);
		}

		if (itemstack1.isEmpty()) {
			return InteractionResultHolder.success(itemstack);
		} else {
			if (!playerIn.getInventory().add(itemstack.copy())) {
				playerIn.drop(itemstack, false);
			}

			playerIn.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.success(itemstack1);
		}
	}
}
