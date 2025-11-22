package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.ItemDisplays;

public record SendChosenMapSlotPacket(int slotIndex) implements CustomPacketPayload {
	public static final Type<twilightforest.network.SendChosenMapSlotPacket> TYPE = new Type<>(TwilightForestMod.prefix("send_chosen_map_slot_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SendChosenMapSlotPacket> STREAM_CODEC = CustomPacketPayload.codec(twilightforest.network.SendChosenMapSlotPacket::write, twilightforest.network.SendChosenMapSlotPacket::new);

	public SendChosenMapSlotPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readInt());
	}

	public static void handle(twilightforest.network.SendChosenMapSlotPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			int index = message.slotIndex();
			boolean reset = (index == -1);
			boolean isValidIndex = (index >= 0 && index < ItemDisplayContents.LAYOUT.size());
			if (reset || (isValidIndex && ItemDisplayContents.LAYOUT.get(index) == ItemDisplays.MAP))
				ctx.player().setData(TFDataAttachments.ITEM_DISPLAY_CHOSEN_MAP_SLOT, index);
		});
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeInt(this.slotIndex);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
