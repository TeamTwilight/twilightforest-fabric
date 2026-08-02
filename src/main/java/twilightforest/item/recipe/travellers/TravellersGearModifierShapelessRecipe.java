package twilightforest.item.recipe.travellers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.init.TFRecipes;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TravellersGearModifierShapelessRecipe extends TravellersGearModifierRecipe {
	protected final NonNullList<Ingredient> ingredients;

	public TravellersGearModifierShapelessRecipe(NonNullList<Ingredient> ingredients, ResourceKey<TravellersModifier> travellersModifier) {
		super(travellersModifier);
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		if (!super.matches(input, level))
			return false;
		if (input.ingredientCount() != this.ingredients.size())
			return false;
		List<ItemStack> nonEmptyItems = new ArrayList<>(input.ingredientCount());
		for (ItemStack item : input.items()) {
			if (!item.isEmpty())
				nonEmptyItems.add(item);
		}
		return findMatches(nonEmptyItems, this.ingredients);
	}

	/**
	 * Simple shapeless recipe matching - checks if each ingredient can be matched
	 * to a unique input item stack. Replaces NeoForge's RecipeMatcher.findMatches.
	 */
	private static boolean findMatches(List<ItemStack> inputs, NonNullList<Ingredient> ingredients) {
		if (inputs.size() != ingredients.size())
			return false;
		boolean[] used = new boolean[inputs.size()];
		for (Ingredient ingredient : ingredients) {
			boolean found = false;
			for (int i = 0; i < inputs.size(); i++) {
				if (!used[i] && ingredient.test(inputs.get(i))) {
					used[i] = true;
					found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return ingredients.size() <= width * height;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public int getWidth() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public int getHeight() {
		return ingredients.size() > 4 ? 3 : 2;
	}

	@Override
	public boolean isShapeless() {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.MODIFIER_SHAPELESS_RECIPE_SERIALIZER.get();
	}

	public static class Serializer extends AbstractModifierRecipeSerializer<TravellersGearModifierShapelessRecipe> {
		public Serializer() {
			super(RecordCodecBuilder.mapCodec(instance -> instance.group(
				Ingredient.CODEC.listOf().xmap(list -> { NonNullList<Ingredient> result = NonNullList.create(); result.addAll(list); return result; }, Function.identity())
					.fieldOf("ingredients")
					.forGetter(recipe -> recipe.ingredients),
				ResourceKey.codec(TFRegistries.Keys.TRAVELLERS_MODIFIERS)
					.fieldOf("modifier_key")
					.forGetter(TravellersGearModifierRecipe::getTravellersModifierKey)
			).apply(instance, (ingredients, key) -> new TravellersGearModifierShapelessRecipe(ingredients, key))));
		}
	}
}
