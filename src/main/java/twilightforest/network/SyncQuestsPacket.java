package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.network.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.util.TFBeanRegistry;

public record SyncQuestsPacket(QuestingRamContext ram) implements CustomPacketPayload {

	private static QuestingRamCurrentContext questingRamCurrentContext;

	private static QuestingRamCurrentContext getQuestingRamCurrentContext() {
		if (questingRamCurrentContext == null) {
			questingRamCurrentContext = TFBeanRegistry.get(QuestingRamCurrentContext.class);
		}
		return questingRamCurrentContext;
	}

	public static final Type<SyncQuestsPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_quests"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.fromCodec(QuestingRamContext.CODEC), SyncQuestsPacket::ram,
		SyncQuestsPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncQuestsPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> getQuestingRamCurrentContext().setContext(packet.ram()));
	}
}
