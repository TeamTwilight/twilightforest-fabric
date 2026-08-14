package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFMain;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

public record SyncQuestsPacket(QuestingRamContext ram) implements CustomPacketPayload {

	private static final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.INSTANCE;

	public static final Type<SyncQuestsPacket> TYPE = new Type<>(TFMain.prefix("sync_quests"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.fromCodec(QuestingRamContext.CODEC), SyncQuestsPacket::ram,
		SyncQuestsPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncQuestsPacket packet, ClientPlayNetworking.Context context) {
		questingRamCurrentContext.setContext(packet.ram());
	}
}