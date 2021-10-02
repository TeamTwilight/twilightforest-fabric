package twilightforest.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.block.TFBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TrophyBlockEntity extends BlockEntity {

	private int animatedTicks;
	private boolean animated;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TROPHY, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TrophyBlockEntity te) {
		if (state.is(TFBlocks.ur_ghast_trophy) || state.is(TFBlocks.ur_ghast_wall_trophy)) {
			te.animated = true;
			++te.animatedTicks;
		}
	}

	@Environment(EnvType.CLIENT)
	public float getAnimationProgress(float time) {
		return this.animated ? this.animatedTicks + time : (float) this.animatedTicks;
	}
}
