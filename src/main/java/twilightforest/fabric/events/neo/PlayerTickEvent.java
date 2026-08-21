package twilightforest.fabric.events.neo;

import net.minecraft.world.entity.player.Player;
import twilightforest.fabric.events.TFEvents;

public abstract class PlayerTickEvent extends PlayerEvent {
	protected PlayerTickEvent(Player player) {
		super(player);
	}

	public static class Pre extends PlayerTickEvent {
		public Pre(Player player) {
			super(player);
		}

		@Override
		public Pre post() {
			TFEvents.PLAYER_TICK_PRE.invoker().firePlayerTickPre(this);
			return this;
		}
	}

	public static class Post extends PlayerTickEvent {
		public Post(Player player) {
			super(player);
		}

		@Override
		public Post post() {
			TFEvents.PLAYER_TICK_POST.invoker().firePlayerTickPost(this);
			return this;
		}
	}
}