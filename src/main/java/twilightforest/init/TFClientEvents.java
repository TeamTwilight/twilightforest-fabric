package twilightforest.init;

import carminite.events.api.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import twilightforest.client.event.*;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.client.overlay.PortalOverlay;

public final class TFClientEvents {
	public static void init() {
		setupFogEvents();
		setupLockedBiomeToastEvents();
		setupCloudEvents();
		setupOverlayEvents();
		setupClientGameEvents();
		setupTravellersClientEvents();
	}

	private static void setupFogEvents() {
		ClientEvents.CARMINITE_COMPUTE_FOG_COLOR.register(FogHandler::colorFog);
	}

	private static void setupLockedBiomeToastEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(_ -> LockedBiomeToastHandler.tickLockedToastLogic());
	}

	private static void setupCloudEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(CloudEvents::tickWeatherEffects);
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(CloudEvents::renderPrecipitation);
	}

	private static void setupOverlayEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, OverlayHandler.QUEST_RAM_INDICATOR, (graphics, _) -> OverlayHandler.renderIndicator(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudElementRegistry.attachElementAfter(VanillaHudElements.MOUNT_HEALTH, OverlayHandler.HOSTILE_MOUNT_HUNGER_BAR, (graphics, _) -> OverlayHandler.renderHostileMountHungerBar(graphics));
		HudStatusBarHeightRegistry.addRight(OverlayHandler.HOSTILE_MOUNT_HUNGER_BAR, _ -> 10);
		HudElementRegistry.addLast(OverlayHandler.ORE_METER_STATS, (graphics, _) -> OverlayHandler.renderOreMeterStats(graphics));
		HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, OverlayHandler.FORTIFICATION_SHIELD_COUNT, (graphics, _) -> OverlayHandler.renderShieldCount(graphics, graphics.guiWidth(), graphics.guiHeight()));
		HudStatusBarHeightRegistry.addLeft(OverlayHandler.FORTIFICATION_SHIELD_COUNT, _ -> 10);
		HudElementRegistry.addLast(OverlayHandler.PORTAL_OVERLAY, (graphics, _) -> PortalOverlay.render(graphics));
		HudElementRegistry.addLast(OverlayHandler.ITEM_DISPLAY_OVERLAY, (graphics, _) -> ItemDisplayOverlay.render(graphics, OverlayHandler.getCameraPlayer()));
	}

	private static void setupClientGameEvents() {
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> ClientGameEvents.addCustomTooltips(stack, lines));
		ClientTickEvents.END_CLIENT_TICK.register(ClientGameEvents::clientTick);
		ScreenEvents.AFTER_INIT.register((_, screen, _, _) -> {
			ClientGameEvents.customizeSplashes(screen);
			ScreenEvents.remove(screen).register(_ -> ClientGameEvents.clearEntityRenderUtilMap());
		});
		ClientEvents.RENDER_FRAME_POST.register(ClientGameEvents::endAuroraFrame);
		ClientEvents.RENDER_FRAME_PRE.register(ClientGameEvents::killVignette);
		HudElementRegistry.replaceElement(VanillaHudElements.MOUNT_HEALTH, hudElement -> (graphics, deltaTracker) -> ClientGameEvents.removeHostileMountHealth(hudElement, graphics, deltaTracker));
		ClientEvents.CARMINITE_RENDER_LEVEL_AFTER_WEATHER.register(ClientGameEvents::renderAurora);
		ClientEvents.CUSTOMIZE_BOSS_HEALTH_OVERLAY.register(ClientGameEvents::renderCustomBossbars);
		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientGameEvents::renderGiantBlockOutlines);
		ClientEvents.COMPUTE_CAMERA_ANGLES.register(ClientGameEvents::shakeCamera);
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> ClientGameEvents.translateBookAuthor(stack, lines));
		ClientEvents.COMPUTE_FOV_MODIFIER.register(ClientGameEvents::updateBowFOV);
	}

	private static void setupTravellersClientEvents() {
		ClientEvents.INPUT_KEY.register(TravellersClientEvents::handleDoubleJump);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEvents::handleAgileRanger);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEvents::handleStraightAhead);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEvents::speedUpControlledWhileSneaking);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(TravellersClientEvents::handleSidestep);
		ClientEvents.RENDER_FRAME_PRE.register(TravellersClientEvents::handleStealth);
		ClientEvents.COMPUTE_FOV_MODIFIER.register(TravellersClientEvents::updateZoomState);
		ClientEvents.RENDER_FRAME_PRE.register(TravellersClientEvents::updateGradualGlideState);
		ClientEvents.INPUT_KEY.register(TravellersClientEvents::cycleItemDisplayMap);
		ClientEvents.CALCULATE_PLAYER_TURN.register(TravellersClientEvents::slowZoomSensitivity);
		ClientEvents.INPUT_KEY.register(TravellersClientEvents::swapHotbar);
		ClientEvents.INPUT_KEY.register(TravellersClientEvents::toggleRedThreadVision);
	}
}