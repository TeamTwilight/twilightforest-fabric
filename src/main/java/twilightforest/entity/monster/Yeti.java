package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFSounds;
import twilightforest.init.TFItemVisuals;

import java.util.List;
import java.util.Objects;

public class Yeti extends Monster {
    private static final ResourceLocation ANGRY_FOLLOW_RANGE_ID = TwilightForestMod.prefix("angry_yeti_follow_range");
    private static final AttributeModifier ANGRY_MODIFIER = new AttributeModifier(ANGRY_FOLLOW_RANGE_ID, 24.0D, AttributeModifier.Operation.ADD_VALUE);
    private boolean angry;

    public Yeti(EntityType<? extends Yeti> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.38D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 4.0D);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }
        if (!this.getPassengers().isEmpty()) {
            this.getLookControl().setLookAt(this.getPassengers().get(0), 100.0F, 100.0F);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && !source.isCreativePlayer()) {
            this.setAngry(true);
        }
        return super.hurt(source, amount);
    }

    public boolean isAngry() {
        return this.angry;
    }

    public void setAngry(boolean angry) {
        this.angry = angry;
        if (!this.level().isClientSide()) {
            if (angry) {
                if (!Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).hasModifier(ANGRY_FOLLOW_RANGE_ID)) {
                    Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addTransientModifier(ANGRY_MODIFIER);
                }
            } else {
                Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).removeModifier(ANGRY_MODIFIER);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Angry", this.isAngry());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAngry(tag.getBoolean("Angry"));
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        Vec3 riderPos = this.getRiderPosition(passenger);
        callback.accept(passenger, riderPos.x(), riderPos.y(), riderPos.z());
    }

    public double getPassengersRidingOffset() {
        return 2.25D;
    }

    private Vec3 getRiderPosition(Entity passenger) {
        float distance = 0.4F;
        double dx = Math.cos((this.getYRot() + 90.0F) * Math.PI / 180.0D) * distance;
        double dz = Math.sin((this.getYRot() + 90.0F) * Math.PI / 180.0D) * distance;
        return new Vec3(this.getX() + dx, this.getY() + this.getPassengersRidingOffset() + passenger.getPassengerRidingPosition(this).y(), this.getZ() + dz);
    }

    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() + 0.55F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.YETI_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.YETI_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.YETI_DEATH;
    }
}