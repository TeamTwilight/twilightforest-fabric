package carminite.event.impl;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface ICancellableEvent {

	@MustBeInvokedByOverriders
	default void setCanceled(boolean canceled) {
		((CarminiteEvent) this).isCanceled = canceled;
	}

	@ApiStatus.NonExtendable
	default boolean isCanceled() {
		return ((CarminiteEvent) this).isCanceled;
	}
}