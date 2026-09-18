package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import twilightforest.TFCommon;
import twilightforest.item.recipe.UncraftingTableCondition;

public final class TFResourceConditionTypes {
	public static final ResourceConditionType<UncraftingTableCondition> UNCRAFTING_RECIPE_CONDITION = create("uncrafting_table_enabled", UncraftingTableCondition.CODEC);

	private static <T extends ResourceCondition> ResourceConditionType<T> create(String name, MapCodec<T> codec) {
		ResourceConditionType<T> type = ResourceConditionType.create(TFCommon.prefix(name), codec);
		ResourceConditions.register(type);
		return type;
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing resource condition types...");
	}
}