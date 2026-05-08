package twilightforest.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

public class MistWolf extends HostileWolf {
    public MistWolf(EntityType<? extends MistWolf> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return HostileWolf.registerAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected int getDisplayModel() {
        return twilightforest.init.TFItemVisuals.MIST_WOLF_DISPLAY;
    }

    @Override
    protected float getDisplayScale() {
        return 1.18F;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)) {
            float brightness = this.level().getMaxLocalRawBrightness(this.blockPosition());
            if (entity instanceof LivingEntity living && brightness < 0.10F && !this.level().getBlockState(this.blockPosition()).isSolid()) {
                int duration = switch (this.level().getDifficulty()) {
                    case EASY -> 0;
                    case HARD -> 15;
                    default -> 7;
                };
                if (duration > 0) {
                    living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration * 20, 0));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected SoundEvent getTargetSound() {
        return TFSounds.MIST_WOLF_TARGET;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.MIST_WOLF_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.MIST_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.MIST_WOLF_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.6F;
    }
}