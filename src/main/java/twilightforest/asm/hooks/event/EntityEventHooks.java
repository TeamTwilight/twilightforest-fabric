package twilightforest.asm.hooks.event;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.init.TFMobEffects;

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
}