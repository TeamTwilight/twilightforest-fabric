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

import java.util.List;
import java.util.concurrent.Executor;

public class UpdateTFMultipartPacket implements S2CPacket {

	private int id;
	private FriendlyByteBuf buffer;
	private Entity entity;

	public UpdateTFMultipartPacket(FriendlyByteBuf buf) {
		this.id = buf.readInt();
		this.buffer = buf;
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this.entity = entity;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entity.getId());
		PartEntity<?>[] parts = ((MultiPartEntity)this.entity).getParts();
		// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
		if (parts != null) {
			for (PartEntity<?> part : parts) {
				if (part instanceof TFPart<?> tfPart) {
					tfPart.writeData(buf);
					boolean dirty = tfPart.getEntityData().isDirty();
					buf.writeBoolean(dirty);
					if (dirty)
						SynchedEntityData.pack(tfPart.getEntityData().packDirty(), buf);
				}
			}
		}
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {
		public static boolean onMessage(UpdateTFMultipartPacket message, Minecraft client) {
				Level world = Minecraft.getInstance().level;
				if (world == null)
					return false;
				Entity ent = world.getEntity(message.id);
				if (ent != null && ent instanceof MultiPartEntity partEntity && partEntity.isMultipartEntity()) {
					PartEntity<?>[] parts = partEntity.getParts();
					if (parts == null)
						return false;
					for (PartEntity<?> part : parts) {
						if (part instanceof TFPart<?> tfPart) {
							tfPart.readData(message.buffer);
							if (message.buffer.readBoolean()) {
								List<SynchedEntityData.DataItem<?>> data = SynchedEntityData.unpack(message.buffer);
								if (data != null)
									tfPart.getEntityData().assignValues(data);
							}
						}
					}
				}
			return true;
		}
	}
}
