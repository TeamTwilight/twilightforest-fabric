package twilightforest.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.SpawnEggItem;

public class TFSpawnEggItem extends SpawnEggItem {

	public TFSpawnEggItem(EntityType<? extends Mob> type, int primaryColor, int secondaryColor, Properties props) {
		super(type, primaryColor, secondaryColor, props);
	}
}