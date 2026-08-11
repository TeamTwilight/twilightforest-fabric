package carminite.network;

import net.minecraft.world.entity.player.Player;

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
}