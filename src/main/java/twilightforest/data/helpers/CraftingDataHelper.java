package twilightforest.data.helpers;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFChestBlock;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CraftingDataHelper extends FabricRecipeProvider {
	public CraftingDataHelper(FabricDataOutput output) {
		super(output);
	}

	public final Ingredient scepter(Item scepter) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt(ItemStack.TAG_DAMAGE, scepter.getMaxDamage());
		return DefaultCustomIngredients.nbt(Ingredient.of(scepter), nbt, false);
	}

	public final Ingredient potion(Potion potion) {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("Potion", BuiltInRegistries.POTION.getKey(potion).toString());
		return DefaultCustomIngredients.nbt(Ingredient.of(Items.POTION), nbt, false);
	}

	protected final void charmRecipe(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Supplier<? extends Item> item) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, result.get())
				.requires(item.get(), 4)
				.unlockedBy("has_item", has(item.get()))
				.save(consumer, TwilightForestMod.prefix(name));
	}

	protected final void castleBlock(Consumer<FinishedRecipe> consumer, Supplier<? extends Block> result, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 4)
				.pattern("##")
				.pattern("##")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.get()))
				.save(consumer, locCastle(BuiltInRegistries.BLOCK.getKey(result.get()).getPath()));
	}

	protected final void stairsBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, Supplier<? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 8)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria.get()))
				.save(consumer, loc);
	}

	protected final void stairsRightBlock(Consumer<FinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, Supplier<? extends Block> criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 8)
				.pattern("###")
				.pattern(" ##")
				.pattern("  #")
				.define('#', Ingredient.of(ingredients))
				.unlockedBy("has_item", has(criteria.get()))
				.save(consumer, loc);
	}

	protected final void compressedBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, TagKey<Item> ingredient) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TwilightForestMod.prefix("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> ingredient) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), 9)
				.requires(ingredient)
				.unlockedBy("has_item", has(ingredient))
				.save(consumer, TwilightForestMod.prefix("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
				.pattern("###")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void chestplateItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
				.pattern("# #")
				.pattern("###")
				.pattern("###")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void leggingsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
				.pattern("###")
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void bootsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
				.pattern("# #")
				.pattern("# #")
				.define('#', material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void pickaxeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void swordItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
				.pattern("#")
				.pattern("#")
				.pattern("X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void axeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, TagKey<Item> material, TagKey<Item> handle) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
				.pattern("##")
				.pattern("#X")
				.pattern(" X")
				.define('#', material)
				.define('X', handle)
				.unlockedBy("has_item", has(material))
				.save(consumer, locEquip(name));
	}

	protected final void buttonBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, result.get())
				.requires(material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_button"));
	}

	protected final void doorBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get(), 3)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_door"));
	}

	protected final void fenceBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
				.pattern("#S#")
				.pattern("#S#")
				.define('#', material.get())
				.define('S', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_fence"));
	}

	protected final void gateBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
				.pattern("S#S")
				.pattern("S#S")
				.define('#', material.get())
				.define('S', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_gate"));
	}

	protected final void planksBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, TagKey<Item> material) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result.get(), 4)
				.requires(material)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_planks"));
	}

	protected final void plateBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_plate"));
	}

	protected final void slabBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 6)
				.pattern("###")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_slab"));
	}

	protected final void bannerPattern(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> trophy, Supplier<? extends Item> result) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get())
				.requires(Ingredient.of(ItemTagGenerator.PAPER))
				.requires(Ingredient.of(trophy.get().asItem()))
				.unlockedBy("has_trophy", has(trophy.get()))
				.save(consumer);
	}

	protected final void trapdoorBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get(), 2)
				.pattern("###")
				.pattern("###")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_trapdoor"));
	}
	
	protected final void woodBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 3)
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_wood"));
	}

	protected final void strippedWoodBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 3)
				.pattern("##")
				.pattern("##")
				.define('#', material.get())
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_stripped_wood"));
	}

	protected final void signBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
				.pattern("###")
				.pattern("###")
				.pattern(" - ")
				.define('#', material.get())
				.define('-', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_sign"));
	}

	protected final void hangingSignBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Item> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 6)
				.pattern("| |")
				.pattern("###")
				.pattern("###")
				.define('#', material.get())
				.define('|', Items.CHAIN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_hanging_sign"));
	}

	protected final void banisterBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		this.banisterBlock(consumer, name, result, material.get());
	}

	protected final void banisterBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> result, Block material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 3)
				.pattern("---")
				.pattern("| |")
				.define('-', material)
				.define('|', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(material))
				.save(consumer, locWood(name + "_banister"));
	}

	protected final void chestBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends TFChestBlock> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 2)
				.pattern("###")
				.pattern("#C#")
				.pattern("###")
				.define('#', material.get())
				.define('C', Tags.Items.CHESTS_WOODEN)
				.unlockedBy("has_item", has(material.get()))
				.save(consumer, locWood(name + "_chest"));
	}

	protected final void fieryConversion(Consumer<FinishedRecipe> consumer, Supplier<? extends Item> result, Item armor, int vials) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, result.get())
				.requires(armor)
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), vials)
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(consumer, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(result.get()).getPath()));
	}

	protected final void buildBoats(Consumer<FinishedRecipe> consumer, Supplier<? extends Item> boat, Supplier<? extends Item> chestBoat, Supplier<? extends Block> planks) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat.get())
				.pattern("P P")
				.pattern("PPP")
				.define('P', planks.get())
				.group("boat")
				.unlockedBy("in_water", insideOf(Blocks.WATER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat.get())
				.requires(boat.get())
				.requires(Tags.Items.CHESTS_WOODEN)
				.group("chest_boat")
				.unlockedBy("has_boat", has(ItemTags.BOATS))
				.save(consumer);
	}

	protected final ResourceLocation locCastle(String name) {
		return TwilightForestMod.prefix("castleblock/" + name);
	}

	protected final ResourceLocation locEquip(String name) {
		return TwilightForestMod.prefix("equipment/" + name);
	}

	protected final ResourceLocation locNaga(String name) {
		return TwilightForestMod.prefix("nagastone/" + name);
	}

	protected final ResourceLocation locWood(String name) {
		return TwilightForestMod.prefix("wood/" + name);
	}
}
