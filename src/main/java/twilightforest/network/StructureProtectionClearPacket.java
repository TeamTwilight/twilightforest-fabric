package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

public class StructureProtectionClearPacket extends ISimplePacket {

	public StructureProtectionClearPacket() {}

	public StructureProtectionClearPacket(FriendlyByteBuf unused) {}

	public void encode(FriendlyByteBuf unused) {}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {
		public static boolean onMessage(StructureProtectionClearPacket message) {
			Minecraft.getInstance().execute(() -> {
				DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TwilightForestMod.prefix("renderer"));

				// add weather box if needed
				if (info instanceof TwilightForestRenderInfo twilightForestRenderInfo) {
					TFWeatherRenderer weatherRenderer = twilightForestRenderInfo.getWeatherRenderHandler();

					if (weatherRenderer instanceof TFWeatherRenderer) {
						weatherRenderer.setProtectedBox(null);
					}
				}
			});

			return true;
		}
	}
}
