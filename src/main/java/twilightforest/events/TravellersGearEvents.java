package twilightforest.events;

import carminite.events.api.*;
import carminite.events.api.EntityEvents;
import carminite.events.neoforge.*;
import carminite.network.PacketDistributor;
import carminite.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.inventory.InventoryUtil;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.GradualGlidePacket;
import twilightforest.network.ParticlePacket;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TravellersGearEvents {
	public static final TravellersGearEvents INSTANCE = new TravellersGearEvents();

	private static final List<AttachmentType<?>> ATTACHMENTS_TO_PRESERVE_ON_DEATH = List.of(
		TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION
	);

	public static void init() {
		EntityEvents.PROJECTILE_IMPACT.register(INSTANCE::magnetizeArrows);
		EntityEvents.PROJECTILE_IMPACT.register(INSTANCE::performPerfectDodge);
		LivingEvents.LIVING_JUMP.register(INSTANCE::cancelSlimySolesJump);
		TickEvents.PLAYER_TICK_PRE.register(INSTANCE::tickMovementModifiers);
		TickEvents.PLAYER_TICK_POST.register(INSTANCE::performStealth);
		TickEvents.PLAYER_TICK_PRE.register(INSTANCE::disableHighStepWhileSneaking);
		TickEvents.ENTITY_TICK_POST.register(INSTANCE::updateOtherModifiers);
		LivingEvents.ARMOR_HURT.register(INSTANCE::stopDamagingTravellersGear);
		LivingEvents.ARMOR_HURT.register(INSTANCE::setLastDamageArmorTime);
		WorkstationEvents.ANVIL_UPDATE.register(INSTANCE::cancelCombiningTravellersGear);
		WorkstationEvents.GRINDSTONE_PLACE.register(INSTANCE::removeModifiersFromTravellersGear);
		WorkstationEvents.GRINDSTONE_TAKE.register(INSTANCE::extractItemsFromSwapHotbarModifier);
		PlayerEvents.ITEM_CRAFTED.register(INSTANCE::fireCraftingModifierTrigger);
		ServerPlayerEvents.COPY_FROM.register(INSTANCE::keepAttachmentsOnDeath);
	}

	private void magnetizeArrows(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();
		Entity entity = projectile.getOwner();
		if (!(entity instanceof LivingEntity livingEntity) || !event.getRayTraceResult().getType().equals(HitResult.Type.BLOCK) || projectile.tickCount >= 200)
			return;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER)
			|| !(projectile instanceof AbstractArrow arrow) || projectile.level().isClientSide())
			return;

		if (!(livingEntity instanceof Player player)) {
			projectile.discard();
			return;
		}
		AbstractArrow.Pickup pickup = arrow.pickup;
		if (!player.hasInfiniteMaterials() && pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
			InventoryUtil.giveItemToPlayer(player, arrow.getPickupItemStackOrigin());
			player.getInventory().setChanged();
		}
		if (pickup.equals(AbstractArrow.Pickup.ALLOWED) || pickup.equals(AbstractArrow.Pickup.CREATIVE_ONLY) && player.isCreative())
			projectile.discard();
	}

	private void performPerfectDodge(ProjectileImpactEvent event) {
		HitResult rayResult = event.getRayTraceResult();
		if (!(rayResult instanceof EntityHitResult entityHitResult) || !(entityHitResult.getEntity() instanceof LivingEntity livingEntity))
			return;
		ItemStack chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Float probability = chest.get(TFDataComponents.PERFECT_DODGE_PROBABILITY);
		Level level = livingEntity.level();
		if (!TravellersModifiersManager.isModifierActive(livingEntity, chest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER) || probability == null)
			return;
		if (level.isClientSide()) {
			event.setCanceled(true); // always cancel on the client side because the game sends a damage packet when it hits the player
			return;
		}
		if (probability <= level.getRandom().nextFloat())
			return;
		Entity projectile = event.getEntity();
		Vec3 hitPosition = projectile.position().add(projectile.getDeltaMovement());
		level.playSound(null, hitPosition.x(), hitPosition.y(), hitPosition.z(), TFSounds.PERFECT_DODGE.value(), livingEntity.getSoundSource(), 1.5F, livingEntity.getVoicePitch());
		event.setCanceled(true);
		ParticlePacket particlePacket = new ParticlePacket();
		for (int particleNumber = 0; particleNumber < 20; particleNumber++) {
			Vec3 particleVelocity = new Vec3(
				(level.getRandom().nextDouble() - 0.5),
				(level.getRandom().nextDouble() - 0.5),
				(level.getRandom().nextDouble() - 0.5)
			);
			ParticleOptions type = TFParticleType.PERFECT_DODGE;
			particlePacket.queueParticle(type, false, false, hitPosition, particleVelocity);
		}
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, particlePacket);
	}

	private void cancelSlimySolesJump(LivingEvent.LivingJumpEvent event) {
		LivingEntity livingEntity = event.getEntity();
		SlimySolesAttachment slimySolesAttachment = livingEntity.getAttached(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		slimySolesAttachment.bounceVelocity = 0;
		slimySolesAttachment.forceBounce = false;
		livingEntity.setAttached(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
	}

	private void tickMovementModifiers(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Boolean hasDoubleJump = null;
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER))
			hasDoubleJump = false;
		else if (player.onGround() || player.isInLiquid() || player.onClimbable())
			hasDoubleJump = true;

		if (hasDoubleJump != null && hasDoubleJump != player.getAttached(TFDataAttachments.HAS_DOUBLE_JUMP)) {
			player.setAttached(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			player.setAttached(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
			if (instance != null)
				instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
		}

		if (!player.level().isClientSide()) {
			boolean modifierActive = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
			if (!modifierActive && player.getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING)) {
				player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, false);
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new GradualGlidePacket(false, player.getUUID()));
			}
		}

		//reset double jump wing anim if on the ground
		if (event.getEntity().level().isClientSide()) {
			if (player.getAttached(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump && player.onGround()) {
				player.getAttached(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = false;
			}
		}

		TravellersGearLogic.travellersWingsSidestepCooldownSound(player);
	}

	private void performStealth(PlayerTickEvent.Post event) {
		if (!event.getEntity().level().isClientSide()) {
			TravellersGearLogic.travellersStealth(event.getEntity(), player1 -> player1.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false)));
		}
	}

	private void disableHighStepWhileSneaking(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEP_UP_ABILITY))
			return;
		AttributeInstance attribute = player.getAttributes().getInstance(Attributes.STEP_HEIGHT);
		if (attribute == null)
			return;

		boolean shouldHaveHighStepModifier = !player.isCrouching();
		boolean hasHighStepModifier = attribute.hasModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP.id());
		if (!shouldHaveHighStepModifier && hasHighStepModifier)
			attribute.removeModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
		if (shouldHaveHighStepModifier && !hasHighStepModifier)
			attribute.addPermanentModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
	}

	private void updateOtherModifiers(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
		TravellersGearLogic.travellersWingsGradualGlide(livingEntity);
		TravellersGearLogic.travellersBootsUnrestrained(livingEntity);
		TravellersGearLogic.travellersBootsSlimySolesBounce(livingEntity);

		if (livingEntity.level().isClientSide()) return;
		TravellersGearLogic.travellersVestHaste(livingEntity);
		TravellersGearLogic.travellersWingsHighJump(livingEntity);
		TravellersGearLogic.travellersGearAutoRepair(livingEntity);
		TravellersGearLogic.travellersBootsStraightAhead(livingEntity);
		TravellersGearLogic.determineWingState(livingEntity);
	}

	private void stopDamagingTravellersGear(ArmorHurtEvent event) {
		if (event.isCanceled())
			return;
		event.getArmorMap().forEach((slot, entry) -> {
			ItemStack damagedStack = event.getArmorItemStack(slot);
			if (!damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
				return;
			if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage()) {
				event.setNewDamage(slot, damagedStack.getMaxDamage() - damagedStack.getDamageValue() - 1);
			} else if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage() - 1 && event.getEntity() instanceof ServerPlayer player) {
				player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0F, player.getVoicePitch(), false);
			}
		});
	}

	private void setLastDamageArmorTime(ArmorHurtEvent event) {
		if (Arrays.stream(EquipmentSlot.values()).noneMatch(slot -> event.getNewDamage(slot) > 0)) return;
		LivingEntity entity = event.getEntity();
		entity.setAttached(TFDataAttachments.LAST_DAMAGE_ARMOR_TIME, entity.level().getGameTime());
	}


	private void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			event.setCanceled(true);
		}
	}

	private void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;
		RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			event.setCanceled(true);
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
		if (modifiers.isEmpty()) {
			event.setCanceled(true);
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));
		event.setOutput(unmodifiedStack.copy());
	}

	private void extractItemsFromSwapHotbarModifier(GrindstoneEvent.OnTakeItem event) {
		returnModifierItems(event,
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER,
			DataComponents.CONTAINER,
			ItemContainerContents::nonEmptyItemCopyStream
		);

		returnModifierItems(event,
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER,
			TFDataComponents.ITEM_DISPLAY,
			contents -> contents.items().stream()
		);
	}

	private <T> void returnModifierItems(GrindstoneEvent.OnTakeItem event, ResourceKey<TravellersModifier> modifierKey, DataComponentType<T> componentType, Function<T, Stream<ItemStack>> itemStreamExtractor) {
		getUniqueTravellersGear(event.getTopItem(), event.getBottomItem(), stack ->
			TravellersModifiersManager.hasTravellersModifier(event.getPlayer().registryAccess(), stack, modifierKey)
		).map(stack -> stack.get(componentType))
			.ifPresent(component ->
				itemStreamExtractor.apply(component)
					.forEach(itemStack -> InventoryUtil.giveItemToPlayer(event.getPlayer(), itemStack))
			);
	}

	private Optional<ItemStack> getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}

	private void fireCraftingModifierTrigger(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player, compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player, event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
					.forEach(modifier -> TFAdvancements.ADD_MODIFIER.trigger(player, modifier.key().identifier()));
			}
		}
	}

	public void keepAttachmentsOnDeath(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		if (!alive) {
			for (AttachmentType<?> attachment : ATTACHMENTS_TO_PRESERVE_ON_DEATH) {
				copyAttachmentData(oldPlayer, newPlayer, attachment);
			}
		}
	}

	private <T> void copyAttachmentData(Player source, Player target, AttachmentType<T> type) {
		if (source.hasAttached(type)) {
			target.setAttached(type, source.getAttached(type));
		}
	}
}