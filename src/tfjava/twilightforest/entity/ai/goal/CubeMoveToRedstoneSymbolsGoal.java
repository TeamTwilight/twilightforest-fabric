package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.RovingCube;

import java.util.EnumSet;

public class CubeMoveToRedstoneSymbolsGoal extends Goal {

	private final RovingCube cube;
	private final double speed;
	private BlockPos targetPos;

	@SuppressWarnings("this-escape")
	public CubeMoveToRedstoneSymbolsGoal(RovingCube cube, double speed) {
		this.cube = cube;
		this.speed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.cube.getRandom().nextInt(20) != 0) {
			return false;
		}
		BlockPos pos = this.searchForRedstoneSymbol(this.cube);
		if (pos == null) {
			return false;
		}
		this.targetPos = pos;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.cube.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.cube.getNavigation().moveTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), this.speed);
	}

	@Nullable
	private BlockPos searchForRedstoneSymbol(RovingCube cube) {
		BlockPos current = cube.blockPosition();
		for (int x = -16; x < 16; x++) {
			for (int z = -16; z < 16; z++) {
				for (int y = -5; y < 5; y++) {
					BlockPos candidate = current.offset(x, y, z);
					if (this.isRedstoneSymbol(candidate)) {
						this.cube.hasFoundSymbol = true;
						this.cube.symbolX = candidate.getX();
						this.cube.symbolY = candidate.getY();
						this.cube.symbolZ = candidate.getZ();
						return candidate;
					}
				}
			}
		}
		return null;
	}

	private boolean isRedstoneSymbol(BlockPos pos) {
		if (!this.cube.level().hasChunkAt(pos) || !this.cube.level().isEmptyBlock(pos)) {
			return false;
		}
		for (Direction direction : Direction.values()) {
			if (this.cube.level().getBlockState(pos.relative(direction)).getBlock() != Blocks.REDSTONE_WIRE) {
				return false;
			}
		}
		return true;
	}
}
