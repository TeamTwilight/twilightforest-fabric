package twilightforest.util.multiparts;

import carminite.multipart.IMultiPartEntity;
import carminite.network.PacketDistributor;
import net.minecraft.world.entity.Entity;
import twilightforest.network.UpdateTFMultipartPacket;

public class MultipartEntityUtil {
	public static final MultipartEntityUtil INSTANCE = new MultipartEntityUtil();

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof IMultiPartEntity multiPartEntity && multiPartEntity.isMultipartEntity())
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}