package twilightforest.entity.monster;

import io.github.fabricators_of_create.porting_lib.entity.StepHeightEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.init.TFSounds;

public class MosquitoSwarm extends Monster implements StepHeightEntity {

	public MosquitoSwarm(EntityType<? extends MosquitoSwarm> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 12.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	public float getStepHeight() {
		return 2.1F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MOSQUITO.get();
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (super.doHurtTarget(entity)) {
			if (entity instanceof LivingEntity living) {
				int duration = switch (this.getLevel().getDifficulty()) {
					case EASY -> 7;
					case HARD -> 30;
					default -> 15;
				};

				living.addEffect(new MobEffectInstance(MobEffects.HUNGER, duration * 20, 0));
			}

			return true;
		} else {
			return false;
		}
	}

	public static boolean canSpawn(EntityType<? extends Monster> type, LevelAccessor accessor, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && Monster.checkAnyLightMonsterSpawnRules(type, accessor, reason, pos, rand);
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
	}

}
