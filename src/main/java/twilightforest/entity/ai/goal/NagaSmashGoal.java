package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.boss.Naga;
import twilightforest.util.EntityUtil;

public class NagaSmashGoal extends Goal {

	private final Naga naga;

	public NagaSmashGoal(Naga naga) {
		this.naga = naga;
	}

	@Override
	public boolean canUse() {
		return this.naga.horizontalCollision && ForgeEventFactory.getMobGriefingEvent(this.naga.getLevel(), this.naga);
	}

	@Override
	public void start() {
		// NAGA SMASH!
		if (this.naga.getLevel().isClientSide()) return;

		AABB bb = this.naga.getBoundingBox();

		int minx = Mth.floor(bb.minX - 0.75D);
		int miny = Mth.floor(bb.minY + 1.01D);
		int minz = Mth.floor(bb.minZ - 0.75D);
		int maxx = Mth.floor(bb.maxX + 0.75D);
		int maxy = Mth.floor(bb.maxY + 0.0D);
		int maxz = Mth.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (this.naga.level.hasChunksAt(min, max)) {
			for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
				if (EntityUtil.canDestroyBlock(this.naga.getLevel(), pos, this.naga)) {
					this.naga.getLevel().destroyBlock(pos, true);
				}
			}
		}
	}
}