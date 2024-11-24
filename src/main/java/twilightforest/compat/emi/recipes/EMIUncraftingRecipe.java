package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.compat.emi.EMICompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EMIUncraftingRecipe extends TFEmiRecipe<CraftingRecipe> {
	public static final int WIDTH = 118;
	public static final int HEIGHT = 54;

	private List<EmiIngredient> displayedOutputs;

	public EMIUncraftingRecipe(CraftingRecipe recipe) {
		super(EMICompat.UNCRAFTING, recipe, WIDTH, HEIGHT);
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
			inputs.add(EmiIngredient.of(uncraftingRecipe.getIngredients().stream().map(EmiIngredient::of).toList(), uncraftingRecipe.count()));//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			inputs.add(EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}
	}

	@Override
	protected void addOutputs(List<EmiStack> finalOutput) {
		this.displayedOutputs = new ArrayList<>();
		List<Ingredient> outputs = new ArrayList<>(recipe.getIngredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> {
			return Ingredient.of(Arrays.stream(ingredient.getItems())
					.filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
					.filter(o -> o.getRecipeRemainder().isEmpty()));//Remove any banned items
		});

		for (int index = 0, offset = 0; index - offset < outputs.size() && index < 9; index++) {
			int x = index % 3, y = index / 3;
			if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
				offset++;
				displayedOutputs.add(EmiStack.EMPTY);
				continue;
			} //Skips empty spaces in shaped recipes
			Ingredient ingredient = outputs.get(index - offset);
			displayedOutputs.add(EmiIngredient.of(ingredient));
			for (ItemStack output : ingredient.getItems())
				finalOutput.add(EmiStack.of(output)); //Set input as output and place in the grid
		}

		while (displayedOutputs.size() < 9) {
			displayedOutputs.add(EmiStack.EMPTY);
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 35, 18);

		for (int i = 0; i < displayedOutputs.size(); i++) {
			int x = i % 3;
			int y = i / 3;
			widgets.addSlot(displayedOutputs.get(i), x * 18 + 63, y * 18);
		}

		if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
			//ItemStack[] stacks = uncraftingRecipe.getIngredient().getItems();
			ItemStack[] stacks = uncraftingRecipe.getIngredients().stream().map(Ingredient::getItems).flatMap(Arrays::stream).toArray(ItemStack[]::new);
			ItemStack[] stackedStacks = new ItemStack[stacks.length];
			for (int i = 0; i < stacks.length; i++)
				stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.count());
			widgets.addSlot(new ListEmiIngredient(Stream.of(stackedStacks).map(EmiStack::of).toList(), uncraftingRecipe.count()), 5, 19);//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			widgets.addSlot(EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())), 5, 14).recipeContext(this); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
