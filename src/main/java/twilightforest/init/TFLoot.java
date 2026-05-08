package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import twilightforest.TwilightForestMod;
import twilightforest.loot.LootingEnchantNumberProvider;
import twilightforest.loot.MultiplayerBasedAdditionLootFunction;
import twilightforest.loot.MultiplayerBasedNumberProvider;
import twilightforest.loot.conditions.GiantPickUsedCondition;
import twilightforest.loot.conditions.IsMinionCondition;
import twilightforest.loot.conditions.ModExistsCondition;
import twilightforest.loot.conditions.NeoForgeCanItemPerformAbilityCondition;
import twilightforest.loot.conditions.UncraftingTableEnabledCondition;

/**
 * Fabric port — registers TF custom loot {@code condition} / {@code function} /
 * {@code number_provider} types so loot-table JSON parses cleanly. Also
 * registers a Fabric-side equivalent of {@code neoforge:can_item_perform_ability}
 * so the NeoForge loot-condition reference common in upstream TF tables
 * (e.g. shears_dig on plant blocks) doesn't reject the whole table.
 */
public final class TFLoot {

    public static final TFRegistryObject<LootItemConditionType> IS_MINION = registerCondition("is_minion",
            new LootItemConditionType(IsMinionCondition.CODEC));
    public static final TFRegistryObject<LootItemConditionType> MOD_EXISTS = registerCondition("mod_exists",
            new LootItemConditionType(ModExistsCondition.CODEC));
    public static final TFRegistryObject<LootItemConditionType> UNCRAFTING_TABLE_ENABLED = registerCondition("uncrafting_table_enabled",
            new LootItemConditionType(UncraftingTableEnabledCondition.CODEC));
    public static final TFRegistryObject<LootItemConditionType> GIANT_PICK_USED_CONDITION = registerCondition("giant_pick_used",
            new LootItemConditionType(GiantPickUsedCondition.CODEC));

    public static final TFRegistryObject<LootItemFunctionType<MultiplayerBasedAdditionLootFunction>> MULTIPLAYER_MULTIPLIER = registerFunction("multiplayer_addition",
            new LootItemFunctionType<>(MultiplayerBasedAdditionLootFunction.CODEC));

    public static final TFRegistryObject<LootNumberProviderType> MULTIPLAYER_ROLLS = registerNumber("multiplayer_rolls",
            new LootNumberProviderType(MultiplayerBasedNumberProvider.CODEC));
    public static final TFRegistryObject<LootNumberProviderType> LOOTING_ROLLS = registerNumber("looting_rolls",
            new LootNumberProviderType(LootingEnchantNumberProvider.CODEC));

    /** Fabric-side replacement registered under the literal {@code neoforge:can_item_perform_ability} id. */
    public static final TFRegistryObject<LootItemConditionType> NEOFORGE_CAN_ITEM_PERFORM_ABILITY = registerNeoforgeCondition("can_item_perform_ability",
            new LootItemConditionType(NeoForgeCanItemPerformAbilityCondition.CODEC));

    private TFLoot() {
    }

    public static void bootstrap() {
        IS_MINION.get();
        MOD_EXISTS.get();
        UNCRAFTING_TABLE_ENABLED.get();
        GIANT_PICK_USED_CONDITION.get();
        MULTIPLAYER_MULTIPLIER.get();
        MULTIPLAYER_ROLLS.get();
        LOOTING_ROLLS.get();
        NEOFORGE_CAN_ITEM_PERFORM_ABILITY.get();
    }

    private static TFRegistryObject<LootItemConditionType> registerCondition(String path, LootItemConditionType type) {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix(path), type);
        return new TFRegistryObject<>(type);
    }

    private static <T extends net.minecraft.world.level.storage.loot.functions.LootItemFunction>
            TFRegistryObject<LootItemFunctionType<T>> registerFunction(String path, LootItemFunctionType<T> type) {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, TwilightForestMod.prefix(path), type);
        return new TFRegistryObject<>(type);
    }

    private static TFRegistryObject<LootNumberProviderType> registerNumber(String path, LootNumberProviderType type) {
        Registry.register(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, TwilightForestMod.prefix(path), type);
        return new TFRegistryObject<>(type);
    }

    private static TFRegistryObject<LootItemConditionType> registerNeoforgeCondition(String path, LootItemConditionType type) {
        ResourceLocation id;
        try {
            id = ResourceLocation.fromNamespaceAndPath("neoforge", path);
        } catch (ResourceLocationException e) {
            throw new IllegalStateException("invalid neoforge loot condition id: " + path, e);
        }
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, id, type);
        return new TFRegistryObject<>(type);
    }
}
