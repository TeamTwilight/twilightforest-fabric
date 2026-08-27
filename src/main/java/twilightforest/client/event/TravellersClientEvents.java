package twilightforest.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.phys.Vec2;
import twilightforest.asm.mixin.KeyMappingAccessor;
import twilightforest.config.TFConfig;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFKeyBinds;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.CycleMapSlotPacket;
import twilightforest.network.GogglesZoomPacket;
import twilightforest.network.GradualGlidePacket;
import twilightforest.network.PerformDoubleJumpPacket;
import twilightforest.network.PerformSidestepPacket;
import twilightforest.network.SwapHotbarPacket;
import twilightforest.tags.TFItemTags;

public class TravellersClientEvents {

	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(TravellersClientEvents::clientTick);
	}

	private static void clientTick(Minecraft mc) {
		handleStealth();
		updateGradualGlideState();
	}

	/**
	 * Movement input handlers (formerly MovementInputUpdateEvent) are applied by
	 * LocalPlayerInputMixin to the ClientInput move vector.
	 */
	public static Vec2 applyTravellersInput(Vec2 moveVector) {
		Minecraft minecraft = Minecraft.getInstance();
		if (!(minecraft.player instanceof LocalPlayer localPlayer))
			return moveVector;

		float forward = moveVector.y;
		float left = moveVector.x;

		// handleAgileRanger
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);
		if (TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) && agileRangerModifier != null) {
			ItemStack useStack = localPlayer.getUseItem();
			boolean isLegalItem = (useStack.getItem() instanceof ProjectileWeaponItem || useStack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !useStack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
			if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
				left *= agileRangerModifier;
				forward *= agileRangerModifier;
			}
		}

		// handleStraightAhead
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance != null) {
			if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || forward <= 0)
				multiplier = 1D;
			attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			left /= multiplier;
		}

		// speedUpControlledWhileSneaking
		AttachmentTarget target = localPlayer;
		if (target.getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING) && localPlayer.isShiftKeyDown()) {
			forward /= 0.2F;
			left /= 0.2F;
		}

		// handleSidestep
		if (localPlayer.onGround()) {
			boolean lastImpulseZero = target.getAttached(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
			boolean sameImpulseDirection = Math.signum(target.getAttached(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(left);
			int currentTime = localPlayer.tickCount;
			int lastWalkingTime = target.getAttached(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
			boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

			if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && left != 0) {
				boolean isLeftSidestep = left > 0;
				if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
					ClientPlayNetworking.send(new PerformSidestepPacket(isLeftSidestep));
				}
			}

			target.setAttached(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, left);
			if (left != 0) {
				target.setAttached(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
				target.setAttached(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, left);
			}
		}

		return new Vec2(left, forward);
	}

	/**
	 * Key handlers (formerly InputEvent.Key) are invoked by KeyboardHandlerMixin
	 * after the vanilla key mappings have consumed the press.
	 */
	public static void handleKeyEvent(int action, int keyCode, int scanCode) {
		if (Minecraft.getInstance().screen != null)
			return;

		// handleDoubleJump
		if (Minecraft.getInstance().player instanceof LocalPlayer localPlayer && !ignoreKeyEvent(keyCode, scanCode, action, Minecraft.getInstance().options.keyJump)) {
			AttachmentTarget target = localPlayer;
			int lastJumpKeyPressTime = target.getAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
			boolean pressedKey = action == InputConstants.PRESS;
			if (pressedKey)
				target.setAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);
			boolean avoidCreativeFly = localPlayer.getAbilities().mayfly && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
			if (pressedKey && !avoidCreativeFly && TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
				if (TravellersGearLogic.performDoubleJump(localPlayer)) {
					ClientPlayNetworking.send(new PerformDoubleJumpPacket());
				}
			}
		}

		cycleItemDisplayMap();
		swapHotbar();
		toggleRedThreadVision();
	}

	private static boolean ignoreKeyEvent(int keyCode, int scanCode, int action, KeyMapping key) {
		return !keyMatches(key, keyCode, scanCode) || action != InputConstants.PRESS || Minecraft.getInstance().screen != null;
	}

	private static boolean keyMatches(KeyMapping key, int keyCode, int scanCode) {
		var boundKey = ((KeyMappingAccessor) key).twilightforest$getKey();
		return boundKey.getValue() == keyCode || boundKey.getValue() == scanCode;
	}

	private static void cycleItemDisplayMap() {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		ClientPlayNetworking.send(CycleMapSlotPacket.INSTANCE);
	}

	private static void swapHotbar() {
		if (!TFKeyBinds.SWAP_HOTBAR_KEY.consumeClick())
			return;
		Player player = Minecraft.getInstance().player;
		if (!(player instanceof LocalPlayer localPlayer)) return;
		ItemStack legArmor = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!TravellersArmorBeltItem.hasSwapHotbar(player, legArmor) || containerContents == null)
			return;
		ClientPlayNetworking.send(SwapHotbarPacket.INSTANCE);
	}

	private static void toggleRedThreadVision() {
		toggleBooleanDataAttachment(TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick(), TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private static void toggleBooleanDataAttachment(boolean pressed, ResourceKey<TravellersModifier> modifier, AttachmentType<Boolean> attachment) {
		if (!pressed)
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, modifier))
			return;

		boolean current = ((AttachmentTarget) player).getAttached(attachment);
		((AttachmentTarget) player).setAttached(attachment, !current);
	}

	/**
	 * FOV modifier for the goggles zoom (formerly ComputeFovModifierEvent), called
	 * from AbstractClientPlayerFovMixin.
	 */
	public static float updateZoomState(float fovModifier) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return fovModifier;
		boolean wasUsingZoom = ((AttachmentTarget) player).getAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom)
			fovModifier = fovModifier * zoomModifier;
		if (isUsingZoom == wasUsingZoom)
			return fovModifier;

		((AttachmentTarget) player).setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.value() : TFSounds.GOGGLES_ZOOM_OUT.value());
		ClientPlayNetworking.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
		return fovModifier;
	}

	/**
	 * Mouse sensitivity for the goggles zoom (formerly CalculatePlayerTurnEvent),
	 * called from MouseHandlerMixin.
	 */
	public static double slowZoomSensitivity(double mouseSensitivity) {
		Player player = Minecraft.getInstance().player;
		if (Minecraft.getInstance().options.smoothCamera || player == null)
			return mouseSensitivity;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null || !isZoomKeyHeld(player))
			return mouseSensitivity;

		double mod = 0.5D - 1 / (6 * mouseSensitivity);
		double fovMod = zoomModifier + 0.05F;
		return mod * mouseSensitivity / fovMod;
	}

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	private static void handleStealth() {
		if (Minecraft.getInstance().level == null)
			return;
		for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));
		}
	}

	private static void updateGradualGlideState() {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasGraduallyGliding = ((AttachmentTarget) player).getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld && player.getKnownMovement().y() < 0 && !player.onGround();
		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		((AttachmentTarget) player).setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		ClientPlayNetworking.send(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	// TODO [Fabric] renderGlovesInFirstPerson needs the 26.1 armour-model API
	// migration (IClientItemExtensions replacement) and will land with the
	// TravellersArmorItem batch.
}
