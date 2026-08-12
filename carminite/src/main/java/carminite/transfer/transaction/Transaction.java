package carminite.transfer.transaction;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public final class Transaction implements AutoCloseable, ITransactionContext {
	private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

	public static Transaction openRoot() {
		return TransactionManager.getManagerForThread().open(null, STACK_WALKER.getCallerClass());
	}

	public static Transaction open(@Nullable ITransactionContext parent) {
		return TransactionManager.getManagerForThread().open(parent, STACK_WALKER.getCallerClass());
	}

	public static Lifecycle getLifecycle() {
		TransactionManager manager = TransactionManager.getManagerForThread();
		int currentDepth = manager.currentDepth;
		if (currentDepth == -1) {
			return manager.processingRootCommitQueue ? Lifecycle.ROOT_CLOSING : Lifecycle.NONE;
		} else {
			return manager.stack.get(currentDepth).open ? Lifecycle.OPEN : Lifecycle.CLOSING;
		}
	}

	@SuppressWarnings("DeprecatedIsStillUsed")
	@Nullable
	@Deprecated
	public static ITransactionContext getCurrentOpenedTransaction() {
		TransactionManager manager = TransactionManager.getManagerForThread();
		if (manager.currentDepth == -1) return null;

		Transaction transaction = manager.stack.get(manager.currentDepth);
		if (transaction.open) return transaction;
		throw new IllegalStateException("`getCurrentOpenedTransaction()` cannot be called while a transaction is closing.");
	}

	public void commit() {
		close(false);
	}

	@Override
	public void close() {
		if (manager.currentDepth >= depth && open) {
			close(true);
		}
	}

	@Override
	public int depth() {
		manager.validateCurrentThread();
		return depth;
	}

	@Override
	public String toString() {
		return "Transaction[depth=%d, open=%s, thread=%s]".formatted(depth, open, manager.thread.getName());
	}

	final TransactionManager manager;
	private final int depth;
	boolean open = false;
	final List<SnapshotJournal<?>> journalsToClose = new ArrayList<>();
	Class<?> callerClass;

	Transaction(TransactionManager manager, int depth, Class<?> callerClass) {
		this.manager = manager;
		this.depth = depth;
		this.callerClass = callerClass;
	}

	void validateOpen() {
		if (!open) {
			throw new IllegalStateException("Transaction operation cannot be applied to a closed or closing transaction.");
		}
	}

	String getDebugName() {
		return callerClass.toString();
	}

	private void close(boolean wasAborted) {
		manager.validateCurrentTransaction(this);
		validateOpen();
		open = false;
		RuntimeException closeException = null;

		for (SnapshotJournal<?> journal : journalsToClose) {
			try {
				journal.onClose(this, wasAborted);
			} catch (Exception exception) {
				if (closeException == null) {
					closeException = new RuntimeException("Encountered an exception while invoking a transaction close callback.", exception);
				} else {
					closeException.addSuppressed(exception);
				}
			}
		}

		journalsToClose.clear();

		manager.currentDepth--;

		if (manager.currentDepth == -1) {
			closeException = manager.processRootCommitQueue(closeException);
		}

		if (closeException != null) {
			throw closeException;
		}
	}

	public enum Lifecycle {
		NONE,
		OPEN,
		CLOSING,
		ROOT_CLOSING;
	}
}