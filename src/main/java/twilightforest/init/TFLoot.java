package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.TwilightForestMod;
import twilightforest.loot.conditions.IsMinion;
import twilightforest.loot.conditions.ModExists;
import twilightforest.loot.functions.Enchant;
import twilightforest.loot.functions.ModItemSwap;

public class TFLoot {

	public static final LazyRegistrar<LootItemConditionType> CONDITIONS = LazyRegistrar.create(Registry.LOOT_CONDITION_TYPE, TwilightForestMod.ID);
	public static final LazyRegistrar<LootItemFunctionType> FUNCTIONS = LazyRegistrar.create(Registry.LOOT_FUNCTION_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<LootItemFunctionType> ENCHANT = FUNCTIONS.register("enchant", () -> new LootItemFunctionType(new Enchant.Serializer()));
	public static final RegistryObject<LootItemFunctionType> ITEM_OR_DEFAULT = FUNCTIONS.register("item_or_default", () -> new LootItemFunctionType(new ModItemSwap.Serializer()));

	public static final RegistryObject<LootItemConditionType> IS_MINION = CONDITIONS.register("is_minion", () -> new LootItemConditionType(new IsMinion.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> MOD_EXISTS = CONDITIONS.register("mod_exists", () -> new LootItemConditionType(new ModExists.ConditionSerializer()));

}
