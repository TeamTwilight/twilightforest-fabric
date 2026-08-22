package twilightforest.fabric.events.neo;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.world.entity.player.Player;
import twilightforest.fabric.events.TFEvents;

public abstract class AdvancementEvent extends PlayerEvent {
	private final AdvancementHolder advancement;

	public AdvancementEvent(Player player, AdvancementHolder advancement) {
		super(player);
		this.advancement = advancement;
	}

	public AdvancementHolder getAdvancement() {
		return advancement;
	}

	public static class AdvancementEarnEvent extends AdvancementEvent {
		public AdvancementEarnEvent(Player player, AdvancementHolder earned) {
			super(player, earned);
		}

		@Override
		public AdvancementEarnEvent post() {
			TFEvents.ADVANCEMENT_EARNED.invoker().onAdvancementEarnedEvent(this);
			return this;
		}
	}
}