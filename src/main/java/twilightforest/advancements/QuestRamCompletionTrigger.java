package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;

public class QuestRamCompletionTrigger extends SimpleCriterionTrigger<QuestRamCompletionTrigger.Instance> {

    public static final ResourceLocation ID = TwilightForestMod.prefix("complete_quest_ram");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		return new QuestRamCompletionTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(QuestRamCompletionTrigger.ID, player);
        }

        public static Instance completeRam() {
            return new Instance(ContextAwarePredicate.ANY);
        }
    }
}
