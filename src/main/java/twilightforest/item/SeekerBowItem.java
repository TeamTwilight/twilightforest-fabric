package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.CustomArrowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem implements CustomArrowItem {

	public SeekerBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new SeekerArrow(arrow.level(), arrow.getOwner());
	}
}