package twilightforest.asmhooks;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.util.multiparts.MultipartEntityUtil;

import java.util.Iterator;

public class MultipartHooks {

	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iterator) {
		return BakedMultiPartRenderers.injectTFPartEntities(() -> iterator).iterator();
	}

	@Nullable
	public static EntityRenderer<?> resolveEntityRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer<?> multipartRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			return multipartRenderer != null ? multipartRenderer : renderer;
		}
		return renderer;
	}

	public static Entity sendDirtyEntityData(Entity entity) {
		return MultipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}
}
