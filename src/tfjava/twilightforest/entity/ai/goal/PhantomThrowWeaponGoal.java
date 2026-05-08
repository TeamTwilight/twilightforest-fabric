package twilightforest.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.entity.projectile.ThrownWep;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class PhantomThrowWeaponGoal extends Goal {

	private final KnightPhantom boss;

	@SuppressWarnings("this-escape")
	public PhantomThrowWeaponGoal(KnightPhantom entity) {
		this.boss = entity;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null && this.boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_ATTACK;
	}

	@Override
	public void tick() {
		if (this.boss.getTarget() != null && this.boss.getTicksProgress() % 4 == 0) {
			if (this.boss.isAxeKnight()) {
				this.launchAxeAt(this.boss.getTarget());
			} else if (this.boss.isPickKnight()) {
				this.launchPicks();
			}
		}
	}

	private void launchAxeAt(Entity target) {
		float angle = (this.boss.yBodyRot * Mth.PI) / 180.0F;
		double sx = this.boss.getX() + Mth.cos(angle);
		double sy = this.boss.getY() + this.boss.getBbHeight() * 0.82D;
		double sz = this.boss.getZ() + Mth.sin(angle);
		double tx = target.getX() - sx;
		double ty = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (this.boss.getY() + this.boss.getBbHeight() / 2.0F);
		double tz = target.getZ() - sz;

		this.boss.playSound(TFSounds.KNIGHT_PHANTOM_THROW_AXE, 1.0F, (this.boss.getRandom().nextFloat() - this.boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		this.boss.gameEvent(GameEvent.PROJECTILE_SHOOT);
		ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP.get(), this.boss.level(), this.boss).setItem(new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));
		projectile.shoot(tx, ty, tz, 0.75F, 1.0F);
		projectile.moveTo(sx, sy, sz, this.boss.getYRot(), this.boss.getXRot());
		this.boss.level().addFreshEntity(projectile);
	}

	private void launchPicks() {
		this.boss.playSound(TFSounds.KNIGHT_PHANTOM_THROW_PICK, 1.0F, (this.boss.getRandom().nextFloat() - this.boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		this.boss.gameEvent(GameEvent.PROJECTILE_SHOOT);
		for (int i = 0; i < 8; i++) {
			float angle = i * Mth.PI / 4.0F;
			double sx = this.boss.getX() + Mth.cos(angle);
			double sy = this.boss.getY() + this.boss.getBbHeight() * 0.82D;
			double sz = this.boss.getZ() + Mth.sin(angle);
			double vx = Mth.cos(angle);
			double vz = Mth.sin(angle);

			ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP.get(), this.boss.level(), this.boss)
					.setDamage(3.0F)
					.setVelocity(0.015F)
					.setItem(new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));
			projectile.moveTo(sx, sy, sz, i * 45.0F, this.boss.getXRot());
			projectile.shoot(vx, 0.0D, vz, 0.5F, 1.0F);
			this.boss.level().addFreshEntity(projectile);
		}
	}
}
