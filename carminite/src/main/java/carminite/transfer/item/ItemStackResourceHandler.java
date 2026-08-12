package carminite.transfer.item;

import java.util.Objects;

import carminite.transfer.IResourceHandler;
import carminite.transfer.TransferPreconditions;
import carminite.transfer.transaction.ITransactionContext;
import carminite.transfer.transaction.SnapshotJournal;
import carminite.util.IValueIOSerializable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class ItemStackResourceHandler extends SnapshotJournal<ItemStack> implements IResourceHandler<ItemResource>, IValueIOSerializable {
	public static final String VALUE_IO_KEY = "stack";

	protected abstract ItemStack getStack();

	protected abstract void setStack(ItemStack stack);

	protected boolean isValid(ItemResource resource) {
		return true;
	}

	protected int getCapacity(ItemResource resource) {
		return resource.isEmpty() ? Item.ABSOLUTE_MAX_STACK_SIZE : Math.min(resource.getMaxStackSize(), Item.ABSOLUTE_MAX_STACK_SIZE);
	}

	@Override
	public final int size() {
		return 1;
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, ITransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		ItemStack currentStack = getStack();

		if ((currentStack.isEmpty() || resource.matches(currentStack)) && isValid(resource)) {
			int insertedAmount = Math.min(amount, getCapacity(resource) - currentStack.getCount());

			if (insertedAmount > 0) {
				updateSnapshots(transaction);
				currentStack = getStack();

				if (currentStack.isEmpty()) {
					currentStack = resource.toStack(insertedAmount);
				} else {
					currentStack.grow(insertedAmount);
				}

				setStack(currentStack);
				return insertedAmount;
			}
		}

		return 0;
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, ITransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		ItemStack currentStack = getStack();

		if (resource.matches(currentStack)) {
			int extracted = Math.min(currentStack.getCount(), amount);

			if (extracted > 0) {
				this.updateSnapshots(transaction);
				currentStack = getStack();
				currentStack.shrink(extracted);
				setStack(currentStack);

				return extracted;
			}
		}

		return 0;
	}

	@Override
	public ItemResource getResource(int index) {
		Objects.checkIndex(index, size());
		return ItemResource.of(getStack());
	}

	@Override
	public long getAmountAsLong(int index) {
		Objects.checkIndex(index, size());
		return getStack().getCount();
	}

	@Override
	public long getCapacityAsLong(int index, ItemResource resource) {
		Objects.checkIndex(index, size());
		return resource.isEmpty() || isValid(resource) ? getCapacity(resource) : 0;
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmpty(resource);
		return isValid(resource);
	}

	@Override
	protected ItemStack createSnapshot() {
		ItemStack original = getStack();
		setStack(original.copy());
		return original;
	}

	@Override
	protected void revertToSnapshot(ItemStack snapshot) {
		setStack(snapshot);
	}

	@Override
	public void serialize(ValueOutput output) {
		if (!this.getStack().isEmpty()) {
			output.store(VALUE_IO_KEY, ItemStack.CODEC, this.getStack());
		}
	}

	@Override
	public void deserialize(ValueInput input) {
		this.setStack(input.read(VALUE_IO_KEY, ItemStack.CODEC).orElse(ItemStack.EMPTY));
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + getStack() + "]";
	}
}