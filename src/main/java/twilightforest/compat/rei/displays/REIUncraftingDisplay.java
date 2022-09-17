package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.CraftingRecipe;
import twilightforest.compat.rei.categories.REIUncraftingCategory;

import java.util.List;

public class REIUncraftingDisplay extends BasicDisplay {

    private final CraftingRecipe craftingRecipe;

    public REIUncraftingDisplay(CraftingRecipe craftingRecipe) {
        super(EntryIngredients.ofIngredients(craftingRecipe.getIngredients()), List.of(EntryIngredients.of(craftingRecipe.getResultItem())));
        this.craftingRecipe = craftingRecipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIUncraftingCategory.UNCRAFTING;
    }

    public CraftingRecipe getRecipe() {
        return craftingRecipe;
    }
}
