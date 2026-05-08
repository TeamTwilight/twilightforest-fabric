package twilightforest.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class LichMinion extends Zombie {
    /** P5.e: visibility bumped private→public so upstream-style Lich goal classes
     *  can do {@code minion.master == this.lich} identity comparison without a getter call. */
    public LivingEntity master;

    public LichMinion(EntityType<? extends LichMinion> type, Level level) {
        super(type, level);
    }

    public LichMinion(Level level, LivingEntity master) {
        super(TFEntities.LICH_MINION.get(), level);
        this.master = master;
    }

    public LivingEntity getMaster() {
        return this.master;
    }

    public void setMaster(LivingEntity master) {
        this.master = master;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        LivingEntity previousTarget = this.getTarget();
        if (super.hurt(source, amount)) {
            if (this.master != null && source.getEntity() == this.master) {
                this.setLastHurtByMob(previousTarget);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 4));
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
            }
            return true;
        }
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.MINION_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.MINION_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.MINION_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return TFSounds.MINION_STEP;
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide() && this.master != null && !this.master.isAlive()) {
            this.kill();
        }
        super.aiStep();
    }
}