package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.client.accessor.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;

import java.util.Objects;

public class TFOverlays {
	//for some reason we need a 256x256 texture to actually render anything so i'll just make this a generic icons sheet
	//if we want to add any more overlay things in the future, we can simply add more icons!
	private static final ResourceLocation TF_ICONS_SHEET = TwilightForestMod.prefix("textures/gui/tf_icons.png");

	public static void registerOverlays(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player != null && !minecraft.options.hideGui && TFConfig.CLIENT_CONFIG.showQuestRamCrosshairIndicator.get()) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, TF_ICONS_SHEET);
			RenderSystem.enableBlend();
			renderIndicator(minecraft, poseStack, gui, player, screenWidth, screenHeight);
			RenderSystem.disableBlend();
		}
	}

	public static void renderIndicator(Minecraft minecraft, GuiGraphics graphics, Gui gui, Player player, int screenWidth, int screenHeight) {
		Options options = minecraft.options;
		if (options.getCameraType().isFirstPerson()) {
			if (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || ((GuiAccessor)gui).porting_lib$canRenderCrosshairForSpectator(minecraft.hitResult)) {
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				int j = ((screenHeight - 1) / 2) - 11;
				int k = ((screenWidth - 1) / 2) - 3;
				if (minecraft.crosshairPickEntity instanceof QuestRam ram) {
					ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
					if (!stack.isEmpty() && stack.is(ItemTags.WOOL)) {
						if (ram.guessColor(stack) != null && !ram.isColorPresent(Objects.requireNonNull(ram.guessColor(stack)))) {
							graphics.blit(TF_ICONS_SHEET, k, j, 0, 0, 7, 7);
						} else {
							graphics.blit(TF_ICONS_SHEET, k, j, 7, 0, 7, 7);
						}
					}
				}
			}
		}
	}
}
