package carminite.event;

import carminite.event.impl.ICancellableEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public final class EntityStruckByLightningEvent {
	public static final Event<Struck> EVENT = EventFactory.createArrayBacked(Struck.class, callbacks -> event -> {
		for (Struck callback : callbacks) {
			callback.onEntityStruckByLightning(event);
		}
	});

	@FunctionalInterface
	public interface Struck {
		void onEntityStruckByLightning(EntityStruckByLightningEventImpl event);
	}

	public static class EntityStruckByLightningEventImpl extends EntityEvent implements ICancellableEvent {
		private final LightningBolt lightning;

		public EntityStruckByLightningEventImpl(Entity entity, LightningBolt lightning) {
			super(entity);
			this.lightning = lightning;
		}

		public LightningBolt getLightning() {
			return lightning;
		}
	}
}