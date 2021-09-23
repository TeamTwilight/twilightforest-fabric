package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HasAdvancementTrigger implements CriterionTrigger<HasAdvancementTrigger.Instance> {

	private static final ResourceLocation ID = TFConstants.prefix("has_advancement");
	private final Map<PlayerAdvancements, HasAdvancementTrigger.Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
		HasAdvancementTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
		HasAdvancementTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
		ResourceLocation advancementId = new ResourceLocation(GsonHelper.getAsString(json, "advancement"));
		return new HasAdvancementTrigger.Instance(player, advancementId);
	}

	public void trigger(ServerPlayer player, Advancement advancement) {
		Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(advancement);
		}
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final ResourceLocation advancementLocation;

		public Instance(EntityPredicate.Composite player, ResourceLocation advancementLocation) {
			super(HasAdvancementTrigger.ID, player);
			this.advancementLocation = advancementLocation;
		}

		boolean test(Advancement advancement) {
			return advancementLocation.equals(advancement.getId());
		}

		@Override
		public JsonObject serializeToJson(SerializationContext serializer) {
			JsonObject json = super.serializeToJson(serializer);
			json.addProperty("advancement", advancementLocation.toString());
			return json;
		}
	}

	private static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<HasAdvancementTrigger.Instance>> listeners = Sets.newHashSet();

		Listeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(CriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(CriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(Advancement advancement) {
			List<Listener<HasAdvancementTrigger.Instance>> list = new ArrayList<>();

			for (CriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : this.listeners) {
				if (listener.getTriggerInstance().test(advancement)) {
					list.add(listener);
				}
			}

			for (CriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : list) {
				listener.run(this.playerAdvancements);
			}
		}
	}
}
