package twilightforest.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.UncraftingGui;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.recipe.TFRecipes;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class REI implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE.get()));
        registry.addWorkstations(REIUncraftingCategory.UNCRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE.get()));

        registry.add(new REIUncraftingCategory());
    }

//    @Override
//    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(UncraftingContainer.class, RecipeTypes.CRAFTING, 11, 9, 20, 36);
//    }

//    @Override
//    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
//        IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
//
//        if(FabricLoader.getInstance().isModLoaded(TFCompat.IE_ID)) {
//            ShaderRegistry.rarityWeightMap.keySet().forEach((rarity) ->
//                    ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(
//                            ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_" + rarity)).getDefaultInstance()
//                    )));
//
//            for (ShaderRegistry.ShaderRegistryEntry entry : IEShaderRegister.getAllTwilightShaders()) {
//                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")));
//                ItemNBTHelper.putString(stack, "shader_name", entry.getName().toString());
//                ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(stack));
//            }
//        }
//    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CraftingRecipe> recipes = manager.getAllRecipesFor(RecipeType.CRAFTING);
        recipes.removeIf(recipe -> recipe.getResultItem().isEmpty() ||
                recipe.getResultItem().is(ItemTagGenerator.BANNED_UNCRAFTABLES) ||
                TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.getId().toString()) ||
                TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() != TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.getId().getNamespace()));//Prevents things that are tagged as banned from showing up
        recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
        for (CraftingRecipe recipe : recipes) {
            Collection<Display> displays = registry.tryFillDisplay(recipe);
            for (Display display : displays) {
                registry.add(new REIUncraftingDisplay(recipe));
            }
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(34, 33, 27, 20), UncraftingGui.class, REIUncraftingCategory.UNCRAFTING);
        registry.registerClickArea(screen -> new Rectangle(115, 33, 27, 20), UncraftingGui.class, BuiltinPlugin.CRAFTING);
    }
}
