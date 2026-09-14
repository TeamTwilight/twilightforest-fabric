package twilightforest.util.multiparts;

import carminite.multipart.IMultiPartEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import twilightforest.network.UpdateTFMultipartPacket;

public class MultipartEntityUtil {
	public static final MultipartEntityUtil INSTANCE = new MultipartEntityUtil();

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof IMultiPartEntity multiPartEntity && multiPartEntity.isMultipartEntity()) {
			for (ServerPlayer player : PlayerLookup.tracking(entity)) {
				ServerPlayNetworking.send(player, new UpdateTFMultipartPacket(entity));
			}
		}
		return entity;
	}
}