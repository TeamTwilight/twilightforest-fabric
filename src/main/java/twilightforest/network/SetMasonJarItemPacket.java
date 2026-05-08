package twilightforest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;

public record SetMasonJarItemPacket(BlockPos pos, boolean empty, ItemStack stack, int rotation) implements CustomPacketPayload {
	public static final Type<SetMasonJarItemPacket> TYPE = new Type<>(TwilightForestMod.prefix("set_mason_jar_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SetMasonJarItemPacket> STREAM_CODEC =
		CustomPacketPayload.codec(SetMasonJarItemPacket::write, SetMasonJarItemPacket::read);

	public SetMasonJarItemPacket(BlockPos pos, ItemStack stack, int rotation) {
		this(pos, stack.isEmpty(), stack, rotation);
	}

	private static SetMasonJarItemPacket read(RegistryFriendlyByteBuf buf) {
		int rotation = buf.readVarInt();
		BlockPos pos = buf.readBlockPos();
		boolean empty = buf.readBoolean();
		return new SetMasonJarItemPacket(pos, empty, empty ? ItemStack.EMPTY : ItemStack.STREAM_CODEC.decode(buf), rotation);
	}

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(this.rotation);
		buf.writeBlockPos(this.pos);
		buf.writeBoolean(this.empty);
		if (!this.empty) {
			ItemStack.STREAM_CODEC.encode(buf, this.stack);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
