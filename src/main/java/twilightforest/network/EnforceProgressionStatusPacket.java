package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import twilightforest.TwilightForestMod;

import java.util.concurrent.Executor;

public class EnforceProgressionStatusPacket implements S2CPacket {

	private final boolean enforce;

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		this.enforce = buf.readBoolean();
	}

	public EnforceProgressionStatusPacket(boolean enforce) {
		this.enforce = enforce;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(enforce);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, SimpleChannel.ResponseTarget responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		public static boolean onMessage(EnforceProgressionStatusPacket message, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce, null);
				}
			});
			return true;
		}
	}
}
