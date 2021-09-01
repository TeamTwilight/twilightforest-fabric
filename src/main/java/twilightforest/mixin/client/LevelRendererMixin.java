package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderSnowAndRain", at = @At("HEAD"))
    public void renderCustom(LightTexture lightTexture, float f, double d, double e, double g, CallbackInfo ci) {

    }
}
