package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.LivingEntity;
import twilightforest.fabric.events.TFEvents;

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

	public static class LivingJumpEvent extends LivingEvent {
		public LivingJumpEvent(LivingEntity e) {
			super(e);
		}

		@Override
		public LivingJumpEvent post() {
			TFEvents.LIVING_JUMP.invoker().onLivingJump(this);
			return this;
		}
	}
}