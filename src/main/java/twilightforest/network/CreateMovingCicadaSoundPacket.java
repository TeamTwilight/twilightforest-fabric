package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.CreateMovingCicadaSoundPacket}
 * — server tells client to start a moving cicada sound instance attached to the
 * referenced entity (cicada-on-mob ride sound).
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} receiver dropped — the
 * client {@code MovingCicadaSoundInstance} hookup lives in {@code CodexNetworking}
 * client side.</p>
 */
public record CreateMovingCicadaSoundPacket(int entityID) implements CustomPacketPayload {

	public static final Type<CreateMovingCicadaSoundPacket> TYPE = new Type<>(TwilightForestMod.prefix("create_cicada_sound"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CreateMovingCicadaSoundPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), CreateMovingCicadaSoundPacket::new);

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
