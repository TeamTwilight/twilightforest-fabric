package twilightforest.data.custom;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class UncraftingGenerator {

	public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
		UncraftingRecipeBuilder.uncrafting(Items.TIPPED_ARROW, 8).setCost(4)
				.pattern("AAA")
				.pattern("A A")
				.pattern("AAA")
				.define('A', Ingredient.of(Items.ARROW)).save(consumer);

		UncraftingRecipeBuilder.uncrafting(Items.WRITTEN_BOOK).setCost(0)
				.pattern("B")
				.define('B', Items.BOOK).save(consumer);
	}
}
