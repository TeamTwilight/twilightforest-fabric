package twilightforest.loot.functions;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.init.TFLoot;

import java.util.HashMap;
import java.util.Map;

//TODO this may no longer be needed, take a look at SetEnchantmentsFunction
// Similar to EnchantRandomly but applies everything and with exact levels
public class Enchant extends LootItemConditionalFunction {

	private final Map<Enchantment, Short> enchantments;

	protected Enchant(LootItemCondition[] conditions, Map<Enchantment, Short> enchantments) {
		super(conditions);
		this.enchantments = enchantments;
	}

	@Override
	public LootItemFunctionType getType() {
		return TFLoot.ENCHANT.get();
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		for (Map.Entry<Enchantment, Short> e : enchantments.entrySet()) {
			if (stack.getItem() == Items.ENCHANTED_BOOK) {
				EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(e.getKey(), e.getValue()));
			} else {
				stack.enchant(e.getKey(), e.getValue());
			}
		}
		return stack;
	}

	public static Enchant.Builder builder() {
		return new Enchant.Builder();
	}

	public static class Builder extends LootItemConditionalFunction.Builder<Enchant.Builder> {
		private final Map<Enchantment, Short> enchants = Maps.newHashMap();

		protected Enchant.Builder getThis() {
			return this;
		}

		public Enchant.Builder apply(Enchantment p_216077_1_, Integer p_216077_2_) {
			this.enchants.put(p_216077_1_, p_216077_2_.shortValue());
			return this;
		}

		public LootItemFunction build() {
			return new Enchant(this.getConditions(), this.enchants);
		}
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<Enchant> {

		@Override
		public void serialize(JsonObject object, Enchant function, JsonSerializationContext ctx) {
			if (!function.enchantments.isEmpty()) {
				JsonObject obj = new JsonObject();

				for (Map.Entry<Enchantment, Short> e : function.enchantments.entrySet()) {
					obj.addProperty(BuiltInRegistries.ENCHANTMENT.getKey(e.getKey()).toString(), e.getValue());
				}

				object.add("enchantments", obj);
			}
		}

		@Override
		public Enchant deserialize(JsonObject object, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
			Map<Enchantment, Short> enchantments = new HashMap<>();

			if (object.has("enchantments")) {
				JsonObject enchantObj = GsonHelper.getAsJsonObject(object, "enchantments");

				for (Map.Entry<String, JsonElement> e : enchantObj.entrySet()) {
					ResourceLocation id = new ResourceLocation(e.getKey());
					if (!BuiltInRegistries.ENCHANTMENT.containsKey(id)) {
						throw new JsonSyntaxException("Can't find enchantment " + e.getKey());
					}

					Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(id);
					short lvl = e.getValue().getAsShort();

					for (Enchantment other : enchantments.keySet()) {
						if (!ench.isCompatibleWith(other)) {
							throw new JsonParseException(String.format("Enchantments %s and %s conflict", BuiltInRegistries.ENCHANTMENT.getKey(ench), BuiltInRegistries.ENCHANTMENT.getKey(other)));
						}
					}

					enchantments.put(ench, lvl);
				}
			}

			return new Enchant(conditions, enchantments);
		}
	}
}
