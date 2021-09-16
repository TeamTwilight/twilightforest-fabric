package twilightforest.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator extends RecipeProvider {
	public StonecuttingGenerator(DataGenerator generator) {
		super(generator);
	}

	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		consumer.accept(stonecutting(TFBlocks.castle_brick, TFBlocks.castle_brick_frame));
		consumer.accept(stonecutting(TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_frame));
		consumer.accept(stonecutting(TFBlocks.castle_brick_worn, TFBlocks.castle_brick_frame));
		consumer.accept(stonecutting(TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_frame));

		consumer.accept(stonecutting(TFBlocks.castle_brick, TFBlocks.castle_brick_worn));
		consumer.accept(stonecutting(TFBlocks.castle_brick_frame, TFBlocks.castle_pillar_bold));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold_tile));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_encased, TFBlocks.castle_pillar_encased_tile));

		consumer.accept(stonecutting(TFBlocks.castle_pillar_encased, TFBlocks.castle_stairs_encased));
		consumer.accept(stonecutting(TFBlocks.castle_pillar_bold, TFBlocks.castle_stairs_bold));
		consumer.accept(stonecutting(TFBlocks.castle_brick, TFBlocks.castle_stairs_brick));
		consumer.accept(stonecutting(TFBlocks.castle_brick_worn, TFBlocks.castle_stairs_worn));
		consumer.accept(stonecutting(TFBlocks.castle_brick_cracked, TFBlocks.castle_stairs_cracked));
		consumer.accept(stonecutting(TFBlocks.castle_brick_mossy, TFBlocks.castle_stairs_mossy));

		consumer.accept(stonecutting(TFBlocks.etched_nagastone, TFBlocks.nagastone_stairs_left));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone, TFBlocks.nagastone_stairs_right));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_mossy, TFBlocks.nagastone_stairs_mossy_left));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_mossy, TFBlocks.nagastone_stairs_mossy_right));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_weathered, TFBlocks.nagastone_stairs_weathered_left));
		consumer.accept(stonecutting(TFBlocks.etched_nagastone_weathered, TFBlocks.nagastone_stairs_weathered_right));

		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_right, TFBlocks.nagastone_stairs_left));
		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_left, TFBlocks.nagastone_stairs_right));
		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_mossy_right, TFBlocks.nagastone_stairs_mossy_left));
		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_mossy_right));
		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_weathered_right, TFBlocks.nagastone_stairs_weathered_left));
		consumer.accept(stonecutting(TFBlocks.nagastone_stairs_weathered_left, TFBlocks.nagastone_stairs_weathered_right));

		consumer.accept(stonecutting(TFBlocks.dark_log, TFBlocks.tower_wood));
		consumer.accept(stonecutting(TFBlocks.dark_wood, TFBlocks.tower_wood));
		consumer.accept(stonecutting(TFBlocks.tower_wood, TFBlocks.tower_wood_encased));

		consumer.accept(stonecutting(TFBlocks.maze_stone, TFBlocks.maze_stone_border));
		consumer.accept(stonecutting(TFBlocks.maze_stone, TFBlocks.maze_stone_brick));
		consumer.accept(stonecutting(TFBlocks.maze_stone, TFBlocks.maze_stone_chiseled));
		consumer.accept(stonecutting(TFBlocks.maze_stone, TFBlocks.maze_stone_decorative));
		consumer.accept(stonecutting(TFBlocks.maze_stone, TFBlocks.maze_stone_mosaic));

		consumer.accept(stonecutting(TFBlocks.maze_stone_brick, TFBlocks.maze_stone_border));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick, TFBlocks.maze_stone_chiseled));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick, TFBlocks.maze_stone_decorative));
		consumer.accept(stonecutting(TFBlocks.maze_stone_brick, TFBlocks.maze_stone_mosaic));

		consumer.accept(stonecutting(TFBlocks.maze_stone_border, TFBlocks.maze_stone_brick));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border, TFBlocks.maze_stone_chiseled));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border, TFBlocks.maze_stone_decorative));
		consumer.accept(stonecutting(TFBlocks.maze_stone_border, TFBlocks.maze_stone_mosaic));

		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled, TFBlocks.maze_stone_border));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled, TFBlocks.maze_stone_brick));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled, TFBlocks.maze_stone_decorative));
		consumer.accept(stonecutting(TFBlocks.maze_stone_chiseled, TFBlocks.maze_stone_mosaic));

		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative, TFBlocks.maze_stone_border));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative, TFBlocks.maze_stone_chiseled));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative, TFBlocks.maze_stone_brick));
		consumer.accept(stonecutting(TFBlocks.maze_stone_decorative, TFBlocks.maze_stone_mosaic));

		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic, TFBlocks.maze_stone_border));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic, TFBlocks.maze_stone_chiseled));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic, TFBlocks.maze_stone_decorative));
		consumer.accept(stonecutting(TFBlocks.maze_stone_mosaic, TFBlocks.maze_stone_brick));

		consumer.accept(stonecutting(Blocks.STONE, TFBlocks.stone_twist));
		consumer.accept(stonecutting(Blocks.STONE, TFBlocks.stone_bold));
	}

    /**
     * Performs this provider's action.
     */
    public void run(HashCache pCache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildCraftingRecipes((p_125991_) -> {
            if (!set.add(p_125991_.getId())) {
                throw new IllegalStateException("Duplicate recipe " + p_125991_.getId());
            } else {
                saveRecipe(pCache, p_125991_.serializeRecipe(), path.resolve("data/" + p_125991_.getId().getNamespace() + "/recipes/" + p_125991_.getId().getPath() + ".json"));
                JsonObject jsonobject = p_125991_.serializeAdvancement();
                if (jsonobject != null) {
                    saveAdvancement(pCache, jsonobject, path.resolve("data/" + p_125991_.getId().getNamespace() + "/advancements/" + p_125991_.getAdvancementId().getPath() + ".json"));
                }

            }
        });
//        if (this.getClass() == RecipeProvider.class) //Forge: Subclasses don't need this.
            saveAdvancement(pCache, Advancement.Builder.advancement().addCriterion("impossible", new ImpossibleTrigger.TriggerInstance()).serializeToJson(), path.resolve("data/minecraft/advancements/recipes/root.json"));
    }

	@Override
	public String getName() {
		return "Twilight Forest stonecutting recipes";
	}

	private static Wrapper stonecutting(ItemLike input, ItemLike output) {
		return stonecutting(input, output, 1);
	}

	private static Wrapper stonecutting(ItemLike input, ItemLike output, int count) {
		return new Wrapper(getIdFor(input.asItem(), output.asItem()), Ingredient.of(input), output.asItem(), count);
	}

	private static ResourceLocation getIdFor(Item input, Item output) {
		String path = String.format("stonecutting/%s/%s", Registry.ITEM.getKey(input).getPath(), Registry.ITEM.getKey(output).getPath());
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
