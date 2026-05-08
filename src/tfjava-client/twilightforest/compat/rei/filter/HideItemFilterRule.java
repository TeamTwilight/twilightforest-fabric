package twilightforest.compat.rei.filter;

import com.google.common.collect.Streams;
import me.shedaniel.rei.api.client.entry.filtering.*;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFItems;

import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class HideItemFilterRule implements FilteringRule<Object> {
	public static HideItemFilterRule INSTANCE = new HideItemFilterRule();

	@Override
	public FilteringRuleType<? extends FilteringRule<Object>> getType() {
		return HideItemFilterType.INSTANCE;
	}

	@Override
	public FilteringResult processFilteredStacks(FilteringContext context, FilteringResultFactory resultFactory, Object o, boolean async) {
		FilteringResult filteringResult = resultFactory.create();

		Stream<EntryStack<?>> all = Streams.concat(context.getShownStacks().stream(), context.getUnsetStacks().stream());

		filteringResult.hide(all.filter(stack -> BuiltInRegistries.ITEM.getKey(TFItems.MAGIC_PAINTING.get()).equals(stack.getIdentifier())
			|| BuiltInRegistries.ITEM.getKey(TFItems.FOUR_LEAF_CLOVER.get()).equals(stack.getIdentifier()) || BuiltInRegistries.ITEM.getKey(TFItems.STALE_BREAD.get()).equals(stack.getIdentifier())
			|| stack.getTagsFor().anyMatch(tagKey -> tagKey.equals(ItemTagGenerator.WIP))
		).toList());

		return filteringResult;
	}
}
