package twilightforest.network;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import twilightforest.inventory.UncraftingMenu;

import java.util.concurrent.Executor;

public class UncraftingGuiPacket implements C2SPacket {
	private final int type;

	public UncraftingGuiPacket(int type) {
		this.type = type;
	}

	public UncraftingGuiPacket(FriendlyByteBuf buf) {
		this.type = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.type);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, server, player);
	}

	public static class Handler {

		public static boolean onMessage(UncraftingGuiPacket message, Executor ctx, ServerPlayer player) {
			ctx.execute(() -> {
				AbstractContainerMenu container = player.containerMenu;

				if (container instanceof UncraftingMenu uncrafting) {
					switch (message.type) {
						case 0 -> uncrafting.unrecipeInCycle++;
						case 1 -> uncrafting.unrecipeInCycle--;
						case 2 -> uncrafting.ingredientsInCycle++;
						case 3 -> uncrafting.ingredientsInCycle--;
						case 4 -> uncrafting.recipeInCycle++;
						case 5 -> uncrafting.recipeInCycle--;
					}

					if (message.type < 4)
						uncrafting.slotsChanged(uncrafting.tinkerInput);

					if (message.type >= 4)
						uncrafting.slotsChanged(uncrafting.assemblyMatrix);
				}
			});

			return true;
		}
	}
}
