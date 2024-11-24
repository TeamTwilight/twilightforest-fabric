package twilightforest.data;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator {

	protected static void buildRecipes(Consumer<FinishedRecipe> consumer) {
		consumer.accept(stonecutting(TFBlocks.CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get()));

		consumer.accept(stonecutting(TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get()));
		consumer.accept(stonecutting(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		consumer.accept(stonecutting(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.get()));

		consumer.accept(stonecutting(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get()));
		consumer.accept(stonecutting(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get()));
		consumer.accept(stonecutting(TFBlocks.CASTLE_BRICK.get(), TFBlocks.CASTLE_BRICK_STAIRS.get()));
		consumer.accept(stonecutting(TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get()));

		consumer.accept(stonecutting(TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get()));

		consumer.accept(stonecutting(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get()));
		consumer.accept(stonecutting(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get()));

		consumer.accept(stonecutting(TFBlocks.DARK_LOG.get(), TFBlocks.TOWERWOOD.get()));
		consumer.accept(stonecutting(TFBlocks.DARK_WOOD.get(), TFBlocks.TOWERWOOD.get()));
		consumer.accept(stonecutting(TFBlocks.TOWERWOOD.get(), TFBlocks.ENCASED_TOWERWOOD.get()));

		consumer.accept(stonecutting(TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE.get(), TFBlocks.CUT_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get()));

		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.MAZESTONE_BORDER.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.CUT_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.DECORATIVE_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.MAZESTONE_MOSAIC.get()));

		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.MAZESTONE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.CUT_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.DECORATIVE_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_BORDER.get(), TFBlocks.MAZESTONE_MOSAIC.get()));

		consumer.accept(stonecutting(TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get()));
		consumer.accept(stonecutting(TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.CUT_MAZESTONE.get(), TFBlocks.DECORATIVE_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.CUT_MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get()));

		consumer.accept(stonecutting(TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_BORDER.get()));
		consumer.accept(stonecutting(TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.CUT_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get()));
		consumer.accept(stonecutting(TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get()));

		consumer.accept(stonecutting(TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BORDER.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.CUT_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.DECORATIVE_MAZESTONE.get()));
		consumer.accept(stonecutting(TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BRICK.get()));

		consumer.accept(stonecutting(TFBlocks.TWILIGHT_OAK_LOG.get(), TFItems.HOLLOW_TWILIGHT_OAK_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.CANOPY_LOG.get(), TFItems.HOLLOW_CANOPY_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.MANGROVE_LOG.get(), TFItems.HOLLOW_MANGROVE_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.DARK_LOG.get(), TFItems.HOLLOW_DARK_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.TIME_LOG.get(), TFItems.HOLLOW_TIME_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.TRANSFORMATION_LOG.get(), TFItems.HOLLOW_TRANSFORMATION_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.MINING_LOG.get(), TFItems.HOLLOW_MINING_LOG.get()));
		consumer.accept(stonecutting(TFBlocks.SORTING_LOG.get(), TFItems.HOLLOW_SORTING_LOG.get()));

		consumer.accept(stonecutting(Blocks.OAK_LOG, TFItems.HOLLOW_OAK_LOG.get()));
		consumer.accept(stonecutting(Blocks.SPRUCE_LOG, TFItems.HOLLOW_SPRUCE_LOG.get()));
		consumer.accept(stonecutting(Blocks.BIRCH_LOG, TFItems.HOLLOW_BIRCH_LOG.get()));
		consumer.accept(stonecutting(Blocks.JUNGLE_LOG, TFItems.HOLLOW_JUNGLE_LOG.get()));
		consumer.accept(stonecutting(Blocks.ACACIA_LOG, TFItems.HOLLOW_ACACIA_LOG.get()));
		consumer.accept(stonecutting(Blocks.DARK_OAK_LOG, TFItems.HOLLOW_DARK_OAK_LOG.get()));
		consumer.accept(stonecutting(Blocks.CRIMSON_STEM, TFItems.HOLLOW_CRIMSON_STEM.get()));
		consumer.accept(stonecutting(Blocks.WARPED_STEM, TFItems.HOLLOW_WARPED_STEM.get()));
		consumer.accept(stonecutting(Blocks.MANGROVE_LOG, TFItems.HOLLOW_VANGROVE_LOG.get()));
		consumer.accept(stonecutting(Blocks.CHERRY_LOG, TFItems.HOLLOW_CHERRY_LOG.get()));
		consumer.accept(stonecutting(Blocks.STONE, TFBlocks.TWISTED_STONE.get()));
		consumer.accept(stonecutting(Blocks.STONE, TFBlocks.BOLD_STONE_PILLAR.get()));
		consumer.accept(stonecutting(TFBlocks.TWISTED_STONE.get(), TFBlocks.TWISTED_STONE_PILLAR.get()));

		consumer.accept(stonecutting(TFBlocks.UNDERBRICK.get(), TFBlocks.UNDERBRICK_FLOOR.get()));
	}

	private static Wrapper stonecutting(ItemLike input, ItemLike output) {
		return stonecutting(input, output, 1);
	}

	private static Wrapper stonecutting(ItemLike input, ItemLike output, int count) {
		return new Wrapper(getIdFor(input.asItem(), output.asItem()), Ingredient.of(input), output.asItem(), count);
	}

	private static ResourceLocation getIdFor(Item input, Item output) {
		String path = String.format("stonecutting/%s/%s", BuiltInRegistries.ITEM.getKey(input).getPath(), BuiltInRegistries.ITEM.getKey(output).getPath());
		return prefix(path);
	}

	// Wrapper that allows you to not have an advancement
	public static class Wrapper extends SingleItemRecipeBuilder.Result {
		public Wrapper(ResourceLocation id, Ingredient input, Item output, int count) {
			super(id, RecipeSerializer.STONECUTTER, "", input, output, count, null, null);
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
