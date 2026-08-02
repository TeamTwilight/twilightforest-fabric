package twilightforest.init;

import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.Registries;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.predicate.ItemColorPredicate;

public class TFItemSubPredicates {

	public static final DeferredRegister<ItemSubPredicate.Type<?>> TYPES = DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<ItemColorPredicate>> COLOR = TYPES.register("color", () -> new ItemSubPredicate.Type<>(ItemColorPredicate.CODEC));
}
