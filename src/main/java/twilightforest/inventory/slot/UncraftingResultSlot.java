package twilightforest.inventory.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.inventory.UncraftingContainer;

public class UncraftingResultSlot extends ResultSlot {

	private final Player player;
	private final Container inputSlot;
	private final UncraftingContainer uncraftingMatrix;
	private final CraftingContainer assemblyMatrix;

	public UncraftingResultSlot(Player player, Container input, Container uncraftingMatrix, Container assemblyMatrix, Container result, int slotIndex, int x, int y) {
		super(player, (CraftingContainer) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (UncraftingContainer) uncraftingMatrix;
		this.assemblyMatrix = (CraftingContainer) assemblyMatrix;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		// let's see, if the assembly matrix can produce this item, then it's a normal recipe, if not, it's combined.  Will that work?
		boolean combined = true;

		for (Recipe<CraftingContainer> recipe : player.getLevel().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, this.assemblyMatrix, this.player.getLevel())) {
			if (ItemStack.isSameItemSameTags(recipe.getResultItem(), stack)) {
				combined = false;
				break;
			}
		}

		if (combined) {
			// charge the player before the stacks empty
			if (this.uncraftingMatrix.recraftingCost > 0) {
				this.player.giveExperienceLevels(-this.uncraftingMatrix.recraftingCost);
			}

			// if we are using a combined recipe, wipe the uncrafting matrix and decrement the input appropriately
			for (int i = 0; i < uncraftingMatrix.getContainerSize(); i++) {
				this.uncraftingMatrix.setItem(i, ItemStack.EMPTY);
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		//VanillaCopy of the super method, but altered to work with the assembly matrix
		this.checkTakeAchievements(stack);

		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> remainingItems = player.level.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.assemblyMatrix, player.level);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		for(int i = 0; i < remainingItems.size(); ++i) {
			ItemStack currentStack = this.assemblyMatrix.getItem(i);
			ItemStack remainingStack = remainingItems.get(i);
			if (!currentStack.isEmpty()) {
				this.assemblyMatrix.removeItem(i, 1);
				currentStack = this.assemblyMatrix.getItem(i);
			}

			if (!remainingStack.isEmpty()) {
				if (currentStack.isEmpty()) {
					this.assemblyMatrix.setItem(i, remainingStack);
				} else if (ItemStack.isSame(currentStack, remainingStack) && ItemStack.tagMatches(currentStack, remainingStack)) {
					remainingStack.grow(currentStack.getCount());
					this.assemblyMatrix.setItem(i, remainingStack);
				} else if (!this.player.getInventory().add(remainingStack)) {
					this.player.drop(remainingStack, false);
				}
			}
		}
	}
}
