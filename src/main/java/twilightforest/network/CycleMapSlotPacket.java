package twilightforest.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFCommon;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;

public record CycleMapSlotPacket() implements CustomPacketPayload {
	public static final CycleMapSlotPacket INSTANCE = new CycleMapSlotPacket();
	public static final Type<CycleMapSlotPacket> TYPE = new Type<>(TFCommon.prefix("cycle_map_slot_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CycleMapSlotPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	public static void handle(CycleMapSlotPacket message, ServerPlayNetworking.Context ctx) {
		ctx.server().execute(() -> {
			ServerPlayer serverPlayer = ctx.player();
			ItemStack headStack = serverPlayer.getItemBySlot(EquipmentSlot.HEAD);
			ItemDisplayContents contents = headStack.get(TFDataComponents.ITEM_DISPLAY);
			if (contents == null || contents.isEmpty() || !TravellersModifiersManager.isModifierActive(serverPlayer, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
				return;

			ItemDisplayContents.Mutable mutable = new ItemDisplayContents.Mutable(contents);
			int oldIndex = mutable.chosenMapSlot();
			int newIndex = mutable.cycleChosenMapSlot();

			if (oldIndex != newIndex) {
				ItemDisplayContents updatedContents = mutable.toImmutable();
				headStack.set(TFDataComponents.ITEM_DISPLAY, updatedContents);
				serverPlayer.getInventory().setChanged();
				serverPlayer.connection.send(
					new ClientboundSoundPacket(newIndex == -1 ? TFSounds.CYCLE_MAPS_EMPTY : TFSounds.CYCLE_MAPS, SoundSource.UI, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1F, 1F, serverPlayer.getRandom().nextLong())
				);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}