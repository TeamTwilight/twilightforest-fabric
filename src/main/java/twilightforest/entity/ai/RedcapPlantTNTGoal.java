package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.BlockPos;
import twilightforest.entity.RedcapEntity;

public class RedcapPlantTNTGoal extends RedcapBaseGoal {

	public RedcapPlantTNTGoal(RedcapEntity entityTFRedcap) {
		super(entityTFRedcap);
	}

	@Override
	public boolean canUse() {
		LivingEntity attackTarget = this.redcap.getTarget();
		return attackTarget != null
				&& !redcap.heldTNT.isEmpty()
				&& redcap.distanceToSqr(attackTarget) < 25
				&& !isTargetLookingAtMe(attackTarget)
				&& redcap.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
				&& !isLitTNTNearby(8)
				&& findBlockTNTNearby(5) == null;
	}

	@Override
	public void start() {
		BlockPos entityPos = new BlockPos(redcap.blockPosition());

		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, redcap.heldTNT);

		if (this.redcap.level.isEmptyBlock(entityPos)) {
			redcap.heldTNT.shrink(1);
			redcap.playAmbientSound();
			redcap.level.setBlockAndUpdate(entityPos, Blocks.TNT.defaultBlockState());
		}
	}

	@Override
	public void stop() {
		this.redcap.setItemSlot(EquipmentSlot.MAINHAND, redcap.heldPick);
	}
}
