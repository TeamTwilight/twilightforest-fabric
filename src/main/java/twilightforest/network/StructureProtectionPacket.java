package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.WeatherRenderer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class StructureProtectionPacket implements S2CPacket {

	private final BoundingBox sbb;

	public StructureProtectionPacket(BoundingBox sbb) {
		this.sbb = sbb;
	}

	public StructureProtectionPacket(FriendlyByteBuf buf) {
		this.sbb = new BoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.sbb.minX());
		buf.writeInt(this.sbb.minY());
		buf.writeInt(this.sbb.minZ());
		buf.writeInt(this.sbb.maxX());
		buf.writeInt(this.sbb.maxY());
		buf.writeInt(this.sbb.maxZ());
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {
		public static boolean onMessage(StructureProtectionPacket message, Executor ctx) {
			ctx.execute(() -> {
				DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TwilightForestMod.prefix("renderer"));

				// add weather box if needed
				if (info instanceof TwilightForestRenderInfo tfInfo) {
					TFWeatherRenderer weatherRenderer = tfInfo.getWeatherRenderHandler();

					if (weatherRenderer instanceof TFWeatherRenderer renderer) {
						renderer.setProtectedBox(message.sbb);
					}
				}
			});

			return true;
		}
	}
}
