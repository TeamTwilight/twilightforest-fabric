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
import twilightforest.events.*;
import twilightforest.events.EntityEvents;

public final class TFCommonEvents {
	public static void init() {
		setupLootEvents();
		setupHostileMountEvents();
		setupMiscEvents();
		setupCapabilityEvents();
		setupToolEvents();
		setupProgressionEvents();
		setupTravellersGearEvents();
		setupEntityEvents();
	}

	private static void setupLootEvents() {
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> LootEvents.handleFieryToolDrops(context, drops));
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> LootEvents.handleGiantToolGrouping(context, drops));
	}

	private static void setupHostileMountEvents() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(HostileMountEvents::handleMountDamage);
		carminite.events.api.EntityEvents.ENTITY_MOUNT.register(HostileMountEvents::preventMountDismount);
		TickEvents.ENTITY_TICK_POST.register(HostileMountEvents::preventHostileMountCrouching);
	}

	private static void setupMiscEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> MiscEvents.addPrey(entity));
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, _, currentStack) -> MiscEvents.updateCicadaSoundsOnHead(livingEntity, equipmentSlot, currentStack));
		UseBlockCallback.EVENT.register(MiscEvents::addTomesToLecterns);
		UseBlockCallback.EVENT.register(MiscEvents::washOffCloth);
	}

	private static void setupCapabilityEvents() {
		TickEvents.ENTITY_TICK_POST.register(CapabilityEvents::updateShields);
		TickEvents.PLAYER_TICK_POST.register(CapabilityEvents::updatePlayerCaps);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(CapabilityEvents::absorbShieldHits);
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, _) -> CapabilityEvents.spawnInTFIfNecessary(newPlayer));
		ServerPlayerEvents.JOIN.register(CapabilityEvents::playerLogsIn);
	}

	private static void setupToolEvents() {
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(ToolEvents::onEnderBowHit);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> ToolEvents.fieryToolSetFire(entity, source));
		PlayerBlockBreakEvents.BEFORE.register((_, player, _, state, _) -> ToolEvents.damageNonMazebreakerToolsMore(player, state));
		ServerMobEffectEvents.ALLOW_ADD.register((effectInstance, entity, _) -> ToolEvents.preventFatigueWithPocketWatch(effectInstance, entity));
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, _) -> ToolEvents.handleGiantPickaxeMining(level, player, pos, state));
		CommonLifecycleEvents.TAGS_LOADED.register((_, _) -> ToolEvents.refreshOreMagnetCache());
	}

	private static void setupProgressionEvents() {
		GameRuleEvents.changeCallback(TFGameRules.ENFORCED_PROGRESSION_RULE).register(ProgressionEvents::gameRuleChanged);
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, _, _) -> ProgressionEvents.preventLockedAreaBlockBreaking(level, player, pos));
		PlayerEvents.RIGHT_CLICK_BLOCK.register(ProgressionEvents::preventLockedAreaBlockPlacing);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(ProgressionEvents::preventLockedAreaBlockInteracting);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> ProgressionEvents.preventLockedAreaEntityDamage(entity, source));
		TickEvents.PLAYER_TICK_POST.register(ProgressionEvents::performProtectionAndPortalChecks);
		ServerPlayerEvents.JOIN.register(ProgressionEvents::syncProgressionGameRuleStatus);
	}

	private static void setupTravellersGearEvents() {
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(TravellersGearEvents::magnetizeArrows);
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(TravellersGearEvents::performPerfectDodge);
		LivingEvents.LIVING_FALL.register(TravellersGearEvents::reduceSlimySolesFallDamage);
		LivingEvents.LIVING_JUMP.register(TravellersGearEvents::cancelSlimySolesJump);
		LevelEvents.ITEM_ATTRIBUTE_MODIFIERS.register(TravellersGearEvents::activateAndDeactivateTravellersModifiers);
		TickEvents.PLAYER_TICK_PRE.register(TravellersGearEvents::tickMovementModifiers);
		TickEvents.PLAYER_TICK_POST.register(TravellersGearEvents::performStealth);
		TickEvents.PLAYER_TICK_PRE.register(TravellersGearEvents::disableHighStepWhileSneaking);
		TickEvents.ENTITY_TICK_POST.register(TravellersGearEvents::updateOtherModifiers);
		LivingEvents.ARMOR_HURT.register(TravellersGearEvents::stopDamagingTravellersGear);
		LivingEvents.ARMOR_HURT.register(TravellersGearEvents::setLastDamageArmorTime);
		WorkstationEvents.ANVIL_UPDATE.register(TravellersGearEvents::cancelCombiningTravellersGear);
		PlayerEvents.SPAWN_PHANTOMS.register(TravellersGearEvents::cancelPhantomSpawns);
		WorkstationEvents.GRINDSTONE_PLACE.register(TravellersGearEvents::removeModifiersFromTravellersGear);
		WorkstationEvents.GRINDSTONE_TAKE.register(TravellersGearEvents::extractItemsFromSwapHotbarModifier);
		PlayerEvents.ITEM_CRAFTED.register(TravellersGearEvents::fireCraftingModifierTrigger);
		ServerPlayerEvents.COPY_FROM.register(TravellersGearEvents::keepAttachmentsOnDeath);
	}

	private static void setupEntityEvents() {
		LivingEvents.LIVING_DEATH.register(EntityEvents::ominousFireConversion);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityEvents::zombifiedPlayerAttacks);
		PlayerEvents.ADVANCEMENT_EARNED.register(EntityEvents::alertPlayerCastleIsWIP);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(EntityEvents::attachLeadToWroughtFence);
		PlayerEvents.LEFT_CLICK_EMPTY.register(EntityEvents::wipeOreMeterOnLeftClick);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEvents::entityHurts);
		PlayerBlockBreakEvents.BEFORE.register(EntityEvents::onCasketBreak);
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(EntityEvents::onParryProjectile);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(EntityEvents::createSkullCandle);
		LivingEvents.LIVING_JUMP.register(EntityEvents::addCloudJumpParticles);
		PlayerEvents.ATTACK_ENTITY.register(EntityEvents::removeCastleTextIfAttacked);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEvents::addQualifiedGroupPlayerIfNeeded);
		LivingEvents.LIVING_DEATH.register(EntityEvents::grantGroupAdvancementIfNeeded);
		LevelEvents.DETONATE.register(EntityEvents::lichBombsDontBlowUpItems);
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, _) -> EntityEvents.handleQuestSyncing(player));
		PlayerEvents.ADVANCEMENT_EARNED.register(EntityEvents::resetFlaskLogic);
		carminite.events.api.EntityEvents.JOIN_LEVEL.register(EntityEvents::handleLeashPathingOverrides);
		carminite.events.api.EntityEvents.JOIN_LEVEL.register(EntityEvents::stopEndermenFromGrabbingBlocksInTF);
	}
}