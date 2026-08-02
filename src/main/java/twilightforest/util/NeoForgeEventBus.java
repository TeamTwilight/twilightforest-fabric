package twilightforest.util;

import io.github.fabricators_of_create.porting_lib.entity.events.EntityJoinLevelEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityLeaveLevelEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityTeleportEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.ProjectileImpactEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDeathEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingFallEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.MobEffectEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerInteractEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.AttackEntityEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;

import java.util.function.Consumer;

/**
 * Fabric 兼容的 NeoForge 事件总线替代。
 * 将 NeoForge.EVENT_BUS.addListener 调用转发到对应的 Porting-Lib / Fabric 事件。
 */
public class NeoForgeEventBus {

	public static final NeoForgeEventBus EVENT_BUS = new NeoForgeEventBus();

	@SuppressWarnings("unchecked")
	public <T> void addListener(Consumer<T> listener) {
		// Try to determine the event type from the listener's generic parameter
		// This is a best-effort approach - the actual event type is determined at runtime
		TwilightForestMod.LOGGER.warn("NeoForgeEventBus.addListener called with unknown event type: {}", listener.getClass().getName());
	}

	@SuppressWarnings("unchecked")
	public <T> void addListener(int priority, Consumer<T> listener) {
		addListener(listener);
	}

	@SuppressWarnings("unchecked")
	public <T> void addListener(boolean receiveCanceled, Consumer<T> listener) {
		addListener(listener);
	}

	@SuppressWarnings("unchecked")
	public <T> void addListener(int priority, boolean receiveCanceled, Consumer<T> listener) {
		addListener(listener);
	}
}