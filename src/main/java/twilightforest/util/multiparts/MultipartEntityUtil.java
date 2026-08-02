package twilightforest.util.multiparts;

import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import twilightforest.network.PacketDistributor;
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
	public EntityRenderer<?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer<?> partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			return partRenderer != null ? partRenderer : renderer;
		}
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof MultiPartEntity)
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}
