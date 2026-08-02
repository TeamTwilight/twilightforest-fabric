package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.entity.boss.bar.ClientTFBossBar;

@Environment(EnvType.CLIENT)
@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {

	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"
		)
	)
	private void twilightforest$renderCustomBossBar(BossHealthOverlay overlay, GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, Operation<Void> original) {
		if (bossEvent instanceof ClientTFBossBar customBar) {
			customBar.renderBossBar(guiGraphics, x, y);
		} else {
			original.call(overlay, guiGraphics, x, y, bossEvent);
		}
	}
}