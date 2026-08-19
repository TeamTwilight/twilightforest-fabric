package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public class ArrowLooseEvent extends PlayerEvent implements ICancellableEvent {
	private final ItemStack bow;
	private final Level level;
	private final boolean hasAmmo;
	private int charge;

	public ArrowLooseEvent(Player player, ItemStack bow, Level level, int charge, boolean hasAmmo) {
		super(player);
		this.bow = bow;
		this.level = level;
		this.charge = charge;
		this.hasAmmo = hasAmmo;
	}

	public ItemStack getBow() {
		return this.bow;
	}

	public Level getLevel() {
		return this.level;
	}

	public boolean hasAmmo() {
		return this.hasAmmo;
	}

	public int getCharge() {
		return this.charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	@Override
	public ArrowLooseEvent post() {
		TFEvents.ARROW_LOOSE.invoker().onArrowLoose(this);
		return this;
	}
}