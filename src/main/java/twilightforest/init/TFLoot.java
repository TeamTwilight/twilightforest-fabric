package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.TwilightForestMod;
import twilightforest.loot.conditions.IsMinionCondition;
import twilightforest.loot.conditions.ModExistsCondition;
import twilightforest.loot.conditions.UncraftingTableEnabledCondition;
import twilightforest.loot.conditions.GiantPickUsedCondition;
import twilightforest.loot.functions.ModItemSwap;

public class TFLoot {

	public static final LazyRegistrar<LootItemConditionType> CONDITIONS = LazyRegistrar.create(Registries.LOOT_CONDITION_TYPE, TwilightForestMod.ID);
	public static final LazyRegistrar<LootItemFunctionType> FUNCTIONS = LazyRegistrar.create(Registries.LOOT_FUNCTION_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<LootItemFunctionType> ITEM_OR_DEFAULT = FUNCTIONS.register("item_or_default", () -> new LootItemFunctionType(new ModItemSwap.Serializer()));

	public static final RegistryObject<LootItemConditionType> IS_MINION = CONDITIONS.register("is_minion", () -> new LootItemConditionType(new IsMinionCondition.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> MOD_EXISTS = CONDITIONS.register("mod_exists", () -> new LootItemConditionType(new ModExistsCondition.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> UNCRAFTING_TABLE_ENABLED = CONDITIONS.register("uncrafting_table_enabled", () -> new LootItemConditionType(new UncraftingTableEnabledCondition.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> GIANT_PICK_USED_CONDITION = CONDITIONS.register("giant_pick_used", () -> new LootItemConditionType(new GiantPickUsedCondition.ConditionSerializer()));

}
