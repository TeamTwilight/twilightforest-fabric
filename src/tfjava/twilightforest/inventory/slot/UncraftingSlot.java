package twilightforest.inventory.slot;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import twilightforest.config.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.recipe.UncraftingRecipe;

public class UncraftingSlot extends Slot {
	protected final Player player;
	protected final Container inputSlot;
	protected final UncraftingContainer uncraftingMatrix;
	protected final Container assemblyMatrix;

	public UncraftingSlot(Player player, Container inputSlot, UncraftingContainer uncraftingMatrix, Container assemblyMatrix, int slotNum, int x, int y) {
		super(uncraftingMatrix, slotNum, x, y);
		this.player = player;
		this.inputSlot = inputSlot;
		this.uncraftingMatrix = uncraftingMatrix;
		this.assemblyMatrix = assemblyMatrix;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public boolean mayPickup(Player player) {
		if (!this.assemblyMatrix.isEmpty()) {
			return false;
		}
		if (UncraftingMenu.isMarked(this.getItem())) {
			return false;
		}
		if (TFConfig.disableUncraftingOnly && !(this.uncraftingMatrix.menu.storedGhostRecipe instanceof UncraftingRecipe)) {
			return false;
		}
		return this.uncraftingMatrix.uncraftingCost <= player.experienceLevel || player.getAbilities().instabuild;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		if (this.uncraftingMatrix.uncraftingCost > 0) {
			this.player.giveExperienceLevels(-this.uncraftingMatrix.uncraftingCost);
		}

		for (int i = 0; i < 9; i++) {
			ItemStack transferStack = this.uncraftingMatrix.getItem(i);
			if (!transferStack.isEmpty() && !UncraftingMenu.isMarked(transferStack)) {
				this.assemblyMatrix.setItem(i, transferStack.copy());
			}
		}

		ItemStack inputStack = this.inputSlot.getItem(0);
		if (!inputStack.isEmpty()) {
			if (player instanceof ServerPlayer server) {
				TFAdvancements.UNCRAFT_ITEM.get().trigger(server, inputStack);
			}
			if (inputStack.has(DataComponents.CONTAINER)) {
				inputStack.get(DataComponents.CONTAINER).nonEmptyItems().forEach(containerStack -> {
					if (!player.getInventory().add(containerStack)) {
						player.drop(containerStack, false);
					}
				});
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		super.onTake(player, stack);
	}

	@Override
	public boolean isActive() {
		return false;
	}
}
