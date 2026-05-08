package twilightforest.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class UncraftingInputContainer implements Container {
	private ItemStack stack = ItemStack.EMPTY;
	private final UncraftingMenu container;

	public UncraftingInputContainer(UncraftingMenu container) {
		this.container = container;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return index == 0 ? this.stack : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		if (index != 0 || this.stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack takenStack;
		if (this.stack.getCount() <= amount) {
			takenStack = this.stack;
			this.stack = ItemStack.EMPTY;
		} else {
			takenStack = this.stack.split(amount);
		}
		this.container.slotsChanged(this);
		return takenStack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		if (index == 0 && !this.stack.isEmpty()) {
			ItemStack stack = this.stack;
			this.stack = ItemStack.EMPTY;
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
			this.container.slotsChanged(this);
		}
	}

	@Override
	public void setChanged() {
		this.container.slotsChanged(this);
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		this.stack = ItemStack.EMPTY;
	}
}
