package twilightforest.entity.ai.goal;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.init.TFItems;

import java.util.List;

public class PhantomUpdateFormationAndMoveGoal extends Goal {

	private static final float CIRCLE_SMALL_RADIUS = 2.5F;
	private static final float CIRCLE_LARGE_RADIUS = 8.5F;

	private final KnightPhantom boss;

	public PhantomUpdateFormationAndMoveGoal(KnightPhantom entity) {
		this.boss = entity;
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void tick() {
		this.boss.noPhysics = this.boss.getTicksProgress() % 20 != 0;
		this.boss.setTicksProgress(this.boss.getTicksProgress() + 1);
		if (this.boss.getTicksProgress() >= this.boss.getMaxTicksForFormation()) {
			this.switchToNextFormation();
		}
		Vec3 destination = this.getDestination();
		this.boss.getMoveControl().setWantedPosition(destination.x(), destination.y(), destination.z(), this.boss.isChargingAtPlayer() ? 2.0D : 1.0D);
	}

	public Vec3 getDestination() {
		if (this.boss.getRestrictionPoint() == null) {
			this.boss.setRestrictionPoint(GlobalPos.of(this.boss.level().dimension(), this.boss.blockPosition()));
		}
		return switch (this.boss.getCurrentFormation()) {
			case LARGE_CLOCKWISE -> this.getCirclePosition(CIRCLE_LARGE_RADIUS, true);
			case SMALL_CLOCKWISE -> this.getCirclePosition(CIRCLE_SMALL_RADIUS, true);
			case LARGE_ANTICLOCKWISE -> this.getCirclePosition(CIRCLE_LARGE_RADIUS, false);
			case SMALL_ANTICLOCKWISE -> this.getCirclePosition(CIRCLE_SMALL_RADIUS, false);
			case CHARGE_PLUSX -> this.getMoveAcrossPosition(true, true);
			case CHARGE_MINUSX -> this.getMoveAcrossPosition(false, true);
			case CHARGE_PLUSZ -> this.getMoveAcrossPosition(true, false);
			case CHARGE_MINUSZ -> this.getMoveAcrossPosition(false, false);
			case ATTACK_PLAYER_START, HOVER -> this.getHoverPosition(CIRCLE_LARGE_RADIUS);
			case ATTACK_PLAYER_ATTACK -> this.getAttackPlayerPosition();
			default -> this.getLoiterPosition();
		};
	}

	private void switchToNextFormation() {
		List<KnightPhantom> nearbyKnights = this.boss.getNearbyKnights();
		if (this.boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_START) {
			this.boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_ATTACK);
		} else if (this.boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_ATTACK) {
			if (nearbyKnights.size() > 1) {
				this.boss.switchToFormation(KnightPhantom.Formation.WAITING_FOR_LEADER);
			} else {
				switch (this.boss.getRandom().nextInt(3)) {
					case 0 -> this.boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get()));
					case 1 -> this.boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));
					case 2 -> this.boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));
				}
				this.boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
			}
		} else if (this.boss.getCurrentFormation() == KnightPhantom.Formation.WAITING_FOR_LEADER) {
			if (nearbyKnights.size() > 1) {
				this.boss.switchToFormation(nearbyKnights.get(1).getCurrentFormation());
				this.boss.setTicksProgress(nearbyKnights.get(1).getTicksProgress());
			} else {
				this.boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
			}
		} else if (this.isThisTheLeader(nearbyKnights)) {
			this.pickRandomFormation();
			this.broadcastMyFormation(nearbyKnights);
			if (this.isNobodyCharging(nearbyKnights)) {
				this.makeARandomKnightCharge(nearbyKnights);
			}
		}
	}

	private boolean isThisTheLeader(List<KnightPhantom> nearbyKnights) {
		for (KnightPhantom knight : nearbyKnights) {
			if (knight.getNumber() < this.boss.getNumber()) {
				return false;
			}
		}
		return true;
	}

	private void pickRandomFormation() {
		switch (this.boss.getRandom().nextInt(8)) {
			case 0, 7 -> this.boss.switchToFormation(KnightPhantom.Formation.SMALL_CLOCKWISE);
			case 1, 2 -> this.boss.switchToFormation(KnightPhantom.Formation.SMALL_ANTICLOCKWISE);
			case 3 -> this.boss.switchToFormation(KnightPhantom.Formation.CHARGE_PLUSX);
			case 4 -> this.boss.switchToFormation(KnightPhantom.Formation.CHARGE_MINUSX);
			case 5 -> this.boss.switchToFormation(KnightPhantom.Formation.CHARGE_PLUSZ);
			case 6 -> this.boss.switchToFormation(KnightPhantom.Formation.CHARGE_MINUSZ);
		}
	}

	private void makeARandomKnightCharge(List<KnightPhantom> nearbyKnights) {
		nearbyKnights.get(this.boss.getRandom().nextInt(nearbyKnights.size())).switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
	}

	private void broadcastMyFormation(List<KnightPhantom> nearbyKnights) {
		for (KnightPhantom knight : nearbyKnights) {
			if (!knight.isChargingAtPlayer()) {
				knight.switchToFormation(this.boss.getCurrentFormation());
			}
		}
	}

	private boolean isNobodyCharging(List<KnightPhantom> nearbyKnights) {
		for (KnightPhantom knight : nearbyKnights) {
			if (knight.isChargingAtPlayer()) {
				return false;
			}
		}
		return true;
	}

	private Vec3 getMoveAcrossPosition(boolean plus, boolean alongX) {
		float offset0 = this.boss.getNumber() * 3.0F - 7.5F;
		float offset1 = this.boss.getTicksProgress() < 60 ? -7.0F : -7.0F + ((this.boss.getTicksProgress() - 60) / 120.0F) * 14.0F;
		if (!plus) {
			offset1 *= -1.0F;
		}
		double dx = this.boss.getRestrictionPoint().pos().getX() + (alongX ? offset0 : offset1);
		double dy = this.boss.getRestrictionPoint().pos().getY() + Math.cos(this.boss.getTicksProgress() / 7.0F + this.boss.getNumber());
		double dz = this.boss.getRestrictionPoint().pos().getZ() + (alongX ? offset1 : offset0);
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getCirclePosition(float distance, boolean clockwise) {
		float angle = this.boss.getTicksProgress() * 2.0F;
		if (!clockwise) {
			angle *= -1.0F;
		}
		angle += 60.0F * this.boss.getNumber();
		double dx = this.boss.getRestrictionPoint().pos().getX() + Math.cos(angle * Math.PI / 180.0D) * distance;
		double dy = this.boss.getRestrictionPoint().pos().getY() + Math.cos(this.boss.getTicksProgress() / 7.0F + this.boss.getNumber());
		double dz = this.boss.getRestrictionPoint().pos().getZ() + Math.sin(angle * Math.PI / 180.0D) * distance;
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getHoverPosition(float distance) {
		double dx = this.boss.xOld;
		double dy = this.boss.getRestrictionPoint().pos().getY() + Math.cos(this.boss.getTicksProgress() / 7.0F + this.boss.getNumber());
		double dz = this.boss.zOld;
		double ox = this.boss.getRestrictionPoint().pos().getX() - dx;
		double oz = this.boss.getRestrictionPoint().pos().getZ() - dz;
		double distance2d = Math.sqrt(ox * ox + oz * oz);
		if (distance2d > distance) {
			dx = this.boss.getRestrictionPoint().pos().getX() + ox / distance2d * distance;
			dz = this.boss.getRestrictionPoint().pos().getZ() + oz / distance2d * distance;
		}
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getLoiterPosition() {
		double dx = this.boss.getRestrictionPoint().pos().getX();
		double dy = this.boss.getRestrictionPoint().pos().getY() + Math.cos(this.boss.getTicksProgress() / 7.0F + this.boss.getNumber());
		double dz = this.boss.getRestrictionPoint().pos().getZ();
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getAttackPlayerPosition() {
		return this.boss.isSwordKnight() ? Vec3.atLowerCornerOf(this.boss.getChargePos()) : this.getHoverPosition(CIRCLE_LARGE_RADIUS);
	}
}
