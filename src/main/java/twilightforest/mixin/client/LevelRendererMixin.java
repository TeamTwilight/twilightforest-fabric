package twilightforest.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.TwilightForestRenderInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow @Nullable private ClientLevel level;

    @Shadow private int ticks;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    public void twilightforest$weatherRenderer(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (level.effects() != null && level.effects() instanceof TwilightForestRenderInfo tfEffects) {
            tfEffects.getWeatherRenderHandler().render(ticks, partialTick, level, minecraft, lightTexture, camX, camY, camZ);
            ci.cancel();
        }
    }
}
