package twilightforest.entity.boss;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.DamageTypeTagGenerator;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.entity.ai.goal.AlwaysWatchTargetGoal;
import twilightforest.entity.ai.goal.AttemptToGoHomeGoal;
import twilightforest.entity.ai.goal.LichAbsorbMinionsGoal;
import twilightforest.entity.ai.goal.LichMinionsGoal;
import twilightforest.entity.ai.goal.LichPopMobsGoal;
import twilightforest.entity.ai.goal.LichShadowsGoal;
import twilightforest.entity.ai.goal.RandomLookAroundIfBoredGoal;
import twilightforest.entity.monster.LichMinion;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.TFAttributes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.entities.EntityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Lich extends BaseTFBoss implements RangedAttackMob {
    public static final int MAX_SHADOW_CLONES = 2;
    public static final int INITIAL_SHIELD_STRENGTH = 6;
    public static final int INITIAL_MINIONS_TO_SUMMON = 9;
    public static final int MAX_ACTIVE_MINIONS = 3;
    public static final int MAX_HEALTH = 100;
    public static final int PARTICLE_BURST_COOLDOWN = 23;
    public static final int DEATH_ANIMATION_POINT_A = PARTICLE_BURST_COOLDOWN * 5;
    public static final int DEATH_ANIMATION_POINT_B = DEATH_ANIMATION_POINT_A + 16;
    public static final int DEATH_ANIMATION_POINT_C = DEATH_ANIMATION_POINT_B + 32;
    public static final int DEATH_ANIMATION_DURATION = DEATH_ANIMATION_POINT_C + 132;
    private static final ItemParticleOption BONE_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.BONE.getDefaultInstance());
    private static final EntityDataAccessor<Optional<UUID>> MASTER_LICH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> SHIELD_STRENGTH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MINIONS_LEFT = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TELEPORT_INVISIBILITY = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
    private int attackCooldown;
    private int minionCooldown;
    private int popCooldown;
    private int heldScepterTime;
    private int spawnTime;
    private boolean initializedAttributes;
    private int previousPhase = 1;
    private int babyMinionsSummoned;
    private int hitsWithoutTeleport;
    private final List<UUID> summonedClones = new ArrayList<>();
    @Nullable
    private Lich masterLich;

    public Lich(EntityType<? extends Lich> type, Level level) {
        super(type, level);
        this.xpReward = 217;
    }

    public Lich(Level level, Lich master) {
        this(TFEntities.LICH.get(), level);
        this.setMasterUUID(master.getUUID());
        this.setRestrictionPoint(master.getRestrictionPoint());
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(TFAttributes.CLONE_COUNT, MAX_SHADOW_CLONES)
                .add(TFAttributes.SHIELD_STRENGTH, INITIAL_SHIELD_STRENGTH)
                .add(TFAttributes.MINION_COUNT, INITIAL_MINIONS_TO_SUMMON);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (!this.isShadowClone()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFItems.FORTIFICATION_SCEPTER.get()));
            this.playSound(TFSounds.SHIELD_ADD, 1.5F, this.getVoicePitch());
            this.swing(InteractionHand.MAIN_HAND);
        }
        return data;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MASTER_LICH, Optional.empty());
        builder.define(SHIELD_STRENGTH, INITIAL_SHIELD_STRENGTH);
        builder.define(MINIONS_LEFT, INITIAL_MINIONS_TO_SUMMON);
        builder.define(ATTACK_TYPE, 0);
        builder.define(TELEPORT_INVISIBILITY, 0);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.YELLOW;
    }

    @Override
    protected BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.NOTCHED_6;
    }

    @Override
    protected boolean shouldShowBossBar(ServerPlayer player) {
        return !this.isShadowClone();
    }

    @Override
    protected void tickBossBar() {
        this.getBossBar().setVisible(!this.isShadowClone());
        int phase = this.getPhase();
        this.getBossBar().setProgress(phase == 1 ? this.getShieldStrength() / (float) Math.max(1.0D, this.getAttributeValue(TFAttributes.SHIELD_STRENGTH)) : this.getHealth() / this.getMaxHealth());
        this.getBossBar().setOverlay(phase == 1 ? BossEvent.BossBarOverlay.NOTCHED_6 : BossEvent.BossBarOverlay.PROGRESS);
        this.getBossBar().setColor(phase == 1 ? BossEvent.BossBarColor.YELLOW : phase == 2 ? BossEvent.BossBarColor.PURPLE : BossEvent.BossBarColor.RED);
        this.previousPhase = phase;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new AttemptToGoHomeGoal<>(this, 1.25D) {
            @Override
            public boolean canUse() {
                return super.canUse() && Lich.this.isOutsideHomeRange(Lich.this.position());
            }

            @Override
            public boolean requiresUpdateEveryTick() {
                return true;
            }

            @Override
            public void tick() {
                if (Lich.this.getTeleportInvisibility() > 0) {
                    return;
                }
                if (Lich.this.getNavigation().getPath() == null || Lich.this.getNavigation().isStuck() || !Lich.this.getNavigation().getPath().canReach()) {
                    if (!Lich.this.teleportToNewTarget(Lich.this.getTarget(), 20.0F, null)) {
                        Lich.this.teleportHome();
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new RandomLookAroundIfBoredGoal(this));
        this.goalSelector.addGoal(1, new AlwaysWatchTargetGoal(this));
        this.goalSelector.addGoal(1, new LichPopMobsGoal(this));
        this.goalSelector.addGoal(1, new LichAbsorbMinionsGoal(this));
        this.goalSelector.addGoal(2, new LichShadowsGoal(this, 30.0F));
        this.goalSelector.addGoal(3, new LichMinionsGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 0.75D, true) {
            @Override
            public boolean canUse() {
                return Lich.this.getPhase() == 3 && super.canUse();
            }

            @Override
            public void tick() {
                if (Lich.this.getTeleportInvisibility() > 0) {
                    return;
                }
                super.tick();
                if (this.mob.getTarget() != null && !this.mob.isWithinMeleeAttackRange(this.mob.getTarget()) && this.mob.getNavigation().isDone()) {
                    if (!this.mob.getNavigation().moveTo(this.mob.getTarget(), 0.75D)) {
                        Lich.this.teleportToSightOfEntity(this.mob.getTarget());
                    }
                }
            }

            @Override
            public void start() {
                super.start();
                this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
            }
        });
        this.addRestrictionGoals(this, this.goalSelector);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                if (this.mob instanceof Lich main && this.mob.getLastHurtByMob() instanceof Lich lich && lich.getMaster() == main.getMaster()) {
                    return false;
                }
                return super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    private void initializeAttributesOnce() {
        if (this.initializedAttributes) {
            return;
        }
        this.initializedAttributes = true;
        this.setShieldStrength((int) this.getAttributeValue(TFAttributes.SHIELD_STRENGTH));
        this.setMinionsToSummon((int) this.getAttributeValue(TFAttributes.MINION_COUNT));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getMasterUUID() != null) {
            tag.putUUID("MasterLich", this.getMasterUUID());
        }
        ListTag clonesTag = new ListTag();
        for (UUID uuid : this.summonedClones) {
            clonesTag.add(NbtUtils.createUUID(uuid));
        }
        if (!clonesTag.isEmpty()) {
            tag.put("SummonedClones", clonesTag);
        }
        tag.putInt("ShieldStrength", this.getShieldStrength());
        tag.putInt("MinionsToSummon", this.getMinionsToSummon());
        tag.putInt("BabyMinionsSummoned", this.babyMinionsSummoned);
        tag.putInt("HitsWithoutTeleport", this.hitsWithoutTeleport);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("MasterLich")) {
            this.setMasterUUID(tag.getUUID("MasterLich"));
        }
        if (tag.contains("SummonedClones", Tag.TAG_LIST)) {
            this.summonedClones.clear();
            ListTag cloneList = tag.getList("SummonedClones", Tag.TAG_INT_ARRAY);
            cloneList.forEach(cloneTag -> this.summonedClones.add(NbtUtils.loadUUID(cloneTag)));
        }
        this.setShieldStrength(tag.contains("ShieldStrength") ? tag.getInt("ShieldStrength") : INITIAL_SHIELD_STRENGTH);
        this.setMinionsToSummon(tag.contains("MinionsToSummon") ? tag.getInt("MinionsToSummon") : INITIAL_MINIONS_TO_SUMMON);
        this.babyMinionsSummoned = tag.getInt("BabyMinionsSummoned");
        this.hitsWithoutTeleport = tag.getInt("HitsWithoutTeleport");
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getTeleportInvisibility() > 0) {
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget());
                this.getLookControl().tick();
            }
            this.setTeleportInvisibility(this.getTeleportInvisibility() - 1);
            return;
        }

        if (this.isDeadOrDying()) {
            return;
        }

        if (this.level().isClientSide()) {
            double x = this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth();
            double y = this.getY() + this.getBbHeight() * 0.85D;
            double z = this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth();
            this.level().addParticle(this.getNextAttackType() == 0 ? ParticleTypes.WITCH : ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.initializeAttributesOnce();
        if (this.isOutsideHomeRange(this.position()) && this.getTeleportInvisibility() <= 0) {
            this.teleportHome();
        }
        if (this.attackCooldown > 0 && this.spawnTime <= 0) {
            --this.attackCooldown;
        }
        if (this.minionCooldown > 0) {
            --this.minionCooldown;
        }
        if (this.popCooldown > 0 && this.getHealth() < this.getMaxHealth() && this.getScepterTimeLeft() <= 0) {
            --this.popCooldown;
        }
        if (this.getScepterTimeLeft() == 0 && this.getPopCooldown() < 30 && this.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.LIFEDRAIN_SCEPTER.get())) {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this.getPhase() == 2 ? TFItems.ZOMBIE_SCEPTER.get() : Items.GOLDEN_SWORD));
        }
        if (this.heldScepterTime > 0) {
            --this.heldScepterTime;
        }
        if (this.spawnTime > 0) {
            --this.spawnTime;
        }
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getPopCooldown() {
        return this.popCooldown;
    }

    public void setPopCooldown(int popCooldown) {
        this.popCooldown = Math.max(0, popCooldown);
    }

    public int getScepterTimeLeft() {
        return this.heldScepterTime;
    }

    public void setScepterTime() {
        this.heldScepterTime = 20 + this.getRandom().nextInt(20);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFItems.LIFEDRAIN_SCEPTER.get()));
    }

    public void resetScepterTime() {
        this.heldScepterTime = 0;
    }

    public void setExtinguishTimer() {
        this.spawnTime = 40;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getTeleportInvisibility() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }

        if (source.is(DamageTypes.IN_WALL) && this.getTarget() != null) {
            this.teleportToNewTarget(this.getTarget(), 20.0F, null);
        }

        if (this.isShadowClone() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.playSound(TFSounds.LICH_CLONE_HURT, 1.0F, this.getVoicePitch() * 2.0F);
            return false;
        }

        if (source.getEntity() instanceof Lich) {
            return false;
        }

        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.getShieldStrength() > 0) {
            if (source.is(DamageTypeTagGenerator.BREAKS_LICH_SHIELDS) && amount > 2.0F) {
                int newShieldStrength = this.getShieldStrength() - 1;
                this.setShieldStrength(newShieldStrength);
                float volume = 1.5F;
                if (newShieldStrength < INITIAL_SHIELD_STRENGTH) {
                    volume += 0.25F * (INITIAL_SHIELD_STRENGTH - newShieldStrength);
                }
                if (newShieldStrength == 0) {
                    volume += 0.5F;
                }
                this.playSound(TFSounds.SHIELD_BREAK, volume, this.getVoicePitch() * 1.25F);
            } else {
                this.playSound(TFSounds.SHIELD_BLOCK, 0.75F, this.getVoicePitch() * 1.75F);
                if (source.getEntity() instanceof LivingEntity living) {
                    this.setLastHurtByMob(living);
                }
            }
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
            return false;
        }

        boolean hurt = super.hurt(source, amount);
        if (hurt && this.getRandom().nextInt(this.getPhase() == 3 ? 6 : 3) <= this.hitsWithoutTeleport++ && !this.isDeadOrDying()) {
            this.hitsWithoutTeleport = 0;
            this.teleportToNewTarget(this.getTarget(), 20.0F, null);
        }
        return hurt;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.isShadowClone()) {
            this.despawnClones();
            if (this.getShieldStrength() > 0) {
                this.setShieldStrength(0);
                this.playSound(TFSounds.SHIELD_BREAK, 1.2F, this.getVoicePitch() * 2.0F);
            }
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && !this.isShadowClone()) {
            this.discard();
        } else {
            super.checkDespawn();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isShadowClone() ? null : TFSounds.LICH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.LICH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.deathTime > 1 || this.isShadowClone() ? TFSounds.LICH_DEATH : TFSounds.LICH_HURT;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.attackCooldown > 0) {
            return;
        }
        ThrowableProjectile projectile = this.getNextAttackType() == 0 ? new LichBolt(this.level(), this) : new LichBomb(this.level(), this);
        this.launchProjectileAt(projectile, target);
        this.setNextAttackType(this.random.nextInt(4) == 0 ? 1 : 0);
        this.attackCooldown = 45;
    }

    public void launchProjectileAt(ThrowableProjectile projectile, LivingEntity target) {
        float angle = (this.yBodyRot * Mth.PI) / 180.0F;
        double sourceX = this.getX() + Mth.cos(angle) * 0.65D;
        double sourceY = this.getY() + this.getBbHeight() * 0.82D;
        double sourceZ = this.getZ() + Mth.sin(angle) * 0.65D;
        double deltaX = target.getX() - sourceX;
        double deltaY = target.getBoundingBox().minY + target.getBbHeight() * 0.5D - (this.getY() + this.getBbHeight() * 0.5D);
        double deltaZ = target.getZ() - sourceZ;

        projectile.moveTo(sourceX, sourceY, sourceZ, this.getYRot(), this.getXRot());
        projectile.shoot(deltaX, deltaY, deltaZ, 0.5F, 1.0F);
        this.playSound(TFSounds.LICH_SHOOT, this.getSoundVolume(), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
        this.level().addFreshEntity(projectile);
    }

    public void launchProjectileAt(ThrowableProjectile projectile) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.launchProjectileAt(projectile, target);
        }
    }

    public void addClone(UUID uuid) {
        this.summonedClones.add(uuid);
    }

    public List<Lich> getAllClones() {
        if (this.isShadowClone() || !(this.level() instanceof ServerLevel server)) {
            return List.of();
        }
        List<Lich> clones = new ArrayList<>();
        for (UUID uuid : this.summonedClones) {
            if (server.getEntity(uuid) instanceof Lich clone && clone.getMaster() == this) {
                clones.add(clone);
            }
        }
        return clones;
    }

    public void despawnClones() {
        if (!(this.level() instanceof ServerLevel server)) {
            return;
        }
        for (UUID uuid : this.summonedClones) {
            if (server.getEntity(uuid) instanceof Lich clone && clone.getMaster() == this) {
                server.sendParticles(ParticleTypes.SMOKE, clone.getX(), clone.getY() + clone.getBbHeight() * 0.5D, clone.getZ(),
                        64, clone.getBbWidth(), clone.getBbHeight() * 0.5D, clone.getBbWidth(), 0.0D);
                clone.remove(RemovalReason.DISCARDED);
            }
        }
        this.summonedClones.clear();
    }

    public boolean wantsNewClone(Lich clone) {
        return clone.isShadowClone() && this.countMyClones() < this.getAttributeValue(TFAttributes.CLONE_COUNT);
    }

    public int countMyClones() {
        return this.getAllClones().size();
    }

    public boolean wantsNewMinion() {
        return !this.isShadowClone() && this.getPhase() == 2 && this.getMinionsToSummon() > 0 && this.countMyMinions() < this.getAttributeValue(TFAttributes.MINION_COUNT);
    }

    public int countMyMinions() {
        return this.level().getEntitiesOfClass(LichMinion.class, this.getNearbyBox(), minion -> minion.getMaster() == this).size();
    }

    private void summonMinion() {
        LichMinion minion = new LichMinion(this.level(), this);
        minion.moveTo(this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D, this.getY(), this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D, this.getYRot(), 0.0F);
        minion.setTarget(this.getTarget());
        this.level().addFreshEntity(minion);
        minion.spawnAnim();
        minion.playSound(TFSounds.MINION_SUMMON, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 0.75F);
        this.setMinionsToSummon(this.getMinionsToSummon() - 1);
        this.makeMagicTrail(this.getEyePosition(), minion.getEyePosition(), 0.37F, 0.99F, 0.89F);
    }

    private boolean tryPopMobForHealing() {
        if (this.isShadowClone() || this.getHealth() >= this.getMaxHealth() || this.popCooldown > 0 || !(this.level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getNearbyBox(), entity -> entity != this && entity.getType().is(EntityTagGenerator.LICH_POPPABLES))) {
            if (!this.getSensing().hasLineOfSight(mob)) {
                continue;
            }
            Vec3 mobEye = mob.getEyePosition();
            Vec3 lichEye = this.getEyePosition();
            mob.discard();
            serverLevel.sendParticles(ParticleTypes.POOF, mobEye.x(), mobEye.y(), mobEye.z(), 16, 0.35D, 0.35D, 0.35D, 0.02D);
            this.level().playSound(null, mob.blockPosition(), TFSounds.LICH_POP_MOB, SoundSource.HOSTILE, 3.0F, 0.4F + this.random.nextFloat() * 0.2F);
            this.playSound(TFSounds.LICH_POP_MOB, 3.0F, 0.4F + this.random.nextFloat() * 0.2F);
            this.makeMagicTrail(mobEye, lichEye, 1.0F, 0.5F, 0.5F);
            this.heal(2.0F);
            this.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            this.gameEvent(GameEvent.ENTITY_DIE);
            return true;
        }
        return false;
    }

    public void makeMagicTrail(Vec3 start, Vec3 end, float red, float green, float blue) {
        Vec3 delta = end.subtract(start);
        for (int i = 0; i <= 8; i++) {
            Vec3 pos = start.add(delta.scale(i / 8.0D));
            this.level().addParticle(ParticleTypes.WITCH, pos.x(), pos.y(), pos.z(), red, green, blue);
        }
    }

    private AABB getNearbyBox() {
        return this.getBoundingBox().inflate(32.0D, 16.0D, 32.0D);
    }

    private void teleportNearTarget() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }
        double x = target.getX() + (this.random.nextDouble() - 0.5D) * 8.0D;
        double y = target.getY();
        double z = target.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D;
        if (this.randomTeleport(x, y, z, true)) {
            this.playSound(TFSounds.LICH_TELEPORT, this.getSoundVolume(), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
        }
    }

    public void teleportHome() {
        if (this.getRestrictionPoint() != null) {
            net.minecraft.core.BlockPos pos = this.getRestrictionPoint().pos();
            if (this.level().getBlockState(pos.below(2)).isAir()) {
                this.level().setBlockAndUpdate(pos.below(2), TFBlocks.BOLD_STONE_PILLAR.get().defaultBlockState());
            }
            if (this.level().getBlockState(pos.below()).isAir()) {
                pos = pos.below();
            }
            this.teleportToNoChecks(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        }
    }

    public boolean teleportToNewTarget(@Nullable LivingEntity target, float range, @Nullable LichShadowsGoal lichShadowsGoal) {
        List<Player> possibleTargets = new ArrayList<>();
        for (Player player : this.level().players()) {
            if (player.distanceTo(this) < range && !player.isDeadOrDying()) {
                possibleTargets.add(player);
            }
        }
        if (!possibleTargets.isEmpty()) {
            target = possibleTargets.get(this.getRandom().nextInt(possibleTargets.size()));
        }
        if (target != null) {
            this.setTarget(target);
            if (this.teleportToSightOfEntity(target)) {
                for (Lich clone : this.getAllClones()) {
                    clone.setTarget(target);
                    clone.teleportToSightOfEntity(target);
                }
                if (lichShadowsGoal != null) {
                    lichShadowsGoal.checkAndSpawnClones(target);
                }
                return true;
            }
        }
        return false;
    }

    public boolean teleportToSightOfEntity(@Nullable Entity entity) {
        Vec3 dest = this.findVecInLOSOf(entity);
        if (dest != null) {
            this.teleportToNoChecks(dest.x(), dest.y(), dest.z());
            this.getLookControl().setLookAt(entity, 100.0F, 100.0F);
            this.yBodyRot = this.getYRot();
            return true;
        }
        return false;
    }

    @Nullable
    public Vec3 findVecInLOSOf(@Nullable Entity targetEntity) {
        if (targetEntity == null) {
            return null;
        }
        double originX = this.getX();
        double originY = this.getY();
        double originZ = this.getZ();
        for (int i = 0; i < 100; i++) {
            double x = targetEntity.getX() + this.getRandom().nextGaussian() * 16.0D;
            double y = targetEntity.getY() + 2.0D;
            double z = targetEntity.getZ() + this.getRandom().nextGaussian() * 16.0D;
            boolean destClear = this.randomTeleport(x, y, z, false);
            if (destClear) {
                x = this.getX();
                y = this.getY();
                z = this.getZ();
            }
            boolean canSeeTargetAtDest = this.hasLineOfSight(targetEntity);
            this.teleportTo(originX, originY, originZ);
            Vec3 teleportPos = new Vec3(x, y, z);
            if (i < 85 && y < targetEntity.getY()) {
                continue;
            }
            if (destClear && canSeeTargetAtDest && !this.isOutsideHomeRange(teleportPos) && teleportPos.distanceToSqr(targetEntity.position()) >= 25.0D) {
                return teleportPos;
            }
        }
        return null;
    }

    private void teleportToNoChecks(double x, double y, double z) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(this.isShadowClone() ? ParticleTypes.SMOKE : ParticleTypes.WITCH,
                    this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
                    this.isShadowClone() ? 32 : 64, this.getBbWidth(), this.getBbHeight() * 0.5D, this.getBbWidth(), 0.0D);
        }
        this.teleportTo(x, y, z);
        this.playSound(TFSounds.LICH_TELEPORT, 0.75F, 0.75F);
        this.gameEvent(GameEvent.TELEPORT);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.broadcastEntityEvent(this, (byte) 46);
        }
        this.setTeleportInvisibility(20);
        this.jumping = false;
        this.clearFire();
    }

    public boolean isShadowClone() {
        return this.getEntityData().get(MASTER_LICH).isPresent();
    }

    public void setShadowClone(boolean clone) {
        if (!clone) {
            this.setMasterUUID(null);
        }
    }

    @Nullable
    public UUID getMasterUUID() {
        return this.getEntityData().get(MASTER_LICH).orElse(null);
    }

    @Nullable
    public Lich getMaster() {
        if (this.masterLich != null && this.masterLich.isAlive()) {
            return this.masterLich;
        }
        UUID masterUuid = this.getMasterUUID();
        if (this.level() instanceof ServerLevel server && masterUuid != null && server.getEntity(masterUuid) instanceof Lich lich) {
            this.masterLich = lich;
            return lich;
        }
        return null;
    }

    public void setMasterUUID(@Nullable UUID uuid) {
        this.getEntityData().set(MASTER_LICH, Optional.ofNullable(uuid));
        this.masterLich = null;
        this.getBossBar().setVisible(uuid == null);
    }

    public int getShieldStrength() {
        return this.isShadowClone() ? 0 : this.getEntityData().get(SHIELD_STRENGTH);
    }

    public void setShieldStrength(int strength) {
        this.getEntityData().set(SHIELD_STRENGTH, Math.max(0, strength));
    }

    public int getMinionsToSummon() {
        return this.getEntityData().get(MINIONS_LEFT);
    }

    public void setMinionsToSummon(int count) {
        this.getEntityData().set(MINIONS_LEFT, Math.max(0, count));
    }

    public int getNextAttackType() {
        return this.getEntityData().get(ATTACK_TYPE);
    }

    public void setNextAttackType(int type) {
        this.getEntityData().set(ATTACK_TYPE, type);
    }

    public int getPhase() {
        if (this.isShadowClone() || this.getShieldStrength() > 0) {
            return 1;
        }
        return this.getMinionsToSummon() > 0 || this.countMyMinions() > 0 ? 2 : 3;
    }

    public int getTeleportInvisibility() {
        return this.getEntityData().get(TELEPORT_INVISIBILITY);
    }

    public void setTeleportInvisibility(int teleportInvisibility) {
        this.getEntityData().set(TELEPORT_INVISIBILITY, Math.max(0, teleportInvisibility));
    }

    public int getBabyMinionsSummoned() {
        return this.babyMinionsSummoned;
    }

    public void setBabyMinionsSummoned(int babyMinionsSummoned) {
        this.babyMinionsSummoned = babyMinionsSummoned;
    }

    @Override
    public int getHomeRadius() {
        return 30;
    }

    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.LICH_TOWER;
    }

    public Block getDeathContainer(RandomSource random) {
        return this.getRandom().nextBoolean() ? TFBlocks.CANOPY_CHEST.get() : TFBlocks.TWILIGHT_OAK_CHEST.get();
    }

    public Block getBossSpawner() {
        return TFBlocks.LICH_BOSS_SPAWNER.get();
    }

    @Override
    protected boolean shouldCreateSpawner() {
        return !this.isShadowClone();
    }

    @Override
    protected boolean shouldSpawnLoot() {
        return !this.isShadowClone() && super.shouldSpawnLoot();
    }

    @Override
    public boolean isDeathAnimationFinished() {
        return this.isShadowClone() || this.deathTime >= DEATH_ANIMATION_DURATION;
    }

    @Override
    public void tickDeathAnimation() {
        if (this.isShadowClone()) {
            return;
        }

        if (this.deathTime <= DEATH_ANIMATION_POINT_A) {
            boolean done = this.deathTime == DEATH_ANIMATION_POINT_A;
            boolean burst = this.deathTime % PARTICLE_BURST_COOLDOWN == 0;
            if (done) {
                SoundEvent sound = this.getDeathSound();
                if (sound != null) {
                    this.level().playLocalSound(this, sound, SoundSource.HOSTILE, this.getSoundVolume(), this.getVoicePitch());
                }
            } else if (burst) {
                SoundEvent sound = this.getHurtSound(this.damageSources().generic());
                if (sound != null) {
                    this.level().playLocalSound(this, sound, SoundSource.HOSTILE, this.getSoundVolume(), this.getVoicePitch());
                }
            }

            Vec3 pos = this.position();
            for (int i = 0; i < (burst ? 12 : 3); i++) {
                double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                double y = this.getRandom().nextDouble() * this.getBbHeight();
                double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                this.level().addParticle(this.getRandom().nextBoolean() || burst ? BONE_PARTICLE : ParticleTypes.SMOKE, pos.x() + x, pos.y() + y, pos.z() + z, 0.0D, 0.0D, 0.0D);
            }

            if (burst) {
                double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                double y = this.getRandom().nextDouble() * this.getBbHeight();
                double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                for (int i = 0; i < 7; i++) {
                    double x1 = x + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
                    double y1 = y + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
                    double z1 = z + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
                    this.level().addParticle(this.getRandom().nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, pos.x() + x1, pos.y() + y1, pos.z() + z1, 0.0D, 0.0D, 0.0D);
                }

                Vec3 center = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
                for (int i = 0; i < (done ? 18 : 6); i++) {
                    double x1 = this.getX(this.random.nextDouble() * this.random.nextDouble() * (this.random.nextBoolean() ? 1.0D : -1.0D));
                    double y1 = this.getY(this.random.nextDouble());
                    double z1 = this.getZ(this.random.nextDouble() * this.random.nextDouble() * (this.random.nextBoolean() ? 1.0D : -1.0D));
                    this.level().addParticle(ParticleTypes.SMOKE, x1, y1, z1, (x1 - center.x()) * 0.0125D, (y1 - center.y()) * 0.0125D, (z1 - center.z()) * 0.0125D);
                }
            }

            if (done) {
                for (int i = 0; i < 32; i++) {
                    double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                    double y = this.getRandom().nextDouble() * this.getBbHeight();
                    double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
                    this.level().addParticle(this.getRandom().nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, pos.x() + x, pos.y() + y, pos.z() + z, 0.0D, 0.0D, 0.0D);
                }
            }
        } else if (this.deathTime == DEATH_ANIMATION_POINT_B) {
            Vec3 pos = this.position();
            for (int i = 0; i < 3; i++) {
                double x = (this.getRandom().nextDouble() - 0.5D) * 0.75D;
                double z = (this.getRandom().nextDouble() - 0.5D) * 0.75D;
                this.level().addParticle(ParticleTypes.CLOUD, pos.x() + x, pos.y(), pos.z() + z, 0.0D, 0.0D, 0.0D);
            }
        } else if (this.deathTime > DEATH_ANIMATION_POINT_C) {
            Vec3 start = this.position().add(0.0D, 0.45F, 0.0D);
            Vec3 end = Vec3.atCenterOf(EntityUtil.bossChestLocation(this));
            int localDeathTime = this.deathTime - DEATH_ANIMATION_POINT_C;
            double factor = localDeathTime / (double) (DEATH_ANIMATION_DURATION - DEATH_ANIMATION_POINT_C);
            double spiral = factor * factor * 2.0D;
            double expand = (Math.cos((factor + 0.5D) * Math.PI * 2.0D) + 1.0D) * 0.5D;
            Vec3 particlePos = start.add(end.subtract(start).scale(Math.min(factor * 2.0D, 1.0D)));
            for (double offset = 0.0D; offset < 1.0D; offset += 0.2D) {
                double x = Math.sin((spiral + offset) * Math.PI * 2.0D) * expand * 1.75D;
                double z = Math.cos((spiral + offset) * Math.PI * 2.0D) * expand * 1.75D;
                this.level().addParticle(TFParticleType.OMINOUS_FLAME, particlePos.x() + x, particlePos.y() - 0.25D, particlePos.z() + z, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.deathTime > DEATH_ANIMATION_POINT_B && this.random.nextFloat() <= 0.33F) {
            Vec3 start = this.position().add(0.0D, 0.15F, 0.0D);
            double x = (this.getRandom().nextDouble() - 0.5D) * 0.25D;
            double y = this.getRandom().nextDouble() * this.getBbHeight() * 0.1D;
            double z = (this.getRandom().nextDouble() - 0.5D) * 0.25D;
            this.level().addParticle(ParticleTypes.SMOKE, start.x() + x, start.y() + y, start.z() + z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime >= DEATH_ANIMATION_POINT_A) {
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        } else if (this.lookAtUponDeath() instanceof LivingEntity living) {
            double deltaX = living.getX() - this.getX();
            double deltaZ = living.getZ() - this.getZ();
            float targetYaw = (float) (Mth.atan2(deltaZ, deltaX) * Mth.RAD_TO_DEG) - 90.0F;
            this.setYHeadRot(this.limitedRotLerp(this.getYHeadRot(), targetYaw));
            this.setXRot(0.0F);
        }
    }

    private float limitedRotLerp(float angle, float targetAngle) {
        float diff = Mth.wrapDegrees(targetAngle - angle);
        if (diff > 30.0F) {
            diff = 30.0F;
        }
        if (diff < -30.0F) {
            diff = -30.0F;
        }
        return angle + diff;
    }

    @Nullable
    @Override
    protected Entity lookAtUponDeath() {
        if (this.getTarget() != null) {
            return this.getTarget();
        }
        if (this.getLastHurtByMob() != null) {
            return this.getLastHurtByMob();
        }
        return this.level().getNearestPlayer(this, 20.0D);
    }

}
