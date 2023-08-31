package twilightforest.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.init.TFLoot;

import javax.annotation.Nonnull;

public record IsMinionCondition(boolean inverse) implements LootItemCondition {

	@Override
	public LootItemConditionType getType() {
		return TFLoot.IS_MINION.get();
	}

	@Override
	public boolean test(@Nonnull LootContext context) {
		return context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof CarminiteGhastling ghastling && ghastling.isMinion() == !inverse;
	}

	public static Builder builder(boolean inverse) {
		return () -> new IsMinionCondition(inverse);
	}

	public static class ConditionSerializer implements Serializer<IsMinionCondition> {
		@Override
		public void serialize(JsonObject json, IsMinionCondition value, JsonSerializationContext context) {
			json.addProperty("inverse", value.inverse);
		}

		@Override
		public IsMinionCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			return new IsMinionCondition(GsonHelper.getAsBoolean(json, "inverse", false));
		}
	}
}
