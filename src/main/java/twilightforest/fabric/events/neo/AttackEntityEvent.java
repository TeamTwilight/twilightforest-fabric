package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public class AttackEntityEvent extends PlayerEvent implements ICancellableEvent {
	private final Entity target;

	public AttackEntityEvent(Player player, Entity target) {
		super(player);
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}

	@Override
	public AttackEntityEvent post() {
		TFEvents.ATTACK_ENTITY.invoker().onPlayerAttackTarget(this);
		return this;
	}
}