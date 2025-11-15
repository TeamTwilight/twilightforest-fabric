package twilightforest.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.network.GogglesZoomPacket;
import twilightforest.network.PerformDoubleJumpPacket;
import twilightforest.network.PerformSidestepPacket;
import twilightforest.network.SwapHotbarPacket;

@Component(dist = Dist.CLIENT)
public class TravellersClientEvents {

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	private void tickEntityGearEffects(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof LivingEntity livingEntity) || !livingEntity.level().isClientSide()) return;
		TravellersGearLogic.travellersWingsControlledFall(livingEntity);
		TravellersGearLogic.travellersBootsUnrestrained(livingEntity);
	}

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::tickEntityGearEffects);
		NeoForge.EVENT_BUS.addListener(this::handleDoubleJump);
		NeoForge.EVENT_BUS.addListener(this::handleAgileRanger);
		NeoForge.EVENT_BUS.addListener(this::handleForwardBoost);
		NeoForge.EVENT_BUS.addListener(this::handleSidestep);
		NeoForge.EVENT_BUS.addListener(this::handleStealth);
		NeoForge.EVENT_BUS.addListener(this::updateZoomState);
		NeoForge.EVENT_BUS.addListener(this::slowZoomSensitivity);
		NeoForge.EVENT_BUS.addListener(this::swapHotbar);
		NeoForge.EVENT_BUS.addListener(this::toggleItemDisplayVisibility);
		NeoForge.EVENT_BUS.addListener(this::toggleRedThreadVision);
	}

	private void handleAgileRanger(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			Input input = event.getInput();
			input.leftImpulse *= agileRangerModifier;
			input.forwardImpulse *= agileRangerModifier;
		}
	}

	private void handleForwardBoost(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.FORWARD_BOOST_MULTIPLIER);
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		Input input = localPlayer.input;
		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || input.forwardImpulse <= 0 || localPlayer.isInLiquid())
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		input.leftImpulse /= multiplier;
	}

	private void handleSidestep(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer) || !localPlayer.onGround())
			return;

		Input input = localPlayer.input;
		boolean lastImpulseZero = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(localPlayer.getData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(input.leftImpulse);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && input.leftImpulse != 0) {
			boolean isLeftSidestep = input.leftImpulse > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				localPlayer.connection.send(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, input.leftImpulse);
		if (input.leftImpulse != 0) {
			localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			localPlayer.setData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, input.leftImpulse);
		}
	}

	private void handleStealth(RenderFrameEvent.Pre event) {
		if (Minecraft.getInstance().level == null)
			return;
		for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));  // call it on client to make player invisible instantly
		}
	}

	private void handleDoubleJump(PlayerTickEvent.Pre event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		int lastJumpKeyPressTime = localPlayer.getData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		boolean holdsJumpKey = localPlayer.tickCount - lastJumpKeyPressTime <= 1;
		boolean pressedKey = Minecraft.getInstance().options.keyJump.isDown();
		if (pressedKey)
			localPlayer.setData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);

		if (pressedKey && !holdsJumpKey && TravellersModifiersManager.isModifierActive(localPlayer, localPlayer.getItemBySlot(EquipmentSlot.LEGS), TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				localPlayer.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = true;
				localPlayer.connection.send(new PerformDoubleJumpPacket());
			}
		}
	}

	private void updateZoomState(ComputeFovModifierEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasUsingZoom = player.getData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom)
			event.setNewFovModifier(event.getNewFovModifier() * zoomModifier);
		if (isUsingZoom == wasUsingZoom)
			return;

		player.setData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.get() : TFSounds.GOGGLES_ZOOM_OUT.get());
		player.connection.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
	}

	private void swapHotbar(InputEvent.Key event) {
		if (TFKeyBinds.SWAP_HOTBAR_KEY.matches(event.getKey(), event.getScanCode())) {
			Player player = Minecraft.getInstance().player;
			if (!(player instanceof LocalPlayer localPlayer)) return;
			ItemStack legArmor = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
			ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
			if (!TravellersArmorBeltItem.hasSwapHotbar(player, legArmor) || containerContents == null)
				return;

			boolean isClicked = false;
			while (TFKeyBinds.SWAP_HOTBAR_KEY.consumeClick()) {
				isClicked = !isClicked;  // clickCount can be even, so we may not swap hotbar
			}
			boolean hasClicked = isClicked;
			if (!hasClicked)
				return;
			localPlayer.connection.send(SwapHotbarPacket.INSTANCE);
		}
	}

	private void toggleRedThreadVision(InputEvent.Key event) {
		this.toggleBooleanDataAttachment(event, TFKeyBinds.RED_THREAD_VISION_KEY, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private void toggleItemDisplayVisibility(InputEvent.Key event) {
		this.toggleBooleanDataAttachment(event, TFKeyBinds.ITEM_DISPLAY_KEY, TFDataAttachments.TRAVELLERS_GOGGLES_ITEM_DISPLAY);
	}

	private void toggleBooleanDataAttachment(InputEvent.Key event, KeyMapping key, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
		if (!key.matches(event.getKey(), event.getScanCode()))
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		boolean current = player.getData(attachment.get());
		boolean isClicked = false;
		while (key.consumeClick()) {
			isClicked = !isClicked;  // clickCount can be even, so we may not toggle
		}

		if (isClicked)
			player.setData(attachment.get(), !current);
	}

	private void slowZoomSensitivity(CalculatePlayerTurnEvent event) {
		Player player = Minecraft.getInstance().player; // Player is never null but we need to check for null to avoid warnings
		if (event.getCinematicCameraEnabled() || player == null)
			return;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null || !isZoomKeyHeld(player))
			return;

		double mouseSensitivity = event.getMouseSensitivity();
		// vanilla math for turning is (m * 0.6 + 0.2)³ * 8; where m is the mouse sensitivity
		// vanilla spyglasses avoid using the "* 8" part, so we probably want to as well
		// the mod value to reverse that was borrowed from IE since they also have zoom functionality
		// we can then divide by our zoom modifier (and add 0.05 to slow it down slightly) to set the sensitivity to a reasonable value when zooming
		double mod = 0.5D - 1 / (6 * mouseSensitivity);
		double fovMod = zoomModifier + 0.05F;
		event.setMouseSensitivity(mod * mouseSensitivity / fovMod);
	}
}
