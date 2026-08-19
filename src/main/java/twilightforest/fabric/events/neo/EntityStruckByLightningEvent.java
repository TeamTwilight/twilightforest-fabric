package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public class EntityStruckByLightningEvent extends EntityEvent implements ICancellableEvent {
	private final LightningBolt lightning;

	public EntityStruckByLightningEvent(Entity entity, LightningBolt lightning) {
		super(entity);
		this.lightning = lightning;
	}

	public LightningBolt getLightning() {
		return lightning;
	}

	@Override
	public EntityStruckByLightningEvent post() {
		TFEvents.ENTITY_STRUCK_BY_LIGHTNING.invoker().onEntityStruckByLightning(this);
		return this;
	}
}