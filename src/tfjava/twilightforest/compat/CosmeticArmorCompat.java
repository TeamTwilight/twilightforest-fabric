package twilightforest.compat;

import net.minecraft.world.entity.player.Player;
import twilightforest.events.CharmEvents;

import java.lang.reflect.Method;

public class CosmeticArmorCompat {
	public static void keepCosmeticArmor(Object event) {
		Player player = getEventPlayer(event);
		if (player != null && CharmEvents.getPlayerData(player).contains(CharmEvents.CHARM_INV_TAG)) {
			setCanceled(event);
		}
	}

	private static Player getEventPlayer(Object event) {
		try {
			Method method = event.getClass().getMethod("getEntityPlayer");
			Object player = method.invoke(event);
			return player instanceof Player value ? value : null;
		} catch (ReflectiveOperationException ignored) {
			return null;
		}
	}

	private static void setCanceled(Object event) {
		try {
			Method method = event.getClass().getMethod("setCanceled", boolean.class);
			method.invoke(event, true);
		} catch (ReflectiveOperationException ignored) {
			// Optional compatibility event is absent or has a different API on this loader.
		}
	}
}
