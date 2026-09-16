package twilightforest.asm.hooks.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import twilightforest.TFCommon;
import twilightforest.config.TFConfig;
import twilightforest.init.TFMobEffects;
import twilightforest.tags.TFEntityTypeTags;

import java.util.List;

public final class EntityEventHooks {
	public static float reduceFrostedEffectIfOnFire(LivingEntity target, DamageSource source, float originalDamage, float newDamage) {
		MobEffectInstance effect = target.getEffect(TFMobEffects.FROSTY);

		if (effect == null) {
			return newDamage;
		}

		if (source.typeHolder().is(DamageTypes.FREEZE)) {
			return originalDamage + (float) (effect.getAmplifier() / 2);
		}

		if (source.typeHolder().is(DamageTypeTags.IS_FIRE)) {
			target.removeEffect(TFMobEffects.FROSTY);

			effect.amplifier -= 1;

			if (effect.amplifier >= 0) {
				target.addEffect(effect);
			}
		}

		return newDamage;
	}

	public static void adjustEntityHealthInMultiplayerFights(ServerLevel level, Mob mob, DifficultyInstance difficulty) {
		if (mob.is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			if (TFConfig.multiplayerFightAdjuster.adjustsHealth()) {
				List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, mob.getBoundingBox().inflate(32, 10, 32), player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player));
				if (nearbyPlayers.size() > 1 && mob.getAttribute(Attributes.MAX_HEALTH) != null) {
					mob.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TFCommon.prefix("group_health_boost"), getHealthBasedOnDifficulty(difficulty.getDifficulty()) * (nearbyPlayers.size() - 1), AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}

	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}
}