package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.control.NoClipMoveControl;
import twilightforest.init.TFDamageSources;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class Wraith extends FlyingMob implements Enemy, EnforcedHomePoint {

	public Wraith(EntityType<? extends Wraith> type, Level world) {
		super(type, world);
		moveControl = new NoClipMoveControl(this);
		noPhysics = true;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new MoveTowardsHomeGoal(this, 0.85D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this));
		this.goalSelector.addGoal(5, new FlyTowardsTargetGoal(this));
		this.goalSelector.addGoal(6, new RandomFloatAroundGoal(this));
		this.goalSelector.addGoal(7, new LookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public boolean isSteppingCarefully() {
		return true;
	}

	// [VanillaCopy] Mob.doHurtTarget. This whole inheritance hierarchy makes me sad.
	@Override
	public boolean doHurtTarget(Entity entityIn) {
		float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entityIn).getMobType());
			i += EnchantmentHelper.getKnockbackBonus(this);
		}

		boolean flag = entityIn.hurt(TFDamageSources.haunt(this), f);

		if (flag) {
			if (i > 0) {
				((LivingEntity) entityIn).knockback(i * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), (-Mth.cos(this.getYRot() * 0.017453292F)));
				this.setDeltaMovement(getDeltaMovement().x() * 0.6D, getDeltaMovement().y(), getDeltaMovement().z() * 0.6D);
			}

			int j = EnchantmentHelper.getFireAspect(this);

			if (j > 0) {
				entityIn.setSecondsOnFire(j * 4);
			}

			if (entityIn instanceof Player entityplayer) {
				ItemStack itemstack = this.getMainHandItem();
				ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof AxeItem && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

					if (this.getRandom().nextFloat() < f1) {
						entityplayer.getCooldowns().addCooldown(Items.SHIELD, 100);
						this.getLevel().broadcastEntityEvent(entityplayer, (byte) 30);
					}
				}
			}

			this.doEnchantDamageEffects(this, entityIn);
		}

		return flag;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (super.hurt(source, amount)) {
			Entity entity = source.getEntity();
			if (this.getVehicle() == entity || this.getPassengers().contains(entity)) {
				return true;
			}
			if (entity != this && entity instanceof LivingEntity && !source.isCreativePlayer()) {
				this.setTarget((LivingEntity) entity);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WRAITH_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.WRAITH_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.WRAITH_DEATH.get();
	}

	public static boolean checkMonsterSpawnRules(EntityType<? extends Wraith> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && checkMobSpawnRules(entity, world, reason, pos, random);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (type == MobSpawnType.STRUCTURE || type == MobSpawnType.SPAWNER) this.restrictTo(this.blockPosition(), 20);
		return super.finalizeSpawn(accessor, difficulty, type, data, tag);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		this.saveHomePointToNbt(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.loadHomePointFromNbt(tag, 20);
	}

	@Override
	public BlockPos getRestrictionCenter() {
		return this.getRestrictCenter();
	}

	@Override
	public void setRestriction(BlockPos pos, int dist) {
		this.restrictTo(pos, dist);
	}

	static class FlyTowardsTargetGoal extends Goal {
		private final Wraith taskOwner;

		FlyTowardsTargetGoal(Wraith wraith) {
			this.taskOwner = wraith;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return taskOwner.getTarget() != null;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			LivingEntity target = taskOwner.getTarget();
			if (target != null)
				taskOwner.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.5F);
		}
	}

	// Similar to MeleeAttackGoal but simpler (no pathfinding)
	static class MeleeAttackGoal extends Goal {
		private final Wraith taskOwner;
		private int attackTick = 20;

		MeleeAttackGoal(Wraith taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean canUse() {
			LivingEntity target = taskOwner.getTarget();

			return target != null
					&& target.getBoundingBox().maxY > this.taskOwner.getBoundingBox().minY
					&& target.getBoundingBox().minY < this.taskOwner.getBoundingBox().maxY
					&& this.taskOwner.distanceToSqr(target) <= 4.0D;
		}

		@Override
		public void tick() {
			if (this.attackTick > 0) {
				this.attackTick--;
			}
		}

		@Override
		public void stop() {
			this.attackTick = 20;
		}

		@Override
		public void start() {
			if (this.taskOwner.getTarget() != null)
				this.taskOwner.doHurtTarget(this.taskOwner.getTarget());
			this.attackTick = 20;
		}
	}

	// [VanillaCopy] Ghast.RandomFloatAroundGoal
	static class RandomFloatAroundGoal extends Goal {
		private final Wraith parentEntity;

		public RandomFloatAroundGoal(Wraith wraith) {
			this.parentEntity = wraith;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (this.parentEntity.getTarget() != null || !this.parentEntity.isWithinRestriction())
				return false;
			MoveControl entitymovehelper = this.parentEntity.getMoveControl();
			double d0 = entitymovehelper.getWantedX() - this.parentEntity.getX();
			double d1 = entitymovehelper.getWantedY() - this.parentEntity.getY();
			double d2 = entitymovehelper.getWantedZ() - this.parentEntity.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			RandomSource random = this.parentEntity.getRandom();
			double d0 = this.parentEntity.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.parentEntity.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.parentEntity.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 0.5D);
		}
	}

	// [VanillaCopy] Ghast.GhastLookGoal
	public static class LookAroundGoal extends Goal {
		private final Wraith parentEntity;

		public LookAroundGoal(Wraith wraith) {
			this.parentEntity = wraith;
			this.setFlags(EnumSet.of(Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public void tick() {
			if (this.parentEntity.getTarget() == null) {
				this.parentEntity.setYRot(-((float) Mth.atan2(this.parentEntity.getDeltaMovement().x(), this.parentEntity.getDeltaMovement().z())) * (180F / (float) Math.PI));
				this.parentEntity.setYBodyRot(this.parentEntity.getYRot());
			} else {
				LivingEntity entitylivingbase = this.parentEntity.getTarget();

				if (entitylivingbase.distanceToSqr(this.parentEntity) < 4096.0D) {
					double d1 = entitylivingbase.getX() - this.parentEntity.getX();
					double d2 = entitylivingbase.getZ() - this.parentEntity.getZ();
					this.parentEntity.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
					this.parentEntity.setYBodyRot(this.parentEntity.getYRot());
				}
			}
		}
	}

	//modified version of MoveTowardsRestrictionGoal. We're limited with what we can use since Wraiths arent PathfinderMobs
	public static class MoveTowardsHomeGoal extends Goal {
		private final FlyingMob mob;
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;

		public MoveTowardsHomeGoal(FlyingMob mob, double speedModifier) {
			this.mob = mob;
			this.speedModifier = speedModifier;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (this.mob.isWithinRestriction() || this.mob.getTarget() != null) {
				return false;
			} else {
				BlockPos pos = this.mob.getRestrictCenter()
						.relative(Direction.getRandom(this.mob.getRandom()))
						.offset(this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5));
				if (pos == null || !this.mob.getLevel().isLoaded(pos)) {
					return false;
				} else {
					this.wantedX = pos.getX();
					this.wantedY = pos.getY();
					this.wantedZ = pos.getZ();
					return true;
				}
			}
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void start() {
			this.mob.getMoveControl().setWantedPosition(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
		}
	}
}
