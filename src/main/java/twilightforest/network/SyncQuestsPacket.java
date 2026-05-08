package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;

/**
 * 1:1 port of upstream {@code twilightforest.network.SyncQuestsPacket} — pushes the
 * datapack-resolved {@link QuestingRamContext} to clients on join / reload.
 *
 * <p>Codex Fabric port note: NF {@code IPayloadContext} static {@code handle(...)}
 * receiver is dropped — codex registers handlers in
 * {@code com.codex.twilight.network.CodexNetworking} via
 * {@code ClientPlayNetworking.registerGlobalReceiver}. The autowired
 * {@code QuestingRamCurrentContext} hookup likewise lives client-side at the
 * receiver site, not on the payload class.</p>
 */
public record SyncQuestsPacket(QuestingRamContext ram) implements CustomPacketPayload {

	public static final Type<SyncQuestsPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_quests"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.fromCodec(QuestingRamContext.CODEC), SyncQuestsPacket::ram,
		SyncQuestsPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
