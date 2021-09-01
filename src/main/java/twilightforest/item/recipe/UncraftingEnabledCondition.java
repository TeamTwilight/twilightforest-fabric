package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

public class UncraftingEnabledCondition implements ICondition {

	@Override
	public ResourceLocation getID() {
		return TwilightForestMod.prefix("uncrafting_enabled");
	}

	@Override
	public boolean test() {
		return !TFConfig.COMMON_CONFIG.disableUncrafting; //(because true is off and false is on)
	}

	public static class Serializer implements IConditionSerializer<UncraftingEnabledCondition> {
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void write(JsonObject json, UncraftingEnabledCondition value) { }

		@Override
		public UncraftingEnabledCondition read(JsonObject json) {
			return new UncraftingEnabledCondition();
		}

		@Override
		public ResourceLocation getID() {
			return TwilightForestMod.prefix("uncrafting_enabled");
		}
	}
}
