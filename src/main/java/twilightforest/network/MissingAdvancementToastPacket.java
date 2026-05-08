package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.MissingAdvancementToastPacket}
 * — server pushes a "you're missing required advancement" toast (with title + icon)
 * to a client that tried to interact with a progression-locked feature.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} receiver dropped — the
 * client-side {@code Minecraft.getInstance().getToasts().addToast(...)} dispatch is
 * wired in {@code CodexNetworking} client side once {@code MissingAdvancementToast}
 * exists.</p>
 */
public record MissingAdvancementToastPacket(Component title, ItemStack icon) implements CustomPacketPayload {

	public static final Type<MissingAdvancementToastPacket> TYPE = new Type<>(TwilightForestMod.prefix("missing_advancement_toast"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MissingAdvancementToastPacket> STREAM_CODEC = StreamCodec.composite(
		ComponentSerialization.STREAM_CODEC, MissingAdvancementToastPacket::title,
		ItemStack.STREAM_CODEC, MissingAdvancementToastPacket::icon,
		MissingAdvancementToastPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
