package twilightforest.data.helpers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentShapedRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStack result;
	private final Map<Character, Ingredient> key = new LinkedHashMap<>();
	private final List<String> rows = new ArrayList<>();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group = "";
	private boolean showNotification = true;

	private ComponentShapedRecipeBuilder(RecipeCategory category, ItemStack result) {
		this.category = category;
		this.result = result;
	}

	public static ComponentShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
		return new ComponentShapedRecipeBuilder(category, result);
	}

	public ComponentShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(tag));
	}

	public ComponentShapedRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	public ComponentShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol)) {
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
		}
		if (symbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' is reserved and cannot be defined");
		}
		this.key.put(symbol, ingredient);
		return this;
	}

	public ComponentShapedRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.iterator().next().length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line");
		}
		this.rows.add(pattern);
		return this;
	}

	@Override
	public ComponentShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	@Override
	public ComponentShapedRecipeBuilder group(String group) {
		this.group = group;
		return this;
	}

	public ComponentShapedRecipeBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result.getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		if (this.rows.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for " + id);
		}
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}

		ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
		CraftingBookCategory bookCategory = RecipeBuilder.determineBookCategory(this.category);
		Advancement.Builder advancement = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement::addCriterion);
		AdvancementHolder advancementHolder = advancement.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/"));

		output.accept(id, new ShapedRecipe(this.group, bookCategory, pattern, this.result, this.showNotification), advancementHolder);
	}
}
