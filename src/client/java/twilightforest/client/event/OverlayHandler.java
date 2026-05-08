package twilightforest.client.event;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.client.overlay.PortalOverlay;
import twilightforest.client.overlay.QuestingRamIndicatorOverlay;
import twilightforest.client.overlay.ShieldOverlay;
import twilightforest.components.item.OreScannerData;
import twilightforest.config.TFConfig;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;
import twilightforest.util.ComponentAlignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OverlayHandler {
	public static final Map<Long, OreMeterInfoCache> ORE_METER_STAT_CACHE = new HashMap<>();
	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	private OverlayHandler() {
	}

	public static void bootstrap() {
		HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			Player player = getCameraPlayer();
			if (minecraft.level == null || player == null) {
				return;
			}

			QuestingRamIndicatorOverlay.render(minecraft, graphics, minecraft.gui, player);
			renderHostileMountHunger(minecraft, graphics, player);
			renderOreMeterStats(graphics, minecraft, player);
			ShieldOverlay.render(graphics, minecraft, minecraft.gui, player);
			PortalOverlay.render(graphics, minecraft, player);
			ItemDisplayOverlay.render(graphics, minecraft, minecraft.getWindow(), minecraft.gui, player);
		});
	}

	private static void renderHostileMountHunger(Minecraft minecraft, GuiGraphics graphics, Player player) {
		if (!minecraft.options.hideGui && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer() && HostileMountEvents.isRidingUnfriendly(player)) {
			int xPos = graphics.guiWidth() / 2 + 91;
			int yPos = graphics.guiHeight() - 39;
			minecraft.gui.renderFood(graphics, player, yPos, xPos);
		}
	}

	private static void renderOreMeterStats(GuiGraphics graphics, Minecraft minecraft, Player player) {
		if (minecraft.options.hideGui || minecraft.gui.getDebugOverlay().showDebugScreen() || minecraft.screen != null || !player.isHolding(TFItems.ORE_METER.get())) {
			return;
		}

		InteractionHand handToUse = player.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.ORE_METER.get()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		ItemStack selectedMeter = player.getItemInHand(handToUse);
		if (OreMeterItem.isLoading(selectedMeter)) {
			int dots = (OreMeterItem.getLoadProgress(selectedMeter) / 5) % 3;
			Component component = Component.translatable("misc.twilightforest.ore_meter_loading");
			for (int i = 0; i <= dots; i++) {
				component = component.copy().append(".");
			}
			graphics.fill(0, 0, 56, 16, 0x9b000000);
			graphics.drawString(minecraft.font, component, 4, 4, 0xFFFFFF, false);
			return;
		}

		OreScannerData oreScannerData = selectedMeter.get(TFDataComponents.ORE_DATA);
		if (oreScannerData == null) {
			return;
		}

		long identifier = oreScannerData.universalId();
		if (identifier != 0L && !ORE_METER_STAT_CACHE.containsKey(identifier)) {
			initTooltips(identifier, selectedMeter.getOrDefault(TFDataComponents.ORE_RANGE, 1), oreScannerData);
		}

		OreMeterInfoCache info = ORE_METER_STAT_CACHE.get(identifier);
		if (info != null) {
			info.renderData(graphics);
		}
	}

	private static void initTooltips(long id, int range, OreScannerData data) {
		ChunkPos pos = data.scannedChunk();
		int totalScanned = data.totalScannedBlocks();

		List<Component> headerRowTexts = ImmutableList.of(
			Component.translatable("misc.twilightforest.ore_meter_range", range, pos.x, pos.z),
			Component.translatable("misc.twilightforest.ore_meter_total", totalScanned)
		);

		ArrayList<ComponentColumn> columns = new ArrayList<>();
		List<Pair<String, Integer>> scanData = data.counts().entrySet().stream()
			.map(e -> Pair.of(e.getKey(), e.getValue()))
			.sorted(Comparator.comparing(Pair::getSecond))
			.toList();

		if (TFConfig.prettifyOreMeterGui) {
			ComponentColumn padding = ComponentColumn.padding(1);
			List<Integer> counts = scanData.stream().map(Pair::getSecond).toList();
			columns.add(nameColumn(scanData.stream().map(Pair::getFirst).toList()));
			columns.add(padding);
			columns.add(dashColumn(scanData.size()));
			columns.add(padding);
			columns.add(countColumn(counts));
			columns.add(padding);
			columns.add(ratioColumn(totalScanned, counts));
		} else {
			columns.add(withoutPrettyPrinting(totalScanned, scanData));
		}

		ORE_METER_STAT_CACHE.put(id, OreMeterInfoCache.build(headerRowTexts, columns));
	}

	private static ComponentColumn withoutPrettyPrinting(int totalScanned, List<Pair<String, Integer>> entries) {
		List<Component> tooltips = new ArrayList<>();
		for (Pair<String, Integer> entry : entries) {
			String percentage = FORMAT.format(entry.getSecond() * 100.0F / totalScanned);
			MutableComponent formattedEntry = Component.translatable(entry.getFirst())
				.append(Component.literal(" "))
				.append(Component.translatable("misc.twilightforest.ore_meter_separator"))
				.append(Component.literal(" " + entry.getSecond() + " "))
				.append(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));
			tooltips.add(formattedEntry);
		}
		return ComponentColumn.build(tooltips, ComponentAlignment.LEFT);
	}

	private static ComponentColumn nameColumn(List<String> oreNameKeys) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();
		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_block").withStyle(ChatFormatting.GRAY));
		for (String oreNameKey : oreNameKeys) {
			toList.add(Component.translatable(oreNameKey));
		}
		return ComponentColumn.build(toList.build(), ComponentAlignment.LEFT);
	}

	private static ComponentColumn dashColumn(int size) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();
		toList.add(Component.empty());
		MutableComponent dash = Component.translatable("misc.twilightforest.ore_meter_separator");
		for (int i = 0; i < size; i++) {
			toList.add(dash);
		}
		return ComponentColumn.build(toList.build(), ComponentAlignment.CENTER);
	}

	private static ComponentColumn countColumn(List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();
		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_count").withStyle(ChatFormatting.GRAY));
		oreCounts.stream().mapToInt(count -> count).mapToObj(count -> Component.literal(String.valueOf(count))).forEach(toList::add);
		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	private static ComponentColumn ratioColumn(int totalScanned, List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();
		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_ratio").withStyle(ChatFormatting.GRAY));
		for (int count : oreCounts) {
			String percentage = FORMAT.format(count * 100.0F / totalScanned);
			toList.add(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));
		}
		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	public record ComponentColumn(List<? extends Component> textRows, int maxPixelWidth, ComponentAlignment textAlignment) {
		public static ComponentColumn build(List<? extends Component> rowTexts, ComponentAlignment textAlignment) {
			int maxColumnPixelWidth = rowTexts.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);
			return new ComponentColumn(rowTexts, maxColumnPixelWidth, textAlignment);
		}

		public static ComponentColumn padding(int forcedExtraMaxWidthBySpaces) {
			return new ComponentColumn(List.of(), forcedExtraMaxWidthBySpaces * Minecraft.getInstance().font.width(" "), ComponentAlignment.LEFT);
		}

		private int renderColumn(GuiGraphics graphics, int xOff, int yOff, int verticalTextPixelsAdvance) {
			for (Component rowText : this.textRows) {
				int textPixelWidth = Minecraft.getInstance().font.width(rowText);
				int textXPos = xOff + this.textAlignment.getTextOffset(textPixelWidth, this.maxPixelWidth);
				graphics.drawString(Minecraft.getInstance().font, rowText, textXPos, yOff, 0xFFFFFF, false);
				yOff += verticalTextPixelsAdvance;
			}
			return this.maxPixelWidth;
		}
	}

	public record OreMeterInfoCache(int totalPixelWidth, int totalRowCount, List<Component> headerRows, List<ComponentColumn> textColumns) {
		public static OreMeterInfoCache build(List<Component> headers, List<ComponentColumn> columns) {
			int summedColumnMaxWidths = columns.stream().mapToInt(ComponentColumn::maxPixelWidth).sum();
			int maxHeaderWidth = headers.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);
			int maxPixelWidth = Math.max(summedColumnMaxWidths, maxHeaderWidth);
			int totalRowCount = headers.size() + columns.stream().mapToInt(column -> column.textRows.size()).max().orElse(0);
			return new OreMeterInfoCache(maxPixelWidth, totalRowCount, ImmutableList.copyOf(headers), ImmutableList.copyOf(columns));
		}

		public void renderData(GuiGraphics graphics) {
			int verticalTextPixelsAdvance = Minecraft.getInstance().font.lineHeight + 1;
			graphics.fill(0, 0, this.totalPixelWidth + 8, this.totalRowCount * verticalTextPixelsAdvance + 6, 0x9b000000);
			int xOff = 4;
			int yOff = 4;
			for (Component headerRowText : this.headerRows) {
				graphics.drawString(Minecraft.getInstance().font, headerRowText, xOff, yOff, 0xFFFFFF, false);
				yOff += verticalTextPixelsAdvance;
			}
			for (ComponentColumn column : this.textColumns) {
				xOff += column.renderColumn(graphics, xOff, yOff, verticalTextPixelsAdvance);
			}
		}
	}

	private static Player getCameraPlayer() {
		return Minecraft.getInstance().getCameraEntity() instanceof Player player ? player : null;
	}
}
