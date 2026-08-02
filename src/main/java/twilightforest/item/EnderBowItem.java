package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.extensions.CustomArrowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class EnderBowItem extends BowItem implements CustomArrowItem {
	public static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		arrow.addTag(KEY);
		return arrow;
	}
}