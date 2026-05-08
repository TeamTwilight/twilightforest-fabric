package twilightforest.inventory.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingMenu;

import java.util.HashMap;
import java.util.Map;

public class UncraftingResultSlot extends ResultSlot {
	private final Player player;
	private final Container inputSlot;
	private final UncraftingContainer uncraftingMatrix;
	private final CraftingContainer assemblyMatrix;
	private final Map<Integer, ItemStack> tempRemainderMap = new HashMap<>();

	public UncraftingResultSlot(Player player, Container input, Container uncraftingMatrix, Container assemblyMatrix, Container result, int slotIndex, int x, int y) {
		super(player, (CraftingContainer) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (UncraftingContainer) uncraftingMatrix;
		this.assemblyMatrix = (CraftingContainer) assemblyMatrix;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		boolean combined = true;
		this.tempRemainderMap.clear();

		for (RecipeHolder<CraftingRecipe> recipe : player.level().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, this.assemblyMatrix.asCraftInput(), this.player.level())) {
			if (ItemStack.isSameItemSameComponents(recipe.value().getResultItem(player.level().registryAccess()), stack)) {
				combined = false;
				break;
			}
		}

		if (combined) {
			if (this.uncraftingMatrix.recraftingCost > 0) {
				this.player.giveExperienceLevels(-this.uncraftingMatrix.recraftingCost);
			}

			for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
				if (this.assemblyMatrix.getItem(i).isEmpty()) {
					if (!UncraftingMenu.isMarked(this.uncraftingMatrix.getItem(i))) {
						this.uncraftingMatrix.setItem(i, ItemStack.EMPTY);
					} else {
						this.tempRemainderMap.put(i, this.uncraftingMatrix.getItem(i));
					}
				}
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		this.checkTakeAchievements(stack);

		CraftingInput.Positioned positioned = this.assemblyMatrix.asPositionedCraftInput();
		CraftingInput input = positioned.input();
		int left = positioned.left();
		int top = positioned.top();
		NonNullList<ItemStack> remainingItems = player.level().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, input, player.level());

		for (int y = 0; y < input.height(); y++) {
			for (int x = 0; x < input.width(); x++) {
				int index = x + left + (y + top) * this.assemblyMatrix.getWidth();
				ItemStack currentStack = this.assemblyMatrix.getItem(index);
				ItemStack remainingStack = remainingItems.get(x + y * input.width());
				if (!currentStack.isEmpty()) {
					this.assemblyMatrix.removeItem(index, 1);
					currentStack = this.assemblyMatrix.getItem(index);
				}

				if (!remainingStack.isEmpty()) {
					if (currentStack.isEmpty()) {
						this.assemblyMatrix.setItem(index, remainingStack);
					} else if (!ItemStack.isSameItemSameComponents(currentStack, remainingStack) && !this.player.getInventory().add(remainingStack)) {
						this.player.drop(remainingStack, false);
					}
				}
			}
		}

		if (!this.tempRemainderMap.isEmpty()) {
			this.tempRemainderMap.forEach(this.assemblyMatrix::setItem);
		}
	}
}
