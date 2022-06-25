package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;

public class TFRecipes {
	public static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = LazyRegistrar.create(Registry.RECIPE_SERIALIZER, TwilightForestMod.ID);
	public static final LazyRegistrar<RecipeType<?>> RECIPE_TYPES = LazyRegistrar.create(Registry.RECIPE_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<SimpleRecipeSerializer<?>> MAGIC_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("magic_map_cloning_recipe", () -> new SimpleRecipeSerializer<>(MagicMapCloningRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<?>> MAZE_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("maze_map_cloning_recipe", () -> new SimpleRecipeSerializer<>(MazeMapCloningRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<?>> MOONWORM_QUEEN_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("moonworm_queen_repair_recipe", () -> new SimpleRecipeSerializer<>(MoonwormQueenRepairRecipe::new));
	public static final RegistryObject<RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", UncraftingRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<CrumbleRecipe>> CRUMBLE_SERIALIZER = RECIPE_SERIALIZERS.register("crumble_horn", CrumbleRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<TransformPowderRecipe>> TRANSFORMATION_SERIALIZER = RECIPE_SERIALIZERS.register("transformation_powder", TransformPowderRecipe.Serializer::new);

	public static final RegistryObject<RecipeType<UncraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> new RecipeType<>() {
		@Override
		public String toString() {
			return "twilightforest:unique_uncrafting";
		}
	});
	public static final RegistryObject<RecipeType<CrumbleRecipe>> CRUMBLE_RECIPE = RECIPE_TYPES.register("crumble_horn", () -> new RecipeType<>() {
		@Override
		public String toString() {
			return "twilightforest:crumble_horn";
		}
	});
	public static final RegistryObject<RecipeType<TransformPowderRecipe>> TRANSFORM_POWDER_RECIPE = RECIPE_TYPES.register("transformation_powder", () -> new RecipeType<>() {
		@Override
		public String toString() {
			return "twilightforest:transformation_powder";
		}
	});
}
