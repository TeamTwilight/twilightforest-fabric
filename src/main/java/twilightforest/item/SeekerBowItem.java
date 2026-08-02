package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.extensions.CustomArrowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem implements CustomArrowItem {

	public SeekerBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return new SeekerArrow(arrow, projectileStack.copyWithCount(1), weaponStack);
	}
}