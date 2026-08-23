package twilightforest.item;

import carminite.interfaces.markers.ICustomArrowItem;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataAttachments;

public class EnderBowItem extends BowItem implements ICustomArrowItem {
	public static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		arrow.setAttached(TFDataAttachments.ENDER_ARROW, true);
		return arrow;
	}
}