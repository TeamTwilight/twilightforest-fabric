package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFMain;
import twilightforest.block.entity.MasonJarBlockEntity;

public record SetMasonJarItemPacket(BlockPos pos, boolean empty, ItemStack stack, int rotation) implements CustomPacketPayload {
	public static final Type<SetMasonJarItemPacket> TYPE = new Type<>(TFMain.prefix("set_mason_jar_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SetMasonJarItemPacket> STREAM_CODEC = CustomPacketPayload.codec(SetMasonJarItemPacket::write, SetMasonJarItemPacket::read);

	public SetMasonJarItemPacket(BlockPos pos, ItemStack stack, int rotation) {
		this(pos, stack.isEmpty(), stack, rotation);
	}

	public static SetMasonJarItemPacket read(RegistryFriendlyByteBuf buf) {
		int rotation = buf.readInt();
		BlockPos pos = buf.readBlockPos();
		boolean empty = buf.readBoolean();
		return new SetMasonJarItemPacket(pos, empty, empty ? ItemStack.EMPTY : ItemStack.STREAM_CODEC.decode(buf), rotation);
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.rotation());
		buf.writeBlockPos(this.pos());
		buf.writeBoolean(this.empty());
		if (!this.empty()) ItemStack.STREAM_CODEC.encode(buf, this.stack());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SetMasonJarItemPacket packet, ClientPlayNetworking.Context ctx) {
		ClientLevel clientLevel = ctx.client().level;
		if (clientLevel.getBlockEntity(packet.pos()) instanceof MasonJarBlockEntity blockEntity) {
			blockEntity.getItemHandler().setItem(packet.stack());
			blockEntity.setItemRotation(packet.rotation());
			blockEntity.setChanged();
		}
	}
}