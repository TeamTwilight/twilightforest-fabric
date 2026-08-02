package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.blocks.extensions.BeaconColorMultiplierBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

public class AuroralizedGlassBlock extends TransparentBlock implements BeaconColorMultiplierBlock {

	public AuroralizedGlassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos, int original) {
		return ColorUtil.hsvToRGB(SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}
}
