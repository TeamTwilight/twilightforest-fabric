package twilightforest.data.custom;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;

import java.util.*;
import java.util.function.Consumer;

public class UncraftingRecipeBuilder implements RecipeBuilder {

	private final Ingredient input;
	private final int count;
	private int cost = -1;
	private final List<String> pattern = new ArrayList<>();
	private final Map<Character, Ingredient> outputs = new HashMap<>();

	public UncraftingRecipeBuilder(Ingredient input, int count) {
		this.input = input;
		this.count = count;
	}

	public static UncraftingRecipeBuilder uncrafting(ItemLike input) {
		return uncrafting(Ingredient.of(input), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(TagKey<Item> input) {
		return uncrafting(Ingredient.of(input), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(ItemLike input, int count) {
		return uncrafting(Ingredient.of(input), count);
	}

	public static UncraftingRecipeBuilder uncrafting(TagKey<Item> input, int count) {
		return uncrafting(Ingredient.of(input), count);
	}

	public static UncraftingRecipeBuilder uncrafting(Ingredient input, int count) {
		return new UncraftingRecipeBuilder(input, count);
	}

	public UncraftingRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
		return this.define(pSymbol, Ingredient.of(pTag));
	}

	public UncraftingRecipeBuilder define(Character pSymbol, ItemLike pItem) {
		return this.define(pSymbol, Ingredient.of(pItem));
	}

	public UncraftingRecipeBuilder setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public UncraftingRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
		if (this.outputs.containsKey(pSymbol)) {
			throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
		} else if (pSymbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.outputs.put(pSymbol, pIngredient);
			return this;
		}
	}

	public UncraftingRecipeBuilder pattern(String pattern) {
		if (!this.pattern.isEmpty() && pattern.length() != this.pattern.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.pattern.add(pattern);
			return this;
		}
	}

	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
		return this;
	}


	@Override
	public RecipeBuilder group(@Nullable String group) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.input.getItems()[0].getItem();
	}

	@Override
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		this.ensureValid(id);
		consumer.accept(new Result(id, this.input, this.count, this.cost, this.pattern, this.outputs));
	}

	@Override
	public void save(Consumer<FinishedRecipe> consumer) {
		this.save(consumer, new ResourceLocation(TwilightForestMod.ID, "uncrafting/" + this.getDefaultRecipeId(this.getResult()).getPath()));
	}

	private void ensureValid(ResourceLocation id) {
		if (this.pattern.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
		} else {
			Set<Character> set = Sets.newHashSet(this.outputs.keySet());
			set.remove(' ');

			for(String s : this.pattern) {
				for(int i = 0; i < s.length(); ++i) {
					char c0 = s.charAt(i);
					if (!this.outputs.containsKey(c0) && c0 != ' ') {
						throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
					}

					set.remove(c0);
				}
			}

			if (!set.isEmpty()) {
				throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
			}
		}
	}

	private ResourceLocation getDefaultRecipeId(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}

	public static class Result implements FinishedRecipe {

		private final ResourceLocation id;
		private final Ingredient input;
		private final int count;
		private final int cost;
		private final List<String> pattern;
		private final Map<Character, Ingredient> outputs;

		public Result(ResourceLocation id, Ingredient input, int count, int cost, List<String> pattern, Map<Character, Ingredient> outputs) {
			this.id = id;
			this.input = input;
			this.count = count;
			this.cost = cost;
			this.pattern = pattern;
			this.outputs = outputs;
		}

		@Override
		public void serializeRecipeData(JsonObject object) {
			JsonArray jsonarray = new JsonArray();

			for(String s : this.pattern) {
				jsonarray.add(s);
			}

			object.add("pattern", jsonarray);
			JsonObject keys = new JsonObject();

			for(Map.Entry<Character, Ingredient> entry : this.outputs.entrySet()) {
				keys.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
			}

			object.add("key", keys);

			JsonObject inputJson = new JsonObject();
			inputJson.add("ingredient", this.input.toJson());
			if (this.count > 1) {
				inputJson.addProperty("count", this.count);
			}

			object.add("input", inputJson);

			if (this.cost > -1) {
				object.addProperty("cost", this.cost);
			}
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return TFRecipes.UNCRAFTING_SERIALIZER.get();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
