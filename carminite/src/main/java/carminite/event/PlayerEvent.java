package carminite.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PlayerEvent extends LivingEvent{
	private final Player player;

	public PlayerEvent(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public Player getEntity() {
		return player;
	}

	public static final Event<Harvest> HARVEST = EventFactory.createArrayBacked(Harvest.class, callbacks -> event -> {
		for (Harvest callback : callbacks) {
			callback.doPlayerHarvestCheck(event);
		}
	});

	@FunctionalInterface
	public interface Harvest {
		void doPlayerHarvestCheck(HarvestCheckImpl event);
	}

	public static class HarvestCheckImpl extends PlayerEvent {
		private final BlockState state;
		private final BlockGetter level;
		private final BlockPos pos;
		private boolean success;

		public HarvestCheckImpl(Player player, BlockState state, BlockGetter level, BlockPos pos, boolean success) {
			super(player);
			this.state = state;
			this.level = level;
			this.pos = pos;
			this.success = success;
		}

		public BlockState getTargetBlock() {
			return this.state;
		}

		public BlockGetter getLevel() {
			return level;
		}

		public BlockPos getPos() {
			return pos;
		}

		public boolean canHarvest() {
			return this.success;
		}

		public void setCanHarvest(boolean success) {
			this.success = success;
		}
	}
}