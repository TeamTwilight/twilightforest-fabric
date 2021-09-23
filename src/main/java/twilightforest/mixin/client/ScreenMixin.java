package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.TFClientEvents;
import twilightforest.client.TFClientSetup;

import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "init()V", at = @At("TAIL"))
    public void checkOptifine(CallbackInfo ci) {
        TFClientEvents.showOptifineWarning((Screen) (Object) this);
    }
}
