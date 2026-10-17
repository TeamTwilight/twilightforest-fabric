package twilightforest.network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.TFCommon;
import twilightforest.util.Codecs;

import java.util.List;
import java.util.Optional;

public record StructureProtectionPacket(Optional<List<Pair<BoundingBox, Boolean>>> boxes) implements CustomPacketPayload {

	public static final Type<StructureProtectionPacket> TYPE = new Type<>(TFCommon.prefix("change_protection_renderer"));

	public static final StreamCodec<RegistryFriendlyByteBuf, StructureProtectionPacket> STREAM_CODEC =
		StreamCodec.composite(
			Codecs.listOf(Codecs.BOX_AND_FLAG_STREAM_CODEC).apply(ByteBufCodecs::optional),
			StructureProtectionPacket::boxes,
			StructureProtectionPacket::new
		);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}