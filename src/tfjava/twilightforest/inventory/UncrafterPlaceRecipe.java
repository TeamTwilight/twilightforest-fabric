package twilightforest.inventory;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UncrafterPlaceRecipe<I extends RecipeInput, R extends Recipe<I>> extends ServerPlaceRecipe<I, R> implements UncraftingPlaceRecipe<Integer> {
	private static final int MATRIX_OFFSET = 11;

	public UncrafterPlaceRecipe(RecipeBookMenu<I, R> menu) {
		super(menu);
	}

	@Override
	public void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<R> recipe, boolean placeAll) {
		if (recipe != null && player.getRecipeBook().contains(recipe)) {
			this.inventory = player.getInventory();
			if (this.tryClearGrid() || player.isCreative()) {
				this.stackedContents.clear();
				player.getInventory().fillStackedContents(this.stackedContents);
				this.menu.fillCraftSlotsStackedContents(this.stackedContents);
				if (this.stackedContents.canCraft(recipe.value(), null)) {
					this.handleRecipeClicked(recipe, placeAll);
				} else {
					this.clearGrid();
					player.connection.send(new ClientboundPlaceGhostRecipePacket(player.containerMenu.containerId, recipe));
				}
				player.getInventory().setChanged();
			}
		}
	}

	@Override
	protected void handleRecipeClicked(RecipeHolder<R> recipeHolder, boolean placeAll) {
		boolean recipeMatches = this.menu.recipeMatches(recipeHolder);
		int maxCraftable = this.stackedContents.getBiggestCraftableStack(recipeHolder, null);
		if (recipeMatches) {
			for (int slot = 0; slot < this.menu.getSize(); ++slot) {
				if (slot != this.menu.getResultSlotIndex()) {
					ItemStack itemStack = this.menu.getSlot(slot).getItem();
					if (!itemStack.isEmpty() && Math.min(maxCraftable, itemStack.getMaxStackSize()) < itemStack.getCount() + 1) {
						return;
					}
				}
			}
		}

		int stackSize = this.getStackSize(placeAll, maxCraftable, recipeMatches);
		IntList placements = new IntArrayList();
		if (this.stackedContents.canCraft(recipeHolder.value(), placements, stackSize)) {
			int maxStackSize = stackSize;
			for (int stackingIndex : placements) {
				int max = StackedContents.fromStackingIndex(stackingIndex).getMaxStackSize();
				if (max < maxStackSize) {
					maxStackSize = max;
				}
			}

			if (this.stackedContents.canCraft(recipeHolder.value(), placements, maxStackSize)) {
				this.clearGrid();
				this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipeHolder, placements.iterator(), maxStackSize);
			}
		}
	}

	@Override
	protected int getStackSize(boolean placeAll, int maxPossible, boolean recipeMatches) {
		int count = 1;
		if (placeAll) {
			count = maxPossible;
		} else if (recipeMatches) {
			count = 64;
			for (int slot = 0; slot < this.menu.getGridWidth() * this.menu.getGridHeight(); ++slot) {
				ItemStack itemStack = this.menu.getSlot(slot + MATRIX_OFFSET).getItem();
				if (!itemStack.isEmpty() && count > itemStack.getCount()) {
					count = itemStack.getCount();
				}
			}
			if (count < 64) {
				++count;
			}
		}
		return count;
	}

	private int getAmountOfFreeSlotsInInventory() {
		int count = 0;
		for (ItemStack itemStack : this.inventory.items) {
			if (itemStack.isEmpty()) {
				++count;
			}
		}
		return count;
	}

	private boolean tryClearGrid() {
		List<ItemStack> list = Lists.newArrayList();
		int freeSlots = this.getAmountOfFreeSlotsInInventory();

		if (freeSlots > 0) {
			ItemStack itemStack = this.menu.getSlot(0).getItem().copy();
			if (!itemStack.isEmpty()) {
				list.add(itemStack);
			}
		}

		for (int slotIndex = 0; slotIndex < this.menu.getGridWidth() * this.menu.getGridHeight(); ++slotIndex) {
			ItemStack itemStack = this.menu.getSlot(slotIndex + MATRIX_OFFSET).getItem().copy();
			if (!itemStack.isEmpty()) {
				int slotWithSpace = this.inventory.getSlotWithRemainingSpace(itemStack);
				if (slotWithSpace == -1 && list.size() <= freeSlots) {
					for (ItemStack queued : list) {
						if (ItemStack.isSameItem(queued, itemStack)
								&& queued.getCount() != queued.getMaxStackSize()
								&& queued.getCount() + itemStack.getCount() <= queued.getMaxStackSize()) {
							queued.grow(itemStack.getCount());
							itemStack.setCount(0);
							break;
						}
					}

					if (!itemStack.isEmpty()) {
						if (list.size() >= freeSlots) {
							return false;
						}
						list.add(itemStack);
					}
				} else if (slotWithSpace == -1) {
					return false;
				}
			}
		}
		return true;
	}
}
