package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
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
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.goal.ThrowRiderGoal;
import twilightforest.entity.projectile.FallingIce;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.entities.EntityUtil;

import java.util.List;

public class AlphaYeti extends BaseTFBoss implements RangedAttackMob, IHostileMount {
    private static final EntityDataAccessor<Byte> RAMPAGE_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> TIRED_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
    private static final net.minecraft.resources.ResourceLocation RAMPAGE_SPEED_ID = TwilightForestMod.prefix("alpha_yeti_rampage_speed");
    private static final AttributeModifier RAMPAGE_SPEED_MODIFIER = new AttributeModifier(RAMPAGE_SPEED_ID, 0.35D, AttributeModifier.Operation.ADD_VALUE);

    private int collisionCounter;
    private int rampageTicks;
    private int tiredTicks;
    private boolean canRampage;

    public AlphaYeti(EntityType<? extends AlphaYeti> type, Level level) {
        super(type, level);
        this.xpReward = 317;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.38D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.WHITE;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RAMPAGE_FLAG, (byte) 0);
        builder.define(TIRED_FLAG, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ThrowRiderGoal(this, 1.0D, false) {
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                boolean hadPassenger = !AlphaYeti.this.getPassengers().isEmpty();
                super.checkAndPerformAttack(target);
                if (!hadPassenger && !AlphaYeti.this.getPassengers().isEmpty()) {
                    AlphaYeti.this.playSound(TFSounds.ALPHA_YETI_GRAB, 4.0F, 0.75F + AlphaYeti.this.getRandom().nextFloat() * 0.25F);
                }
            }

            @Override
            public void stop() {
                if (!AlphaYeti.this.getPassengers().isEmpty()) {
                    AlphaYeti.this.playSound(TFSounds.ALPHA_YETI_THROW, 4.0F, 0.75F + AlphaYeti.this.getRandom().nextFloat() * 0.25F);
                }
                super.stop();
            }
        });
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true) {
            @Override
            public boolean canUse() {
                return !AlphaYeti.this.isTired() && AlphaYeti.this.getPassengers().isEmpty() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 40.0F) {
            @Override
            public boolean canUse() {
                return !AlphaYeti.this.canRampage && !AlphaYeti.this.isTired() && AlphaYeti.this.getTarget() != null && AlphaYeti.this.distanceToSqr(AlphaYeti.this.getTarget()) >= 16.0D && super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 2.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.updateRampageState();
            if (!this.getPassengers().isEmpty()) {
                this.getLookControl().setLookAt(this.getPassengers().get(0), 100.0F, 100.0F);
            }
            if (this.isRampaging() && (this.horizontalCollision || this.verticalCollision)) {
                this.collisionCounter++;
            }
            if (this.collisionCounter >= 15) {
                this.destroyBlocksInAABB(this.getBoundingBox());
                this.collisionCounter = 0;
            }
        } else {
            this.clientParticles();
        }
    }

    private void updateRampageState() {
        LivingEntity target = this.getTarget();
        if (this.tiredTicks > 0) {
            --this.tiredTicks;
            this.setTired(true);
            this.setRampaging(false);
            this.setRampageSpeed(false);
            if (this.tiredTicks % 10 == 0) {
                this.playSound(TFSounds.ALPHA_YETI_PANT, 4.0F, 0.5F + this.getRandom().nextFloat() * 0.5F);
            }
            return;
        }
        this.setTired(false);
        if (this.canRampage && target != null && !this.isRampaging() && this.random.nextInt(70) == 0) {
            this.rampageTicks = 100 + this.random.nextInt(80);
            this.setRampaging(true);
            this.playSound(TFSounds.ALPHA_YETI_ROAR, 4.0F, 0.5F + this.getRandom().nextFloat() * 0.5F);
            this.gameEvent(GameEvent.ENTITY_ACTION);
        }
        if (this.rampageTicks > 0) {
            --this.rampageTicks;
            this.setRampaging(true);
            this.setRampageSpeed(true);
            this.tickRampageEffects();
            if (target != null) {
                this.getNavigation().moveTo(target, 1.8D);
            }
        } else if (this.isRampaging()) {
            this.setRampaging(false);
            this.setRampageSpeed(false);
            this.tiredTicks = 100;
            this.setTired(true);
        }
    }

    private void tickRampageEffects() {
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().x(), 0.4D, this.getDeltaMovement().z());
            this.gameEvent(GameEvent.HIT_GROUND);
        }
        this.destroyBlocksInAABB(this.getBoundingBox().inflate(1.0D, 2.0D, 1.0D).move(0.0D, 2.0D, 0.0D));
        if (this.rampageTicks % 10 == 0) {
            this.makeRandomBlockFall(30, 80);
        }
        if (this.rampageTicks % 20 == 0) {
            this.makeBlockAboveTargetFall();
        }
        if (this.rampageTicks < 40 && this.rampageTicks % 10 == 0) {
            this.makeRandomBlockFall(15, 40);
        }
        if (this.rampageTicks % 20 == 0) {
            IceBomb ice = new IceBomb(this.level(), this);
            Vec3 vector = new Vec3(0.5F + this.getRandom().nextFloat() * 0.5F, 0.5F + this.getRandom().nextFloat() * 0.3F, 0.0D).yRot(this.getRandom().nextFloat() * 360.0F);
            ice.shoot(vector.x(), vector.y(), vector.z(), 0.4F + this.getRandom().nextFloat() * 0.3F, 0.0F);
            this.playSound(TFSounds.ALPHA_YETI_ICE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.gameEvent(GameEvent.PROJECTILE_SHOOT);
            this.level().addFreshEntity(ice);
        }
    }

    public void makeRandomBlockFall(int range, int hangTime) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        int x = Mth.floor(this.getX()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
        int z = Mth.floor(this.getZ()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
        int y = Mth.floor(this.getY() + this.getEyeHeight());
        this.makeBlockFallAbove(new BlockPos(x, y, z), hangTime);
    }

    public void makeBlockAboveTargetFall() {
        if (this.getTarget() != null) {
            this.makeBlockFallAbove(this.getTarget().blockPosition(), 40);
        }
    }

    private void makeBlockFallAbove(BlockPos pos, int hangTime) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        for (int i = 1; i < 25; i++) {
            BlockPos above = pos.above(i);
            if ((this.level().getBlockState(above).is(Blocks.ICE) || this.level().getBlockState(above).is(Blocks.PACKED_ICE) || this.level().getBlockState(above).is(Blocks.BLUE_ICE)) && this.level().getBlockState(above.below()).isAir()) {
                this.makeBlockFall(above, hangTime);
                break;
            }
        }
    }

    private void makeBlockFall(BlockPos pos, int hangTime) {
        FallingIce ice = new FallingIce(this.level(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, this.level().getBlockState(pos), hangTime);
        this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        this.level().addFreshEntity(ice);
    }

    private void setRampageSpeed(boolean enabled) {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) == null) {
            return;
        }
        if (enabled) {
            if (!this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(RAMPAGE_SPEED_ID)) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(RAMPAGE_SPEED_MODIFIER);
            }
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(RAMPAGE_SPEED_ID);
        }
    }

    private void clientParticles() {
        if (this.isRampaging()) {
            for (int i = 0; i < 20; i++) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
                        0.0D, 0.0D, 0.0D);
            }
        }
        if (this.isTired()) {
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(ParticleTypes.SPLASH,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5D,
                        this.getY() + this.getEyeHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5D,
                        (this.random.nextFloat() - 0.5F) * 0.75F, 0.0D, (this.random.nextFloat() - 0.5F) * 0.75F);
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        if (entity != null && entity != this.getTarget()) {
            this.playSound(TFSounds.ALPHA_YETI_ALERT, 4.0F, 0.5F + this.getRandom().nextFloat() * 0.5F);
        }
        super.setTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.canRampage && !this.isTired() && source.is(DamageTypeTags.IS_PROJECTILE)) {
            return false;
        }
        this.canRampage = true;
        return super.hurt(source, amount);
    }

    @Override
    public void lavaHurt() {
        if (!this.fireImmune()) {
            if (this.hurt(this.damageSources().lava(), 4.0F)) {
                this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.ALPHA_YETI_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.ALPHA_YETI_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.ALPHA_YETI_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.5F + this.getRandom().nextFloat() * 0.5F;
    }

    @Override
    protected float getSoundVolume() {
        return 4.0F;
    }

    public void destroyBlocksInAABB(AABB box) {
        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
            BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
            if (this.level().hasChunksAt(min, max)) {
                for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                    if (EntityUtil.canDestroyBlock(this.level(), pos, this)) {
                        this.level().destroyBlock(pos, false);
                    }
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.canRampage) {
            IceBomb snowball = new IceBomb(this.level(), this);
            double deltaX = target.getX() - this.getX();
            double deltaY = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - snowball.getY();
            double deltaZ = target.getZ() - this.getZ();
            double horizontal = Mth.sqrt((float) (deltaX * deltaX + deltaZ * deltaZ));
            snowball.shoot(deltaX, deltaY + horizontal * 0.2D, deltaZ, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
            this.playSound(TFSounds.ALPHA_YETI_ICE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.gameEvent(GameEvent.PROJECTILE_SHOOT);
            this.level().addFreshEntity(snowball);
        }
    }

    public boolean canRampage() {
        return this.canRampage;
    }

    public void setRampaging(boolean rampaging) {
        this.getEntityData().set(RAMPAGE_FLAG, (byte) (rampaging ? 1 : 0));
    }

    public boolean isRampaging() {
        return this.getEntityData().get(RAMPAGE_FLAG) == 1;
    }

    public void setTired(boolean tired) {
        this.getEntityData().set(TIRED_FLAG, (byte) (tired ? 1 : 0));
        if (tired) {
            this.canRampage = false;
        }
    }

    public boolean isTired() {
        return this.getEntityData().get(TIRED_FLAG) == 1;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        if (!this.level().isClientSide() && this.isRampaging()) {
            this.playSound(TFSounds.ALPHA_YETI_ICE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.hitNearbyEntities();
        }
        return super.causeFallDamage(distance, multiplier, source);
    }

    private void hitNearbyEntities() {
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5.0D, 0.0D, 5.0D))) {
            if (entity != this && entity.hurt(this.damageSources().mobAttack(this), 5.0F)) {
                entity.push(0.0D, 0.4D, 0.0D);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("CanRampage", this.canRampage);
        tag.putBoolean("Rampaging", this.isRampaging());
        tag.putBoolean("Tired", this.isTired());
        tag.putInt("RampageTicks", this.rampageTicks);
        tag.putInt("TiredTicks", this.tiredTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.canRampage = tag.getBoolean("CanRampage");
        this.setRampaging(tag.getBoolean("Rampaging"));
        this.setTired(tag.getBoolean("Tired"));
        this.rampageTicks = tag.getInt("RampageTicks");
        this.tiredTicks = tag.getInt("TiredTicks");
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        Vec3 riderPos = this.getRiderPosition(passenger);
        callback.accept(passenger, riderPos.x(), riderPos.y(), riderPos.z());
    }

    public double getPassengersRidingOffset() {
        return 4.75D;
    }

    private Vec3 getRiderPosition(Entity passenger) {
        double distance = 0.4D;
        double x = Math.cos((this.getYRot() + 90.0F) * Math.PI / 180.0D) * distance;
        double z = Math.sin((this.getYRot() + 90.0F) * Math.PI / 180.0D) * distance;
        return new Vec3(this.getX() + x, this.getY() + this.getPassengersRidingOffset() + passenger.getPassengerRidingPosition(this).y(), this.getZ() + z);
    }

    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public int getHomeRadius() {
        return 30;
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.YETI_CAVE;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.CANOPY_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get();
    }

}
