package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.RegistryOps;
import org.jspecify.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.config.TFConfig;

public record UncraftingTableCondition() implements ResourceCondition {
	public static final MapCodec<UncraftingTableCondition> CODEC = MapCodec.unit(new UncraftingTableCondition());

	public static final ResourceConditionType<UncraftingTableCondition> TYPE =
		ResourceConditionType.create(TFMain.prefix("uncrafting_table_enabled"), CODEC);

	@Override
	public ResourceConditionType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean test(RegistryOps.@Nullable RegistryInfoLookup registryInfo) {
		return !TFConfig.disableEntireTable;
	}
}