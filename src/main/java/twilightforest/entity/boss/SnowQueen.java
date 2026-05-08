package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.goal.HoverBeamGoal;
import twilightforest.entity.ai.goal.HoverSummonGoal;
import twilightforest.entity.ai.goal.HoverThenDropGoal;
import twilightforest.entity.monster.IceCrystal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.entities.EntityUtil;

import java.util.List;

public class SnowQueen extends BaseTFBoss implements TFPart.Owner {
    private static final int MAX_SUMMONS = 6;
    private static final int ICE_SHIELD_COUNT = 7;
    private static final EntityDataAccessor<Boolean> BEAM_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> PHASE_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BYTE);
    private static final int MAX_DAMAGE_WHILE_BEAMING = 25;

    private int summonsRemaining;
    private int successfulDrops;
    private int maxDrops;
    private int damageWhileBeaming;
    private int phaseTicks;
    private final SnowQueenIceShield[] iceShields = new SnowQueenIceShield[ICE_SHIELD_COUNT];

    public enum Phase {SUMMON, DROP, BEAM}

    public SnowQueen(EntityType<? extends SnowQueen> type, Level level) {
        super(type, level);
        this.xpReward = 317;
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.setCurrentPhase(Phase.SUMMON);
        for (int index = 0; index < this.iceShields.length; index++) {
            this.iceShields[index] = new SnowQueenIceShield(this);
        }
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.FLYING_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.WHITE;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEAM_FLAG, false);
        builder.define(PHASE_FLAG, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HoverSummonGoal(this));
        this.goalSelector.addGoal(2, new HoverThenDropGoal(this, 80, 20));
        this.goalSelector.addGoal(3, new HoverBeamGoal(this, 80, 100));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            this.spawnParticles();
        }
        this.updateIceShieldDisplays();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getTarget() == null || !this.getTarget().isAlive()) {
            Player nearest = this.level().getNearestPlayer(this, 40.0D);
            if (nearest != null && !nearest.isCreative()) {
                this.setTarget(nearest);
            }
        }
        if (this.getCurrentPhase() == Phase.SUMMON && this.getSummonsRemaining() == 0 && this.countMyMinions() <= 0) {
            this.setCurrentPhase(Phase.DROP);
        }
        if (this.getCurrentPhase() == Phase.DROP && this.successfulDrops >= this.maxDrops) {
            this.setCurrentPhase(Phase.BEAM);
        }
        if (this.getCurrentPhase() == Phase.BEAM && this.damageWhileBeaming >= MAX_DAMAGE_WHILE_BEAMING) {
            this.setCurrentPhase(Phase.SUMMON);
        }
    }

    private void spawnParticles() {
        for (int particle = 0; particle < 3; particle++) {
            this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getRandomX(0.6D), this.getY() + this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F, this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }
        if (this.isBreathing()) {
            Vec3 look = this.getLookAngle();
            this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX() + look.x() * 0.5D, this.getY() + 1.7D, this.getZ() + look.z() * 0.5D, look.x() * 2.0D, look.y() * 2.0D, look.z() * 2.0D);
        }
    }

    public void summonMinionAt(LivingEntity target) {
        IceCrystal minion = TFEntities.ICE_CRYSTAL.get().create(this.level());
        if (minion == null) {
            return;
        }
        minion.moveTo(target.getX() + this.getRandom().nextGaussian() * 6.0D, target.getY() + 1.0D, target.getZ() + this.getRandom().nextGaussian() * 6.0D, 0.0F, 0.0F);
        minion.setTarget(target);
        minion.setToDieIn30Seconds();
        this.level().addFreshEntity(minion);
        this.summonsRemaining--;
    }

    public int getSummonsRemaining() {
        return this.summonsRemaining;
    }

    public void setSummonsRemaining(int summonsRemaining) {
        this.summonsRemaining = summonsRemaining;
    }

    public int countMyMinions() {
        return this.level().getEntitiesOfClass(IceCrystal.class, this.getBoundingBox().inflate(32.0D, 16.0D, 32.0D)).size();
    }

    public void incrementSuccessfulDrops() {
        this.successfulDrops++;
        this.doDropImpact();
    }

    public void destroyBlocksInAABB(AABB box) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = this.level().getBlockState(pos);
            if (state.is(BlockTags.ICE) && EntityUtil.canDestroyBlock(this.level(), pos, this)) {
                this.level().destroyBlock(pos, false);
            }
        }
    }

    private void doDropImpact() {
        AABB area = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, area, entity -> entity != this)) {
            if (entity.hurt(this.damageSources().mobAttack(this), 10.0F)) {
                entity.push(0.0D, 0.6D, 0.0D);
            }
        }
        this.playSound(TFSounds.SNOW_QUEEN_ATTACK, 1.0F, 1.0F);
    }

    private void updateIceShieldDisplays() {
        if (!this.isAlive()) {
            this.removeIceShieldDisplays();
            return;
        }
        for (int index = 0; index < this.iceShields.length; index++) {
            SnowQueenIceShield shield = this.iceShields[index];
            shield.tick();
            shield.activate();
            Vec3 position = index < this.iceShields.length - 1 ? this.getIceShieldPosition(index) : new Vec3(this.getX(), this.getY() - 1.0D, this.getZ());
            float yRot = index < this.iceShields.length - 1 ? this.getIceShieldAngle(index) : this.yBodyRot;
            shield.setPos(position.x(), position.y(), position.z());
            shield.setYRot(yRot);
            shield.yRotO = yRot;
            shield.renderYawOffset = yRot;
            if (!this.level().isClientSide()) {
                this.applyIceShieldCollisions(position);
            }
        }
    }

    private Vec3 getIceShieldPosition(int index) {
        return this.getIceShieldPosition(this.getIceShieldAngle(index), 1.0F);
    }

    private float getIceShieldAngle(int index) {
        return 60.0F * index + this.tickCount * 5.0F;
    }

    private Vec3 getIceShieldPosition(float angle, float distance) {
        float radians = angle * Mth.DEG_TO_RAD;
        double x = this.getX() + Mth.cos(radians) * distance;
        double z = this.getZ() + Mth.sin(radians) * distance;
        return new Vec3(x, this.getY() + 0.1D, z);
    }

    private void applyIceShieldCollisions(Vec3 position) {
        AABB shieldBox = new AABB(position.x() - 0.375D, position.y() - 0.375D, position.z() - 0.375D, position.x() + 0.375D, position.y() + 0.375D, position.z() + 0.375D);
        for (Projectile projectile : this.level().getEntitiesOfClass(Projectile.class, shieldBox, projectile -> projectile.isAlive() && projectile.getOwner() != this)) {
            this.handleIceShieldProjectile(projectile);
        }
        for (Entity entity : this.level().getEntities(this, shieldBox, entity -> entity.isPushable() && entity != this)) {
            entity.push(this);
            if (entity instanceof LivingEntity && this.doHurtTarget(entity)) {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x(), motion.y() + 0.4D, motion.z());
                this.playSound(TFSounds.SNOW_QUEEN_ATTACK, 1.0F, 1.0F);
            }
        }
    }

    private void handleIceShieldProjectile(Projectile projectile) {
        if (projectile instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
            return;
        }
        this.playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        projectile.discard();
    }

    private void removeIceShieldDisplays() {
        for (SnowQueenIceShield shield : this.iceShields) {
            shield.deactivate();
        }
    }

    public boolean isBreathing() {
        return this.getEntityData().get(BEAM_FLAG);
    }

    public void setBreathing(boolean breathing) {
        this.getEntityData().set(BEAM_FLAG, breathing);
    }

    public Phase getCurrentPhase() {
        return Phase.values()[this.getEntityData().get(PHASE_FLAG)];
    }

    public void setCurrentPhase(Phase phase) {
        this.getEntityData().set(PHASE_FLAG, (byte) phase.ordinal());
        this.phaseTicks = 0;
        this.setBreathing(false);
        if (phase == Phase.SUMMON) {
            this.setSummonsRemaining(MAX_SUMMONS);
        } else if (phase == Phase.DROP) {
            this.successfulDrops = 0;
            this.maxDrops = 2 + this.getRandom().nextInt(3);
        } else if (phase == Phase.BEAM) {
            this.damageWhileBeaming = 0;
        }
    }

    public void doBreathAttack(Entity target) {
        if (target.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.CHILLING_BREATH, this), 4.0F) && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        DamageSource source = this.getCurrentPhase() == Phase.DROP ? TFDamageTypes.entitySource(this.level(), TFDamageTypes.SQUISH, this) : this.damageSources().mobAttack(this);
        return entity.hurt(source, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && this.getCurrentPhase() == Phase.BEAM) {
            this.damageWhileBeaming += amount;
        }
        return hurt;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SNOW_QUEEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SNOW_QUEEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SNOW_QUEEN_DEATH;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        this.removeIceShieldDisplays();
    }

    @Override
    public void remove(RemovalReason reason) {
        this.removeIceShieldDisplays();
        super.remove(reason);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Phase", (byte) this.getCurrentPhase().ordinal());
        tag.putInt("SummonsRemaining", this.summonsRemaining);
        tag.putInt("SuccessfulDrops", this.successfulDrops);
        tag.putInt("MaxDrops", this.maxDrops);
        tag.putInt("DamageWhileBeaming", this.damageWhileBeaming);
        tag.putInt("PhaseTicks", this.phaseTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(PHASE_FLAG, tag.getByte("Phase"));
        this.summonsRemaining = tag.getInt("SummonsRemaining");
        this.successfulDrops = tag.getInt("SuccessfulDrops");
        this.maxDrops = tag.getInt("MaxDrops");
        this.damageWhileBeaming = tag.getInt("DamageWhileBeaming");
        this.phaseTicks = tag.getInt("PhaseTicks");
    }

    @Override
    public TFPart<?>[] getParts() {
        return this.iceShields;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        TFPart.assignPartIDs(this);
    }

    @Override
    public int getHomeRadius() {
        return 30;
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.AURORA_PALACE;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.TWILIGHT_OAK_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get();
    }
}
