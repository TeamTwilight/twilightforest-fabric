package twilightforest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.SpawnFallenLeafFromPacket} —
 * server tells client to drop a colored leaf particle at a specific block with the
 * given drift motion (used by autumn dark forest ambience).
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} dropped — leaf-particle
 * spawning hooks live in {@code CodexNetworking} client side once
 * {@code LeafParticleData} is ported.</p>
 */
public record SpawnFallenLeafFromPacket(BlockPos pos, Vec3 motion) implements CustomPacketPayload {

	public static final Type<SpawnFallenLeafFromPacket> TYPE = new Type<>(TwilightForestMod.prefix("spawn_fallen_leaf"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnFallenLeafFromPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), SpawnFallenLeafFromPacket::new);

	public SpawnFallenLeafFromPacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos());
		buf.writeDouble(this.motion().x());
		buf.writeDouble(this.motion().y());
		buf.writeDouble(this.motion().z());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
