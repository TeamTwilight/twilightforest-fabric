package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArmorInventoryChangedTrigger implements CriterionTrigger<ArmorInventoryChangedTrigger.Instance> {

	public static final ResourceLocation ID = TFConstants.prefix("armor_changed");
	private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addPlayerListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
		Listeners listeners = this.listeners.computeIfAbsent(playerAdvancements, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removePlayerListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
		Listeners listeners = this.listeners.get(playerAdvancements);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				this.listeners.remove(playerAdvancements);
			}
		}
	}

	@Override
	public void removePlayerListeners(PlayerAdvancements playerAdvancements) {
		this.listeners.remove(playerAdvancements);
	}

	@Override
	public Instance createInstance(JsonObject json, DeserializationContext condition) {
		EntityPredicate.Composite player = EntityPredicate.Composite.fromJson(json, "player", condition);
		ItemPredicate from = ItemPredicate.fromJson(json.get("from"));
		ItemPredicate to = ItemPredicate.fromJson(json.get("to"));
		return new Instance(player, from, to);
	}

	public void trigger(ServerPlayer player, ItemStack from, ItemStack to) {
		Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(from, to);
		}
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final ItemPredicate from;
		private final ItemPredicate to;

		public Instance(EntityPredicate.Composite player, ItemPredicate from, ItemPredicate to) {
			super(ArmorInventoryChangedTrigger.ID, player);
			this.from = from;
			this.to = to;
		}

		public boolean test(ItemStack from, ItemStack to) {
			return this.from.matches(from) && this.to.matches(to);
		}

		@Override
		public ResourceLocation getCriterion() {
			return ArmorInventoryChangedTrigger.ID;
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(ItemStack from, ItemStack to) {

			List<Listener<Instance>> list = new ArrayList<>();

			for (Listener<Instance> listener : this.listeners) {
				if (listener.getTriggerInstance().test(from, to)) {
					list.add(listener);
				}
			}

			for (Listener<Instance> listener : list) {
				listener.run(this.playerAdvancements);
			}
		}
	}
}
