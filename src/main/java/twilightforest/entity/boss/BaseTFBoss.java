package twilightforest.entity.boss;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.loot.TFLootTables;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public abstract class BaseTFBoss extends Monster implements IBossLootBuffer, EnforcedHomePoint {
    private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT =
            SynchedEntityData.defineId(BaseTFBoss.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

    private final ServerBossEvent bossInfo;
    private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(IBossLootBuffer.CONTAINER_SIZE, ItemStack.EMPTY);

    protected BaseTFBoss(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.bossInfo = this.createBossBar();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOME_POINT, Optional.empty());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.shouldShowBossBar(player)) {
            this.getBossBar().addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.getBossBar().removePlayer(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        this.saveHomePointToNbt(tag);
        this.addDeathItemsSaveData(tag, this.registryAccess());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readDeathItemsSaveData(tag, this.registryAccess());
        this.loadHomePointFromNbt(tag);
        if (this.hasCustomName()) {
            this.getBossBar().setName(this.getBossBarTitle());
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.level().isClientSide()) {
            this.tickBossBar();
            if (this instanceof twilightforest.entity.TFPart.Owner) {
                twilightforest.util.multiparts.MultipartEntityUtil.sendDirtyMultipartEntityData(this);
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.getBossBar().setProgress(0.0F);
        if (this.shouldSpawnLoot() && this.level() instanceof ServerLevel server) {
            this.postmortem(server, source);
        }
    }

    @Override
    public void lavaHurt() {
        if (!this.fireImmune()) {
            this.igniteForSeconds(5.0F);
            if (this.hurt(this.damageSources().lava(), 4.0F)) {
                this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.getRandom().nextFloat() * 0.4F);
                EntityUtil.killLavaAround(this);
            }
        }
    }

    protected void postmortem(ServerLevel serverLevel, DamageSource source) {
        IBossLootBuffer.saveDropsIntoBoss(this, TFLootTables.createLootParams(this, true, source).create(LootContextParamSets.ENTITY), serverLevel);
        java.util.Optional.ofNullable(this.getHomeStructure()).ifPresent(structure -> LandmarkUtil.markStructureConquered(serverLevel, this, structure, true));
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.level() instanceof ServerLevel serverLevel) {
            this.postRemoval(serverLevel, reason);
        }
        super.remove(reason);
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            if (this.shouldCreateSpawner() && this.isRestrictionPointValid(this.level().dimension()) && this.getRestrictionPoint() != null && this.level().isLoaded(this.getRestrictionPoint().pos())) {
                this.placeSpawner(this.getRestrictionPoint().pos());
            }
            this.discard();
        } else {
            super.checkDespawn();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected boolean shouldDropLoot() {
        return !TFConfig.bossDropChests;
    }

    @Override
    public @Nullable GlobalPos getRestrictionPoint() {
        return this.getEntityData().get(HOME_POINT).orElse(null);
    }

    @Override
    public void setRestrictionPoint(@Nullable GlobalPos pos) {
        this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
    }

    public ServerBossEvent getBossBar() {
        return this.bossInfo;
    }

    protected void tickBossBar() {
        float maxHealth = this.getMaxHealth();
        this.getBossBar().setProgress(maxHealth <= 0.0F ? 0.0F : this.getHealth() / maxHealth);
    }

    protected boolean shouldShowBossBar(ServerPlayer player) {
        return true;
    }

    protected ServerBossEvent createBossBar() {
        return new ServerBossEvent(this.getBossBarTitle(), this.getBossBarColor(), this.getBossBarOverlay());
    }

    public Component getBossBarTitle() {
        Component displayName = this.getDisplayName();
        return displayName != null ? displayName : this.getTypeName();
    }

    protected abstract BossEvent.BossBarColor getBossBarColor();

    protected BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.PROGRESS;
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.getBossBar().setName(this.getBossBarTitle());
    }

    protected boolean shouldSpawnLoot() {
        return this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT);
    }

    protected boolean shouldCreateSpawner() {
        return true;
    }

    public void placeSpawner(BlockPos pos) {
        Block spawner = this.getBossSpawner();
        if (spawner != null) this.level().setBlockAndUpdate(pos, spawner.defaultBlockState());
    }

    protected void postRemoval(ServerLevel serverLevel, RemovalReason reason) {
        Block deathContainer = this.getDeathContainer(this.getRandom());
        if (reason.equals(RemovalReason.KILLED) && deathContainer != null && this.shouldSpawnLoot()) {
            IBossLootBuffer.depositDropsIntoChest(this, deathContainer.defaultBlockState().setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(this.level().getRandom())), EntityUtil.bossChestLocation(this), serverLevel);
        }
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (!this.isRemoved()) {
            if (!this.level().isClientSide()) {
                if (this.isDeathAnimationFinished()) {
                    this.level().broadcastEntityEvent(this, (byte) 60);
                    this.remove(RemovalReason.KILLED);
                } else {
                    this.tickBossBar();
                }
            } else {
                this.tickDeathAnimation();
            }
        }
    }

    public boolean isDeathAnimationFinished() {
        return this.deathTime >= 20;
    }

    public void tickDeathAnimation() {
    }

    @Nullable
    protected net.minecraft.world.entity.Entity lookAtUponDeath() {
        return null;
    }

    public net.minecraft.core.BlockPos homeOrElseCurrent() {
        return this.getRestrictionPoint() == null ? this.blockPosition() : this.getRestrictionPoint().pos();
    }

    public boolean isOutsideHomeRange(net.minecraft.world.phys.Vec3 pos) {
        if (this.getRestrictionPoint() == null) return false;
        net.minecraft.core.BlockPos point = this.getRestrictionPoint().pos();
        int radius = this.getHomeRadius();
        return point.distToCenterSqr(pos) > (double) (radius * radius);
    }

    public int getHomeRadius() {
        return 20;
    }

    protected void addRestrictionGoals(net.minecraft.world.entity.PathfinderMob mob, net.minecraft.world.entity.ai.goal.GoalSelector selector) {
    }

    @Nullable
    public net.minecraft.resources.ResourceKey<Structure> getHomeStructure() {
        return null;
    }

    @Nullable
    public Block getDeathContainer(RandomSource random) {
        return null;
    }

    @Nullable
    public Block getBossSpawner() {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.dyingInventory;
    }
}
