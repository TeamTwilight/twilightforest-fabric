package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.monster.LichMinion;
import twilightforest.init.TFSounds;
import twilightforest.item.LifedrainScepterItem;
import twilightforest.util.entities.EntityUtil;

import java.util.EnumSet;
import java.util.List;

public class LichAbsorbMinionsGoal extends Goal {

	private final Lich lich;

	@SuppressWarnings("this-escape")
	public LichAbsorbMinionsGoal(Lich lich) {
		this.lich = lich;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.lich.getHealth() < this.lich.getMaxHealth() / 2.0F && this.lich.countMyMinions() > 0;
	}

	@Override
	public void start() {
		super.start();
		this.lich.setScepterTime();
	}

	@Override
	public void stop() {
		super.stop();
		this.lich.resetScepterTime();
	}

	@Override
	public void tick() {
		if (this.lich.getTeleportInvisibility() > 0) {
			return;
		}
		super.tick();
		if (this.lich.getScepterTimeLeft() > 0 || this.lich.level().isClientSide()) {
			return;
		}

		List<LichMinion> minions = this.lich.level().getEntitiesOfClass(LichMinion.class, this.lich.getBoundingBox().inflate(32.0D, 16.0D, 32.0D))
				.stream().filter(minion -> minion.getMaster() == this.lich).toList();

		if (!minions.isEmpty() && this.lich.level() instanceof ServerLevel serverLevel) {
			LichMinion minion = minions.get(0);
			float healAmount = minion.getHealth();
			minion.discard();
			LifedrainScepterItem.animateTargetShatter(serverLevel, minion);

			SoundEvent deathSound = EntityUtil.getDeathSound(minion);
			if (deathSound != null) {
				this.lich.level().playSound(null, minion.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, minion.getVoicePitch());
			}
			this.lich.playSound(TFSounds.LICH_POP_MOB, 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
			minion.playSound(TFSounds.LICH_POP_MOB, 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
			this.lich.makeMagicTrail(minion.getEyePosition(), this.lich.getEyePosition(), 1.0F, 0.5F, 0.5F);
			this.lich.heal(healAmount);
			this.lich.swing(InteractionHand.MAIN_HAND);
			this.lich.setPopCooldown(40);
			this.lich.gameEvent(GameEvent.ENTITY_DIE);
		}
	}
}
