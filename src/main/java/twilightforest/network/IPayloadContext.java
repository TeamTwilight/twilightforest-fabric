package twilightforest.network;

import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Fabric version of IPayloadContext
 */
public interface IPayloadContext {

	Player player();

	void enqueueWork(Runnable runnable);

	PacketFlow flow();

	enum PacketFlow {
		CLIENTBOUND,
		SERVERBOUND;

		public boolean isClientbound() {
			return this == CLIENTBOUND;
		}

		public boolean isServerbound() {
			return this == SERVERBOUND;
		}
	}

	static IPayloadContext fromClientNetworking(ClientPlayNetworking.Context context) {
		return new IPayloadContext() {
			@Override
			public Player player() {
				return context.player();
			}

			@Override
			public void enqueueWork(Runnable runnable) {
				net.minecraft.client.Minecraft.getInstance().execute(runnable);
			}

			@Override
			public PacketFlow flow() {
				return PacketFlow.CLIENTBOUND;
			}
		};
	}

	static IPayloadContext fromServerNetworking(ServerPlayNetworking.Context context) {
		return new IPayloadContext() {
			@Override
			public Player player() {
				return context.player();
			}

			@Override
			public void enqueueWork(Runnable runnable) {
				context.player().getServer().execute(runnable);
			}

			@Override
			public PacketFlow flow() {
				return PacketFlow.SERVERBOUND;
			}
		};
	}
}