package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.loot.conditions.IsMinion;
import twilightforest.loot.conditions.ModExists;
import twilightforest.loot.functions.Enchant;
import twilightforest.loot.functions.ModItemSwap;

public class TFLoot {

	public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, TwilightForestMod.ID);
	public static final DeferredRegister<LootItemFunctionType> FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<LootItemFunctionType> ENCHANT = FUNCTIONS.register("enchant", () -> new LootItemFunctionType(new Enchant.Serializer()));
	public static final RegistryObject<LootItemFunctionType> ITEM_OR_DEFAULT = FUNCTIONS.register("item_or_default", () -> new LootItemFunctionType(new ModItemSwap.Serializer()));

	public static final RegistryObject<LootItemConditionType> IS_MINION = CONDITIONS.register("is_minion", () -> new LootItemConditionType(new IsMinion.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> MOD_EXISTS = CONDITIONS.register("mod_exists", () -> new LootItemConditionType(new ModExists.ConditionSerializer()));

}
