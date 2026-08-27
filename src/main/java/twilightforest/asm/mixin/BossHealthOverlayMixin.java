package twilightforest.asm.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.bar.ClientTFBossBar;

/**
 * Recreates NeoForge's CustomizeGuiOverlayEvent.BossEventProgress handling:
 * twilight boss bars use their custom colour rendering instead of the default.
 */
@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {

	@Inject(method = "extractBar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/BossEvent;)V", at = @At("HEAD"), cancellable = true)
	private void twilightforest$renderCustomBossBar(GuiGraphicsExtractor extractor, int x, int y, BossEvent event, CallbackInfo ci) {
		if (event instanceof ClientTFBossBar bossBar) {
			bossBar.renderBossBar(extractor, x, y);
			ci.cancel();
		}
	}
}
