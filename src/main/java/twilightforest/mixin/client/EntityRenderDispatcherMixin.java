package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.ASMHooks;
import twilightforest.client.model.entity.PartEntity;
import twilightforest.extensions.IEntityEx;

import java.util.Map;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    //TODO: Make this not a overwrite

    @Shadow public abstract <T extends Entity> EntityRenderer<? super T> getRenderer(T entity);

    @Shadow private Map<EntityType<?>, EntityRenderer<?>> renderers;

    /**
     * @author AlphaMode
     * fuck you forge I don't want to write a actual mixin rn
     */
    @Overwrite
    private static void renderHitbox(PoseStack pMatrixStack, VertexConsumer pBuffer, Entity pEntity, float pPartialTicks) {
        AABB aabb = pEntity.getBoundingBox().move(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ());
        LevelRenderer.renderLineBox(pMatrixStack, pBuffer, aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        if (((IEntityEx)pEntity).isMultipartEntity()) {
            double d0 = -Mth.lerp(pPartialTicks, pEntity.xOld, pEntity.getX());
            double d1 = -Mth.lerp(pPartialTicks, pEntity.yOld, pEntity.getY());
            double d2 = -Mth.lerp(pPartialTicks, pEntity.zOld, pEntity.getZ());

            for(PartEntity<?> enderdragonpart : ((IEntityEx)pEntity).getParts()) {
                pMatrixStack.pushPose();
                double d3 = d0 + Mth.lerp(pPartialTicks, enderdragonpart.xOld, enderdragonpart.getX());
                double d4 = d1 + Mth.lerp(pPartialTicks, enderdragonpart.yOld, enderdragonpart.getY());
                double d5 = d2 + Mth.lerp(pPartialTicks, enderdragonpart.zOld, enderdragonpart.getZ());
                pMatrixStack.translate(d3, d4, d5);
                LevelRenderer.renderLineBox(pMatrixStack, pBuffer, enderdragonpart.getBoundingBox().move(-enderdragonpart.getX(), -enderdragonpart.getY(), -enderdragonpart.getZ()), 0.25F, 1.0F, 0.0F, 1.0F);
                pMatrixStack.popPose();
            }
        }
        if (pEntity instanceof EnderDragon) {
            double d = -Mth.lerp(pPartialTicks, pEntity.xOld, pEntity.getX());
            double e = -Mth.lerp(pPartialTicks, pEntity.yOld, pEntity.getY());
            double g = -Mth.lerp(pPartialTicks, pEntity.zOld, pEntity.getZ());
            EnderDragonPart[] var11 = ((EnderDragon)pEntity).getSubEntities();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                EnderDragonPart enderDragonPart = var11[var13];
                pMatrixStack.pushPose();
                double h = d + Mth.lerp(pPartialTicks, enderDragonPart.xOld, enderDragonPart.getX());
                double i = e + Mth.lerp(pPartialTicks, enderDragonPart.yOld, enderDragonPart.getY());
                double j = g + Mth.lerp(pPartialTicks, enderDragonPart.zOld, enderDragonPart.getZ());
                pMatrixStack.translate(h, i, j);
                LevelRenderer.renderLineBox(pMatrixStack, pBuffer, enderDragonPart.getBoundingBox().move(-enderDragonPart.getX(), -enderDragonPart.getY(), -enderDragonPart.getZ()), 0.25F, 1.0F, 0.0F, 1.0F);
                pMatrixStack.popPose();
            }
        }

        if (pEntity instanceof LivingEntity) {
            float f = 0.01F;
            LevelRenderer.renderLineBox(pMatrixStack, pBuffer, aabb.minX, (double)(pEntity.getEyeHeight() - 0.01F), aabb.minZ, aabb.maxX, (double)(pEntity.getEyeHeight() + 0.01F), aabb.maxZ, 1.0F, 0.0F, 0.0F, 1.0F);
        }

        Vec3 vec3 = pEntity.getViewVector(pPartialTicks);
        Matrix4f matrix4f = pMatrixStack.last().pose();
        Matrix3f matrix3f = pMatrixStack.last().normal();
        pBuffer.vertex(matrix4f, 0.0F, pEntity.getEyeHeight(), 0.0F).color(0, 0, 255, 255).normal(matrix3f, (float)vec3.x, (float)vec3.y, (float)vec3.z).endVertex();
        pBuffer.vertex(matrix4f, (float)(vec3.x * 2.0D), (float)((double)pEntity.getEyeHeight() + vec3.y * 2.0D), (float)(vec3.z * 2.0D)).color(0, 0, 255, 255).normal(matrix3f, (float)vec3.x, (float)vec3.y, (float)vec3.z).endVertex();
    }

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void nonAsmCursedness(ResourceManager resourceManager, CallbackInfo ci, EntityRendererProvider.Context context) {
        ASMHooks.bakeMultipartRenders(context);
    }

//    @Inject(method = "getRenderer", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
//    public <T extends Entity> void getMultipartRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
//        ASMHooks.getMultipartRenderer(this.renderers.get(entity.getType()), entity);
//    }
}
