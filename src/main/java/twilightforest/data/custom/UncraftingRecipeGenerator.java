package twilightforest.data.custom;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;

public class UncraftingRecipeGenerator extends UncraftingRecipeProvider {

	public UncraftingRecipeGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerUncraftingRecipes() {
		this.addUncraftingRecipe("tipped_arrow_uncraft", Ingredient.of(Items.TIPPED_ARROW), 8, 4, new String[]{"AAA", "A A", "AAA"}, Ingredient.of(Items.ARROW));
	}
}
