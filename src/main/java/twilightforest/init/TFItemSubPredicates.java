package twilightforest.init;

import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.predicate.ItemColorPredicate;

/**
 * Fabric port of upstream {@code TFItemSubPredicates} — registers
 * {@code twilightforest:color} so advancement JSONs that test
 * dyed-armor color (e.g. {@code arctic_armor_dyed}) parse without
 * «Unknown registry key in ITEM_SUB_PREDICATE_TYPE».
 */
public final class TFItemSubPredicates {

    public static final ItemSubPredicate.Type<ItemColorPredicate> COLOR = Registry.register(
            BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE,
            TwilightForestMod.prefix("color"),
            new ItemSubPredicate.Type<>(ItemColorPredicate.CODEC)
    );

    private TFItemSubPredicates() {
    }

    public static void bootstrap() {
        COLOR.toString();
    }
}
