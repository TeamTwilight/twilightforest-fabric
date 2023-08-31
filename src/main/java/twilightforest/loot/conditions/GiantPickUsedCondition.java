package twilightforest.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.giant_pick.GiantPickMineCapability;
import twilightforest.init.TFLoot;

import java.util.Set;

public class GiantPickUsedCondition implements LootItemCondition {
    final LootContext.EntityTarget entityTarget;

    private GiantPickUsedCondition(LootContext.EntityTarget pEntityTarget) {
        this.entityTarget = pEntityTarget;
    }

    @Override
    public LootItemConditionType getType() {
        return TFLoot.GIANT_PICK_USED_CONDITION.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.entityTarget.getParam());
    }

    @Override
    public boolean test(LootContext context) {
        if (context.getParamOrNull(this.entityTarget.getParam()) instanceof Player player) {
            GiantPickMineCapability capability = player.getCapability(CapabilityList.GIANT_PICK_MINE).resolve().orElse(null);
            if (capability != null) {
                return player.level().getGameTime() == capability.getMining() && capability.canMakeGiantBlock();
            }
        }
        return false;
    }

    public static LootItemCondition.Builder builder(LootContext.EntityTarget target) {
        return () -> new GiantPickUsedCondition(target);
    }

    public static class ConditionSerializer implements Serializer<GiantPickUsedCondition> {
        @Override
        public void serialize(JsonObject json, GiantPickUsedCondition condition, JsonSerializationContext context) {
            json.add("entity", context.serialize(condition.entityTarget));
        }

        @Override
        public GiantPickUsedCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new GiantPickUsedCondition(GsonHelper.getAsObject(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}