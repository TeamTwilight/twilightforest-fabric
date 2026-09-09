package twilightforest;

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
	private static final FogHandler fogEvents = FogHandler.INSTANCE;
	private static final LockedBiomeToastHandler lockedBiomeToastEvents = LockedBiomeToastHandler.INSTANCE;
	private static final CloudEvents cloudEvents = CloudEvents.INSTANCE;
	private static final OverlayHandler overlayEvents = OverlayHandler.INSTANCE;
	private static final ClientGameEvents clientGameEvents = ClientGameEvents.INSTANCE;
	private static final TravellersClientEvents travellersClientEvents = TravellersClientEvents.INSTANCE;

	public static void init() {
		setupFogEvents();
		setupLockedBiomeToastEvents();
		setupCloudEvents();
		setupOverlayEvents();
		setupClientGameEvents();
		setupTravellersClientEvents();
	}

	private static void setupFogEvents() {
		ClientEvents.CARMINITE_COMPUTE_FOG_COLOR.register(fogEvents::colorFog);
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
		ClientEvents.CUSTOMIZE_BOSS_HEALTH_OVERLAY.register(clientGameEvents::renderCustomBossbars);
		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(clientGameEvents::renderGiantBlockOutlines);
		ClientEvents.COMPUTE_CAMERA_ANGLES.register(clientGameEvents::shakeCamera);
		ItemTooltipCallback.EVENT.register((stack, _, _, lines) -> clientGameEvents.translateBookAuthor(stack, lines));
		ClientEvents.COMPUTE_FOV_MODIFIER.register(clientGameEvents::updateBowFOV);
	}

	private static void setupTravellersClientEvents() {
		ClientEvents.INPUT_KEY.register(travellersClientEvents::handleDoubleJump);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(travellersClientEvents::handleAgileRanger);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(travellersClientEvents::handleStraightAhead);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(travellersClientEvents::speedUpControlledWhileSneaking);
		ClientEvents.MOVEMENT_INPUT_UPDATE.register(travellersClientEvents::handleSidestep);
		ClientEvents.RENDER_FRAME_PRE.register(travellersClientEvents::handleStealth);
		ClientEvents.COMPUTE_FOV_MODIFIER.register(travellersClientEvents::updateZoomState);
		ClientEvents.RENDER_FRAME_PRE.register(travellersClientEvents::updateGradualGlideState);
		ClientEvents.INPUT_KEY.register(travellersClientEvents::cycleItemDisplayMap);
		ClientEvents.CALCULATE_PLAYER_TURN.register(travellersClientEvents::slowZoomSensitivity);
		ClientEvents.INPUT_KEY.register(travellersClientEvents::swapHotbar);
		ClientEvents.INPUT_KEY.register(travellersClientEvents::toggleRedThreadVision);
	}
}