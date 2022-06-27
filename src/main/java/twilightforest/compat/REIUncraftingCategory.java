package twilightforest.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class REIUncraftingCategory implements DisplayCategory<REIUncraftingDisplay> {
    public static final CategoryIdentifier<REIUncraftingDisplay> UNCRAFTING = CategoryIdentifier.of(TwilightForestMod.ID, "uncrafting");
    public static final int width = 126;
    public static final int height = 64;
    private final Renderer icon;
    private final Component localizedName;

    public REIUncraftingCategory() {
        ResourceLocation location = TwilightForestMod.getGuiTexture("uncrafting_jei.png");
        this.icon = EntryStacks.of(TFBlocks.UNCRAFTING_TABLE.get());
        this.localizedName = Component.translatable("gui.uncrafting_jei");
    }

    @Override
    public CategoryIdentifier<REIUncraftingDisplay> getCategoryIdentifier() {
        return UNCRAFTING;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }

    @Override
    public int getDisplayWidth(REIUncraftingDisplay display) {
        return width;
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(REIUncraftingDisplay display, Rectangle origin) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(origin));
        Rectangle bounds = origin.getBounds();
        bounds.translate(5, 5);
        widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("uncrafting_jei.png"), bounds));
        List<Ingredient> outputs = new ArrayList<>(display.getRecipe().getIngredients()); //Collect each ingredient
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i, Ingredient.of(Arrays.stream(outputs.get(i).getItems())
                    .filter(o -> !(o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS)))
                    .filter(o -> !(o.getItem().hasCraftingRemainingItem()))));//Remove any banned items
        }

        CraftingRecipe recipe = display.getRecipe();

        for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
            int x = j % 3, y = j / 3;
            if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
                k++;
                continue;
            } //Skips empty spaces in shaped recipes
            widgets.add(Widgets.createSlot(new Point(bounds.getX() + x * 18 + 63, bounds.getY() + y * 18 + 1)).markOutput().disableBackground().entries(EntryIngredients.ofIngredient(outputs.get(j - k)))); //Set input as output and place in the grid
        }

        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 5, bounds.getY() + 19)).markInput().disableBackground().entries(EntryIngredients.of(recipe.getResultItem())));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
        return widgets;
    }
}