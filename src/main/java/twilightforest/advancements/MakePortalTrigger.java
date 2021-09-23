package twilightforest.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;

//im pretty sure we dont have to handle this via advancement triggers
public class MakePortalTrigger implements CriterionTrigger<MakePortalTrigger.Instance> {

    public static final ResourceLocation ID = TFConstants.prefix("make_tf_portal");
    private final Map<PlayerAdvancements, MakePortalTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<MakePortalTrigger.Instance> listener) {
        MakePortalTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<MakePortalTrigger.Instance> listener) {
        MakePortalTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
		return new MakePortalTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        MakePortalTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(EntityPredicate.Composite player) {
            super(MakePortalTrigger.ID, player);
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<CriterionTrigger.Listener<MakePortalTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(CriterionTrigger.Listener<MakePortalTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(CriterionTrigger.Listener<MakePortalTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            for (CriterionTrigger.Listener<MakePortalTrigger.Instance> listener : Lists.newArrayList(this.listeners)) {
                listener.run(this.playerAdvancements);
            }
        }
    }
}
