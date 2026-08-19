package twilightforest.fabric.util;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public final class Lazy<T> implements Supplier<T> {
	public static <T> Lazy<T> of(Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	public synchronized void invalidate() {
		this.cachedValue = null;
	}

	private final Supplier<T> delegate;

	@Nullable
	private volatile T cachedValue;

	private Lazy(Supplier<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public T get() {
		T ret = cachedValue;
		if (ret == null) {
			synchronized (this) {
				ret = cachedValue;
				if (ret == null) {
					cachedValue = ret = delegate.get();
					if (ret == null) {
						throw new IllegalStateException("Lazy value cannot be null, but supplier returned null: " + delegate);
					}
				}
			}
		}
		return ret;
	}
}