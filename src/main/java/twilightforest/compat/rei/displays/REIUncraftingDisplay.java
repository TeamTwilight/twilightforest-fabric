package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import twilightforest.compat.rei.TwilightForestREIServerPlugin;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.List;
import java.util.Optional;

public class REIUncraftingDisplay extends DefaultCraftingDisplay<CraftingRecipe> {

    private final CraftingRecipe craftingRecipe;

    private REIUncraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CraftingRecipe craftingRecipe) {
        super(inputs,
                outputs,
                Optional.of(craftingRecipe));
        this.craftingRecipe = craftingRecipe;
    }

    public static REIUncraftingDisplay of(CraftingRecipe craftingRecipe){
        boolean isUncraftingRecipe = craftingRecipe instanceof UncraftingRecipe;

        List<EntryIngredient> inputs = isUncraftingRecipe ? EntryIngredients.ofIngredients(craftingRecipe.getIngredients()) : List.of(EntryIngredients.of(craftingRecipe.getResultItem()));
        List<EntryIngredient> outputs = isUncraftingRecipe ? List.of(EntryIngredients.of(craftingRecipe.getResultItem())) : EntryIngredients.ofIngredients(craftingRecipe.getIngredients());

        return new REIUncraftingDisplay(inputs, outputs, craftingRecipe);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TwilightForestREIServerPlugin.UNCRAFTING;
    }

    public CraftingRecipe getRecipe() {
        return craftingRecipe;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    public enum Serializer implements DisplaySerializer<REIUncraftingDisplay> {
        INSTANCE;

        @Override
        public CompoundTag save(CompoundTag tag, REIUncraftingDisplay display) {
            tag.putString("recipe_id", display.getRecipe().getId().toString());

            return tag;
        }

        @Override
        public REIUncraftingDisplay read(CompoundTag tag) {
            ResourceLocation location = ResourceLocation.tryParse(tag.getString("recipe_id"));

            if(location != null){
                Optional<CraftingRecipe> recipe = (Optional<CraftingRecipe>) RecipeManagerContext.getInstance().getRecipeManager().byKey(location);

                if(recipe.isPresent()) return REIUncraftingDisplay.of(recipe.get());
            }

            return null;
        }
    }

}
