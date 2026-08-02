package twilightforest.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.HostileMountEvents;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {

	@Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
	private void twilightforest$removeHostileMountHealth(CallbackInfo ci) {
		if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
			ci.cancel();
		}
	}
}