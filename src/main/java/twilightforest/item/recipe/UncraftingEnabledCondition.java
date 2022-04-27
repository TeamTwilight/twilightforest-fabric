package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

public class UncraftingEnabledCondition implements ConditionJsonProvider {

	@Override
	public ResourceLocation getConditionId() {
		return TwilightForestMod.prefix("uncrafting_enabled");
	}

	@Override
	public void writeParameters(JsonObject json) { }

	//implementing ICondition requires this to be implemented, remove this once its not required
	public void register() {
		ResourceConditions.register(getConditionId(), json -> !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncrafting.get());
	}
}
