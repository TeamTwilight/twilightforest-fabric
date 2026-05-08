package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.entity.boss.HydraNeck;

public class HydraNeckRenderer<T extends HydraNeck, M extends ListModel<T>> extends TFPartRenderer<T, M> {
    public HydraNeckRenderer(EntityRendererProvider.Context context, M model) {
        super(context, model);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        if (entity.isActive()) {
            float yawDiff = entity.getYRot() - entity.yRotO;
            if (yawDiff > 180.0F) {
                yawDiff -= 360.0F;
            } else if (yawDiff < -180.0F) {
                yawDiff += 360.0F;
            }
            float yaw = entity.yRotO + yawDiff * partialTicks;
            stack.mulPose(Axis.YN.rotationDegrees(yaw + 180.0F));
            super.render(entity, entityYaw, partialTicks, stack, buffer, light);
        }
    }

    @Override
    protected float getFlipDegrees(T entity) {
        return 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return HydraRenderer.TEXTURE;
    }
}
