package twilightforest.util.multiparts;

import carminite.multipart.IMultiPartEntity;
import carminite.network.PacketDistributor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;

public class MultipartEntityUtil {
	public static final MultipartEntityUtil INSTANCE = new MultipartEntityUtil();

	public Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
		return new MultipartEntityIteratorWrapper(iter);
	}

	@Nullable
	public EntityRenderer<?,?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?,?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part)
			return BakedMultiPartRenderers.lookup(part.renderer());
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof IMultiPartEntity)
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}