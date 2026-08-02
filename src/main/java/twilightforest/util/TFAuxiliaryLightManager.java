package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fabric 兼容的 AuxiliaryLightManager 替代。
 * 在 NeoForge 中用于修改方块的辅助光照，Fabric 中暂不实现。
 */
public class TFAuxiliaryLightManager {

	public static int getLightAt(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getLightEmission();
	}
}