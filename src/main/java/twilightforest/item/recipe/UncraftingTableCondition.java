package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.RegistryOps;
import org.jspecify.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFResourceConditionTypes;

public record UncraftingTableCondition() implements ResourceCondition {
	public static final MapCodec<UncraftingTableCondition> CODEC = MapCodec.unit(new UncraftingTableCondition());

	@Override
	public ResourceConditionType<?> getType() {
		return TFResourceConditionTypes.UNCRAFTING_RECIPE_CONDITION;
	}

	@Override
	public boolean test(RegistryOps.@Nullable RegistryInfoLookup registryInfo) {
		return !TFConfig.disableEntireTable;
	}
}