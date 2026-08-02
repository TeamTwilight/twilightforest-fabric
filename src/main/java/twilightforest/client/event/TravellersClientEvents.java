package twilightforest.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.InputEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.RenderArmEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.MovementInputUpdateCallback;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.network.*;

@Environment(EnvType.CLIENT)
public class TravellersClientEvents {

	public static final TravellersClientEvents INSTANCE = new TravellersClientEvents();

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	public static void init() {
		// Available in Porting-Lib:
		InputEvent.Key.EVENT.register(INSTANCE::handleDoubleJump);
		InputEvent.Key.EVENT.register(INSTANCE::cycleItemDisplayMap);
		InputEvent.Key.EVENT.register(INSTANCE::swapHotbar);
		InputEvent.Key.EVENT.register(INSTANCE::toggleRedThreadVision);
		ComputeFovModifierEvent.EVENT.register(INSTANCE::updateZoomState);
		RenderArmEvent.EVENT.register(INSTANCE::renderGlovesInFirstPerson);

		// MovementInputUpdateCallback - different API, needs adaptation
		MovementInputUpdateCallback.EVENT.register(INSTANCE::handleMovementInput);

		// Fabric API: Client tick events for per-frame updates
		ClientTickEvents.END_CLIENT_TICK.register(INSTANCE::handleStealth);
		ClientTickEvents.END_CLIENT_TICK.register(INSTANCE::updateGradualGlideState);

		// slowZoomSensitivity needs CalculatePlayerTurnEvent - not available in Porting-Lib
	}

	// Adapter for MovementInputUpdateCallback that combines handleAgileRanger, handleStraightAhead, speedUpControlledWhileSneaking, handleSidestep
	private void handleMovementInput(Player player, Input input) {
		if (!(player instanceof LocalPlayer localPlayer)) return;
		handleAgileRanger(localPlayer, input);
		handleStraightAhead(localPlayer, input);
		speedUpControlledWhileSneaking(localPlayer, input);
		handleSidestep(localPlayer, input);
	}

	private void handleAgileRanger(LocalPlayer localPlayer, Input input) {
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER.get());
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			input.leftImpulse *= agileRangerModifier;
			input.forwardImpulse *= agileRangerModifier;
		}
	}

	private void handleStraightAhead(LocalPlayer localPlayer, Input input) {
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER.get());
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		Input localInput = localPlayer.input;
		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || localInput.forwardImpulse <= 0)
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		input.leftImpulse /= multiplier;
	}

	private void speedUpControlledWhileSneaking(LocalPlayer localPlayer, Input input) {
		if (!localPlayer.getAttachedOrCreate(TFDataAttachments.IS_GRADUALLY_GLIDING) || !localPlayer.isShiftKeyDown())
			return;
		localPlayer.input.forwardImpulse /= 0.2F;
		localPlayer.input.leftImpulse /= 0.2F;
	}

	private void handleSidestep(LocalPlayer localPlayer, Input input) {
		if (!localPlayer.onGround())
			return;

		Input localInput = localPlayer.input;
		boolean lastImpulseZero = localPlayer.getAttachedOrCreate(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(localPlayer.getAttachedOrCreate(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(localInput.leftImpulse);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = localPlayer.getAttachedOrCreate(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && localInput.leftImpulse != 0) {
			boolean isLeftSidestep = localInput.leftImpulse > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				ClientPlayNetworking.send(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		localPlayer.setAttached(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, localInput.leftImpulse);
		if (localInput.leftImpulse != 0) {
			localPlayer.setAttached(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			localPlayer.setAttached(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, localInput.leftImpulse);
		}
	}

	private void handleStealth(Minecraft client) {
		if (client.level == null)
			return;
		for (Entity entity : client.level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));  // call it on client to make player invisible instantly
		}
	}

	private void handleDoubleJump(InputEvent.Key event) {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || ignoreKeyEvent(event, Minecraft.getInstance().options.keyJump))
			return;
		int lastJumpKeyPressTime = localPlayer.getAttachedOrCreate(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		boolean pressedKey = event.getAction() == InputConstants.PRESS;
		if (pressedKey)
			localPlayer.setAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);
		boolean avoidCreativeFly = localPlayer.getAbilities().mayfly && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
		if (pressedKey && !avoidCreativeFly && TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				ClientPlayNetworking.send(new PerformDoubleJumpPacket());
			}
		}
	}

	private void updateZoomState(ComputeFovModifierEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasUsingZoom = player.getAttachedOrCreate(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER.get());
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom)
			event.setNewFovModifier(event.getNewFovModifier() * zoomModifier);
		if (isUsingZoom == wasUsingZoom)
			return;

		player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.get() : TFSounds.GOGGLES_ZOOM_OUT.get());
		ClientPlayNetworking.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
	}

	private void updateGradualGlideState(Minecraft client) {
		LocalPlayer player = client.player;
		if (player == null) return;
		boolean wasGraduallyGliding = player.getAttachedOrCreate(TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld && player.getKnownMovement().y() < 0 && !player.onGround();
		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		ClientPlayNetworking.send(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	private void cycleItemDisplayMap(InputEvent.Key event) {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		ClientPlayNetworking.send(CycleMapSlotPacket.INSTANCE);
	}

	private void swapHotbar(InputEvent.Key event) {
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

	private void toggleRedThreadVision(InputEvent.Key event) {
		if (!TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick()) return;
		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER)) return;
		boolean current = player.getAttachedOrCreate(TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
		player.setAttached(TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION, !current);
	}

	private boolean ignoreKeyEvent(InputEvent.Key event, KeyMapping key) {
		return !key.matches(event.getKey(), event.getScanCode()) || event.getAction() != InputConstants.PRESS || Minecraft.getInstance().screen != null;
	}

	@SuppressWarnings("unchecked") //meh
	private void renderGlovesInFirstPerson(RenderArmEvent event) {
		if (TFConfig.firstPersonGloveOverlay) {
			AbstractClientPlayer player = event.getPlayer();
			ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
			if (chestStack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES.get()) && !chestStack.has(TFDataComponents.EMPERORS_CLOTH.get())) {
				PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
				// IClientItemExtensions.of() API changed in 1.21.1 - using renderer.getModel() instead
				HumanoidModel<AbstractClientPlayer> model = renderer.getModel();
				ModelPart armPart = event.getArm() == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
				model.attackTime = 0.0F;
				model.crouching = false;
				model.swimAmount = 0.0F;
				model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				armPart.xRot = 0.0F;
				ResourceLocation gloveLocation = TwilightForestMod.prefix("textures/models/armor/travellers_layer_1.png");
				armPart.render(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.armorCutoutNoCull(gloveLocation)), event.getPackedLight(), OverlayTexture.NO_OVERLAY);
			}
		}
	}
}
