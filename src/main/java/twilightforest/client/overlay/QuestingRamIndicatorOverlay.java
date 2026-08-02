package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

public class QuestingRamIndicatorOverlay {

	private static final ResourceLocation QUESTING_RAM_CHECK_SPRITE = TwilightForestMod.prefix("questing_ram_check");
	private static final ResourceLocation QUESTING_RAM_X_SPRITE = TwilightForestMod.prefix("questing_ram_x");

	// QuestingRamCurrentContext is a simple wrapper with a FALLBACK default - instantiate directly
	// since static fields cannot be injected via Beanification on Fabric
	private static final QuestingRamCurrentContext questingRamCurrentContext = new QuestingRamCurrentContext();

	public static void render(Minecraft minecraft, GuiGraphics graphics, Gui gui, Player player) {
		if (player != null && !minecraft.options.hideGui && TFConfig.showQuestRamCrosshairIndicator) {
			if (minecraft.options.getCameraType().isFirstPerson() && (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.canRenderCrosshairForSpectator(minecraft.hitResult)) && minecraft.crosshairPickEntity instanceof QuestRam ram) {
				ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
				if (!stack.isEmpty()) {
					for (var questEntry : questingRamCurrentContext.getContext().questItems().entrySet()) {
						if (questEntry.getValue().test(stack)) {
							RenderSystem.enableBlend();
							RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
							int j = ((graphics.guiHeight() - 1) / 2) - 11;
							int k = ((graphics.guiHeight() - 1) / 2) - 3;
							if (!ram.isColorPresent(questEntry.getKey())) {
								graphics.blitSprite(QUESTING_RAM_X_SPRITE, k, j, 7, 7);
							} else {
								graphics.blitSprite(QUESTING_RAM_CHECK_SPRITE, k, j, 7, 7);
							}
							RenderSystem.defaultBlendFunc();
							RenderSystem.disableBlend();
							break;
						}
					}
				}
			}
		}
	}
}
