//package twilightforest.data;
//
//import com.google.gson.JsonObject;
//
//import net.minecraft.tags.ItemTags;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.data.*;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.SimpleCookingSerializer;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraftforge.common.Tags;
//import net.minecraftforge.common.crafting.ConditionalRecipe;
//import twilightforest.TwilightForestMod;
//import twilightforest.block.TFBlocks;
//import twilightforest.item.TFItems;
//import twilightforest.item.recipe.UncraftingEnabledCondition;
//
//import java.nio.file.Path;
//import java.util.function.Consumer;
//
//import net.minecraft.data.recipes.FinishedRecipe;
//import net.minecraft.data.recipes.ShapedRecipeBuilder;
//import net.minecraft.data.recipes.ShapelessRecipeBuilder;
//import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
//import net.minecraft.data.recipes.UpgradeRecipeBuilder;
//
//public class CraftingGenerator extends CraftingDataHelper {
//	public CraftingGenerator(DataGenerator generator) {
//		super(generator);
//	}
//
//	@Override
//	protected void saveAdvancement(HashCache p_208310_1_, JsonObject p_208310_2_, Path p_208310_3_) {
//		//Silence. This just makes it so that we don't gen advancements
//		//TODO Recipe advancements control the unlock of a recipe, so if we ever consider actually making them, recipes should unlock based on also possible prerequisite conditions, instead of ONLY obtaining the item itself
//	}
//
//	@Override
//	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
//		// The Recipe Builder currently doesn't support enchantment-resulting recipes, those must be manually created.
//		blockCompressionRecipes(consumer);
//		equipmentRecipes(consumer);
//		emptyMapRecipes(consumer);
//		woodRecipes(consumer);
//		fieryConversions(consumer);
//
//		nagastoneRecipes(consumer);
//		darkTowerRecipes(consumer);
//		castleRecipes(consumer);
//
//		bannerPattern(consumer, "naga_banner_pattern", TFBlocks.naga_trophy, TFItems.naga_banner_pattern);
//		bannerPattern(consumer, "lich_banner_pattern", TFBlocks.lich_trophy, TFItems.lich_banner_pattern);
//		bannerPattern(consumer, "minoshroom_banner_pattern", TFBlocks.minoshroom_trophy, TFItems.minoshroom_banner_pattern);
//		bannerPattern(consumer, "hydra_banner_pattern", TFBlocks.hydra_trophy, TFItems.hydra_banner_pattern);
//		bannerPattern(consumer, "knight_phantom_banner_pattern", TFBlocks.knight_phantom_trophy, TFItems.knight_phantom_banner_pattern);
//		bannerPattern(consumer, "ur_ghast_banner_pattern", TFBlocks.ur_ghast_trophy, TFItems.ur_ghast_banner_pattern);
//		bannerPattern(consumer, "alpha_yeti_banner_pattern", TFBlocks.yeti_trophy, TFItems.alpha_yeti_banner_pattern);
//		bannerPattern(consumer, "snow_queen_banner_pattern", TFBlocks.snow_queen_trophy, TFItems.snow_queen_banner_pattern);
//		bannerPattern(consumer, "questing_ram_banner_pattern", TFBlocks.quest_ram_trophy, TFItems.questing_ram_banner_pattern);
//
//		slabBlock(consumer, "aurora_slab", TFBlocks.aurora_slab, TFBlocks.aurora_block);
//		ShapedRecipeBuilder.shaped(TFBlocks.aurora_pillar, 2)
//				.pattern("#")
//				.pattern("#")
//				.define('#', Ingredient.of(TFBlocks.aurora_block))
//				.unlockedBy("has_" + TFBlocks.aurora_pillar.getId().getPath(), has(TFBlocks.aurora_pillar))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.iron_ladder, 3)
//				.pattern("-#-")
//				.pattern("-#-")
//				.define('#', Ingredient.of(Blocks.IRON_BARS))
//				.define('-', Tags.Items.NUGGETS_IRON)
//				.unlockedBy("has_" + TFBlocks.iron_ladder.getId().getPath(), has(TFBlocks.iron_ladder))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.firefly_jar)
//				.requires(Ingredient.of(TFBlocks.firefly))
//				.requires(Ingredient.of(Items.GLASS_BOTTLE))
//				.unlockedBy("has_item", has(TFBlocks.firefly))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.firefly_spawner)
//				.requires(Ingredient.of(TFBlocks.firefly_jar))
//				.requires(Ingredient.of(TFBlocks.firefly))
//				.requires(Ingredient.of(Blocks.POPPY))
//				.unlockedBy("has_jar", has(TFBlocks.firefly_jar))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.cicada_jar)
//				.requires(Ingredient.of(TFBlocks.cicada))
//				.requires(Ingredient.of(Items.GLASS_BOTTLE))
//				.unlockedBy("has_item", has(TFBlocks.cicada))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(Items.STICK)
//				.requires(Ingredient.of(TFBlocks.root_strand))
//				.unlockedBy("has_item", has(TFBlocks.root_strand))
//				.save(consumer, TwilightForestMod.prefix("root_stick"));
//
//		ShapedRecipeBuilder.shaped(Blocks.TORCH, 5)
//				.pattern("∴")
//				.pattern("|")
//				.define('∴', Ingredient.of(TFItems.torchberries))
//				.define('|', Tags.Items.RODS_WOODEN)
//				.unlockedBy("has_item", has(TFItems.torchberries))
//				.save(consumer, TwilightForestMod.prefix("berry_torch"));
//
//		ConditionalRecipe.builder()
//				.addCondition(new UncraftingEnabledCondition())
//				.addRecipe(ShapedRecipeBuilder.shaped(TFBlocks.uncrafting_table)
//						.pattern("###")
//						.pattern("#X#")
//						.pattern("###")
//						.define('#', Blocks.CRAFTING_TABLE)
//						.define('X', TFItems.maze_map_focus)
//						.unlockedBy("has_uncrafting_table", has(TFBlocks.uncrafting_table))
//						::save)
//				.build(consumer, TwilightForestMod.prefix("uncrafting_table"));
//
//		// Patchouli books would also go here, except they also must craft-result with NBT data.
//
//		cookingRecipes(consumer, "smelted", RecipeSerializer.SMELTING_RECIPE, 200);
//		cookingRecipes(consumer, "smoked", RecipeSerializer.SMOKING_RECIPE, 100);
//		cookingRecipes(consumer, "campfired", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, 600);
//
//		ingotRecipes(consumer, "smelted", RecipeSerializer.SMELTING_RECIPE, 200);
//		ingotRecipes(consumer, "blasted", RecipeSerializer.BLASTING_RECIPE, 100);
//
//		crackedWoodRecipes(consumer, "smoked", RecipeSerializer.SMOKING_RECIPE, 100);
//		crackedStoneRecipes(consumer, "smelted", RecipeSerializer.SMELTING_RECIPE, 200);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.empty_bookshelf)
//				.pattern("---")
//				.pattern("   ")
//				.pattern("---")
//				.define('-', TFBlocks.canopy_slab)
//				.unlockedBy("has_item", has(TFBlocks.canopy_slab))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.canopy_bookshelf)
//				.pattern("---")
//				.pattern("BBB")
//				.pattern("---")
//				.define('-', TFBlocks.canopy_planks)
//				.define('B', Items.BOOK)
//				.unlockedBy("has_item", has(TFBlocks.canopy_planks))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(TFItems.armor_shard_cluster)
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.requires(Ingredient.of(TFItems.armor_shard))
//				.unlockedBy("has_item", has(TFItems.armor_shard))
//				.save(consumer, TwilightForestMod.prefix("material/" + TFItems.armor_shard_cluster.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.underbrick_mossy, 1)
//				.requires(Ingredient.of(Blocks.VINE))
//				.requires(Ingredient.of(TFBlocks.underbrick))
//				.unlockedBy("has_item", has(TFBlocks.underbrick))
//				.save(consumer, TwilightForestMod.prefix("underbrick_mossy"));
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.maze_stone_mossy, 1)
//				.requires(Ingredient.of(Blocks.VINE))
//				.requires(Ingredient.of(TFBlocks.maze_stone_brick))
//				.unlockedBy("has_item", has(TFBlocks.maze_stone_brick))
//				.save(consumer, TwilightForestMod.prefix("maze_stone/maze_stone_mossy"));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.carminite)
//				.requires(Ingredient.of(TFItems.borer_essence))
//				.requires(Ingredient.of(TFItems.borer_essence))
//				.requires(Ingredient.of(TFItems.borer_essence))
//				.requires(Ingredient.of(TFItems.borer_essence))
//				.requires(Ingredient.of(Items.GHAST_TEAR))
//				.requires(Items.REDSTONE)
//				.requires(Items.REDSTONE)
//				.requires(Items.REDSTONE)
//				.requires(Items.REDSTONE)
//				.unlockedBy("has_item", has(TFItems.borer_essence))
//				.save(consumer, TwilightForestMod.prefix("material/" + TFItems.carminite.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.ironwood_raw, 2)
//				.requires(Ingredient.of(TFItems.liveroot))
//				.requires(Ingredient.of(Items.RAW_IRON))
//				.requires(Items.GOLD_NUGGET)
//				.unlockedBy("has_item", has(TFItems.liveroot))
//				.save(consumer, TwilightForestMod.prefix("material/" + TFItems.ironwood_raw.getId().getPath()));
//	}
//
//	private void darkTowerRecipes(Consumer<FinishedRecipe> consumer) {
//		ShapedRecipeBuilder.shaped(TFBlocks.encased_fire_jet)
//				.pattern("#∴#")
//				.pattern("∴^∴")
//				.pattern("uuu")
//				.define('∴', Items.REDSTONE)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('^', Ingredient.of(TFBlocks.fire_jet))
//				.define('u', Ingredient.of(Items.LAVA_BUCKET))
//				.unlockedBy("has_item", has(TFBlocks.fire_jet))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.encased_smoker)
//				.pattern("#∴#")
//				.pattern("∴^∴")
//				.pattern("#∴#")
//				.define('∴', Items.REDSTONE)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('^', Ingredient.of(TFBlocks.smoker))
//				.unlockedBy("has_item", has(TFBlocks.smoker))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.carminite_builder)
//				.pattern("#6#")
//				.pattern("6o6")
//				.pattern("#6#")
//				.define('6', ItemTagGenerator.CARMINITE_GEMS)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('o', Ingredient.of(Blocks.DISPENSER))
//				.unlockedBy("has_item", has(TFItems.carminite))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.carminite_reactor)
//				.pattern("#6#")
//				.pattern("6%6")
//				.pattern("#6#")
//				.define('6', ItemTagGenerator.CARMINITE_GEMS)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('%', ItemTags.REDSTONE_ORES)
//				.unlockedBy("has_item", has(TFBlocks.carminite_reactor))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.reappearing_block, 2)
//				.pattern("#∴#")
//				.pattern("∴6∴")
//				.pattern("#∴#")
//				.define('∴', Items.REDSTONE)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('6', ItemTagGenerator.CARMINITE_GEMS)
//				.unlockedBy("has_item", has(TFBlocks.reappearing_block))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFBlocks.vanishing_block, 8)
//				.pattern("#w#")
//				.pattern("w6w")
//				.pattern("#w#")
//				.define('w', ItemTagGenerator.TOWERWOOD)
//				.define('#', Ingredient.of(TFBlocks.tower_wood_encased))
//				.define('6', ItemTagGenerator.CARMINITE_GEMS)
//				.unlockedBy("has_item", has(TFBlocks.reappearing_block))
//				.save(consumer);
//		ShapelessRecipeBuilder.shapeless(TFBlocks.tower_wood_mossy)
//			.requires(Ingredient.of(TFBlocks.tower_wood))
//			.requires(Items.VINE)
//			.unlockedBy("has_item", has(TFBlocks.tower_wood))
//			.save(consumer, TwilightForestMod.prefix("wood/" + TFBlocks.tower_wood_mossy.getId().getPath()));
//
//	}
//
//	private void equipmentRecipes(Consumer<FinishedRecipe> consumer) {
//		bootsItem(consumer, "arctic_boots", TFItems.arctic_boots, ItemTagGenerator.ARCTIC_FUR);
//		chestplateItem(consumer, "arctic_chestplate", TFItems.arctic_chestplate, ItemTagGenerator.ARCTIC_FUR);
//		helmetItem(consumer, "arctic_helmet", TFItems.arctic_helmet, ItemTagGenerator.ARCTIC_FUR);
//		leggingsItem(consumer, "arctic_leggings", TFItems.arctic_leggings, ItemTagGenerator.ARCTIC_FUR);
//
//		bootsItem(consumer, "fiery_boots", TFItems.fiery_boots, ItemTagGenerator.FIERY_INGOTS);
//		chestplateItem(consumer, "fiery_chestplate", TFItems.fiery_chestplate, ItemTagGenerator.FIERY_INGOTS);
//		helmetItem(consumer, "fiery_helmet", TFItems.fiery_helmet, ItemTagGenerator.FIERY_INGOTS);
//		leggingsItem(consumer, "fiery_leggings", TFItems.fiery_leggings, ItemTagGenerator.FIERY_INGOTS);
//		swordItem(consumer, "fiery_sword", TFItems.fiery_sword, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);
//		pickaxeItem(consumer, "fiery_pickaxe", TFItems.fiery_pickaxe, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);
//
//		bootsItem(consumer, "knightmetal_boots", TFItems.knightmetal_boots, ItemTagGenerator.KNIGHTMETAL_INGOTS);
//		chestplateItem(consumer, "knightmetal_chestplate", TFItems.knightmetal_chestplate, ItemTagGenerator.KNIGHTMETAL_INGOTS);
//		helmetItem(consumer, "knightmetal_helmet", TFItems.knightmetal_helmet, ItemTagGenerator.KNIGHTMETAL_INGOTS);
//		leggingsItem(consumer, "knightmetal_leggings", TFItems.knightmetal_leggings, ItemTagGenerator.KNIGHTMETAL_INGOTS);
//		pickaxeItem(consumer, "knightmetal_pickaxe", TFItems.knightmetal_pickaxe, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
//		swordItem(consumer, "knightmetal_sword", TFItems.knightmetal_sword, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
//		axeItem(consumer, "knightmetal_axe", TFItems.knightmetal_axe, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
//
//		ShapedRecipeBuilder.shaped(TFItems.giant_pickaxe)
//				.pattern("###")
//				.pattern(" X ")
//				.pattern(" X ")
//				.define('#', TFBlocks.giant_cobblestone)
//				.define('X', TFBlocks.giant_log)
//				.unlockedBy("has_item", has(TFBlocks.giant_cobblestone))
//				.save(consumer, locEquip(TFItems.giant_pickaxe.getId().getPath()));
//
//		ShapedRecipeBuilder.shaped(TFItems.giant_sword)
//				.pattern(" # ")
//				.pattern(" # ")
//				.pattern(" X ")
//				.define('#', TFBlocks.giant_cobblestone)
//				.define('X', TFBlocks.giant_log)
//				.unlockedBy("has_item", has(TFBlocks.giant_cobblestone))
//				.save(consumer, locEquip(TFItems.giant_sword.getId().getPath()));
//
//		charmRecipe(consumer, "charm_of_keeping_2", TFItems.charm_of_keeping_2, TFItems.charm_of_keeping_1);
//		charmRecipe(consumer, "charm_of_keeping_3", TFItems.charm_of_keeping_3, TFItems.charm_of_keeping_2);
//		charmRecipe(consumer, "charm_of_life_2", TFItems.charm_of_life_2, TFItems.charm_of_life_1);
//
//		ShapelessRecipeBuilder.shapeless(TFItems.moonworm_queen)
//				.requires(TFItems.moonworm_queen)
//				.requires(TFItems.torchberries, 3)
//				.unlockedBy("has_item", has(TFItems.moonworm_queen))
//				.save(consumer, TwilightForestMod.prefix("moonworm_queen"));
//
//		ShapelessRecipeBuilder.shapeless(Blocks.COBBLESTONE, 64)
//				.requires(TFBlocks.giant_cobblestone)
//				.unlockedBy("has_item", has(TFBlocks.giant_cobblestone))
//				.save(consumer, TwilightForestMod.prefix(TFBlocks.giant_cobblestone.getId().getPath() + "_to_" + Blocks.COBBLESTONE.asItem().getRegistryName().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(Blocks.OAK_PLANKS, 64)
//				.requires(TFBlocks.giant_log)
//				.unlockedBy("has_item", has(TFBlocks.giant_log))
//				.save(consumer, TwilightForestMod.prefix(TFBlocks.giant_log.getId().getPath() + "_to_" + Blocks.OAK_PLANKS.asItem().getRegistryName().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(Blocks.OAK_LEAVES, 64)
//				.requires(TFBlocks.giant_leaves)
//				.unlockedBy("has_item", has(TFBlocks.giant_log))
//				.save(consumer, TwilightForestMod.prefix(TFBlocks.giant_leaves.getId().getPath() + "_to_" + Blocks.OAK_LEAVES.asItem().getRegistryName().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.block_and_chain)
//				.requires(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL)
//				.requires(ItemTagGenerator.KNIGHTMETAL_INGOTS)
//				.requires(ItemTagGenerator.KNIGHTMETAL_INGOTS)
//				.requires(ItemTagGenerator.KNIGHTMETAL_INGOTS)
//				.requires(Ingredient.of(TFItems.knightmetal_ring))
//				.unlockedBy("has_item", has(TFBlocks.knightmetal_block))
//				.save(consumer, locEquip(TFItems.block_and_chain.getId().getPath()));
//
//		ShapedRecipeBuilder.shaped(TFItems.knightmetal_ring)
//				.pattern(" - ")
//				.pattern("- -")
//				.pattern(" - ")
//				.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
//				.unlockedBy("has_item", has(TFItems.knightmetal_ingot))
//				.save(consumer, locEquip(TFItems.knightmetal_ring.getId().getPath()));
//
//		ShapedRecipeBuilder.shaped(TFItems.knightmetal_shield)
//				.pattern("-#")
//				.pattern("-o")
//				.pattern("-#")
//				.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
//				.define('#', ItemTagGenerator.TOWERWOOD)
//				.define('o', Ingredient.of(TFItems.knightmetal_ring))
//				.unlockedBy("has_item", has(TFItems.knightmetal_ingot))
//				.save(consumer, locEquip(TFItems.knightmetal_shield.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.lifedrain_scepter)
//				.requires(itemWithNBT(TFItems.lifedrain_scepter, nbt -> nbt.putInt("Damage", TFItems.lifedrain_scepter.getMaxDamage())))
//				.requires(Ingredient.of(Items.FERMENTED_SPIDER_EYE))
//				.unlockedBy("has_item", has(TFItems.lifedrain_scepter))
//				.save(consumer, locEquip(TFItems.lifedrain_scepter.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.shield_scepter)
//				.requires(itemWithNBT(TFItems.shield_scepter, nbt -> nbt.putInt("Damage", TFItems.shield_scepter.getMaxDamage())))
//				.requires(Ingredient.of(Items.GOLDEN_APPLE))
//				.unlockedBy("has_item", has(TFItems.shield_scepter))
//				.save(consumer, locEquip(TFItems.shield_scepter.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.twilight_scepter)
//				.requires(itemWithNBT(TFItems.twilight_scepter, nbt -> nbt.putInt("Damage", TFItems.twilight_scepter.getMaxDamage())))
//				.requires(Tags.Items.ENDER_PEARLS)
//				.unlockedBy("has_item", has(TFItems.twilight_scepter))
//				.save(consumer, locEquip(TFItems.twilight_scepter.getId().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.zombie_scepter)
//				.requires(multipleIngredients(
//						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:strength")),
//						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:strong_strength")),
//						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:long_strength"))
//				))
//				.requires(itemWithNBT(TFItems.zombie_scepter, nbt -> nbt.putInt("Damage", TFItems.zombie_scepter.getMaxDamage())))
//				.requires(Ingredient.of(Items.ROTTEN_FLESH))
//				.unlockedBy("has_item", has(TFItems.zombie_scepter))
//				.save(consumer, locEquip(TFItems.zombie_scepter.getId().getPath()));
//
//		// Testing
//		//ShapelessRecipeBuilder.shapelessRecipe(TFItems.zombie_scepter)
//		//		.addIngredient(multipleIngredients(
//		//				Ingredient.fromTag(Tags.Items.GEMS_DIAMOND),
//		//				Ingredient.fromItems(Items.BEDROCK)
//		//		))
//		//		.addIngredient(itemWithNBT(TFItems.zombie_scepter, nbt -> nbt.putInt("Damage", TFItems.zombie_scepter.getMaxDamage())))
//		//		.addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH))
//		//		.addIngredient(Tags.Items.GEMS_EMERALD)
//		//		.addCriterion("has_item", hasItem(TFItems.zombie_scepter))
//		//		.build(consumer, locEquip(TFItems.zombie_scepter.getId().getPath() + "_rv"));
//	}
//
//	private void blockCompressionRecipes(Consumer<FinishedRecipe> consumer) {
//		reverseCompressBlock(consumer, "arctic_block_to_item", TFItems.arctic_fur, ItemTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR);
//		reverseCompressBlock(consumer, "carminite_block_to_item", TFItems.carminite, ItemTagGenerator.STORAGE_BLOCKS_CARMINITE);
//		reverseCompressBlock(consumer, "fiery_block_to_ingot", TFItems.fiery_ingot, ItemTagGenerator.STORAGE_BLOCKS_FIERY);
//		reverseCompressBlock(consumer, "ironwood_block_ingot", TFItems.ironwood_ingot, ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD);
//		reverseCompressBlock(consumer, "knightmetal_block_ingot", TFItems.knightmetal_ingot,ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL);
//		reverseCompressBlock(consumer, "steeleaf_block_ingot", TFItems.steeleaf_ingot, ItemTagGenerator.STORAGE_BLOCKS_STEELEAF);
//
//		compressedBlock(consumer, "arctic_block", TFBlocks.arctic_fur_block, ItemTagGenerator.ARCTIC_FUR);
//		compressedBlock(consumer, "carminite_block", TFBlocks.carminite_block, ItemTagGenerator.CARMINITE_GEMS);
//		compressedBlock(consumer, "fiery_block", TFBlocks.fiery_block, ItemTagGenerator.FIERY_INGOTS);
//		compressedBlock(consumer, "ironwood_block", TFBlocks.ironwood_block, ItemTagGenerator.IRONWOOD_INGOTS);
//		compressedBlock(consumer, "knightmetal_block", TFBlocks.knightmetal_block, ItemTagGenerator.KNIGHTMETAL_INGOTS);
//		compressedBlock(consumer, "steeleaf_block", TFBlocks.steeleaf_block, ItemTagGenerator.STEELEAF_INGOTS);
//	}
//
//	private void emptyMapRecipes(Consumer<FinishedRecipe> consumer) {
//		ShapelessRecipeBuilder.shapeless(TFItems.magic_map_focus)
//				.requires(TFItems.raven_feather)
//				.requires(TFItems.torchberries)
//				.requires(Tags.Items.DUSTS_GLOWSTONE)
//				.unlockedBy("has_item", has(TFItems.torchberries))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFItems.magic_map_empty)
//				.pattern("###")
//				.pattern("#•#")
//				.pattern("###")
//				.define('#', ItemTagGenerator.PAPER)
//				.define('•', Ingredient.of(TFItems.magic_map_focus))
//				.unlockedBy("has_item", has(TFItems.magic_map_focus))
//				.save(consumer);
//
//		ShapedRecipeBuilder.shaped(TFItems.maze_map_empty)
//				.pattern("###")
//				.pattern("#•#")
//				.pattern("###")
//				.define('#', ItemTagGenerator.PAPER)
//				.define('•', Ingredient.of(TFItems.maze_map_focus))
//				.unlockedBy("has_item", has(TFItems.maze_map_focus))
//				.save(consumer);
//
//		ShapelessRecipeBuilder.shapeless(TFItems.ore_map_empty)
//				.requires(TFItems.maze_map_empty)
//				.requires(Tags.Items.STORAGE_BLOCKS_DIAMOND)
//				.requires(Tags.Items.STORAGE_BLOCKS_GOLD)
//				.requires(Tags.Items.STORAGE_BLOCKS_IRON)
//				.unlockedBy("has_item", has(TFItems.ore_magnet))
//				.save(consumer);
//	}
//
//	private void woodRecipes(Consumer<FinishedRecipe> consumer) {
//		buttonBlock(consumer, "canopy", TFBlocks.canopy_button, TFBlocks.canopy_planks);
//		buttonBlock(consumer, "darkwood", TFBlocks.dark_button, TFBlocks.dark_planks);
//		buttonBlock(consumer, "mangrove", TFBlocks.mangrove_button, TFBlocks.mangrove_planks);
//		buttonBlock(consumer, "mine", TFBlocks.mine_button, TFBlocks.mine_planks);
//		buttonBlock(consumer, "sort", TFBlocks.sort_button, TFBlocks.sort_planks);
//		buttonBlock(consumer, "time", TFBlocks.time_button, TFBlocks.time_planks);
//		buttonBlock(consumer, "trans", TFBlocks.trans_button, TFBlocks.trans_planks);
//		buttonBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_button, TFBlocks.twilight_oak_planks);
//
//		doorBlock(consumer, "canopy", TFBlocks.canopy_door, TFBlocks.canopy_planks);
//		doorBlock(consumer, "darkwood", TFBlocks.dark_door, TFBlocks.dark_planks);
//		doorBlock(consumer, "mangrove", TFBlocks.mangrove_door, TFBlocks.mangrove_planks);
//		doorBlock(consumer, "mine", TFBlocks.mine_door, TFBlocks.mine_planks);
//		doorBlock(consumer, "sort", TFBlocks.sort_door, TFBlocks.sort_planks);
//		doorBlock(consumer, "time", TFBlocks.time_door, TFBlocks.time_planks);
//		doorBlock(consumer, "trans", TFBlocks.trans_door, TFBlocks.trans_planks);
//		doorBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_door, TFBlocks.twilight_oak_planks);
//
//		fenceBlock(consumer, "canopy", TFBlocks.canopy_fence, TFBlocks.canopy_planks);
//		fenceBlock(consumer, "darkwood", TFBlocks.dark_fence, TFBlocks.dark_planks);
//		fenceBlock(consumer, "mangrove", TFBlocks.mangrove_fence, TFBlocks.mangrove_planks);
//		fenceBlock(consumer, "mine", TFBlocks.mine_fence, TFBlocks.mine_planks);
//		fenceBlock(consumer, "sort", TFBlocks.sort_fence, TFBlocks.sort_planks);
//		fenceBlock(consumer, "time", TFBlocks.time_fence, TFBlocks.time_planks);
//		fenceBlock(consumer, "trans", TFBlocks.trans_fence, TFBlocks.trans_planks);
//		fenceBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_fence, TFBlocks.twilight_oak_planks);
//
//		gateBlock(consumer, "canopy", TFBlocks.canopy_gate, TFBlocks.canopy_planks);
//		gateBlock(consumer, "darkwood", TFBlocks.dark_gate, TFBlocks.dark_planks);
//		gateBlock(consumer, "mangrove", TFBlocks.mangrove_gate, TFBlocks.mangrove_planks);
//		gateBlock(consumer, "mine", TFBlocks.mine_gate, TFBlocks.mine_planks);
//		gateBlock(consumer, "sort", TFBlocks.sort_gate, TFBlocks.sort_planks);
//		gateBlock(consumer, "time", TFBlocks.time_gate, TFBlocks.time_planks);
//		gateBlock(consumer, "trans", TFBlocks.trans_gate, TFBlocks.trans_planks);
//		gateBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_gate, TFBlocks.twilight_oak_planks);
//
//		planksBlock(consumer, "canopy", TFBlocks.canopy_planks, TFBlocks.canopy_log);
//		planksBlock(consumer, "darkwood", TFBlocks.dark_planks, TFBlocks.dark_log);
//		planksBlock(consumer, "mangrove", TFBlocks.mangrove_planks, TFBlocks.mangrove_log);
//		planksBlock(consumer, "mine", TFBlocks.mine_planks, TFBlocks.mining_log);
//		planksBlock(consumer, "sort", TFBlocks.sort_planks, TFBlocks.sorting_log);
//		planksBlock(consumer, "time", TFBlocks.time_planks, TFBlocks.time_log);
//		planksBlock(consumer, "trans", TFBlocks.trans_planks, TFBlocks.transformation_log);
//		planksBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_planks, TFBlocks.oak_log);
//
//		planksBlock(consumer, "canopy_from_stripped", TFBlocks.canopy_planks, TFBlocks.stripped_canopy_log);
//		planksBlock(consumer, "darkwood_from_stripped", TFBlocks.dark_planks, TFBlocks.stripped_dark_log);
//		planksBlock(consumer, "mangrove_from_stripped", TFBlocks.mangrove_planks, TFBlocks.stripped_mangrove_log);
//		planksBlock(consumer, "mine_from_stripped", TFBlocks.mine_planks, TFBlocks.stripped_mining_log);
//		planksBlock(consumer, "sort_from_stripped", TFBlocks.sort_planks, TFBlocks.stripped_sorting_log);
//		planksBlock(consumer, "time_from_stripped", TFBlocks.time_planks, TFBlocks.stripped_time_log);
//		planksBlock(consumer, "trans_from_stripped", TFBlocks.trans_planks, TFBlocks.stripped_transformation_log);
//		planksBlock(consumer, "twilight_oak_from_stripped", TFBlocks.twilight_oak_planks, TFBlocks.stripped_oak_log);
//
//		planksBlock(consumer, "canopy_from_wood", TFBlocks.canopy_planks, TFBlocks.canopy_wood);
//		planksBlock(consumer, "darkwood_from_wood", TFBlocks.dark_planks, TFBlocks.dark_wood);
//		planksBlock(consumer, "mangrove_from_wood", TFBlocks.mangrove_planks, TFBlocks.mangrove_wood);
//		planksBlock(consumer, "mine_from_wood", TFBlocks.mine_planks, TFBlocks.mining_wood);
//		planksBlock(consumer, "sort_from_wood", TFBlocks.sort_planks, TFBlocks.sorting_wood);
//		planksBlock(consumer, "time_from_wood", TFBlocks.time_planks, TFBlocks.time_wood);
//		planksBlock(consumer, "trans_from_wood", TFBlocks.trans_planks, TFBlocks.transformation_wood);
//		planksBlock(consumer, "twilight_oak_from_wood", TFBlocks.twilight_oak_planks, TFBlocks.oak_wood);
//
//		planksBlock(consumer, "canopy_from_stripped_wood", TFBlocks.canopy_planks, TFBlocks.stripped_canopy_wood);
//		planksBlock(consumer, "darkwood_from_stripped_wood", TFBlocks.dark_planks, TFBlocks.stripped_dark_wood);
//		planksBlock(consumer, "mangrove_from_stripped_wood", TFBlocks.mangrove_planks, TFBlocks.stripped_mangrove_wood);
//		planksBlock(consumer, "mine_from_stripped_wood", TFBlocks.mine_planks, TFBlocks.stripped_mining_wood);
//		planksBlock(consumer, "sort_from_stripped_wood", TFBlocks.sort_planks, TFBlocks.stripped_sorting_wood);
//		planksBlock(consumer, "time_from_stripped_wood", TFBlocks.time_planks, TFBlocks.stripped_time_wood);
//		planksBlock(consumer, "trans_from_stripped_wood", TFBlocks.trans_planks, TFBlocks.stripped_transformation_wood);
//		planksBlock(consumer, "twilight_oak_from_stripped_wood", TFBlocks.twilight_oak_planks, TFBlocks.stripped_oak_wood);
//
//		woodBlock(consumer, "canopy", TFBlocks.canopy_wood, TFBlocks.canopy_log);
//		woodBlock(consumer, "darkwood", TFBlocks.dark_wood, TFBlocks.dark_log);
//		woodBlock(consumer, "mangrove", TFBlocks.mangrove_wood, TFBlocks.mangrove_log);
//		woodBlock(consumer, "mine", TFBlocks.mining_wood, TFBlocks.mining_log);
//		woodBlock(consumer, "sort", TFBlocks.sorting_wood, TFBlocks.sorting_log);
//		woodBlock(consumer, "time", TFBlocks.time_wood, TFBlocks.time_log);
//		woodBlock(consumer, "trans", TFBlocks.transformation_wood, TFBlocks.transformation_log);
//		woodBlock(consumer, "twilight_oak", TFBlocks.oak_wood, TFBlocks.oak_log);
//
//		strippedWoodBlock(consumer, "canopy", TFBlocks.stripped_canopy_wood, TFBlocks.stripped_canopy_log);
//		strippedWoodBlock(consumer, "darkwood", TFBlocks.stripped_dark_wood, TFBlocks.stripped_dark_log);
//		strippedWoodBlock(consumer, "mangrove", TFBlocks.stripped_mangrove_wood, TFBlocks.stripped_mangrove_log);
//		strippedWoodBlock(consumer, "mine", TFBlocks.stripped_mining_wood, TFBlocks.stripped_mining_log);
//		strippedWoodBlock(consumer, "sort", TFBlocks.stripped_sorting_wood, TFBlocks.stripped_sorting_log);
//		strippedWoodBlock(consumer, "time", TFBlocks.stripped_time_wood, TFBlocks.stripped_time_log);
//		strippedWoodBlock(consumer, "trans", TFBlocks.stripped_transformation_wood, TFBlocks.stripped_transformation_log);
//		strippedWoodBlock(consumer, "twilight_oak", TFBlocks.stripped_oak_wood, TFBlocks.stripped_oak_log);
//
//		plateBlock(consumer, "canopy", TFBlocks.canopy_plate, TFBlocks.canopy_planks);
//		plateBlock(consumer, "darkwood", TFBlocks.dark_plate, TFBlocks.dark_planks);
//		plateBlock(consumer, "mangrove", TFBlocks.mangrove_plate, TFBlocks.mangrove_planks);
//		plateBlock(consumer, "mine", TFBlocks.mine_plate, TFBlocks.mine_planks);
//		plateBlock(consumer, "sort", TFBlocks.sort_plate, TFBlocks.sort_planks);
//		plateBlock(consumer, "time", TFBlocks.time_plate, TFBlocks.time_planks);
//		plateBlock(consumer, "trans", TFBlocks.trans_plate, TFBlocks.trans_planks);
//		plateBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_plate, TFBlocks.twilight_oak_planks);
//
//		slabBlock(consumer, "canopy", TFBlocks.canopy_slab, TFBlocks.canopy_planks);
//		slabBlock(consumer, "darkwood", TFBlocks.dark_slab, TFBlocks.dark_planks);
//		slabBlock(consumer, "mangrove", TFBlocks.mangrove_slab, TFBlocks.mangrove_planks);
//		slabBlock(consumer, "mine", TFBlocks.mine_slab, TFBlocks.mine_planks);
//		slabBlock(consumer, "sort", TFBlocks.sort_slab, TFBlocks.sort_planks);
//		slabBlock(consumer, "time", TFBlocks.time_slab, TFBlocks.time_planks);
//		slabBlock(consumer, "trans", TFBlocks.trans_slab, TFBlocks.trans_planks);
//		slabBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_slab, TFBlocks.twilight_oak_planks);
//
//		stairsBlock(consumer, locWood("canopy_stairs"), TFBlocks.canopy_stairs, TFBlocks.canopy_planks, TFBlocks.canopy_planks);
//		stairsBlock(consumer, locWood("darkwood_stairs"), TFBlocks.dark_stairs, TFBlocks.dark_planks, TFBlocks.dark_planks);
//		stairsBlock(consumer, locWood("mangrove_stairs"), TFBlocks.mangrove_stairs, TFBlocks.mangrove_planks, TFBlocks.mangrove_planks);
//		stairsBlock(consumer, locWood("mine_stairs"), TFBlocks.mine_stairs, TFBlocks.mine_planks, TFBlocks.mine_planks);
//		stairsBlock(consumer, locWood("sort_stairs"), TFBlocks.sort_stairs, TFBlocks.sort_planks, TFBlocks.sort_planks);
//		stairsBlock(consumer, locWood("time_stairs"), TFBlocks.time_stairs, TFBlocks.time_planks, TFBlocks.time_planks);
//		stairsBlock(consumer, locWood("trans_stairs"), TFBlocks.trans_stairs, TFBlocks.trans_planks, TFBlocks.trans_planks);
//		stairsBlock(consumer, locWood("twilight_oak_stairs"), TFBlocks.twilight_oak_stairs, TFBlocks.twilight_oak_planks, TFBlocks.twilight_oak_planks);
//
//		trapdoorBlock(consumer, "canopy", TFBlocks.canopy_trapdoor, TFBlocks.canopy_planks);
//		trapdoorBlock(consumer, "darkwood", TFBlocks.dark_trapdoor, TFBlocks.dark_planks);
//		trapdoorBlock(consumer, "mangrove", TFBlocks.mangrove_trapdoor, TFBlocks.mangrove_planks);
//		trapdoorBlock(consumer, "mine", TFBlocks.mine_trapdoor, TFBlocks.mine_planks);
//		trapdoorBlock(consumer, "sort", TFBlocks.sort_trapdoor, TFBlocks.sort_planks);
//		trapdoorBlock(consumer, "time", TFBlocks.time_trapdoor, TFBlocks.time_planks);
//		trapdoorBlock(consumer, "trans", TFBlocks.trans_trapdoor, TFBlocks.trans_planks);
//		trapdoorBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_trapdoor, TFBlocks.twilight_oak_planks);
//
//		signBlock(consumer, "canopy_sign", TFBlocks.canopy_sign, TFBlocks.canopy_planks);
//		signBlock(consumer, "darkwood_sign", TFBlocks.darkwood_sign, TFBlocks.dark_planks);
//		signBlock(consumer, "mangrove_sign", TFBlocks.mangrove_sign, TFBlocks.mangrove_planks);
//		signBlock(consumer, "mine_sign", TFBlocks.mine_sign, TFBlocks.mine_planks);
//		signBlock(consumer, "sort_sign", TFBlocks.sort_sign, TFBlocks.sort_planks);
//		signBlock(consumer, "time_sign", TFBlocks.time_sign, TFBlocks.time_planks);
//		signBlock(consumer, "trans_sign", TFBlocks.trans_sign, TFBlocks.trans_planks);
//		signBlock(consumer, "twilight_oak_sign", TFBlocks.twilight_oak_sign, TFBlocks.twilight_oak_planks);
//
//		banisterBlock(consumer, "canopy", TFBlocks.canopy_banister, TFBlocks.canopy_slab);
//		banisterBlock(consumer, "darkwood", TFBlocks.darkwood_banister, TFBlocks.dark_slab);
//		banisterBlock(consumer, "mangrove", TFBlocks.mangrove_banister, TFBlocks.mangrove_slab);
//		banisterBlock(consumer, "mine", TFBlocks.mine_banister, TFBlocks.mine_slab);
//		banisterBlock(consumer, "sort", TFBlocks.sort_banister, TFBlocks.sort_slab);
//		banisterBlock(consumer, "time", TFBlocks.time_banister, TFBlocks.time_slab);
//		banisterBlock(consumer, "trans", TFBlocks.trans_banister, TFBlocks.trans_slab);
//		banisterBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_banister, TFBlocks.twilight_oak_slab);
//
//		banisterBlock(consumer, "oak", TFBlocks.oak_banister, Blocks.OAK_SLAB);
//		banisterBlock(consumer, "spruce", TFBlocks.spruce_banister, Blocks.SPRUCE_SLAB);
//		banisterBlock(consumer, "birch", TFBlocks.birch_banister, Blocks.BIRCH_SLAB);
//		banisterBlock(consumer, "jungle", TFBlocks.jungle_banister, Blocks.JUNGLE_SLAB);
//		banisterBlock(consumer, "acacia", TFBlocks.acacia_banister, Blocks.ACACIA_SLAB);
//		banisterBlock(consumer, "dark_oak", TFBlocks.dark_oak_banister, Blocks.DARK_OAK_SLAB);
//		banisterBlock(consumer, "crimson", TFBlocks.crimson_banister, Blocks.CRIMSON_SLAB);
//		banisterBlock(consumer, "warped", TFBlocks.warped_banister, Blocks.WARPED_SLAB);
//
//	}
//
//	private void nagastoneRecipes(Consumer<FinishedRecipe> consumer) {
//		ShapedRecipeBuilder.shaped(TFBlocks.spiral_bricks, 8)
//				.pattern("BSS")
//				.pattern("BSS")
//				.pattern("BBB")
//				.define('B', Ingredient.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
//				.define('S', Ingredient.of(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
//				.unlockedBy("has_item", has(TFBlocks.spiral_bricks))
//				.save(consumer, locNaga("nagastone_spiral"));
//
//		stairsBlock(consumer, locNaga("nagastone_stairs_left"), TFBlocks.nagastone_stairs_left, TFBlocks.etched_nagastone, TFBlocks.etched_nagastone);
//		stairsRightBlock(consumer, locNaga("nagastone_stairs_right"), TFBlocks.nagastone_stairs_right, TFBlocks.etched_nagastone, TFBlocks.etched_nagastone);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.etched_nagastone, 3)
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right))
//				.unlockedBy("has_item", has(TFBlocks.etched_nagastone))
//				.save(consumer, locNaga("nagastone_stairs_reverse"));
//
//		stairsBlock(consumer, locNaga("nagastone_stairs_left_mossy"), TFBlocks.nagastone_stairs_mossy_left, TFBlocks.etched_nagastone_mossy, TFBlocks.etched_nagastone_mossy);
//		stairsRightBlock(consumer, locNaga("nagastone_stairs_right_mossy"), TFBlocks.nagastone_stairs_mossy_right, TFBlocks.etched_nagastone_mossy, TFBlocks.etched_nagastone_mossy);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.etched_nagastone_mossy, 3)
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right))
//				.unlockedBy("has_item", has(TFBlocks.etched_nagastone_mossy))
//				.save(consumer, locNaga("nagastone_stairs_mossy_reverse"));
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.etched_nagastone_mossy, 1)
//			.requires(Ingredient.of(Blocks.VINE))
//			.requires(Ingredient.of(TFBlocks.etched_nagastone))
//			.unlockedBy("has_item", has(TFBlocks.etched_nagastone))
//			.save(consumer, locNaga("etched_nagastone_mossy"));
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.nagastone_pillar_mossy, 1)
//			.requires(Ingredient.of(Blocks.VINE))
//			.requires(Ingredient.of(TFBlocks.nagastone_pillar))
//			.unlockedBy("has_item", has(TFBlocks.nagastone_pillar))
//			.save(consumer, locNaga("nagastone_pillar_mossy"));
//
//		stairsBlock(consumer, locNaga("nagastone_stairs_left_weathered"), TFBlocks.nagastone_stairs_weathered_left, TFBlocks.etched_nagastone_weathered, TFBlocks.etched_nagastone_weathered);
//		stairsRightBlock(consumer, locNaga("nagastone_stairs_right_weathered"), TFBlocks.nagastone_stairs_weathered_right, TFBlocks.etched_nagastone_weathered, TFBlocks.etched_nagastone_weathered);
//
//		ShapelessRecipeBuilder.shapeless(TFBlocks.etched_nagastone_weathered, 3)
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right))
//				.requires(Ingredient.of(TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right))
//				.unlockedBy("has_item", has(TFBlocks.etched_nagastone_weathered))
//				.save(consumer, locNaga("nagastone_stairs_weathered_reverse"));
//	}
//
//	private void castleRecipes(Consumer<FinishedRecipe> consumer) {
//		castleBlock(consumer, "castle_paver", TFBlocks.castle_brick_frame, TFBlocks.castle_brick, TFBlocks.castle_brick, TFBlocks.castle_brick_worn, TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_mossy);
//		castleBlock(consumer, "castle_pillar_bold", TFBlocks.castle_pillar_bold, TFBlocks.castle_brick_frame, TFBlocks.castle_brick_frame);
//		castleBlock(consumer, "castle_pillar_bold_none", TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold_tile);
//		ShapedRecipeBuilder.shaped(TFBlocks.castle_pillar_encased, 6)
//				.pattern("#H#")
//				.pattern("#H#")
//				.define('#', Ingredient.of(TFBlocks.castle_brick, TFBlocks.castle_brick_worn, TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_frame))
//				.define('H', Ingredient.of(TFBlocks.castle_pillar_encased, TFBlocks.castle_pillar_encased_tile, TFBlocks.castle_pillar_bold_tile))
//				.unlockedBy("has_" + TFBlocks.castle_pillar_encased.getRegistryName().getPath(), has(TFBlocks.castle_pillar_encased))
//				.save(consumer, locCastle("castle_pillar_encased"));
//		castleBlock(consumer, "castle_pillar_encased_none", TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold_tile);
//		stairsBlock(consumer, locCastle("castleblock_stairs_bold"), TFBlocks.castle_stairs_bold, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold_tile);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_bold_reverse"), TFBlocks.castle_pillar_bold, TFBlocks.castle_stairs_bold, TFBlocks.castle_stairs_bold);
//		stairsBlock(consumer, locCastle("castleblock_stairs_brick"), TFBlocks.castle_stairs_brick, TFBlocks.castle_brick, TFBlocks.castle_brick);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_brick_reverse"), TFBlocks.castle_brick, TFBlocks.castle_stairs_brick, TFBlocks.castle_stairs_brick);
//		stairsBlock(consumer, locCastle("castleblock_stairs_cracked"), TFBlocks.castle_stairs_cracked, TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_cracked);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_cracked_reverse"), TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_cracked, TFBlocks.castle_stairs_cracked);
//		stairsBlock(consumer, locCastle("castleblock_stairs_encased"), TFBlocks.castle_stairs_encased, TFBlocks.castle_pillar_encased, TFBlocks.castle_pillar_encased, TFBlocks.castle_pillar_encased_tile);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_encased_reverse"), TFBlocks.castle_pillar_encased, TFBlocks.castle_stairs_encased, TFBlocks.castle_stairs_encased);
//		stairsBlock(consumer, locCastle("castleblock_stairs_mossy"), TFBlocks.castle_stairs_mossy, TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_mossy);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_mossy_reverse"), TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_mossy, TFBlocks.castle_stairs_mossy);
//		stairsBlock(consumer, locCastle("castleblock_stairs_worn"), TFBlocks.castle_stairs_worn, TFBlocks.castle_brick_worn, TFBlocks.castle_brick_worn);
//		reverseStairsBlock(consumer, locCastle("castleblock_stairs_worn_reverse"), TFBlocks.castle_brick_worn, TFBlocks.castle_brick_worn, TFBlocks.castle_stairs_worn);
//		ShapelessRecipeBuilder.shapeless(TFBlocks.castle_brick_mossy, 1)
//		.requires(Ingredient.of(Blocks.VINE))
//		.requires(Ingredient.of(TFBlocks.castle_brick))
//		.unlockedBy("has_item", has(TFBlocks.castle_brick))
//		.save(consumer, locCastle("castle_brick_mossy"));
//	}
//
//	private void fieryConversions(Consumer<FinishedRecipe> consumer) {
//		UpgradeRecipeBuilder.smithing(Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTagGenerator.FIERY_VIAL), TFItems.fiery_ingot).unlocks("has_item", has(TFItems.fiery_ingot)).save(consumer, TwilightForestMod.prefix("material/fiery_iron_ingot"));
//
//		fieryConversion(consumer, TFItems.fiery_helmet, Items.IRON_HELMET, 5);
//		fieryConversion(consumer, TFItems.fiery_chestplate, Items.IRON_CHESTPLATE, 8);
//		fieryConversion(consumer, TFItems.fiery_leggings, Items.IRON_LEGGINGS, 7);
//		fieryConversion(consumer, TFItems.fiery_boots, Items.IRON_BOOTS, 4);
//		ShapelessRecipeBuilder.shapeless(TFItems.fiery_sword)
//				.requires(Items.IRON_SWORD)
//				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 2)
//				.requires(Ingredient.of(Tags.Items.RODS_BLAZE))
//				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
//				.save(consumer, locEquip("fiery_" + Items.IRON_SWORD.getRegistryName().getPath()));
//
//		ShapelessRecipeBuilder.shapeless(TFItems.fiery_pickaxe)
//				.requires(Items.IRON_PICKAXE)
//				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 3)
//				.requires(Ingredient.of(Tags.Items.RODS_BLAZE), 2)
//				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
//				.save(consumer, locEquip("fiery_" + Items.IRON_PICKAXE.getRegistryName().getPath()));
//	}
//	private void cookingRecipes(Consumer<FinishedRecipe> consumer, String processName, SimpleCookingSerializer<?> process, int smeltingTime) {
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFItems.raw_meef), TFItems.cooked_meef, 0.3f, smeltingTime, process).unlockedBy("has_food", has(TFItems.raw_meef)).save(consumer, TwilightForestMod.prefix("food/" + processName + "_meef").toString());
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFItems.raw_venison), TFItems.cooked_venison, 0.3f, smeltingTime, process).unlockedBy("has_food", has(TFItems.raw_venison)).save(consumer, TwilightForestMod.prefix("food/" + processName + "_venison").toString());
//	}
//
//	private void ingotRecipes(Consumer<FinishedRecipe> consumer, String processName, SimpleCookingSerializer<?> process, int smeltingTime) {
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFItems.armor_shard_cluster), TFItems.knightmetal_ingot, 1.0f, smeltingTime, process).unlockedBy("has_item", has(TFItems.armor_shard_cluster)).save(consumer, TwilightForestMod.prefix( "material/" + processName + "_knightmetal_ingot").toString());
//	}
//
//	private void crackedWoodRecipes(Consumer<FinishedRecipe> consumer, String processName, SimpleCookingSerializer<?> process, int smeltingTime) {
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.tower_wood), TFBlocks.tower_wood_cracked, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.tower_wood)).save(consumer, TwilightForestMod.prefix("wood/" + processName + "_cracked_towerwood").toString());
//	}
//
//	private void crackedStoneRecipes(Consumer<FinishedRecipe> consumer, String processName, SimpleCookingSerializer<?> process, int smeltingTime) {
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.nagastone_pillar), TFBlocks.nagastone_pillar_weathered, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.nagastone_pillar)).save(consumer, TwilightForestMod.prefix("nagastone/" + processName + "_cracked_nagastone_pillar").toString());
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.etched_nagastone), TFBlocks.etched_nagastone_weathered, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.etched_nagastone)).save(consumer, TwilightForestMod.prefix("nagastone/" + processName + "_cracked_etched_nagastone").toString());
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.maze_stone_brick), TFBlocks.maze_stone_cracked, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.maze_stone_brick)).save(consumer, TwilightForestMod.prefix("maze_stone/" + processName + "_maze_stone_cracked").toString());
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.castle_brick), TFBlocks.castle_brick_cracked, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.castle_brick)).save(consumer, TwilightForestMod.prefix("castleblock/" + processName + "_cracked_castle_brick").toString());
//		SimpleCookingRecipeBuilder.cooking(Ingredient.of(TFBlocks.underbrick), TFBlocks.underbrick_cracked, 0.3f, smeltingTime, process).unlockedBy("has_item", has(TFBlocks.underbrick)).save(consumer, TwilightForestMod.prefix(processName + "_cracked_underbrick").toString());
//	}
//}
