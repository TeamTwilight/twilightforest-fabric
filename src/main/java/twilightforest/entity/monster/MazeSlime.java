package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import org.jetbrains.annotations.Nullable;

public class MazeSlime extends Slime {

	private static final AttributeModifier DOUBLE_HEALTH = new AttributeModifier("Maze slime double health", 1, AttributeModifier.Operation.MULTIPLY_BASE);

	public MazeSlime(EntityType<? extends MazeSlime> type, Level world) {
		super(type, world);
	}

	@Override
	public void setSize(int size, boolean resetHealth) {
		super.setSize(size, resetHealth);
		this.xpReward += 3;
	}

	public static boolean getCanSpawnHere(EntityType<MazeSlime> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entity, world, reason, pos, random) && Monster.isDarkEnoughToSpawn(world, pos, random);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
		assert health != null;
		health.addPermanentModifier(DOUBLE_HEALTH);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public double getMyRidingOffset() {
		return 0.25D;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTiny() ? TFSounds.MAZE_SLIME_HURT_SMALL.get() : TFSounds.MAZE_SLIME_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTiny() ? TFSounds.MAZE_SLIME_DEATH_SMALL.get() : TFSounds.MAZE_SLIME_DEATH.get();
	}

	@Override
	protected SoundEvent getSquishSound() {
		return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL.get() : TFSounds.MAZE_SLIME_SQUISH.get();
	}

	@Override
	protected SoundEvent getJumpSound() {
		return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL.get() : TFSounds.MAZE_SLIME_SQUISH.get();
	}

	@Override
	protected boolean isDealsDamage() {
		return true;
	}

	@Override
	public void tick() {
		this.squish += (this.targetSquish - this.squish) * 0.5F;
		this.oSquish = this.squish;
		super.tick();
		if (this.onGround && !this.wasOnGround) {
			int i = this.getSize();

			if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
			for(int j = 0; j < i * 8; ++j) {
				float f = this.random.nextFloat() * ((float)Math.PI * 2F);
				float f1 = this.random.nextFloat() * 0.5F + 0.5F;
				float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
				float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
				this.level.addParticle(this.getParticleType(), this.getX() + (double)f2, this.getY(), this.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
			}

			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.targetSquish = -0.5F;
		} else if (!this.onGround && this.wasOnGround) {
			this.targetSquish = 1.0F;
		}

		this.wasOnGround = this.onGround;
		this.decreaseSquish();
	}

	public boolean spawnCustomParticles() {
		// [VanillaCopy] from super tick with own particles
		int i = getSize();
		for (int j = 0; j < i * 8; ++j) {
			float f = this.getRandom().nextFloat() * ((float) Math.PI * 2F);
			float f1 = this.getRandom().nextFloat() * 0.5F + 0.5F;
			float f2 = Mth.sin(f) * i * 0.5F * f1;
			float f3 = Mth.cos(f) * i * 0.5F * f1;
			double d0 = this.getX() + f2;
			double d1 = this.getZ() + f3;
			BlockState state = TFBlocks.MAZESTONE_BRICK.get().defaultBlockState();
			this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
		}
		return true;
	}

	@Override
	protected float getSoundVolume() {
		// OH MY GOD, SHUT UP
		return 0.1F * this.getSize();
	}
}
