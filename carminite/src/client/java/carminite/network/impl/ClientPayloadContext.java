package carminite.network.impl;

import carminite.network.IPayloadContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;

public record ClientPayloadContext(ClientPlayNetworking.Context context) implements IPayloadContext {

	@Override
	public Player player() {
		return this.context.player();
	}

	@Override
	public void enqueueWork(Runnable runnable) {
		this.context.client().execute(runnable);
	}

	@Override
	public PacketFlow flow() {
		return PacketFlow.CLIENTBOUND;
	}
}