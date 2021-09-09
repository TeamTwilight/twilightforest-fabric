package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.ASMHooks;
import twilightforest.TwilightForestMod;
import twilightforest.extensions.IMapDecorationEx;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Mixin(MapRenderer.MapInstance.class)
public class MapRendererMixin {
    @Shadow private MapItemSavedData data;

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/MapRenderer$MapInstance;updateTexture()V"))
    public void hookMapRenderData(PoseStack poseStack, MultiBufferSource multiBufferSource, boolean bl, int light, CallbackInfo ci) {
        ASMHooks.mapRenderContext(poseStack, multiBufferSource, light);
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, boolean bl, int pPackedLight, CallbackInfo ci, int j, int k, float f, Matrix4f matrix4f, VertexConsumer consumer, int l) {
        for(MapDecoration mapdecoration : this.data.getDecorations()) {
            TwilightForestMod.LOGGER.info(mapdecoration.getName().getString());
            if (!bl || mapdecoration.renderOnFrame()) {
                if (((IMapDecorationEx)mapdecoration).render(l)) { l++; continue; }
                poseStack.pushPose();
                poseStack.translate(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F, 0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F, -0.02F);
                poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)(mapdecoration.getRot() * 360) / 16.0F));
                poseStack.scale(4.0F, 4.0F, 3.0F);
                poseStack.translate(-0.125D, 0.125D, 0.0D);
                byte b0 = mapdecoration.getImage();
                float f1 = (float)(b0 % 16 + 0) / 16.0F;
                float f2 = (float)(b0 / 16 + 0) / 16.0F;
                float f3 = (float)(b0 % 16 + 1) / 16.0F;
                float f4 = (float)(b0 / 16 + 1) / 16.0F;
                Matrix4f matrix4f1 = poseStack.last().pose();
                float f5 = -0.001F;
                VertexConsumer vertexconsumer1 = multiBufferSource.getBuffer(MapRenderer.MAP_ICONS);
                vertexconsumer1.vertex(matrix4f1, -1.0F, 1.0F, (float)l * -0.001F).color(255, 255, 255, 255).uv(f1, f2).uv2(pPackedLight).endVertex();
                vertexconsumer1.vertex(matrix4f1, 1.0F, 1.0F, (float)l * -0.001F).color(255, 255, 255, 255).uv(f3, f2).uv2(pPackedLight).endVertex();
                vertexconsumer1.vertex(matrix4f1, 1.0F, -1.0F, (float)l * -0.001F).color(255, 255, 255, 255).uv(f3, f4).uv2(pPackedLight).endVertex();
                vertexconsumer1.vertex(matrix4f1, -1.0F, -1.0F, (float)l * -0.001F).color(255, 255, 255, 255).uv(f1, f4).uv2(pPackedLight).endVertex();
                poseStack.popPose();
                if (mapdecoration.getName() != null) {
                    Font font = Minecraft.getInstance().font;
                    Component component = mapdecoration.getName();
                    float f6 = (float)font.width(component);
                    float f7 = Mth.clamp(25.0F / f6, 0.0F, 6.0F / 9.0F);
                    poseStack.pushPose();
                    poseStack.translate(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F - f6 * f7 / 2.0F, 0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F + 4.0F, -0.025F);
                    poseStack.scale(f7, f7, 1.0F);
                    poseStack.translate(0.0D, 0.0D, (double)-0.1F);
                    font.drawInBatch(component, 0.0F, 0.0F, -1, false, poseStack.last().pose(), multiBufferSource, false, Integer.MIN_VALUE, pPackedLight);
                    poseStack.popPose();
                }

                ++l;
            }
        }
        ci.cancel();
    }
}
