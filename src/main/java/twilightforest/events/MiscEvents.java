package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.item.ItemStack;
import twilightforest.advancements.TFAdvancements;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MiscEvents {

	public static void init() {
		ServerEntityEvents.ENTITY_LOAD.register(MiscEvents::addPrey);
		LivingEntityEvents.EQUIPMENT_CHANGE.register(MiscEvents::armorChanged);
		LivingEntityEvents.TICK.register(MiscEvents::livingUpdate);
		LivingEntityEvents.ATTACK.register(MiscEvents::livingAttack);
	}

	public static void addPrey(Entity entity, ServerLevel world) {
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

	public static void armorChanged(LivingEntity living, EquipmentSlot slot, @Nonnull ItemStack from, @Nonnull ItemStack to) {
		if (!living.getLevel().isClientSide() && living instanceof ServerPlayer) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayer) living, from, to);
		}
	}

	public static void livingUpdate(LivingEntity entity) {
		CapabilityList.SHIELDS.maybeGet(entity).ifPresent(IShieldCapability::update);
	}

	public static boolean livingAttack(LivingEntity living, DamageSource source, float amount) {
		// shields
		if (!living.getLevel().isClientSide() && !source.isBypassArmor()) {
			Optional<IShieldCapability> optional = CapabilityList.SHIELDS.maybeGet(living);
			if (optional.isPresent()) {
				IShieldCapability cap = optional.get();
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					return true;
				}
			}
		}
		return false;
	}
}
