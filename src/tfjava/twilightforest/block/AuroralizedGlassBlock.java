package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

/**
 * 1:1 port of upstream {@code twilightforest.block.AuroralizedGlassBlock}.
 *
 * <p>Beacon beam tints by ripple-fractal-noise sampled at the block's column — every Y
 * level produces a different aurora colour. Codex Fabric port note: NF-only
 * {@code Block#getBeaconColorMultiplier} callback (vanilla 1.21.1 has no equivalent
 * hook on Block — beacon beam colour comes from the static {@code Beacon} class). Kept
 * body 1:1 without {@code @Override} so a future Fabric mixin or beacon-color registry
 * (e.g. via Fabric's beacon-color API) can re-attach it.</p>
 */
public class AuroralizedGlassBlock extends TransparentBlock {

	public AuroralizedGlassBlock(Properties properties) {
		super(properties);
	}

	// Codex Fabric port note: NF-only hook; kept body 1:1 for future mixin/registry hookup.
	public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		return ColorUtil.hsvToRGB(SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}
}
