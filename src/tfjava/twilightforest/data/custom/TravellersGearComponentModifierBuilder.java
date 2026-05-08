package twilightforest.data.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearComponentModifierBuilder {
	private final List<TravellersGearModifierRecipe> recipes = new ArrayList<>();

	private TravellersGearComponentModifierBuilder(Iterable<ShapedRecipePattern> pattern, ResourceKey<TravellersModifier> travellersModifierKey, boolean isRotated) {
		pattern.forEach(shapedRecipePattern -> this.recipes.add(new TravellersGearModifierShapedRecipe(shapedRecipePattern, travellersModifierKey, isRotated)));
	}

	private TravellersGearComponentModifierBuilder(Iterable<NonNullList<Ingredient>> ingredients, ResourceKey<TravellersModifier> travellersModifierKey) {
		ingredients.forEach(recipeIngredients -> this.recipes.add(new TravellersGearModifierShapelessRecipe(recipeIngredients, travellersModifierKey)));
	}

	public static TravellersGearComponentModifierBuilder buildShaped(Iterable<ShapedRecipePattern> pattern, ResourceKey<TravellersModifier> travellersModifier) {
		return buildShaped(pattern, travellersModifier, false);
	}

	public static TravellersGearComponentModifierBuilder buildShaped(Iterable<ShapedRecipePattern> pattern, ResourceKey<TravellersModifier> travellersModifier, boolean isRotated) {
		return new TravellersGearComponentModifierBuilder(pattern, travellersModifier, isRotated);
	}

	public static TravellersGearComponentModifierBuilder buildShapeless(Iterable<NonNullList<Ingredient>> ingredients, ResourceKey<TravellersModifier> travellersModifier) {
		return new TravellersGearComponentModifierBuilder(ingredients, travellersModifier);
	}

	public void save(RecipeOutput output) {
		for (TravellersGearModifierRecipe recipe : this.recipes) {
			output.accept(recipe.getId(), recipe, null);
		}
	}
}
