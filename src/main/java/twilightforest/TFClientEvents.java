package twilightforest;

import carminite.events.api.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import twilightforest.client.event.ClientGameEvents;
import twilightforest.client.event.CloudEvents;
import twilightforest.client.event.LockedBiomeToastHandler;
import twilightforest.client.event.OverlayHandler;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.client.overlay.PortalOverlay;

public final class TFClientEvents {
	private static final LockedBiomeToastHandler lockedBiomeToastEvents = LockedBiomeToastHandler.INSTANCE;
	private static final CloudEvents cloudEvents = CloudEvents.INSTANCE;
	private static final OverlayHandler overlayEvents = OverlayHandler.INSTANCE;
	private static final ClientGameEvents clientGameEvents = ClientGameEvents.INSTANCE;

	public static void init() {
		setupLockedBiomeToastEvents();
		setupCloudEvents();
		setupOverlayEvents();
		setupClientGameEvents();
	}

	private static void setupLockedBiomeToastEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(_ -> lockedBiomeToastEvents.tickLockedToastLogic());
	}

	private static void setupCloudEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(cloudEvents::tickWeatherEffects);
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(cloudEvents::renderPrecipitation);
	}

	private static void setupOverlayEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, OverlayHandler.QUEST_RAM_INDICATOR, (graphics, _) -> overlayEvents.renderIndicator(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudElementRegistry.attachElementAfter(VanillaHudElements.MOUNT_HEALTH, OverlayHandler.HOSTILE_MOUNT_HUNGER_BAR, (graphics, _) -> overlayEvents.renderHostileMountHungerBar(graphics));
		HudStatusBarHeightRegistry.addRight(OverlayHandler.HOSTILE_MOUNT_HUNGER_BAR, _ -> 10);
		HudElementRegistry.addLast(OverlayHandler.ORE_METER_STATS, (graphics, _) -> overlayEvents.renderOreMeterStats(graphics));
		HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, OverlayHandler.FORTIFICATION_SHIELD_COUNT, (graphics, _) -> overlayEvents.renderShieldCount(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudStatusBarHeightRegistry.addLeft(OverlayHandler.FORTIFICATION_SHIELD_COUNT, _ -> 10);
		HudElementRegistry.addLast(OverlayHandler.PORTAL_OVERLAY, (graphics, _) -> PortalOverlay.render(graphics));
		HudElementRegistry.addLast(OverlayHandler.ITEM_DISPLAY_OVERLAY, (graphics, _) -> ItemDisplayOverlay.render(graphics, OverlayHandler.getCameraPlayer()));
	}

	private static void setupClientGameEvents() {
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> clientGameEvents.addCustomTooltips(stack, lines));
		ClientTickEvents.END_CLIENT_TICK.register(clientGameEvents::clientTick);
		ScreenEvents.AFTER_INIT.register((_, screen, _, _) -> {
			clientGameEvents.customizeSplashes(screen);
			ScreenEvents.remove(screen).register(_ -> clientGameEvents.clearEntityRenderUtilMap());
		});
		ClientEvents.RENDER_FRAME_POST.register(clientGameEvents::endAuroraFrame);
		ClientEvents.RENDER_FRAME_PRE.register(clientGameEvents::killVignette);
		HudElementRegistry.replaceElement(VanillaHudElements.MOUNT_HEALTH, hudElement -> (graphics, deltaTracker) -> clientGameEvents.removeHostileMountHealth(hudElement, graphics, deltaTracker));
		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(clientGameEvents::renderGiantBlockOutlines);
		ClientEvents.COMPUTE_CAMERA_ANGLES.register(clientGameEvents::shakeCamera);
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> clientGameEvents.translateBookAuthor(stack, lines));
	}
}