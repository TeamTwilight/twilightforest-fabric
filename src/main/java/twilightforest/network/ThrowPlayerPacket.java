package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.Executor;

public class ThrowPlayerPacket implements S2CPacket {
	private final double motionX;
	private final double motionY;
	private final double motionZ;

	public ThrowPlayerPacket(double motionX, double motionY, double motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public ThrowPlayerPacket(FriendlyByteBuf buf) {
		this.motionX = buf.readDouble();
		this.motionY = buf.readDouble();
		this.motionZ = buf.readDouble();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeDouble(this.motionX);
		buf.writeDouble(this.motionY);
		buf.writeDouble(this.motionZ);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		public static boolean onMessage(ThrowPlayerPacket message, Executor ctx) {
			ctx.execute(() ->
					Minecraft.getInstance().player.push(message.motionX, message.motionY, message.motionZ));
			return true;
		}
	}
}
