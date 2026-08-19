package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.Entity;
import twilightforest.fabric.events.internal.TFEvent;

public abstract class EntityEvent extends TFEvent {
	private final Entity entity;

	public EntityEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}