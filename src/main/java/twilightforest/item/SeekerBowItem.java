package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.util.CustomArrowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem implements CustomArrowItem {

	public SeekerBowItem(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new SeekerArrow(arrow.level, arrow.getOwner());
	}
}
