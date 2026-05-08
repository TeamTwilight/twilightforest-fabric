package com.codex.twilight.client.render.entity;

import com.codex.twilight.client.render.CodexModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.entity.KoboldModel;
import twilightforest.entity.monster.Kobold;

public class KoboldEntityRenderer extends HumanoidMobRenderer<Kobold, KoboldModel> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("twilightforest", "textures/entity/kobold.png");

    private final Font font;

    public KoboldEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new KoboldModel(ctx.bakeLayer(CodexModelLayers.KOBOLD)), 0.4F);
        this.font = ctx.getFont();
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)),
                ctx.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
    }

    @Override
    public void render(Kobold entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        super.render(entity, yaw, partialTicks, stack, buffer, light);
        if (entity.isPanicked() && !entity.isInvisible()) {
            this.renderPanicMarker(entity, stack, buffer);
        }
    }

    private void renderPanicMarker(Kobold entity, PoseStack stack, MultiBufferSource buffer) {
        String marker = "!";
        stack.pushPose();
        stack.translate(0.0D, entity.getBbHeight() + 0.35D, 0.0D);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.scale(-0.025F, -0.025F, 0.025F);
        float x = -this.font.width(marker) / 2.0F;
        this.font.drawInBatch(marker, x, 0.0F, 0xFFFF5555, false, stack.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, 0, 0xF000F0);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Kobold entity) {
        return TEXTURE;
    }
}
