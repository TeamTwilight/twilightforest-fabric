package twilightforest.advancements;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;

public class UncraftItemTrigger extends SimpleCriterionTrigger<UncraftItemTrigger.TriggerInstance> {
	public static final ResourceLocation ID = TwilightForestMod.prefix("uncraft_item");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
		return new TriggerInstance(player, ItemPredicate.fromJson(json.get("item")));
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, (instance) -> instance.matches(stack));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private final ItemPredicate item;

		public TriggerInstance(ContextAwarePredicate player, ItemPredicate item) {
			super(ID, player);
			this.item = item;
		}

		public static TriggerInstance uncraftedItem() {
			return new TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.ANY);
		}

		public static TriggerInstance uncraftedItem(ItemPredicate pItem) {
			return new TriggerInstance(ContextAwarePredicate.ANY, pItem);
		}

		public static TriggerInstance uncraftedItem(ItemLike pItem) {
			return new TriggerInstance(ContextAwarePredicate.ANY, new ItemPredicate((TagKey<Item>)null, ImmutableSet.of(pItem.asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion)null, NbtPredicate.ANY));
		}

		public boolean matches(ItemStack item) {
			return this.item.matches(item);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext ctx) {
			JsonObject jsonobject = super.serializeToJson(ctx);
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}
}
