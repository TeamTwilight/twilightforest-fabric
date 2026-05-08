package twilightforest.entity.boss;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.control.NagaMoveControl;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.ai.goal.NagaSmashGoal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.entities.EntityUtil;

import java.util.List;
import java.util.Objects;

public class Naga extends BaseTFBoss implements TFPart.Owner {
    private static final int TICKS_BEFORE_HEALING = 600;
    private static final int MAX_SEGMENTS = 12;
    private static final int XZ_HOME_BOUNDS = 46;
    private static final int Y_HOME_BOUNDS = 7;
    private static final double DEFAULT_SPEED = 0.5D;
    private static final ResourceLocation MOVEMENT_SPEED_ID = TwilightForestMod.prefix("naga_segment_speed_boost");
    private static final ResourceLocation DIFFICULTY_HEALTH_ID = TwilightForestMod.prefix("naga_difficulty_health_boost");
    private static final EntityDataAccessor<Boolean> DATA_DAZE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_STUNLESS = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);

    private int currentSegmentCount;
    private final float healthPerSegment;
    private final NagaSegment[] bodySegments = new NagaSegment[MAX_SEGMENTS];
    private NagaMovementPattern movementAI;
    private int ticksSinceDamaged;
    private int damageDuringCurrentStun;
    public float stunlessRedOverlayProgress;

    public Naga(EntityType<? extends Naga> type, Level level) {
        super(type, level);
        this.xpReward = 217;
        this.noCulling = true;
        for (int i = 0; i < this.bodySegments.length; i++) {
            this.bodySegments[i] = new NagaSegment(this);
        }
        this.healthPerSegment = this.getMaxHealth() / 10.0F;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 80.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
                .add(Attributes.STEP_HEIGHT, 2.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAZE, false);
        builder.define(DATA_CHARGE, false);
        builder.define(DATA_STUNLESS, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new NagaSmashGoal(this));
        this.goalSelector.addGoal(3, this.movementAI = new NagaMovementPattern(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D, 1) {
            @Override
            public boolean canUse() {
                return Naga.this.isMobWithinHomeArea(Naga.this) && Naga.this.getTarget() == null && super.canUse();
            }

            @Override
            protected Vec3 getPosition() {
                return DefaultRandomPos.getPos(this.mob, 30, 7);
            }
        });
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && Naga.this.areSelfAndTargetInHome(Naga.this.getTarget());
            }
        });
        this.moveControl = new NagaMoveControl(this);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data) {
        if (this.level().getDifficulty() != Difficulty.EASY && this.getAttribute(Attributes.MAX_HEALTH) != null) {
            boolean hard = this.level().getDifficulty() == Difficulty.HARD;
            AttributeModifier modifier = new AttributeModifier(DIFFICULTY_HEALTH_ID, hard ? 130.0D : 80.0D, AttributeModifier.Operation.ADD_VALUE);
            if (!Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).hasModifier(DIFFICULTY_HEALTH_ID)) {
                Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
                this.setHealth(this.getMaxHealth());
            }
        }
        this.setSegmentsPerHealth();
        return data;
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            this.clientParticles();
        }
        this.setSegmentsPerHealth();
        super.tick();
        this.moveSegments();
    }

    private void clientParticles() {
        if (this.isStunlessCharging()) {
            this.stunlessRedOverlayProgress = Math.min(0.65F, this.stunlessRedOverlayProgress + 0.01F);
        } else {
            this.stunlessRedOverlayProgress = Math.max(0.0F, this.stunlessRedOverlayProgress - 0.1F);
        }
        if (this.isDazed() && this.deathTime < 10) {
            for (int i = 0; i < 5; i++) {
                Vec3 pos = new Vec3(this.getX(), this.getY() + 2.15D, this.getZ()).add(new Vec3(1.5D, 0.0D, 0.0D).yRot((float) Math.toRadians(this.getRandom().nextInt(360))));
                this.level().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.isStunlessCharging() && this.deathTime <= 0) {
            this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(0.85D), this.blockPosition().getY() + 2.25F, this.getRandomZ(0.85D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getTarget() != null && (this.distanceToSqr(this.getTarget()) > 80.0D * 80.0D || !this.areSelfAndTargetInHome(this.getTarget()))) {
            this.setTarget(null);
        }
        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            this.smashNearbyBlocks();
        }
        GlobalPos home = this.getRestrictionPoint();
        if (this.tickCount % 20 == 0 && home != null && this.level().dimension().equals(home.dimension()) && this.getY() < home.pos().getY() - 5) {
            this.teleportTo(home.pos().getX(), home.pos().getY(), home.pos().getZ());
            this.getNavigation().stop();
        }
        this.advanceClosePathPoints();
        this.ticksSinceDamaged++;
        if (this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
            this.heal(1.0F);
        }
        if (this.damageDuringCurrentStun > 15 && this.movementAI != null) {
            this.movementAI.forceCircle();
            this.damageDuringCurrentStun = 0;
        }
    }

    private void smashNearbyBlocks() {
        AABB bounds = this.getBoundingBox();
        BlockPos min = new BlockPos(Mth.floor(bounds.minX - 0.75D), Mth.floor(bounds.minY + (this.shouldDestroyAllBlocks() ? 1.01F : 0.5F)), Mth.floor(bounds.minZ - 0.75D));
        BlockPos max = new BlockPos(Mth.floor(bounds.maxX + 0.75D), Mth.floor(bounds.maxY + 1.0D), Mth.floor(bounds.maxZ + 0.75D));
        if (this.level().hasChunksAt(min, max)) {
            for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                BlockState state = this.level().getBlockState(pos);
                if (state.is(BlockTags.LEAVES) || (this.shouldDestroyAllBlocks() && EntityUtil.canDestroyBlock(this.level(), pos, this))) {
                    this.level().destroyBlock(pos, !state.is(BlockTags.LEAVES));
                }
            }
        }
    }

    private void advanceClosePathPoints() {
        double distance = this.getBbWidth() * 4.0F;
        Vec3 next = this.isPathFinding() && this.getNavigation().getPath() != null ? this.getNavigation().getPath().getNextEntityPos(this) : null;
        while (next != null && next.distanceToSqr(this.getX(), next.y(), this.getZ()) < distance * distance) {
            this.getNavigation().getPath().advance();
            if (this.getNavigation().getPath().isDone()) {
                next = null;
            } else {
                next = this.getNavigation().getPath().getNextEntityPos(this);
            }
        }
    }

    private void setSegmentsPerHealth() {
        int oldSegments = this.currentSegmentCount;
        int newSegments = Mth.clamp((int) ((this.getHealth() / this.healthPerSegment) + (this.getHealth() > 0.0F ? 2 : 0)), 0, MAX_SEGMENTS);
        this.currentSegmentCount = newSegments;
        if (newSegments < oldSegments) {
            for (int i = newSegments; i < oldSegments; i++) {
                this.bodySegments[i].selfDestruct((oldSegments - i) * 12);
            }
        } else if (newSegments > oldSegments) {
            this.activateBodySegments();
        }
        if (!this.level().isClientSide() && oldSegments != newSegments && this.getAttribute(Attributes.MOVEMENT_SPEED) != null && newSegments > 0) {
            double speedMod = (MAX_SEGMENTS / (double) newSegments) * 0.02D;
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(MOVEMENT_SPEED_ID);
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(new AttributeModifier(MOVEMENT_SPEED_ID, speedMod, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private void activateBodySegments() {
        for (int i = 0; i < this.currentSegmentCount; i++) {
            NagaSegment segment = this.bodySegments[i];
            segment.activate();
            segment.setPos(this.getX() + 0.1D * i, this.getY() + 0.5D, this.getZ() + 0.1D * i);
            segment.setRot(this.getRandom().nextFloat() * 360.0F, 0.0F);
            this.makePoofAt(segment.position());
        }
    }

    public void makePoofAt(Vec3 position) {
        for (int i = 0; i < 20; i++) {
            double xSpeed = this.getRandom().nextGaussian() * 0.02D;
            double ySpeed = this.getRandom().nextGaussian() * 0.02D;
            double zSpeed = this.getRandom().nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.EXPLOSION,
                    position.x() + this.getRandom().nextFloat() * 4.0F - 2.0F - xSpeed * 10.0D,
                    position.y() + this.getRandom().nextFloat() * 2.0F - ySpeed * 10.0D,
                    position.z() + this.getRandom().nextFloat() * 4.0F - 2.0F - zSpeed * 10.0D,
                    xSpeed, ySpeed, zSpeed);
        }
    }

    private void moveSegments() {
        for (int i = 0; i < this.bodySegments.length; i++) {
            NagaSegment segment = this.bodySegments[i];
            segment.tick();
            if (segment.isActive()) {
                if (i == 0) {
                    segment.follow(this, i);
                } else {
                    segment.follow(this.bodySegments[i - 1], i);
                }
            }
        }
    }

    public boolean shouldDestroyAllBlocks() {
        return this.isCharging() || !this.isMobWithinHomeArea(this);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.NAGA_HISS;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.NAGA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.NAGA_HURT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.getEntity() != null && !this.isOtherEntityWithinHomeArea(source.getEntity())
                || source.getDirectEntity() != null && !this.isOtherEntityWithinHomeArea(source.getDirectEntity())
                || source.is(DamageTypeTags.IS_EXPLOSION)
                || super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            this.ticksSinceDamaged = 0;
            if (this.isDazed()) {
                this.damageDuringCurrentStun += amount;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity living && living.isBlocking() && this.movementAI != null) {
            if (this.movementAI.getState() == NagaMovementPattern.MovementState.CHARGE) {
                Vec3 motion = this.getDeltaMovement();
                target.push(motion.x() * 1.5D, 0.5D, motion.z() * 1.5D);
                this.push(motion.x() * -1.25D, 0.5D, motion.z() * -1.25D);
                if (target instanceof ServerPlayer player) {
                    player.getUseItem().hurtAndBreak(5, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                }
                this.hurt(this.damageSources().generic(), 2.0F);
                this.level().playSound(null, target.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
                this.movementAI.doDaze();
                return false;
            } else if (this.movementAI.getState() == NagaMovementPattern.MovementState.STUNLESS_CHARGE) {
                if (target instanceof ServerPlayer player) {
                    player.getUseItem().hurtAndBreak(10, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                    player.getCooldowns().addCooldown(player.getUseItem().getItem(), 200);
                    player.stopUsingItem();
                    this.level().broadcastEntityEvent(player, (byte) 30);
                }
                living.hurt(this.damageSources().mobAttack(this), 4.0F);
                this.playSound(SoundEvents.FOX_BITE, 2.0F, 0.5F);
                this.movementAI.doCircle();
                return false;
            }
        }
        if (!this.isDazed()) {
            boolean result = super.doHurtTarget(target);
            if (result) {
                target.push(-Mth.sin((this.getYRot() * Mth.PI) / 180.0F) * 2.0F, 0.4F, Mth.cos((this.getYRot() * Mth.PI) / 180.0F) * 2.0F);
            }
            return result;
        }
        return false;
    }

    public float getWalkTargetValue(BlockPos pos) {
        return this.isMobWithinHomeArea(this) ? 0.0F : Float.MIN_VALUE;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        for (NagaSegment segment : this.bodySegments) {
            segment.deactivate();
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        this.setDazed(false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Dazed", this.isDazed());
        tag.putBoolean("Charging", this.isCharging());
        tag.putBoolean("StunlessCharging", this.isStunlessCharging());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDazed(tag.getBoolean("Dazed"));
        this.setCharging(tag.getBoolean("Charging"));
        this.setStunlessCharging(tag.getBoolean("StunlessCharging"));
    }

    public boolean isMobWithinHomeArea(Entity entity) {
        GlobalPos home = this.getRestrictionPoint();
        if (home == null || !this.level().dimension().equals(home.dimension())) {
            return true;
        }
        double distX = Math.abs(home.pos().getX() - entity.blockPosition().getX());
        double distY = Math.abs(home.pos().getY() - entity.blockPosition().getY());
        double distZ = Math.abs(home.pos().getZ() - entity.blockPosition().getZ());
        return distX <= XZ_HOME_BOUNDS && distY <= Y_HOME_BOUNDS && distZ <= XZ_HOME_BOUNDS;
    }

    public boolean isOtherEntityWithinHomeArea(Entity entity) {
        return this.isMobWithinHomeArea(entity);
    }

    public boolean areSelfAndTargetInHome(@Nullable Entity entity) {
        return this.isMobWithinHomeArea(this) && (entity == null || this.isOtherEntityWithinHomeArea(entity));
    }

    public boolean isDazed() {
        return this.getEntityData().get(DATA_DAZE);
    }

    public void setDazed(boolean dazed) {
        this.getEntityData().set(DATA_DAZE, dazed);
    }

    public boolean isCharging() {
        return this.getEntityData().get(DATA_CHARGE);
    }

    public void setCharging(boolean charging) {
        this.getEntityData().set(DATA_CHARGE, charging);
        if (!charging) {
            this.setStunlessCharging(false);
        }
    }

    public boolean isStunlessCharging() {
        return this.getEntityData().get(DATA_STUNLESS);
    }

    public void setStunlessCharging(boolean charging) {
        this.getEntityData().set(DATA_STUNLESS, charging);
    }

    public NagaMovementPattern getMovementAI() {
        return this.movementAI;
    }

    public NagaMovementPattern getMovementPattern() {
        return this.movementAI;
    }

    @Override
    public TFPart<?>[] getParts() {
        return this.bodySegments;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        TFPart.assignPartIDs(this);
    }

    @Override
    public int getHomeRadius() {
        return 40;
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.NAGA_COURTYARD;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return random.nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get() : TFBlocks.CANOPY_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.NAGA_BOSS_SPAWNER.get();
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.GREEN;
    }

    @Override
    protected BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.NOTCHED_10;
    }
}
