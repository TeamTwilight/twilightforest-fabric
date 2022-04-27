package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CrumbleRecipe implements Recipe<Container> {

	private final ResourceLocation recipeID;
	private final BlockState input;
	private final BlockState result;

	public CrumbleRecipe(ResourceLocation recipeID, BlockState input, BlockState result) {
		this.recipeID = recipeID;
		this.input = input;
		this.result = result;
	}

	public BlockState getInput() {
		return input;
	}

	public BlockState getResult() {
		return result;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
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
		return TFRecipes.CRUMBLE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return TFRecipes.CRUMBLE_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<CrumbleRecipe> {

		@Override
		public CrumbleRecipe fromJson(ResourceLocation id, JsonObject object) {
			Block input = Registry.BLOCK.get(ResourceLocation.tryParse(GsonHelper.getAsString(object, "from")));
			Block output = Registry.BLOCK.get(ResourceLocation.tryParse(GsonHelper.getAsString(object, "to")));
			if(input != null && output != null) {
				return new CrumbleRecipe(id, input.defaultBlockState(), output.defaultBlockState());
			}
			return new CrumbleRecipe(id, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState());
		}

		@Nullable
		@Override
		public CrumbleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			Block input = Registry.BLOCK.get(buffer.readResourceLocation());
			Block output = Registry.BLOCK.get(buffer.readResourceLocation());
			return new CrumbleRecipe(id, input.defaultBlockState(), output.defaultBlockState());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CrumbleRecipe recipe) {
			buffer.writeResourceLocation(Registry.BLOCK.getKey(recipe.input.getBlock()));
			buffer.writeResourceLocation(Registry.BLOCK.getKey(recipe.result.getBlock()));
		}
	}
}
