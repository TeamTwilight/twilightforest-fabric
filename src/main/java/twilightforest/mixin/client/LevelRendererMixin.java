package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.ASMHooks;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;

import java.util.Iterator;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow private ClientLevel level;

    @Shadow private int ticks;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    public void renderCustom(LightTexture lightTexture, float f, double d, double e, double g, CallbackInfo ci) {
        if(level.effects() instanceof TwilightForestRenderInfo twilightForestRenderInfo) {
            twilightForestRenderInfo.getWeatherRenderHandler().render(this.ticks, f, this.level, this.minecraft, lightTexture, d, e, g);
            ci.cancel();
        }
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V", shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    public void renderTwilightSky(PoseStack poseStack, Matrix4f matrix4f, float f, Runnable runnable, CallbackInfo ci) {
        if(level.effects() instanceof TwilightForestRenderInfo twilightForestRenderInfo) {
            twilightForestRenderInfo.getSkyRenderHandler().render(this.ticks, f,poseStack, this.level, this.minecraft);
            ci.cancel();
        }
    }


    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"), ordinal = 0)
    public Iterator<Entity> renderMultiparts(Iterator iterator) {
        return ASMHooks.renderMutiparts(iterator).iterator();
    }


}
