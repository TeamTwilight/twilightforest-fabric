package twilightforest.init;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.item.recipe.ComponentsIngredient;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.DryingRecipe;
import twilightforest.item.recipe.CasketRepairRecipe;
import twilightforest.item.recipe.EmperorsClothRecipe;
import twilightforest.item.recipe.EssenceRepairRecipe;
import twilightforest.item.recipe.MagicMapCloningRecipe;
import twilightforest.item.recipe.MazeMapCloningRecipe;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;
import twilightforest.item.recipe.ScepterRepairRecipe;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

/**
 * Codex Fabric port of upstream {@code twilightforest.init.TFRecipes}.
 * Currently registers {@link DryingRecipe} type + serializer so the
 * drying-rack tick loop can resolve recipes loaded from JSON datapacks.
 *
 * <p>Bootstrap is class-init driven (calling {@link #bootstrap()} once forces
 * both static fields to resolve, which performs the registry writes through
 * vanilla's {@link Registry#register} hook).</p>
 */
public final class TFRecipes {

	public static final RecipeType<DryingRecipe> DRYING_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_TYPE,
		TwilightForestMod.prefix("drying"),
		new RecipeType<>() {
			@Override
			public String toString() { return "twilightforest:drying"; }
		});

	public static final RecipeSerializer<DryingRecipe> DRYING_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("drying"),
		new DryingRecipe.Serializer());

	public static final RecipeType<UncraftingRecipe> UNCRAFTING_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_TYPE,
		TwilightForestMod.prefix("uncrafting"),
		new RecipeType<>() {
			@Override
			public String toString() { return "twilightforest:uncrafting"; }
		});

	public static final RecipeSerializer<UncraftingRecipe> UNCRAFTING_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("uncrafting"),
		new UncraftingRecipe.Serializer());

	public static final RecipeSerializer<MagicMapCloningRecipe> MAGIC_MAP_CLONING_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("crafting_special_magic_map_cloning"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(MagicMapCloningRecipe::new));

	public static final RecipeSerializer<MazeMapCloningRecipe> MAZE_MAP_CLONING_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("crafting_special_maze_map_cloning"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(MazeMapCloningRecipe::new));

	public static final RecipeSerializer<CasketRepairRecipe> CASKET_REPAIR_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("casket_repair_recipe"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(CasketRepairRecipe::new));

	public static final RecipeSerializer<EmperorsClothRecipe> EMPERORS_CLOTH_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("emperors_cloth_recipe"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(EmperorsClothRecipe::new));

	public static final RecipeSerializer<EssenceRepairRecipe> ESSENCE_REPAIR_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("essence_repair_recipe"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(EssenceRepairRecipe::new));

	public static final RecipeSerializer<MoonwormQueenRepairRecipe> MOONWORM_QUEEN_REPAIR_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("moonworm_queen_repair_recipe"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(MoonwormQueenRepairRecipe::new));

	public static final RecipeSerializer<ScepterRepairRecipe> SCEPTER_REPAIR_RECIPE = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("scepter_repair"),
		new ScepterRepairRecipe.Serializer());

	public static final RecipeSerializer<NoTemplateSmithingRecipe> NO_TEMPLATE_SMITHING_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("no_template_smithing"),
		new NoTemplateSmithingRecipe.Serializer());

	public static final RecipeSerializer<TravellersGearModifierShapelessRecipe> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("travellers_gear_modifier_shapeless_recipe"),
		new TravellersGearModifierShapelessRecipe.Serializer());

	public static final RecipeSerializer<TravellersGearModifierShapedRecipe> MODIFIER_SHAPED_RECIPE_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("travellers_gear_modifier_shaped_recipe"),
		new TravellersGearModifierShapedRecipe.Serializer());

	public static final RecipeSerializer<TravellersVestGlovesMergeRecipe> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = Registry.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TwilightForestMod.prefix("travellers_vest_gloves_merge_recipe"),
		new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(TravellersVestGlovesMergeRecipe::new));

	private TFRecipes() {}

	public static void bootstrap() {
		CustomIngredientSerializer.register(ComponentsIngredient.SERIALIZER);
	}
}
