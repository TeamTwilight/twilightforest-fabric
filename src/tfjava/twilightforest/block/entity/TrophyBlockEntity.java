package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

/**
 * 1:1 port of upstream {@code twilightforest.block.entity.TrophyBlockEntity}.
 */
public class TrophyBlockEntity extends BlockEntity {

	private int animatedTicks;
	private boolean animated;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TROPHY, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TrophyBlockEntity te) {
		if (state.is(TFBlocks.UR_GHAST_TROPHY.get()) || state.is(TFBlocks.UR_GHAST_WALL_TROPHY.get())) {
			te.animated = true;
			++te.animatedTicks;
		}
	}

	public float getAnimationProgress(float time) {
		return this.animated ? this.animatedTicks + time : (float) this.animatedTicks;
	}
}
