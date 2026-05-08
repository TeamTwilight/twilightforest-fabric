package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.init.TFSounds;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Kobold extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_PANICKED =
            SynchedEntityData.defineId(Kobold.class, EntityDataSerializers.BOOLEAN);

    private int lastEatenBreadTicks;
    private int eatingTime;

    public Kobold(EntityType<? extends Kobold> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PANICKED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SeekBreadGoal(this));
        this.goalSelector.addGoal(2, new RunAwayWhileHoldingBreadGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new KoboldAttackPlayerTarget(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 13.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public boolean isPanicked() {
        return this.getEntityData().get(DATA_PANICKED);
    }

    public void setPanicked(boolean panicked) {
        this.getEntityData().set(DATA_PANICKED, panicked);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        ItemStack held = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!this.level().isClientSide() && this.isAlive() && isPacificationBread(held)) {
            ++this.lastEatenBreadTicks;
            if (this.eatingTime > 0) {
                --this.eatingTime;
            }
            if (this.canEat(held) && this.eatingTime <= 0) {
                ItemStack remainder = held.finishUsingItem(this.level(), this);
                this.setItemSlot(EquipmentSlot.MAINHAND, remainder);
            }
            if (this.lastEatenBreadTicks > 60 && this.getRandom().nextFloat() < 0.1F) {
                this.playSound(this.getEatingSound(held), 0.75F, 0.9F);
                this.gameEvent(GameEvent.EAT);
                this.lastEatenBreadTicks = 0;
            }
        }
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return TFSounds.KOBOLD_MUNCH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.KOBOLD_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.KOBOLD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.KOBOLD_DEATH;
    }

    private boolean canEat(ItemStack stack) {
        return stack.is(Items.BREAD) && !this.isPanicked();
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        return this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && isPacificationBread(stack) && super.canTakeItem(stack);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && isPacificationBread(stack) && !this.isPanicked();
    }

    @Override
    protected void pickUpItem(ItemEntity item) {
        ItemStack stack = item.getItem();
        if (this.canHoldItem(stack)) {
            int count = stack.getCount();
            if (count > 1) {
                this.dropItemStack(stack.split(count - 1));
            }
            this.onItemPickup(item);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(item, stack.getCount());
            this.gameEvent(GameEvent.EQUIP);
            item.discard();
            this.lastEatenBreadTicks = 1;
            this.eatingTime = this.difficultyTime() + this.getRandom().nextInt(600);
            this.setTarget(null);
        }
    }

    private int difficultyTime() {
        return switch (this.level().getDifficulty()) {
            case EASY -> 400;
            case NORMAL -> 200;
            case HARD -> 100;
            default -> 200;
        };
    }

    private void dropItemStack(ItemStack stack) {
        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Panicked", this.isPanicked());
        tag.putInt("EatingTimeLeft", this.eatingTime);
        tag.putInt("TimeSinceBreadLastEaten", this.lastEatenBreadTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPanicked(tag.getBoolean("Panicked"));
        this.eatingTime = tag.getInt("EatingTimeLeft");
        this.lastEatenBreadTicks = tag.getInt("TimeSinceBreadLastEaten");
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    private static boolean isPacificationBread(ItemStack stack) {
        return stack.is(Items.BREAD) || stack.is(Items.WHEAT);
    }

    private static class KoboldAttackPlayerTarget extends NearestAttackableTargetGoal<Player> {
        public KoboldAttackPlayerTarget(Kobold mob) {
            super(mob, Player.class, true);
        }

        @Override
        public boolean canUse() {
            return !isPacificationBread(this.mob.getItemBySlot(EquipmentSlot.MAINHAND)) && super.canUse();
        }
    }

    private static class SeekBreadGoal extends Goal {
        private static final Predicate<ItemEntity> ALLOWED_ITEMS = item -> isPacificationBread(item.getItem());
        private final Kobold mob;

        public SeekBreadGoal(Kobold mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (!this.mob.getUseItem().isEmpty() || this.mob.isPanicked() || this.mob.getRandom().nextInt(10) != 0) {
                return false;
            }
            List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0D), ALLOWED_ITEMS);
            return !items.isEmpty() && this.mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }

        @Override
        public void tick() {
            List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0D), ALLOWED_ITEMS);
            if (this.mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && !items.isEmpty()) {
                ItemEntity item = items.get(0);
                this.mob.getNavigation().moveTo(item, 1.2F);
                this.mob.getLookControl().setLookAt(item.getX(), item.getY(), item.getZ());
            }
        }

        @Override
        public void start() {
            List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0D), ALLOWED_ITEMS);
            if (!items.isEmpty()) {
                this.mob.getNavigation().moveTo(items.get(0), 1.2F);
            }
        }
    }

    private static class RunAwayWhileHoldingBreadGoal extends AvoidEntityGoal<Player> {
        public RunAwayWhileHoldingBreadGoal(Kobold mob) {
            super(mob, Player.class, 8.0F, 1.5F, 1.5F);
        }

        @Override
        public boolean canUse() {
            return isPacificationBread(this.mob.getItemBySlot(EquipmentSlot.MAINHAND)) && super.canUse();
        }
    }
}
