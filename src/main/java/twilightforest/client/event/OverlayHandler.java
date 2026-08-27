package twilightforest.client.event;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.client.overlay.PortalOverlay;
import twilightforest.components.item.OreScannerData;
import twilightforest.config.TFConfig;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;
import twilightforest.util.ComponentAlignment;

import java.text.DecimalFormat;
import java.util.*;

public class OverlayHandler {
	private static final Identifier QUESTING_RAM_CHECK_SPRITE = TFMain.prefix("questing_ram_check");
	private static final Identifier QUESTING_RAM_X_SPRITE = TFMain.prefix("questing_ram_x");
	private static final Identifier FORTIFICATION_SHIELD_SPRITE = TFMain.prefix("fortification_shield");
	public static final Map<Long, OreMeterInfoCache> ORE_METER_STAT_CACHE = new HashMap<>();

	private static final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.INSTANCE;

	public static void init() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, TFMain.prefix("quest_ram_indicator"), (graphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			Gui gui = minecraft.gui;
			if (player != null && !minecraft.options.hideGui && TFConfig.showQuestRamCrosshairIndicator) {
				renderIndicator(minecraft, graphics, gui, player, graphics.guiWidth(), graphics.guiHeight());
			}
		});
		HudElementRegistry.attachElementAfter(VanillaHudElements.MOUNT_HEALTH, TFMain.prefix("hostile_mount_hunger_bar"), (graphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			Gui gui = minecraft.gui;
			if (!minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer() && player != null && HostileMountEvents.isRidingUnfriendly(player)) {
				int xPos = graphics.guiWidth() / 2 + 91;
				int yPos = graphics.guiHeight() - HudStatusBarHeightRegistry.getHeight(TFMain.prefix("hostile_mount_hunger_bar"));
				gui.extractFood(graphics, player, yPos, xPos);
			}
		});
		HudStatusBarHeightRegistry.addRight(TFMain.prefix("hostile_mount_hunger_bar"), player -> 10);
		HudElementRegistry.addLast(TFMain.prefix("ore_meter_stats"), (graphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			Gui gui = minecraft.gui;
			if (player != null && !minecraft.options.hideGui && !gui.getDebugOverlay().showDebugScreen() && minecraft.screen == null) {
				renderOreMeterStats(graphics, player);
			}
		});
		HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, TFMain.prefix("fortification_shield_count"), (graphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (player != null && !minecraft.options.hideGui && (minecraft.gameMode.canHurtPlayer() || TFConfig.showFortificationShieldIndicatorInCreative) && ((AttachmentTarget) player).hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS) && ((AttachmentTarget) player).getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft() > 0 && TFConfig.showFortificationShieldIndicator) {
				renderShieldCount(graphics, graphics.guiWidth(), graphics.guiHeight(), ((AttachmentTarget) player).getAttached(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft());
			}
		});
		HudStatusBarHeightRegistry.addLeft(TFMain.prefix("fortification_shield_count"), player -> 10);
		HudElementRegistry.addLast(TFMain.prefix("portal_overlay"), (graphics, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			PortalOverlay.render(graphics, minecraft, minecraft.player);
		});

		// TODO [Fabric] item display overlay needs the ItemDisplayType renderers
		// migrated from GuiGraphics to the extractor stage (GuiGraphicsExtractor#item)
	}

	private static void renderIndicator(Minecraft minecraft, GuiGraphicsExtractor graphics, Gui gui, Player player, int screenWidth, int screenHeight) {
		if (minecraft.options.getCameraType().isFirstPerson() && (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.canRenderCrosshairForSpectator(minecraft.hitResult)) && minecraft.crosshairPickEntity instanceof QuestRam ram) {
			ItemStack stack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
			if (!stack.isEmpty()) {
				for (var questEntry : questingRamCurrentContext.getContext().questItems().entrySet()) {
					if (questEntry.getValue().test(stack)) {
						int j = ((screenHeight - 1) / 2) - 11;
						int k = ((screenWidth - 1) / 2) - 3;
						if (!ram.isColorPresent(questEntry.getKey())) {
							graphics.blitSprite(RenderPipelines.CROSSHAIR, QUESTING_RAM_X_SPRITE, k, j, 7, 7);
						} else {
							graphics.blitSprite(RenderPipelines.CROSSHAIR, QUESTING_RAM_CHECK_SPRITE, k, j, 7, 7);
						}
						break;
					}
				}
			}
		}
	}

	private static void renderShieldCount(GuiGraphicsExtractor graphics, int screenWidth, int screenHeight, int shieldCount) {
		for (int i = 0; i < Math.min(shieldCount, 10); i++) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTIFICATION_SHIELD_SPRITE, screenWidth / 2 - 91 + (i * 8), screenHeight - HudStatusBarHeightRegistry.getHeight(TFMain.prefix("fortification_shield_count")), 9, 9);
		}
	}

	private static void renderOreMeterStats(GuiGraphicsExtractor graphics, Player player) {
		if (player.isHolding(TFItems.ORE_METER)) {
			InteractionHand handToUse = player.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.ORE_METER) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			ItemStack selectedMeter = player.getItemInHand(handToUse);
			if (OreMeterItem.isLoading(selectedMeter)) {
				int dots = (OreMeterItem.getLoadProgress(selectedMeter) / 5) % 3;
				Component component = Component.translatable("misc.twilightforest.ore_meter_loading");
				for (int i = 0; i <= dots; i++) {
					component = component.copy().append(".");
				}
				graphics.fill(0, 0, 56, 16, 0x9b000000);
				graphics.text(Minecraft.getInstance().font, component, 4, 4, 16777215, false);
			} else {
				OreScannerData oreScannerData = selectedMeter.get(TFDataComponents.ORE_DATA);

				if (oreScannerData == null) return;

				long identifier = oreScannerData.universalId();
				if (identifier != 0L && !ORE_METER_STAT_CACHE.containsKey(identifier)) {
					initTooltips(identifier, selectedMeter.getOrDefault(TFDataComponents.ORE_RANGE, 1), oreScannerData);
				}

				if (ORE_METER_STAT_CACHE.containsKey(identifier)) {
					OreMeterInfoCache info = ORE_METER_STAT_CACHE.get(identifier);

					if (info != null) {
						info.renderData(graphics);
					}
				}
			}
		}
	}

	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	private static void initTooltips(long identifier, int oreRange, OreScannerData oreScannerData) {
		ImmutableList.Builder<OreMeterInfoCache> builder = ImmutableList.builder();

		int y = 0;
		List<Map.Entry<String, Integer>> ores = oreScannerData.counts().entrySet().stream()
			.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
			.toList();

		for (Map.Entry<String, Integer> ore : ores) {
			String descriptionId = ore.getKey();
			Identifier oreId = Identifier.parse(descriptionId.substring(descriptionId.indexOf('.') + 1));
			ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(oreId));
			if (stack.isEmpty()) continue;
			float percent = ore.getValue() / (float) (oreRange * oreRange * oreRange);
			Component text = Component.translatable(stack.getItem().getDescriptionId()).append(": " + FORMAT.format(percent * 100F) + "%");

			builder.add(new OreMeterInfoCache(stack, ore.getValue(), percent, text, y));
			y += 20;
		}

		ORE_METER_STAT_CACHE.put(identifier, new OreMeterInfoCache(builder.build()));
	}

	// TODO old code that still needs to be ported, which relies on GuiGraphics
	@Nullable
	private static Player getCameraPlayer() {
		return Minecraft.getInstance().player;
	}

	public record OreMeterInfoCache(ItemStack stack, int count, float percent, Component text, int offset) {
		private OreMeterInfoCache(List<OreMeterInfoCache> infos) {
			this(infos.get(0).stack(), infos.get(0).count(), infos.get(0).percent(), infos.get(0).text(), 0);
		}

		void renderData(GuiGraphicsExtractor graphics) {
			int x = 10;
			int y = 10 + offset;
			graphics.fill(x, y, x + 110, y + 20, 0x8b000000);
			graphics.item(stack, x + 3, y + 2);
			graphics.text(Minecraft.getInstance().font, text, x + 25, y + 7, 0xffffff, true);
		}
	}
}
