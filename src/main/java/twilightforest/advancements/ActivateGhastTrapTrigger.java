package twilightforest.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;

import java.util.Map;
import java.util.Set;

//can we move this to the block class itself rather than having this?
public class ActivateGhastTrapTrigger implements CriterionTrigger<ActivateGhastTrapTrigger.Instance> {

    public static final ResourceLocation ID = TFConstants.prefix("activate_ghast_trap");
    private final Map<PlayerAdvancements, ActivateGhastTrapTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<ActivateGhastTrapTrigger.Instance> listener) {
        ActivateGhastTrapTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<ActivateGhastTrapTrigger.Instance> listener) {
        ActivateGhastTrapTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance createInstance(JsonObject json, DeserializationContext condition) {
        EntityPredicate.Composite player = EntityPredicate.Composite.fromJson(json, "player", condition);
        return new ActivateGhastTrapTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        ActivateGhastTrapTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite player) {
            super(ActivateGhastTrapTrigger.ID, player);
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<ActivateGhastTrapTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<ActivateGhastTrapTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<ActivateGhastTrapTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            for (Listener<ActivateGhastTrapTrigger.Instance> listener : Lists.newArrayList(this.listeners)) {
                listener.run(this.playerAdvancements);
            }
        }
    }
}
