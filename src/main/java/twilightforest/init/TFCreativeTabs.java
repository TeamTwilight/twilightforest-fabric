package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;

public final class TFCreativeTabs {

	public static final CreativeModeTab BLOCKS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TwilightForestMod.prefix("blocks"), CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
		.title(Component.translatable("itemGroup.twilightforest.blocks"))
		.icon(() -> new ItemStack(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get()))
		.displayItems((parameters, output) -> acceptTwilightItems(output::accept, item -> item instanceof BlockItem))
		.build());

	public static final CreativeModeTab ITEMS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TwilightForestMod.prefix("items"), CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
		.title(Component.translatable("itemGroup.twilightforest.items"))
		.icon(() -> new ItemStack(TFItems.NAGA_SCALE.get()))
		.displayItems((parameters, output) -> acceptTwilightItems(output::accept, item -> !(item instanceof BlockItem)))
		.build());

	private TFCreativeTabs() {
	}

	public static void bootstrap() {
	}

	private static void acceptTwilightItems(java.util.function.Consumer<ItemLike> output, Predicate<Item> filter) {
		BuiltInRegistries.ITEM.entrySet().stream()
			.filter(entry -> entry.getKey().location().getNamespace().equals(TwilightForestMod.ID))
			.sorted(Comparator.comparing(entry -> entry.getKey().location().toString()))
			.map(Map.Entry<ResourceKey<Item>, Item>::getValue)
			.filter(filter)
			.filter(TFCreativeTabs::isVisible)
			.forEach(output);
	}

	private static boolean isVisible(Item item) {
		return !TFConfig.disableEntireTable || !(item instanceof BlockItem blockItem) || blockItem.getBlock() != TFBlocks.UNCRAFTING_TABLE.get();
	}
}
