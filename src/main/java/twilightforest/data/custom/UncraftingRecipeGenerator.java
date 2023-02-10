package twilightforest.data.custom;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;

public class UncraftingRecipeGenerator extends UncraftingRecipeProvider {

	public UncraftingRecipeGenerator(FabricDataOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerUncraftingRecipes() {
		this.addUncraftingRecipe("tipped_arrow_uncraft", Ingredient.of(Items.TIPPED_ARROW), 8, 4, new String[]{"AAA", "A A", "AAA"}, Ingredient.of(Items.ARROW));
	}
}
