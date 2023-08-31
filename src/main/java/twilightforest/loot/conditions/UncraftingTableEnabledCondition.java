package twilightforest.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.TFConfig;
import twilightforest.init.TFLoot;

public class UncraftingTableEnabledCondition implements LootItemCondition {

	private UncraftingTableEnabledCondition() {

	}

	@Override
	public LootItemConditionType getType() {
		return TFLoot.UNCRAFTING_TABLE_ENABLED.get();
	}

	@Override
	public boolean test(LootContext context) {
		return !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get();
	}

	public static LootItemCondition.Builder uncraftingTableEnabled() {
		return UncraftingTableEnabledCondition::new;
	}

	public static class ConditionSerializer implements Serializer<UncraftingTableEnabledCondition> {
		@Override
		public void serialize(JsonObject json, UncraftingTableEnabledCondition value, JsonSerializationContext context) {

		}

		@Override
		public UncraftingTableEnabledCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			return new UncraftingTableEnabledCondition();
		}
	}
}
