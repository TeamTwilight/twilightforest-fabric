package carminite.event;

import carminite.event.internal.ICancellableEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArrowLooseEvent {
	public static final Event<Loose> EVENT = EventFactory.createArrayBacked(Loose.class, callbacks -> event -> {
		for (Loose callback : callbacks) {
			callback.onArrowLoose(event);
		}
	});

	@FunctionalInterface
	public interface Loose {
		void onArrowLoose(ArrowLooseEventImpl event);
	}

	public static class ArrowLooseEventImpl extends PlayerEvent implements ICancellableEvent {
		private final ItemStack bow;
		private final Level level;
		private final boolean hasAmmo;
		private int charge;

		public ArrowLooseEventImpl(Player player, ItemStack bow, Level level, int charge, boolean hasAmmo) {
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
	}
}