package twilightforest.asm.hooks.coremod;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.util.multiparts.MultipartEntityUtil;

import java.util.Iterator;

@SuppressWarnings({"JavadocReference", "unused"})
public class MultipartHooks {

	
	private static final MultipartEntityUtil multipartEntityUtil = MultipartEntityUtil.INSTANCE;

	/**
	 * twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer<p/>
	 *
	 * Injection Point:<br/>
	 * net.minecraft.client.renderer.LevelRenderer#renderLevel(DeltaTracker, boolean, Camera, GameRenderer, LightTexture, Matrix4f, Matrix4f)<br/>
	 * [Targets: net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering]
	 */
	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	/**
	 * twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer<p/>
	 *
	 * Injection Point:<br/>
	 * net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)<br/>
	 * Targets: net.minecraft.client.renderer.entity.EntityRenderDispatcher#renderers
	 */
	@Nullable
	public static EntityRenderer<?, ?> resolveEntityRenderer(@Nullable EntityRenderer<?, ?> renderer, Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	/**
	 * SendDirtyEntityDataTransformer<p/>
	 *
	 * Injection Point:<br/>
	 * net.minecraft.server.level.ServerEntity#sendDirtyEntityData
	 */
	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

}
