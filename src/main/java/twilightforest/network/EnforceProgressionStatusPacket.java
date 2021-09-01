package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.fmllegacy.network.NetworkEvent;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class EnforceProgressionStatusPacket extends ISimplePacket {

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
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(EnforceProgressionStatusPacket message) {
			Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce, null);
			return true;
		}
	}
}
