package twilightforest.entity;

import net.minecraft.world.entity.Entity;

/**
 * 1:1 port of upstream {@code twilightforest.entity.IBreathAttacker} — implemented by
 * mobs that have a continuous breath/cone attack (e.g. Hydra fire/ice heads, Snow
 * Queen ice cone). Lets goal classes drive the breathing state without coupling to a
 * specific mob type.
 */
public interface IBreathAttacker {

	boolean isBreathing();

	void setBreathing(boolean flag);

	/**
	 * Deal damage for our breath attack.
	 */
	void doBreathAttack(Entity target);
}
