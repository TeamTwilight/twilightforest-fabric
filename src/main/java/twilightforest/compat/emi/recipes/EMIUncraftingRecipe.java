package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.compat.emi.EMICompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EMIUncraftingRecipe<T extends Recipe<?>> implements EmiRecipe {
    public static final int width = 118;
    public static final int height = 54;

    protected final EmiRecipeCategory category;
    protected final T recipe;
    protected ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiIngredient> outputs;
    private final List<EmiStack> flattenedOutputs;

    public static <T extends Recipe<?>> EMIUncraftingRecipe<T> of(T crafting) {
        ResourceLocation baseId = crafting.getId();
        ResourceLocation newId = TwilightForestMod.prefix("emi/uncrafting/%s/%s".formatted(baseId.getNamespace(), baseId.getPath()));
        return new EMIUncraftingRecipe<>(EMICompat.UNCRAFTING, crafting, newId);
    }

    public EMIUncraftingRecipe(EmiRecipeCategory category, T recipe, ResourceLocation id) {
        this.category = category;
        this.recipe = recipe;
        this.id = id;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.flattenedOutputs = new ArrayList<>();

        List<Ingredient> outputs = new ArrayList<>(recipe.getIngredients()); //Collect each ingredient
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i, Ingredient.of(Arrays.stream(outputs.get(i).getItems())
                    .filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
                    .filter(o -> (o.getRecipeRemainder().isEmpty()))));//Remove any banned items
        }

        for (int index = 0, offset = 0; index - offset < outputs.size() && index < 9; index++) {
            int x = index % 3, y = index / 3;
            if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
                offset++;
                this.outputs.add(EmiStack.EMPTY);
                continue;
            } //Skips empty spaces in shaped recipes
            Ingredient ingredient = outputs.get(index - offset);
            this.outputs.add(EmiIngredient.of(ingredient));
            for (ItemStack output : ingredient.getItems())
                this.flattenedOutputs.add(EmiStack.of(output)); //Set input as output and place in the grid
        }

        while (this.outputs.size() < 9) {
            this.outputs.add(EmiStack.EMPTY);
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
        return this.flattenedOutputs;
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
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 35, 18);

        for (int i = 0; i < outputs.size(); i++) {
            int x = i % 3;
            int y = i / 3;
            widgets.addSlot(outputs.get(i), x * 18 + 63, y * 18);
        }

        if (recipe instanceof UncraftingRecipe uncraftingRecipe) {
            ItemStack[] stacks = uncraftingRecipe.getIngredient().getItems();
            ItemStack[] stackedStacks = new ItemStack[stacks.length];
            for (int i = 0; i < stacks.length; i++) stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.count());
            widgets.addSlot(new ListEmiIngredient(List.of(stackedStacks).stream().map(EmiStack::of).toList(), uncraftingRecipe.count()), 5, 19);//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
        } else {
            widgets.addSlot(EmiStack.of(recipe.getResultItem()), 5, 14).output(true).recipeContext(this); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
        }
    }
}
