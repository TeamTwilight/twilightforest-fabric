package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.goal.PhantomAttackStartGoal;
import twilightforest.entity.ai.goal.PhantomThrowWeaponGoal;
import twilightforest.entity.ai.goal.PhantomUpdateFormationAndMoveGoal;
import twilightforest.entity.ai.goal.PhantomWatchAndAttackGoal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;

import java.util.List;

public class KnightPhantom extends BaseTFBoss {
    private static final net.minecraft.resources.ResourceLocation CHARGING_ID = TwilightForestMod.prefix("knight_phantom_charging_attack");
    private static final net.minecraft.resources.ResourceLocation ARMOR_ID = TwilightForestMod.prefix("knight_phantom_inactive_armor");
    private static final AttributeModifier CHARGING_MODIFIER = new AttributeModifier(CHARGING_ID, 7.0D, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier NON_CHARGING_ARMOR_MODIFIER = new AttributeModifier(ARMOR_ID, 4.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private int attackCooldown;
    private int chargeTicks;
    private boolean chargingAtPlayer;
    private int number;
    private int totalKnownKnights = Integer.MIN_VALUE;
    private int ticksProgress;
    private Formation currentFormation = Formation.HOVER;
    private BlockPos chargePos = BlockPos.ZERO;

    public KnightPhantom(EntityType<? extends KnightPhantom> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.xpReward = 93;
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FLYING_SPEED, 0.24D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.WHITE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PhantomUpdateFormationAndMoveGoal(this));
        this.goalSelector.addGoal(2, new PhantomAttackStartGoal(this));
        this.goalSelector.addGoal(3, new PhantomThrowWeaponGoal(this));
        this.goalSelector.addGoal(4, new PhantomWatchAndAttackGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide() && this.isChargingAtPlayer()) {
            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.IRON_SWORD)), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0.0D, -0.1D, 0.0D);
            this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0.0D, 0.1D, 0.0D);
        }
    }

    private void updatePhantomAttack() {
        if (this.attackCooldown > 0) {
            --this.attackCooldown;
        }
        if (this.totalKnownKnights == Integer.MIN_VALUE || this.tickCount % 100 == 0) {
            this.updateMyNumber();
        }
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.setChargingAtPlayer(false);
            this.switchToFormation(Formation.HOVER);
            return;
        }
        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
        this.tickFormation(target);
    }

    private void tickFormation(LivingEntity target) {
        ++this.ticksProgress;
        if (this.currentFormation == Formation.ATTACK_PLAYER_START || this.currentFormation == Formation.ATTACK_PLAYER_ATTACK) {
            if (this.currentFormation == Formation.ATTACK_PLAYER_START) {
                if (this.ticksProgress == 1) {
                    this.chargePos = target.blockPosition();
                    this.playSound(SoundEvents.VEX_CHARGE, 2.0F, this.getVoicePitch());
                }
                Vec3 retreat = this.position().subtract(target.position()).normalize().scale(8.0D);
                this.moveControl.setWantedPosition(target.getX() + retreat.x(), target.getY() + 3.0D, target.getZ() + retreat.z(), 0.8D);
                if (this.ticksProgress >= this.currentFormation.duration) {
                    this.switchToFormation(Formation.ATTACK_PLAYER_ATTACK);
                }
            } else {
                this.setChargingAtPlayer(true);
                this.chargeTicks = Math.max(0, this.currentFormation.duration - this.ticksProgress);
                this.moveControl.setWantedPosition(target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ(), 1.45D);
                if (this.distanceToSqr(target) < 6.25D) {
                    this.doHurtTarget(target);
                    this.attackCooldown = 80;
                    this.switchToFormation(Formation.HOVER);
                } else if (this.ticksProgress >= this.currentFormation.duration) {
                    this.attackCooldown = 60;
                    this.switchToFormation(Formation.HOVER);
                }
            }
            return;
        }

        this.setChargingAtPlayer(false);
        if (this.attackCooldown <= 0 && target.distanceToSqr(this) < 48.0D * 48.0D && this.getSensing().hasLineOfSight(target) && this.getNumber() == 0) {
            this.broadcastFormation(Formation.ATTACK_PLAYER_START);
            return;
        }
        Vec3 destination = this.getFormationDestination(target);
        this.moveControl.setWantedPosition(destination.x(), destination.y(), destination.z(), this.currentFormation == Formation.HOVER ? 0.55D : 0.75D);
        if (this.ticksProgress >= this.currentFormation.duration && this.getNumber() == 0) {
            this.broadcastFormation(this.currentFormation.next());
        }
    }

    private Vec3 getFormationDestination(LivingEntity target) {
        double baseAngle = switch (this.currentFormation) {
            case LARGE_ANTICLOCKWISE, SMALL_ANTICLOCKWISE -> -this.tickCount * 0.04D;
            case CHARGE_PLUSX -> 0.0D;
            case CHARGE_MINUSX -> Math.PI;
            case CHARGE_PLUSZ -> Math.PI * 0.5D;
            case CHARGE_MINUSZ -> Math.PI * 1.5D;
            default -> this.tickCount * 0.04D;
        };
        double numberAngle = baseAngle + this.getNumber() * (Math.PI * 2.0D / Math.max(1, this.totalKnownKnights));
        double radius = switch (this.currentFormation) {
            case SMALL_CLOCKWISE, SMALL_ANTICLOCKWISE -> 4.0D;
            case CHARGE_PLUSX, CHARGE_MINUSX, CHARGE_PLUSZ, CHARGE_MINUSZ -> 10.0D;
            default -> 8.0D;
        };
        return new Vec3(target.getX() + Math.cos(numberAngle) * radius, target.getY() + 3.5D + (this.getNumber() % 3), target.getZ() + Math.sin(numberAngle) * radius);
    }

    private void broadcastFormation(Formation formation) {
        for (KnightPhantom phantom : this.getNearbyKnights()) {
            phantom.switchToFormation(formation);
        }
    }

    public void switchToFormation(Formation formation) {
        if (this.currentFormation != formation) {
            this.currentFormation = formation;
            this.ticksProgress = 0;
            this.chargeTicks = 0;
            this.setChargingAtPlayer(formation == Formation.ATTACK_PLAYER_START || formation == Formation.ATTACK_PLAYER_ATTACK);
        }
    }

    public Formation getCurrentFormation() {
        return this.currentFormation;
    }

    public BlockPos getChargePos() {
        return this.chargePos;
    }

    public void setChargePos(BlockPos chargePos) {
        this.chargePos = chargePos;
    }

    public int getTicksProgress() {
        return this.ticksProgress;
    }

    public void setTicksProgress(int ticksProgress) {
        this.ticksProgress = ticksProgress;
    }

    public int getMaxTicksForFormation() {
        return this.currentFormation.duration;
    }

    public boolean isSwordKnight() {
        return this.getMainHandItem().is(TFItems.KNIGHTMETAL_SWORD.get());
    }

    public boolean isAxeKnight() {
        return this.getMainHandItem().is(TFItems.KNIGHTMETAL_AXE.get());
    }

    public boolean isPickKnight() {
        return this.getMainHandItem().is(TFItems.KNIGHTMETAL_PICKAXE.get());
    }

    public List<KnightPhantom> getNearbyKnights() {
        return this.level().getEntitiesOfClass(KnightPhantom.class, this.getBoundingBox().inflate(64.0D), LivingEntity::isAlive);
    }

    public boolean isMobWithinHomeArea(LivingEntity entity) {
        return !this.isOutsideHomeRange(entity.position());
    }

    private void updateMyNumber() {
        List<KnightPhantom> knights = this.getNearbyKnights();
        this.totalKnownKnights = Math.max(1, knights.size());
        boolean[] used = new boolean[this.totalKnownKnights + 1];
        for (KnightPhantom knight : knights) {
            if (knight != this && knight.getNumber() >= 0 && knight.getNumber() < used.length) {
                used[knight.getNumber()] = true;
            }
        }
        if (this.number >= used.length || used[Math.max(0, this.number)]) {
            for (int i = 0; i < used.length; i++) {
                if (!used[i]) {
                    this.setNumber(i);
                    break;
                }
            }
        }
    }

    public boolean isChargingAtPlayer() {
        return this.chargingAtPlayer;
    }

    public boolean hasYetToDisappear() {
        return !this.isRemoved();
    }

    private void setChargingAtPlayer(boolean charging) {
        this.chargingAtPlayer = charging;
        if (this.level().isClientSide() || this.getAttribute(Attributes.ATTACK_DAMAGE) == null || this.getAttribute(Attributes.ARMOR) == null) {
            return;
        }
        if (charging) {
            if (!this.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(CHARGING_ID)) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(CHARGING_MODIFIER);
            }
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_ID);
        } else {
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CHARGING_ID);
            if (!this.getAttribute(Attributes.ARMOR).hasModifier(ARMOR_ID)) {
                this.getAttribute(Attributes.ARMOR).addTransientModifier(NON_CHARGING_ARMOR_MODIFIER);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.HAUNT, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (hurt) {
            entity.push(this.getLookAngle().x() * 0.8D, 0.3D, this.getLookAngle().z() * 0.8D);
        }
        return hurt;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_WALL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        this.hasImpulse = true;
        float distance = 0.2F;
        float scale = net.minecraft.util.Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        this.setDeltaMovement(new Vec3(this.getDeltaMovement().x() - xRatio / scale * distance, Math.min(this.getDeltaMovement().y() + distance, 0.4D), this.getDeltaMovement().z() - zRatio / scale * distance));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.KNIGHT_PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.KNIGHT_PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.KNIGHT_PHANTOM_DEATH;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
        this.setItemSlot(EquipmentSlot.MAINHAND, switch (number % 3) {
            case 1 -> new ItemStack(TFItems.KNIGHTMETAL_AXE.get());
            case 2 -> new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get());
            default -> new ItemStack(TFItems.KNIGHTMETAL_SWORD.get());
        });
        this.setItemSlot(EquipmentSlot.CHEST, TFItemVisuals.withModel(new ItemStack(Items.IRON_CHESTPLATE), TFItemVisuals.IRONWOOD_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.HEAD, TFItemVisuals.withModel(new ItemStack(Items.IRON_HELMET), TFItemVisuals.IRONWOOD_HELMET));
    }

    @Override
    public void setRestrictionPoint(@org.jetbrains.annotations.Nullable GlobalPos pos) {
        super.setRestrictionPoint(pos);
        if (pos != null) {
            this.chargePos = pos.pos();
        }
    }

    @Override
    public int getHomeRadius() {
        return 30;
    }

    @Override
    protected boolean shouldSpawnLoot() {
        return super.shouldSpawnLoot() && this.level().getEntitiesOfClass(KnightPhantom.class, this.getBoundingBox().inflate(64.0D), knight -> knight != this && knight.isAlive()).isEmpty();
    }

    @Override
    protected void postRemoval(ServerLevel serverLevel, RemovalReason reason) {
        if (reason != RemovalReason.KILLED || this.shouldSpawnLoot()) {
            super.postRemoval(serverLevel, reason);
        }
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.KNIGHT_STRONGHOLD;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.DARK_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MyNumber", this.getNumber());
        tag.putInt("AttackCooldown", this.attackCooldown);
        tag.putInt("ChargeTicks", this.chargeTicks);
        tag.putInt("TotalKnownKnights", this.totalKnownKnights);
        tag.putInt("Formation", this.currentFormation.ordinal());
        tag.putInt("TicksProgress", this.ticksProgress);
        tag.putLong("ChargePos", this.chargePos.asLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setNumber(tag.getInt("MyNumber"));
        this.attackCooldown = tag.getInt("AttackCooldown");
        this.chargeTicks = tag.getInt("ChargeTicks");
        this.totalKnownKnights = tag.contains("TotalKnownKnights") ? tag.getInt("TotalKnownKnights") : Integer.MIN_VALUE;
        if (tag.contains("Formation")) {
            int formation = Mth.clamp(tag.getInt("Formation"), 0, Formation.values().length - 1);
            this.currentFormation = Formation.values()[formation];
        }
        this.ticksProgress = tag.getInt("TicksProgress");
        if (tag.contains("ChargePos")) {
            this.chargePos = BlockPos.of(tag.getLong("ChargePos"));
        }
    }

    public enum Formation {
        HOVER(90),
        LARGE_CLOCKWISE(180),
        SMALL_CLOCKWISE(90),
        LARGE_ANTICLOCKWISE(180),
        SMALL_ANTICLOCKWISE(90),
        CHARGE_PLUSX(180),
        CHARGE_MINUSX(180),
        CHARGE_PLUSZ(180),
        CHARGE_MINUSZ(180),
        WAITING_FOR_LEADER(10),
        ATTACK_PLAYER_START(50),
        ATTACK_PLAYER_ATTACK(50);

        final int duration;

        Formation(int duration) {
            this.duration = duration;
        }

        Formation next() {
            return switch (this) {
                case HOVER -> LARGE_CLOCKWISE;
                case LARGE_CLOCKWISE -> SMALL_CLOCKWISE;
                case SMALL_CLOCKWISE -> LARGE_ANTICLOCKWISE;
                case LARGE_ANTICLOCKWISE -> SMALL_ANTICLOCKWISE;
                case SMALL_ANTICLOCKWISE -> CHARGE_PLUSX;
                case CHARGE_PLUSX -> CHARGE_PLUSZ;
                case CHARGE_PLUSZ -> CHARGE_MINUSX;
                case CHARGE_MINUSX -> CHARGE_MINUSZ;
                default -> HOVER;
            };
        }
    }
}
