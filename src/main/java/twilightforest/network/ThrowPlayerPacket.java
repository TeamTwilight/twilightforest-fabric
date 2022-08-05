package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.Executor;

public class ThrowPlayerPacket implements S2CPacket {
	private final float motionX;
	private final float motionY;
	private final float motionZ;

	public ThrowPlayerPacket(float motionX, float motionY, float motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public ThrowPlayerPacket(FriendlyByteBuf buf) {
		this.motionX = buf.readFloat();
		this.motionY = buf.readFloat();
		this.motionZ = buf.readFloat();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeFloat(this.motionX);
		buf.writeFloat(this.motionY);
		buf.writeFloat(this.motionZ);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		public static boolean onMessage(ThrowPlayerPacket message, Executor ctx) {
			ctx.execute(() ->
					Minecraft.getInstance().player.push(message.motionX, message.motionY, message.motionZ));
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
