package twilightforest.inventory;

import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Iterator;

public interface UncraftingPlaceRecipe<C> extends PlaceRecipe<C> {
	int matrixOffset = 11;

	@Override
	default void placeRecipe(int width, int height, int outputSlot, RecipeHolder<?> recipe, Iterator<C> ingredients, int maxAmount) {
		int widthModified = width;
		int heightModified = height;
		if (recipe.value() instanceof ShapedRecipe shapedRecipe) {
			widthModified = shapedRecipe.getWidth();
			heightModified = shapedRecipe.getHeight();
		}

		int slotIndex = matrixOffset;
		for (int gridY = 0; gridY < height; ++gridY) {
			boolean yOverfitted = (float) heightModified < (float) height / 2.0F;
			int radius = Mth.floor((float) height / 2.0F - (float) heightModified / 2.0F);
			if (yOverfitted && radius > gridY) {
				slotIndex += width;
				++gridY;
			}

			for (int gridX = 0; gridX < width; ++gridX) {
				if (!ingredients.hasNext()) {
					return;
				}

				yOverfitted = (float) widthModified < (float) width / 2.0F;
				radius = Mth.floor((float) width / 2.0F - (float) widthModified / 2.0F);
				int edge = widthModified;
				boolean xOverfitted = gridX < widthModified;
				if (yOverfitted) {
					edge = radius + widthModified;
					xOverfitted = radius <= gridX && gridX < radius + widthModified;
				}

				if (xOverfitted) {
					this.addItemToSlot(ingredients.next(), slotIndex, maxAmount, gridY, gridX);
				} else if (edge == gridX) {
					slotIndex += width - gridX;
					break;
				}

				++slotIndex;
			}
		}
	}
}
