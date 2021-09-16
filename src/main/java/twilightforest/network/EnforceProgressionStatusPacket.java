package twilightforest.network;

import twilightforest.TwilightForestMod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class EnforceProgressionStatusPacket extends ISimplePacket {

	private final boolean enforce;

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		TwilightForestMod.LOGGER.info("decoding");
		this.enforce = buf.readBoolean();
	}

	public EnforceProgressionStatusPacket(boolean enforce) {
		this.enforce = enforce;
	}

	public void encode(FriendlyByteBuf buf) {
		TwilightForestMod.LOGGER.info("encoding");
		buf.writeBoolean(enforce);
	}

	@Override
	public void onMessage(Player playerEntity) {
		TwilightForestMod.LOGGER.info("GOT");
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(EnforceProgressionStatusPacket message) {
			Minecraft.getInstance().execute(() -> {
				TwilightForestMod.LOGGER.info(Minecraft.getInstance().level);
				Minecraft.getInstance().level.getGameRules().getRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce, null);

			});
			return true;
		}
	}
}
