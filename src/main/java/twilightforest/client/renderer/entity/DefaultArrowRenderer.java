package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class DefaultArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T> {
	public static final Identifier RES_ARROW = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");

	public DefaultArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Identifier getTextureLocation(T entity) {
		return RES_ARROW;
	}
}
