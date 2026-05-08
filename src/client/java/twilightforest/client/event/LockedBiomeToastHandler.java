package twilightforest.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import twilightforest.client.LockedBiomeToast;
import twilightforest.config.TFConfig;
import twilightforest.util.Restriction;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public final class LockedBiomeToastHandler {
	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	private LockedBiomeToastHandler() {
	}

	public static void bootstrap() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tickLockedToastLogic());
	}

	private static void tickLockedToastLogic() {
		Player player = Minecraft.getInstance().player;
		if (player == null || !(player.level() instanceof ClientLevel level)) {
			return;
		}
		if (player.tickCount % 5 != 0 || !LandmarkUtil.isProgressionEnforced(level) || player.isCreative() || player.isSpectator() || TFConfig.disableLockedBiomeToasts) {
			return;
		}
		Optional<Restriction> restriction = Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player);
		if (restriction.isPresent() && restriction.get().lockedBiomeToast() != null) {
			timeUntilToast--;
			if (!shownToast && timeUntilToast <= 0) {
				Minecraft.getInstance().getToasts().addToast(new LockedBiomeToast(restriction.get().lockedBiomeToast()));
				shownToast = true;
			}
		} else if (shownToast) {
			timeUntilToast = 60;
			shownToast = false;
		}
	}
}
