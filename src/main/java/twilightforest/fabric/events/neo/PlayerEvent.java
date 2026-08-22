package twilightforest.fabric.events.neo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.fabric.events.TFEvents;

public abstract class PlayerEvent extends LivingEvent {
	private final Player player;

	public PlayerEvent(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public Player getEntity() {
		return player;
	}

	public static class HarvestCheck extends PlayerEvent {
		private final BlockState state;
		private final BlockGetter level;
		private final BlockPos pos;
		private boolean success;

		public HarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos, boolean success) {
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

		@Override
		public HarvestCheck post() {
			TFEvents.HARVEST_CHECK.invoker().doPlayerHarvestCheck(this);
			return this;
		}
	}

	public static class StartTracking extends PlayerEvent {
		private final Entity target;

		public StartTracking(Player player, Entity target) {
			super(player);
			this.target = target;
		}

		public Entity getTarget() {
			return target;
		}

		@Override
		public StartTracking post() {
			TFEvents.START_TRACKING.invoker().onStartEntityTracking(this);
			return this;
		}
	}

	public static class StopTracking extends PlayerEvent {
		private final Entity target;

		public StopTracking(Player player, Entity target) {
			super(player);
			this.target = target;
		}

		public Entity getTarget() {
			return target;
		}

		@Override
		public StopTracking post() {
			TFEvents.STOP_TRACKING.invoker().onStopEntityTracking(this);
			return this;
		}
	}

	public static class PlayerLoggedInEvent extends PlayerEvent {
		public PlayerLoggedInEvent(Player player) {
			super(player);
		}

		@Override
		public PlayerLoggedInEvent post() {
			TFEvents.PLAYER_LOGGED_IN.invoker().firePlayerLoggedIn(this);
			return this;
		}
	}

	public static class PlayerLoggedOutEvent extends PlayerEvent {
		public PlayerLoggedOutEvent(Player player) {
			super(player);
		}

		@Override
		public PlayerLoggedOutEvent post() {
			TFEvents.PLAYER_LOGGED_OUT.invoker().firePlayerLoggedOut(this);
			return this;
		}
	}

	public static class PlayerRespawnEvent extends PlayerEvent {
		private final boolean endConquered;

		public PlayerRespawnEvent(Player player, boolean endConquered) {
			super(player);
			this.endConquered = endConquered;
		}

		public boolean isEndConquered() {
			return this.endConquered;
		}

		@Override
		public PlayerRespawnEvent post() {
			TFEvents.PLAYER_RESPAWN.invoker().firePlayerRespawnEvent(this);
			return this;
		}
	}
}