package twilightforest.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFCommon;

public record SpawnCharmPacket(ItemStack charm, ResourceKey<SoundEvent> event) implements CustomPacketPayload {

	public static final Type<SpawnCharmPacket> TYPE = new Type<>(TFCommon.prefix("spawn_charm"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCharmPacket> STREAM_CODEC = CustomPacketPayload.codec(SpawnCharmPacket::write, SpawnCharmPacket::new);

	public SpawnCharmPacket(RegistryFriendlyByteBuf buf) {
		this(ItemStack.STREAM_CODEC.decode(buf), buf.readResourceKey(Registries.SOUND_EVENT));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		ItemStack.STREAM_CODEC.encode(buf, this.charm());
		buf.writeResourceKey(this.event());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}