package twilightforest.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.TFOverlays;

@Mixin(Gui.class)
public class GuiMixin {
	@Shadow
	private int screenWidth;

	@Shadow
	private int screenHeight;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = At.Shift.AFTER))
	public void twilightforest$renderQuestingRamOverlay(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
		TFOverlays.registerOverlays((Gui) (Object) this, guiGraphics, partialTick, this.screenWidth, this.screenHeight);
		RenderSystem.defaultBlendFunc(); //Must happen or the sky rendering will die!!!!!
	}
}
