package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.PartEntity;
import twilightforest.entity.TFPartEntity;
import twilightforest.extensions.IEntityEx;

import java.util.List;

public class UpdateTFMultipartPacket extends ISimplePacket {

	private int id;
	private FriendlyByteBuf buffer;
	private Entity entity;

	public UpdateTFMultipartPacket(FriendlyByteBuf buf) {
		id = buf.readInt();
		buffer = buf;
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this.entity = entity;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entity.getId());
		PartEntity<?>[] parts = ((IEntityEx)entity).getParts();
		// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
		if (parts != null) {
			for (PartEntity<?> part : parts) {
				if (part instanceof TFPartEntity) {
					TFPartEntity<?> tfPart = (TFPartEntity<?>) part;
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
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {
		public static void onMessage(UpdateTFMultipartPacket message) {
			Level world = Minecraft.getInstance().level;
			if (world == null)
				return;
			Entity ent = world.getEntity(message.id);
			if (ent != null && ((IEntityEx)ent).isMultipartEntity()) {
				PartEntity<?>[] parts = ((IEntityEx)ent).getParts();
				if (parts == null)
					return;
				for (PartEntity<?> part : parts) {
					if (part instanceof TFPartEntity) {
						TFPartEntity<?> tfPart = (TFPartEntity<?>) part;
						tfPart.readData(message.buffer);
						if (message.buffer.readBoolean()) {
							List<SynchedEntityData.DataItem<?>> data = SynchedEntityData.unpack(message.buffer);
							if (data != null)
								tfPart.getEntityData().assignValues(data);
						}
					}
				}
			}
		}
	}
}
