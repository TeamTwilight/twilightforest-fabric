package twilightforest.client.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.client.event.OverlayHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public class ShieldOverlay {
	private static final Identifier FORTIFICATION_SHIELD_SPRITE = TFMain.prefix("fortification_shield");

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, @Nullable Player player) {
		if (player != null && !minecraft.options.hideGui && (minecraft.gameMode.canHurtPlayer() || TFConfig.showFortificationShieldIndicatorInCreative) && player.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS) && player.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft() > 0 && TFConfig.showFortificationShieldIndicator) {
			int shieldCount = player.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft();
			for (int i = 0; i < Math.min(shieldCount, 10); i++) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTIFICATION_SHIELD_SPRITE, graphics.guiWidth() / 2 - 91 + (i * 8), graphics.guiHeight() - HudStatusBarHeightRegistry.getHeight(OverlayHandler.FORTIFICATION_SHIELD_COUNT), 9, 9);
			}
		}
	}
}