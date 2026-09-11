package twilightforest.init;

import carminite.events.api.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import twilightforest.client.listeners.*;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.client.overlay.PortalOverlay;

public final class TFClientEvents {
	public static void init() {
		setupFogEvents();
		setupLockedBiomeToastEvents();
		setupCloudEvents();
		setupOverlayEvents();
		setupTravellersClientEvents();
		setupClientGameEvents();
	}

	private static void setupFogEvents() {
		ClientEvents.CARMINITE_COMPUTE_FOG_COLOR.register(FogEventListeners::colorFog);
	}

	private static void setupLockedBiomeToastEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(_ -> LockedBiomeToastEventListeners.tickLockedToastLogic());
	}

	private static void setupCloudEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(CloudEventListeners::tickWeatherEffects);
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(CloudEventListeners::renderPrecipitation);
	}

	private static void setupOverlayEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, OverlayEventListeners.QUEST_RAM_INDICATOR, (graphics, _) -> OverlayEventListeners.renderIndicator(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudElementRegistry.attachElementAfter(VanillaHudElements.MOUNT_HEALTH, OverlayEventListeners.HOSTILE_MOUNT_HUNGER_BAR, (graphics, _) -> OverlayEventListeners.renderHostileMountHungerBar(graphics));
		HudStatusBarHeightRegistry.addRight(OverlayEventListeners.HOSTILE_MOUNT_HUNGER_BAR, _ -> 10);
		HudElementRegistry.addLast(OverlayEventListeners.ORE_METER_STATS, (graphics, _) -> OverlayEventListeners.renderOreMeterStats(graphics));
		HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, OverlayEventListeners.FORTIFICATION_SHIELD_COUNT, (graphics, _) -> OverlayEventListeners.renderShieldCount(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudStatusBarHeightRegistry.addLeft(OverlayEventListeners.FORTIFICATION_SHIELD_COUNT, _ -> 10);
		HudElementRegistry.addLast(OverlayEventListeners.PORTAL_OVERLAY, (graphics, _) -> PortalOverlay.render(graphics));
		HudElementRegistry.addLast(OverlayEventListeners.ITEM_DISPLAY_OVERLAY, (graphics, _) -> ItemDisplayOverlay.render(graphics, OverlayEventListeners.getCameraPlayer()));
	}

	private static void setupTravellersClientEvents() {
		ClientEvents.INPUT_KEY.register(TravellersClientEventListeners::handleDoubleJump);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEventListeners::handleAgileRanger);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEventListeners::handleStraightAhead);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEventListeners::speedUpControlledWhileSneaking);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEventListeners::handleSidestep);
		ClientEvents.RENDER_FRAME_PRE.register(TravellersClientEventListeners::handleStealth);
		ClientEvents.COMPUTE_FOV_MODIFIER.register(TravellersClientEventListeners::updateZoomState);
		ClientEvents.RENDER_FRAME_PRE.register(TravellersClientEventListeners::updateGradualGlideState);
		ClientEvents.INPUT_KEY.register(TravellersClientEventListeners::cycleItemDisplayMap);
		ClientEvents.CALCULATE_PLAYER_TURN.register(TravellersClientEventListeners::slowZoomSensitivity);
		ClientEvents.INPUT_KEY.register(TravellersClientEventListeners::swapHotbar);
		ClientEvents.INPUT_KEY.register(TravellersClientEventListeners::toggleRedThreadVision);
	}

	private static void setupClientGameEvents() {
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> ClientGameEventListeners.addCustomTooltips(stack, lines));
		ClientTickEvents.END_CLIENT_TICK.register(ClientGameEventListeners::clientTick);
		ScreenEvents.AFTER_INIT.register((_, screen, _, _) -> {
			ClientGameEventListeners.customizeSplashes(screen);
			ScreenEvents.remove(screen).register(_ -> ClientGameEventListeners.clearEntityRenderUtilMap());
		});
		ClientEvents.RENDER_FRAME_POST.register(ClientGameEventListeners::endAuroraFrame);
		ClientEvents.RENDER_FRAME_PRE.register(ClientGameEventListeners::killVignette);
		HudElementRegistry.replaceElement(VanillaHudElements.MOUNT_HEALTH, hudElement -> (graphics, deltaTracker) -> ClientGameEventListeners.removeHostileMountHealth(hudElement, graphics, deltaTracker));
		ClientEvents.CARMINITE_RENDER_LEVEL_AFTER_WEATHER.register(ClientGameEventListeners::renderAurora);
		ClientEvents.CUSTOMIZE_BOSS_HEALTH_OVERLAY.register(ClientGameEventListeners::renderCustomBossbars);
		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientGameEventListeners::renderGiantBlockOutlines);
		ClientEvents.SELECT_MUSIC.register(ClientGameEventListeners::setMusicInDimension);
		ClientEvents.COMPUTE_CAMERA_ANGLES.register(ClientGameEventListeners::shakeCamera);
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> ClientGameEventListeners.translateBookAuthor(stack, lines));
		ClientEvents.COMPUTE_FOV_MODIFIER.register(ClientGameEventListeners::updateBowFOV);
	}
}