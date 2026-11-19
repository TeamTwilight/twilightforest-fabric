package twilightforest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.TFCommon;

import java.util.ArrayList;
import java.util.List;

public class AreaProtectionPacket implements CustomPacketPayload {

	public static final Type<AreaProtectionPacket> TYPE = new Type<>(TFCommon.prefix("add_protection_box"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AreaProtectionPacket> STREAM_CODEC = CustomPacketPayload.codec(AreaProtectionPacket::write, AreaProtectionPacket::new);

	public final List<BoundingBox> sbb;
	public final BlockPos pos;
	public final int[] entityIds;

	public AreaProtectionPacket(List<BoundingBox> sbb, BlockPos pos, int[] entityIds) {
		this.sbb = sbb;
		this.pos = pos;
		this.entityIds = entityIds;
	}

	public AreaProtectionPacket(FriendlyByteBuf buf) {
		this.sbb = new ArrayList<>();
		int len = buf.readInt();
		for (int i = 0; i < len; i++) {
			this.sbb.add(new BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));
		}
		this.pos = buf.readBlockPos();
		this.entityIds = buf.readVarIntArray();
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
		buf.writeVarIntArray(this.entityIds);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}