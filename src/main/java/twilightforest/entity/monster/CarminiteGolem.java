package twilightforest.entity.monster;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.init.TFSounds;

public class CarminiteGolem extends Monster {

	private int attackTimer;

	public CarminiteGolem(EntityType<? extends CarminiteGolem> type, Level world) {
		super(type, world);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ATTACK_DAMAGE, 9.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		this.attackTimer = 10;
		this.getLevel().broadcastEntityEvent(this, (byte) 4);
		boolean attackSuccess = super.doHurtTarget(entity);

		if (attackSuccess) {
			entity.push(0.0D, 0.4D, 0.0D);
		}

		return attackSuccess;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.CARMINITE_GOLEM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.CARMINITE_GOLEM_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.CARMINITE_GOLEM_STEP.get(), 1.0F, 1.0F);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		// [VanillaCopy] last half of IronGolem.aiStep
		if (this.getDeltaMovement().x() * this.getDeltaMovement().x() + this.getDeltaMovement().z() * this.getDeltaMovement().z() > 2.500000277905201E-7D && this.getRandom().nextInt(5) == 0) {
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY() - 0.2D);
			int k = Mth.floor(this.getZ());
			BlockState state = this.getLevel().getBlockState(new BlockPos(i, j, k));

			if (state.getMaterial() != Material.AIR) {
				this.getLevel().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.getX() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), this.getBoundingBox().minY + 0.1D, this.getZ() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), 4.0D * (this.getRandom().nextFloat() - 0.5D), 0.5D, (this.getRandom().nextFloat() - 0.5D) * 4.0D);
			}
		}
		// End copy

		if (this.getRandom().nextBoolean()) {
			this.getLevel().addParticle(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F), this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), 0, 0, 0);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.attackTimer = 10;
			this.playSound(TFSounds.CARMINITE_GOLEM_ATTACK.get(), 1.0F, 1.0F);
		} else {
			super.handleEntityEvent(id);
		}
	}

	public int getAttackTimer() {
		return this.attackTimer;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}
}
