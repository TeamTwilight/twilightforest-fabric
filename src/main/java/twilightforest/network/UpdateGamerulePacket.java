package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class UpdateGamerulePacket {

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

	public static class Handler {
		public static boolean onMessage(UpdateGamerulePacket packet, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				if (Minecraft.getInstance().level != null)
					Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(packet.enforced, null);
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
