package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.BeaconColorMultiplierBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.ColorUtil;

public class AuroralizedGlassBlock extends AbstractGlassBlock implements BeaconColorMultiplierBlock {

	public AuroralizedGlassBlock(Properties props) {
		super(props);
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		int color = ColorUtil.hsvToRGB(AuroraBrickBlock.rippleFractialNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
		int red = (color & 16711680) >> 16;
		int green = (color & '\uff00') >> 8;
		int blue = (color & 255);
		return new float[]{red / 255.0F, green / 255.0F, blue / 255.0F};
	}
}
