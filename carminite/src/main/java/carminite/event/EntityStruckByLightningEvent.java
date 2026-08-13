package carminite.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public final class EntityStruckByLightningEvent {
	public static final Event<Struck> EVENT = EventFactory.createArrayBacked(Struck.class, callbacks -> (entity, lightning) -> {
		for (Struck callback : callbacks) {
			if (callback.onEntityStruckByLightning(entity, lightning)) {
				return true;
			}
		}
		return false;
	});

	@FunctionalInterface
	public interface Struck {
		boolean onEntityStruckByLightning(Entity entity, LightningBolt lightning);
	}
}