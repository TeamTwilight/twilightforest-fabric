package twilightforest.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import twilightforest.TFConstants;
import twilightforest.block.TFBlocks;

import java.util.function.Consumer;

public abstract class CraftingDataHelper extends RecipeProvider {
	public CraftingDataHelper(DataGenerator generator) {
		super(generator);
	}

	//TODO: PORT
//	protected final Ingredient itemWithNBT(ItemLike item, Consumer<CompoundTag> nbtSetter) {
//		ItemStack stack = new ItemStack(item);
//
//		CompoundTag nbt = new CompoundTag();
//		nbtSetter.accept(nbt);
//		stack.setTag(nbt);
//
//		try {
//			Constructor<Ingredient> constructor = Ingredient.class.getDeclaredConstructor(Stream.class);
//
//			constructor.setAccessible(true);
//
//			return constructor.newInstance(stack);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
//		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
//		return Ingredient.of(stack);
//	}

//	protected final Ingredient multipleIngredients(Ingredient... ingredientArray) {
//		List<Ingredient> ingredientList = ImmutableList.copyOf(ingredientArray);
//
//		try {
//			Constructor<CompoundIngredient> constructor = CompoundIngredient.class.getDeclaredConstructor(List.class);
//
//			constructor.setAccessible(true);
//
//			return constructor.newInstance(ingredientList);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
//		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
//		return Ingredient.merge(ingredientList);
//	}

	protected final void charmRecipe(Consumer<FinishedRecipe> consumer, String name, Item result, Item item) {
		ShapelessRecipeBuilder.shapeless(result)
				.requires(item, 4)
				.unlockedBy("has_item", has(item))
				.save(consumer, TFConstants.prefix(name));
	}

	protected final void castleBlock(Consumer<FinishedRecipe> consumer, Block result, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result, 4)
				.pattern("##")
				.pattern("##")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_castle_brick", has(TFBlocks.castle_brick))
				.save(consumer, locCastle(Registry.BLOCK.getKey(result).getPath()));
	}

	protected final void stairsBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Block result, Block criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result,  8)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria))
				.save(consumer, loc);
	}

	protected final void stairsRightBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Block result, Block criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(result,  8)
				.pattern("###")
				.pattern(" ##")
				.pattern("  #")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria))
				.save(consumer, loc);
	}

	protected final void compressedBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Tag.Named<Item> ingredient) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TFConstants.prefix("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> ingredient) {
		ShapelessRecipeBuilder.shapeless(result, 9)
				.requires(ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TFConstants.prefix("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("###")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void chestplateItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("# #")
				.pattern("###")
				.pattern("###")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void leggingsItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("###")
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void bootsItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void pickaxeItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material, Item handle) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void swordItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material, Item handle) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("#")
				.pattern("#")
				.pattern("X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void axeItem(Consumer<FinishedRecipe> consumer, String name, Item result, Tag.Named<Item> material, Item handle) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("##")
				.pattern("#X")
				.pattern(" X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void buttonBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapelessRecipeBuilder.shapeless(result)
				.requires(material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_button"));
	}

	protected final void doorBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_door"));
	}

	protected final void fenceBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("#S#")
				.pattern("#S#")
				.define('#', material)
				.define('S', Items.STICK)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_fence"));
	}

	protected final void gateBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("S#S")
				.pattern("S#S")
				.define('#', material)
				.define('S', Items.STICK)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_gate"));
	}

	protected final void planksBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapelessRecipeBuilder.shapeless(result, 4)
				.requires(material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_planks"));
	}

	protected final void plateBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result)
				.pattern("##")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_plate"));
	}

	protected final void slabBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 6)
				.pattern("###")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_slab"));
	}

	protected final void bannerPattern(Consumer<FinishedRecipe> consumer, String name, Block trophy, Item result) {
		ShapelessRecipeBuilder.shapeless(result)
				.requires(Ingredient.of(ItemTagGenerator.PAPER))
				.requires(Ingredient.of(trophy.asItem()))
				.unlockedBy("has_trophy", has(trophy))
				.save(consumer);
	}

	protected final void trapdoorBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 2)
				.pattern("###")
				.pattern("###")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_trapdoor"));
	}

	protected final void woodBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("##")
				.pattern("##")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_wood"));
	}

	protected final void strippedWoodBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("##")
				.pattern("##")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_stripped_wood"));
	}

	protected final void signBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("###")
				.pattern("###")
				.pattern(" - ")
				.define('#', material)
				.define('-', Items.STICK)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_wood"));
	}

	protected final void banisterBlock(Consumer<FinishedRecipe> consumer, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(result, 3)
				.pattern("---")
				.pattern("| |")
				.define('-', material)
				.define('|', Items.STICK)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_banister"));
	}

	protected final void fieryConversion(Consumer<FinishedRecipe> consumer, Item result, Item armor, int vials) {
		ShapelessRecipeBuilder.shapeless(result)
				.requires(armor)
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), vials)
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(consumer, locEquip("fiery_" + Registry.ITEM.getKey(armor).getPath()));
	}

	protected final ResourceLocation locCastle(String name) {
		return TFConstants.prefix("castleblock/" + name);
	}

	protected final ResourceLocation locEquip(String name) {
		return TFConstants.prefix("equipment/" + name);
	}

	protected final ResourceLocation locNaga(String name) {
		return TFConstants.prefix("nagastone/" + name);
	}

	protected final ResourceLocation locWood(String name) {
		return TFConstants.prefix("wood/" + name);
	}
}
