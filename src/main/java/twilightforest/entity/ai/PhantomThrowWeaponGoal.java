package twilightforest.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.KnightPhantomEntity;
import twilightforest.entity.boss.ThrownWepEntity;
import twilightforest.item.TFItems;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class PhantomThrowWeaponGoal extends Goal {

	private final KnightPhantomEntity boss;

	public PhantomThrowWeaponGoal(KnightPhantomEntity entity) {
		boss = entity;
		setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return boss.getTarget() != null && boss.getCurrentFormation() == KnightPhantomEntity.Formation.ATTACK_PLAYER_ATTACK;
	}

	@Override
	public void tick() {
		if (boss.getTarget() != null && boss.getTicksProgress() % 4 == 0) {
			if (boss.isAxeKnight())
				launchAxeAt(boss.getTarget());
			else if (boss.isPickKnight())
				launchPicks();
		}
	}

	private void launchAxeAt(Entity targetedEntity) {
		float bodyFacingAngle = ((boss.yBodyRot * 3.141593F) / 180F);
		double sx = boss.getX() + (Mth.cos(bodyFacingAngle) * 1);
		double sy = boss.getY() + (boss.getBbHeight() * 0.82);
		double sz = boss.getZ() + (Mth.sin(bodyFacingAngle) * 1);

		double tx = targetedEntity.getX() - sx;
		double ty = (targetedEntity.getBoundingBox().minY + targetedEntity.getBbHeight() / 2.0F) - (boss.getY() + boss.getBbHeight() / 2.0F);
		double tz = targetedEntity.getZ() - sz;

		boss.playSound(TFSounds.PHANTOM_THROW_AXE, 1.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		ThrownWepEntity projectile = new ThrownWepEntity(TFEntities.thrown_wep, boss.level, boss).setItem(new ItemStack(TFItems.knightmetal_axe));

		float speed = 0.75F;

		projectile.shoot(tx, ty, tz, speed, 1.0F);

		projectile.moveTo(sx, sy, sz, boss.yRot, boss.xRot);

		boss.level.addFreshEntity(projectile);
	}

	private void launchPicks() {
		boss.playSound(TFSounds.PHANTOM_THROW_PICK, 1.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.2F + 0.4F);

		for (int i = 0; i < 8; i++) {
			float throwAngle = i * 3.14159165F / 4F;

			double sx = boss.getX() + (Mth.cos(throwAngle) * 1);
			double sy = boss.getY() + (boss.getBbHeight() * 0.82);
			double sz = boss.getZ() + (Mth.sin(throwAngle) * 1);

			double vx = Mth.cos(throwAngle);
			double vy = 0;
			double vz = Mth.sin(throwAngle);


			ThrownWepEntity projectile = new ThrownWepEntity(TFEntities.thrown_wep, boss.level, boss).setDamage(3).setVelocity(0.015F).setItem(new ItemStack(TFItems.knightmetal_pickaxe));


			projectile.moveTo(sx, sy, sz, i * 45F, boss.xRot);

			float speed = 0.5F;

			projectile.shoot(vx, vy, vz, speed, 1.0F);

			boss.level.addFreshEntity(projectile);
		}
	}
}
