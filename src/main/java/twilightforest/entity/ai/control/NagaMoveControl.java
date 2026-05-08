package twilightforest.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.boss.Naga;

public class NagaMoveControl extends MoveControl {
    public NagaMoveControl(Naga naga) {
        super(naga);
    }

    @Override
    public void tick() {
        Naga naga = (Naga) this.mob;
        NagaMovementPattern movement = naga.getMovementAI();
        if (movement != null) {
            NagaMovementPattern.MovementState state = movement.getState();
            if (state == NagaMovementPattern.MovementState.DAZE) {
                this.mob.xxa = 0.0F;
            } else if (state != NagaMovementPattern.MovementState.CHARGE && state != NagaMovementPattern.MovementState.INTIMIDATE) {
                this.mob.xxa = Mth.cos(this.mob.tickCount * 0.3F) * 0.6F;
            } else {
                this.mob.xxa *= 0.8F;
            }
        }
        super.tick();
    }
}