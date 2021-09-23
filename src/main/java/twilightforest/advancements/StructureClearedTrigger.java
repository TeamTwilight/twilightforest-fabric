package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
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

public class StructureClearedTrigger implements CriterionTrigger<StructureClearedTrigger.Instance> {

	public static final ResourceLocation ID = TFConstants.prefix("structure_cleared");
	private final Map<PlayerAdvancements, StructureClearedTrigger.Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<StructureClearedTrigger.Instance> listener) {
		StructureClearedTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, Listener<StructureClearedTrigger.Instance> listener) {
		StructureClearedTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
		String structureName = GsonHelper.getAsString(json, "structure");
		return new StructureClearedTrigger.Instance(player, structureName);
	}

	public void trigger(ServerPlayer player, String structureName) {
		StructureClearedTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(structureName);
		}
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final String structureName;

		public Instance(EntityPredicate.Composite player, String structureName) {
			super(StructureClearedTrigger.ID, player);
			this.structureName = structureName;
		}

		boolean test(String structureName) {
			return this.structureName.equals(structureName);
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<StructureClearedTrigger.Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<StructureClearedTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<StructureClearedTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(String structureName) {

			List<Listener<StructureClearedTrigger.Instance>> list = new ArrayList<>();

			for (CriterionTrigger.Listener<StructureClearedTrigger.Instance> listener : this.listeners) {
				if (listener.getTriggerInstance().test(structureName)) {
					list.add(listener);
				}
			}

			for (CriterionTrigger.Listener<StructureClearedTrigger.Instance> listener : list) {
				listener.run(this.playerAdvancements);
			}
		}
	}
}
