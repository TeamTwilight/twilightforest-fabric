package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.util.Restriction;
import twilightforest.init.custom.Restrictions;
import twilightforest.util.LandmarkUtil;

import java.util.Optional;

public class LockedBiomeListener {

	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	public static void clientTick(Minecraft client) {
		Player player = client.player;
		if (player == null || !(player.level() instanceof ClientLevel level))
			return;

		//attempt to send a biome locked toast if our player is in a locked biome, only every 5 ticks
		if(level.isClientSide() && player.tickCount % 5 == 0
				&& LandmarkUtil.isProgressionEnforced(level)
				&& !player.isCreative() && !player.isSpectator() && !TFConfig.CLIENT_CONFIG.disableLockedBiomeToasts.get()) {
			Optional<Restriction> restriction = Restrictions.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player);
			if (restriction.isPresent() && restriction.get().lockedBiomeToast() != null) {
				timeUntilToast--;
				if(!shownToast && timeUntilToast <= 0) {
					Minecraft.getInstance().getToasts().addToast(new LockedBiomeToast(restriction.get().lockedBiomeToast()));
					shownToast = true;
				}
			} else {
				if(shownToast) {
					timeUntilToast = 60;
					shownToast = false;
				}
			}
		}
	}
}
