package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.entity.boss.HydraHead;

public class HydraHeadRenderer<T extends HydraHead, M extends ListModel<T>> extends TFPartRenderer<T, M> {
    public HydraHeadRenderer(EntityRendererProvider.Context context, M model) {
        super(context, model);
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        if (entity.isActive()) {
            stack.mulPose(Axis.YP.rotationDegrees(-180.0F));
            super.render(entity, yaw, partialTicks, stack, buffer, light);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return HydraRenderer.TEXTURE;
    }
}
