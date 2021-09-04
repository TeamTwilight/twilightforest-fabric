package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;

import javax.annotation.Nullable;
import java.util.Random;

public class HostileWolfEntity extends Wolf implements Enemy {

	public HostileWolfEntity(EntityType<? extends HostileWolfEntity> type, Level world) {
		super(type, world);
		this.setTame(false);
		this.setOrderedToSit(false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Wolf.createAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
		this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
		this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
	}

	public static boolean getCanSpawnHere(EntityType<? extends HostileWolfEntity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		// are we near a hedge maze?
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		return (TFFeature.getNearestFeature(chunkX, chunkZ, world.getLevel()) == TFFeature.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(world, pos, random));
				/*&& world.checkNoEntityCollision(this)
				&& world.getCollisionBoxes(this, getBoundingBox()).size() == 0
				&& !world.containsAnyLiquid(getBoundingBox());*/
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getTarget())
			playSound(getTargetSound(), 4F, getVoicePitch());
		super.setTarget(entity);
	}

	protected SoundEvent getTargetSound() {
		return TFSounds.HOSTILE_WOLF_TARGET;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HOSTILE_WOLF_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.HOSTILE_WOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.HOSTILE_WOLF_DEATH;
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isInterested() {
		return false;
	}

	@Override
	public boolean canMate(Animal otherAnimal) {
		return false;
	}

	@Override
	public Wolf getBreedOffspring(ServerLevel world, AgeableMob mate) {
		return null;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}
}
