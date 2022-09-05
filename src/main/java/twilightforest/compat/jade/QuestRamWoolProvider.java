package twilightforest.compat.jade;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import snownee.jade.Jade;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;

import java.util.AbstractMap;
import java.util.Map;

public enum QuestRamWoolProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final Map<DyeColor, Block> WOOL_TO_DYE = ImmutableMap.ofEntries(
			entryOf(DyeColor.WHITE, Blocks.WHITE_WOOL), entryOf(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL),
			entryOf(DyeColor.GRAY, Blocks.GRAY_WOOL), entryOf(DyeColor.BLACK, Blocks.BLACK_WOOL),
			entryOf(DyeColor.RED, Blocks.RED_WOOL), entryOf(DyeColor.ORANGE, Blocks.ORANGE_WOOL),
			entryOf(DyeColor.YELLOW, Blocks.YELLOW_WOOL), entryOf(DyeColor.GREEN, Blocks.GREEN_WOOL),
			entryOf(DyeColor.LIME, Blocks.LIME_WOOL), entryOf(DyeColor.BLUE, Blocks.BLUE_WOOL),
			entryOf(DyeColor.CYAN, Blocks.CYAN_WOOL), entryOf(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL),
			entryOf(DyeColor.PURPLE, Blocks.PURPLE_WOOL), entryOf(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL),
			entryOf(DyeColor.PINK, Blocks.PINK_WOOL), entryOf(DyeColor.BROWN, Blocks.BROWN_WOOL));

	static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
		if (entityAccessor.getEntity() instanceof QuestRam ram) {
			int getRenderedWools = 0;
			for (Map.Entry<DyeColor, Block> entry : WOOL_TO_DYE.entrySet()) {
				if (!ram.isColorPresent(entry.getKey())) {
					if (getRenderedWools % 8 == 0) {
						tooltip.add(Jade.smallItem(tooltip.getElementHelper(), new ItemStack(entry.getValue())).size(new Vec2(4.0F, 4.0F)));
					} else {
						tooltip.append(Jade.smallItem(tooltip.getElementHelper(), new ItemStack(entry.getValue())).size(new Vec2(4.0F, 4.0F)));
					}
					tooltip.append(Component.literal(" "));
					getRenderedWools++;
				}
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return TwilightForestMod.prefix("quest_ram_wool");
	}
}
