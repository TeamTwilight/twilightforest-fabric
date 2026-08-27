package twilightforest.asm.hooks.coremod;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import twilightforest.util.multiparts.MultipartEntityUtil;

import java.util.Iterator;

public final class MultipartHooks {
	private static final MultipartEntityUtil multipartEntityUtil = MultipartEntityUtil.INSTANCE;

	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	@Nullable
	public static EntityRenderer<?, ?> resolveEntityRenderer(@Nullable EntityRenderer<?, ?> renderer, Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}
}