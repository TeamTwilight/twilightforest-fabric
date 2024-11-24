package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.emi.TFEmiRecipeCategory;

import java.util.ArrayList;
import java.util.List;

public abstract class TFEmiRecipe<T extends Recipe<?>> implements EmiRecipe {

	public final EmiRecipeCategory category;
	public final T recipe;
	public final ResourceLocation id;
	public final int width;
	public final int height;
	protected final List<EmiIngredient> inputs;
	protected final List<EmiStack> outputs;

	public TFEmiRecipe(TFEmiRecipeCategory category, T recipe, int width, int height) {
		this.category = category;
		this.recipe = recipe;
		this.width = width;
		this.height = height;

		ResourceLocation recipeId = recipe.getId();
		String path = "emi/%s/%s/%s".formatted(category.name, recipeId.getNamespace(), recipeId.getPath());
		this.id = TwilightForestMod.prefix(path);

		this.inputs = new ArrayList<>();
		addInputs(this.inputs);
		this.outputs = new ArrayList<>();
		addOutputs(this.outputs);
	}

	protected abstract void addInputs(List<EmiIngredient> inputs);

	protected abstract void addOutputs(List<EmiStack> outputs);

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	@Nullable
	public ResourceLocation getId() {
		return id;
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
	public List<EmiIngredient> getInputs() {
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}
}
