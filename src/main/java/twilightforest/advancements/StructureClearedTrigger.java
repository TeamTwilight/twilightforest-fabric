package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import twilightforest.TwilightForestMod;

public class StructureClearedTrigger extends SimpleCriterionTrigger<StructureClearedTrigger.Instance> {

	public static final ResourceLocation ID = TwilightForestMod.prefix("structure_cleared");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		String structureName = GsonHelper.getAsString(json, "structure");
		return new StructureClearedTrigger.Instance(player, structureName);
	}

	public void trigger(ServerPlayer player, String structureName) {
		this.trigger(player, (instance) -> instance.test(structureName));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final String structureName;

		public Instance(ContextAwarePredicate player, String structureName) {
			super(StructureClearedTrigger.ID, player);
			this.structureName = structureName;
		}

		public static Instance clearedStructure(String name) {
			return new Instance(ContextAwarePredicate.ANY, name);
		}

		boolean test(String structureName) {
			return this.structureName.equals(structureName);
		}
	}
}
