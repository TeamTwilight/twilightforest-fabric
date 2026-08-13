package carminite.event.hooks;

import carminite.event.EntityStruckByLightningEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public class EventHooks {
	public static boolean onEntityStruckByLightning(Entity entity, LightningBolt bolt) {
		return EntityStruckByLightningEvent.EVENT.invoker().onEntityStruckByLightning(entity, bolt);
	}
}