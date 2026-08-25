package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TFMain;
import twilightforest.advancements.predicate.ItemColorPredicate;

public class TFItemSubPredicates {
	public static final DataComponentPredicate.Type<ItemColorPredicate> COLOR = Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, TFMain.prefix("color"), ItemColorPredicate.TYPE);

	public static void init() {
		TFMain.LOGGER.info("Initializing item sub predicates...");
	}
}