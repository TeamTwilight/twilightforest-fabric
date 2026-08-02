package twilightforest.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import twilightforest.client.LockedBiomeToast;
import twilightforest.config.TFConfig;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.Restriction;

import java.util.Optional;

public class LockedBiomeToastHandler {

	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	/**
	 * Register Fabric API callback for locked biome toast logic.
	 * Called from {@link twilightforest.client.TFClientSetup#onInitializeClient()}.
	 */
	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(LockedBiomeToastHandler::tickLockedToastLogic);
	}

	protected static void tickLockedToastLogic(Minecraft client) {
		Player player = client.player;
		if (player == null || !(player.level() instanceof ClientLevel level))
			return;

		//attempt to send a biome locked toast if our player is in a locked biome, only every 5 ticks
		if (level.isClientSide() && player.tickCount % 5 == 0
			&& LandmarkUtil.isProgressionEnforced(level)
			&& !player.isCreative() && !player.isSpectator() && !TFConfig.disableLockedBiomeToasts) {
			Optional<Restriction> restriction = Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player);
			if (restriction.isPresent() && restriction.get().lockedBiomeToast() != null) {
				timeUntilToast--;
				if (!shownToast && timeUntilToast <= 0) {
					client.getToasts().addToast(new LockedBiomeToast(restriction.get().lockedBiomeToast()));
					shownToast = true;
				}
			} else {
				if (shownToast) {
					timeUntilToast = 60;
					shownToast = false;
				}
			}
		}
	}
}
