package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.entity.projectile.ThrownBlock;
import twilightforest.init.TFSounds;

import java.util.List;

public class Troll extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> ROCK_FLAG = SynchedEntityData.defineId(Troll.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockState> ROCK_STATE = SynchedEntityData.defineId(Troll.class, EntityDataSerializers.BLOCK_STATE);
    private static final ResourceLocation ROCK_FOLLOW_RANGE_ID = TwilightForestMod.prefix("troll_rock_follow_range");
    private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier(ROCK_FOLLOW_RANGE_ID, 24.0D, AttributeModifier.Operation.ADD_VALUE);

    private int rockCooldown = 300;
    private BlockState rock = Blocks.AIR.defaultBlockState();

    public Troll(EntityType<? extends Troll> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROCK_FLAG, false);
        builder.define(ROCK_STATE, Blocks.AIR.defaultBlockState());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Troll.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (!this.hasRock() && this.getTarget() != null) {
                if (this.rockCooldown > 0) {
                    --this.rockCooldown;
                } else {
                    this.tryPickUpRock();
                }
            }
            this.syncRockModifier();
        }
    }

    private void tryPickUpRock() {
        BlockPos pos = BlockPos.containing(this.getX() - 2.0D + this.random.nextDouble() * 4.0D, this.getY() + this.random.nextDouble() * 3.0D, this.getZ() - 2.0D + this.random.nextDouble() * 4.0D);
        Vec3 from = new Vec3(this.getBlockX() + 0.5D, pos.getY() + 0.5D, this.getBlockZ() + 0.5D);
        Vec3 to = Vec3.atCenterOf(pos);
        BlockHitResult hit = this.level().clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
        BlockState state = this.level().getBlockState(pos);
        if (hit.getBlockPos().equals(pos) && state.isSolidRender(this.level(), pos)) {
            this.level().removeBlock(pos, false);
            this.level().gameEvent(this, GameEvent.BLOCK_DESTROY, pos);
            this.setRockState(state);
            this.setHasRock(true);
            this.playSound(TFSounds.TROLL_GRABS_ROCK, 1.0F, 1.0F);
        }
    }

    public boolean hasRock() {
        return this.getEntityData().get(ROCK_FLAG);
    }

    public void setHasRock(boolean rock) {
        this.getEntityData().set(ROCK_FLAG, rock);
        if (!rock) {
            this.setRockState(Blocks.AIR.defaultBlockState());
        }
        this.syncRockModifier();
    }

    public BlockState getRockState() {
        return this.getEntityData().get(ROCK_STATE);
    }

    private void setRockState(BlockState state) {
        this.rock = state == null ? Blocks.AIR.defaultBlockState() : state;
        this.getEntityData().set(ROCK_STATE, this.rock);
    }

    private void syncRockModifier() {
        if (this.level().isClientSide() || this.getAttribute(Attributes.FOLLOW_RANGE) == null) {
            return;
        }
        if (this.hasRock()) {
            if (!this.getAttribute(Attributes.FOLLOW_RANGE).hasModifier(ROCK_FOLLOW_RANGE_ID)) {
                this.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(ROCK_MODIFIER);
            }
        } else {
            this.getAttribute(Attributes.FOLLOW_RANGE).removeModifier(ROCK_FOLLOW_RANGE_ID);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        BlockState rockState = this.getRockState();
        if (this.hasRock() && !rockState.isAir()) {
            ThrownBlock block = new ThrownBlock(this.level(), this, rockState);
            double dx = target.getX() - this.getX();
            double dy = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - block.getY();
            double dz = target.getZ() - this.getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            block.shoot(dx, dy + horizontal * 0.2D, dz, 1.6F, Math.max(0.0F, 4.0F - this.level().getDifficulty().getId()));
            this.level().addFreshEntity(block);
            this.playSound(TFSounds.TROLL_THROWS_ROCK, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.gameEvent(GameEvent.PROJECTILE_SHOOT);
            this.setHasRock(false);
            this.rockCooldown = 300;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if (hurt && this.hasRock() && entity instanceof LivingEntity living) {
            this.performRangedAttack(living, 1.0F);
        }
        return hurt;
    }

    public double getPassengersRidingOffset() {
        return 1.75D;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        passenger.setXRot(this.getXRot());
    }

    public Vec3 getHeldRockPosition() {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        double sin = Mth.sin(yaw);
        double cos = Mth.cos(yaw);
        double localX = 0.0D;
        double localZ = -0.62D;
        double x = this.getX() + localX * cos - localZ * sin;
        double z = this.getZ() + localX * sin + localZ * cos;
        return new Vec3(x, this.getY() + 2.35D, z);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime % 5 == 0) {
            for (int i = 0; i < 4; i++) {
                double angle = (this.deathTime + i * 90.0D) * Mth.DEG_TO_RAD;
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER, this.getX() + Math.cos(angle), this.getY() + 0.5D, this.getZ() + Math.sin(angle), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.TROLL_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.TROLL_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.TROLL_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasRock", this.hasRock());
        tag.putInt("RockCooldown", this.rockCooldown);
        BlockState rockState = this.getRockState();
        if (!rockState.isAir()) {
            tag.put("RockState", net.minecraft.nbt.NbtUtils.writeBlockState(rockState));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rockCooldown = tag.getInt("RockCooldown");
        if (tag.contains("RockState")) {
            this.setRockState(net.minecraft.nbt.NbtUtils.readBlockState(this.level().holderLookup(net.minecraft.core.registries.Registries.BLOCK), tag.getCompound("RockState")));
        } else {
            this.setRockState(Blocks.AIR.defaultBlockState());
        }
        this.setHasRock(tag.getBoolean("HasRock") && !this.rock.isAir());
    }
}
