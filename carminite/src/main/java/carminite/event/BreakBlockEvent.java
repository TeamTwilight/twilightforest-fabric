package carminite.event;

import carminite.event.internal.ICancellableEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class BreakBlockEvent {
	public static final Event<Break> EVENT = EventFactory.createArrayBacked(Break.class, callbacks -> event -> {
		for (Break callback : callbacks) {
			callback.fireBlockBreak(event);
		}
	});

	@FunctionalInterface
	public interface Break {
		void fireBlockBreak(BreakBlockEventImpl event);
	}

	public static class BreakBlockEventImpl extends BlockEvent implements ICancellableEvent {
		private final Player player;
		private boolean notifyClient = false;

		public BreakBlockEventImpl(Level level, BlockPos pos, BlockState state, Player player) {
			super(level, pos, state);
			this.player = player;
		}

		public Player getPlayer() {
			return player;
		}

		@Override
		public void setCanceled(boolean canceled) {
			ICancellableEvent.super.setCanceled(canceled);
		}

		public boolean shouldNotifyClient() {
			return notifyClient;
		}

		public void setNotifyClient(boolean notify) {
			this.notifyClient = notify;
		}
	}
}