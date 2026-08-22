package twilightforest.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import twilightforest.fabric.events.neo.*;

public final class TFEvents {
	public static final Event<BreakBlock> BREAK_BLOCK = EventFactory.createArrayBacked(BreakBlock.class, callbacks -> event -> {
		for (BreakBlock callback : callbacks) {
			callback.fireBlockBreak(event);
		}
	});

	@FunctionalInterface
	public interface BreakBlock {
		void fireBlockBreak(BreakBlockEvent event);
	}

	public static final Event<LightningStruck> ENTITY_STRUCK_BY_LIGHTNING = EventFactory.createArrayBacked(LightningStruck.class, callbacks -> event -> {
		for (LightningStruck callback : callbacks) {
			callback.onEntityStruckByLightning(event);
		}
	});

	@FunctionalInterface
	public interface LightningStruck {
		void onEntityStruckByLightning(EntityStruckByLightningEvent event);
	}

	public static final Event<HarvestCheck> HARVEST_CHECK = EventFactory.createArrayBacked(HarvestCheck.class, callbacks -> event -> {
		for (HarvestCheck callback : callbacks) {
			callback.doPlayerHarvestCheck(event);
		}
	});

	@FunctionalInterface
	public interface HarvestCheck {
		void doPlayerHarvestCheck(PlayerEvent.HarvestCheck event);
	}

	public static final Event<StartTracking> START_TRACKING = EventFactory.createArrayBacked(StartTracking.class, callbacks -> event -> {
		for (StartTracking callback : callbacks) {
			callback.onStartEntityTracking(event);
		}
	});

	@FunctionalInterface
	public interface StartTracking {
		void onStartEntityTracking(PlayerEvent.StartTracking event);
	}

	public static final Event<StopTracking> STOP_TRACKING = EventFactory.createArrayBacked(StopTracking.class, callbacks -> event -> {
		for (StopTracking callback : callbacks) {
			callback.onStopEntityTracking(event);
		}
	});

	@FunctionalInterface
	public interface StopTracking {
		void onStopEntityTracking(PlayerEvent.StopTracking event);
	}

	public static final Event<ArrowLoose> ARROW_LOOSE = EventFactory.createArrayBacked(ArrowLoose.class, callbacks -> event -> {
		for (ArrowLoose callback : callbacks) {
			callback.onArrowLoose(event);
		}
	});

	@FunctionalInterface
	public interface ArrowLoose {
		void onArrowLoose(ArrowLooseEvent event);
	}

	public static final Event<EntityTickPre> ENTITY_TICK_PRE = EventFactory.createArrayBacked(EntityTickPre.class, callbacks -> event -> {
		for (EntityTickPre callback : callbacks) {
			callback.fireEntityTickPre(event);
		}
	});

	@FunctionalInterface
	public interface EntityTickPre {
		void fireEntityTickPre(EntityTickEvent.Pre event);
	}

	public static final Event<EntityTickPost> ENTITY_TICK_POST = EventFactory.createArrayBacked(EntityTickPost.class, callbacks -> event -> {
		for (EntityTickPost callback : callbacks) {
			callback.fireEntityTickPost(event);
		}
	});

	@FunctionalInterface
	public interface EntityTickPost {
		void fireEntityTickPost(EntityTickEvent.Post event);
	}

	public static final Event<PlayerTickPre> PLAYER_TICK_PRE = EventFactory.createArrayBacked(PlayerTickPre.class, callbacks -> event -> {
		for (PlayerTickPre callback : callbacks) {
			callback.firePlayerTickPre(event);
		}
	});

	@FunctionalInterface
	public interface PlayerTickPre {
		void firePlayerTickPre(PlayerTickEvent.Pre event);
	}

	public static final Event<PlayerTickPost> PLAYER_TICK_POST = EventFactory.createArrayBacked(PlayerTickPost.class, callbacks -> event -> {
		for (PlayerTickPost callback : callbacks) {
			callback.firePlayerTickPost(event);
		}
	});

	@FunctionalInterface
	public interface PlayerTickPost {
		void firePlayerTickPost(PlayerTickEvent.Post event);
	}

	public static final Event<PlayerLoggedIn> PLAYER_LOGGED_IN = EventFactory.createArrayBacked(PlayerLoggedIn.class, callbacks -> event -> {
		for (PlayerLoggedIn callback : callbacks) {
			callback.firePlayerLoggedIn(event);
		}
	});

	@FunctionalInterface
	public interface PlayerLoggedIn {
		void firePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event);
	}

	public static final Event<PlayerLoggedOut> PLAYER_LOGGED_OUT = EventFactory.createArrayBacked(PlayerLoggedOut.class, callbacks -> event -> {
		for (PlayerLoggedOut callback : callbacks) {
			callback.firePlayerLoggedOut(event);
		}
	});

	@FunctionalInterface
	public interface PlayerLoggedOut {
		void firePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event);
	}

	public static final Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createArrayBacked(PlayerRespawn.class, callbacks -> event -> {
		for (PlayerRespawn callback : callbacks) {
			callback.firePlayerRespawnEvent(event);
		}
	});

	@FunctionalInterface
	public interface PlayerRespawn{
		void firePlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event);
	}
}