package carminite.event;

import carminite.event.impl.CarminiteEvent;
import net.minecraft.world.entity.Entity;

public abstract class EntityEvent extends CarminiteEvent {
	private final Entity entity;

	public EntityEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}