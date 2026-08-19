package twilightforest.item;

import twilightforest.fabric.interfaces.marker.ICustomArrowItem;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.IceArrow;

public class IceBowItem extends BowItem implements ICustomArrowItem {

	public IceBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return new IceArrow(arrow, projectileStack.copyWithCount(1), weaponStack);
	}
}