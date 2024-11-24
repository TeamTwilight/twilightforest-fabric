package twilightforest.network;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import twilightforest.TwilightForestMod;

import java.util.concurrent.Executor;

public class UpdateGamerulePacket implements C2SPacket {

	private final boolean enforced;

	public UpdateGamerulePacket(boolean enforced) {
		this.enforced = enforced;
	}

	public UpdateGamerulePacket(FriendlyByteBuf buf) {
		this.enforced = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(this.enforced);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
		Handler.onMessage(this, server);
	}

	public static class Handler {
		public static boolean onMessage(UpdateGamerulePacket packet, Executor ctx) {
			ctx.execute(() -> {
				if (Minecraft.getInstance().level != null)
					Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(packet.enforced, null);
			});
			return true;
		}
	}
}
