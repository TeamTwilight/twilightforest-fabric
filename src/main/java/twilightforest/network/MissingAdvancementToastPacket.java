package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.MissingAdvancementToast;

import java.util.concurrent.Executor;

public class MissingAdvancementToastPacket implements S2CPacket {
	private final Component title;
	private final ItemStack icon;

	public MissingAdvancementToastPacket(Component title, ItemStack icon) {
		this.title = title;
		this.icon = icon;
	}

	public MissingAdvancementToastPacket(FriendlyByteBuf buf) {
		this.title = buf.readComponent();
		this.icon = buf.readItem();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeComponent(this.title);
		buf.writeItem(this.icon);
	}

	public static class Handler {
		@SuppressWarnings("Convert2Lambda")
		public static boolean onMessage(MissingAdvancementToastPacket packet, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().getToasts().addToast(new MissingAdvancementToast(packet.title, packet.icon));
				}
			});
			return true;
		}
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}
}
