package twilightforest;

import carminite.events.api.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import twilightforest.events.*;
import twilightforest.events.EntityEvents;
import twilightforest.init.TFGameRules;

public final class TFCommonEvents {
	private static final LootEvents lootEvents = LootEvents.INSTANCE;
	private static final HostileMountEvents hostileMountEvents = HostileMountEvents.INSTANCE;
	private static final MiscEvents miscEvents = MiscEvents.INSTANCE;
	private static final CapabilityEvents capabilityEvents = CapabilityEvents.INSTANCE;
	private static final ToolEvents toolEvents = ToolEvents.INSTANCE;
	private static final ProgressionEvents progressionEvents = ProgressionEvents.INSTANCE;
	private static final TravellersGearEvents travellersGearEvents = TravellersGearEvents.INSTANCE;
	private static final EntityEvents entityEvents = EntityEvents.INSTANCE;

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
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> lootEvents.handleFieryToolDrops(context, drops));
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> lootEvents.handleGiantToolGrouping(context, drops));
	}

	private static void setupHostileMountEvents() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(hostileMountEvents::handleMountDamage);
		carminite.events.api.EntityEvents.ENTITY_MOUNT.register(hostileMountEvents::preventMountDismount);
		TickEvents.ENTITY_TICK_POST.register(hostileMountEvents::preventHostileMountCrouching);
	}

	private static void setupMiscEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> miscEvents.addPrey(entity));
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, _, currentStack) -> miscEvents.updateCicadaSoundsOnHead(livingEntity, equipmentSlot, currentStack));
		UseBlockCallback.EVENT.register(miscEvents::addTomesToLecterns);
		UseBlockCallback.EVENT.register(miscEvents::washOffCloth);
	}

	private static void setupCapabilityEvents() {
		TickEvents.ENTITY_TICK_POST.register(capabilityEvents::updateShields);
		TickEvents.PLAYER_TICK_POST.register(capabilityEvents::updatePlayerCaps);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(capabilityEvents::absorbShieldHits);
		ServerPlayerEvents.AFTER_RESPAWN.register((_, newPlayer, _) -> capabilityEvents.spawnInTFIfNecessary(newPlayer));
		ServerPlayerEvents.JOIN.register(capabilityEvents::playerLogsIn);
	}

	private static void setupToolEvents() {
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(toolEvents::onEnderBowHit);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> toolEvents.fieryToolSetFire(entity, source));
		PlayerBlockBreakEvents.BEFORE.register((_, player, _, state, _) -> toolEvents.damageNonMazebreakerToolsMore(player, state));
		ServerMobEffectEvents.ALLOW_ADD.register((effectInstance, entity, _) -> toolEvents.preventFatigueWithPocketWatch(effectInstance, entity));
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, _) -> toolEvents.handleGiantPickaxeMining(level, player, pos, state));
		CommonLifecycleEvents.TAGS_LOADED.register((_, _) -> toolEvents.refreshOreMagnetCache());
	}

	private static void setupProgressionEvents() {
		GameRuleEvents.changeCallback(TFGameRules.ENFORCED_PROGRESSION_RULE).register(progressionEvents::gameRuleChanged);
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, _, _) -> progressionEvents.preventLockedAreaBlockBreaking(level, player, pos));
		PlayerEvents.RIGHT_CLICK_BLOCK.register(progressionEvents::preventLockedAreaBlockPlacing);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(progressionEvents::preventLockedAreaBlockInteracting);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> progressionEvents.preventLockedAreaEntityDamage(entity, source));
		TickEvents.PLAYER_TICK_POST.register(progressionEvents::performProtectionAndPortalChecks);
		ServerPlayerEvents.JOIN.register(progressionEvents::syncProgressionGameRuleStatus);
	}

	private static void setupTravellersGearEvents() {
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(travellersGearEvents::magnetizeArrows);
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(travellersGearEvents::performPerfectDodge);
		LivingEvents.LIVING_JUMP.register(travellersGearEvents::cancelSlimySolesJump);
		TickEvents.PLAYER_TICK_PRE.register(travellersGearEvents::tickMovementModifiers);
		TickEvents.PLAYER_TICK_POST.register(travellersGearEvents::performStealth);
		TickEvents.PLAYER_TICK_PRE.register(travellersGearEvents::disableHighStepWhileSneaking);
		TickEvents.ENTITY_TICK_POST.register(travellersGearEvents::updateOtherModifiers);
		LivingEvents.ARMOR_HURT.register(travellersGearEvents::stopDamagingTravellersGear);
		LivingEvents.ARMOR_HURT.register(travellersGearEvents::setLastDamageArmorTime);
		WorkstationEvents.ANVIL_UPDATE.register(travellersGearEvents::cancelCombiningTravellersGear);
		WorkstationEvents.GRINDSTONE_PLACE.register(travellersGearEvents::removeModifiersFromTravellersGear);
		WorkstationEvents.GRINDSTONE_TAKE.register(travellersGearEvents::extractItemsFromSwapHotbarModifier);
		PlayerEvents.ITEM_CRAFTED.register(travellersGearEvents::fireCraftingModifierTrigger);
		ServerPlayerEvents.COPY_FROM.register(travellersGearEvents::keepAttachmentsOnDeath);
	}

	private static void setupEntityEvents() {
		LivingEvents.LIVING_DEATH.register(entityEvents::ominousFireConversion);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(entityEvents::zombifiedPlayerAttacks);
		PlayerEvents.ADVANCEMENT_EARNED.register(entityEvents::alertPlayerCastleIsWIP);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(entityEvents::attachLeadToWroughtFence);
		PlayerEvents.LEFT_CLICK_EMPTY.register(entityEvents::wipeOreMeterOnLeftClick);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(entityEvents::entityHurts);
		PlayerBlockBreakEvents.BEFORE.register(entityEvents::onCasketBreak);
		carminite.events.api.EntityEvents.PROJECTILE_IMPACT.register(entityEvents::onParryProjectile);
		PlayerEvents.RIGHT_CLICK_BLOCK.register(entityEvents::createSkullCandle);
		LivingEvents.LIVING_JUMP.register(entityEvents::addCloudJumpParticles);
		PlayerEvents.ATTACK_ENTITY.register(entityEvents::removeCastleTextIfAttacked);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(entityEvents::addQualifiedGroupPlayerIfNeeded);
		LivingEvents.LIVING_DEATH.register(entityEvents::grantGroupAdvancementIfNeeded);
		LevelEvents.DETONATE.register(entityEvents::lichBombsDontBlowUpItems);
		PlayerEvents.ADVANCEMENT_EARNED.register(entityEvents::resetFlaskLogic);
		carminite.events.api.EntityEvents.JOIN_LEVEL.register(entityEvents::handleLeashPathingOverrides);
		carminite.events.api.EntityEvents.JOIN_LEVEL.register(entityEvents::stopEndermenFromGrabbingBlocksInTF);
	}
}