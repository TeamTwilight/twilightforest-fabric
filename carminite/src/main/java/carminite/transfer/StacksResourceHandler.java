package carminite.transfer;

import carminite.transfer.resource.IResource;
import carminite.transfer.transaction.ITransactionContext;
import carminite.transfer.transaction.SnapshotJournal;
import carminite.util.IValueIOSerializable;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class StacksResourceHandler<S, T extends IResource> implements IResourceHandler<T>, IValueIOSerializable {
	public static final String VALUE_IO_KEY = "stacks";

	protected final S emptyStack;
	protected NonNullList<S> stacks;
	protected final Codec<NonNullList<S>> codec;

	private final ArrayList<StackJournal> snapshotJournals;

	protected StacksResourceHandler(int size, S emptyStack, Codec<S> stackCodec) {
		this(NonNullList.withSize(size, emptyStack), emptyStack, stackCodec);
	}

	protected StacksResourceHandler(NonNullList<S> stacks, S emptyStack, Codec<S> stackCodec) {
		this.emptyStack = emptyStack;
		this.stacks = mutableCopyOf(stacks);
		// Don't use NonNullList.codecOf because it creates an unmodifiable list
		this.codec = stackCodec.listOf().xmap(this::mutableCopyOf, Function.identity());
		this.snapshotJournals = new ArrayList<>(this.stacks.size());
		updateStacksSize();
	}

	/**
	 * Creates a {@link NonNullList} that is a fixed-size mutable copy of the given collection.
	 */
	private NonNullList<S> mutableCopyOf(Collection<S> list) {
		return NonNullList.of(emptyStack, (S[]) list.toArray(Object[]::new));
	}

	/**
	 * Changes the list of stacks. Can change the size of the handler.
	 *
	 * @param stacks The new list of stacks. A shallow copy will be made.
	 */
	protected void setStacks(NonNullList<S> stacks) {
		this.stacks = mutableCopyOf(stacks);
		updateStacksSize();
	}

	private void updateStacksSize() {
		snapshotJournals.ensureCapacity(stacks.size());
		// Add missing entries
		while (snapshotJournals.size() < stacks.size()) {
			snapshotJournals.add(new StackJournal(snapshotJournals.size()));
		}
		// Remove superfluous entries
		if (snapshotJournals.size() > stacks.size()) {
			snapshotJournals.subList(stacks.size(), snapshotJournals.size()).clear();
		}
	}

	@Override
	public void serialize(ValueOutput output) {
		output.store(VALUE_IO_KEY, codec, stacks);
	}

	@Override
	public void deserialize(ValueInput input) {
		input.read(VALUE_IO_KEY, codec).ifPresent(l -> {
			stacks = l;
			updateStacksSize();
		});
	}

	public void set(int index, T resource, int amount) {
		TransferPreconditions.checkNonNegative(amount);
		if (resource.isEmpty() && amount > 0) {
			throw new IllegalArgumentException("Resource is empty but the amount is positive: " + amount);
		}

		S oldContents = stacks.set(index, getStackFrom(resource, amount));
		onContentsChanged(index, oldContents);
	}

	protected abstract T getResourceFrom(S stack);

	protected abstract int getAmountFrom(S stack);

	protected abstract S getStackFrom(T resource, int amount);

	protected abstract S copyOf(S stack);

	protected boolean matches(S stack, T resource) {
		return getResourceFrom(stack).equals(resource);
	}

	@Override
	public boolean isValid(int index, T resource) {
		return true;
	}

	protected abstract int getCapacity(int index, T resource);

	protected void onContentsChanged(int index, S previousContents) {}

	public NonNullList<S> copyToList() {
		return mutableCopyOf(stacks);
	}

	@Override
	public int size() {
		return stacks.size();
	}

	@Override
	public T getResource(int index) {
		Objects.checkIndex(index, size());
		return getResourceFrom(stacks.get(index));
	}

	@Override
	public long getAmountAsLong(int index) {
		Objects.checkIndex(index, size());
		return getAmountFrom(stacks.get(index));
	}

	@Override
	public long getCapacityAsLong(int index, T resource) {
		Objects.checkIndex(index, size());
		return resource.isEmpty() || isValid(index, resource) ? getCapacity(index, resource) : 0;
	}

	@Override
	public int insert(int index, T resource, int amount, ITransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		S currentStack = stacks.get(index);
		int currentAmount = getAmountFrom(currentStack);

		if ((currentAmount == 0 || matches(currentStack, resource)) && isValid(index, resource)) {
			int inserted = Math.min(amount, getCapacity(index, resource) - currentAmount);

			if (inserted > 0) {
				snapshotJournals.get(index).updateSnapshots(transaction);
				stacks.set(index, getStackFrom(resource, currentAmount + inserted));
				return inserted;
			}
		}

		return 0;
	}

	@Override
	public int extract(int index, T resource, int amount, ITransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		S currentStack = stacks.get(index);

		if (matches(currentStack, resource)) {
			int currentAmount = getAmountFrom(currentStack);
			int extracted = Math.min(amount, currentAmount);

			if (extracted > 0) {
				snapshotJournals.get(index).updateSnapshots(transaction);
				stacks.set(index, getStackFrom(resource, currentAmount - extracted));
				return extracted;
			}
		}

		return 0;
	}

	private class StackJournal extends SnapshotJournal<S> {
		private final int index;

		private StackJournal(int index) {
			this.index = index;
		}

		@Override
		protected S createSnapshot() {
			return copyOf(stacks.get(index));
		}

		@Override
		protected void revertToSnapshot(S snapshot) {
			stacks.set(index, snapshot);
		}

		@Override
		protected void onRootCommit(S originalState) {
			onContentsChanged(index, originalState);
		}
	}
}