package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;

public class TrophyPedestalTrigger extends SimpleCriterionTrigger<TrophyPedestalTrigger.Instance> {

    public static final ResourceLocation ID = TwilightForestMod.prefix("placed_on_trophy_pedestal");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

	@Override
	public TrophyPedestalTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		return new TrophyPedestalTrigger.Instance(player);
	}

	public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ContextAwarePredicate player) {
            super(TrophyPedestalTrigger.ID, player);
        }

        public static Instance activatePedestal() {
            return new Instance(ContextAwarePredicate.ANY);
        }
    }
}
