package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFCommon;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;

public record SyncQuestsPacket(QuestingRamContext ram) implements CustomPacketPayload {

	public static final Type<SyncQuestsPacket> TYPE = new Type<>(TFCommon.prefix("sync_quests"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
		QuestingRamContext.STREAM_CODEC, SyncQuestsPacket::ram,
		SyncQuestsPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}