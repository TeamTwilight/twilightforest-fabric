package carminite.transfer.transaction;

import java.util.ArrayList;
import org.jspecify.annotations.Nullable;

public abstract class SnapshotJournal<T extends @Nullable Object> {
	private static final Object NO_SNAPSHOT = new Object();

	private final ArrayList<T> snapshots = new ArrayList<>();

	@Nullable
	private T originalState = null;

	protected abstract T createSnapshot();

	protected abstract void revertToSnapshot(T snapshot);

	protected void releaseSnapshot(T snapshot) {}

	protected void onRootCommit(T originalState) {}

	@SuppressWarnings("unchecked")
	public void updateSnapshots(ITransactionContext transaction) {
		int currentDepth = transaction.depth();

		snapshots.ensureCapacity(currentDepth);
		for (int i = snapshots.size(); i <= currentDepth; i++) {
			snapshots.add((T) NO_SNAPSHOT);
		}

		if (snapshots.get(currentDepth) == NO_SNAPSHOT) {
			snapshots.set(currentDepth, createSnapshot());

			var transactionImpl = (Transaction) transaction;
			transactionImpl.validateOpen();
			transactionImpl.journalsToClose.add(this);
		}
	}

	void onClose(Transaction transaction, boolean wasAborted) {
		int currentDepth = transaction.depth();

		T snapshot = snapshots.remove(currentDepth);

		if (wasAborted) {
			revertToSnapshot(snapshot);
			releaseSnapshot(snapshot);
		} else if (currentDepth <= 0) {
			if (originalState == null) {
				originalState = snapshot;
				transaction.manager.rootCommitQueue.add(this);
			} else {
				releaseSnapshot(snapshot);
			}
		} else if (snapshots.get(currentDepth - 1) == NO_SNAPSHOT) {
			snapshots.set(currentDepth - 1, snapshot);
			transaction.manager.getOpenTransaction(currentDepth - 1).journalsToClose.add(this);
		} else {
			releaseSnapshot(snapshot);
		}
	}

	void callOnRootCommit() {
		T originalState = this.originalState;
		this.originalState = null;
		onRootCommit(originalState);
		releaseSnapshot(originalState);
	}
}
