package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;

public interface EnforcedHomePoint {

	default void addRestrictionGoals(PathfinderMob entity, GoalSelector selector) {
		selector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.25D));
	}

	default void saveHomePointToNbt(CompoundTag tag) {
		if (this.getRestrictionCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictionCenter();
			tag.put("Home", this.makeDoubleList(home.getX(), home.getY(), home.getZ()));
		}
	}

	default void loadHomePointFromNbt(CompoundTag tag, int radius) {
		if (tag.contains("Home", 9)) {
			ListTag nbttaglist = tag.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.setRestriction(new BlockPos(hx, hy, hz), radius);
		}
	}

	default ListTag makeDoubleList(double... pNumbers) {
		ListTag listtag = new ListTag();

		for(double d0 : pNumbers) {
			listtag.add(DoubleTag.valueOf(d0));
		}

		return listtag;
	}

	BlockPos getRestrictionCenter();

	void setRestriction(BlockPos pos, int dist);
}
