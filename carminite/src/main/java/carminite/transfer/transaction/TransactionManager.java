package carminite.transfer.transaction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.jspecify.annotations.Nullable;

final class TransactionManager {
	private static final ThreadLocal<TransactionManager> MANAGERS = ThreadLocal.withInitial(TransactionManager::new);
	final Thread thread = Thread.currentThread();

	final List<Transaction> stack = new ArrayList<>();
	int currentDepth = -1;

	final Queue<SnapshotJournal<?>> rootCommitQueue = new ArrayDeque<>();
	boolean processingRootCommitQueue = false;

	static TransactionManager getManagerForThread() {
		return MANAGERS.get();
	}

	Transaction open(@Nullable ITransactionContext parent, Class<?> callerClass) {
		if (parent != null) {
			Transaction parentImpl = (Transaction) parent;
			validateCurrentTransaction(parentImpl);
			parentImpl.validateOpen();
		} else if (currentDepth >= 0) {
			String currentRoot = getOpenTransaction(0).getDebugName();
			throw new IllegalStateException("A root transaction of `" + currentRoot + "` is already active on this thread " + thread + " when `" + callerClass + "` tried to open.");
		}

		Transaction current;
		if (stack.size() == ++currentDepth) {
			current = new Transaction(this, currentDepth, callerClass);
			stack.add(current);
		} else {
			current = stack.get(currentDepth);
			current.callerClass = callerClass;
		}
		current.open = true;
		return current;
	}

	Transaction getOpenTransaction(int depth) {
		validateCurrentThread();

		if (depth < 0) {
			throw new IndexOutOfBoundsException("Depth may not be negative.");
		}

		if (depth > this.currentDepth) {
			throw new IndexOutOfBoundsException("There is no open transaction for depth `" + depth + "`");
		}

		Transaction transaction = this.stack.get(depth);
		transaction.validateOpen();
		return transaction;
	}

	void validateCurrentThread() {
		if (Thread.currentThread() != thread) {
			String errorMessage = String.format(
				"Attempted to access transaction state from thread %s, but this transaction is only valid on thread %s.",
				Thread.currentThread().getName(),
				thread.getName());
			throw new IllegalStateException(errorMessage);
		}
	}

	void validateCurrentTransaction(Transaction transaction) {
		validateCurrentThread();

		if (currentDepth != -1 && stack.get(currentDepth) == transaction)
			return;

		String errorMessage = String.format(
			"Transaction function was called on a transaction (%s) with depth `%d`, " +
				"but the current transaction (%s) has depth `%d`.",
			transaction.getDebugName(),
			transaction.depth(),
			stack.get(currentDepth).getDebugName(),
			currentDepth);
		throw new IllegalStateException(errorMessage);
	}

	@Nullable
	RuntimeException processRootCommitQueue(@Nullable RuntimeException closeException) {
		if (processingRootCommitQueue) {
			return closeException;
		}

		processingRootCommitQueue = true;

		while (!rootCommitQueue.isEmpty()) {
			SnapshotJournal<?> journal = rootCommitQueue.remove();
			try {
				journal.callOnRootCommit();
			} catch (Exception exception) {
				if (closeException == null) {
					closeException = new RuntimeException("Encountered an exception while invoking a journal's onRootCommit method.", exception);
				} else {
					closeException.addSuppressed(exception);
				}
			}
		}

		processingRootCommitQueue = false;
		return closeException;
	}

	private TransactionManager() {}
}