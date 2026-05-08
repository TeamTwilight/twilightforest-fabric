package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.passive.QuestRam;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class QuestRamEatWoolGoal extends Goal {
    private final QuestRam ram;
    private final PathNavigation navigation;
    @Nullable
    private ItemEntity targetItem;

    public QuestRamEatWoolGoal(QuestRam ram) {
        this.ram = ram;
        this.navigation = ram.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<ItemEntity> items = this.ram.level().getEntitiesOfClass(ItemEntity.class, this.ram.getBoundingBox().inflate(16.0D), item ->
                (item.onGround() || item.isInWater()) && item.isAlive()
                        && !item.getItem().isEmpty() && this.ram.hasLineOfSight(item) && this.ram.isItemTempting(item.getItem()));
        items.sort(Comparator.comparingDouble(this.ram::distanceToSqr));

        if (!items.isEmpty()) {
            this.targetItem = items.getFirst();
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.ram.isAlive() && !this.navigation.isStuck() && !this.navigation.isDone()
                && this.targetItem != null && this.targetItem.isAlive() && this.ram.isItemTempting(this.targetItem.getItem());
    }

    @Override
    public void start() {
        if (this.targetItem != null) {
            this.navigation.stop();
            this.ram.getLookControl().setLookAt(this.targetItem, this.ram.getMaxHeadYRot() + 20, this.ram.getMaxHeadXRot());
            this.navigation.moveTo(this.targetItem, 1.0D);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.ram.level().isClientSide() && this.targetItem != null && this.ram.isItemTempting(this.targetItem.getItem())) {
            this.ram.getLookControl().setLookAt(this.targetItem, this.ram.getMaxHeadYRot() + 20, this.ram.getMaxHeadXRot());
            if (this.ram.distanceToSqr(this.targetItem.position()) < 6.25D && this.ram.tryAccept(this.targetItem.getItem())) {
                this.targetItem.discard();
                this.ram.gameEvent(GameEvent.EAT);
            }
        }
    }
}
