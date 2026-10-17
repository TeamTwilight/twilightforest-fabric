package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStackTemplate;
import twilightforest.TFCommon;

public record MissingAdvancementToastPacket(Component title, ItemStackTemplate icon) implements CustomPacketPayload {

	public static final Type<MissingAdvancementToastPacket> TYPE = new Type<>(TFCommon.prefix("missing_advancement_toast"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MissingAdvancementToastPacket> STREAM_CODEC = StreamCodec.composite(
		ComponentSerialization.STREAM_CODEC, MissingAdvancementToastPacket::title,
		ItemStackTemplate.STREAM_CODEC, MissingAdvancementToastPacket::icon,
		MissingAdvancementToastPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}