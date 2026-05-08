package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.monster.LichMinion;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class LichMinionsGoal extends Goal {

	public static final float ATTACK_RANGE = 20.0F;
	private final Lich lich;

	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public LichMinionsGoal(Lich boss) {
		this.lich = boss;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.lich.getPhase() == 2 && !this.lich.isShadowClone();
	}

	@Override
	public void start() {
		this.lich.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.ZOMBIE_SCEPTER.get()));
	}

	@Override
	public void stop() {
		super.stop();
		this.seeTime = 0;
	}

	@Override
	public void tick() {
		if (this.lich.getTeleportInvisibility() > 0) {
			return;
		}
		LivingEntity target = this.lich.getTarget();
		if (target == null) {
			return;
		}
		float distance = this.lich.distanceTo(target);
		if (this.lich.getAttackCooldown() % 15 == 0) {
			this.checkAndSpawnMinions();
		}

		boolean hasLineOfSight = this.lich.getSensing().hasLineOfSight(target);
		if (hasLineOfSight != this.seeTime > 0) {
			this.seeTime = 0;
		}
		if (hasLineOfSight) {
			++this.seeTime;
		} else {
			--this.seeTime;
		}

		if (distance < ATTACK_RANGE && this.seeTime >= 20) {
			this.lich.getNavigation().stop();
			++this.strafingTime;
		} else {
			this.strafingTime = -1;
		}

		if (this.strafingTime >= 20) {
			if (this.lich.getRandom().nextFloat() < 0.3F) {
				this.strafingClockwise = !this.strafingClockwise;
			}
			if (this.lich.getRandom().nextFloat() < 0.3F) {
				this.strafingBackwards = !this.strafingBackwards;
			}
			this.strafingTime = 0;
		}

		if (this.strafingTime > -1) {
			if (distance > ATTACK_RANGE * 0.75F) {
				this.strafingBackwards = false;
			} else if (distance < ATTACK_RANGE * 0.25F) {
				this.strafingBackwards = true;
			}
			this.lich.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
			this.lich.lookAt(target, 30.0F, 30.0F);
		} else {
			this.lich.getLookControl().setLookAt(target, 30.0F, 30.0F);
		}

		if (this.lich.getAttackCooldown() == 0) {
			if (distance < 2.0F) {
				this.lich.doHurtTarget(target);
				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setAttackCooldown(20);
			} else if (distance < ATTACK_RANGE && this.lich.getSensing().hasLineOfSight(target)) {
				if (this.lich.getNextAttackType() == 0) {
					this.lich.launchProjectileAt(new LichBolt(this.lich.level(), this.lich));
				} else {
					this.lich.launchProjectileAt(new LichBomb(this.lich.level(), this.lich));
				}
				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setNextAttackType(this.lich.getRandom().nextBoolean() ? 0 : 1);
				this.lich.setAttackCooldown(60);
			} else {
				this.lich.teleportToNewTarget(target, ATTACK_RANGE, null);
				this.lich.setAttackCooldown(20);
			}
		}
	}

	private void checkAndSpawnMinions() {
		if (!this.lich.level().isClientSide() && this.lich.getMinionsToSummon() > 0 && this.lich.countMyMinions() < Lich.MAX_ACTIVE_MINIONS) {
			this.spawnMinionAt();
			this.lich.setMinionsToSummon(this.lich.getMinionsToSummon() - 1);
		}
	}

	private void spawnMinionAt() {
		LivingEntity target = this.lich.getTarget();
		Vec3 minionSpot = this.lich.findVecInLOSOf(target);
		if (minionSpot != null && this.lich.level() instanceof ServerLevelAccessor accessor) {
			LichMinion minion = new LichMinion(this.lich.level(), this.lich);
			minion.setPos(minionSpot.x(), minionSpot.y(), minionSpot.z());
			minion.finalizeSpawn(accessor, this.lich.level().getCurrentDifficultyAt(BlockPos.containing(minionSpot)), MobSpawnType.MOB_SUMMONED, null);
			this.lich.level().addFreshEntity(minion);
			minion.setTarget(target);
			minion.spawnAnim();
			minion.playSound(TFSounds.MINION_SUMMON, 1.0F, ((this.lich.getRandom().nextFloat() - this.lich.getRandom().nextFloat()) * 0.7F + 1.0F) * 0.75F);
			this.lich.swing(InteractionHand.MAIN_HAND);
			this.lich.makeMagicTrail(this.lich.getEyePosition(), minion.getEyePosition(), this.lich.getRandom().nextFloat() * 0.0625F + 0.125F, this.lich.getRandom().nextFloat() * 0.0625F + 0.125F, this.lich.getRandom().nextFloat() * 0.0625F + 0.125F);
		}
	}
}
