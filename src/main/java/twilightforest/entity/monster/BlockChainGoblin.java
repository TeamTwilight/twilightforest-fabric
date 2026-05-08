package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.SpikeBlock;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.goal.ThrowSpikeBlockGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;

import java.util.EnumSet;
import java.util.List;

public class BlockChainGoblin extends Monster implements TFPart.Owner {
    private static final float CHAIN_SPEED = 16.0F;
    private static final double SPIKE_SIZE = 0.75D;

    private static final EntityDataAccessor<Byte> DATA_CHAIN_LENGTH = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_CHAIN_POS = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_THROWING = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BOOLEAN);

    private int recoilCounter;
    private float chainAngle;
    private float chainMoveLength;
    private Vec3 spikePosition = Vec3.ZERO;
    private Vec3 chainPart1 = Vec3.ZERO;
    private Vec3 chainPart2 = Vec3.ZERO;
    private Vec3 chainPart3 = Vec3.ZERO;
    public final SpikeBlock block;
    private final MultipartGenericsAreDumb[] partsArray;

    public BlockChainGoblin(EntityType<? extends BlockChainGoblin> type, Level level) {
        super(type, level);
        this.block = new SpikeBlock(this);
        this.partsArray = new MultipartGenericsAreDumb[]{this.block};
    }

    public static abstract class MultipartGenericsAreDumb extends TFPart<Entity> {
        public MultipartGenericsAreDumb(Entity parent) {
            super(parent);
        }
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 11.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHAIN_LENGTH, (byte) 0);
        builder.define(DATA_CHAIN_POS, (byte) 0);
        builder.define(IS_THROWING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidTntGoal(this));
        this.goalSelector.addGoal(4, new ThrowSpikeBlockGoal(this, this.block));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        this.block.tick();
        if (this.recoilCounter > 0) {
            --this.recoilCounter;
        }
        this.chainAngle = this.level().isClientSide() ? this.getSyncedChainAngle() : (this.chainAngle + CHAIN_SPEED) % 360.0F;
        this.updateChainPositions();
        this.block.setPos(this.spikePosition.x(), this.spikePosition.y(), this.spikePosition.z());
        this.block.setYRot(this.level().isClientSide() ? this.getSyncedChainAngle() : this.chainAngle);
        this.chainMove();

        if (!this.level().isClientSide()) {
            this.getEntityData().set(DATA_CHAIN_LENGTH, (byte) Math.floor(this.getChainLength() * 127.0F));
            this.getEntityData().set(DATA_CHAIN_POS, (byte) Math.floor(this.chainAngle / 360.0F * 255.0F));
            twilightforest.util.multiparts.MultipartEntityUtil.sendDirtyMultipartEntityData(this);
            if (this.isAlive() && (this.isThrowing() || this.isSwingingChain())) {
                this.applyBlockCollisions();
            }
        }
    }

    private void applyChainHit(LivingEntity target) {
        if (target.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.SPIKED, this, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            double distance = Math.max(0.1D, Math.sqrt(dx * dx + dz * dz));
            target.push(dx / distance * 0.7D, 0.4D, dz / distance * 0.7D);
            this.playSound(TFSounds.BLOCK_AND_CHAIN_HIT, 1.0F, 1.0F);
            this.gameEvent(GameEvent.PROJECTILE_LAND);
            this.recoilCounter = 40;
        }
    }

    private void updateChainPositions() {
        if (this.isAlive() && this.chainMoveLength > 0.0F) {
            Vec3 blockPos = this.getThrowPos();
            double startX = this.getX();
            double startY = this.getY() + this.getBbHeight() - 0.1D;
            double startZ = this.getZ();
            double offsetX = startX - blockPos.x();
            double offsetY = startY - blockPos.y() - 0.25D;
            double offsetZ = startZ - blockPos.z();

            if (this.chainMoveLength >= 6.0F) {
                this.setThrowing(false);
            }

            this.chainPart1 = new Vec3(startX - offsetX * 0.25D, startY - offsetY * 0.25D, startZ - offsetZ * 0.25D);
            this.chainPart2 = new Vec3(startX - offsetX * 0.5D, startY - offsetY * 0.5D, startZ - offsetZ * 0.5D);
            this.chainPart3 = new Vec3(startX - offsetX * 0.85D, startY - offsetY * 0.85D, startZ - offsetZ * 0.85D);
            this.spikePosition = new Vec3(startX - offsetX, startY - offsetY, startZ - offsetZ);
        } else {
            Vec3 blockPos = this.getChainPosition();
            double startX = this.getX();
            double startY = this.getY() + this.getBbHeight() - 0.1D;
            double startZ = this.getZ();
            double offsetX = startX - blockPos.x();
            double offsetY = startY - blockPos.y() - SPIKE_SIZE / 3.0D;
            double offsetZ = startZ - blockPos.z();

            this.spikePosition = blockPos;
            this.chainPart1 = new Vec3(startX - offsetX * 0.4D, startY - offsetY * 0.4D, startZ - offsetZ * 0.4D);
            this.chainPart2 = new Vec3(startX - offsetX * 0.5D, startY - offsetY * 0.5D, startZ - offsetZ * 0.5D);
            this.chainPart3 = new Vec3(startX - offsetX * 0.6D, startY - offsetY * 0.6D, startZ - offsetZ * 0.6D);
        }
    }

    private Vec3 getThrowPos() {
        Vec3 view = this.getViewVector(1.0F);
        return new Vec3(this.getX() + view.x() * this.chainMoveLength, this.getY() + this.getEyeHeight(), this.getZ() + view.z() * this.chainMoveLength);
    }

    public double getChainYOffset() {
        return 1.5D - this.getChainLength() / 4.0D;
    }

    public Vec3 getChainPosition() {
        return this.getChainPosition(this.chainAngle, this.getChainLength());
    }

    public Vec3 getChainPosition(float angle, float distance) {
        double dx = Math.cos(angle * Math.PI / 180.0D) * distance;
        double dz = Math.sin(angle * Math.PI / 180.0D) * distance;
        return new Vec3(this.getX() + dx, this.getY() + this.getChainYOffset(), this.getZ() + dz);
    }

    public boolean isSwingingChain() {
        return this.swinging || this.getTarget() != null && this.recoilCounter == 0;
    }

    private void chainMove() {
        this.chainMoveLength = Mth.clamp(this.chainMoveLength + (this.isThrowing() ? 0.5F : -1.5F), 0.0F, 6.0F);
    }

    private void applyBlockCollisions() {
        AABB spikeBox = AABB.ofSize(this.spikePosition, SPIKE_SIZE, SPIKE_SIZE, SPIKE_SIZE);
        List<Entity> entities = this.level().getEntities(this, spikeBox.inflate(0.2D, 0.0D, 0.2D), entity -> entity.isPushable() && !entity.is(this));

        for (Entity entity : entities) {
            entity.push(this);
            if (entity instanceof LivingEntity living) {
                this.applyChainHit(living);
                if (this.isThrowing()) {
                    this.setThrowing(false);
                }
            }
        }

        if (this.isThrowing() && !this.level().noCollision(this, spikeBox)) {
            this.setThrowing(false);
            this.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE, 0.65F, 0.75F);
            this.gameEvent(GameEvent.HIT_GROUND);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if (hurt) {
            entity.push(0.0D, 0.4D, 0.0D);
            this.recoilCounter = 20;
        }
        return hurt;
    }

    public boolean isThrowing() {
        return this.getEntityData().get(IS_THROWING);
    }

    public void setThrowing(boolean throwing) {
        this.getEntityData().set(IS_THROWING, throwing);
    }

    public float getChainMoveLength() {
        return this.chainMoveLength;
    }

    public Vec3 getSpikePosition() {
        return this.spikePosition;
    }

    public Vec3 getChainPart1() {
        return this.chainPart1;
    }

    public Vec3 getChainPart2() {
        return this.chainPart2;
    }

    public Vec3 getChainPart3() {
        return this.chainPart3;
    }

    private float getChainLength() {
        if (this.level().isClientSide()) {
            return Byte.toUnsignedInt(this.getEntityData().get(DATA_CHAIN_LENGTH)) / 127.0F;
        }
        return this.isSwingingChain() ? 0.9F : 0.3F;
    }

    private float getSyncedChainAngle() {
        return Byte.toUnsignedInt(this.getEntityData().get(DATA_CHAIN_POS)) / 255.0F * 360.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.BLOCKCHAIN_GOBLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.BLOCKCHAIN_GOBLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.BLOCKCHAIN_GOBLIN_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsThrowing", this.isThrowing());
        tag.putInt("Recoil", this.recoilCounter);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setThrowing(tag.getBoolean("IsThrowing"));
        this.recoilCounter = tag.getInt("Recoil");
    }

    static class AvoidTntGoal extends Goal {
        private final BlockChainGoblin goblin;
        private PrimedTnt nearestTnt;

        AvoidTntGoal(BlockChainGoblin goblin) {
            this.goblin = goblin;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            List<PrimedTnt> tnts = this.goblin.level().getEntitiesOfClass(PrimedTnt.class, this.goblin.getBoundingBox().inflate(2.0D), Entity::isAlive);
            if (tnts.isEmpty()) {
                this.nearestTnt = null;
                return false;
            }
            tnts.sort((first, second) -> Double.compare(this.goblin.distanceToSqr(first), this.goblin.distanceToSqr(second)));
            this.nearestTnt = tnts.get(0);
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.nearestTnt != null && this.nearestTnt.isAlive() && this.goblin.distanceToSqr(this.nearestTnt) < 9.0D;
        }

        @Override
        public void tick() {
            if (this.nearestTnt != null) {
                Vec3 away = this.goblin.position().subtract(this.nearestTnt.position()).normalize().scale(2.0D);
                this.goblin.getNavigation().moveTo(this.goblin.getX() + away.x(), this.goblin.getY(), this.goblin.getZ() + away.z(), this.goblin.distanceToSqr(this.nearestTnt) < 4.0D ? 2.0D : 1.0D);
            }
        }
    }

    @Override
    public MultipartGenericsAreDumb[] getParts() {
        return this.partsArray;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        TFPart.assignPartIDs(this);
    }
}
