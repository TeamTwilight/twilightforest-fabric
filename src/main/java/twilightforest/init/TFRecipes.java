package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.TFMain;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

public class TFRecipes {

	public static final RecipeSerializer<CasketRepairRecipe> CASKET_REPAIR_RECIPE = registerSerializer("casket_repair_recipe", CasketRepairRecipe.SERIALIZER);
	public static final RecipeSerializer<EmperorsClothRecipe> EMPERORS_CLOTH_RECIPE = registerSerializer("emperors_cloth_recipe", EmperorsClothRecipe.SERIALIZER);
	public static final RecipeSerializer<EssenceRepairRecipe> ESSENCE_REPAIR_RECIPE = registerSerializer("essence_repair_recipe", EssenceRepairRecipe.SERIALIZER);
	public static final RecipeSerializer<MagicMapCloningRecipe> MAGIC_MAP_CLONING_RECIPE = registerSerializer("magic_map_cloning_recipe", MagicMapCloningRecipe.SERIALIZER);
	public static final RecipeSerializer<MazeMapCloningRecipe> MAZE_MAP_CLONING_RECIPE = registerSerializer("maze_map_cloning_recipe", MazeMapCloningRecipe.SERIALIZER);
	public static final RecipeSerializer<MoonwormQueenRepairRecipe> MOONWORM_QUEEN_REPAIR_RECIPE = registerSerializer("moonworm_queen_repair_recipe", MoonwormQueenRepairRecipe.SERIALIZER);
	public static final RecipeSerializer<ScepterRepairRecipe> SCEPTER_REPAIR_RECIPE = registerSerializer("scepter_repair", ScepterRepairRecipe.SERIALIZER);
	public static final RecipeSerializer<UncraftingRecipe> UNCRAFTING_SERIALIZER = registerSerializer("uncrafting", UncraftingRecipe.SERIALIZER);
	public static final RecipeSerializer<TravellersGearModifierShapelessRecipe> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = registerSerializer("travellers_gear_modifier_shapeless_recipe", TravellersGearModifierShapelessRecipe.SERIALIZER);
	public static final RecipeSerializer<TravellersGearModifierShapedRecipe> MODIFIER_SHAPED_RECIPE_SERIALIZER = registerSerializer("travellers_gear_modifier_shaped_recipe", TravellersGearModifierShapedRecipe.SERIALIZER);
	public static final RecipeSerializer<TravellersVestGlovesMergeRecipe> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = registerSerializer("travellers_vest_gloves_merge_recipe", TravellersVestGlovesMergeRecipe.SERIALIZER);
	public static final RecipeSerializer<NoTemplateSmithingRecipe> NO_TEMPLATE_SMITHING_SERIALIZER = registerSerializer("no_template_smithing", NoTemplateSmithingRecipe.SERIALIZER);
	public static final RecipeSerializer<DryingRecipe> DRYING_SERIALIZER = registerSerializer("drying", DryingRecipe.SERIALIZER);

	public static final RecipeType<UncraftingRecipe> UNCRAFTING_RECIPE = registerType("uncrafting");
	public static final RecipeType<DryingRecipe> DRYING_RECIPE = registerType("drying");

	private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
		return Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			TFMain.prefix(name),
			serializer
		);
	}

	private static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
		return Registry.register(BuiltInRegistries.RECIPE_TYPE, TFMain.prefix(name), new RecipeType<T>() {
			@Override
			public String toString() {
				return name;
			}
		});
	}
}