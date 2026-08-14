package carminite.network.internal;

import carminite.network.IPayloadContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;

public record ServerPayloadContext(ServerPlayNetworking.Context context) implements IPayloadContext {

	@Override
	public Player player() {
		return this.context.player();
	}

	@Override
	public void enqueueWork(Runnable runnable) {
		this.context.server().execute(runnable);
	}

	@Override
	public PacketFlow flow() {
		return PacketFlow.SERVERBOUND;
	}
}