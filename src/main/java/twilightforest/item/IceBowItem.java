package twilightforest.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.IceArrowEntity;

import net.minecraft.world.item.Item.Properties;

public class IceBowItem extends BowItem {

	public IceBowItem(Properties props) {
		super(props);
	}

	//TODO: PORT
	//@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new IceArrowEntity(arrow.level, arrow.getOwner());
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return toRepair.getItem() instanceof BlockItem blockItem && BlockTags.ICE.contains(blockItem.getBlock()) || super.isValidRepairItem(toRepair, repairWith);
	}
}
