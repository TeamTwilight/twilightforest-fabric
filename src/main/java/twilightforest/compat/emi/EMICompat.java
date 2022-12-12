package twilightforest.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.TFConfig;
import twilightforest.compat.emi.recipes.EMIUncraftingRecipe;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFRecipes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EMICompat implements EmiPlugin {
	public static final EmiRecipeCategory UNCRAFTING = new TFEmiRecipeCategory("uncrafting", TFBlocks.UNCRAFTING_TABLE);

	@Override
	public void register(EmiRegistry registry) {
		// TODO implement when stuff compiles
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE.get()));
		registry.addWorkstation(UNCRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE.get()));

		registry.addCategory(UNCRAFTING);

		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncrafting.get()) { //we only do this if uncrafting is not disabled
			List<CraftingRecipe> recipes = manager.getAllRecipesFor(RecipeType.CRAFTING);
			recipes = recipes.stream().filter(recipe ->
							!recipe.getResultItem().isEmpty() && //get rid of empty items
									!recipe.getResultItem().is(ItemTagGenerator.BANNED_UNCRAFTABLES) && //Prevents things that are tagged as banned from showing up
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.getId().toString()) && //remove disabled recipes
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.getId().getNamespace())) //remove blacklisted mod ids
					.collect(Collectors.toList());
			recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
			recipes.forEach(craftingRecipe -> registry.addRecipe(EMIUncraftingRecipe.of(craftingRecipe)));
		}
	}
}
