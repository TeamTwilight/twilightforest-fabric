package twilightforest.item;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.Item;
import twilightforest.compat.trinkets.TrinketsCompat;

public class CuriosCharmItem extends Item {

	public CuriosCharmItem(Properties props) {
		super(props);
		setupTrinket(this);
	}

	public static void setupTrinket(Item item) {
		if (FabricLoader.getInstance().isModLoaded("trinkets"))
			TrinketsCompat.setupCuriosCapability(item);
	}
}