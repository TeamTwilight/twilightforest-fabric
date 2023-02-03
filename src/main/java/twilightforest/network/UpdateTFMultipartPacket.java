package twilightforest.network;

import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import twilightforest.entity.TFPart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class UpdateTFMultipartPacket implements S2CPacket {

	private int id;
	private Entity entity;
	private int len;
	private final List<PartDataHolder> data = new ArrayList<>();

	public UpdateTFMultipartPacket(FriendlyByteBuf buf) {
		this.id = buf.readInt();
		this.len = buf.readInt();
		for (int i = 0; i < len; i++) {
			if (buf.readBoolean()) {
				data.add(PartDataHolder.decode(buf));
			}
		}
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this.entity = entity;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entity.getId());
		PartEntity<?>[] parts = ((MultiPartEntity)this.entity).getParts();
		// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
		if (parts != null) {
			buf.writeInt(parts.length);
			for (PartEntity<?> part : parts) {
				if (part instanceof TFPart<?> tfPart) {
					buf.writeBoolean(true);
					tfPart.writeData().encode(buf);
				} else {
					buf.writeBoolean(false);
				}
			}
		} else {
			buf.writeInt(0);
		}
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this);
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static void onMessage(UpdateTFMultipartPacket message) {
//			ctx.get().enqueueWork(new Runnable() {
//				@Override
//				public void run() {
					Level world = Minecraft.getInstance().level;
					if (world == null)
						return;
					Entity ent = world.getEntity(message.id);
					if (ent instanceof MultiPartEntity multipart) {
						PartEntity<?>[] parts = multipart.getParts();
						if (parts == null)
							return;
						int index = 0;
						for (PartEntity<?> part : parts) {
							if (part instanceof TFPart<?> tfPart) {
								tfPart.readData(message.data.get(index));
								index++;
							}
						}
					}
//				}
//			});
		}
	}

	public record PartDataHolder(double x,
								 double y,
								 double z,
								 float yRot,
								 float xRot,
								 float width,
								 float height,
								 boolean fixed,
								 boolean dirty,
								 List<SynchedEntityData.DataValue<?>> data) {


		public void encode(FriendlyByteBuf buffer) {
			buffer.writeDouble(this.x);
			buffer.writeDouble(this.y);
			buffer.writeDouble(this.z);
			buffer.writeFloat(this.yRot);
			buffer.writeFloat(this.xRot);
			buffer.writeFloat(this.width);
			buffer.writeFloat(this.height);
			buffer.writeBoolean(this.fixed);
			buffer.writeBoolean(this.dirty);
			if (this.dirty) {
				for(SynchedEntityData.DataValue<?> datavalue : this.data) {
					datavalue.write(buffer);
				}

				buffer.writeByte(255);
			}
		}

		static PartDataHolder decode(FriendlyByteBuf buffer) {
			boolean dirty;
			return new PartDataHolder(
					buffer.readDouble(),
					buffer.readDouble(),
					buffer.readDouble(),
					buffer.readFloat(),
					buffer.readFloat(),
					buffer.readFloat(),
					buffer.readFloat(),
					buffer.readBoolean(),
					dirty = buffer.readBoolean(),
					dirty ? unpack(buffer) : null
			);
		}

		private static List<SynchedEntityData.DataValue<?>> unpack(FriendlyByteBuf buf) {
			List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();

			int i;
			while((i = buf.readUnsignedByte()) != 255) {
				list.add(SynchedEntityData.DataValue.read(buf, i));
			}

			return list;
		}

	}
}
