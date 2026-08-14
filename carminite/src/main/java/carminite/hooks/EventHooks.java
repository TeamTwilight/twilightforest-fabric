package carminite.hooks;

import carminite.event.EntityStruckByLightningEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public class EventHooks {
	public static boolean onEntityStruckByLightning(Entity entity, LightningBolt bolt) {
		var event = new EntityStruckByLightningEvent.EntityStruckByLightningEventImpl(entity, bolt);
		EntityStruckByLightningEvent.EVENT.invoker().onEntityStruckByLightning(event);
		return event.isCanceled();
	}
}