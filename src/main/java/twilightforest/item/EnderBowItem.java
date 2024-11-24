package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.CustomArrowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;

public class EnderBowItem extends BowItem implements CustomArrowItem {
	public static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		arrow.getCustomData().putBoolean(KEY, true);
		return arrow;
	}
}