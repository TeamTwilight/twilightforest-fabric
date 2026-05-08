package com.codex.twilight.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

/**
 * F2.4 — generic textured renderer used by every TF Mob entity that doesn't need
 * a custom model. The model is whatever vanilla mesh fits the silhouette
 * (HumanoidModel for kobolds and goblins, SpiderModel for spiders, WolfModel for
 * wolves, etc.) and the texture is the upstream Twilight Forest sprite shipped
 * inside the mod jar at {@code assets/twilightforest/textures/entity/<file>.png}.
 *
 * <p>This class only adapts the texture binding — it does not reproduce any
 * model geometry. The model classes are stock Minecraft client renderers
 * accessed via {@code ctx.bakeLayer(ModelLayers.X)}; per-entity proportion
 * adaptations (kobold ears, hydra heads, etc.) are deferred to F2.1d / F2.4d.
 */
public final class TFGenericMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation texture;

    public TFGenericMobRenderer(EntityRendererProvider.Context ctx, M model, float shadowSize, ResourceLocation texture) {
        super(ctx, model, shadowSize);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }
}
