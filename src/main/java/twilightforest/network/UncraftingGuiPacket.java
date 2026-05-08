package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public record UncraftingGuiPacket(int operationType) implements CustomPacketPayload {
	public static final Type<UncraftingGuiPacket> TYPE = new Type<>(
			TwilightForestMod.prefix("switch_uncrafting_operation"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UncraftingGuiPacket> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.INT, UncraftingGuiPacket::operationType,
					UncraftingGuiPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
