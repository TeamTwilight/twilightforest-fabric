package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.projectile.SlimeProjectile;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class SlimeBeetle extends Monster implements RangedAttackMob {
    public SlimeBeetle(EntityType<? extends SlimeBeetle> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 3.0F, 1.25D, 2.0D));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 30, 10.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        SlimeProjectile projectile = new SlimeProjectile(TFEntities.SLIME_BLOB.get(), this.level(), this);
        projectile.setPos(this.getX(), this.getY() + 0.25D, this.getZ());
        double deltaX = target.getX() - this.getX();
        double deltaY = target.getY() + target.getEyeHeight() - 1.1D - projectile.getY();
        double deltaZ = target.getZ() - this.getZ();
        float arc = Mth.sqrt((float) (deltaX * deltaX + deltaZ * deltaZ)) * 0.2F;
        projectile.shoot(deltaX, deltaY + arc, deltaZ, 0.6F, 6.0F);
        this.playSound(TFSounds.SLIME_BEETLE_SQUISH, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(projectile);
    }

    public double getMyRidingOffset() {
        return -0.15D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SLIME_BEETLE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SLIME_BEETLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SLIME_BEETLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.SLIME_BEETLE_STEP, 0.15F, 1.0F);
    }
}