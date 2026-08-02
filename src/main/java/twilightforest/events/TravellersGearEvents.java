package twilightforest.events;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;




import io.github.fabricators_of_create.porting_lib.entity.events.ProjectileImpactEvent;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingFallEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;

import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import io.github.fabricators_of_create.porting_lib.event.common.ItemAttributeModifierEvent;
import io.github.fabricators_of_create.porting_lib.event.common.GrindstoneEvent;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import twilightforest.network.PacketDistributor;
import io.github.fabricators_of_create.porting_lib.core.util.ServerLifecycleHooks;
import twilightforest.util.TFBeanRegistry;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
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
	static {
		TFBeanRegistry.register(TravellersGearEvents.class, INSTANCE);
		TFBeanRegistry.addPostInit(INSTANCE::init);
	}

	private static final List<AttachmentType<?>> ATTACHMENTS_TO_PRESERVE_ON_DEATH = List.of(
		TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION
	);

	private void init() {
		ProjectileImpactEvent.EVENT.register(this::magnetizeArrows);
		ProjectileImpactEvent.EVENT.register(this::performPerfectDodge);
		LivingFallEvent.EVENT.register(this::reduceSlimySolesFallDamage);
		PlayerTickEvent.Pre.EVENT.register(this::tickMovementModifiers);
		PlayerTickEvent.Post.EVENT.register(this::performStealth);
		PlayerTickEvent.Pre.EVENT.register(this::disableHighStepWhileSneaking);
		EntityTickEvent.Post.EVENT.register(this::updateOtherModifiers);
		LivingEvents.LivingJumpEvent.EVENT.register(this::cancelSlimySolesJump);
		ItemAttributeModifierEvent.EVENT.register(this::activateAndDeactivateTravellersModifiers);
		// AnvilUpdateEvent needs migration to Fabric API
		// NeoForge.EVENT_BUS.addListener(this::cancelCombiningTravellersGear);
		// PlayerSpawnPhantomsEvent needs migration to Fabric API
		// NeoForge.EVENT_BUS.addListener(this::cancelPhantomSpawns);
		PlayerEvents.ItemCraftedEvent.EVENT.register(this::fireCraftingModifierTrigger);
		// GrindstoneEvent.OnTakeItem lacks getPlayer(), needs custom mixin
		// GrindstoneEvent.OnTakeItem.EVENT.register(this::extractItemsFromSwapHotbarModifier);
		GrindstoneEvent.OnPlaceItem.EVENT.register(this::removeModifiersFromTravellersGear);
		// ArmorHurtEvent needs migration to Fabric API LivingArmorDamageCallback
		// NeoForge.EVENT_BUS.addListener(this::stopDamagingTravellersGear);
		// NeoForge.EVENT_BUS.addListener(this::setLastDamageArmorTime);
		PlayerEvents.Clone.EVENT.register(this::keepAttachmentsOnDeath);
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
			ItemHandlerHelper.giveItemToPlayer(player, arrow.getPickupItemStackOrigin());
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
		Float probability = chest.get(TFDataComponents.PERFECT_DODGE_PROBABILITY.get());
		Level level = livingEntity.level();
		if (!TravellersModifiersManager.isModifierActive(livingEntity, chest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER) || probability == null)
			return;
		if (level.isClientSide()) {
			event.setCanceled(true); // always cancel on the client side because the game sends a damage packet when it hits the player
			return;
		}
		if (probability <= level.random.nextFloat())
			return;
		Entity projectile = event.getEntity();
		Vec3 hitPosition = projectile.position().add(projectile.getDeltaMovement());
		level.playSound(null, hitPosition.x(), hitPosition.y(), hitPosition.z(), TFSounds.PERFECT_DODGE.get(), livingEntity.getSoundSource(), 1.5F, livingEntity.getVoicePitch());
		event.setCanceled(true);
		ParticlePacket particlePacket = new ParticlePacket();
		for (int particleNumber = 0; particleNumber < 20; particleNumber++) {
			Vec3 particleVelocity = new Vec3(
				(level.random.nextDouble() - 0.5),
				(level.random.nextDouble() - 0.5),
				(level.random.nextDouble() - 0.5)
			);
			ParticleOptions type = TFParticleType.PERFECT_DODGE.get();
			particlePacket.queueParticle(type, false, hitPosition, particleVelocity);
		}
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, particlePacket);
	}

	private void reduceSlimySolesFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Float coefficient = boots.get(TFDataComponents.SLIMY_SOLES_COEFFICIENT.get());
		SlimySolesAttachment slimySolesAttachment = livingEntity.getAttachedOrCreate(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		if (!livingEntity.isShiftKeyDown() && TravellersModifiersManager.isModifierActive(livingEntity, boots, TravellersModifiersManager.SLIMY_SOLES_MODIFIER) && coefficient != null && (calculateFallDamage(event) > 0 || slimySolesAttachment.forceBounce)) {
			event.setCanceled(true);
			slimySolesAttachment.bounceVelocity = -livingEntity.getDeltaMovement().y() * Math.sqrt(coefficient);
			slimySolesAttachment.doubleJumpBoostVelocity = slimySolesAttachment.bounceVelocity;
			slimySolesAttachment.hasBounced = false;
			livingEntity.setAttached(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
		}
	}

	// [VanillaCopy]
	private double calculateFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		double safeFallDistance = livingEntity.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
		double unsafeFallDistance = event.getDistance() - safeFallDistance;
		return Mth.ceil(unsafeFallDistance * event.getDamageMultiplier() * livingEntity.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
	}

	private void cancelSlimySolesJump(LivingEvents.LivingJumpEvent event) {
		LivingEntity livingEntity = event.getEntity();
		SlimySolesAttachment slimySolesAttachment = livingEntity.getAttachedOrCreate(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
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
		if (hasDoubleJump != null && hasDoubleJump != player.getAttachedOrCreate(TFDataAttachments.HAS_DOUBLE_JUMP)) {
			player.setAttached(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			player.setAttached(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
			if (instance != null)
				instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
		}

		if (!player.level().isClientSide()) {
			boolean modifierActive = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
			if (!modifierActive && player.getAttachedOrCreate(TFDataAttachments.IS_GRADUALLY_GLIDING)) {
				player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, false);
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new GradualGlidePacket(false, player.getUUID()));
			}
		}

		//reset double jump wing anim if on the ground
		if (event.getEntity().level().isClientSide()) {
			if (player.getAttachedOrCreate(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump && player.onGround()) {
				player.getAttachedOrCreate(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = false;
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

	private void activateAndDeactivateTravellersModifiers(ItemAttributeModifierEvent event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;

		ItemStack armor = event.getItemStack();
		if (!armor.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) || !armor.isDamageableItem())
			return;

		if (armor.getMaxDamage() - 1 <= armor.getDamageValue()) {
			if (armor.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
				Set<ItemAttributeModifiers.Entry> entries = new LinkedHashSet<>(armor.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers());
				if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get())) {
					entries.addAll(armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get()).modifiers());
				}
				armor.set(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get(), new ItemAttributeModifiers(entries.stream().toList(), armor.get(DataComponents.ATTRIBUTE_MODIFIERS).showInTooltip()));
				event.clearModifiers();
			}
		} else {
			if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get())) {
				armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get()).modifiers().forEach(entry -> event.replaceModifier(entry.attribute(), entry.modifier(), entry.slot()));
				armor.remove(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get());
				armor.set(DataComponents.ATTRIBUTE_MODIFIERS, event.build());
			}
		}
	}

	// ArmorHurtEvent needs migration to Fabric API LivingArmorDamageCallback
	/*
	private void stopDamagingTravellersGear(ArmorHurtEvent event) {
		if (event.isCanceled())
			return;
		event.getArmorMap().forEach((slot, entry) -> {
			ItemStack damagedStack = event.getArmorItemStack(slot);
			if (!damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
				return;
			if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage()) {
				event.setNewDamage(slot, damagedStack.getMaxDamage() - damagedStack.getDamageValue() - 1);
			} else if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage() - 1 && event.getEntity() instanceof ServerPlayer player) {
				player.playNotifySound(SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, player.getVoicePitch());
			}
		});
	}

		private void setLastDamageArmorTime(ArmorHurtEvent event) {
		if (Arrays.stream(EquipmentSlot.values()).noneMatch(slot -> event.getNewDamage(slot) > 0)) return;
		LivingEntity entity = event.getEntity();
		entity.setAttached(TFDataAttachments.LAST_DAMAGE_ARMOR_TIME, entity.level().getGameTime());
	}
	*/

	// AnvilUpdateEvent needs migration to Fabric API
	/*
	private void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR.get())) {
			event.setCanceled(true);
		}
	}
	*/

	private void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;
		RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
				.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
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

	// GrindstoneEvent.OnTakeItem 缺少 getPlayer()，需要自定义 mixin
	/*
	private void extractItemsFromSwapHotbarModifier(GrindstoneEvent.OnTakeItem event) {
		returnModifierItems(event,
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER,
			DataComponents.CONTAINER,
			ItemContainerContents::nonEmptyStream
		);

		returnModifierItems(event,
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER,
			TFDataComponents.ITEM_DISPLAY.get(),
			contents -> contents.items().stream()
		);
	}

	private <T> void returnModifierItems(GrindstoneEvent.OnTakeItem event, ResourceKey<TravellersModifier> modifierKey, DataComponentType<T> componentType, Function<T, Stream<ItemStack>> itemStreamExtractor) {
		if (event.getPlayer() == null)
			return;

		getUniqueTravellersGear(event.getTopItem(), event.getBottomItem(), stack ->
			TravellersModifiersManager.hasTravellersModifier(event.getPlayer().registryAccess(), stack, modifierKey)
		).map(stack -> stack.get(componentType))
			.ifPresent(component ->
				itemStreamExtractor.apply(component)
					.forEach(itemStack -> ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), itemStack))
			);
	}

	private Optional<ItemStack> getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}
	*/

	// PlayerSpawnPhantomsEvent needs migration to Fabric API
	/*
	private void cancelPhantomSpawns(PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity(), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}
	*/

	private void fireCraftingModifierTrigger(PlayerEvents.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR.get())) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player, compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player, event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
						.forEach(modifier -> TFAdvancements.ADD_MODIFIER.get().trigger(player, modifier.key().location()));
			}
		}
	}

	public void keepAttachmentsOnDeath(PlayerEvents.Clone event) {
		if (event.isWasDeath()) {
			for (AttachmentType<?> attachmentHolder : ATTACHMENTS_TO_PRESERVE_ON_DEATH) {
				copyAttachmentData(event.getOriginal(), event.getEntity(), attachmentHolder);
			}
		}
	}

	private <T> void copyAttachmentData(Player source, Player target, AttachmentType<T> type) {
		if (source.hasAttached(type)) {
			target.setAttached(type, source.getAttachedOrCreate(type));
		}
	}
}
