package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import twilightforest.init.TFRecipes;

import org.jetbrains.annotations.Nullable;

public record TransformPowderRecipe(ResourceLocation recipeID, EntityType<?> input, EntityType<?> result, boolean isReversible) implements Recipe<Container> {

	@Override
	public boolean matches(Container container, Level level) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeID;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.TRANSFORMATION_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.TRANSFORM_POWDER_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<TransformPowderRecipe> {

		@Override
		public TransformPowderRecipe fromJson(ResourceLocation id, JsonObject object) {
			EntityType<?> input = Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(GsonHelper.getAsString(object, "from")));
			EntityType<?> output = Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(GsonHelper.getAsString(object, "to")));
			boolean reversible = GsonHelper.getAsBoolean(object, "reversible");
			if (input != null && output != null) {
				return new TransformPowderRecipe(id, input, output, reversible);
			}
			return new TransformPowderRecipe(id, EntityType.PIG, EntityType.ZOMBIFIED_PIGLIN, reversible);
		}

		@Nullable
		@Override
		public TransformPowderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			EntityType<?> input = Registry.ENTITY_TYPE.get(buffer.readResourceLocation());
			EntityType<?> output = Registry.ENTITY_TYPE.get(buffer.readResourceLocation());
			boolean reversible = buffer.readBoolean();
			return new TransformPowderRecipe(id, input, output, reversible);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TransformPowderRecipe recipe) {
			buffer.writeResourceLocation(Registry.ENTITY_TYPE.getKey(recipe.input));
			buffer.writeResourceLocation(Registry.ENTITY_TYPE.getKey(recipe.result));
			buffer.writeBoolean(recipe.isReversible());
		}
	}
}
