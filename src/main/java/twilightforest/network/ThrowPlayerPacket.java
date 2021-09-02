package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class ThrowPlayerPacket extends ISimplePacket {
	private final float motionX;
	private final float motionY;
	private final float motionZ;

	public ThrowPlayerPacket(float motionX, float motionY, float motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public ThrowPlayerPacket(FriendlyByteBuf buf) {
		motionX = buf.readFloat();
		motionY = buf.readFloat();
		motionZ = buf.readFloat();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeFloat(motionX);
		buf.writeFloat(motionY);
		buf.writeFloat(motionZ);
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(ThrowPlayerPacket message) {
			Minecraft.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().player.push(message.motionX, message.motionY, message.motionZ);
				}
			});

			return true;
		}
	}
}
