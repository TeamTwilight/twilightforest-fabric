package twilightforest.client.event;

import carminite.events.modified.CarminiteComputeFogColorEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import twilightforest.init.TFDimension;

public class FogHandler {
	public static final FogHandler INSTANCE = new FogHandler();

	public void colorFog(CarminiteComputeFogColorEvent event) {
		if (event.getCamera().entity() instanceof LocalPlayer player && player.level() instanceof ClientLevel client && client.dimension() == TFDimension.DIMENSION_KEY) {
			double time = 13000;
			double d0 = Mth.frac(time / (double)24000.0F - (double)0.25F);
			double d1 = (double)0.5F - Math.cos(d0 * Math.PI) / (double)2.0F;
			double d2 = (float)(d0 * (double)2.0F + d1) / 3.0F;
			float daylight = Mth.clamp(Mth.cos(d2 * (float) (Math.PI * 2)) * 2.0F + 0.5F, 0.0F, 1.0F);

			event.setRed(event.getRed() * (daylight * 0.94F + 0.06F));
			event.setGreen(event.getGreen() * (daylight * 0.94F + 0.06F));
			event.setBlue(event.getBlue() * (daylight * 0.91F + 0.09F));
		}
	}
}