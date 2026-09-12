package twilightforest.init;

import carminite.events.api.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import twilightforest.listeners.*;
import twilightforest.listeners.EntityEventListeners;

public final class TFCommonEvents {
	public static void init() {
		setupLootEvents();
		setupCharmEvents();
		setupHostileMountEvents();
		setupMiscEvents();
		setupCapabilityEvents();
		setupToolEvents();
		setupProgressionEvents();
		setupTravellersGearEvents();
		setupEntityEvents();
	}

	private static void setupLootEvents() {
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> LootEventListeners.handleFieryToolDrops(context, drops));
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> LootEventListeners.handleGiantToolGrouping(context, drops));
	}

	private static void setupCharmEvents() {
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, _, _) -> CharmEventListeners.applyCharmOfLife(entity));
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, _, _) -> CharmEventListeners.applyKeepingAndCasket(entity));
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, alive) -> CharmEventListeners.returnItemsOnRespawn(newPlayer, alive));
	}

	private static void setupHostileMountEvents() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(HostileMountEventListeners::handleMountDamage);
		EntityEvents.CARMINITE_ENTITY_TELEPORT.register(HostileMountEventListeners::preventTeleportingOffHostileMounts);
		EntityEvents.ENTITY_MOUNT.register(HostileMountEventListeners::preventMountDismount);
		TickEvents.ENTITY_TICK_POST.register(HostileMountEventListeners::preventHostileMountCrouching);
	}

	private static void setupMiscEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> MiscEventListeners.addPrey(entity));
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, _, currentStack) -> MiscEventListeners.updateCicadaSoundsOnHead(livingEntity, equipmentSlot, currentStack));
		UseBlockCallback.EVENT.register(MiscEventListeners::addTomesToLecterns);
		UseBlockCallback.EVENT.register(MiscEventListeners::washOffCloth);
	}

	private static void setupCapabilityEvents() {
		TickEvents.ENTITY_TICK_POST.register(CapabilityEventListeners::updateShields);
		TickEvents.PLAYER_TICK_POST.register(CapabilityEventListeners::updatePlayerCaps);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(CapabilityEventListeners::absorbShieldHits);
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, _) -> CapabilityEventListeners.spawnInTFIfNecessary(newPlayer));
		ServerPlayerEvents.JOIN.register(CapabilityEventListeners::playerLogsIn);
	}

	private static void setupToolEvents() {
		EntityEvents.PROJECTILE_IMPACT.register(ToolEventListeners::onEnderBowHit);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> ToolEventListeners.fieryToolSetFire(entity, source));
		PlayerBlockBreakEvents.BEFORE.register((_, player, _, state, _) -> ToolEventListeners.damageNonMazebreakerToolsMore(player, state));
		ServerMobEffectEvents.ALLOW_ADD.register((effectInstance, entity, _) -> ToolEventListeners.preventFatigueWithPocketWatch(effectInstance, entity));
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, _) -> ToolEventListeners.handleGiantPickaxeMining(level, player, pos, state));
		CommonLifecycleEvents.TAGS_LOADED.register((_, _) -> ToolEventListeners.refreshOreMagnetCache());
	}

	private static void setupProgressionEvents() {
		GameRuleEvents.changeCallback(TFGameRules.ENFORCED_PROGRESSION_RULE).register(ProgressionEventListeners::gameRuleChanged);
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, _, _) -> ProgressionEventListeners.preventLockedAreaBlockBreaking(level, player, pos));
		PlayerEvents.RIGHT_CLICK_BLOCK.register(ProgressionEventListeners::preventLockedAreaBlockPlacing);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(ProgressionEventListeners::preventLockedAreaBlockInteracting);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> ProgressionEventListeners.preventLockedAreaEntityDamage(entity, source));
		TickEvents.PLAYER_TICK_POST.register(ProgressionEventListeners::performProtectionAndPortalChecks);
		ServerPlayerEvents.JOIN.register(ProgressionEventListeners::syncProgressionGameRuleStatus);
	}

	private static void setupTravellersGearEvents() {
		EntityEvents.PROJECTILE_IMPACT.register(TravellersGearEventListeners::magnetizeArrows);
		EntityEvents.PROJECTILE_IMPACT.register(TravellersGearEventListeners::performPerfectDodge);
		LivingEvents.LIVING_FALL.register(TravellersGearEventListeners::reduceSlimySolesFallDamage);
		LivingEvents.LIVING_JUMP.register(TravellersGearEventListeners::cancelSlimySolesJump);
		LevelEvents.ITEM_ATTRIBUTE_MODIFIERS.register(TravellersGearEventListeners::activateAndDeactivateTravellersModifiers);
		TickEvents.PLAYER_TICK_PRE.register(TravellersGearEventListeners::tickMovementModifiers);
		TickEvents.PLAYER_TICK_POST.register(TravellersGearEventListeners::performStealth);
		TickEvents.PLAYER_TICK_PRE.register(TravellersGearEventListeners::disableHighStepWhileSneaking);
		TickEvents.ENTITY_TICK_POST.register(TravellersGearEventListeners::updateOtherModifiers);
		LivingEvents.ARMOR_HURT.register(TravellersGearEventListeners::stopDamagingTravellersGear);
		LivingEvents.ARMOR_HURT.register(TravellersGearEventListeners::setLastDamageArmorTime);
		WorkstationEvents.ANVIL_UPDATE.register(TravellersGearEventListeners::cancelCombiningTravellersGear);
		PlayerEvents.SPAWN_PHANTOMS.register(TravellersGearEventListeners::cancelPhantomSpawns);
		WorkstationEvents.GRINDSTONE_PLACE.register(TravellersGearEventListeners::removeModifiersFromTravellersGear);
		WorkstationEvents.GRINDSTONE_TAKE.register(TravellersGearEventListeners::extractItemsFromSwapHotbarModifier);
		PlayerEvents.ITEM_CRAFTED.register(TravellersGearEventListeners::fireCraftingModifierTrigger);
		ServerPlayerEvents.COPY_FROM.register(TravellersGearEventListeners::keepAttachmentsOnDeath);
	}

	private static void setupEntityEvents() {
		LivingEvents.LIVING_DEATH.register(EntityEventListeners::ominousFireConversion);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityEventListeners::zombifiedPlayerAttacks);
		PlayerEvents.ADVANCEMENT_EARNED.register(EntityEventListeners::alertPlayerCastleIsWIP);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(EntityEventListeners::attachLeadToWroughtFence);
		PlayerEvents.LEFT_CLICK_EMPTY.register(EntityEventListeners::wipeOreMeterOnLeftClick);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEventListeners::entityHurts);
		PlayerBlockBreakEvents.BEFORE.register(EntityEventListeners::onCasketBreak);
		EntityEvents.PROJECTILE_IMPACT.register(EntityEventListeners::onParryProjectile);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(EntityEventListeners::createSkullCandle);
		LivingEvents.LIVING_JUMP.register(EntityEventListeners::addCloudJumpParticles);
		PlayerEvents.ATTACK_ENTITY.register(EntityEventListeners::removeCastleTextIfAttacked);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEventListeners::addQualifiedGroupPlayerIfNeeded);
		LivingEvents.LIVING_DEATH.register(EntityEventListeners::grantGroupAdvancementIfNeeded);
		LevelEvents.DETONATE.register(EntityEventListeners::lichBombsDontBlowUpItems);
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, _) -> EntityEventListeners.handleQuestSyncing(player));
		PlayerEvents.ADVANCEMENT_EARNED.register(EntityEventListeners::resetFlaskLogic);
		EntityEvents.JOIN_LEVEL.register(EntityEventListeners::handleLeashPathingOverrides);
		EntityEvents.JOIN_LEVEL.register(EntityEventListeners::stopEndermenFromGrabbingBlocksInTF);
	}
}