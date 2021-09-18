package twilightforest.item;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import twilightforest.entity.projectile.SeekerArrowEntity;
import twilightforest.extensions.IBowItemEx;

public class SeekerBowItem extends BowItem implements IBowItemEx {

	public SeekerBowItem(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new SeekerArrowEntity(arrow.level, arrow.getOwner());
	}
}
