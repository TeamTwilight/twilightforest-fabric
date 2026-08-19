package twilightforest.fabric.events.internal;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface ICancellableEvent {

	@MustBeInvokedByOverriders
	default void setCanceled(boolean canceled) {
		((TFEvent) this).isCanceled = canceled;
	}

	@ApiStatus.NonExtendable
	default boolean isCanceled() {
		return ((TFEvent) this).isCanceled;
	}
}