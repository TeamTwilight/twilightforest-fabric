package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;

public class TFRecipes {
	public static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = LazyRegistrar.create(Registries.RECIPE_SERIALIZER, TwilightForestMod.ID);
	public static final LazyRegistrar<RecipeType<?>> RECIPE_TYPES = LazyRegistrar.create(Registries.RECIPE_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<RecipeSerializer<MagicMapCloningRecipe>> MAGIC_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("magic_map_cloning_recipe", () -> new SimpleCraftingRecipeSerializer<>(MagicMapCloningRecipe::new));
	public static final RegistryObject<RecipeSerializer<MazeMapCloningRecipe>> MAZE_MAP_CLONING_RECIPE = RECIPE_SERIALIZERS.register("maze_map_cloning_recipe", () -> new SimpleCraftingRecipeSerializer<>(MazeMapCloningRecipe::new));
	public static final RegistryObject<RecipeSerializer<MoonwormQueenRepairRecipe>> MOONWORM_QUEEN_REPAIR_RECIPE = RECIPE_SERIALIZERS.register("moonworm_queen_repair_recipe", () -> new SimpleCraftingRecipeSerializer<>(MoonwormQueenRepairRecipe::new));
	public static final RegistryObject<RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("uncrafting", UncraftingRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<CrumbleRecipe>> CRUMBLE_SERIALIZER = RECIPE_SERIALIZERS.register("crumble_horn", CrumbleRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<TransformPowderRecipe>> TRANSFORMATION_SERIALIZER = RECIPE_SERIALIZERS.register("transformation_powder", TransformPowderRecipe.Serializer::new);

	public static final RegistryObject<RecipeType<UncraftingRecipe>> UNCRAFTING_RECIPE = RECIPE_TYPES.register("uncrafting", () -> RecipeType.simple(TwilightForestMod.prefix("uncrafting")));
	public static final RegistryObject<RecipeType<CrumbleRecipe>> CRUMBLE_RECIPE = RECIPE_TYPES.register("crumble_horn", () -> RecipeType.simple(TwilightForestMod.prefix("crumble_horn")));
	public static final RegistryObject<RecipeType<TransformPowderRecipe>> TRANSFORM_POWDER_RECIPE = RECIPE_TYPES.register("transformation_powder", () -> RecipeType.simple(TwilightForestMod.prefix("transformation_powder")));
}
