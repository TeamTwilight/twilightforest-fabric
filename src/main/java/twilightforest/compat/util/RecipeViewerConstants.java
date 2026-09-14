package twilightforest.compat.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.level.block.Block;
import twilightforest.block.OminousFireBlock;
import twilightforest.config.TFConfig;
import twilightforest.item.CrumbleHornItem;
import twilightforest.item.TransformPowderItem;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.UncraftingMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewerConstants {
	public static final int GENERIC_RECIPE_WIDTH = 116;
	public static final int GENERIC_RECIPE_HEIGHT = 54;
	public static final Component MOONWORM_QUEEN_TOOLTIP = Component.translatable("item.twilightforest.moonworm_queen.jei_info_message").withStyle(ChatFormatting.GREEN);

	public static final ItemStack DAMAGED_MOONWORM_QUEEN = Util.make(new ItemStack(TFItems.MOONWORM_QUEEN), stack -> stack.setDamageValue(256));
	//trickery is afoot
	public static final List<ItemStack> BERRY_2_LIST = List.of(ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES), new ItemStack(TFItems.TORCHBERRIES), new ItemStack(TFItems.TORCHBERRIES));
	public static final List<ItemStack> BERRY_3_LIST = List.of(ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES), new ItemStack(TFItems.TORCHBERRIES));
	public static final List<ItemStack> BERRY_4_LIST = List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES));
	public static final List<ItemStack> MOONWORM_QUEEN_LIST = List.of(
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN), stack -> stack.setDamageValue(192)),
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN), stack -> stack.setDamageValue(128)),
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN), stack -> stack.setDamageValue(64)),
		new ItemStack(TFItems.MOONWORM_QUEEN));

	public static List<RecipeHolder<CraftingRecipe>> getAllUncraftingRecipes(RecipeAccess recipes, ContextMap context) {
		if (!TFConfig.disableUncraftingOnly) { //we only do this if uncrafting is not disabled
			List<RecipeHolder<CraftingRecipe>> filtered = recipes.getSynchronizedRecipes().getAllOfType(RecipeType.CRAFTING).stream()
				.filter(recipe -> {
					ItemStack result = getDisplayResult(recipe.value(), context);
					return !result.isEmpty() && //get rid of empty items
						!result.is(TFItemTags.BANNED_UNCRAFTABLES) && //Prevents things that are tagged as banned from showing up
						TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().identifier().toString()) && //remove disabled recipes
						TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().identifier().getNamespace()); //remove blacklisted mod ids
				})
				.collect(Collectors.toCollection(ArrayList::new));
			filtered.removeIf(recipe -> (recipe.value() instanceof ShapelessRecipe && !TFConfig.allowShapelessUncrafting));
			filtered.addAll(getUncraftingOnly(recipes));
			return filtered;
		} else {
			return getUncraftingOnly(recipes);
		}
	}

	private static List<RecipeHolder<CraftingRecipe>> getUncraftingOnly(RecipeAccess recipes) {
		@SuppressWarnings("unchecked") // Yuck
		List<RecipeHolder<CraftingRecipe>> uncrafting = new ArrayList<>((Collection<RecipeHolder<CraftingRecipe>>) (Collection<?>) recipes.getSynchronizedRecipes().getAllOfType(TFRecipes.UNCRAFTING_RECIPE));
		return uncrafting;
	}

	public static ItemStack getDisplayResult(Recipe<?> recipe, ContextMap context) {
		List<RecipeDisplay> displays = recipe.display();
		if (displays.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return displays.getFirst().result().resolveForFirstStack(context);
	}

	public static int getDisplayWidth(Recipe<?> recipe) {
		List<RecipeDisplay> displays = recipe.display();
		if (!displays.isEmpty() && displays.getFirst() instanceof ShapedCraftingRecipeDisplay shaped) {
			return shaped.width();
		}
		return 0;
	}

	public static int getDisplayHeight(Recipe<?> recipe) {
		List<RecipeDisplay> displays = recipe.display();
		if (!displays.isEmpty() && displays.getFirst() instanceof ShapedCraftingRecipeDisplay shaped) {
			return shaped.height();
		}
		return 0;
	}

	//all recipe viewers run this once when initializing recipes
	public static List<TransformationPowderInfo> getTransformationPowderRecipes() {
		List<TransformationPowderInfo> info = new ArrayList<>();
		List<EntityType<?>> inputs = new ArrayList<>(TransformPowderItem.TRANSFORMATION_POWDER.keySet());
		for (EntityType<?> input : new ArrayList<>(inputs)) {
			var output = TransformPowderItem.TRANSFORMATION_POWDER.get(input);
			if (output != null) {
				TransformationPowderInfo dummy = new TransformationPowderInfo(output, input, true);
				if (!info.contains(dummy)) {
					if (inputs.contains(output)) {
						info.add(new TransformationPowderInfo(input, output, true));
					} else {
						info.add(new TransformationPowderInfo(input, output, false));
					}
				}
			}
		}
		return info;
	}

	//all recipe viewers run this once when initializing recipes
	public static List<OminousFireInfo> getOminousFireRecipes() {
		List<OminousFireInfo> info = new ArrayList<>();
		List<EntityType<?>> inputs = new ArrayList<>(OminousFireBlock.OMINOUS_FIRE.keySet());
		for (EntityType<?> input : new ArrayList<>(inputs)) {
			var output = OminousFireBlock.OMINOUS_FIRE.get(input);
			if (output != null) {
				OminousFireInfo dummy = new OminousFireInfo(output, input);
				if (!info.contains(dummy)) {
					info.add(new OminousFireInfo(input, output));
				}
			}
		}
		return info;
	}

	//all recipe viewers run this once when initializing recipes
	public static List<Pair<Block, Block>> getCrumbleHornRecipes() {
		List<Pair<Block, Block>> info = new ArrayList<>();
		for (Block input : CrumbleHornItem.CRUMBLE_HORN.keySet()) {
			var output = CrumbleHornItem.CRUMBLE_HORN.get(input);
			if (output != null) {
				info.add(Pair.of(input, output.getFirst()));
			}
		}
		return info;
	}

	public static int getRecipeCost(List<ItemStack> inputs) {
		int cost = 0;
		for (ItemStack stack : inputs) {
			if (UncraftingMenu.isDamageableComponent(stack) && !UncraftingMenu.isIngredientProblematic(stack) && !UncraftingMenu.isMarked(stack)) {
				cost++;
			}
		}
		return cost;
	}

	public static int getXPColor(int cost) {
		if (Minecraft.getInstance().player.experienceLevel < cost && !Minecraft.getInstance().player.getAbilities().instabuild) {
			return 0xFFA00000;
		} else {
			return 0xFF80FF20;
		}
	}

	public static Component getDryingTime(int dryingTicks) {
		int dryingMinutes = dryingTicks / 60 / 20;
		int dryingSeconds = (dryingTicks / 20) % 60;
		Component time;

		if (dryingMinutes > 0) {
			if (dryingSeconds > 0) {
				time = Component.translatable("gui.twilightforest.drying_time", dryingMinutes, dryingSeconds);
			} else {
				if (dryingMinutes == 1) {
					time = Component.translatable("gui.twilightforest.drying_minute", dryingMinutes);
				} else {
					time = Component.translatable("gui.twilightforest.drying_minutes", dryingMinutes);
				}
			}
		} else {
			if (dryingSeconds == 1) {
				time = Component.translatable("gui.twilightforest.drying_second", dryingSeconds);
			} else {
				time = Component.translatable("gui.twilightforest.drying_seconds", dryingSeconds);
			}
		}
		return time;
	}

	//copy of JEI's CraftingGridHelper.getCraftingIndex
	public static int getCraftingIndex(int width, int height, int i) {
		int index;
		if (width == 1) {
			if (height == 3) {
				index = (i * 3) + 1;
			} else if (height == 2) {
				index = (i * 3) + 1;
			} else {
				index = 4;
			}
		} else if (height == 1) {
			index = i + 3;
		} else if (width == 2) {
			index = i;
			if (i > 1) {
				index++;
				if (i > 3) {
					index++;
				}
			}
		} else if (height == 2) {
			index = i + 3;
		} else {
			index = i;
		}
		return index;
	}

	public record TransformationPowderInfo(EntityType<?> input, EntityType<?> output, boolean reversible) {
	}

	public record OminousFireInfo(EntityType<?> input, EntityType<?> output) {
	}
}