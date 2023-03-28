package twilightforest.entity.boss;

import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import me.pepperbell.simplenetworking.S2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.control.NagaMoveControl;
import twilightforest.entity.ai.goal.NagaAttackGoal;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.ai.goal.NagaSmashGoal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.loot.TFLootTables;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.ThrowPlayerPacket;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Naga extends Monster implements EnforcedHomePoint, MultiPartEntity {

	private static final int TICKS_BEFORE_HEALING = 600;
	private static final int MAX_SEGMENTS = 12;
	private static final int LEASH_X = 46;
	private static final int LEASH_Y = 7;
	private static final int LEASH_Z = 46;
	private static final double DEFAULT_SPEED = 0.3;

	private int currentSegmentCount = 0; // not including head
	private final float healthPerSegment;
	private final NagaSegment[] bodySegments = new NagaSegment[MAX_SEGMENTS];
	private NagaMovementPattern movementAI;
	private int ticksSinceDamaged = 0;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10);

	private final AttributeModifier slowSpeed = new AttributeModifier("Naga Slow Speed", 0.25F, AttributeModifier.Operation.ADDITION);
	private final AttributeModifier fastSpeed = new AttributeModifier("Naga Fast Speed", 0.50F, AttributeModifier.Operation.ADDITION);

	private static final EntityDataAccessor<Boolean> DATA_DAZE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);

	public Naga(EntityType<? extends Naga> type, Level level) {
		super(type, level);
		setMaxUpStep(getStepHeight());
		this.xpReward = 217;
		this.noCulling = true;

		for (int i = 0; i < this.bodySegments.length; i++) {
			this.bodySegments[i] = new NagaSegment(this);
		}

		this.goNormal();
		if (this.getLevel().getDifficulty() != Difficulty.NORMAL && this.getAttribute(Attributes.MAX_HEALTH) != null) {
			boolean hard = this.level.getDifficulty() == Difficulty.HARD;
			Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("Difficulty Health Boost", hard ? 130 : 80, AttributeModifier.Operation.ADDITION));
			this.setHealth(this.getMaxHealth());
		}
		//run the segment health after we adjust the difficulty health
		this.healthPerSegment = this.getMaxHealth() / 10;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_DAZE, false);
		this.entityData.define(DATA_CHARGE, false);
	}

	public boolean isDazed() {
		return this.entityData.get(DATA_DAZE);
	}

	public void setDazed(boolean daze) {
		this.entityData.set(DATA_DAZE, daze);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGE);
	}

	public void setCharging(boolean charge) {
		this.entityData.set(DATA_CHARGE, charge);
	}

	public NagaMovementPattern getMovementAI() {
		return this.movementAI;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new NagaAttackGoal(this));
		this.goalSelector.addGoal(3, new NagaSmashGoal(this));
		this.goalSelector.addGoal(4, this.movementAI = new NagaMovementPattern(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D) {
			@Override
			public void start() {
				Naga.this.setTarget(null);
				super.start();
			}
		});
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1, 1) {
			@Override
			public void start() {
				Naga.this.goNormal();
				super.start();
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
				return Naga.this.isWithinRestriction(Naga.this.blockPosition()) && super.canUse();
			}
		});

		this.moveControl = new NagaMoveControl(this);
	}

	@Override
	public void aiStep() {

		super.aiStep();

		if (this.getLevel().isClientSide() || !this.getLevel().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return;

		AABB bb = this.getBoundingBox();

		int minx = Mth.floor(bb.minX - 0.75D);
		int miny = Mth.floor(bb.minY + 1.01D);
		int minz = Mth.floor(bb.minZ - 0.75D);
		int maxx = Mth.floor(bb.maxX + 0.75D);
		int maxy = Mth.floor(bb.maxY + 0.0D);
		int maxz = Mth.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (this.getLevel().hasChunksAt(min, max)) {
			for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
				BlockState state = this.getLevel().getBlockState(pos);
				if (state.getMaterial() == Material.LEAVES && EntityUtil.canDestroyBlock(this.getLevel(), pos, state, this)) {
					this.getLevel().destroyBlock(pos, true);
				}
			}
		}
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 120)
				.add(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 80.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
	}

	/**
	 * Determine how many segments, from 2-12, the naga should have, dependent on its current health
	 */
	private void setSegmentsPerHealth() {
		int oldSegments = this.currentSegmentCount;
		int newSegments = Mth.clamp((int) ((this.getHealth() / this.healthPerSegment) + (getHealth() > 0 ? 2 : 0)), 0, MAX_SEGMENTS);
		this.currentSegmentCount = newSegments;
		if (newSegments < oldSegments) {
			for (int i = newSegments; i < oldSegments; i++) {
				this.bodySegments[i].selfDestruct();
			}
		} else if (newSegments > oldSegments) {
			this.activateBodySegments();
		}

		if (!this.getLevel().isClientSide()) {
			double newSpeed = DEFAULT_SPEED - newSegments * (-0.2F / 12F);
			if (newSpeed < 0)
				newSpeed = 0;
			Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(newSpeed);
		}
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	@Override
	public void tick() {
		if (this.deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.getLevel().addParticle((this.getRandom().nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.POOF),
						(this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
						this.getY() + this.getRandom().nextFloat() * this.getBbHeight(),
						(this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
						d, d1, d2);
			}
		}

		if (this.isDazed()) {
			for (int i = 0; i < 5; i++) {
				Vec3 pos = new Vec3(this.getX(), this.getY() + 2.15D, this.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(this.getRandom().nextInt(360))));
				this.getLevel().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);
			}
		}

		// update health
		this.ticksSinceDamaged++;

		if (!this.getLevel().isClientSide() && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		this.setSegmentsPerHealth();

		super.tick();

		this.moveSegments();
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (this.getTarget() != null && (this.distanceToSqr(getTarget()) > 80 * 80 || !this.areSelfAndTargetInHome(this.getTarget()))) {
			this.setTarget(null);
		}

		// if we are very close to the path point, go to the next point, unless the path is finished
		double d = this.getBbWidth() * 4.0F;
		Vec3 vec3d = this.isPathFinding() ? Objects.requireNonNull(this.getNavigation().getPath()).getNextEntityPos(this) : null;

		while (vec3d != null && vec3d.distanceToSqr(this.getX(), vec3d.y(), this.getZ()) < d * d) {
			this.getNavigation().getPath().advance();

			if (this.getNavigation().getPath().isDone()) {
				vec3d = null;
			} else {
				vec3d = this.getNavigation().getPath().getNextEntityPos(this);
			}
		}

		// BOSS BAR!
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.NAGA_HISS.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.NAGA_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.NAGA_HURT.get();
	}

	/**
	 * Sets the naga to move slowly, such as when he is intimidating the player
	 */
	public void goSlow() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed); // if we apply this twice, we crash, but we can always remove it
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(slowSpeed);
	}

	/**
	 * Normal speed, like when he is circling
	 */
	public void goNormal() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
	}

	/**
	 * Fast, like when he is charging
	 */
	public void goFast() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(fastSpeed);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.getEntity() != null && !this.isEntityWithinHomeArea(src.getEntity()) // reject damage from outside of our home radius
				|| src.getDirectEntity() != null && !this.isEntityWithinHomeArea(src.getDirectEntity())
				|| src.is(DamageTypeTags.IS_FIRE) || src.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(src);
	}

	@Override
	public boolean causeFallDamage(float dist, float mult, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypeTags.IS_FALL) && super.hurt(source, amount)) {
			this.ticksSinceDamaged = 0;
			if (source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
				this.hurtBy.add(player);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doHurtTarget(Entity toAttack) {
		if (this.movementAI.getState() == NagaMovementPattern.MovementState.CHARGE && toAttack instanceof LivingEntity living && living.isBlocking()) {
			Vec3 motion = this.getDeltaMovement();
			toAttack.push(motion.x() * 1.5D, 0.5D, motion.z() * 1.5D);
			this.push(motion.x() * -1.25D, 0.5D, motion.z() * -1.25D);
			if (toAttack instanceof ServerPlayer player) {
				player.getUseItem().hurtAndBreak(5, player, user -> user.broadcastBreakEvent(player.getUsedItemHand()));
				TFPacketHandler.CHANNEL.sendToClient(new ThrowPlayerPacket(motion.x() * 3.0D,  motion.y() + 0.75D, motion.z() * 3.0D), player);
			}
			this.hurt(this.damageSources().generic(), 4.0F);
			this.getLevel().playSound(null, toAttack.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + this.getLevel().getRandom().nextFloat() * 0.4F);
			this.movementAI.doDaze();
			return false;
		}

		if (!this.isDazed()) {
			boolean result = super.doHurtTarget(toAttack);

			if (result) {
				// charging, apply extra pushback
				toAttack.push(-Mth.sin((getYRot() * Mth.PI) / 180.0F) * 2.0F, 0.4F, Mth.cos((getYRot() * Mth.PI) / 180.0F) * 2.0F);
			}

			return result;
		}
		return false;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		if (!this.isWithinRestriction(pos)) {
			return Float.MIN_VALUE;
		} else {
			return 0.0F;
		}
	}

	@Override
	public void checkDespawn() {
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.getRestrictCenter() != BlockPos.ZERO) {
				this.getLevel().setBlockAndUpdate(this.getRestrictCenter(), TFBlocks.NAGA_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		if (this.getLevel() instanceof ServerLevel) {
			for (NagaSegment seg : this.bodySegments) {
				// must use this instead of setDead
				// since multiparts are not added to the world tick list which is what checks isDead
				seg.kill();
			}
		}
	}

	@Override
	public boolean isWithinRestriction(BlockPos pos) {
		if (this.getRestrictRadius() == -1) {
			return true;
		} else {
			int distX = Math.abs(this.getRestrictCenter().getX() - pos.getX());
			int distY = Math.abs(this.getRestrictCenter().getY() - pos.getY());
			int distZ = Math.abs(this.getRestrictCenter().getZ() - pos.getZ());

			return distX <= LEASH_X && distY <= LEASH_Y && distZ <= LEASH_Z;
		}
	}

	public boolean isEntityWithinHomeArea(Entity entity) {
		return this.isWithinRestriction(entity.blockPosition());
	}

	public boolean areSelfAndTargetInHome(Entity entity) {
		return this.isWithinRestriction(this.blockPosition()) && this.isEntityWithinHomeArea(entity);
	}

	private void activateBodySegments() {
		for (int i = 0; i < this.currentSegmentCount; i++) {
			NagaSegment segment = this.bodySegments[i];
			segment.activate();
			segment.moveTo(getX() + 0.1 * i, getY() + 0.5D, getZ() + 0.1 * i, this.getRandom().nextFloat() * 360.0F, 0.0F);
			for (int j = 0; j < 20; j++) {
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.getLevel().addParticle(ParticleTypes.EXPLOSION,
						segment.getX() + this.getRandom().nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d0 * 10.0D,
						segment.getY() + this.getRandom().nextFloat() * segment.getBbHeight() - d1 * 10.0D,
						segment.getZ() + this.getRandom().nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d2 * 10.0D,
						d0, d1, d2);
			}
		}
	}

	/**
	 * Sets the heading (ha ha) of the bodySegments segments
	 */
	private void moveSegments() {
		for (int i = 0; i < this.bodySegments.length; i++) {
			this.bodySegments[i].tick();
			Entity leader = i == 0 ? this : this.bodySegments[i - 1];
			double followX = leader.getX();
			double followY = leader.getY();
			double followZ = leader.getZ();

			// also weight the position so that the segments straighten out a little bit, and the front ones straighten more
			float angle = (((leader.getYRot() + 180) * Mth.PI) / 180.0F);


			double straightenForce = 0.05D + (1.0D / (i + 1)) * 0.5D;

			double idealX = -Mth.sin(angle) * straightenForce;
			double idealZ = Mth.cos(angle) * straightenForce;

			double groundY = this.bodySegments[i].isInWall() ? followY + 2.0F : followY;
			double idealY = (groundY - followY) * straightenForce;

			Vec3 diff = new Vec3(this.bodySegments[i].getX() - followX, this.bodySegments[i].getY() - followY, this.bodySegments[i].getZ() - followZ);
			diff = diff.normalize();

			// weight so segments drift towards their ideal position
			diff = diff.add(idealX, idealY, idealZ).normalize();

			double f = 2.0D;

			double destX = followX + f * diff.x();
			double destY = followY + f * diff.y();
			double destZ = followZ + f * diff.z();

			this.bodySegments[i].setPos(destX, destY, destZ);

			double distance = Mth.sqrt((float) (diff.x() * diff.x() + diff.z() * diff.z()));

			if (i == 0) {
				// tilt segment next to head up towards head
				diff = diff.add(0.0D, -0.15D, 0.0D);
			}

			this.bodySegments[i].setRot((float) (Math.atan2(diff.z(), diff.x()) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y(), distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound, 20);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the courtyard as defeated
		if (!this.getLevel().isClientSide()) {
			LandmarkUtil.markStructureConquered(this.getLevel(), this, TFStructures.NAGA_COURTYARD, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			TFLootTables.entityDropsIntoContainer(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), this.getRandom().nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get().defaultBlockState() : TFBlocks.CANOPY_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		TFPart.assignPartIDs(this);
	}

	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return this.bodySegments;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

//	@Override TODO: PORT
	public float getStepHeight() {
		return 2.0F;
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
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

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public BlockPos getRestrictionCenter() {
		return this.getRestrictCenter();
	}

	@Override
	public void setRestriction(BlockPos pos, int dist) {
		this.restrictTo(pos, dist);
	}
}
