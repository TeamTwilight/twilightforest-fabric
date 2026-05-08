package twilightforest.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFMenuTypes;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.slot.AssemblySlot;
import twilightforest.inventory.slot.UncraftingResultSlot;
import twilightforest.inventory.slot.UncraftingSlot;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class UncraftingMenu extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {
	private static final String TAG_MARKER = "TwilightForestMarker";
	private static final TagKey<Item> TOOLS_BOW = commonItemTag("tools/bow");
	private static final TagKey<Item> TOOLS_BOWS = commonItemTag("tools/bows");
	private static final TagKey<Item> TOOLS_CROSSBOW = commonItemTag("tools/crossbow");
	private static final TagKey<Item> TOOLS_CROSSBOWS = commonItemTag("tools/crossbows");
	private static final TagKey<Item> TOOLS_FISHING_ROD = commonItemTag("tools/fishing_rod");
	private static final TagKey<Item> TOOLS_FISHING_RODS = commonItemTag("tools/fishing_rods");

	private final UncraftingContainer uncraftingMatrix = new UncraftingContainer(this);
	public final CraftingContainer assemblyMatrix = new TransientCraftingContainer(this, 3, 3);
	private final CraftingContainer combineMatrix = new TransientCraftingContainer(this, 3, 3);
	public final Container tinkerInput = new UncraftingInputContainer(this);
	private final ResultContainer tinkerResult = new ResultContainer();
	private final ContainerLevelAccess positionData;
	private final Level level;
	private final Player player;

	public int unrecipeInCycle = 0;
	public int ingredientsInCycle = 0;
	public int recipeInCycle = 0;
	@Nullable
	public Recipe<?> storedGhostRecipe = null;

	public static UncraftingMenu fromNetwork(int id, Inventory inventory) {
		return new UncraftingMenu(id, inventory, inventory.player.level(), ContainerLevelAccess.NULL);
	}

	public UncraftingMenu(int id, Inventory inventory, Level level, ContainerLevelAccess positionData) {
		super(TFMenuTypes.UNCRAFTING, id);

		this.positionData = positionData;
		this.level = level;
		this.player = inventory.player;

		this.addSlot(new Slot(this.tinkerInput, 0, 13, 35));
		this.addSlot(new UncraftingResultSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

		for (int invX = 0; invX < 3; ++invX) {
			for (int invY = 0; invY < 3; ++invY) {
				this.addSlot(new UncraftingSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
			}
		}
		for (int invX = 0; invX < 3; ++invX) {
			for (int invY = 0; invY < 3; ++invY) {
				this.addSlot(new AssemblySlot(this.assemblyMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
			}
		}
		for (int invX = 0; invX < 3; ++invX) {
			for (int invY = 0; invY < 9; ++invY) {
				this.addSlot(new Slot(inventory, invY + invX * 9 + 9, 8 + invY * 18, 84 + invX * 18));
			}
		}
		for (int invX = 0; invX < 9; ++invX) {
			this.addSlot(new Slot(inventory, invX, 8 + invX * 18, 142));
		}

		this.slotsChanged(this.assemblyMatrix);

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			StringJoiner joiner = new StringJoiner(",\n", "Uncrafting Menu Slots:\n", "(" + this.slots.size() + " total slots)");
			for (Slot slot : this.slots) {
				joiner.add("[index " + slot.index + ": " + slot.getClass().getName() + " (container slot: " + slot.getContainerSlot() + ")]");
			}
			TwilightForestMod.LOGGER.info(joiner.toString());
		}
	}

	@Override
	public void slotsChanged(Container inventory) {
		if (inventory == this.tinkerInput) {
			this.uncraftingMatrix.clearContent();

			ItemStack inputStack = this.tinkerInput.getItem(0);
			Recipe<?>[] recipes = getRecipesFor(inputStack, this.level);

			if (recipes.length > 0 && !inputStack.is(ItemTagGenerator.BANNED_UNCRAFTABLES)) {
				Recipe<?> recipe = recipes[Math.floorMod(this.unrecipeInCycle, recipes.length)];
				this.storedGhostRecipe = recipe;
				ItemStack[] recipeItems = this.getIngredients(recipe);

				if (recipe instanceof ShapedRecipe shapedRecipe) {
					int recipeWidth = shapedRecipe.getWidth();
					int recipeHeight = shapedRecipe.getHeight();
					for (int y = 0; y < recipeHeight; y++) {
						for (int x = 0; x < recipeWidth; x++) {
							int index = x + y * recipeWidth;
							if (index >= recipeItems.length) {
								continue;
							}
							this.uncraftingMatrix.setItem(x + y * 3, normalizeIngredient(recipeItems[index].copy()));
						}
					}
				} else {
					for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
						if (i < recipeItems.length) {
							this.uncraftingMatrix.setItem(i, normalizeIngredient(recipeItems[i].copy()));
						}
					}
				}

				if (inputStack.isDamaged()) {
					int damagedParts = this.countDamagedParts(inputStack);
					for (int i = 0; i < 9 && damagedParts > 0; i++) {
						ItemStack stack = this.uncraftingMatrix.getItem(i);
						if (isDamageableComponent(stack)) {
							markStack(stack);
							damagedParts--;
						}
					}
				}

				for (int i = 0; i < 9; i++) {
					ItemStack ingredient = this.uncraftingMatrix.getItem(i);
					if (isIngredientProblematic(ingredient)) {
						markStack(ingredient);
					}
				}

				this.uncraftingMatrix.numberOfInputItems = recipe instanceof UncraftingRecipe uncraftingRecipe
						? uncraftingRecipe.getCount()
						: recipe.getResultItem(this.level.registryAccess()).getCount();
				this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;
			} else {
				this.storedGhostRecipe = null;
				this.uncraftingMatrix.numberOfInputItems = 0;
				this.uncraftingMatrix.uncraftingCost = 0;
			}
		}

		if (inventory == this.assemblyMatrix || inventory == this.tinkerInput) {
			if (this.tinkerInput.isEmpty()) {
				this.chooseRecipe(this.assemblyMatrix.asCraftInput());
			} else {
				this.tinkerResult.setItem(0, ItemStack.EMPTY);
				this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
			}
			this.uncraftingMatrix.recraftingCost = 0;
		}

		if (inventory != this.combineMatrix && !this.uncraftingMatrix.isEmpty() && !this.assemblyMatrix.isEmpty()) {
			for (int i = 0; i < 9; i++) {
				ItemStack assembly = this.assemblyMatrix.getItem(i);
				ItemStack uncrafting = this.uncraftingMatrix.getItem(i);

				if (!assembly.isEmpty()) {
					this.combineMatrix.setItem(i, assembly);
				} else if (!uncrafting.isEmpty() && !isMarked(uncrafting)) {
					this.combineMatrix.setItem(i, uncrafting);
				} else {
					this.combineMatrix.setItem(i, ItemStack.EMPTY);
				}
			}

			this.chooseRecipe(this.combineMatrix.asCraftInput());

			ItemStack input = this.tinkerInput.getItem(0);
			ItemStack result = this.tinkerResult.getItem(0);

			if (!result.isEmpty() && isValidMatchForInput(input, result)) {
				if (result.getItem().isEnchantable(result)) {
					ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(input.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
					if (result.has(DataComponents.ENCHANTMENTS)) {
						Objects.requireNonNull(result.get(DataComponents.ENCHANTMENTS)).entrySet().forEach(enchantment -> enchants.set(enchantment.getKey(), enchantment.getIntValue()));
					}
					enchants.removeIf(holder -> !holder.value().canEnchant(result));
					result.remove(DataComponents.ENCHANTMENTS);
					EnchantmentHelper.setEnchantments(result, enchants.toImmutable());
				}

				this.tinkerResult.setItem(0, result);
				this.uncraftingMatrix.uncraftingCost = 0;
				this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();
			}
		}
	}

	public static void markStack(ItemStack stack) {
		TFItemStackUtils.addInfoTag(stack, TAG_MARKER);
	}

	public static boolean isMarked(ItemStack stack) {
		return TFItemStackUtils.hasInfoTag(stack, TAG_MARKER);
	}

	@SuppressWarnings("unused")
	public static void unmarkStack(ItemStack stack) {
		TFItemStackUtils.clearInfoTag(stack, TAG_MARKER);
	}

	public static boolean isIngredientProblematic(ItemStack ingredient) {
		return (!ingredient.isEmpty() && ingredient.getItem().hasCraftingRemainingItem()) || ingredient.is(Items.BARRIER);
	}

	private static ItemStack normalizeIngredient(ItemStack ingredient) {
		if (ingredient.getCount() > 1) {
			ingredient.setCount(1);
		}
		return ingredient;
	}

	private static Recipe<?>[] getRecipesFor(ItemStack inputStack, Level level) {
		List<Recipe<?>> recipes = new ArrayList<>();

		if (!inputStack.isEmpty()) {
			for (RecipeHolder<?> recipe : level.getRecipeManager().getRecipes()) {
				if (isRecipeSupported(recipe.value())
						&& !recipe.value().isIncomplete()
						&& recipe.value().canCraftInDimensions(3, 3)
						&& !recipe.value().getIngredients().isEmpty()
						&& matches(inputStack, recipe.value().getResultItem(level.registryAccess()))
						&& TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().toString())
						&& TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().getNamespace())) {
					recipes.add(recipe.value());
				}
			}
			for (RecipeHolder<UncraftingRecipe> uncraftingRecipe : level.getRecipeManager().getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE)) {
				if (uncraftingRecipe.value().isItemStackAnIngredient(inputStack)) {
					recipes.add(uncraftingRecipe.value());
				}
			}
		}

		return recipes.toArray(new Recipe<?>[0]);
	}

	private static boolean isRecipeSupported(Recipe<?> recipe) {
		return TFConfig.allowShapelessUncrafting ? recipe instanceof CraftingRecipe : recipe instanceof ShapedRecipe;
	}

	private static boolean matches(ItemStack input, ItemStack output) {
		return input.is(output.getItem()) && input.getCount() >= output.getCount();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static RecipeHolder<CraftingRecipe>[] getRecipesFor(CraftingInput input, Level level) {
		return level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, level).toArray(new RecipeHolder[0]);
	}

	private void chooseRecipe(CraftingInput input) {
		RecipeHolder<CraftingRecipe>[] recipes = getRecipesFor(input, this.level);

		if (recipes.length == 0) {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
			return;
		}

		RecipeHolder<CraftingRecipe> recipe = recipes[Math.floorMod(this.recipeInCycle, recipes.length)];
		boolean allowed = !this.level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING)
				|| (this.player instanceof ServerPlayer serverPlayer && serverPlayer.getRecipeBook().contains(recipe.id()));

		if (allowed) {
			this.tinkerResult.setRecipeUsed(recipe);
			this.tinkerResult.setItem(0, recipe.value().assemble(input, this.level.registryAccess()));
		} else {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
		}
	}

	private static boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
		if (inputStack.is(ItemTags.PICKAXES) && resultStack.is(ItemTags.PICKAXES)) return true;
		if (inputStack.is(ItemTags.AXES) && resultStack.is(ItemTags.AXES)) return true;
		if (inputStack.is(ItemTags.SHOVELS) && resultStack.is(ItemTags.SHOVELS)) return true;
		if (inputStack.is(ItemTags.HOES) && resultStack.is(ItemTags.HOES)) return true;
		if (inputStack.is(ItemTags.SWORDS) && resultStack.is(ItemTags.SWORDS)) return true;
		if (sameCommonTool(inputStack, resultStack, Items.BOW, TOOLS_BOW, TOOLS_BOWS)) return true;
		if (sameCommonTool(inputStack, resultStack, Items.CROSSBOW, TOOLS_CROSSBOW, TOOLS_CROSSBOWS)) return true;
		if (sameCommonTool(inputStack, resultStack, Items.FISHING_ROD, TOOLS_FISHING_ROD, TOOLS_FISHING_RODS)) return true;

		if (inputStack.getItem() instanceof ArmorItem input && resultStack.getItem() instanceof ArmorItem result) {
			return input.getEquipmentSlot() == result.getEquipmentSlot();
		}

		return false;
	}

	public int getUncraftingCost() {
		return this.uncraftingMatrix.uncraftingCost;
	}

	public int getRecraftingCost() {
		return this.uncraftingMatrix.recraftingCost;
	}

	private int calculateUncraftingCost() {
		if ((!TFConfig.disableUncraftingOnly || this.storedGhostRecipe instanceof UncraftingRecipe) && this.assemblyMatrix.isEmpty()) {
			return this.storedGhostRecipe instanceof UncraftingRecipe recipe
					? recipe.getCost()
					: (int) Math.round(countDamageableParts(this.uncraftingMatrix) * TFConfig.uncraftingXpCostMultiplier);
		}
		return 0;
	}

	private int calculateRecraftingCost() {
		ItemStack input = this.tinkerInput.getItem(0);
		ItemStack output = this.tinkerResult.getItem(0);

		if (input.isEmpty() || output.isEmpty()) {
			return 0;
		}
		if (!output.getItem().isEnchantable(output)) {
			return 0;
		}

		int cost = 0;
		if (!ItemStack.isSameItem(input, output)) {
			cost += this.assemblyMatrix.getItems().stream().filter(stack -> !stack.isEmpty()).toList().size();
		}

		cost += countTotalEnchantmentCost(input);
		cost += (1 + this.countDamagedParts(input)) * output.getEnchantments().size();
		cost = Math.max(1, cost);

		return (int) Math.round(cost * TFConfig.repairingXpCostMultiplier);
	}

	private static int countTotalEnchantmentCost(ItemStack stack) {
		int count = 0;
		for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
			Enchantment enchantment = entry.getKey().value();
			int level = entry.getIntValue();
			if (level > 0) {
				count += getWeightModifier(enchantment) * level;
				count += 1;
			}
		}
		return count;
	}

	private static int getWeightModifier(Enchantment enchantment) {
		return switch (enchantment.getWeight()) {
			case 1 -> 8;
			case 2 -> 4;
			case 3, 4, 5 -> 2;
			default -> 1;
		};
	}

	@Override
	public void clicked(int slotNum, int mouseButton, ClickType clickType, Player player) {
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.assemblyMatrix
				&& player.containerMenu.getCarried().isEmpty() && !this.slots.get(slotNum).hasItem()
				&& this.assemblyMatrix.isEmpty()
				&& (clickType != ClickType.SWAP || player.getInventory().getItem(mouseButton).isEmpty())) {
			slotNum -= 9;
		}

		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.tinkerResult
				&& this.calculateRecraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {
			return;
		}

		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.uncraftingMatrix) {
			if (TFConfig.disableUncraftingOnly && !(this.storedGhostRecipe instanceof UncraftingRecipe)) {
				return;
			}
			if (this.calculateUncraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {
				return;
			}
			ItemStack stackInSlot = this.slots.get(slotNum).getItem();
			if (stackInSlot.isEmpty() || isMarked(stackInSlot)) {
				return;
			}
		}

		super.clicked(slotNum, mouseButton, clickType, player);

		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.tinkerInput) {
			this.slotsChanged(this.tinkerInput);
		}
	}

	@NotNull
	private Container getSlotContainer(int slotNum) {
		return this.slots.get(slotNum).container;
	}

	public static boolean isDamageableComponent(ItemStack stack) {
		return !stack.isEmpty() && !stack.is(ItemTagGenerator.UNCRAFTING_IGNORES_COST);
	}

	public static int countDamageableParts(Container matrix) {
		int count = matrix.getContainerSize();
		for (int i = 0; i < matrix.getContainerSize(); i++) {
			if (isIngredientProblematic(matrix.getItem(i)) || isMarked(matrix.getItem(i)) || !isDamageableComponent(matrix.getItem(i))) {
				count--;
			}
		}
		return count;
	}

	private int countDamagedParts(ItemStack input) {
		int totalMax4 = Math.max(4, countDamageableParts(this.uncraftingMatrix));
		float damage = (float) input.getDamageValue() / (float) input.getMaxDamage();
		return (int) Math.ceil(totalMax4 * damage);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNum) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNum);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemStack = slotStack.copy();
			if (slotNum == 0) {
				if (!this.moveItemStackTo(slotStack, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(slotStack, itemStack);
			} else if (slotNum == 1) {
				this.positionData.execute((level, pos) -> slotStack.getItem().onCraftedBy(slotStack, level, player));
				if (!this.moveItemStackTo(slotStack, 20, 56, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(slotStack, itemStack);
			} else if (slotNum >= 20 && slotNum < 56) {
				if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slot.container == this.assemblyMatrix) {
				if (!this.moveItemStackTo(slotStack, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.moveItemStackTo(slotStack, 20, 56, false)) {
				slot.onTake(player, slotStack);
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
			if (slotNum == 1) {
				player.drop(slotStack, false);
			}
		}
		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.positionData.execute((level, pos) -> {
			this.clearContainer(player, this.assemblyMatrix);
			this.clearContainer(player, this.tinkerInput);
		});
	}

	private ItemStack[] getIngredients(Recipe<?> recipe) {
		ItemStack[] stacks = new ItemStack[recipe.getIngredients().size()];

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack[] matchingStacks = Arrays.stream(recipe.getIngredients().get(i).getItems())
					.filter(stack -> !stack.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS))
					.toArray(ItemStack[]::new);
			stacks[i] = matchingStacks.length > 0 ? matchingStacks[Math.floorMod(this.ingredientsInCycle, matchingStacks.length)] : ItemStack.EMPTY;
		}

		return stacks;
	}

	@Override
	public boolean stillValid(Player player) {
		return !TFConfig.disableEntireTable && stillValid(this.positionData, player, TFBlocks.UNCRAFTING_TABLE.get());
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
		this.assemblyMatrix.fillStackedContents(stackedContents);
	}

	@Override
	public void clearCraftingContent() {
		this.tinkerInput.clearContent();
		this.assemblyMatrix.clearContent();
		this.tinkerResult.clearContent();
	}

	@Override
	public int getResultSlotIndex() {
		return 1;
	}

	@Override
	public int getGridWidth() {
		return this.assemblyMatrix.getWidth();
	}

	@Override
	public int getGridHeight() {
		return this.assemblyMatrix.getHeight();
	}

	@Override
	public int getSize() {
		return 20;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int slot) {
		return slot == 0 || (11 <= slot && slot <= 19);
	}

	@Override
	public boolean recipeMatches(RecipeHolder<Recipe<RecipeInput>> recipeHolder) {
		return recipeHolder.value().matches(this.assemblyMatrix.asCraftInput(), this.player.level());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void handlePlacement(boolean placeAll, RecipeHolder<?> recipe, ServerPlayer player) {
		new UncrafterPlaceRecipe<>(this).recipeClicked(player, (RecipeHolder<Recipe<RecipeInput>>) recipe, placeAll);
	}

	private static TagKey<Item> commonItemTag(String path) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
	}

	private static boolean sameCommonTool(ItemStack input, ItemStack result, Item vanilla, TagKey<Item> singular, TagKey<Item> plural) {
		return (input.is(vanilla) || input.is(singular) || input.is(plural))
				&& (result.is(vanilla) || result.is(singular) || result.is(plural));
	}
}
