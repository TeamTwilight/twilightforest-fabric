package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ai.goal.UrGhastAttackGoal;
import twilightforest.entity.ai.goal.UrGhastFlightGoal;
import twilightforest.entity.ai.goal.UrGhastLookGoal;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrGhast extends BaseTFBoss {
    public static final int DEATH_ANIMATION_DURATION = 90;
    private static final EntityDataAccessor<Boolean> DATA_TANTRUM = SynchedEntityData.defineId(UrGhast.class, EntityDataSerializers.BOOLEAN);
    private static final Vec3 DYING_DESCENT = new Vec3(0.0D, -0.03D, 0.0D);

    private float damageUntilNextPhase = 10.0F;
    private int nextTantrumCry;
    private int tripleFireballCooldown;
    private int prevTripleFireballCooldown;
    private int inTrapCounter;
    private boolean charging;
    private UrGhastAttackGoal attackGoal;
    private final List<BlockPos> trapLocations = new ArrayList<>();
    public UrGhast(EntityType<? extends UrGhast> type, Level level) {
        super(type, level);
        this.xpReward = 317;
        this.noPhysics = true;
        this.noCulling = true;
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.08D)
                .add(Attributes.FLYING_SPEED, 0.08D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TANTRUM, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new UrGhastFlightGoal(this));
        this.goalSelector.addGoal(7, new UrGhastLookGoal(this));
        this.goalSelector.addGoal(7, this.attackGoal = new UrGhastAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        this.clearFire();
        super.aiStep();
        if (this.level().isClientSide()) {
            this.clientTantrumParticles();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.prevTripleFireballCooldown = this.tripleFireballCooldown;
        if (this.tripleFireballCooldown > 0) {
            --this.tripleFireballCooldown;
        }
        if (this.inTrapCounter > 0) {
            --this.inTrapCounter;
            this.setTarget(null);
        }
        this.absorbNearbyMinions();
        this.updateTrapLocations();
        LivingEntity target = this.getTarget();
        if (this.isInTantrum()) {
            this.setTarget(null);
            if (--this.nextTantrumCry <= 0) {
                this.playSound(TFSounds.UR_GHAST_TANTRUM, this.getSoundVolume(), this.getVoicePitch());
                this.nextTantrumCry = 20 + this.getRandom().nextInt(30);
            }
            if (this.tickCount % 10 == 0) {
                this.doTantrumDamageEffects();
            }
            return;
        }
        if (target == null || !target.isAlive()) {
            Player nearest = this.level().getNearestPlayer(this, 96.0D);
            if (nearest != null && !nearest.isCreative()) {
                this.setTarget(nearest);
            }
            return;
        }
    }

    private void updateFlightTarget(@Nullable LivingEntity target) {
        if (this.inTrapCounter > 0 || this.tickCount % 40 != 0) {
            return;
        }
        double x;
        double y;
        double z;
        if (target != null) {
            x = target.getX() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 18.0D;
            y = target.getY() + 6.0D + this.getRandom().nextDouble() * 8.0D;
            z = target.getZ() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 18.0D;
        } else {
            BlockPos center = this.getLogicalScanPoint();
            x = center.getX() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 32.0D;
            y = center.getY() + 8.0D + this.getRandom().nextDouble() * 18.0D;
            z = center.getZ() + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 32.0D;
        }
        this.moveControl.setWantedPosition(x, y, z, 0.7D);
    }

    private void clientTantrumParticles() {
        if (this.isInTantrum() && !this.isDeadOrDying()) {
            this.level().addParticle(ParticleTypes.DRIPPING_WATER,
                    this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
                    this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 0.5D,
                    this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
                    0.0D, 0.0D, 0.0D);
        }
    }

    private void doTantrumDamageEffects() {
        AABB below = this.getBoundingBox().move(0.0D, -16.0D, 0.0D).inflate(0.0D, 16.0D, 0.0D);
        for (Player player : this.level().getEntitiesOfClass(Player.class, below)) {
            if (this.level().canSeeSkyFromBelowWater(player.blockPosition())) {
                player.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.GHAST_TEAR, this), 3.0F);
            }
        }
        for (CarminiteGhastling ghast : this.level().getEntitiesOfClass(CarminiteGhastling.class, below)) {
            ghast.push(0.0D, 1.0D, 0.0D);
        }
    }

    public void spitFireball(LivingEntity target) {
        double offsetX = target.getX() - this.getX();
        double offsetY = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (this.getY() + this.getBbHeight() / 2.0F);
        double offsetZ = target.getZ() - this.getZ();
        Vec3 look = this.getViewVector(1.0F);
        for (int i = 0; i < 3; i++) {
            double spreadX = i == 0 ? 0.0D : (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8.0D;
            double spreadZ = i == 0 ? 0.0D : (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8.0D;
            UrGhastFireball fireball = new UrGhastFireball(this.level(), this, offsetX + spreadX, offsetY, offsetZ + spreadZ, 1);
            double shotSpawnDistance = 8.5D;
            fireball.setPos(this.getX() + look.x() * shotSpawnDistance, this.getY() + this.getBbHeight() / 2.0F + look.y() * shotSpawnDistance, this.getZ() + look.z() * shotSpawnDistance);
            this.level().addFreshEntity(fireball);
        }
        this.playSound(TFSounds.UR_GHAST_SHOOT, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.IN_WALL)) {
            return false;
        }
        if (this.isInTantrum()) {
            amount /= 10.0F;
        }
        float oldHealth = this.getHealth();
        boolean hurt = super.hurt(source, amount);
        float actualDamage = oldHealth - this.getHealth();
        if (hurt && !this.level().isClientSide() && this.hurtTime == this.hurtDuration && !this.isDeadOrDying()) {
            this.damageUntilNextPhase -= actualDamage;
            if (this.damageUntilNextPhase <= 0.0F && this.inTrapCounter <= 0) {
                this.switchPhase();
            }
        }
        return hurt;
    }

    private void switchPhase() {
        if (this.isInTantrum()) {
            this.setInTantrum(false);
        } else {
            this.startTantrum();
        }
        this.damageUntilNextPhase = 18.0F;
    }

    private void startTantrum() {
        this.setInTantrum(true);
        this.nextTantrumCry = 1;
        if (this.level() instanceof ServerLevel serverLevel) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (lightning != null) {
                lightning.moveTo(this.position().add(0.0D, this.getBbHeight() * 0.5F, 0.0D));
                lightning.setVisualOnly(true);
                serverLevel.addFreshEntity(lightning);
            }
            this.spawnGhastsAtTraps(serverLevel);
        }
    }

    public void setInTrap() {
        this.inTrapCounter = 20;
        this.setCharging(false);
    }

    public boolean isCharging() {
        return this.charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public boolean checkGhastsAtTraps() {
        this.updateTrapLocations();
        if (this.trapLocations.isEmpty()) {
            return false;
        }
        for (BlockPos trap : this.trapLocations) {
            AABB box = new AABB(trap).inflate(8.0D, 16.0D, 8.0D);
            if (!this.level().getEntitiesOfClass(CarminiteGhastling.class, box, CarminiteGhastling::isAlive).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void resetDamageUntilNextPhase() {
        this.damageUntilNextPhase = 18.0F;
    }

    public List<BlockPos> getTrapLocations() {
        return this.trapLocations;
    }

    private void updateTrapLocations() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (this.tickCount % 60 == 0 && !this.trapLocations.isEmpty()) {
            this.trapLocations.removeIf(pos -> !this.level().getBlockState(pos).is(TFBlocks.GHAST_TRAP.get()) || !this.level().canSeeSky(pos.above()));
        }
        if (this.firstTick || this.tickCount % 100 == 0) {
            int scanRadius = 64;
            BlockPos center = this.getLogicalScanPoint();
            BlockPos.betweenClosed(center.offset(-scanRadius, -16, -scanRadius), center.offset(scanRadius, 16, scanRadius)).forEach(pos -> {
                BlockPos immutable = pos.immutable();
                if (this.trapLocations.size() < 16 && !this.trapLocations.contains(immutable) && serverLevel.getBlockState(immutable).is(TFBlocks.GHAST_TRAP.get()) && serverLevel.canSeeSky(immutable.above())) {
                    this.trapLocations.add(immutable);
                }
            });
        }
    }

    public BlockPos getLogicalScanPoint() {
        GlobalPos homePoint = this.getRestrictionPoint();
        return homePoint != null && homePoint.dimension().equals(this.level().dimension()) ? homePoint.pos() : this.blockPosition();
    }

    public void spawnGhastsAtTraps(ServerLevel level) {
        List<BlockPos> ghastSpawns = new ArrayList<>(this.trapLocations);
        Collections.shuffle(ghastSpawns);
        int numSpawns = Math.min(2, ghastSpawns.size());
        for (int i = 0; i < numSpawns; i++) {
            BlockPos spawn = ghastSpawns.get(i);
            this.spawnMinionGhastsAt(level, spawn.getX(), spawn.getY(), spawn.getZ());
        }
    }

    private void spawnMinionGhastsAt(ServerLevel level, int x, int y, int z) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.setPos(x, y + 4.0D, z);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
        int spawns = 0;
        for (int i = 0; i < 24 && spawns < 6; i++) {
            CarminiteGhastling minion = TFEntities.CARMINITE_GHASTLING.get().create(level);
            if (minion == null) {
                continue;
            }
            double spawnX = x + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 4.0D;
            double spawnY = y + this.getRandom().nextDouble() * 8.0D;
            double spawnZ = z + (this.getRandom().nextDouble() - this.getRandom().nextDouble()) * 4.0D;
            minion.moveTo(spawnX, spawnY, spawnZ, level.getRandom().nextFloat() * 360.0F, 0.0F);
            minion.makeBossMinion();
            if (CarminiteGhastling.canSpawnHere(TFEntities.CARMINITE_GHASTLING.get(), level, MobSpawnType.MOB_SUMMONED, minion.blockPosition(), this.getRandom())) {
                level.addFreshEntity(minion);
                minion.spawnAnim();
                ++spawns;
            }
        }
    }

    private void absorbNearbyMinions() {
        if (this.level().isClientSide()) {
            return;
        }
        for (CarminiteGhastling ghast : this.level().getEntitiesOfClass(CarminiteGhastling.class, this.getBoundingBox().inflate(1.0D))) {
            ghast.spawnAnim();
            ghast.discard();
            this.heal(2.0F);
        }
    }

    public boolean isInTantrum() {
        return this.getEntityData().get(DATA_TANTRUM);
    }

    public int getAttackStatus() {
        if (this.attackGoal == null || this.getTarget() == null || this.isInTantrum()) {
            return 0;
        }
        return this.attackGoal.attackTimer > 10 ? 2 : 1;
    }

    public int getAttackTimer() {
        return this.attackGoal != null ? this.attackGoal.attackTimer : Math.max(0, 90 - this.tripleFireballCooldown);
    }

    public int getPrevAttackTimer() {
        return this.attackGoal != null ? this.attackGoal.prevAttackTimer : Math.max(0, 90 - this.prevTripleFireballCooldown);
    }

    public void setInTantrum(boolean tantrum) {
        this.getEntityData().set(DATA_TANTRUM, tantrum);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || source.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(source);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
    }

    @Override
    public Vec3 getDeltaMovement() {
        return this.isDeadOrDying() ? DYING_DESCENT : super.getDeltaMovement();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.UR_GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.UR_GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.UR_GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 16.0F;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("inTantrum", this.isInTantrum());
        tag.putFloat("damageUntilNextPhase", this.damageUntilNextPhase);
        tag.putInt("tripleFireballCooldown", this.tripleFireballCooldown);
        tag.putBoolean("Charging", this.isCharging());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setInTantrum(tag.getBoolean("inTantrum"));
        this.damageUntilNextPhase = tag.getFloat("damageUntilNextPhase");
        this.tripleFireballCooldown = tag.getInt("tripleFireballCooldown");
        this.setCharging(tag.getBoolean("Charging"));
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.DARK_TOWER;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.DARK_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.UR_GHAST_BOSS_SPAWNER.get();
    }
}
