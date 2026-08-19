package twilightforest.fabric.events.internal;

public abstract class TFEvent<T extends TFEvent<T>> {
	boolean isCanceled = false;

	public abstract T post();

	protected TFEvent() {}
}