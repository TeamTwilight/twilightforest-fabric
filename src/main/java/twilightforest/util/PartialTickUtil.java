package twilightforest.util;

import net.minecraft.client.Minecraft;

public class PartialTickUtil {

	public static float getPartialTick() {
		var mc = Minecraft.getInstance();
		return mc.isPaused() ? mc.pausePartialTick : mc.timer.partialTick;
	}
}
