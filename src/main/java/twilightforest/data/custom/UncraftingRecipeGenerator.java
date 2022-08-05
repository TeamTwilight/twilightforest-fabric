package twilightforest.data.custom;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class UncraftingRecipeGenerator extends UncraftingRecipeProvider {

	public UncraftingRecipeGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerUncraftingRecipes() {
		addUncraftingRecipe("tipped_arrow_uncraft", Ingredient.of(Items.TIPPED_ARROW), 8, 4, new String[]{"AAA", "A A", "AAA"}, Ingredient.of(Items.ARROW));
	}
}
