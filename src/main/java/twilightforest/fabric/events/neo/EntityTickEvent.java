package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.Entity;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public abstract class EntityTickEvent extends EntityEvent {
	protected EntityTickEvent(Entity entity) {
		super(entity);
	}

	public static class Pre extends EntityTickEvent implements ICancellableEvent {
		public Pre(Entity entity) {
			super(entity);
		}

		@Override
		public void setCanceled(boolean canceled) {
			ICancellableEvent.super.setCanceled(canceled);
		}

		@Override
		public Pre post() {
			TFEvents.ENTITY_TICK_PRE.invoker().fireEntityTickPre(this);
			return this;
		}
	}

	public static class Post extends EntityTickEvent {
		public Post(Entity entity) {
			super(entity);
		}

		@Override
		public Post post() {
			TFEvents.ENTITY_TICK_POST.invoker().fireEntityTickPost(this);
			return this;
		}
	}
}