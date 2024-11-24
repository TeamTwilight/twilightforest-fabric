package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import twilightforest.compat.rei.TwilightForestREIClientPlugin;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;
import twilightforest.item.recipe.CrumbleRecipe;

import java.util.List;
import java.util.Optional;

public class REICrumbleHornDisplay extends BasicDisplay {

	public final boolean isResultAir;

	private REICrumbleHornDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, ResourceLocation recipeId, boolean isResultAir) {
		super(inputs, outputs, Optional.of(recipeId));

		this.isResultAir = isResultAir;
	}

	public static REICrumbleHornDisplay of(CrumbleRecipe recipe) {
		boolean isResultAir = recipe.result().isAir();

		List<EntryIngredient> inputs = List.of(EntryIngredient.of(EntryStacks.of(recipe.input().getBlock())));
		List<EntryIngredient> outputs = isResultAir
				? List.of(EntryIngredient.of(EntryStack.of(TwilightForestREIClientPlugin.ENTITY_DEFINITION, TwilightForestREIClientPlugin.createItemEntity(recipe.input().getBlock().asItem()))))
				: List.of(EntryIngredient.of(EntryStacks.of(recipe.result().getBlock())));

		return new REICrumbleHornDisplay(inputs, outputs, recipe.getId(), isResultAir);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICrumbleHornCategory.CRUMBLE_HORN;
	}
}
