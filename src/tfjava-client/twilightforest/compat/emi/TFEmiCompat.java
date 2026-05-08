package twilightforest.compat.emi;

import com.mojang.datafixers.util.Pair;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import dev.emi.emi.recipe.EmiGrindstoneRecipe;
import dev.emi.emi.recipe.special.EmiAnvilEnchantRecipe;
import dev.emi.emi.recipe.special.EmiAnvilRepairItemRecipe;
import dev.emi.emi.recipe.special.EmiGrindstoneDisenchantingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.recipes.*;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@EmiEntrypoint
public class TFEmiCompat implements EmiPlugin {

	private static final Function<List<EmiIngredient>, Boolean> CANT_USE_ENCHANTS = stack ->
		stack.contains(EmiStack.of(TFItems.MOONWORM_QUEEN.get())) || stack.contains(EmiStack.of(TFItems.LAMPOFCINDERS.get())) || stack.contains(EmiStack.of(TFItems.ORE_MAGNET.get())) ||
			stack.contains(EmiStack.of(TFItems.TWILIGHT_SCEPTER.get())) || stack.contains(EmiStack.of(TFItems.LIFEDRAIN_SCEPTER.get())) ||
			stack.contains(EmiStack.of(TFItems.ZOMBIE_SCEPTER.get())) || stack.contains(EmiStack.of(TFItems.FORTIFICATION_SCEPTER.get())) ||
			stack.contains(EmiStack.of(TFItems.TRAVELLERS_GOGGLES.get())) || stack.contains(EmiStack.of(TFItems.TRAVELLERS_VEST.get())) || stack.contains(EmiStack.of(TFItems.TRAVELLERS_GLOVES.get())) ||
			stack.contains(EmiStack.of(TFItems.TRAVELLERS_BELT.get())) || stack.contains(EmiStack.of(TFItems.TRAVELLERS_WINGS.get())) || stack.contains(EmiStack.of(TFItems.TRAVELLERS_BOOTS.get()));

	private static final Function<List<EmiIngredient>, Boolean> NO_REPAIRING = stack ->
		stack.contains(EmiStack.of(TFItems.LAMPOFCINDERS.get())) || stack.contains(EmiStack.of(TFItems.GLASS_SWORD.get())) || stack.contains(EmiStack.of(TFItems.MAZEBREAKER_PICKAXE.get()));

	@Override
	public void register(EmiRegistry registry) {

		registry.addCategory(TFEmiCategories.UNCRAFTING);
		registry.addCategory(TFEmiCategories.CRUMBLE_HORN);
		registry.addCategory(TFEmiCategories.TRANSFORMATION);
		registry.addCategory(TFEmiCategories.EXANIMATE);
		registry.addCategory(TFEmiCategories.DRYING);

		if (!TFConfig.disableEntireTable) {
			registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE.get()));
			registry.addWorkstation(TFEmiCategories.UNCRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE.get()));
		}
		registry.addWorkstation(TFEmiCategories.CRUMBLE_HORN, EmiStack.of(TFItems.CRUMBLE_HORN.get()));
		registry.addWorkstation(TFEmiCategories.TRANSFORMATION, EmiStack.of(TFItems.TRANSFORMATION_POWDER.get()));
		registry.addWorkstation(TFEmiCategories.EXANIMATE, EmiStack.of(TFItems.EXANIMATE_ESSENCE.get()));

		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		if (!TFConfig.disableEntireTable) {
			List<RecipeHolder<? extends CraftingRecipe>> recipes = RecipeViewerConstants.getAllUncraftingRecipes(manager);
			recipes.forEach(recipe -> registry.addRecipe(new EmiUncraftingRecipe<>(recipe)));
		}
		for (RecipeViewerConstants.TransformationPowderInfo info : RecipeViewerConstants.getTransformationPowderRecipes()) {
			registry.addRecipe(new EmiTransformationPowderRecipe(info.input(), info.output(), info.reversible()));
		}

		for (RecipeViewerConstants.OminousFireInfo info : RecipeViewerConstants.getOminousFireRecipes()) {
			registry.addRecipe(new EmiOminousFireRecipe(info.input(), info.output()));
		}

		for (Pair<Block, Block> info : RecipeViewerConstants.getCrumbleHornRecipes()) {
			registry.addRecipe(new EmiCrumbleHornRecipe(info.getFirst(), info.getSecond()));
		}

		for (RecipeHolder<SmithingRecipe> holder : manager.getAllRecipesFor(RecipeType.SMITHING).stream().filter(holder -> holder.value() instanceof NoTemplateSmithingRecipe).toList()) {
			NoTemplateSmithingRecipe recipe = (NoTemplateSmithingRecipe) holder.value();
			registry.addRecipe(new EmiNoSmithingTemplateRecipe(EmiIngredient.of(recipe.getBase()), EmiIngredient.of(recipe.getAddition()), EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())), recipe));
		}


		for (RecipeHolder<DryingRecipe> holder : manager.getAllRecipesFor(TFRecipes.DRYING_RECIPE)) {
			if (!holder.value().getResult().is(TFItems.STALE_BREAD.get())) {
				registry.addRecipe(new EmiDryingRecipe(holder));
			}
		}

		EmiTravellersGearGrindstoneRecipe.register(registry);

		for (RecipeHolder<?> holder : manager.getAllRecipesFor(RecipeType.CRAFTING)) {
			EmiRecipe emiRecipe = switch (holder.value()) {
				case MoonwormQueenRepairRecipe ignored -> new EmiMoonwormQueenRecipe();
				case EmperorsClothRecipe ignored -> new EmiEmperorsClothRecipe();
				case ScepterRepairRecipe recipe ->
					new EmiScepterRepairRecipe(
						recipe.getIngredients().stream()
							.map(EmiIngredient::of)
							.toList(),
						EmiStack.of(recipe.getScepter()),
						recipe.getRepairDurability(),
						holder.id()
					);
				case TravellersGearModifierRecipe recipe -> {
					if (TravellersModifiersManager.isModifierEnabled(Minecraft.getInstance().level.registryAccess(), recipe.getTravellersModifierKey()))
						yield new EmiTravellersGearModifierRecipe(recipe);
					else
						yield null;
				}

				default -> null;
			};

			if (emiRecipe != null) {
				registry.addRecipe(emiRecipe);
			}
		}

		//remove other recipes as they aren't actually possible recipes to use
		//emi makes a few assumptions about damageable items that it honestly shouldnt
		registry.removeRecipes(recipe -> {
			if (recipe instanceof EmiPatternCraftingRecipe || recipe instanceof EmiGrindstoneRecipe) {
				return recipe.getInputs().contains(EmiStack.of(TFItems.MOONWORM_QUEEN.get())) || NO_REPAIRING.apply(recipe.getInputs());
			} else if (recipe instanceof EmiGrindstoneDisenchantingRecipe || recipe instanceof EmiAnvilEnchantRecipe) {
				return CANT_USE_ENCHANTS.apply(recipe.getInputs());
			} else if (recipe instanceof EmiAnvilRepairItemRecipe || recipe instanceof EmiAnvilRecipe) {
				return NO_REPAIRING.apply(recipe.getInputs());
			}
			return false;
		});

		registry.removeEmiStacks(stack -> BuiltInRegistries.ITEM.getKey(TFItems.MAGIC_PAINTING.get()).equals(stack.getId()));
	}
}
