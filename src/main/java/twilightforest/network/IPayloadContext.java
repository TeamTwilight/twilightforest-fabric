package twilightforest.network;

import net.minecraft.world.entity.player.Player;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Fabric 兼容的 IPayloadContext 接口。
 * 在 NeoForge 中此接口由网络系统提供，在 Fabric 中我们需要自己实现。
 */
public interface IPayloadContext {

	Player player();

	void enqueueWork(Runnable runnable);

	PacketFlow flow();

	/**
	 * 数据包流向枚举，兼容 NeoForge 的 PacketFlow
	 */
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

	/**
	 * 从 Fabric 的 S2C 网络上下文创建 IPayloadContext
	 * Fabric API 1.21.1 uses ClientPlayNetworking.Context
	 */
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

	/**
	 * 从 Fabric 的 C2S 网络上下文创建 IPayloadContext
	 * Fabric API 1.21.1 uses ServerPlayNetworking.Context
	 */
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