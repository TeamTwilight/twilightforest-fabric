package net.neoforged.neoforge.items;

import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

public interface IItemHandler {
	int getSlots();

	ItemStack getStackInSlot(int slot);

	ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

	ItemStack extractItem(int slot, int amount, boolean simulate);

	int getSlotLimit(int slot);

	boolean isItemValid(int slot, ItemStack stack);

	static IItemHandler of(Container container) {
		return new ContainerBackedItemHandler(container);
	}

	final class ContainerBackedItemHandler implements IItemHandler {
		private final Container container;

		private ContainerBackedItemHandler(Container container) {
			this.container = container;
		}

		@Override
		public int getSlots() {
			return this.container.getContainerSize();
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return this.validSlot(slot) ? this.container.getItem(slot) : ItemStack.EMPTY;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!this.validSlot(slot) || stack.isEmpty() || !this.isItemValid(slot, stack)) {
				return stack;
			}

			ItemStack current = this.container.getItem(slot);
			int limit = Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
			if (!current.isEmpty()) {
				if (!ItemStack.isSameItemSameComponents(current, stack) || current.getCount() >= limit) {
					return stack;
				}
				limit -= current.getCount();
			}

			if (limit <= 0) {
				return stack;
			}

			int inserted = Math.min(limit, stack.getCount());
			ItemStack remainder = stack.copy();
			remainder.shrink(inserted);

			if (!simulate) {
				if (current.isEmpty()) {
					this.container.setItem(slot, stack.copyWithCount(inserted));
				} else {
					current.grow(inserted);
					this.container.setItem(slot, current);
				}
				this.container.setChanged();
			}

			return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (!this.validSlot(slot) || amount <= 0) {
				return ItemStack.EMPTY;
			}
			ItemStack current = this.container.getItem(slot);
			if (current.isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack extracted = current.copyWithCount(Math.min(amount, current.getCount()));
			if (!simulate) {
				current.shrink(extracted.getCount());
				this.container.setItem(slot, current.isEmpty() ? ItemStack.EMPTY : current);
				this.container.setChanged();
			}
			return extracted;
		}

		@Override
		public int getSlotLimit(int slot) {
			return this.validSlot(slot) ? this.container.getMaxStackSize() : 0;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (!this.validSlot(slot) || !this.container.canPlaceItem(slot, stack)) {
				return false;
			}
			return !(this.container instanceof WorldlyContainer worldly) || worldly.canPlaceItemThroughFace(slot, stack, null);
		}

		private boolean validSlot(int slot) {
			return slot >= 0 && slot < this.container.getContainerSize();
		}
	}
}
