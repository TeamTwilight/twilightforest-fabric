package twilightforest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.network.AreaProtectionPacket} —
 * server pushes a list of {@link BoundingBox}es plus an origin {@link BlockPos}
 * so clients can spawn / refresh the visual {@link twilightforest.entity.ProtectionBox}
 * markers + origin-position protection particles.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} dropped — client-side
 * box-spawn / particle dispatch lives in {@code CodexNetworking}.</p>
 */
public class AreaProtectionPacket implements CustomPacketPayload {

	public static final Type<AreaProtectionPacket> TYPE = new Type<>(TwilightForestMod.prefix("add_protection_box"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AreaProtectionPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), AreaProtectionPacket::new);

	private final List<BoundingBox> sbb;
	private final BlockPos pos;

	public AreaProtectionPacket(List<BoundingBox> sbb, BlockPos pos) {
		this.sbb = sbb;
		this.pos = pos;
	}

	public AreaProtectionPacket(FriendlyByteBuf buf) {
		this.sbb = new ArrayList<>();
		int len = buf.readInt();
		for (int i = 0; i < len; i++) {
			this.sbb.add(new BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));
		}
		this.pos = buf.readBlockPos();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.sbb.size());
		this.sbb.forEach(box -> {
			buf.writeInt(box.minX());
			buf.writeInt(box.minY());
			buf.writeInt(box.minZ());
			buf.writeInt(box.maxX());
			buf.writeInt(box.maxY());
			buf.writeInt(box.maxZ());
		});
		buf.writeBlockPos(this.pos);
	}

	public List<BoundingBox> boxes() {
		return this.sbb;
	}

	public BlockPos pos() {
		return this.pos;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
