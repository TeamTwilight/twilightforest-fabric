package twilightforest.fabric.events.neo;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public class LivingDeathEvent extends LivingEvent implements ICancellableEvent {
	private final DamageSource source;

	public LivingDeathEvent(LivingEntity entity, DamageSource source) {
		super(entity);
		this.source = source;
	}

	public DamageSource getSource() {
		return source;
	}

	@Override
	public LivingDeathEvent post() {
		TFEvents.LIVING_DEATH.invoker().onLivingDeath(this);
		return this;
	}
}
