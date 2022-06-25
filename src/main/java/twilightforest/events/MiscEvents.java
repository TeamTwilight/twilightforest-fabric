package twilightforest.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class MiscEvents {

	@SubscribeEvent
	public static void addPrey(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		EntityType<?> type = entity.getType();
		if (entity instanceof Mob mob) {
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) entity, TinyBird.class, false, null));
			} else if (type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, false));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
			} else if (type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) entity, Bighorn.class, false, null));
			}
		}
	}

	@SubscribeEvent
	public static void armorChanged(LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (!living.getLevel().isClientSide() && living instanceof ServerPlayer) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayer) living, event.getFrom(), event.getTo());
		}
	}

	@SubscribeEvent
	public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		event.getEntityLiving().getCapability(CapabilityList.SHIELDS).ifPresent(IShieldCapability::update);
	}

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		LivingEntity living = event.getEntityLiving();
		// shields
		if (!living.getLevel().isClientSide() && !event.getSource().isBypassArmor()) {
			living.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					event.setCanceled(true);
				}
			});
		}
	}
}
