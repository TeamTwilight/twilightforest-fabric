package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.WeatherRenderer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class StructureProtectionClearPacket implements S2CPacket {

	public StructureProtectionClearPacket() {
	}

	public StructureProtectionClearPacket(FriendlyByteBuf unused) {
	}

	public void encode(FriendlyByteBuf unused) {
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {
		public static boolean onMessage(StructureProtectionClearPacket message, Executor ctx) {
			ctx.execute(() -> {
				DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TwilightForestMod.prefix("renderer"));

				// add weather box if needed
				if (info instanceof TwilightForestRenderInfo) {
					TFWeatherRenderer.setProtectedBox(null);
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
