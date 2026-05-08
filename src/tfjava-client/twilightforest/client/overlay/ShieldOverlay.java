package twilightforest.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public class ShieldOverlay {

	private static final ResourceLocation FORTIFICATION_SHIELD_SPRITE = TwilightForestMod.prefix("fortification_shield");

	public static void render(GuiGraphics graphics, Minecraft minecraft, Gui gui, @Nullable Player player) {
		if (player == null || minecraft.gameMode == null) return;
		int shieldCount = TFDataAttachments.get(player, TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft();
		if (!minecraft.options.hideGui && (minecraft.gameMode.canHurtPlayer() || TFConfig.showFortificationShieldIndicatorInCreative) && shieldCount > 0 && TFConfig.showFortificationShieldIndicator) {
			int y = graphics.guiHeight() - 49;
			for (int i = 0; i < Math.min(shieldCount, 10); i++) {
				graphics.blitSprite(FORTIFICATION_SHIELD_SPRITE, graphics.guiWidth() / 2 - 91 + (i * 8), y, 9, 9);
			}
		}
	}
}
