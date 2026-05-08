package twilightforest.entity.monster;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.projectile.NatureBolt;
import twilightforest.init.TFItemVisuals;

import java.util.List;

public class Adherent extends Monster implements RangedAttackMob, ITFCharger {
    private static final EntityDataAccessor<Boolean> CHARGE_FLAG = SynchedEntityData.defineId(Adherent.class, EntityDataSerializers.BOOLEAN);

    public Adherent(EntityType<? extends Adherent> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGE_FLAG, false);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        NatureBolt natureBolt = new NatureBolt(this.level(), this);
        this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

        double targetY = target.getY() + target.getEyeHeight() - 1.100000023841858D;
        double deltaX = target.getX() - this.getX();
        double deltaY = targetY - natureBolt.getY();
        double deltaZ = target.getZ() - this.getZ();
        float heightOffset = Mth.sqrt((float) (deltaX * deltaX + deltaZ * deltaZ)) * 0.2F;
        natureBolt.shoot(deltaX, deltaY + heightOffset, deltaZ, 0.6F, 10 - this.level().getDifficulty().getId() * 4);

        this.level().addFreshEntity(natureBolt);
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean isCharging() {
        return this.getEntityData().get(CHARGE_FLAG);
    }

    @Override
    public void setCharging(boolean flag) {
        this.getEntityData().set(CHARGE_FLAG, flag);
    }
}