package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.LivingEntity;

public abstract class LivingEvent extends EntityEvent {
	private final LivingEntity livingEntity;

	public LivingEvent(LivingEntity entity) {
		super(entity);
		livingEntity = entity;
	}

	@Override
	public LivingEntity getEntity() {
		return livingEntity;
	}
}