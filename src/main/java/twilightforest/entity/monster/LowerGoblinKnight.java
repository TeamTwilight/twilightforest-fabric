package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

public class LowerGoblinKnight extends Monster {
    private static final EntityDataAccessor<Boolean> ARMOR = SynchedEntityData.defineId(LowerGoblinKnight.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation ARMOR_ID = TwilightForestMod.prefix("lower_goblin_knight_armor");
    private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(ARMOR_ID, 17.0D, AttributeModifier.Operation.ADD_VALUE);

    public LowerGoblinKnight(EntityType<? extends LowerGoblinKnight> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ARMOR, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.syncArmorModifier();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData spawnData = super.finalizeSpawn(accessor, difficulty, reason, data);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, TFItemVisuals.withModel(new ItemStack(Items.IRON_SWORD), TFItemVisuals.KNIGHTMETAL_SWORD));
        UpperGoblinKnight upper = TFEntities.UPPER_GOBLIN_KNIGHT.get().create(this.level());
        if (upper != null) {
            upper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            upper.finalizeSpawn(accessor, difficulty, MobSpawnType.JOCKEY, null);
            upper.startRiding(this);
        }
        return spawnData;
    }

    public boolean hasArmor() {
        return this.getEntityData().get(ARMOR);
    }

    private void setHasArmor(boolean armored) {
        this.getEntityData().set(ARMOR, armored);
        this.syncArmorModifier();
    }

    private void syncArmorModifier() {
        if (this.level().isClientSide() || this.getAttribute(Attributes.ARMOR) == null) {
            return;
        }
        if (this.hasArmor()) {
            if (!this.getAttribute(Attributes.ARMOR).hasModifier(ARMOR_ID)) {
                this.getAttribute(Attributes.ARMOR).addTransientModifier(ARMOR_MODIFIER);
            }
        } else {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_ID);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.isVehicle() && this.getPassengers().get(0) instanceof LivingEntity living) {
            return living.doHurtTarget(entity);
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker != null) {
            double dx = this.getX() - attacker.getX();
            double dz = this.getZ() - attacker.getZ();
            float angle = (float) (Math.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            float difference = Mth.abs((this.yBodyRot - angle) % 360.0F);
            UpperGoblinKnight upper = this.isVehicle() && this.getPassengers().get(0) instanceof UpperGoblinKnight goblin ? goblin : null;
            if (upper != null && upper.hasShield() && difference > 150.0F && difference < 230.0F && upper.takeHitOnShield(source, amount)) {
                return false;
            }
            if (this.hasArmor() && (difference > 300.0F || difference < 60.0F)) {
                this.breakArmor();
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof UpperGoblinKnight goblin) {
            goblin.setYBodyRot(this.getYRot());
            goblin.setYHeadRot(this.getYRot());
            goblin.setYRot(this.getYRot());
        }
    }

    public double getPassengersRidingOffset() {
        return 1.0D;
    }

    private void breakArmor() {
        this.level().broadcastEntityEvent(this, (byte) 5);
        this.setHasArmor(false);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 5) {
            this.playSound(SoundEvents.ITEM_BREAK, 0.8F, 0.8F + this.getRandom().nextFloat() * 0.4F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasArmor() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_AMBIENT : TFSounds.GOBLIN_KNIGHT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.hasArmor() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_HURT : TFSounds.GOBLIN_KNIGHT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.hasArmor() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_DEATH : TFSounds.GOBLIN_KNIGHT_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("hasArmor", this.hasArmor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasArmor(tag.getBoolean("hasArmor"));
    }
}