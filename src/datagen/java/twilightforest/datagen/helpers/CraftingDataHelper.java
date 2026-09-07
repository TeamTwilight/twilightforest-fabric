package twilightforest.datagen.helpers;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import twilightforest.TFCommon;
import twilightforest.block.TFTrappedChestBlock;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;

public abstract class CraftingDataHelper extends RecipeProvider {
	public CraftingDataHelper(RecipeOutput output, HolderLookup.Provider provider) {
		super(provider, output);
	}

	protected final void charmRecipe(HolderGetter<Item> getter, String name, Item result, Item item) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.TOOLS, result)
			.requires(item, 4)
			.unlockedBy("has_item", has(item))
			.save(this.output, this.createKey(name));
	}

	protected final void castleBlock(HolderGetter<Item> getter, Block result, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 4)
			.pattern("##")
			.pattern("##")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle(BuiltInRegistries.BLOCK.getKey(result).getPath()));
	}

	protected final void woodenStairsBlock(HolderGetter<Item> getter, ResourceKey<Recipe<?>> loc, Block result, Block criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 8)
			.pattern("#  ")
			.pattern("## ")
			.pattern("###")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria))
			.group("wooden_stairs")
			.save(this.output, loc);
	}

	protected final void stairsBlock(HolderGetter<Item> getter, ResourceKey<Recipe<?>> loc, Block result, Block criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 8)
			.pattern("#  ")
			.pattern("## ")
			.pattern("###")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria))
			.save(this.output, loc);
	}

	protected final void stairsRightBlock(HolderGetter<Item> getter, ResourceKey<Recipe<?>> loc, Block result, Block criteria, ItemLike... ingredients) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 8)
			.pattern("###")
			.pattern(" ##")
			.pattern("  #")
			.define('#', Ingredient.of(ingredients))
			.unlockedBy("has_item", has(criteria))
			.save(this.output, loc);
	}

	protected final void compressedBlock(HolderGetter<Item> getter, String name, Block result, TagKey<Item> ingredient) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result)
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.define('#', ingredient)
			.unlockedBy("has_item", has(ingredient))
			.save(this.output, this.createKey("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(HolderGetter<Item> getter, String name, Item result, TagKey<Item> ingredient) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, result, 9)
			.requires(ingredient)
			.unlockedBy("has_item", has(ingredient))
			.save(this.output, this.createKey("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(HolderGetter<Item> getter, Item result, TagKey<Item> material) {
		this.helmetItem(getter, result, material, DataComponentPatch.builder());
	}

	protected final void helmetItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, result, 1)
			.pattern("###")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void chestplateItem(HolderGetter<Item> getter, Item result, TagKey<Item> material) {
		this.chestplateItem(getter, result, material, DataComponentPatch.builder());
	}

	protected final void chestplateItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, result, 1)
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void leggingsItem(HolderGetter<Item> getter, Item result, TagKey<Item> material) {
		this.leggingsItem(getter, result, material, DataComponentPatch.builder());
	}

	protected final void leggingsItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, result, 1)
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void bootsItem(HolderGetter<Item> getter, Item result, TagKey<Item> material) {
		this.bootsItem(getter, result, material, DataComponentPatch.builder());
	}

	protected final void bootsItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, result, 1)
			.pattern("# #")
			.pattern("# #")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void pickaxeItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle) {
		this.pickaxeItem(getter, result, material, handle, DataComponentPatch.builder());
	}

	protected final void pickaxeItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, result, 1)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void swordItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle) {
		this.swordItem(getter, result, material, handle, DataComponentPatch.builder());
	}

	protected final void swordItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, result, 1)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void axeItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle) {
		this.axeItem(getter, result, material, handle, DataComponentPatch.builder());
	}

	protected final void axeItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, result, 1)
			.pattern("##")
			.pattern("#X")
			.pattern(" X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void shovelItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, result, 1)
			.pattern("#")
			.pattern("X")
			.pattern("X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	protected final void hoeItem(HolderGetter<Item> getter, Item result, TagKey<Item> material, TagKey<Item> handle, DataComponentPatch.Builder component) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, result, 1)
			.pattern("##")
			.pattern(" X")
			.pattern(" X")
			.define('#', material)
			.define('X', handle)
			.unlockedBy("has_item", has(material))
			.save(this.output, locEquip(BuiltInRegistries.ITEM.getKey(result).getPath()));
	}

	@SafeVarargs
	protected final DataComponentPatch.Builder buildEnchants(HolderLookup.Provider provider, Pair<ResourceKey<Enchantment>, Integer>... enchantments) {
		HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		var itemEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (var pair : enchantments) {
			itemEnchants.set(lookup.getOrThrow(pair.getFirst()), pair.getSecond());
		}
		return DataComponentPatch.builder().set(DataComponents.ENCHANTMENTS, itemEnchants.toImmutable());
	}

	protected final void buttonBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.REDSTONE, result)
			.requires(material)
			.unlockedBy("has_item", has(material))
			.group("wooden_button")
			.save(this.output, locWood(name + "_button"));
	}

	protected final void doorBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, result, 3)
			.pattern("##")
			.pattern("##")
			.pattern("##")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("wooden_door")
			.save(this.output, locWood(name + "_door"));
	}

	protected final void fenceBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, result, 3)
			.pattern("#S#")
			.pattern("#S#")
			.define('#', material)
			.define('S', ConventionalItemTags.WOODEN_RODS)
			.unlockedBy("has_item", has(material))
			.group("wooden_fence")
			.save(this.output, locWood(name + "_fence"));
	}

	protected final void gateBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, result)
			.pattern("S#S")
			.pattern("S#S")
			.define('#', material)
			.define('S', ConventionalItemTags.WOODEN_RODS)
			.unlockedBy("has_item", has(material))
			.group("wooden_fence_gate")
			.save(this.output, locWood(name + "_gate"));
	}

	protected final void planksBlock(HolderGetter<Item> getter, String name, Block result, TagKey<Item> material) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, result, 4)
			.requires(material)
			.unlockedBy("has_item", has(material))
			.group("planks")
			.save(this.output, locWood(name + "_planks"));
	}

	protected final void plateBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, result)
			.pattern("##")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("wooden_pressure_plate")
			.save(this.output, locWood(name + "_plate"));
	}

	protected final void woodenSlabBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 6)
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("wooden_slab")
			.save(this.output, locWood(name + "_slab"));
	}

	protected final void slabBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 6)
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locWood(name + "_slab"));
	}

	protected final void bannerPattern(HolderGetter<Item> getter, String name, Block trophy, Item result) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, result)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.PAPER)))
			.requires(Ingredient.of(trophy.asItem()))
			.unlockedBy("has_trophy", has(trophy))
			.save(this.output);
	}

	protected final void trapdoorBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, result, 2)
			.pattern("###")
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("wooden_trapdoor")
			.save(this.output, locWood(name + "_trapdoor"));
	}

	protected final void woodBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 3)
			.pattern("##")
			.pattern("##")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("bark")
			.save(this.output, locWood(name + "_wood"));
	}

	protected final void strippedWoodBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, result, 3)
			.pattern("##")
			.pattern("##")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.save(this.output, locWood(name + "_stripped_wood"));
	}

	protected final void signBlock(HolderGetter<Item> getter, String name, Item result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, result, 3)
			.pattern("###")
			.pattern("###")
			.pattern(" - ")
			.define('#', material)
			.define('-', ConventionalItemTags.WOODEN_RODS)
			.unlockedBy("has_item", has(material))
			.group("wooden_sign")
			.save(this.output, locWood(name + "_sign"));
	}

	protected final void hangingSignBlock(HolderGetter<Item> getter, String name, Item result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, result, 6)
			.pattern("| |")
			.pattern("###")
			.pattern("###")
			.define('#', material)
			.define('|', Items.IRON_CHAIN)
			.unlockedBy("has_item", has(material))
			.group("hanging_sign")
			.save(this.output, locWood(name + "_hanging_sign"));
	}

	protected final void banisterBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, result, 3)
			.pattern("---")
			.pattern("| |")
			.define('-', material)
			.define('|', ConventionalItemTags.WOODEN_RODS)
			.unlockedBy("has_item", has(material))
			.group("wooden_banister")
			.save(this.output, locWood(name + "_banister"));
	}

	protected final void dryingRackBlock(HolderGetter<Item> getter, String name, Block result, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, result)
			.pattern("---")
			.define('-', material)
			.unlockedBy("has_item", has(material))
			.group("drying_rack")
			.save(this.output, locWood(name + "_drying_rack"));
	}

	protected final void chestBlock(HolderGetter<Item> getter, String name, ChestBlock chest, TFTrappedChestBlock trapped, Block material) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, chest)
			.pattern("###")
			.pattern("# #")
			.pattern("###")
			.define('#', material)
			.unlockedBy("has_item", has(material))
			.group("chest")
			.save(this.output, locWood(name + "_chest"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.REDSTONE, trapped)
			.requires(chest)
			.requires(Items.TRIPWIRE_HOOK)
			.unlockedBy("has_item", has(material))
			.group("trapped_chest")
			.save(this.output, locWood(name + "_trapped_chest"));
	}

	protected final void fieryConversion(HolderGetter<Item> getter, Item result, Item armor, int vials) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.COMBAT, result)
			.requires(armor)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.FIERY_VIAL)), vials)
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group(BuiltInRegistries.ITEM.getKey(result).getPath())
			.save(this.output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(armor).getPath()));
	}

	protected final void buildBoats(HolderGetter<Item> getter, Item boat, Item chestBoat, Block planks) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, boat)
			.pattern("P P")
			.pattern("PPP")
			.define('P', planks)
			.group("boat")
			.unlockedBy("in_water", insideOf(Blocks.WATER))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.TRANSPORTATION, chestBoat)
			.requires(boat)
			.requires(ConventionalItemTags.WOODEN_CHESTS)
			.group("chest_boat")
			.unlockedBy("has_boat", has(ItemTags.BOATS))
			.save(this.output);
	}

	protected final ResourceKey<Recipe<?>> locCastle(String name) {
		return this.createKey("castleblock/" + name);
	}

	protected final ResourceKey<Recipe<?>> locEquip(String name) {
		return this.createKey("equipment/" + name);
	}

	protected final ResourceKey<Recipe<?>> locNaga(String name) {
		return this.createKey("nagastone/" + name);
	}

	protected final ResourceKey<Recipe<?>> locWood(String name) {
		return this.createKey("wood/" + name);
	}

	protected ResourceKey<Recipe<?>> createKey(String name) {
		return ResourceKey.create(Registries.RECIPE, TFCommon.prefix(name));
	}
}
