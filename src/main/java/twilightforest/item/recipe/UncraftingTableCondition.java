package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

public class UncraftingTableCondition implements ICondition {

	private static final ResourceLocation ID = TwilightForestMod.prefix("uncrafting_table_enabled");
	public static final UncraftingTableCondition INSTANCE = new UncraftingTableCondition();

	private UncraftingTableCondition() {

	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		return !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get();
	}

	public static class Serializer implements IConditionSerializer<UncraftingTableCondition> {

		public static final Serializer INSTANCE = new Serializer();

		public Serializer() {
		}

		@Override
		public ResourceLocation getID() {
			return ID;
		}

		@Override
		public UncraftingTableCondition read(JsonObject json) {
			return new UncraftingTableCondition();
		}

		@Override
		public void write(JsonObject json, UncraftingTableCondition value) {
		}
	}
}
