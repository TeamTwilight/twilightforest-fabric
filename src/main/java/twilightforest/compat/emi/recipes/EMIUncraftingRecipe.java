package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackConvertible;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.compat.emi.EMICompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EMIUncraftingRecipe<T extends Recipe<?>> implements EmiRecipe {
    public static final int width = 126;
    public static final int height = 64;

    protected final EmiRecipeCategory category;
    protected final T recipe;
    protected ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public EMIUncraftingRecipe(EmiRecipeCategory category, T recipe) {
        this.category = category;
        this.recipe = recipe;
        this.id = recipe.getId();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();

        List<Ingredient> outputs = new ArrayList<>(recipe.getIngredients()); //Collect each ingredient
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i, Ingredient.of(Arrays.stream(outputs.get(i).getItems())
                    .filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
                    .filter(o -> !(o.getItem().hasCraftingRemainingItem()))));//Remove any banned items
        }

        for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
            int x = j % 3, y = j / 3;
            if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
                k++;
                continue;
            } //Skips empty spaces in shaped recipes
            for (ItemStack output : outputs.get(j - k).getItems())
                this.outputs.add(EmiStack.of(output)); //Set input as output and place in the grid
        }

        if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
            this.inputs.add(EmiIngredient.of(uncraftingRecipe.getIngredient(), uncraftingRecipe.count()));//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
        } else {
            this.inputs.add(EmiStack.of(recipe.getResultItem()));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICompat.UNCRAFTING;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
            int x = j % 3, y = j / 3;
            if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
                k++;
                continue;
            } //Skips empty spaces in shaped recipes
            widgets.addSlot(outputs.get(j - k), x * 18 + 63, y * 18 + 1);
        }

        if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
            ItemStack[] stacks = uncraftingRecipe.getIngredient().getItems();
            ItemStack[] stackedStacks = new ItemStack[stacks.length];
            for (int i = 0; i < stacks.length; i++) stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.count());
            widgets.addSlot(new ListEmiIngredient(List.of(stackedStacks).stream().map(EmiStack::of).toList(), uncraftingRecipe.count()), 5, 19);//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
        } else {
            widgets.addSlot(EmiStack.of(recipe.getResultItem()), 5, 19); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
        }
    }
}
